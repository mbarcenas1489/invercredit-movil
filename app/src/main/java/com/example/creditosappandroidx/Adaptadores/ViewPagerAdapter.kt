package com.example.creditosappandroidx.Adaptadores

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.creditosappandroidx.fragment.fragment_Creditos_diarios
import com.example.creditosappandroidx.fragment.fragment_Creditos_semanales

/**
 * Created by Gastón Saillén on 30 December 2019
 */
class ViewPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    companion object{
        private const val ARG_OBJECT = "object"
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> { fragment_Creditos_diarios()}
            1 -> { fragment_Creditos_semanales() }

            else -> fragment_Creditos_semanales()
        }
    }


}