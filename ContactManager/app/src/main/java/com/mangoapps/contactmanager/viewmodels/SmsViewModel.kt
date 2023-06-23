package com.mangoapps.contactmanager.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mangoapps.contactmanager.backend.Controller
import com.mangoapps.contactmanager.model.SMSModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SmsViewModel : ViewModel() {
    private val _sms_list =
        MutableLiveData<MutableList<SMSModel>>()

    val smsList = _sms_list.map {
        _sms_list.value
    }
    //Function to get all SMS from Device
    fun getAllSms(context: Context) {

        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        scope.launch() {
            val smsList: ArrayList<SMSModel>

            withContext(Dispatchers.IO) {
                //Fetch all sms list in device on background thread
                smsList =
                    Controller.getSmsListInDevice(context)

            }
            //Update UI
            withContext(Dispatchers.IO) {
                viewModelScope.launch {
                    _sms_list.value = smsList

                }
            }
        }


    }



}