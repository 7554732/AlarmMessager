package com.fomichev.alarmmessager

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionGetter(val activity: MainActivity) {

    private val requestPermissionLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    fun isGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(permission: String) {
        if(isGranted(permission)) return

        requestPermissionLauncher.launch(permission)
        ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            permission
        )
    }
}