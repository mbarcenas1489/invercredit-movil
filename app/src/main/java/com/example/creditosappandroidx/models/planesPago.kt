package com.example.creditosappandroidx.models

import com.example.creditosappandroidx.cssqlite.dbprestamo
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = dbprestamo::class)
public class planesPago
  (
  @Column
  @PrimaryKey(autoincrement = true)
  var id: Int = 0,
  @Column var fecha_pago: String,
  @Column var monto: Float = 0.0f,
  @Column var pagado: Float = 0.0f,
  @Column var pendiente: Float = 0.0f,
  @Column var prestamoid: Int = 0,
) : BaseModel()


