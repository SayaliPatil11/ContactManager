package com.mangoapps.contactmanager.model

import android.net.Uri

data class ContactModel(val id: String, val image: Uri?, val name: String, val number: String)
