package com.example.paytmapp.Viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.paytmapp.Model.TransactionTokenModel
import com.example.paytmapp.Api.OperationCallback
import com.example.paytmapp.Repo.PayRepository
import com.example.paytmapp.Utlis
import okhttp3.ResponseBody

class PaymentViewmodel(application: Application): AndroidViewModel(application) {
    val context = application

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<Int>()
    val onMessageError: LiveData<Int> = _onMessageError
    private val _onMessageErrorMsg =MutableLiveData<ResponseBody>()
    val onMessageErrormsg:LiveData<ResponseBody> = _onMessageErrorMsg
    private val _ontokenresponse = MutableLiveData<TransactionTokenModel>()
    val ontokenresponse:LiveData<TransactionTokenModel> = _ontokenresponse

    fun getTokeen(order_id:String,txn_amount:String,dev_prod:String) {
        _isViewLoading.postValue(true)
        val baseURL =Utlis.getBaseUrl(context)
        val auth_token = Utlis.getAuthToken(context)
        PayRepository.getToken(
            baseURL!!,
            auth_token,
            order_id,txn_amount,dev_prod,
            object : OperationCallback<TransactionTokenModel>
            {
                override fun onSuccess(data: TransactionTokenModel) {
                    _isViewLoading.postValue(false)
                    _ontokenresponse.postValue(data)
                }

                override fun onError(error: Int) {
                    _isViewLoading.postValue(false)
                    _onMessageError.postValue(error)
                }

                override fun onErrorMsg(data: ResponseBody?) {
                    _isViewLoading.postValue(false)
                    _onMessageErrorMsg.postValue(data!!)
                }
            }
        )

    }

}