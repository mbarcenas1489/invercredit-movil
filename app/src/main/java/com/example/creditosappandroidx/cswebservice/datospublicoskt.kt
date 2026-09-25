package cswebservice

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.*
import com.example.creditosappandroidx.models.cobrador
import com.google.android.material.badge.BadgeDrawable
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import java.text.SimpleDateFormat
import java.util.*

object datospublicoskt {
  fun GetUrlServer(): String {
    return "http://" + ipServer + "/invercreditWeb_laravel8/public/";
  }


  var context_cuotas_por_cliente: Context? = null
  var progressbar: View? = null
  var entorno_prueba = false
  var monto_pendiente: Float = 0f
  var cuota_insertada: Boolean = false
  var solicitud_guardada: Boolean = false
  var nombreCobrador: String = ""
  var ipServer: String = ""
  var idCobrador: String = ""
  var nuevo_credito = solicitud_credito()
  var cli_select_new_credito: creditocliente? = null
  var cli_exitente = false

  var listacompleta = mutableListOf<creditocliente>()
  var listacuotas = mutableListOf<cuotas>()
  var lista_solicitudes = mutableListOf<solicitud_credito>()
  var texto = ""
  var badge: BadgeDrawable? = null


  var badge_credito_diario: BadgeDrawable? = null
  var badge_credito_semanal: BadgeDrawable? = null
  var badge_credito_quincenal: BadgeDrawable? = null
  var badge_credito_mensual: BadgeDrawable? = null
  var pref: SharedPreferences? = null

  /*Esta variable servira para controlar la forma en como se conecta la
  * App al servidor, un servidor local o un servidor en la nube
  * "Local" o "Remota"*/
  var conexion_server = "Local"

  var listacobradores = mutableListOf<cobrador>()

  val listaNombreCobradores = ArrayList<String>()

  //  arrayOf("RUTA 1", "RUTA 2", "RUTA 3", "RUTA 4", "ADMINISTRADOR")
  var cobradorSelectId = 1
  var diasSemanas = mutableListOf<String>(
    "vacio",
    "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"
  )

  fun obtenerIpSharedPreferences(context: Context) {
    val pref: SharedPreferences = PreferenceManager
      .getDefaultSharedPreferences(context)
    val ip = pref.getString("ip", "192.168.1.1")
    ipServer = ip.toString()
  }

  fun obtenerIdCobradorSharedPreferences(context: Context) {
    val pref: SharedPreferences = PreferenceManager
      .getDefaultSharedPreferences(context)
    val id = pref.getString("idcobrador", "1")
    idCobrador = id.toString()
  }

  fun primer_dia_semana_calendar(): Calendar {
    val cal: Calendar =
      Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0) // ! clear would not reset the hour of day !
    cal.clear(Calendar.MINUTE)
    cal.clear(Calendar.SECOND);
    cal.clear(Calendar.MILLISECOND);
    cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

    return cal;
  }

  fun primer_dia_semana1(): Date {
    val cal: Calendar =
      Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.clear(Calendar.MINUTE)
    cal.clear(Calendar.SECOND);
    cal.clear(Calendar.MILLISECOND);
    cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

    val formatter = SimpleDateFormat("yyyy/MM/dd")
    val date = formatter.format(cal.time)
    return formatter.parse(date)
  }

  fun imprimir_recibo(
    fecha: String, monto: Float, nombre_cliente: String, saldo: Float, saldoAnterior: Float, moneda: String, fechafin: String
  ) {
    if (monto == 0f)
      return;
    var signo = "C$";
    if (moneda == "Cordoba")
      signo = "C$";
    else
      signo = "$";
    if (Printooth.hasPairedPrinter()) {
      var printables = ArrayList<Printable>()

      printables.add(
        TextPrintable.Builder()
          .setText("          InverCredit S.A       ")
          .setLineSpacing(5)
          .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
          .setAlignment(DefaultPrinter.Companion.ALIGNMENT_CENTER)
          .setEmphasizedMode(DefaultPrinter.Companion.EMPHASIZED_MODE_BOLD)
          .setNewLinesAfter(1)
          .build()
      )


      printables.add(
        TextPrintable.Builder()
          .setText("Oficial de Credito")
          .setLineSpacing(5)
          .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
          .setAlignment(DefaultPrinter.Companion.ALIGNMENT_CENTER)
          .setEmphasizedMode(DefaultPrinter.Companion.EMPHASIZED_MODE_BOLD)
          .setNewLinesAfter(1)
          .build()
      )
      printables.add(
        TextPrintable.Builder()
          .setText(nombreCobrador)
          .setLineSpacing(5)
          .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
          .setAlignment(DefaultPrinter.Companion.ALIGNMENT_CENTER)
          .setEmphasizedMode(DefaultPrinter.Companion.EMPHASIZED_MODE_NORMAL)
          .setNewLinesAfter(1)
          .build()
      )

      printables.add(
        TextPrintable.Builder()
          .setText("********Copia Cliente*******")
          .setAlignment(DefaultPrinter.Companion.ALIGNMENT_CENTER)
          .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
          .setEmphasizedMode(DefaultPrinter.Companion.EMPHASIZED_MODE_BOLD)
          .setNewLinesAfter(1)
          .setLineSpacing(5)
          .build()
      )

      printables.add(
        TextPrintable.Builder()
          .setText("Cliente=>" + nombre_cliente)
          .setAlignment(DefaultPrinter.Companion.ALIGNMENT_CENTER)
          .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
          //.setEmphasizedMode(DefaultPrinter.Companion.EMPHASIZED_MODE_BOLD)
          .setNewLinesAfter(1)
          .setLineSpacing(5)
          .build()
      )
      printables.add(
        TextPrintable.Builder()
          .setText("     " + fecha + "   ")
          .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
          .setEmphasizedMode(DefaultPrinter.Companion.EMPHASIZED_MODE_BOLD)
          .setNewLinesAfter(1)
          .setLineSpacing(5)
          .build()
      )

      printables.add(
        TextPrintable.Builder()
          .setText("   Saldo Anterior      " + signo + saldoAnterior.toInt().toString())
          .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
          .setNewLinesAfter(1)
          .setLineSpacing(5)
          .build()
      )
      printables.add(
        TextPrintable.Builder()
          .setText("   Cuota              " + signo + monto.toInt().toString())
          .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
          .setNewLinesAfter(1)
          .setLineSpacing(5)
          .build()
      )
      printables.add(
        TextPrintable.Builder()
          .setText("   Saldo Actual       " + signo + saldo.toInt().toString())
          .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
          .setNewLinesAfter(1)
          .setLineSpacing(5)
          .build()
      )
      printables.add(
        TextPrintable.Builder()
          .setText("Finaliza=>       " + fechafin)
          .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
          .setNewLinesAfter(1)
          .setLineSpacing(10)
          .build()
      )

      printables.add(
        TextPrintable.Builder()
          .setText("                     ")
          .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
          .setNewLinesAfter(1)
          .build()
      )
      Printooth.printer().print(printables)
    }


  }

  fun getDateHoursNow(): String {
    val c = Calendar.getInstance()


    val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val formattedDate = df.format(c.time)
    return formattedDate
  }

  fun primer_dia_mes(): Date {
    val cal: Calendar =
      Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0) // ! clear would not reset the hour of day !
    cal.clear(Calendar.MINUTE)
    cal.clear(Calendar.SECOND);
    cal.clear(Calendar.MILLISECOND);

    cal.set(Calendar.DAY_OF_WEEK, 1);

    val formatter = SimpleDateFormat("yyyy/MM/dd")

    val date = formatter.format(cal.time)
    return formatter.parse(date)

  }

  fun ultimoDiaMes(): Date {
    val calendar_today: Calendar = Calendar.getInstance()
    calendar_today.set(
      Calendar.DAY_OF_MONTH,
      calendar_today.getActualMaximum(Calendar.DAY_OF_MONTH)
    )
    val formatter = SimpleDateFormat("yyyy/MM/dd")

    val date = formatter.format(calendar_today.time)
    return formatter.parse(date)

  }

  fun ultimo_dia_quincena(): Date {
    val calendar_today: Calendar = Calendar.getInstance()
    calendar_today.set(
      Calendar.DAY_OF_MONTH,
      calendar_today.getActualMaximum(Calendar.DAY_OF_MONTH)
    )
    val formatter = SimpleDateFormat("yyyy/MM/dd")

    val date = formatter.format(calendar_today.time)
    return formatter.parse(date)

  }

  fun primer_dia_quincena(): Date {
    val cal: Calendar =
      Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0) // ! clear would not reset the hour of day !
    cal.clear(Calendar.MINUTE)
    cal.clear(Calendar.SECOND);
    cal.clear(Calendar.MILLISECOND);
    if (cal.get(Calendar.DAY_OF_WEEK) <= 15)
      cal.set(Calendar.DAY_OF_WEEK, 1);
    else
      cal.set(Calendar.DAY_OF_WEEK, 15);

    val formatter = SimpleDateFormat("yyyy/MM/dd")

    val date = formatter.format(cal.time)
    return formatter.parse(date)
    // return cal;
  }

  fun fecha_rango_quincenal(fecha_cuota: String): Boolean {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateTime = simpleDateFormat.parse(fecha_cuota)
    val primerdia = primer_dia_quincena();
    val ultimo_dia = ultimo_dia_quincena();

    return (primerdia.equals(dateTime) || primerdia.before(dateTime)) && (ultimo_dia.after(
      dateTime
    ) || ultimo_dia.equals(dateTime))

  }

  fun getDatOfWeek(fecha: String): Int {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateTime = simpleDateFormat.parse(fecha)
    val cal: Calendar = Calendar.getInstance()
    cal.time = dateTime

    cal.set(Calendar.HOUR_OF_DAY, 0) // ! clear would not reset the hour of day !
    cal.clear(Calendar.MINUTE)
    cal.clear(Calendar.SECOND)
    cal.clear(Calendar.MILLISECOND)
    return cal.get(Calendar.DAY_OF_WEEK)
  }

  fun getDayOfTWeek(): Int {
    val cal: Calendar =
      Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0) // ! clear would not reset the hour of day !
    cal.clear(Calendar.MINUTE)
    cal.clear(Calendar.SECOND)
    cal.clear(Calendar.MILLISECOND)

    return cal.get(Calendar.DAY_OF_WEEK) - 1

  }

  fun getDayOfTMonth(): Int {
    val cal: Calendar =
      Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0) // ! clear would not reset the hour of day !
    cal.clear(Calendar.MINUTE)
    cal.clear(Calendar.SECOND)
    cal.clear(Calendar.MILLISECOND)

    // Log.e("Dia del Mes",cal.get(Calendar.DAY_OF_MONTH).toString())
    return cal.get(Calendar.DAY_OF_MONTH)


  }

  fun fechaRangoMensual(fecha_cuota: String): Boolean {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateTime = simpleDateFormat.parse(fecha_cuota)
    val primerdia = primer_dia_mes();
    val ultimo_dia = ultimoDiaMes();

    Log.e(
      simpleDateFormat.format(primerdia) + "=>" + simpleDateFormat.format(dateTime),
      simpleDateFormat.format(
        ultimo_dia
      )
    )

    return (primerdia.equals(dateTime) || primerdia.before(dateTime)) && (ultimo_dia.after(
      dateTime
    ) || ultimo_dia.equals(dateTime))

  }

  fun fecha_en_rango_semanal(fecha_cuota: String): Boolean {


    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateTime = simpleDateFormat.parse(fecha_cuota)

    // Log.e("Comparando Fech",dateTime.time.toString())
    val primerdia = primer_dia_semana1()
    val ultimodia = ultimo_dia_semana();


    return (primerdia.before(dateTime) || primerdia.compareTo(dateTime) == 0) && (ultimodia.after(
      dateTime
    ) || ultimodia.compareTo(dateTime) == 0)


  }

  fun actualizar_pendiente(monto: Float) {
    var m = monto;
    var pendiente = 0f
    datospublicos.listacuotas.forEach {
      if (m > 0 && it.pendiente > 0) {
        if (m >= it.pendiente) {
          pendiente = it.pendiente;
          it.pendiente = 0f
          it.save()
          m -= pendiente;
        } else {
          pendiente = it.pendiente;
          it.pendiente = it.pendiente - m;
          it.save()
          m -= pendiente;
        }


      }


    }

  }
  //Esta fecha esta en la semana actual

  fun compareWithToday(fecha: String): Boolean {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val datefecha = simpleDateFormat.parse(fecha)
    var dateToday = Calendar.getInstance().time;
    dateToday = removeTime(dateToday);
    return datefecha.equals(dateToday)


  }

  fun removeTime(date: Date?): Date? {
    val cal = Calendar.getInstance()
    cal.time = date
    cal[Calendar.HOUR_OF_DAY] = 0
    cal[Calendar.MINUTE] = 0
    cal[Calendar.SECOND] = 0
    cal[Calendar.MILLISECOND] = 0
    return cal.time
  }

  fun daysBetween(fecha_inicial: String, fecha_final: String): Int {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val d1 = simpleDateFormat.parse(fecha_inicial)
    val d2 = simpleDateFormat.parse(fecha_final)

    return ((d2.time - d1.time) / (1000 * 60 * 60 * 24)).toInt()
  }

  fun ultimo_dia_semana(): Date {
    var cal = primer_dia_semana_calendar()
    cal.add(Calendar.DAY_OF_YEAR, 6)

    val formatter = SimpleDateFormat("yyyy/MM/dd")
    val date = formatter.format(cal.time)
    return formatter.parse(date)

  }

  fun cargarlistacredito(context: Context, forma_pago: Int) {
    var c = crudsqlite(context)
    var crud = crudsqlite(context)
    when (forma_pago) {
      0 -> {
        listacompleta = crud.consultaTodo().toMutableList()
      }
      1 -> {
        listacompleta = crud.all_credito_by_forma_pago(forma_pago).toMutableList()
      }
      2 -> {
        listacompleta = crud.all_credito_by_forma_pago(forma_pago).toMutableList()
        if (listacompleta.size > 0) {
          var listaordenada =
            listacompleta.sortedBy { it -> it.dia_pago1 } as MutableList<creditocliente>
          listacompleta = listaordenada
        }
      }
      3 -> {
        listacompleta = crud.all_credito_by_forma_pago(forma_pago).toMutableList()
      }
      4 -> {
        listacompleta = crud.all_credito_by_forma_pago(forma_pago).toMutableList()

      }
    }
  }

  fun ObtenerCreditoPorCobrador(context: Context, forma_pago: Int) {
    var crud = crudsqlite(context)
    when (forma_pago) {
      0 -> {
        listacompleta = crud.consultaTodo().toMutableList()
      }
      1 -> {
        listacompleta = crud.all_credito_by_cobrador(forma_pago, cobradorSelectId).toMutableList()
      }
      2 -> {
        listacompleta = crud.all_credito_by_cobrador(forma_pago, cobradorSelectId).toMutableList()
        if (listacompleta.size > 0) {
          var listaordenada =
            listacompleta.sortedBy { it -> it.dia_pago1 } as MutableList<creditocliente>
          listacompleta = listaordenada
        }
      }
      3 -> {
        listacompleta = crud.all_credito_by_cobrador(forma_pago, cobradorSelectId).toMutableList()
      }
      4 -> {
        listacompleta = crud.all_credito_by_cobrador(forma_pago, cobradorSelectId).toMutableList()
      }
    }
  }

  fun reiniciar_nuevo_credito() {
    nuevo_credito = solicitud_credito()
  }

  fun cargar_todas_solicitudes(context: Context) {
    var c = crudsqlite(context)
    var crud = crudsqlite(context)
    lista_solicitudes = crud.consultaTodo_solicitudes().toMutableList()
  }

  fun obtenerNombreCobrador(context: Context) {
    val pref: SharedPreferences = PreferenceManager
      .getDefaultSharedPreferences(context)
    val cobrador = pref.getString("idcobrador", "1")
    if(cobrador?.toInt() == 505){
      nombreCobrador = "Administrador";
      return
    }
    if(listacobradores.size > 0 ) {
      var c = listacobradores.filter { it.idServer == cobrador?.toInt() }
      nombreCobrador = c[0].nombre;
    }
  }

  fun CargarCobradores(context: Context) {
    var c = crudsqlite(context)
    var crud = crudsqlite(context)
    var lista = crud.obtenerCobradores().toMutableList()
    listacobradores.clear()
    listaNombreCobradores.clear()

    for (item in lista){
      listacobradores.add(item)
      listaNombreCobradores.add(item.nombre)
    }

  }

  fun cambiar_formato(fecha: String): String {
    var f = fecha.split('-')
    var dia = f[0]
    var mes = f[1]
    var anyo = f[2]
    val meses = arrayOf(
      " ", "Enero", "Febrero", "Marzo", "Abril",
      "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Nobiembre", "Diciembre"
    )

    var mes_int = mes.toInt()
    return dia + " " + meses[mes_int] + " " + anyo

  }

  var pos_credito_select = 0;
  public fun actualizar_Badge(context: Context) {
    var crud = crudsqlite(context)
    var listacuotasdeldia = crud.ConsultaCuotasByFecha(getfecha())
    if (listacuotasdeldia != null && listacuotasdeldia.size > 0)
      badge?.number = listacuotasdeldia.size
    else
      badge?.number = 0
  }

  fun getfecha_format(): String {
    val date = Calendar.getInstance().time
    val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
    val formatedDate = formatter.format(date)
    //  root.tv_hoy_fecha.setText(formatedDate)
    return formatedDate
  }

  fun getfecha(dias: Int, c: Calendar): String {
    //val calendar = Calendar.getInstance()
    c.add(Calendar.DAY_OF_YEAR, dias)

    val year = c.get(Calendar.YEAR)

    val month = c.get(Calendar.MONTH) + 1
    val day = c.get(Calendar.DAY_OF_MONTH)
    return cambiar_formato(day.toString() + "-" + month + "-" + year)
  }

  fun getfecha_format_mysql(dias: Int, c: Calendar): String {
    //val c = Calendar.getInstance()
    c.add(Calendar.DAY_OF_YEAR, dias)
    // c.add(Calendar.DAY_OF_YEAR, dias)
    var mm = ""
    var dd = ""

    val year = c.get(Calendar.YEAR)

    val month = c.get(Calendar.MONTH) + 1

    if (month < 9)
      mm = "0" + month.toShort();
    else {
      mm = month.toString()
    }

    val day = c.get(Calendar.DAY_OF_MONTH)

    if (day < 9)
      dd = "0" + day.toString()
    else
      dd = day.toString()

    return (year.toString() + "-" + mm + "-" + dd).toString()
  }

  fun getfecha_format_mysql(): String {
    val c = Calendar.getInstance()
    //c.add(Calendar.DAY_OF_YEAR, dias)
    // c.add(Calendar.DAY_OF_YEAR, dias)
    var mm = ""
    var dd = ""

    val year = c.get(Calendar.YEAR)

    val month = c.get(Calendar.MONTH) + 1

    if (month <= 9)
      mm = "0" + month.toShort();
    else {
      mm = month.toString()
    }

    val day = c.get(Calendar.DAY_OF_MONTH)

    if (day <= 9)
      dd = "0" + day.toString()
    else
      dd = day.toString()

    return (year.toString() + "-" + mm + "-" + dd).toString()
  }

  fun getfecha(): String {
    val c = Calendar.getInstance()
    // c.add(Calendar.DAY_OF_YEAR, dias)
    var mm = ""
    var dd = ""

    val year = c.get(Calendar.YEAR)

    val month = c.get(Calendar.MONTH) + 1

    if (month <= 9)
      mm = "0" + month.toShort();

    val day = c.get(Calendar.DAY_OF_MONTH)

    if (day <= 9)
      dd = "0" + day.toString()

    return (year.toString() + "/" + mm + "/" + dd)
  }

}


