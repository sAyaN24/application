package com.saibal.logincontent.dashboard

import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.saibal.logincontent.R
import com.saibal.logincontent.dashboard.ui.ContentFragment
import com.saibal.logincontent.dashboard.ui.FileUploadFragment
import com.saibal.logincontent.dashboard.ui.UserFragment


class MainActivity : AppCompatActivity() {

    private lateinit var fragementContainerView: FrameLayout
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fragementContainerView = findViewById(R.id.nav_host_fragment_container)
        bottomNavigationView = findViewById(R.id.bottom_navbar)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            bottomNavigationView.setOnApplyWindowInsetsListener(null)
            bottomNavigationView.setPadding(0, 0, 0, 0);
        }

        loadFragement(FileUploadFragment())
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard -> {
                    loadFragement(FileUploadFragment.newInstance())
                    true
                }

                R.id.history -> {
                    //loadFragement(WorkersFragment())
                    true
                }

                R.id.search->{
                    loadFragement(ContentFragment())
                    true
                }

                R.id.user -> {
                    loadFragement(UserFragment())
                    true
                }


                else -> false
            }

        }

    }

    private fun loadFragement(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_container, fragment).commit()
    }
}