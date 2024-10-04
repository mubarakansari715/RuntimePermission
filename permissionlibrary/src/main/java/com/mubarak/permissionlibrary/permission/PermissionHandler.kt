package com.mubarak.permissionlibrary.permission

import androidx.activity.result.ActivityResult

interface PermissionHandler {
    fun onPermissionResult(activityResult: ActivityResult?)
    fun showPermissionRationaleDialog(permission: String)
}