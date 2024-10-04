package com.mubarak.permissionlibrary.permission

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/***
 * Created by Mubarak Ansari
 * This is for Activity only
 * if you are take permission of the single then, you just need to called following function only
 *
 * 1. requestSinglePermission()
 * Note : base on your requirement, you can called this fun direct
 *        onCreateView or any view setOnClickListener
 *
 * 2. override the onSinglePermissionGranted on your fragment class
 *
 * 3. override onPermissionResult for both permission activity result
 *
 */
abstract class BasePermissionActivity : AppCompatActivity(), PermissionHandler {

    open fun getSinglePermission(): String = ""
    open fun getMultiplePermissions(): Array<String> = emptyArray()

    private lateinit var requestSinglePermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestMultiPermissionsLauncher: ActivityResultLauncher<Array<String>>

    lateinit var activityResultIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissionLaunchers()
        initActivityResultLauncher()
    }

    private fun initPermissionLaunchers() {
        val singlePermission = getSinglePermission()
        if (singlePermission.isNotEmpty()) {
            requestSinglePermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        onSinglePermissionGranted()
                    } else {
                        onSinglePermissionDenied(singlePermission)
                    }
                }
        }

        val multiplePermissions = getMultiplePermissions()
        if (multiplePermissions.isNotEmpty()) {
            requestMultiPermissionsLauncher =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    val allPermissionsGranted = permissions.values.all { it }
                    if (allPermissionsGranted) {
                        onMultiplePermissionsGranted()
                    } else {
                        onMultiplePermissionsDenied(multiplePermissions)
                    }
                }
        }
    }

    private fun initActivityResultLauncher() {
        activityResultIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                if (activityResult.resultCode == android.app.Activity.RESULT_OK) {
                    // Handle the result after the permission is granted
                    Log.e("TAG", "initActivityResultLauncher: ${activityResult.data}")
                    onPermissionResult(activityResult)
                }
            }
    }

    /***
     * make single permission called()
     */
    fun requestSinglePermission() {
        val singlePermission = getSinglePermission()
        if (singlePermission.isNotEmpty()) {
            if (singlePermission.isNotEmpty() &&
                ContextCompat.checkSelfPermission(
                    this,
                    singlePermission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onSinglePermissionGranted()
            } else {
                if (singlePermission.isNotEmpty()) {
                    requestSinglePermissionLauncher.launch(singlePermission)
                } else {
                    onSinglePermissionGranted()
                }
            }
        } else {
            Toast.makeText(
                this,
                "Please override single permission getSinglePermission() function",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /***
     * make multi permission called()
     */
    fun requestMultiplePermissions() {
        val multiplePermissions = getMultiplePermissions()
        if (multiplePermissions.isNotEmpty()) {
            if (multiplePermissions.all {
                    ContextCompat.checkSelfPermission(this, it) ==
                            PackageManager.PERMISSION_GRANTED
                }
            ) {
                onMultiplePermissionsGranted()
            } else {
                if (multiplePermissions.isNotEmpty()) {
                    requestMultiPermissionsLauncher.launch(multiplePermissions)
                } else {
                    onMultiplePermissionsGranted()
                }
            }
        } else {
            Toast.makeText(
                this,
                "Please override multiple permission requestMultiplePermissions() function",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    /***
     * take result if permission grand from the user
     */
    open fun onSinglePermissionGranted() {
        // Default implementation for single permission granted
        Log.e("TAG", "Single permission granted")
    }

    /***
     * take result if permission grand from the user
     */
    open fun onMultiplePermissionsGranted() {
        // Default implementation for multiple permissions granted
        Log.e("TAG", "Multiple permissions granted")
    }

    // Handle the result after the permission is granted
    abstract override fun onPermissionResult(activityResult: ActivityResult?)

    // base on your requirement, you are able to override the fun on fragment
    abstract override fun showPermissionRationaleDialog(permission: String)

    private fun onSinglePermissionDenied(permission: String) {
        printLog("Single permission denied")

        // Check if the permission should be shown rationale
        if (permission.isNotEmpty() && ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                permission
            ).not() &&
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showPermissionRationaleDialog(permission)
        }
    }

    private fun onMultiplePermissionsDenied(permissions: Array<String>) {
        printLog("Some permissions are denied")

        // Check if any of the permissions should be shown rationale
        permissions.forEach { permission ->
            if (permission.isNotEmpty() && ActivityCompat.shouldShowRequestPermissionRationale(
                    this, permission).not() &&
                ContextCompat.checkSelfPermission(
                    this, permission) != PackageManager.PERMISSION_GRANTED
            ) {
                showPermissionRationaleDialog(permission)
                return@forEach
            }
        }
    }

    private fun printLog(message: String) {
        Log.e(TAG, "printLog: $message")
    }
}