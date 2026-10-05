package com.example.creditosappandroidx.actividades.Cuotas.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.creditosappandroidx.actividades.Cuotas.fragment.FragmentDetalleCredito
import com.example.creditosappandroidx.actividades.Cuotas.fragment.FragmentTabCuotas
import com.example.creditosappandroidx.actividades.Cuotas.fragment.FragmentTabPlanPago

private const val NUM_TABS = 3

public class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
  FragmentStateAdapter(fragmentManager, lifecycle) {

  override fun getItemCount(): Int {
    return NUM_TABS
  }

  override fun createFragment(position: Int): Fragment {
    when (position) {
      0 -> return FragmentTabCuotas()
      1 -> return FragmentTabPlanPago()
      2 -> return FragmentDetalleCredito()
    }
    return FragmentTabCuotas()
  }
}