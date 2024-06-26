package com.saibal.logincontent.dashboard.ui

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.saibal.logincontent.R
import com.saibal.logincontent.common.Common
import com.saibal.logincontent.common.hasAllPermissions
import com.saibal.logincontent.common.hasCameraPermission
import com.saibal.logincontent.common.permissionNeeded
import com.saibal.logincontent.common.permissionNeededForLesserAndroidVersion
import com.takusemba.cropme.CropLayout
import com.takusemba.cropme.OnCropListener
import java.io.ByteArrayOutputStream


class FileUploadFragment : Fragment(), View.OnClickListener {

    private lateinit var cameraBtn: Button
    private lateinit var uploadBtn: Button
    private lateinit var cropBtn: Button
    private lateinit var cropview: CropLayout

    private lateinit var context: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cropview = view.findViewById(R.id.crop_view)
        cameraBtn = view.findViewById(R.id.camera_button)
        cropBtn = view.findViewById(R.id.crop_button)
        uploadBtn = view.findViewById(R.id.upload_button)
        cameraBtn.setOnClickListener(this)
        uploadBtn.setOnClickListener(this)
        cropBtn.setOnClickListener(this)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_file_upload, container, false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.camera_button -> {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    if (!requireContext().hasCameraPermission()) {
                        checkPermissionThenDisplayAlert(requireContext().permissionNeeded())
                    } else {
                        doWithUi()
                    }
                } else {
                    if (!requireContext().hasAllPermissions()) {

                        checkPermissionThenDisplayAlert(requireContext().permissionNeededForLesserAndroidVersion())
                    } else {
                       doWithUi()
                    }
                }
            }
            R.id.crop_button -> {

                cropview.addOnCropListener(
                    object : OnCropListener {
                        override fun onFailure(e: Exception) {
                            Log.e("Crop",  e.message.toString())
                        }
                        @SuppressLint("SetTextI18n")
                        override fun onSuccess(bitmap: Bitmap) {
                            cameraBtn.visibility = View.VISIBLE
                            cameraBtn.text ="Retake Picture"
                            cropBtn.visibility = View.GONE
                            uploadBtn.visibility = View.VISIBLE
                            cropview.setBitmap(bitmap)
                            Toast.makeText(requireContext(), "One day i will do something", Toast.LENGTH_SHORT)
                                .show()
                            val base64String = bitmapToBase64(bitmap)
                            Log.i("base64String", base64String)
                        }
                    }).also { cropview.crop() }
            }

            R.id.upload_button -> {

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentContainer,
                        LoadingFragment())?.
                    addToBackStack(null)?.commit()
                //startActivity(Intent(activity?.applicationContext, MainActivity::class.java))

            }
        }
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                cropBtn.visibility = View.VISIBLE

                if (imageBitmap != null) {
                    cropview.setBitmap(imageBitmap)
                }
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

    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Camera Permission Required")
            .setMessage("This app needs the Camera permission to take pictures. Please grant it in the app settings.")
            .setPositiveButton("Go to Settings") { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun checkPermissionThenDisplayAlert(permissionsNeeded: MutableList<String>){
        if (permissionsNeeded.isNotEmpty()) {
            for (permission in permissionsNeeded) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        permission
                    )
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(permission),
                        Common.REQUEST_PERMISSION
                    )
                } else {
                    showSettingsDialog()
                }
            }
        }
    }

    private fun doWithUi(){
        uploadBtn.visibility = View.GONE
        cameraBtn.visibility = View.GONE
        cropBtn.visibility = View.VISIBLE
        dispatchTakePictureIntent()
    }

}