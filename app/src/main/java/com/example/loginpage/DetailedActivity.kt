package com.example.loginpage



import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anilkumawat3104.recyclerview.message
import com.anilkumawat3104.recyclerview.messages
import com.example.loginpage.SignalProtocolImplementation.Curve25519Implementation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import io.socket.engineio.parser.Base64
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime


class DetailedActivity : AppCompatActivity() {
     lateinit var messageRecyclerView: RecyclerView
     lateinit var messageBox : EditText
     lateinit var sendButton : ImageView
      lateinit var messAdapter: message
    lateinit var messageList : ArrayList<localMessages>
    lateinit var username11 : String
    lateinit var username12 : String
    private lateinit var viewModel: ViewModel
    private lateinit var clickedUserPublicKey:ByteArray
    private lateinit var fab : FloatingActionButton
    private lateinit var date : TextView

//    private lateinit var localmess : localMessages
    val gson: Gson =Gson()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        username12  = intent.getStringExtra("name")!!
         username11 = intent.getStringExtra("user_name")!!
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        date = findViewById(R.id.date)

        var str="asjcbas"
        clickedUserPublicKey=str.toByteArray()

        getPublicKey()

//        Log.d("Clicked",clickedUserPublicLey.toString())
//         val viewModel: ViewModel by lazy {
//            return@lazy when {
//                this@DetailedActivity != null -> {
//                    ViewModelProviders.of(activity as FragmentActivity).get(ViewModel::class.java) // you can either pass activity object
//                }
//                else -> {
//                    ViewModelProviders.of(this).get(ViewModel::class.java) // or pass fragment object, both are not possible at same time.
//                }
//            }
//        }



        Log.d("rrr","12")

        messageRecyclerView = findViewById(R.id.chatRecycler)
        messageRecyclerView.setHasFixedSize(true)

        messageRecyclerView.layoutManager = LinearLayoutManager(this@DetailedActivity)


//        SocketHandler.setSocket()
//
//        val mSocket=SocketHandler.getSocket()
//
//        mSocket.connect()
//
//        var str="anil"
//        mSocket.emit("setup",str)

        val mSocket=SocketHandler.getSocket()
        messageBox = findViewById(R.id.typed_mess)
        sendButton = findViewById(R.id.send_btn)
        supportActionBar?.title = username12
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        messageList = ArrayList()
        messAdapter = message(this,username11,username12)
        messageRecyclerView.adapter = messAdapter

        messageRecyclerView.scrollToPosition(messAdapter.getItemCount()+1)
        viewModel.getmess(username11,username12).observe(this, Observer { messageList ->
            messageList?.let { messAdapter.setMessages(it)
                messageRecyclerView.smoothScrollToPosition(messAdapter.getItemCount()+1)}
        })
//        messAdapter.notifyDataSetChanged()
//        messageRecyclerView.smoothScrollToPosition(messAdapter.getItemCount()+1)
       fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            messageRecyclerView.smoothScrollToPosition(messAdapter.getItemCount()+1)
            fab.setVisibility(View.GONE)
        }
        messageRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy ==1 && fab.isShown) {
                    fab.hide()
                }
                if (dy > 10 && date.isShown) {
                    date.setVisibility(View.INVISIBLE);
                }

                if (dy < -10 && (!fab.isShown||!date.isShown)) {
                    fab.show()
                    date.setVisibility(View.VISIBLE);
                    val dateofmess = messAdapter.messageList[messAdapter.position1].timestamp?.substring(0,10)
                    if(dateofmess==LocalDateTime.now().toString().substring(0,10)){
                        date.text = "Today"
                    }
                    else
                        date.text = dateofmess
                }

                if (!recyclerView.canScrollVertically(-1)) {
                    fab.show()
                    date.setVisibility(View.VISIBLE);
                    val dateofmess = messAdapter.messageList[messAdapter.position1].timestamp?.substring(0,10)
                    if(dateofmess==LocalDateTime.now().toString().substring(0,10)){
                        date.text = "Today"
                    }
                    else
                        date.text = dateofmess

                }
            }
        })
        sendButton.setOnClickListener{
//            Toast.makeText(this,username12.toString(), Toast.LENGTH_SHORT).show()
//            val messs =
//                username?.username?.let { it1 -> messages("ayush",it1,messageBox.text.toString()) }
//
//            val ms= arrayOf("ayush",username?.username,messageBox.text.toString())
//
//            if (messs != null) {
//                messs.senderid="ayush"
//            }
//            if (username != null) {
//                if (messs != null) {
//                    messs.receiverid=username.username
//                }
//            }
//            if (messs != null) {
//                messs.mess=messageBox.text.toString()
//            }

            //Log.d("message", messs.toString())
            //var ms:messages(" "," "," ")
            if(username12!=null) {

                Log.d("hello",getSecretKey().toString())
                var privateKeyuser : ByteArray = Base64.decode(getSecretKey(), Base64.DEFAULT)
                Log.d("anil",privateKeyuser.toString())
                val encryptedMessage= Curve25519Implementation.encryptMessage(clickedUserPublicKey,
                    privateKeyuser,messageBox.text.toString())

                 val ms =  messages(username11, username12,encryptedMessage)

                if (ms != null) {
                    Log.d("encryptedmess",ms.mess.toString())
                }
                val localmess = localMessages(null,messageBox.text.toString(),username11,username12,
                    LocalDateTime.now().toString(),username12)
                val messs=gson.toJson(ms)
/////////////////////need to add time tamp in messs
                mSocket.emit("private-message",messs)
                if (messs != null) {
                    messageList.add(localmess)
                }

                writedata(localmess)
                messageBox.setText("")



            }


        }

//           mSocket.on("receive-message") { args ->
//               if (args[0] != null) {
//                   val m = args[0]
//
//                   Log.d("kkk", "9")
//                   val mes = gson.fromJson(args[0].toString(), messages::class.java)
//
//                   localmess = localMessages(
//                       null, mes.mess, mes.senderid, mes.receiverid,
//                       LocalDateTime.now().toString()
//                   )
//
////                Log.d("rrr","12")
////                writedata(localmess)
//                   runOnUiThread {
//                       if ((mes.senderid == username11 && mes.receiverid == username12) || (mes.senderid == username12 && mes.receiverid == username11)) {
//                           messageList.add(localmess)
//
////                    Log.d("xxx",mes.toString())
//                           messAdapter.notifyDataSetChanged()
//                           messageRecyclerView.smoothScrollToPosition(messAdapter.getItemCount())
//
//                           Log.d("qqq", "1")
//                           writedata(localmess)
//
//
//                       } else {
//                           Log.d("www", "1")
//                           writedata(localmess)
//
//                       }
//
////                    Toast.makeText(this,mes.toString(), Toast.LENGTH_SHORT).show()
//
//                   }
//
//               }
//
//           }


    }


    private fun writedata(localmess : localMessages){
        viewModel.insert(localmess)

//        Toast.makeText(this@MainActivity,"Successfully written",Toast.LENGTH_SHORT).show()

    }
    private fun getSecretKey() : String?{
        val sharedPreferences = getSharedPreferences("KeyData",Context.MODE_PRIVATE)
        val SecretKey : String? = sharedPreferences.getString("SecretKey",null)
        return SecretKey
    }

    private fun getPublicKey() {
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)

        Log.d("UserClicked",username12)

        val clickedUser=username(username12)
        retrofit.getPublickey(clickedUser).enqueue(
            object : Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    retrofit: Response<List<String>>
                ) {

                   val s : List<String>  = retrofit.body()!!

//                    Log.d("clickedhihi",s[0])
                    clickedUserPublicKey=Base64.decode(s[0], Base64.DEFAULT)
//                    Log.d("anilkuu",s[0].toByteArray(UTF_16LE).toString(UTF_16LE))
//                    Log.d("clickedhihihh",clickedUserPublicKey.toString(Charset.defaultCharset()))
//                    Log.d("clickedhihihh",clickedUserPublicKey.toString(Charset.defaultCharset()).toByteArray(
//                        Charset.defaultCharset()).toString(Charset.defaultCharset()))


                }
                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Toast.makeText(this@DetailedActivity,"error", Toast.LENGTH_LONG).show()
                }

            }
        )

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
    override fun onBackPressed() {
         finish()
    }

}