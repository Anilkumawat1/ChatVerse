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



import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class newUser : AppCompatActivity() {
    private  lateinit var Recycler2 : RecyclerView
    private lateinit var useradapter : newUserAdapter
    private lateinit var username11 : String
    var s : List<String> = ArrayList<String>()
    var display = ArrayList<String>()
    private lateinit var clickedUser: ClickedUser


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        supportActionBar?.title = "Select user"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        username11 = intent.getStringExtra("user_name")!!
        clickedUser=ClickedUser("")
        Recycler2 = findViewById(R.id.user211)
        Recycler2.setHasFixedSize(true)
        Recycler2.layoutManager = LinearLayoutManager(this)
        User()
        useradapter = newUserAdapter(this,display)
        Recycler2.adapter = useradapter


        //mSocket.emit("disconnect")



        useradapter.onItemClick = {

            val intent = Intent(this@newUser,DetailedActivity::class.java)
            intent.putExtra("name" ,it)
            clickedUser.username=it
            Log.d("clicked user",clickedUser.username)

            intent.putExtra("user_name",username11)

            startActivity(intent)
            finish()
        }
    }
    private fun User() {
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        val user = username(username11)
        retrofit.data(user).enqueue(
            object : Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    retrofit: Response<List<String>>
                ) {
                    s = retrofit.body()!!
//                    Toast.makeText(this@users,s.toString(), Toast.LENGTH_LONG).show()
                    display.addAll(s)
                    useradapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Toast.makeText(this@newUser,t.toString(), Toast.LENGTH_LONG).show()
                }

            }
        )

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
                        s.forEach {
                            if(it.toLowerCase(Locale.getDefault()).contains(search)){
                                display.add(it)
                            }
                        }
                        useradapter.notifyDataSetChanged()
                    }
                    else{
                        display.clear()
                        display.addAll(s)
                        useradapter.notifyDataSetChanged()
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
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
}

