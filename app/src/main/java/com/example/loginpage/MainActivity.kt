package com.example.loginpage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.anilkumawat3104.recyclerview.messages
import com.example.loginpage.SignalProtocolImplementation.Curve25519Implementation
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import io.socket.client.Socket
import org.whispersystems.curve25519.Curve25519KeyPair
import java.nio.charset.Charset
import java.time.LocalDateTime
import kotlin.text.Charsets.UTF_16LE

class MainActivity : AppCompatActivity() {

    private lateinit var signup : TextView
    private lateinit var login : TextView
    private lateinit var signuplayout : LinearLayout
    private lateinit var loginlayout : LinearLayout
    private lateinit var loginB : Button
    private lateinit var signupB : Button
    private lateinit var username : TextView
    private lateinit var password : TextView
    private lateinit var usernameS : TextView
    private lateinit var passwordS1 : TextView
    private lateinit var passwordS2 : TextView
    private lateinit var localmess : localMessages
    private lateinit var keypairObj: Curve25519KeyPair
    private lateinit var keyData:KeyPair
    lateinit var username11 : String
    lateinit var mSocket : Socket
    private lateinit var viewModel: ViewModel
    val gson: Gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        initializeVariable() // initializing variable

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SocketHandler.setSocket()
        }

        keypairObj=Curve25519Implementation.getKeyPair()

        keyData=KeyPair(keypairObj.privateKey,keypairObj.publicKey)

        SocketHandler.setSocket()

        mSocket=SocketHandler.getSocket()

        mSocket.connect()

        mSocket.on("receive-message"){args ->
            if(args[0]!=null){
                val m=args[0]

                val mes=gson.fromJson(args[0].toString(), messages::class.java)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    var user : String
                    if(mes.receiverid==username11)
                        user = mes.senderid
                    else
                        user = mes.receiverid
                    localmess = localMessages(null,mes.mess,mes.senderid,mes.receiverid,
                        LocalDateTime.now().toString(),user)
                }

                runOnUiThread{
//                    val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
//                    val user = username(localmess.sender!!)
//                    var publickey : ByteArray = "anil".toByteArray()
//                    retrofit.getPublickey(user!!).enqueue(
//                        object : Callback<List<String>>{
//
//                            override fun onResponse(
//                                call: Call<List<String>>,
//                                retrofit: Response<List<String>>
//                            ) {
//                                val s : List<String> = retrofit.body()!!
//                                publickey = s[0].toByteArray(UTF_16LE)
//                                Log.d("publickey",publickey.toString(UTF_16LE))
//
//
//                            }
//
//                            override fun onFailure(call: Call<List<String>>, t: Throwable) {
//                                Toast.makeText(this@MainActivity,t.toString(),Toast.LENGTH_LONG).show()
//                            }
//
//                        }
//                    )
//
//
//
                    val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
                    val user = username(localmess.sender!!)
                    var publickey : ByteArray = "anil".toByteArray()
                    retrofit.getPublickey(user!!).enqueue(
                        object : Callback<List<String>>{
                            override fun onResponse(
                                call: Call<List<String>>,
                                retrofit: Response<List<String>>
                            ) {
                                val s : List<String> = retrofit.body()!!
                               val publickey1 = Base64.decode(s[0], Base64.DEFAULT)
                                val messageR = localmess.content
                                localmess.content = Curve25519Implementation.decryptMessage(publickey1,Base64.decode(getSecretKey()!!, Base64.DEFAULT),messageR!!)
                                writedata(localmess)
                            }
                            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                                Toast.makeText(this@MainActivity,t.toString(),Toast.LENGTH_LONG).show()
                            }

                        }
                    )


//                    }
//                    Toast.makeText(this,mes.toString(), Toast.LENGTH_SHORT).show()

                }

            }

        }

        signup.setOnClickListener{

                signFrontent()

        }
        login.setOnClickListener{

            loginFrontent()
        }
        signupB.setOnClickListener{
            var usernameEntered : String = usernameS.text.toString()
            var pass1 : String = passwordS1.text.toString()
            var pass2 : String = passwordS2.text.toString()

            if(usernameEntered != "" && pass1 != "" &&pass2!==""&&pass1==pass2) {
                addUserToDatabase()

            }

        }
        loginB.setOnClickListener{
            var usernameEntered : String = username.text.toString()
            var pass1 : String = password.text.toString()
            if(usernameEntered != "" && pass1 != ""){
                validUser(usernameEntered,pass1)
            }
        }
    }
    private fun addUserToDatabase() {
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        val userInfo = userDataItem(id=5, password = passwordS1.text.toString(), username = usernameS.text.toString())

        retrofit.addUser(userInfo).enqueue(
            object : Callback<Boolean>{

                override fun onResponse(
                    call: Call<Boolean>,
                    retrofit: Response<Boolean>
                ) {
                    val s = retrofit.body()!!.toString()
                    if(s=="true") {
                        ////////////////////////////


                        insertPublicKey()


                    }
                    else{
                        Toast.makeText(this@MainActivity,"User name already taken",Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(this@MainActivity,t.toString(),Toast.LENGTH_LONG).show()
                }

            }
        )

    }
    private fun insertPublicKey() {
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        val userInfo = userDataItem(id=5, password = passwordS1.text.toString(), username = usernameS.text.toString())
        var publickeycredential=PublicKeyCredential(usernameS.text.toString(),Base64.encodeToString(keyData.publicKey, Base64.DEFAULT))
           retrofit.insertKey(publickeycredential).enqueue(
            object : Callback<Boolean>{
                override fun onResponse(
                    call: Call<Boolean>,
                    retrofit: Response<Boolean>
                ) {
                   val s = retrofit.body()!!.toString()
                  if(s=="true") {
                      username11 = userInfo.username
                      mSocket.emit("setup",username11)

                      SharedPreferencesSaveData()
                      val intent = Intent(this@MainActivity, users::class.java)
                      // start your next activity
                      intent.putExtra("user_name", userInfo.username)

                      startActivity(intent)
                      passwordS1.text = ""
                      passwordS2.text = ""
                      usernameS.text = ""
                  }
                    else{
                      Toast.makeText(this@MainActivity,"error",Toast.LENGTH_LONG).show()
                  }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(this@MainActivity,t.toString(),Toast.LENGTH_LONG).show()
                }

            }
        )

    }
    private fun validUser(username1 : String, password1 : String){
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        val userdata = RegisterData(username1,password1)

        retrofit.userExists(userdata).enqueue(
            object : Callback<Boolean>{

                override fun onResponse(
                    call: Call<Boolean>,
                    retrofit: Response<Boolean>
                ) {
                    val s = retrofit.body()!!.toString()
//                    Toast.makeText(this@MainActivity,s,Toast.LENGTH_LONG).show()
                    if(s=="true"){
                        username11 = username1

                        mSocket.emit("setup",username11)

                        //////////////////////////////////////////////////////////////
                        val intent = Intent(this@MainActivity, users::class.java)
                        // start your next activity
                        intent.putExtra("user_name",username1)

                        startActivity(intent)
                        username.text = ""
                        password.text=""
                    }
                    else
                    Toast.makeText(this@MainActivity,"Invalid username or password",Toast.LENGTH_LONG).show()

                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(this@MainActivity,t.toString(),Toast.LENGTH_LONG).show()
                }

            }
        )
    }
    private fun initializeVariable(){
        signup = findViewById(R.id.signup)
        login = findViewById(R.id.login)
        signuplayout = findViewById(R.id.signupLayout)
        loginlayout = findViewById(R.id.loginLayout)
        loginB = findViewById(R.id.loginB)
        signupB = findViewById(R.id.signupB)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        usernameS = findViewById(R.id.usernameS)
        passwordS1 = findViewById(R.id.passwordS1)
        passwordS2 = findViewById(R.id.passwordS2)
        viewModel =  ViewModelProviders.of(this).get(ViewModel::class.java)
    }
    private fun signFrontent(){
        signup.background = resources.getDrawable(R.drawable.switch_trcks,null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            signup.setTextColor(resources.getColor(R.color.textColor,null))
        }
        login.background = null
        signuplayout.visibility = View.VISIBLE
        loginlayout.visibility = View.GONE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            login.setTextColor(resources.getColor(R.color.color,null))
        }
    }
    private fun loginFrontent(){
        signup.background = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            signup.setTextColor(resources.getColor(R.color.color,null))
        }
        login.background = resources.getDrawable(R.drawable.switch_trcks,null)
        signuplayout.visibility = View.GONE
        loginlayout.visibility = View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            login.setTextColor(resources.getColor(R.color.textColor,null))
        }

    }

    private fun writedata(localmess : localMessages){

        viewModel.insert(localmess)

    }
     private fun SharedPreferencesSaveData(){
         val sharedPreferences = getSharedPreferences("KeyData",Context.MODE_PRIVATE)
         val editor = sharedPreferences.edit()
//         Log.d("anil",keyData.privateKey.toString())
         editor.apply{
             putString("publicKey", Base64.encodeToString(keyData.publicKey, Base64.DEFAULT))
             putString("SecretKey",Base64.encodeToString(keyData.privateKey, Base64.DEFAULT))
         }.apply()
     }

 private fun getSecretKey() : String?{
        val sharedPreferences = getSharedPreferences("KeyData",Context.MODE_PRIVATE)
        val SecretKey : String? = sharedPreferences.getString("SecretKey",null)
        return SecretKey
    }

}