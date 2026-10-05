package com.example.creditosappandroidx

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.csnetwork
import com.example.creditosappandroidx.cswebservice.cuotas
import com.example.creditosappandroidx.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.mazenrashed.printooth.Printooth.init
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import cswebservice.datospublicoskt
import cswebservice.datospublicoskt.listaNombreCobradores
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
  private lateinit var binding: ActivityMainBinding

  private lateinit var navigationView: BottomNavigationView
  private lateinit var navController: NavController
  private var tabLayoutSelect = 0
  private var rutaSelect = 0
  private var creditBadgeJob: Job? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(binding.root)
    init(applicationContext)


    tabLayout = binding.tabLayout
    navigationView = binding.navView
    val navHostFragment = supportFragmentManager
      .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    navController = navHostFragment.navController

    FlowManager.init(FlowConfig.Builder(this).build())

    datospublicoskt.obtenerIpSharedPreferences(this);
    datospublicoskt.obtenerIdCobradorSharedPreferences(this);
    datospublicoskt.CargarCobradores(this)
    datospublicoskt.obtenerNombreCobrador(this)

    val badge = navigationView.getOrCreateBadge(R.id.navigation_home)

    tabLayout.getTabAt(0)?.contentDescription = "0"

    datospublicoskt.badge = badge
    badge.isVisible = true
    initializeCreditBadges()

    badge.number = 0
    datospublicoskt.actualizar_Badge(this)
    navigationView.setupWithNavController(navController)
    binding.tabLayout.addOnTabSelectedListener(this)
  }

  override fun onResume() {
    super.onResume()
    refreshCreditBadges()
  }

  override fun onRestart() {
    val badge = navigationView.getOrCreateBadge(R.id.navigation_home)
    datospublicoskt.badge = badge
    badge?.isVisible = true
    badge?.number = 25
    datospublicoskt.actualizar_Badge(this)
    super.onRestart()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_actionbarsearch, menu)
    return true
  }

  override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
    var menu_cobrador = menu!!.findItem(R.id.menu_cobrador_select)
    if (BuildConfig.BUILD_TYPE == "admin") {
      menu_cobrador!!.setVisible(true)
    } else {
      menu_cobrador!!.setVisible(false)
    }
    return super.onPrepareOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {

    when (item.itemId) {
      R.id.menu_wifi -> {
        csnetwork.isServerConect1(this, item);
        return true;
      }

      R.id.menu_cobrador_select -> {
        DialogSelectCobrador()
        return true;
      }
    }
    return super.onOptionsItemSelected(item)
  }

  fun DialogSelectCobrador() {

    MaterialAlertDialogBuilder(binding.root.context)
      .setTitle("Seleccione Ruta")
      .setNeutralButton("Cancelar") { dialog, which ->
      }
      .setPositiveButton("Ok") { dialog, which ->
      }
      .setSingleChoiceItems(
        listaNombreCobradores.toTypedArray(),
        rutaSelect
      ) { dialog, which ->
        datospublicoskt.cobradorSelectId = datospublicoskt.listacobradores[which].idServer
        rutaSelect = which;
        refreshCreditBadges()
        navController.navigate(R.id.navigation_creditos_diarios)
        binding.tabLayout.getTabAt(0)!!.select()
      }
      .show()
  }

  override fun onTabSelected(tab: TabLayout.Tab?) {
    when (tab!!.position) {
      0 -> {
        navController.navigate(R.id.navigation_creditos_diarios)
        binding.tabLayout.getTabAt(0)!!.select()
      }

      1 -> {
        navController.navigate(R.id.navigation_creditos_semanales)
        binding.tabLayout.getTabAt(1)!!.select()
      }

      2 -> {
        navController.navigate(R.id.navigation_creditos_quincenales)
        binding.tabLayout.getTabAt(2)!!.select()
      }

      3 -> {
        navController.navigate(R.id.navigation_creditos_mensual)
        binding.tabLayout.getTabAt(3)!!.select()
      }
    }
  }

  override fun onTabUnselected(tab: TabLayout.Tab?) {
  }

  override fun onTabReselected(tab: TabLayout.Tab?) {
  }

  private fun initializeCreditBadges() {
    repeat(4) { position ->
      binding.tabLayout.getTabAt(position)?.orCreateBadge?.isVisible = false
    }
  }

  private fun refreshCreditBadges() {
    creditBadgeJob?.cancel()
    val collectorId = datospublicoskt.cobradorSelectId

    creditBadgeJob = lifecycleScope.launch {
      val counts = withContext(Dispatchers.IO) {
        val crud = crudsqlite(applicationContext)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val lastPayments = mutableMapOf<Int, cuotas>()

        crud.ConsultaTodas_CUOTAS().forEach { payment ->
          val creditId = payment.prestamo_prestamoid
          val current = lastPayments[creditId]
          if (current == null || payment.id > current.id) {
            lastPayments[creditId] = payment
          }
        }

        CreditBadgeCounts(
          daily = pendingCreditCount(crud, 1, collectorId, today, lastPayments),
          weekly = pendingCreditCount(crud, 2, collectorId, today, lastPayments),
          biweekly = pendingCreditCount(crud, 3, collectorId, today, lastPayments),
          monthly = pendingCreditCount(crud, 4, collectorId, today, lastPayments)
        )
      }

      updateCreditBadge(0, counts.daily)
      updateCreditBadge(1, counts.weekly)
      updateCreditBadge(2, counts.biweekly)
      updateCreditBadge(3, counts.monthly)
    }
  }

  private fun updateCreditBadge(position: Int, number: Int) {
    binding.tabLayout.getTabAt(position)?.orCreateBadge?.apply {
      this.number = number
      isVisible = true
    }
  }

  private fun pendingCreditCount(
    crud: crudsqlite,
    paymentType: Int,
    collectorId: Int,
    today: String,
    lastPayments: Map<Int, cuotas>
  ): Int {
    val credits = if (BuildConfig.BUILD_TYPE == "admin") {
      crud.all_credito_by_cobrador(paymentType, collectorId)
    } else {
      crud.all_credito_by_forma_pago(paymentType)
    }

    return credits.count { credit ->
      val lastPayment = lastPayments[credit.prestamoid]
      lastPayment == null || !runCatching {
        when (paymentType) {
          1 -> lastPayment.fecha == today
          2 -> datospublicoskt.fecha_en_rango_semanal(lastPayment.fecha)
          3 -> datospublicoskt.fecha_rango_quincenal(lastPayment.fecha)
          4 -> datospublicoskt.fechaRangoMensual(lastPayment.fecha)
          else -> false
        }
      }.getOrDefault(false)
    }
  }

  private data class CreditBadgeCounts(
    val daily: Int,
    val weekly: Int,
    val biweekly: Int,
    val monthly: Int
  )

  companion object {
    lateinit var tabLayout: TabLayout
  }
}
