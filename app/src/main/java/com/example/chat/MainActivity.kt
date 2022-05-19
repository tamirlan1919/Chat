package com.example.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_registration.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    lateinit var adapter: UserAdapter


    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setUpIcon()
        setContentView(binding.root)
        // Write a message to the database
        // Write a message to the database
        val database = FirebaseDatabase.getInstance("https://mychat-c6ef4-default-rtdb.firebaseio.com")
        val myRef = database.getReference("message")

        binding.bSend.setOnClickListener {
            if (binding.editMessage.getText().toString()!=""){
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                var currentDate = sdf.format(Date())
                currentDate = currentDate.replace("/",".")
                var name = auth.currentUser?.displayName
                if(name==null)name="Anonim"


                myRef.child(myRef.push().key ?: "Grozny")
                    .setValue(User(name+" #"+auth.currentUser?.uid.toString().substring(0,6)+"\n"+currentDate, binding.editMessage.text.toString()))
                binding.editMessage.setText("")
                if (adapter.itemCount>0) {
                    binding.rcView.smoothScrollToPosition(adapter.itemCount - 1)

                }

            }

//        binding.bEmoji.setOnClickListener{
//            emojicon.ShowEmojIcon()
//        }
        }




        onChanchgeListener(myRef)//На каком пути будем слушать, будем слушать на пути myRef
        initRcView()

    }

    private fun initRcView() = with(binding){
        adapter = UserAdapter()
        rcView.layoutManager = LinearLayoutManager(this@MainActivity)
        rcView.adapter = adapter




    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sign_out){
            auth.signOut()
            val i = Intent(this, NewSignIn::class.java)
            startActivity(i)

        }
        else if(item.itemId ==R.id.delete_chat){
            val database = FirebaseDatabase.getInstance("https://mychat-c6ef4-default-rtdb.firebaseio.com")
            val myRef = database.getReference("message")
            myRef.removeValue()
            val list = ArrayList<User>()
            list.clear()
            adapter.submitList(list)

        }
        else if(item.itemId ==R.id.settings){

            val i = Intent(this, Settings::class.java)
            startActivity(i)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun onChanchgeListener(dRef:DatabaseReference){ //Добавлние в textview что написали
        dRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<User>()
                for(s in snapshot.children){
                    val user = s.getValue(User::class.java)
                    //user?.id.toString().replace(user?.id.toString(),auth.currentUser?.displayName + " #" + auth.currentUser?.uid.toString().subSequence(0,6))
                    //user?.id = auth.currentUser?.displayName+ " #" + auth.currentUser?.uid.toString().substring(0,6)
                    if(user!=null) {

                             list.add(user)
                    }
                }
                adapter.submitList(list)
                if(adapter.itemCount>0) {
                    binding.rcView.smoothScrollToPosition(adapter.itemCount - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })//Один раз добавить
    }

    private fun setUpIcon(){
        val ab = supportActionBar
        Thread{
            val bMap = Picasso.get().load(auth.currentUser?.photoUrl).get()
            val dIcon = BitmapDrawable(resources, bMap)
            runOnUiThread{
                ab?.setDisplayHomeAsUpEnabled(true)
                ab?.setHomeAsUpIndicator(dIcon)
                ab?.title = "RSUE CHAT"
            }

        }.start()
    }
}