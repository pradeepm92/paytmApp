package com.example.paytmapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import com.example.paytmapp.Api.ErrorParser
import com.example.paytmapp.Model.TransactionTokenModel
import com.example.paytmapp.Viewmodel.PaymentViewmodel
import com.paytm.pgsdk.Constants.MID
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import com.paytm.pgsdk.TransactionManager
import okhttp3.ResponseBody

class PaymentActivity : AppCompatActivity() {

    val requestCode = 2
    var ORDER_ID: String? = null
    var loaderManager: LoaderManager? = null
    var bodyData = ""
    var developmode = "test"
    lateinit var orderID: TextView
    lateinit var pay: Button
    lateinit var amount: EditText
    lateinit var Amount: String
    var value = 0f
    var production=true
    var checksum=""
    var midString: String? = null
    var txnAmountString: String? = null
    lateinit var paymentViewModel: PaymentViewmodel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewModel()
        ORDER_ID = "ID" + System.currentTimeMillis()+getRandomString(3)
        orderID = findViewById(R.id.orderid)
        amount = findViewById(R.id.amount)
        pay = findViewById(R.id.pay)
        orderID.text = ORDER_ID

        Log.e("amount",  amount.text.toString() )
        if(production){
            developmode="production"
        }
        pay.setOnClickListener {
            Amount = amount.text.toString()
            if (Amount.isEmpty()){
                Toast .makeText(this@PaymentActivity,"please enter the amount"
                    ,Toast.LENGTH_SHORT).show()
            }else{
            Log.e("amount1",  amount.text.toString() )
            paymentViewModel.getTokeen(ORDER_ID!!,Amount,developmode)}
        }
    }

    private fun setViewModel() {
        paymentViewModel=ViewModelProvider(this)[PaymentViewmodel::class.java]
        paymentViewModel.onMessageError.observe(this, errorObserver)
        paymentViewModel.onMessageErrormsg.observe(this, errormsgObserver)
        paymentViewModel.ontokenresponse.observe(this, tokenObserver)

    }
    private val tokenObserver = Observer<TransactionTokenModel>{
        if(it!=null) {
            if(it.message.equals("Success",true)){
                checksum=it.checksum
                paymentProcessEvent(it.txn_token)
            }else{
                Toast .makeText(this@PaymentActivity,it.message
                    ,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun paymentProcessEvent(token: String) {
        midString = Constants.MID_TEST
        txnAmountString=Amount
//        testhost
        var host = "https://securegw-stage.paytm.in/"

//     forproduction  ....
        if (production) {
            host = "https://securegw.paytm.in/"
            midString=Constants.MID
            developmode="production"
        }
        var errors = ""
        if (midString.equals("", ignoreCase = true)) {
            errors += "Enter valid MID here\n"
        }
        if (ORDER_ID.equals("", ignoreCase = true)) {
            errors += "Enter valid Order ID here\n"
        }
        if (token.equals("", ignoreCase = true)) {
            errors += "Enter valid Txn Token here\n"
        }
        if (txnAmountString.equals("", ignoreCase = true)) {
            errors += "Enter valid Amount here\n"
        }

        if (errors.equals("",ignoreCase = true)){
            val orderDetails =
                "MID: $midString, OrderId: $ORDER_ID, TxnToken: $token, Amount: $txnAmountString"

            val callBackUrl = host + "theia/paytmCallback?ORDER_ID=" + ORDER_ID
            val paytmOrder = PaytmOrder(ORDER_ID, midString, token, txnAmountString, callBackUrl)
            val transactionManager=TransactionManager(paytmOrder,object :
                PaytmPaymentTransactionCallback{
                override fun onTransactionResponse(p0: Bundle?) {
                    Toast .makeText(this@PaymentActivity,"status success"
                        ,Toast.LENGTH_SHORT).show()
//                    here to add the update api for getting status
                }

                override fun networkNotAvailable() {
                    Toast .makeText(this@PaymentActivity,"no internet connection"
                        ,Toast.LENGTH_SHORT).show()
                }

                override fun onErrorProceed(p0: String?) {
                    TODO("Not yet implemented")
                }

                override fun clientAuthenticationFailed(p0: String?) {
                    TODO("Not yet implemented")
                }

                override fun someUIErrorOccurred(p0: String?) {
                    TODO("Not yet implemented")
                }

                override fun onErrorLoadingWebPage(p0: Int, p1: String?, p2: String?) {
                    TODO("Not yet implemented")
                }

                override fun onBackPressedCancelTransaction() {
                    TODO("Not yet implemented")
                }

                override fun onTransactionCancel(p0: String?, p1: Bundle?) {
                    TODO("Not yet implemented")
                }

            })
            transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage")
            transactionManager.startTransaction(this,requestCode)
        }

    }

    private val errorObserver= Observer<Int> {
        Toast .makeText(this@PaymentActivity,resources.getString(it)
            ,Toast.LENGTH_SHORT).show()
    }
    private val errormsgObserver= Observer<ResponseBody> {
        val error= ErrorParser(it).message
        Toast.makeText(this@PaymentActivity,error,Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        dialog("Want to cancel payment?");

    }
    private fun dialog(alertMessage: String) {
        val dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_confirmation)
        val title = dialog.findViewById<TextView>(R.id.title)
        val message = dialog.findViewById<TextView>(R.id.message)
        title.text = "Exit"
        message.text = alertMessage
        val cancelbtn = dialog.findViewById<Button>(R.id.cancelbtn)
        val okbtn = dialog.findViewById<Button>(R.id.okbtn)
        okbtn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        cancelbtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }




    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCode && data != null) {
            val nsdk = data.getStringExtra("nativeSdkForMerchantMessage")
            val response = data.getStringExtra("response")
            Toast.makeText(this, nsdk + response, Toast.LENGTH_SHORT).show()
        }
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}