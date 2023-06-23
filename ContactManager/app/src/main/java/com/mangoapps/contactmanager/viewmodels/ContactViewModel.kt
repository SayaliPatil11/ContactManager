package com.mangoapps.contactmanager.viewmodels

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telephony.SmsManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mangoapps.contactmanager.MainActivity
import com.mangoapps.contactmanager.backend.Controller
import com.mangoapps.contactmanager.model.ContactModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ContactViewModel : ViewModel() {

    private val _contact_list =
        MutableLiveData<MutableList<ContactModel>>()

    val contactList = _contact_list.map {
        _contact_list.value
    }

    /* Function to get contact list from device*/
    fun getContactList(context: Context) {
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        scope.launch() {
            val contactList: ArrayList<ContactModel>

            withContext(Dispatchers.IO) {
                //Fetch contact list on backend thread
                contactList =
                    Controller.getAllContact(context) as ArrayList<ContactModel>

            }
            //update ui
            withContext(Dispatchers.IO) {
                viewModelScope.launch {
                    _contact_list.value = contactList

                }
            }
        }
    }

    /*Function to initiate call to given number*/
    fun initiateCall(number: String?, context: Context) {
        number?.let { Controller.initiateCall(context, it) }
    }


    /* Function to send SMS to given number*/
    fun sendSms(number: String, context: Context) {
        Controller.sendSms(context, number)
    }


}


