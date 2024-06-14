package com.saibal.logincontent.util

import android.annotation.SuppressLint
import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONArray
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


object NetworkCall {

    private var requestQueue: RequestQueue? = null

    private fun getRequestQueue(context: Context): RequestQueue {
        return requestQueue ?: Volley.newRequestQueue(context.applicationContext).also { requestQueue = it }
    }

    suspend fun fetchResponse(context: Context, url: String): JSONArray = suspendCancellableCoroutine {
        continuation ->
                        var requestQueue = getRequestQueue(context)
                        val stringRequest = StringRequest(Request.Method.GET, url,
                            { response ->
                                try {
                                    val jsonArray = JSONArray(response)
                                    continuation.resume(jsonArray)
                                }catch (e:Exception){
                                    continuation.resumeWithException(e)
                                }
                            },
                             { error ->
                                continuation.resumeWithException(error)
                            })
                        requestQueue.add(stringRequest)
            continuation.invokeOnCancellation { requestQueue.cancelAll(stringRequest) }
    }

}