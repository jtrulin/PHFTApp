package com.example.phftapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for displaying messages in RecyclerView
class MessageAdapter(private val messageList: List<String>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    // ViewHolder to hold references to each message item view
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewMessage: TextView = itemView.findViewById(R.id.textViewMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        // Inflate the item layout for each message
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        // Bind the message text to the TextView
        val message = messageList[position]
        holder.textViewMessage.text = message
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}
