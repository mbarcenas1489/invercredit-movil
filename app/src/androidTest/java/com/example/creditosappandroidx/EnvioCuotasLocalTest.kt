package com.example.creditosappandroidx

import android.content.Context
import android.content.ContextWrapper
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.creditosappandroidx.cssqlite.EnvioCuotasLocal
import com.example.creditosappandroidx.cssqlite.EstadoEnvioCuotasMigration
import com.example.creditosappandroidx.cssqlite.dbprestamo
import com.example.creditosappandroidx.cswebservice.cuotas
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.database.AndroidDatabase
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class EnvioCuotasLocalTest {
  private lateinit var contexto: Context

  @Before
  fun prepararBaseAislada() {
    val original = InstrumentationRegistry.getInstrumentation().targetContext
    val directorio = File(original.cacheDir, "envio-test-${UUID.randomUUID()}")
    check(directorio.mkdirs())
    contexto = object : ContextWrapper(original) {
      override fun getApplicationContext(): Context = this
      override fun getDatabasePath(name: String): File = File(directorio, name)
      override fun openOrCreateDatabase(name: String, mode: Int, factory: SQLiteDatabase.CursorFactory?): SQLiteDatabase =
        SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory)
      override fun openOrCreateDatabase(name: String, mode: Int, factory: SQLiteDatabase.CursorFactory?, errorHandler: DatabaseErrorHandler?): SQLiteDatabase =
        SQLiteDatabase.openDatabase(getDatabasePath(name).path, factory, SQLiteDatabase.CREATE_IF_NECESSARY, errorHandler)
      override fun deleteDatabase(name: String): Boolean = SQLiteDatabase.deleteDatabase(getDatabasePath(name))
    }
    assertNotEquals(original.getDatabasePath("creditos.db"), contexto.getDatabasePath("creditos.db"))
    FlowManager.destroy()
    FlowManager.init(FlowConfig.Builder(contexto).build())
  }

  @After
  fun cerrar() {
    FlowManager.destroy()
  }

  private fun nueva(cuenta: Int = 0) = cuotas().apply {
    cuentaid = cuenta
    monto = 100f
    fecha = "2026-09-25"
    fechahora = "2026-09-25 12:00:00"
    assertTrue(save())
  }

  @Test
  fun soloConfirmaElLoteReservadoYConservaElEstadoAlReabrir() {
    nueva()
    nueva()
    nueva(345)
    assertEquals(2L, EnvioCuotasLocal.contarPendientes())
    val lote = EnvioCuotasLocal.reservarPendientes()
    assertEquals(2, lote.size)
    assertEquals(0L, EnvioCuotasLocal.contarPendientes())
    assertEquals(2L, EnvioCuotasLocal.contarSinConfirmar())
    val posterior = nueva()
    EnvioCuotasLocal.confirmar(lote)
    FlowManager.destroy()
    FlowManager.init(FlowConfig.Builder(contexto).build())
    assertEquals(1L, EnvioCuotasLocal.contarPendientes())
    assertEquals(0L, EnvioCuotasLocal.contarSinConfirmar())
    val siguiente = EnvioCuotasLocal.reservarPendientes()
    assertEquals(listOf(posterior.id), siguiente.map { it.id })
    assertEquals(2, SQLite.select().from(cuotas::class.java).queryList().count { it.enviada })
  }

  @Test
  fun envioInterrumpidoNoSeReenviaAlReabrir() {
    nueva()
    EnvioCuotasLocal.reservarPendientes()
    FlowManager.destroy()
    FlowManager.init(FlowConfig.Builder(contexto).build())
    assertTrue(EnvioCuotasLocal.reservarPendientes().isEmpty())
    assertEquals(1L, EnvioCuotasLocal.contarSinConfirmar())
  }

  @Test
  fun falloAlConfirmarRevierteTodoElLote() {
    nueva()
    val lote = EnvioCuotasLocal.reservarPendientes().toMutableList()
    lote.add(cuotas().apply { id = -1 })
    try {
      EnvioCuotasLocal.confirmar(lote)
      fail("Se esperaba un error al confirmar una cuota inexistente")
    } catch (_: IllegalStateException) {
      assertEquals(1L, EnvioCuotasLocal.contarSinConfirmar())
      assertFalse(SQLite.select().from(cuotas::class.java).querySingle()!!.enviada)
    }
  }

  @Test
  fun actualizaVersionCincoSinPerderCuotas() {
    // Crea la tabla previa en la base aislada y deja que DBFlow ejecute la migración registrada.
    contexto.openOrCreateDatabase("creditos.db", 0, null).use { db ->
      db.execSQL("CREATE TABLE cuotas (id INTEGER PRIMARY KEY AUTOINCREMENT, cuentaid INTEGER, monto REAL)")
      db.execSQL("INSERT INTO cuotas (cuentaid, monto) VALUES (0, 123.5), (456, 22)")
      db.version = 5
    }
    val db = FlowManager.getDatabase(dbprestamo::class.java).writableDatabase
    assertEquals(6, db.version)
    db.rawQuery("SELECT monto, enviada, envioSinConfirmar FROM cuotas ORDER BY id", null).use {
      assertEquals(2, it.count)
      assertTrue(it.moveToFirst())
      assertEquals(123.5, it.getDouble(0), 0.0)
      assertEquals(0, it.getInt(1))
      assertEquals(0, it.getInt(2))
    }
    // Una segunda ejecución no debe duplicar columnas ni borrar datos.
    EstadoEnvioCuotasMigration().migrate(db)
  }
}
