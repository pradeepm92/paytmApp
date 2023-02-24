package com.example.paytmapp.Api

import com.example.paytmapp.Model.TransactionTokenModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("calcibill/api/client/paytm/transaction_token_new/create")
    fun paytmToken(
        @Header("Authorization") token: String?,
        @Field("order_id")order_id:String,
        @Field("txn_amount")txn_amount:String,
        @Field("dev_prod")dev_prod:String,
    ): retrofit2.Call<TransactionTokenModel>


}