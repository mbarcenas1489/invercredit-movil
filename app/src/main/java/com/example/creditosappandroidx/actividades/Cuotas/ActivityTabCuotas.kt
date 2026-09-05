package com.example.creditosappandroidx.actividades.Cuotas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.creditosappandroidx.actividades.Cuotas.adapter.ViewPagerAdapter
import com.example.creditosappandroidx.databinding.ActivityTabCuotasBinding
import com.google.android.material.tabs.TabLayoutMediator

val tabsArray = arrayOf(
  "Cuotas",
  "Plan de Pago",
  "Info"
)

class ActivityTabCuotas : AppCompatActivity() {
  private lateinit var binding: ActivityTabCuotasBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityTabCuotasBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    val viewPager = binding.viewPager
    val tabLayout = binding.tabLayout

    val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
    viewPager.adapter = adapter

    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
      tab.text = tabsArray[position]
    }.attach()

    supportActionBar!!.hide()
  }
}