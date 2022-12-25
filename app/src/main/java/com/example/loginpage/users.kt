package com.example.loginpage

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.loginpage.R.id.user1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton

class users : AppCompatActivity() {
    private  lateinit var Recycler3 : RecyclerView
    lateinit var messageList : ArrayList<localMessages>
        private lateinit var useradapter : Adapter
    private lateinit var username11 : String
    private lateinit var viewModel: ViewModel
    var display = ArrayList<localMessages>()
    private lateinit var newUsers : FloatingActionButton
    private lateinit var clickedUser: ClickedUser



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        username11 = intent.getStringExtra("user_name")!!
        clickedUser=ClickedUser("")
        Recycler3 = findViewById(user1)
        newUsers = findViewById(R.id.newUsers)
        Recycler3.setHasFixedSize(true)
        Recycler3.layoutManager = LinearLayoutManager(this)
        messageList = ArrayList()

        useradapter = Adapter(this)
        Recycler3.adapter = useradapter
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModel.getLastmess().observe(this, Observer { messageList ->
            messageList?.let {
                useradapter.setMessages(it)
                useradapter.setMess(it)

//                Toast.makeText(this,useradapter.messageList.con,Toast.LENGTH_LONG).show()
            }

        })


        //mSocket.emit("disconnect")




        useradapter.onItemClick = {

            val intent = Intent(this@users,DetailedActivity::class.java)
            intent.putExtra("name" ,it)
            clickedUser.username=it
            Log.d("clicked user",clickedUser.username)

            intent.putExtra("user_name",username11)

            startActivity(intent)
        }
        newUsers.setOnClickListener{
            val intent = Intent(this@users,newUser::class.java)

            intent.putExtra("user_name",username11)

            startActivity(intent)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_bar, menu)
        val menuItem = menu!!.findItem(R.id.search)
        if(menuItem!=null){
            val searchView = menuItem.actionView as SearchView
            val searchBox = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            searchBox.hint = "Search.."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    if(p0!!.isNotEmpty()){
                        display.clear()
                        val search = p0.toLowerCase(Locale.getDefault())
                        useradapter.messageList1.forEach {
                            if(it.user.toLowerCase(Locale.getDefault()).contains(search)){
                                display.add(it)
                            }
                        }
                        useradapter.setMessages(display)

                    }
                    else{
                        display.clear()
                        display.addAll(useradapter.messageList1)
                        useradapter.setMessages(display)

                    }
                   return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}

