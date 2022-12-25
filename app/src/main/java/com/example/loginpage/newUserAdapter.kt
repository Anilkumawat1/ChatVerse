package com.example.loginpage


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class newUserAdapter(val context : Context, val name : ArrayList<String>) : RecyclerView.Adapter<newUserAdapter.MyviewHolder>() {

    var onItemClick : ((String)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val inflater  = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view,parent,false)
        return MyviewHolder(view)
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        holder.text1.text = name[position]
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(name[position])
        }
    }

    override fun getItemCount(): Int {
        return name.size
    }
    class MyviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var text1= itemView.findViewById<TextView>(R.id.textView)

    }
}
