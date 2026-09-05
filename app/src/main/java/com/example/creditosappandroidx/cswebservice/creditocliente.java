package com.example.creditosappandroidx.cswebservice;

import android.content.Context;
import android.util.Log;

import com.example.creditosappandroidx.cssqlite.crudsqlite;
import com.example.creditosappandroidx.cssqlite.dbprestamo;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


@Table(database = dbprestamo.class)
public class creditocliente extends BaseModel {
  @Column
  @PrimaryKey(autoincrement = true)
  private int id;

  @Column
  private int prestamoid;


  public int getDia_pago1() {
    return dia_pago1;
  }

  public void setDia_pago1(int dia_pago1) {
    this.dia_pago1 = dia_pago1;
  }

  public int getDia_pago2() {
    return dia_pago2;
  }

  public void setDia_pago2(int dia_pago2) {
    this.dia_pago2 = dia_pago2;
  }

  @Column
  private int dia_pago1;
  @Column
  private int dia_pago2;

  @Column
  private int cantidadCuotasAtrasadas;
  public int getCantidadCuotasAtrasadas() {
    return cantidadCuotasAtrasadas;
  }
  public void setCantidadCuotasAtrasadas(int c) {
    this.cantidadCuotasAtrasadas = c;
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }

  @Column
  private float monto;

  public String getMoneda() {
    return moneda;
  }

  public void setMoneda(String moneda) {
    this.moneda = moneda;
  }

  public boolean pago_cuota_dia = false;

  public boolean isCuota_completada() {
    return cuota_completada;
  }

  public void setCuota_completada(boolean cuota_completada) {
    this.cuota_completada = cuota_completada;
  }

  public boolean cuota_completada = false;

  public int getTipopago() {
    return tipopago;
  }

  public void setTipopago(int tipopago) {
    this.tipopago = tipopago;
  }

  public int getDia_pago() {
    return dia_pago;
  }

  public void setDia_pago(int dia_pago) {
    this.dia_pago = dia_pago;
  }

  @Column
  private int tipopago;
  @Column
  private int dia_pago;
  @Column
  private String moneda;
  @Column
  private float interes;
  @Column
  private int plazo;
  @Column
  private String fechainicial;
  @Column
  private String fechafin;

  @Column
  private String cobrador_nombre;
  public String getFechafin() {
    return fechafin;
  }

  public void setFechafin(String fechafin) {
    this.fechafin = fechafin;
  }

  @Column
  private float monto_a_pagar;

  public float getMonto_cuota() {
    return monto_cuota;
  }

  public void setMonto_cuota(float monto_cuota) {
    this.monto_cuota = monto_cuota;
  }

  @Column
  private float monto_cuota;
  @Column
  private float monto_pendiente;
  @Column
  private int activo;
  @Column
  private int cliente_clienteid;
  @Column
  private int cobrador_idCobrador;

  @Column
  private int malo;

  public int getMalo() {
    return malo;
  }

  public void setMalo(int malo) {
    this.malo = malo;
  }

  @Column
  private int clienteid;
  @Column
  private String nombre;
  @Column
  private String apellido;
  @Column
  private String cedula;
  @Column
  private String telefono;
  @Column
  private String direccion;
  @Column
  private int orden;

  @Column
  private String correo;

  public int getActivo() {
    return activo;
  }

  public String fechafin() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    try {
      Date date = formatter.parse(getFechainicial());
      c.setTime(formatter.parse(getFechainicial()));

      // int ndomingo=(getPlazo()/7);
      c.add(Calendar.DATE, getPlazo() + 4);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return formatter.format(c.getTime());
  }

  public Date obtenerfecha() {
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
      date = format1.parse(getFechainicial());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return date;
  }

  public Date obtenerfechaFin() {
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
      if (getFechafin() != null)
        date = format1.parse(getFechafin());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return date;
  }

  public int obtener_dias_dias_atrasados() {
    Date fincredito = obtenerfechaFin();
    if (fincredito == null)
      return -1;
    Date fechahoy = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //fechahoy=dateFormat.parse(fechahoy);
    int dias = 0;

    if (obtenerfechaFin() != null) {
      dias = (int) ((fechahoy.getTime() - fincredito.getTime()) / 86400000);    }
    return dias;

  }

  public int cuotaspendientes(Context context) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    int dias = 0;
    try {
      Date dateinicial = formatter.parse(getFechainicial());
      Date datehoy = new Date();

      dias = (int) ((datehoy.getTime() - dateinicial.getTime()) / 86400000);
      int ndomingo = (dias / 7);


      dias = dias - ndomingo;


    } catch (ParseException e) {
      e.printStackTrace();
    }
    if (dias <= getPlazo()) {
      return dias - cuotasabonadasreales(context);

    } else
      return getPlazo() - cuotasabonadasreales(context);


  }

  public String getfechaString() {
    String f = fechainicial;
    //f.replace('-','/');
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    if (f != null) {
      Date date = null;
      try {
        date = formatter.parse(f);
      } catch (ParseException e) {
        e.printStackTrace();
      }

      return "Fecha credito: " + formatter.format(date);
    } else
      return "No tiene";


  }

  public int cuotas_Abonadas(Context context) {
    crudsqlite crudsqlite = new crudsqlite(context);
    ArrayList<cuotas> listacuotas = null;

    listacuotas = crudsqlite.Cuotas_by_prestamo_SinMora(getPrestamoid());

    return listacuotas.size();
  }

  public int cuotasabonadasreales(Context context) {

    float abonado = getabonado(context);
    int montocuota = (int) (monto_a_pagar / getPlazo());
    int nc = (int) (abonado / montocuota);
    return nc;
  }

  public float getabonado(Context context) {
    crudsqlite crud = new crudsqlite(context);
    ArrayList<cuotas> listacuotas = null;
    float abonado = 0;

    listacuotas = crud.consultacuotaByPrestamoID(prestamoid);


    for (int i = 0; i < listacuotas.size(); i++) {
      if (listacuotas.get(i).getMora() == 0)
        abonado += listacuotas.get(i).getMonto();
    }
    return abonado;
  }

  public int getClienteid() {
    return clienteid;
  }

  public void setClienteid(int clienteid) {
    this.clienteid = clienteid;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getCedula() {
    return cedula;
  }

  public void setCedula(String cedula) {
    this.cedula = cedula;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public int getOrden() {
    return orden;
  }

  public void setOrden(int orden) {
    this.orden = orden;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }


  public int getPrestamoid() {
    return prestamoid;
  }

  public void setPrestamoid(int prestamoid) {
    this.prestamoid = prestamoid;
  }

  public float getMonto() {
    return monto;
  }

  public void setMonto(float monto) {
    this.monto = monto;
  }

  public float getInteres() {
    return interes;
  }

  public void setInteres(float interes) {
    this.interes = interes;
  }

  public int getPlazo() {
    return plazo;
  }

  public void setPlazo(int plazo) {
    this.plazo = plazo;
  }

  public String getFechainicial() {
    return fechainicial.split(" ")[0];
  }

  public void setFechainicial(String fechainicial) {
    this.fechainicial = fechainicial;
  }

  public float getMonto_a_pagar() {
    return monto_a_pagar;
  }

  public void setMonto_a_pagar(float monto_a_pagar) {
    this.monto_a_pagar = monto_a_pagar;
  }

  public float getMonto_pendiente() {
    return monto_pendiente;
  }

  public void setMonto_pendiente(float monto_pendiente) {
    this.monto_pendiente = monto_pendiente;
  }

  public int isActivo() {
    return activo;
  }

  public void setActivo(int activo) {
    this.activo = activo;
  }

  public int getCliente_clienteid() {
    return cliente_clienteid;
  }

  public void setCliente_clienteid(int cliente_clienteid) {
    this.cliente_clienteid = cliente_clienteid;
  }

  public int getCobrador_idCobrador() {
    return cobrador_idCobrador;
  }

  public void setCobrador_idCobrador(int cobrador_idCobrador) {
    this.cobrador_idCobrador = cobrador_idCobrador;
  }

  public int getCuenta_idCuenta() {
    return cuenta_idCuenta;
  }

  public void setCuenta_idCuenta(int cuenta_idCuenta) {
    this.cuenta_idCuenta = cuenta_idCuenta;
  }

  private int cuenta_idCuenta;

  public float getPendiente() {
    return pendiente;
  }

  public void setPendiente(float pendiente) {
    this.pendiente = pendiente;
  }

  @Column
  private float pendiente;

  public String getCobrador_nombre() {
    return cobrador_nombre;
  }

  public void setCobrador_nombre(String cobrador_nombre) {
    this.cobrador_nombre = cobrador_nombre;
  }
}
