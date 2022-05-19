package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.chat.databinding.ActivityMainBinding
import com.example.chat.databinding.ActivityNewSignInBinding
import com.example.chat.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.comeback.setOnClickListener{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        val themeSwitch = binding.themeSwitch
        themeSwitch.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }
}