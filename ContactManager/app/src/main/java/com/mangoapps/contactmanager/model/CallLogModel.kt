package com.mangoapps.contactmanager.model


data class CallLogModel(
    val name: String? = "",
    val number: String,
    val duration: String,
    val date: String,
    val callType: Int
) {
}