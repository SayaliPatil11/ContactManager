package com.mangoapps.contactmanager.common

/* File to declare all constants in app*/
class Constants {
    companion object {
        val REQUIRED_PERMISSIONS =
            arrayOf("android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.CALL_PHONE","android.permission.SEND_SMS","android.permission.READ_SMS","android.permission.READ_CALL_LOG","android.permission.WRITE_CALL_LOG")
        val PERMISSION_REQUEST_CODE = 200
        val TAB_NUMBER = 3
        val DATE_PATTERN_WITH_YEAR_FIRST = "yyyy-MM-dd"
        val DATE_PATTERN_WITH_TIME = "yyyy-MM-dd hh:mm"

    }
}