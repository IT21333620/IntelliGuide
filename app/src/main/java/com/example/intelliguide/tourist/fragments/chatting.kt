package com.example.intelliguide.tourist.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.R
import com.example.intelliguide.adapter.MessageAdapter
import com.example.intelliguide.models.Message
import okhttp3.*
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class chatting : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageView
    lateinit var messageList:MutableList<Message>
    private lateinit var messageAdapter: MessageAdapter
    val client = OkHttpClient.Builder().readTimeout(60, java.util.concurrent.TimeUnit.SECONDS).build()
    val API_KEY = "sk-6f2b2-4f2b2-4f2b2-4f2b2-4f2b2" // add a openAi api key here


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chatting, container, false)
        arguments?.let {
            val locationName = arguments?.getString("locationName")

            recyclerView = view.findViewById(R.id.recyclerView)
            messageEditText = view.findViewById(R.id.txtQuestion)
            sendButton = view.findViewById(R.id.btnsend)
            messageList = ArrayList()

            //setup recycler view
            messageAdapter = MessageAdapter(messageList)
            recyclerView.adapter = messageAdapter
            val llm = LinearLayoutManager( requireContext() )
            llm.stackFromEnd = true
            recyclerView.layoutManager = llm

            sendButton.setOnClickListener {
                val question = messageEditText.text.toString().trim()
                addToChat(question,Message.SENT_BY_ME)
                messageEditText.setText("")

                if (!locationName.isNullOrEmpty()) {
                    val message =
                        "I am talking about $locationName. " +
                                "Don't answer any questions outside of $locationName. This is the question: $question"
                    callAPI(message)
                }
            }
        }

        return view
    }
    fun addResponse(response:String?){
        messageList.removeAt(messageList.size -1)
        addToChat(response!!,Message.SENT_BY_BOT)

    }

    private fun addToChat(message: String, sentBy: String){
        requireActivity().runOnUiThread {
            messageList.add(Message(message, sentBy))
            messageAdapter.notifyDataSetChanged()
            recyclerView.smoothScrollToPosition(messageAdapter.itemCount)
        }
    }

    private fun callAPI(question: String) {
        //call okhttp
        messageList.add(Message("Typing...",Message.SENT_BY_BOT))
        val jsonBody = JSONObject()

        try {
            jsonBody.put("model", "gpt-3.5-turbo")
            val messageArr = JSONArray()
            val obj = JSONObject().apply {
                put("role", "user")
                put("content", question)
            }
            messageArr.put(obj)

            jsonBody.put("messages", messageArr)

        }catch (e: JSONException){
            e.printStackTrace()
        }

        val body :RequestBody = jsonBody.toString().toRequestBody(JSON)
        val request:Request = Request.Builder()
            .url("\n" +
                    "https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer $API_KEY")
            .post(body)
            .build()


        client.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                addResponse("Failed to load response due to ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var jsonObject: JSONObject?
                    try {
                        response.body?.let {
                            jsonObject = JSONObject(it.string())
                            val jsonArray = jsonObject?.getJSONArray("choices")
                            val result = jsonArray?.getJSONObject(0)?.getJSONObject("message")?.getString("content")
                            result?.let {
                                addResponse(it.trim())
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    addResponse("Failed to load response due to ${response.body.toString()}")
                }
            }
        })

    }
    companion object{
        val JSON : MediaType = "application/json; charset=utf-8".toMediaType()
    }


}
