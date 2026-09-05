package com.example.creditosappandroidx.cswebservice;


import com.example.creditosappandroidx.cssqlite.dbprestamo;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


@Table(database = dbprestamo.class)
public class moras extends BaseModel
{
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column
    @PrimaryKey(autoincrement=true)
    private int id;

    @Column
    private int idmora;


    @Column
    private float monto;

    public Date getFechahora() {
        return fechahora;
    }

    public void setFechahora(Date fechahora) {
        this.fechahora = fechahora;
    }

    @Column
    private Date fechahora;

    public int getIdmora() {
        return idmora;
    }

    public void setIdmora(int idmora) {
        this.idmora = idmora;
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

    public int getIdprestamo() {
        return idprestamo;
    }

    public void setIdprestamo(int idprestamo) {
        this.idprestamo = idprestamo;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }
    public String getfechaFormat()
    {
        return fecha.replace('-','/');
    }
    @Column
    private String fecha;
    @Column
    private int idprestamo;

    public creditocliente getprestamo()
    {
        ArrayList<creditocliente> listcredito=(ArrayList<creditocliente>) SQLite.select().from(creditocliente.class)
                .where(creditocliente_Table.prestamoid.is(getIdprestamo())).queryList();
        return  listcredito.get(0);
    }
    @Column
    private float saldo;
    public Date obtenerfecha()
    {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format1.parse(getFecha());
        } catch (ParseException e) {
            e.printStackTrace();
        }

      return  date;
    }
}
