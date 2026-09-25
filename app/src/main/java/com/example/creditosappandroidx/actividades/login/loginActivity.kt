package com.example.creditosappandroidx.actividades.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.creditosappandroidx.cswebservice.crudWebservice_laravel
import com.example.creditosappandroidx.databinding.ActivityLoginBinding

class loginActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLoginBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)


  }

  fun btingresar_onclick(view: View) {
    val crudweb = crudWebservice_laravel(this)
    crudweb.login(binding.etemail.text.toString(), binding.etemail.text.toString(), this)
  }


}