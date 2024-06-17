package com.saibal.logincontent.dashboard.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.saibal.logincontent.R
import com.saibal.logincontent.common.Common
import com.saibal.logincontent.common.hasCameraPerrmission
import java.io.ByteArrayOutputStream



class FileUploadFragment : Fragment(), View.OnClickListener {

    private lateinit var ingrdientImageView: ImageView
    private lateinit var cameraBtn: Button
    private lateinit var uploadBtn: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ingrdientImageView = view.findViewById(R.id.ingredint_ImageView)
        cameraBtn = view.findViewById(R.id.camera_button)
        uploadBtn = view.findViewById(R.id.upload_button)
        cameraBtn.setOnClickListener(this)
        uploadBtn.setOnClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_file_upload, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FileUploadFragment()
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.camera_button -> {
                if(requireContext().hasCameraPerrmission()){
                    dispatchTakePictureIntent()
                }else{
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), Common.REQUEST_CAMERA_PERMISSION)
                }
            }
            R.id.upload_button -> {
                //val intent = Intent(this,UploadActivity::class.java)
            }
        }
    }



    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            ingrdientImageView.setImageBitmap(imageBitmap)
            var base64String = imageBitmap?.let { bitmapToBase64(it) }
            Log.i("base64String", base64String.toString())
        }
    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureLauncher.launch(takePictureIntent)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


}