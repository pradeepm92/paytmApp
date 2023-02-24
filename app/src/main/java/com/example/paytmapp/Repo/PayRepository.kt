package com.example.paytmapp.Repo

import com.example.paytmapp.Api.APIClient
import com.example.paytmapp.Model.TransactionTokenModel
import com.example.paytmapp.Api.OperationCallback
import com.example.paytmapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

object PayRepository {
    fun getToken(
        baseURL: String,
        auth_token: String,
        order_id:String, txn_amount:String,
        dev_prod:String,
        callback: OperationCallback<TransactionTokenModel>,
    ) {
        APIClient(baseURL!!).instance.paytmToken(auth_token,order_id,txn_amount,dev_prod)
            .enqueue(object : Callback<TransactionTokenModel>
            {
                override fun onResponse(
                    call: Call<TransactionTokenModel>,
                    response: Response<TransactionTokenModel>
                ) {
                    when (response.code()) {
                        200 -> callback.onSuccess(response.body()!!)
                        422 -> callback.onError(R.string.invalid_login)
                        401 -> callback.onError(R.string.invalid_login)
                        else -> callback.onError(R.string.unknown_error)
                    }
                }

                override fun onFailure(call: Call<TransactionTokenModel>, t: Throwable) {
                    if (t is SocketTimeoutException) {
                        callback.onError(R.string.timed_out)
                    } else {
                        callback.onError(R.string.internet_connection)
                    }
                }
            }
            )

    }
}