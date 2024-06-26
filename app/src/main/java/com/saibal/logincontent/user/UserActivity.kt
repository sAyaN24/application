package com.saibal.logincontent.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.saibal.logincontent.R
import com.saibal.logincontent.login_and_signup.LoginActivity

class UserActivity : AppCompatActivity() {

    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var logoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        profileName = findViewById(R.id.profile_name)
        profileEmail =findViewById(R.id.profile_email)
        logoutBtn = findViewById(R.id.logout_btn)

        profileName.text = "Username"
        profileEmail.text = "username@email.com"

        logoutBtn.setOnClickListener {
            startActivity(Intent(this@UserActivity, LoginActivity::class.java))
            finishAffinity()
        }

    }
}