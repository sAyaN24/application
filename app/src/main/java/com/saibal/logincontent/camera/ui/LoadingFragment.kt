package com.saibal.logincontent.camera.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.saibal.logincontent.R

private const val ARG_PARAM = "param"

class LoadingFragment(var flag: Boolean) : Fragment() {

    private lateinit var mainHandler: Handler
    private lateinit var loaddingSpinner: CircularProgressIndicator
    private lateinit var loadingTv: TextView
    private lateinit var noRespTv: TextView
    private lateinit var noRespIv: ImageView
    private lateinit var goBackBtn:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainHandler = Handler(Looper.getMainLooper())
        loaddingSpinner = view.findViewById(R.id.circularProgressIndicator)
        noRespTv = view.findViewById(R.id.noRespTv)
        noRespIv = view.findViewById(R.id.noResponseIv)
        goBackBtn = view.findViewById(R.id.goBackBtn)
        loadingTv = view.findViewById(R.id.loadingTv)
        arguments?.let {
            Toast.makeText(requireContext(), flag.toString()+"CREATE", Toast.LENGTH_SHORT).show()
            flag = it.getBoolean(ARG_PARAM)
        }
        if(flag) {
            Toast.makeText(requireContext(), flag.toString(), Toast.LENGTH_SHORT).show()
            loaddingSpinner.visibility = View.VISIBLE
            loadingTv.visibility = View.VISIBLE
            mainHandler.postDelayed({
                loaddingSpinner.visibility = View.GONE
                loadingTv.visibility = View.GONE
                noRespTv.visibility = View.VISIBLE
                noRespIv.visibility = View.VISIBLE
                goBackBtn.visibility = View.VISIBLE
            }, 15500)

        }
        else{
            Toast.makeText(requireContext(), flag.toString(), Toast.LENGTH_SHORT).show()
            loaddingSpinner.visibility = View.GONE
            loadingTv.visibility = View.GONE
            noRespTv.visibility = View.VISIBLE
            noRespIv.visibility = View.VISIBLE
            goBackBtn.visibility = View.VISIBLE
        }

        goBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacksAndMessages(null)
    }

}