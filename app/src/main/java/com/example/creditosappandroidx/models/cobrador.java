package com.example.creditosappandroidx.models;

import com.example.creditosappandroidx.cssqlite.dbprestamo;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = dbprestamo.class)
public class cobrador extends BaseModel {
  @Column
  @PrimaryKey(autoincrement = true)
  private int id;
  @Column
  private int IdServer;
  @Column
  private String Nombre;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getIdServer() {
    return IdServer;
  }

  public void setIdServer(int idServer) {
    IdServer = idServer;
  }

  public String getNombre() {
    return Nombre;
  }

  public void setNombre(String nombre) {
    Nombre = nombre;
  }
}
