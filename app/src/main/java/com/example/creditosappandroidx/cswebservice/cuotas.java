package com.example.creditosappandroidx.cswebservice;


import android.util.Log;

import com.example.creditosappandroidx.cssqlite.dbprestamo;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


@Table(database = dbprestamo.class)
public class cuotas extends BaseModel {
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Column
  @PrimaryKey(autoincrement = true)
  private int id;

  @Column
  private int cuentaid;

  @Column
  private float monto;

  public float getPendiente() {
    return pendiente;
  }

  public void setPendiente(float pendiente) {
    this.pendiente = pendiente;
  }

  @Column
  private float pendiente;
  @Column
  private String fecha;

  public String getFechahora() {
    return fechahora;
  }

  public void setFechahora(String fechahora) {
    this.fechahora = fechahora;
  }

  @Column
  private String fechahora;

  /* @Column
   private Date fechahora;

   public Date getFechaHora() {
       return fechahora;
   }

   public void setFechaHora(Date fechaHora) {
       this.fechahora = fechaHora;
   }*/
  @Column
  private int prestamo_prestamoid;

  @Column
  private int mora;

  @Column
  private float saldo;

  public int getMora() {
    return mora;
  }

  public void setMora(int mora) {
    this.mora = mora;
  }

  public float getSaldo() {
    return saldo;
  }

  public void setSaldo(float saldo) {
    this.saldo = saldo;
  }

  public int getnumerocuota() {
    return obtenerfecha().getMonth() + obtenerfecha().getDay();

  }

  public int obtener_dias_dias_atrasados() {
    Date fecha_ultima_cuota = obtenerfecha();
    Date fechahoy = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //fechahoy=dateFormat.parse(fechahoy);
    int dias = 0;

    if (obtenerfecha() != null) {
      dias = (int) ((fechahoy.getTime() - fecha_ultima_cuota.getTime()) / 86400000);

      Log.e("Comparando Fechas", dateFormat.format(fecha_ultima_cuota) +
        "<->" + dateFormat.format(fechahoy) + "<->" + String.valueOf(dias));
    }
    return dias;

  }

  public Date obtenerfecha() {
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
      date = format1.parse(getFecha());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return date;
  }

  public String getfechaFormat() {
    return fecha.replace('-', '/');
  }

  public int getCuentaid() {
    return cuentaid;
  }

  public void setCuentaid(int cuentaid) {
    this.cuentaid = cuentaid;
  }

  public float getMonto() {
    return monto;
  }

  public void setMonto(float monto) {
    this.monto = monto;
  }

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public int getPrestamo_prestamoid() {
    return prestamo_prestamoid;
  }

  public void setPrestamo_prestamoid(int prestamo_prestamoid) {
    this.prestamo_prestamoid = prestamo_prestamoid;
  }

  public creditocliente getprestamo() {
    ArrayList<creditocliente> listcredito = (ArrayList<creditocliente>) SQLite.select().from(creditocliente.class)
      .where(creditocliente_Table.prestamoid.is(prestamo_prestamoid)).queryList();
    if (listcredito == null || listcredito.size() == 0) {
      creditocliente lcuota = new creditocliente();
      return lcuota;
    }
    return listcredito.get(0);
  }
}
