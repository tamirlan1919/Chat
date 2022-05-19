package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.chat.databinding.ActivityNewSignInBinding
import com.example.chat.databinding.ActivitySigninBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class NewSignIn : AppCompatActivity() {
    lateinit var binding: ActivityNewSignInBinding
    lateinit var launcher: ActivityResultLauncher<Intent>
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try{
                val account = task.getResult(ApiException::class.java)
                if(account != null){
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException){
                Log.d("MyLog", "Api exception")
            }
        }
        binding.bSignin.setOnClickListener{


                signInWithGoogle()

        }
        //checkAuth()

    }

    fun login(view: View)=with(binding) {
        val email= etEmail.text.toString()
        val password= etPassword.text.toString()
        val text = "Заполните поля E-mail и Password!"

        if(email == "" || password == "")
        {
            Toast.makeText(applicationContext,text, Toast.LENGTH_LONG).show()
        }
        else{auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent= Intent(this@NewSignIn,  MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }}
    }

    fun goToRegister(view: View){
        val intent= Intent(this,Registration::class.java)
        startActivity(intent)
    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this,gso)

    }

    private fun signInWithGoogle(){
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }
    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(){
            if(it.isSuccessful) {
                Log.d("MyLog", "Google signIn done")
                checkAuth()
            }
            else {
                Log.d("MyLog", "Google signIn error")
            }
        }
    }

    private fun checkAuth(){
        if(auth.currentUser!=null){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }
}
