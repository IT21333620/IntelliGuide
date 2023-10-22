package com.example.intelliguide.models

data class Payment(
    var cardNumber: String? = null,
    var expiryDate: String? = null,
    var cvv: String? = null
)
