package com.anilkumawat3104.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginpage.R
import com.example.loginpage.localMessages
import com.google.android.material.floatingactionbutton.FloatingActionButton

class message(val context : Context,val user : String,val friend : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var messageList = emptyList<localMessages>()
    var position1 = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       if(viewType==1){
           val view : View = LayoutInflater.from(context).inflate(R.layout.send,parent,false)
           return SendViewHolder(view)
       }
        else {
           val view : View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)

           return ReceiverViewHolder(view)
       }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentmessage = messageList[position]
        if (holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            holder.sendmessage.text = currentmessage.content
            holder.send_time.text = currentmessage.timestamp?.subSequence(11,16)
        }
        else {
            val viewHolder = holder as ReceiverViewHolder
            holder.receivemessage.text = currentmessage.content
            holder.receive_time.text = currentmessage.timestamp?.subSequence(11,16)
        }
    }
    override fun getItemCount(): Int {
        return messageList.size
    }
    override fun getItemViewType(position: Int): Int {
       position1 = position
        if(messageList[position].sender==user){
            return 1
        }
        else{
            return 2
        }
    }

    internal fun setMessages(messageList: List<localMessages>) {
        this.messageList = messageList
        notifyDataSetChanged()

    }

    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
           var sendmessage = itemView.findViewById<TextView>(R.id.send_text)
           var send_time = itemView.findViewById<TextView>(R.id.send_time)
    }
    class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var receivemessage = itemView.findViewById<TextView>(R.id.receivetext)
        var receive_time = itemView.findViewById<TextView>(R.id.receive_time)
    }
}