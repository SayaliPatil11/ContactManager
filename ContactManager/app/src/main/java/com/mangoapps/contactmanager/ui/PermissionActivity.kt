package com.mangoapps.contactmanager.ui

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mangoapps.contactmanager.R
import com.mangoapps.contactmanager.common.Constants
import com.mangoapps.contactmanager.common.UiHelper
import com.mangoapps.contactmanager.databinding.ActivityPermissionBinding
import com.mangoapps.contactmanager.model.PermissionModel
import com.mangoapps.contactmanager.ui.adapter.PermissionListAdapter

class PermissionActivity : AppCompatActivity() {
    private var binding: ActivityPermissionBinding? = null
    private lateinit var manager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = LinearLayoutManager(this)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val permissionList = ArrayList<PermissionModel>()
        for (i in Constants.REQUIRED_PERMISSIONS) {
            var permissionName = UiHelper.getPermissionUi(i, this)
            if (!permissionList.contains(PermissionModel(permissionName))) {
                if (!permissionName.isNullOrBlank()) {
                    permissionList.add(PermissionModel(permissionName))
                }

            }
        }
        binding?.rlPermissionList?.apply {
            adapter = permissionList?.let { it1 ->
                PermissionListAdapter(it1)

            }
            layoutManager = manager
        }

        binding?.btnGrantPermission?.setOnClickListener {
            requestPermission()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!UiHelper.isAllPermissionGranted(this)) {
            finishAffinity()
        }
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            Constants.REQUIRED_PERMISSIONS,
            Constants.PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissions?.let { super.onRequestPermissionsResult(requestCode, it, grantResults) }
        when (requestCode) {
            Constants.PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                if (UiHelper.isAllPermissionGranted(this)) {

                    finish()
                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //   if (shouldShowRequestPermissionRationale(*{Constants.REQUIRED_PERMISSIONS})) {
                        showMessageOKCancel(
                            getString(R.string.lbl_grant_all_permission)
                        ) { dialog, which ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermission()
                            }
                        }
                        return
                        //  }
                    }
                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@PermissionActivity)
            .setMessage(message)
            .setPositiveButton(getString(R.string.btn_ok), okListener)
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .create()
            .show()
    }


}