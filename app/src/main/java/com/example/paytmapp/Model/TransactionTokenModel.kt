package com.example.paytmapp.Model

data class TransactionTokenModel(
    val checksum: String,
    val message: String,
    val order_id: String,
    val signature: String,
    val txn_token: String
)