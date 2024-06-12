package com.saibal.logincontent

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.saibal.logincontent.common.Common

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var ingrdientImageView:ImageView
    private lateinit var cameraBtn:Button
    private lateinit var uploadBtn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ingrdientImageView = findViewById(R.id.ingredint_ImageView)
        cameraBtn = findViewById(R.id.camera_button)
        uploadBtn = findViewById(R.id.upload_button)

        cameraBtn.setOnClickListener(this)
        uploadBtn.setOnClickListener(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Common.PICTURE_INTENT_KEY){
            val bitmap = data?.extras?.get("data") as Bitmap
            ingrdientImageView.setImageBitmap(bitmap)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.camera_button -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityIfNeeded(intent, Common.PICTURE_INTENT_KEY)

            }
            R.id.upload_button -> {
                //val intent = Intent(this,UploadActivity::class.java)
            }
        }
    }
}