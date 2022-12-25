package com.example.loginpage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class Adapter(val context : Context) : RecyclerView.Adapter<Adapter.MyviewHolder>() {
    var messageList = emptyList<localMessages>()
    var messageList1 = emptyList<localMessages>()
    var onItemClick : ((String)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
       val inflater  = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view,parent,false)
        return MyviewHolder(view)
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
       holder.text1.text = messageList[position].user
        holder.lastmsg.text = messageList[position].content
        holder.lastime.text = messageList[position].timestamp?.substring(11,16)
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(messageList[position].user)
        }
    }
    internal fun setMessages(messageList: List<localMessages>) {
        this.messageList = messageList
        notifyDataSetChanged()

    }
    internal fun setMess(messageList2: List<localMessages>){
        messageList1 = messageList2
    }
    override fun getItemCount(): Int {
        return messageList.size
    }
    class MyviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var text1= itemView.findViewById<TextView>(R.id.textView)
        var lastmsg = itemView.findViewById<TextView>(R.id.textView1)
        var lastime = itemView.findViewById<TextView>(R.id.lastTimeMss)
    }
}
