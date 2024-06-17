package com.saibal.logincontent.dashboard.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.saibal.logincontent.R
import com.saibal.logincontent.common.Common
import com.saibal.logincontent.login_and_signup.LoginActivity

class UserFragment : Fragment() {


    private lateinit var profileName:TextView
    private lateinit var profileEmail:TextView
    private lateinit var logoutBtn:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = this.arguments?.getString(Common.USER_EMAIL_INTENET_KEY,"abc@email.com").toString()
        val name = email.split("@").get(0)
        profileName = view.findViewById(R.id.profile_name)
        profileEmail = view.findViewById(R.id.profile_email)
        logoutBtn = view.findViewById(R.id.logout_btn)
        profileEmail.text = email
        profileName.text = name
        logoutBtn.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finishAffinity()
         }

    }

}