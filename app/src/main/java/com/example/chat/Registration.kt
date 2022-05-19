package com.example.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chat.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


lateinit var binding: ActivityRegistrationBinding
class Registration : AppCompatActivity() {
    var email = ""
    var password = ""
    var name = ""
    var secondname = ""
    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title="Register"

        auth= FirebaseAuth.getInstance()
    }


    fun register(view: View) = with(binding){
        email=binding.regEmail.text.toString()
        password=binding.regPass.text.toString()
        name = binding.regName.text.toString()
        secondname = binding.regSecondName.text.toString()
        val text = "Заполните поля E-mail и пароль!"
        if (email == "" || password == "" || name =="" || secondname =="") {

            Toast.makeText(applicationContext,text, Toast.LENGTH_LONG).show()
        }


        else{
//            val to: String = binding.regEmail.getText().toString()
//            val message = (100000..999999).random().toString()
//
//
//
//            val email = Intent(Intent.ACTION_SEND)
//            email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
//            email.putExtra(Intent.EXTRA_SUBJECT, "Одноразовый ключ")
//            email.putExtra(Intent.EXTRA_TEXT, "Здравствуйте "+ name + " " + secondname+"Вот ват ключ\n"+ message)
//
//            //для того чтобы запросить email клиент устанавливаем тип
//
//            //для того чтобы запросить email клиент устанавливаем тип
//            email.type = "message/rfc822"
//
//            startActivity(Intent.createChooser(email, to))

//                val intent = Intent(this@Registration, MainActivity::class.java)
//                startActivity(intent)

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task ->


                val user = FirebaseAuth.getInstance().currentUser

                val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name+" "+secondname).build()

                user!!.updateProfile(profileUpdates)

            if(task.isSuccessful){

                val intent= Intent(this@Registration,  MainActivity::class.java)
                startActivity(intent)
                finish()
            }



        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
        }}

    fun goToLogin(view: View){
        val intent= Intent(this,NewSignIn::class.java)
        startActivity(intent)
    }
}