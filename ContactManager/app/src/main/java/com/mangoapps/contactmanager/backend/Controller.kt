package com.mangoapps.contactmanager.backend

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.Telephony
import android.telephony.SmsManager
import com.mangoapps.contactmanager.MainActivity
import com.mangoapps.contactmanager.R
import com.mangoapps.contactmanager.common.BackendHelper
import com.mangoapps.contactmanager.common.Constants
import com.mangoapps.contactmanager.common.UiHelper
import com.mangoapps.contactmanager.model.CallLogModel
import com.mangoapps.contactmanager.model.ContactModel
import com.mangoapps.contactmanager.model.SMSModel

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.READ_CALL_LOG
import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.READ_SMS
import android.Manifest.permission.SEND_SMS
import android.Manifest.permission.WRITE_CALL_LOG
import android.Manifest.permission.WRITE_CONTACTS

/* Backend class to handle all business logic*/
class Controller {

    companion object {

        var incomingCallListFromDevice: ArrayList<CallLogModel> = arrayListOf()
        var outgoingCallList: ArrayList<CallLogModel> = arrayListOf()
        var missedCallList: ArrayList<CallLogModel> = arrayListOf()

        @SuppressLint("Range")
                /*
                * getAllContact
                * context - Current Context
                * return -  MutableList<ContactModel>
                * Method to fetch contact list in device
                * */
        fun getAllContact(context: Context): MutableList<ContactModel> {
            var contactNumberList = mutableListOf<String>()
            var contactList = mutableListOf<ContactModel>()

            if (UiHelper.isPermissionGranted(
                    READ_CONTACTS,
                    context
                ) && UiHelper.isPermissionGranted(
                    WRITE_CONTACTS, context
                )
            ) {
                val contentResolver: ContentResolver = context.contentResolver
                val cursor =
                    contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        null,
                        null,
                        null,
                        "display_name ASC"
                    )
                if (cursor != null && cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id: String = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts._ID)
                        )
                        var name: String? = cursor.getString(
                            cursor.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME
                            )
                        )

                        // getting contact image
                        val contactImage: String? = cursor.getString(
                            cursor.getColumnIndex(
                                ContactsContract.Contacts.PHOTO_URI
                            )
                        )

                        val cursorInfo = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )
                        while (cursorInfo != null && cursorInfo.moveToNext()) {
                            var mobileNumber =
                                cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            mobileNumber = mobileNumber.replace(" ", "")
                            mobileNumber = mobileNumber.replace("(", "")
                            mobileNumber = mobileNumber.replace(")", "")
                            mobileNumber = mobileNumber.replace("-", "")
                            if (null == name) {
                                name = ""
                            }
                            if (!contactNumberList.contains(mobileNumber)) {
                                if (contactImage != null) {
                                    contactList.add(
                                        ContactModel(
                                            id,
                                            Uri.parse(contactImage),
                                            name,
                                            mobileNumber
                                        )
                                    )
                                } else {
                                    val resourcePath =
                                        "android.resource://" + context.packageName + "/" + R.drawable.ic_contact_person
                                    contactList.add(
                                        ContactModel(
                                            id,
                                            Uri.parse(resourcePath),
                                            name,
                                            mobileNumber
                                        )
                                    )
                                }
                                contactNumberList.add(mobileNumber)
                            }
                        }
                    }
                }
            }
            return contactList
        }

        /*
        * getSmsListInDevice
        * context - Current Context
        * return - ArrayList<SMSModel>
        * Method to get SMS list in device
        * */
        fun getSmsListInDevice(context: Context): ArrayList<SMSModel> {
            val smsList = ArrayList<SMSModel>()
            val smsNumberList = ArrayList<String>()
            if (UiHelper.isPermissionGranted(READ_SMS, context)) {
                val cursor: Cursor? =
                    context.contentResolver
                        .query(Telephony.Sms.CONTENT_URI, null, null, null, null)

                if (cursor?.moveToFirst() == true) { // must check the result to prevent exception
                    do {
                        val phoneNumber =
                            cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                        val body =
                            cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                        val date =
                            cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                        val dateFormat = BackendHelper.convertTimeStampToDate(
                            date.toLong(),
                            Constants.DATE_PATTERN_WITH_YEAR_FIRST
                        );


                        if (!smsNumberList.contains(phoneNumber)) {
                            smsList.add(SMSModel(phoneNumber, body, dateFormat.toString()))
                        }

                        smsNumberList.add(phoneNumber)

                    } while (cursor?.moveToNext() == true)
                }
            }
            return smsList

        }

        /*
        * getAllCallLogs
        * context:Current Context
        * return - ArrayList<CallLogModel>
        Method to get all call logs in device*/
        @SuppressLint("Range", "SuspiciousIndentation")
        fun getAllCallLogs(context: Context): ArrayList<CallLogModel> {
            var contactList = ArrayList<String>()
            var callLogList = ArrayList<CallLogModel>()
            //reading all data in descending order according to DATE

            if (UiHelper.isPermissionGranted(
                    READ_CALL_LOG,
                    context
                ) && UiHelper.isPermissionGranted(
                    WRITE_CALL_LOG, context
                )
            ) {
                val strOrder = CallLog.Calls.DATE + " DESC"
                val callUri = Uri.parse("content://call_log/calls")
                val cur = context.contentResolver.query(callUri, null, null, null, strOrder)
                // loop through cursor
                while (cur!!.moveToNext()) {
                    val callNumber = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER))
                    val callName = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    val callDate = cur.getString(cur.getColumnIndex(CallLog.Calls.DATE))
                    val callType = cur.getString(cur.getColumnIndex(CallLog.Calls.TYPE))
                    val duration = cur.getString(cur.getColumnIndex(CallLog.Calls.DURATION))



                    if (!contactList.contains(callNumber)) {
                        val item = CallLogModel(
                            callName,
                            callNumber,
                            duration,
                            BackendHelper.convertTimeStampToDate(
                                callDate.toLong(),
                                Constants.DATE_PATTERN_WITH_TIME
                            ), callType.toInt()
                        )
                        callLogList.add(
                            item
                        )
                    }
                    contactList.add(callNumber)
                    //Process only minimum data
                    if (callLogList.size == 10) {
                        break
                    }

                }
            }

            return callLogList
        }

        /*
        * initiateCall
        *  context-Current context
        * number- phone/mobile number
        * return - unit
        * Method to make phone call to provided number*/
        fun initiateCall(context: Context, number: String) {
            if (UiHelper.isPermissionGranted(CALL_PHONE, context)) {
                try {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:" + number)
                    context.startActivity(intent)

                } catch (e: Exception) {
                }
            }
        }

        /*sendSms
        * context - Context
        * number - phone/mobile number
        * returns- Unit
        * Method to send sms to number passed in parameter*/
        fun sendSms(context: Context, number: String) {
            if (UiHelper.isPermissionGranted(SEND_SMS, context)) {
                //Getting intent and PendingIntent instance
                val intent = Intent(context, MainActivity::class.java)
                val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


//Get the SmsManager instance and call the sendTextMessage method to send message
                val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.getSystemService(SmsManager::class.java)
                } else {
                    SmsManager.getDefault()
                }
                smsManager.sendTextMessage(
                    number,
                    null,
                    context.getString(R.string.lbl_hello_text),
                    pi,
                    null
                )
            }
        }
    }
}