package com.example.creditosappandroidx

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.creditosappandroidx.cswebservice.csnetwork
import com.example.creditosappandroidx.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.mazenrashed.printooth.Printooth.init
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import cswebservice.datospublicoskt
import cswebservice.datospublicoskt.listaNombreCobradores


class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navigationView: BottomNavigationView
    private lateinit var navController: NavController
    private var tabLayoutSelect = 0
    private var rutaSelect = 0
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

        val tab_diario = tabLayout.getTabAt(0)
        tab_diario?.contentDescription = "0"

        datospublicoskt.badge = badge
        badge.isVisible = true
        datospublicoskt.badge_credito_diario = binding.tabLayout.getTabAt(0)!!.orCreateBadge
        datospublicoskt.badge_credito_diario!!.number = 0

        badge.number = 0
        datospublicoskt.actualizar_Badge(this)
        navigationView.setupWithNavController(navController)
        binding.tabLayout.addOnTabSelectedListener(this)
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
                if (datospublicoskt.badge_credito_diario == null) {
                    datospublicoskt.badge_credito_diario = binding.tabLayout.getTabAt(0)!!.orCreateBadge
                    datospublicoskt.badge_credito_diario!!.number = 0
                }
            }

            1 -> {
                navController.navigate(R.id.navigation_creditos_semanales)
                binding.tabLayout.getTabAt(1)!!.select()
                if (datospublicoskt.badge_credito_semanal == null) {
                    datospublicoskt.badge_credito_semanal = binding.tabLayout.getTabAt(1)!!.orCreateBadge
                    datospublicoskt.badge_credito_semanal!!.number = 0
                }
            }

            2 -> {
                navController.navigate(R.id.navigation_creditos_quincenales)
                binding.tabLayout.getTabAt(2)!!.select()

                if (datospublicoskt.badge_credito_quincenal == null) {
                    datospublicoskt.badge_credito_quincenal = binding.tabLayout.getTabAt(2)!!.orCreateBadge
                    datospublicoskt.badge_credito_quincenal!!.number = 0
                }
            }

            3 -> {
                navController.navigate(R.id.navigation_creditos_mensual)
                binding.tabLayout.getTabAt(3)!!.select()

                if (datospublicoskt.badge_credito_mensual == null) {
                    datospublicoskt.badge_credito_mensual = binding.tabLayout.getTabAt(3)!!.orCreateBadge
                    datospublicoskt.badge_credito_mensual!!.number = 0
                }
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    companion object {
        lateinit var tabLayout: TabLayout
    }
}
