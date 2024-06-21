package com.saibal.logincontent.common

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.hasCameraPermission():Boolean{
    return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

}
fun Context.hasAllPermissions():Boolean{
    return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    && ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}

fun Context.permissionNeeded():MutableList<String>{
    var permissionsNeeded = mutableListOf<String>()
    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        permissionsNeeded.add(android.Manifest.permission.CAMERA)
    }

    return permissionsNeeded
}

fun Context.permissionNeededForLesserAndroidVersion():MutableList<String>{
    var permissionsNeeded = mutableListOf<String>()
    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        permissionsNeeded.add(android.Manifest.permission.CAMERA)
    }
    if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
        permissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
        permissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    return permissionsNeeded
}