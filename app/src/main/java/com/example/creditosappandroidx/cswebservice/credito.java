package com.example.creditosappandroidx.cswebservice;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class credito {
  private int prestamoid;
  private float monto;
  private float interes;
  private int plazo;
  @SerializedName("date")
  private Date fechainicial;
  private float monto_a_pagar;
  private float monto_pendiente;
  private int activo;
  private int cliente_clienteid;
  private int cobrador_idCobrador;
  private boolean malo;

  public boolean isMalo() {
    return malo;
  }

  public void setMalo(boolean malo) {
    this.malo = malo;
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

  public Date getFechainicial() {
    return fechainicial;
  }

  public void setFechainicial(Date fechainicial) {
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

}
