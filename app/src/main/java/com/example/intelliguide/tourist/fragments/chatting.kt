package com.example.intelliguide.tourist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intelliguide.R
import com.example.intelliguide.adapter.MessageAdapter
import com.example.intelliguide.models.Message
import okhttp3.OkHttpClient


class chatting : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageList: List<Message>
    private lateinit var messageAdapter: MessageAdapter
    val client = OkHttpClient()
    var API_KEY = "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"

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
            messageAdapter = MessageAdapter(messageList);
            recyclerView.adapter = messageAdapter;
            var llm = LinearLayoutManager( requireContext() );
            llm.stackFromEnd = true
            recyclerView.layoutManager = llm

//            sendButton.setOnClickListener {
//                val question = messageEditText.text.toString().trim()
//                addToChat(question,Message.SENT_BY_ME)
//                messageEditText.setText("")
//                callAPI(question)
//
//                Toast.makeText(requireContext(),question,Toast.LENGTH_LONG).show()
//            }
        }

        return view
    }
//    private fun addToChat(message: String, sentBy: String){
//        runOnUiThread{
//            messageList.add(Message(message,sentBy))
//            messageAdapter.notifyDataSetChanged()
//            recyclerView.smoothScrollToPosition(messageAdapter.itemCount)
//        }
//    }





}