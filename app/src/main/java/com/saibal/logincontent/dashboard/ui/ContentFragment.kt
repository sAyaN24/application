package com.saibal.logincontent.dashboard.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.saibal.logincontent.R
import com.saibal.logincontent.dashboard.controller.ChemicalAdapter
import com.saibal.logincontent.model.Chemical
import com.saibal.logincontent.util.NetworkCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContentFragment : Fragment() {


    private lateinit var spinner: Spinner
    private val chemicalList = mutableListOf<Chemical>()
    private lateinit var chemicalAdapter: ChemicalAdapter
    private lateinit var recyclerView:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val a = listOf("Lorem", "Ipsum", "Dolor", "Sit", "Amet")
        spinner = view.findViewById(R.id.filterSpinner)
        recyclerView = view.findViewById(R.id.checmicalDetailsRv)

        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, a)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        chemicalAdapter = ChemicalAdapter(requireContext(), chemicalList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = chemicalAdapter


        fetchDetailsOfChemicals()
    }


    private fun fetchDetailsOfChemicals() {
        val url = "http://192.168.0.101:3000/Ingredients"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NetworkCall.fetchResponse(requireContext(), url)
                withContext(Dispatchers.Main) {
                    for(i in 0 until response.length()){
                        var jsonObject = response.getJSONObject(i)
                        var todoJson = jsonObject.toString()
                        var gson = Gson()
                        var chemical = gson.fromJson(todoJson, Chemical::class.java)
                        chemicalList.add(chemical)

                    }
                    chemicalAdapter.notifyDataSetChanged()
                }
            }catch(e: Exception){

                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Please connect to internet or Server is too busy", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContentFragment().apply {

            }
    }
}