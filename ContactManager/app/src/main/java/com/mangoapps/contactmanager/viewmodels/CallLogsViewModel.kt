package com.mangoapps.contactmanager.viewmodels

import android.content.Context
import android.provider.CallLog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mangoapps.contactmanager.backend.Controller
import com.mangoapps.contactmanager.model.CallLogModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CallLogsViewModel : ViewModel() {
    var incomingCallListFromDevice: ArrayList<CallLogModel> = arrayListOf()
    var outgoingCallList: MutableList<CallLogModel> = mutableListOf()
    var missedCallList: MutableList<CallLogModel> = mutableListOf()

    private val _incomingCallList =
        MutableLiveData<MutableList<CallLogModel>>()

    val incomingCallLogs = _incomingCallList.map {
        _incomingCallList.value
    }


    private val _outgoingCallList =
        MutableLiveData<MutableList<CallLogModel>>()

    val outgoingCallLogs = _outgoingCallList.map {
        _outgoingCallList.value
    }


    private val _missedCallList =
        MutableLiveData<MutableList<CallLogModel>>()

    val missedCallLogs = _missedCallList.map {
        _missedCallList.value
    }


    fun getCallLogInformation(context: Context) {

        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        scope.launch() {
            val callLogList: ArrayList<CallLogModel>

            withContext(Dispatchers.IO) {
                //Get calllog list list on background thread
                callLogList = async {
                    Controller.getAllCallLogs(context)
                }.await()


            }
            withContext(Dispatchers.IO) {
                if (!callLogList.isNullOrEmpty()) {
                    incomingCallListFromDevice.clear()
                    missedCallList.clear()
                    outgoingCallList.clear()
                    //Differentiate incoming, outgoing, and missed call logs
                    for (i in callLogList) {
                        when (i.callType) {
                            CallLog.Calls.OUTGOING_TYPE -> {
                                outgoingCallList.add(i)
                            }

                            CallLog.Calls.INCOMING_TYPE -> {
                                incomingCallListFromDevice.add(i)
                            }

                            CallLog.Calls.MISSED_TYPE -> {
                                missedCallList.add(i)
                            }
                        }
                    }

//Update UI with current data
                    viewModelScope.launch {
                        _incomingCallList.value = incomingCallListFromDevice
                        _outgoingCallList.value = outgoingCallList
                        _missedCallList.value = missedCallList

                    }
                }

            }
        }
    }
    /*Function to initiate call to given number*/
    fun initiateCall(number: String?, context: Context) {
        number?.let { Controller.initiateCall(context, it) }
    }

}