package com.mangoapps.contactmanager.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mangoapps.contactmanager.R
import java.text.SimpleDateFormat

/*Class for supportive methods required on UI*/
class UiHelper {
    companion object {
        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        fun isPermissionGranted(permission: String, context: Context): Boolean {
            val result = ContextCompat.checkSelfPermission(
                context,
                permission
            )
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun isAllPermissionGranted(context: Context): Boolean {
            var isAllPermissionGranted = false
            val predicate: (String) -> Boolean =
                {
                    isPermissionGranted(it, context)
                }

            isAllPermissionGranted = Constants.REQUIRED_PERMISSIONS.all(predicate)
            return isAllPermissionGranted


        }

        fun getPermissionUi(permission: String, context: Context): String {
            when (permission) {
                Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS -> {
                     context.getString(R.string.lbl_contacts_permission)
                }

                Manifest.permission.CALL_PHONE -> {
                    return context.getString(R.string.lbl_call_phone)
                }

                Manifest.permission.SEND_SMS -> {
                    return context.getString(R.string.lbl_send_sms)
                }

                Manifest.permission.READ_SMS -> {
                    return context.getString(R.string.lbl_read_sms)
                }

                Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG -> {
                    return context.getString(R.string.lbl_call_log)
                }

            }
            return ""

        }
    }
}