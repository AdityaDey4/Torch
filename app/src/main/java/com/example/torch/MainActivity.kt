package com.example.torch

import android.Manifest
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : AppCompatActivity() {
    lateinit var torchBT: ImageButton
    lateinit var layout: CoordinatorLayout
    var state: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.layout)

        torchBT = findViewById(R.id.torchBT)
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(object :
            PermissionListener {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                runFlashLight()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Snackbar.make(layout, "Permission Needed", Snackbar.LENGTH_SHORT).show()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {

            }

        }).check()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun runFlashLight() {
        torchBT.setOnClickListener {
            val cameraManager: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

            try {
                val cameraId = cameraManager.cameraIdList[0]
                if(!state) {
                    state = true
                    cameraManager.setTorchMode(cameraId, state)
                    torchBT.setImageResource(R.drawable.torch_on)
                }else{
                    state = false
                    cameraManager.setTorchMode(cameraId, state)
                    torchBT.setImageResource(R.drawable.torch_off)
                }
            }catch (e: CameraAccessException){}
        }
    }
}