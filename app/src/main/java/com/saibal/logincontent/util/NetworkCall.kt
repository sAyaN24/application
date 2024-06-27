package com.saibal.logincontent.util

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONArray
import org.json.JSONObject
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

    suspend fun postData(context: Context, url: String, jsonBody: JSONObject): String =
        suspendCancellableCoroutine { continuation ->
            val requestQueue = getRequestQueue(context)

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener<String> { response ->
                    continuation.resume(response)
                },
                Response.ErrorListener { error ->
                    continuation.resumeWithException(error)
                }) {
                override fun getBody(): ByteArray {
                    return jsonBody.toString().toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            var socketTimeout:Int = 5000
            var policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            stringRequest.setRetryPolicy(policy)
            requestQueue.add(stringRequest)
            continuation.invokeOnCancellation {
                requestQueue.cancelAll(stringRequest)
            }
        }

}