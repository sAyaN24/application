package com.saibal.logincontent.dashboard

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.saibal.logincontent.R
import com.saibal.logincontent.camera.PictureUploadActivity
import com.saibal.logincontent.common.Common
import com.saibal.logincontent.dashboard.controller.ChemicalAdapter
import com.saibal.logincontent.model.Chemical
import com.saibal.logincontent.user.UserActivity
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    private val chemicalList = mutableListOf<Chemical>()
    private lateinit var chemicalAdapter: ChemicalAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var ingradientCroppedIv: ImageView
    private var ingredientsResponse = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if(intent.hasExtra(Common.INGREDIENT_RES_INTENT_KEY)){
            ingredientsResponse = intent.getStringExtra(Common.INGREDIENT_RES_INTENT_KEY)!!
        }

        recyclerView = findViewById(R.id.checmicalDetailsRv)
        floatingActionButton = findViewById(R.id.userFloatingActionButton)
        ingradientCroppedIv = findViewById(R.id.ingredientCroppedIv)
        if(intent.hasExtra(Common.IMAGE_BASE_64_INTENT_KEY)){
            val imageUriString = intent.getStringExtra(Common.IMAGE_BASE_64_INTENT_KEY)
            ingradientCroppedIv.setImageURI(Uri.parse(imageUriString))
        }

        chemicalAdapter = ChemicalAdapter(this, chemicalList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chemicalAdapter

       fetchIngredients()

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this,UserActivity::class.java))
        }
    }

    private fun fetchIngredients(){
        var ingredientJsonArray = JSONArray(ingredientsResponse)
        for (i in 0 until ingredientJsonArray.length()) {
            var jsonObject = ingredientJsonArray.getJSONObject(i)
            var todoJson = jsonObject.toString()
            var gson = Gson()
            var chemical = gson.fromJson(todoJson, Chemical::class.java)
            chemicalList.add(chemical)
            chemicalAdapter.notifyItemInserted(i)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, PictureUploadActivity::class.java))
    }

    private fun decodeBase64(base64String: String):Bitmap{
        val byteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

//    val severityComparator = Comparator<Chemical> { chem1, chem2 ->
//        when {
//            chem1.severity == "High" && chem2.severity != "High" -> -1 // chem1 is High, chem2 is not
//            chem1.severity != "High" && chem2.severity == "High" -> 1 // chem2 is High, chem1 is not
//            chem1.severity == "Medium" && chem2.severity == "Low" -> -1 // chem1 is Medium, chem2 is Low
//            chem1.severity == "Low" && chem2.severity == "Medium" -> 1 // chem1 is Low, chem2 is Medium
//            else ->0 // Equal severity
//        }
//    }
}