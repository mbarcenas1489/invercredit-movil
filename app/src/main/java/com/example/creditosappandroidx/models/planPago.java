package com.example.creditosappandroidx.models;

import com.example.creditosappandroidx.cssqlite.dbprestamo;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = dbprestamo.class)
public class planPago extends BaseModel {
  @Column
  @PrimaryKey(autoincrement = true)
  public int id;

  @Column
  public String fecha_pago;

  @Column
  public float monto;

  @Column
  public float pagado;

  @Column
  public float pendiente;

  @Column
  public Integer prestamoid;
}
