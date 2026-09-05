package com.example.creditosappandroidx.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.creditosappandroidx.MainActivity
import com.example.creditosappandroidx.R
import com.example.creditosappandroidx.actividades.*
import com.example.creditosappandroidx.actividades.CargarDatos.CargarDatos
import com.example.creditosappandroidx.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {
  private lateinit var binding: FragmentNotificationsBinding

  private lateinit var notificationsViewModel: NotificationsViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    notificationsViewModel =
      ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_notifications, container, false)
    notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
    })
    binding = FragmentNotificationsBinding.inflate(layoutInflater)

    MainActivity.tabLayout.isVisible = false;
    binding.imCargardatos.setOnClickListener {
      var intent = Intent(root.context, CargarDatos::class.java)
      startActivity(intent)
    }
    binding.imgConfiguracion.setOnClickListener {
      var intent = Intent(root.context, ActivityPreferences::class.java)
      startActivity(intent)
    }
    binding.imgEnviarcuotas.setOnClickListener {
      val intent = Intent(root.context, activityEnvioCuotas_laravel::class.java)
      startActivity(intent)
    }
    binding.imgRecaudacion.setOnClickListener {
      val intent = Intent(root.context, Activity_Recaudacionpordia::class.java)
      startActivity(intent)
    }

    binding.imgClientebuscar.setOnClickListener {
      val intent = Intent(root.context, Activity_Buscar::class.java)
      startActivity(intent)
    }
    return binding.root
  }
}
