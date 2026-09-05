package com.example.creditosappandroidx.cssqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSqliteOpenHelper extends SQLiteOpenHelper {
  public AdminSqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table Cobrador(Id int,Nombre text)");
    db.execSQL("create table planPago(id int,monto real,fecha text,pagado float,pendiente float,prestamoid int )");
    db.execSQL("create table cuotas(cuentaid int,monto real,fecha text,prestamo_prestamoid int)");
    db.execSQL("create table creditocliente(prestamoid int," +
      "monto real," +
      "interes int, plazo int, fechainicial text,monto_a_pagar real,monto_pendiente real,activo int, cliente_clienteid int,cobrador_idCobrador int,clienteid int,nombre text, apellido text,cedula text,telefono text,direccion text ,orden int)");

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    db.execSQL("drop table if exists cuotas");

    db.execSQL("create table cuotas(cuentaid int,monto real,fecha text,prestamo_prestamoid int)");


    db.execSQL("drop table if exists creditocliente");
    db.execSQL("create table creditocliente(prestamoid int," +
      "monto real," +
      "interes int, plazo int, fechainicial date,monto_a_pagar real,monto_pendiente real,activo int, cliente_clienteid int,cobrador_idCobrador int,clienteid int,nombre text, apellido text,cedula text,telefono text,direccion text ,orden int)");


  }
}
