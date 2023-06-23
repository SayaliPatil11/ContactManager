package com.mangoapps.contactmanager.common

import java.text.SimpleDateFormat

class BackendHelper {
    companion object{
        /* Method to convert timestamp to date*/
        fun convertTimeStampToDate(timestamp: Long?, pattern:String): String {
            return SimpleDateFormat(pattern).format(timestamp)
        }
    }
}