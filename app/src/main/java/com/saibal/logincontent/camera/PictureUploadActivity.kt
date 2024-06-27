package com.saibal.logincontent.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.saibal.logincontent.R
import com.saibal.logincontent.common.Common
import com.saibal.logincontent.camera.ui.FileUploadFragment
import java.io.ByteArrayOutputStream


class PictureUploadActivity : AppCompatActivity(){

    private lateinit var fragmentContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_picture_upload)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        fragmentContainer = findViewById(R.id.fragmentContainer)


        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), Common.REQUEST_CAMERA_PERMISSION)
        }else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                Common.REQUEST_CAMERA_PERMISSION
            )
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, FileUploadFragment()).commit()

    }

    private fun compressImage(image: Bitmap): Bitmap {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }


}