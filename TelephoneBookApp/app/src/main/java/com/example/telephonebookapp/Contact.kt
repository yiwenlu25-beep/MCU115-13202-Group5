package com.example.telephonebookapp

data class Contact(
    val id: String = java.util.UUID.randomUUID().toString(),
    var name: String,
    var phone: String,
    var email: String
)