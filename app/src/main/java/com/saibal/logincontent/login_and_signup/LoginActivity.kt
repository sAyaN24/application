package com.saibal.logincontent.login_and_signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.saibal.logincontent.R
import com.saibal.logincontent.camera.PictureUploadActivity
import com.saibal.logincontent.common.Common

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwdEditText: TextInputEditText
    private lateinit var signupTextView: TextView
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailEditText = findViewById(R.id.loginEmailEdt)
        passwdEditText = findViewById(R.id.loginPswdEdt)
        signupTextView = findViewById(R.id.signup_tv)
        loginButton = findViewById(R.id.loginBtn)

        loginButton.setOnClickListener(this)
        signupTextView.setOnClickListener(this)

    }

    private fun authenticateUser(){
        if((emailEditText.text.toString().equals("admin@gmail.com") &&
                    passwdEditText.text.toString().equals("admin"))||
            (emailEditText.text.toString().equals("user@gmail.com") &&
                    passwdEditText.text.toString().equals("user"))){
            //change activity to main activity
            //finish()
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            var intent:Intent = Intent(this, PictureUploadActivity::class.java)
            intent.putExtra(Common.USER_EMAIL_INTENET_KEY, emailEditText.text.toString())
            startActivity(intent)
            finishAffinity()

        }else{
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
        }
    }

    private fun putExtras(s: String) {

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.loginBtn -> {
                authenticateUser()
            }

            R.id.signup_tv -> {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }
        }
    }
}