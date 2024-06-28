package com.saibal.logincontent.camera.ui

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
import com.saibal.logincontent.dashboard.MainActivity
import com.saibal.logincontent.util.NetworkCall
import com.takusemba.cropme.CropLayout
import com.takusemba.cropme.OnCropListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class FileUploadFragment : Fragment(), View.OnClickListener {

    private lateinit var cameraBtn: Button
    private lateinit var uploadBtn: Button
    private lateinit var cropBtn: Button
    private lateinit var cropview: CropLayout

    private lateinit var context: Context
    private lateinit var base64String: String
    private var imageUriString = ""


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
                            Log.e("Crop", e.message.toString())
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onSuccess(bitmap: Bitmap) {
                            cameraBtn.visibility = View.VISIBLE
                            cameraBtn.text = "Retake Picture"
                            cropBtn.visibility = View.GONE
                            uploadBtn.visibility = View.VISIBLE
                            cropview.setBitmap(bitmap)
                            base64String = compressedBitmapToBase64(bitmap)
                            Log.i("base64String", base64String)
                        }
                    }).also { cropview.crop() }
            }

            R.id.upload_button -> {

                uploadImageToCloud()
                //starts the loading fragment
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(
                        R.id.fragmentContainer,
                        LoadingFragment(true)
                    )?.addToBackStack(null)?.commit()
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

    private fun compressedBitmapToBase64(bitmap: Bitmap): String {
        //val compressedBitmap = compressImage(bitmap)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        imageUriString = getImageUriFromBitmap(bitmap)
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

    private fun checkPermissionThenDisplayAlert(permissionsNeeded: MutableList<String>) {
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

    private fun doWithUi() {
        uploadBtn.visibility = View.GONE
        cameraBtn.visibility = View.GONE
        cropBtn.visibility = View.VISIBLE
        dispatchTakePictureIntent()
    }

//    private fun compressImage(image: Bitmap): Bitmap {
//        val stream = ByteArrayOutputStream()
//        image.compress(Bitmap.CompressFormat.JPEG, 50, stream)
//        val byteArray = stream.toByteArray()
//        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//    }

    private fun getImageUriFromBitmap(bitmap: Bitmap):String{
        val path: String = MediaStore.Images.Media.insertImage(
            requireContext().getContentResolver(),
            bitmap,
            "Title",
            null
        )
        return Uri.parse(path).toString()
    }


    private fun uploadImageToCloud() {
        val url = "https://wrft502lgh.execute-api.ap-south-1.amazonaws.com/PROD/inference"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val params = mapOf<String, String>(
                    "u_id" to "90764375-61ae-4170-a2f8-98b582c79458",
                    "body-json" to base64String
                )
                Log.i("base64StringUU", base64String)


                val response = NetworkCall.postData(requireContext(), url, JSONObject(params))
                withContext(Dispatchers.Main) {
                    Log.d(
                        "response",
                        response.getJSONObject("body").getJSONArray("ingredients").toString()
                    )
                    if(response.has("errorMessage")){

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.fragmentContainer,
                                LoadingFragment(false)
                            )?.commit()
                        return@withContext
                    }

                    val jsonArray = response.getJSONObject("body").getJSONArray("ingredients")

                    startActivity(
                        Intent(
                            activity?.applicationContext,
                            MainActivity::class.java
                        ).also {
                            it.putExtra(
                                Common.INGREDIENT_RES_INTENT_KEY,
                                jsonArray.toString()
                            )
                            it.putExtra(Common.IMAGE_BASE_64_INTENT_KEY,imageUriString)
                        })
                }

            } catch (e: Exception) {
                //Error message for no response due to internet or something general
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentContainer,
                        LoadingFragment(false)
                    )?.commit()
            }

        }
    }

}