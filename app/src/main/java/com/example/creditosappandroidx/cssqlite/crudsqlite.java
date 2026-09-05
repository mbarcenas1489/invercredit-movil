package com.example.creditosappandroidx.cssqlite;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.creditosappandroidx.cswebservice.creditocliente;
import com.example.creditosappandroidx.cswebservice.creditocliente_Table;
import com.example.creditosappandroidx.cswebservice.cuotas;
import com.example.creditosappandroidx.cswebservice.cuotas_Table;
import com.example.creditosappandroidx.cswebservice.moras;
import com.example.creditosappandroidx.cswebservice.moras_Table;
import com.example.creditosappandroidx.cswebservice.solicitud_credito;
import com.example.creditosappandroidx.cswebservice.solicitud_credito_Table;
import com.example.creditosappandroidx.models.cobrador;
import com.example.creditosappandroidx.models.cobrador_Table;
import com.example.creditosappandroidx.models.planPago;
import com.example.creditosappandroidx.models.planPago_Table;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.ParseException;
import java.util.ArrayList;


public class crudsqlite {
  Context context = null;
  AdminSqliteOpenHelper admin = null;
  public String NOMBREBD = "creditos";
  String TablaCreditoCliente = "creditocliente";
  String Tablacuotas = "cuotas";

  public crudsqlite(Context c) {
    FlowManager.init(new FlowConfig.Builder(c).build());
    context = c;
    admin = new AdminSqliteOpenHelper(context, NOMBREBD, null, 2);
  }
  public void Eliminar_una_solicitud(solicitud_credito solicitud) {
    SQLite.delete().from(solicitud_credito.class)
      .where(solicitud_credito_Table.prestamoid.is(solicitud.getPrestamoid()))
      .execute();
  }
  public void Eliminar_Todas_solicitudes() {
    SQLite.delete().from(solicitud_credito.class).execute();
  }
  public void Eliminar_Cuotas_sin_Credito() {
    SQLite.delete().from(cuotas.class)
      .where(cuotas_Table.prestamo_prestamoid
        .notIn(
          SQLite.select(creditocliente_Table.prestamoid).from(creditocliente.class)
        ))
      .execute();
    Toast.makeText(context, "Eliminando Todas las cuotas", Toast.LENGTH_SHORT).show();

  }


public ArrayList<creditocliente> consultaTodo(){
  ArrayList<creditocliente> array=(ArrayList<creditocliente>)SQLite.select().from(creditocliente.class)

  .queryList();
  return array;
  }

public ArrayList<creditocliente> all_credito_by_forma_pago(int formapago){
  ArrayList<creditocliente> array=(ArrayList<creditocliente>)SQLite.select().from(creditocliente.class)
  .where(creditocliente_Table.tipopago.is(formapago))
  .queryList();
  return array;
  }
public ArrayList<creditocliente> all_credito_by_cobrador(int formaPago,int cobradorId){
  ArrayList<creditocliente> array=(ArrayList<creditocliente>)SQLite.select().from(creditocliente.class)
  .where(creditocliente_Table.tipopago.is(formaPago)).and(creditocliente_Table.cobrador_idCobrador.is(cobradorId))
  .queryList();
  return array;
  }

public ArrayList<solicitud_credito> consultaTodo_solicitudes(){
  ArrayList<solicitud_credito> array=(ArrayList<solicitud_credito>)SQLite.select().from(solicitud_credito.class).queryList();
  return array;
  }


public void Eliminarcreditoclinte(creditocliente credito){
  SQLite.delete().from(cuotas.class).where(cuotas_Table.prestamo_prestamoid.is(credito.getPrestamoid())).execute();
  credito.delete();
  }

public void EliminarCuota(cuotas c){
  c.delete();
  }

public void Eliminar_Todas_cuotas(){
  SQLite.delete().from(cuotas.class)
  .execute();
  }

public void EliminarMoras(){
  SQLite.delete().from(moras.class)
  .execute();
  }

public void Eliminar_Todas_Moras(){
  SQLite.delete().from(moras.class)
  .execute();
  Toast.makeText(context,"Eliminando Todas las Moras",Toast.LENGTH_SHORT).show();
  }

public void Eliminar_cuota_por_credito(creditocliente credito){
  SQLite.delete().from(cuotas.class)
  .where(cuotas_Table.prestamo_prestamoid.is(credito.getPrestamoid())).execute();
  }

public void Eliminar_Moras_por_credito(creditocliente credito){
  SQLite.delete().from(moras.class)
  .where(moras_Table.idprestamo.is(credito.getPrestamoid())).execute();
  }


public int ConsultaByID(int idprestamo){
  creditocliente credito=null;

  ArrayList<creditocliente> listacredito=
  (ArrayList<creditocliente>)SQLite.select().
  from(creditocliente.class).where(creditocliente_Table.prestamoid.is(idprestamo))
  .queryList();
  if(listacredito!=null){
  if(listacredito.size()>0)
  return 1;
  else
  return 0;
  }
  return 0;
  }

public creditocliente get_credito_cliente(int idprestamo){
  creditocliente credito=null;

  ArrayList<creditocliente> listacredito=
  (ArrayList<creditocliente>)SQLite.select().
  from(creditocliente.class).where(creditocliente_Table.prestamoid.is(idprestamo))
  .queryList();
  if(listacredito!=null){
  if(listacredito.size()>0)
  return listacredito.get(0);
  else
  return null;
  }
  return null;
  }

public int CantidadCredito_inBD(){

  ArrayList<creditocliente> lista=
  (ArrayList<creditocliente>)SQLite.select().from(creditocliente.class).queryList();
  return lista.size();
  }

public int CantidadCuotasinBD()throws ParseException{
  ArrayList<cuotas> lista=
  (ArrayList<cuotas>)SQLite.select().from(cuotas.class).queryList();
  return lista.size();
  }

public ArrayList<cuotas> consultacuotaByPrestamoID(int prestamoid){
  ArrayList<cuotas> listafiltrada=new ArrayList<cuotas>();
  ArrayList<cuotas> lista=
  (ArrayList<cuotas>)SQLite.select().from(cuotas.class)
  .orderBy(cuotas_Table.id,false)
  .queryList();
  if(lista!=null&&lista.size()>0){
  for(int i=0;i<lista.size();i++){
  if(lista.get(i).getPrestamo_prestamoid()==prestamoid)
  listafiltrada.add(lista.get(i));
  }
  }
  return listafiltrada;
  }

public ArrayList<creditocliente> get_creditos_malos(){
  ArrayList<creditocliente> listamalos=new ArrayList<creditocliente>();
  ArrayList<creditocliente> lista=
  (ArrayList<creditocliente>)SQLite.select().from(creditocliente.class).queryList();
  if(lista!=null&&lista.size()>0){
  for(int i=0;i<lista.size();i++){
  if(lista.get(i).getMalo()==1)
  listamalos.add(lista.get(i));
  }
  }
  return listamalos;
  }


public cuotas Get_Ultima_Cuota(int prestamoid){
  ArrayList<cuotas> listafiltrada=new ArrayList<cuotas>();
  cuotas cuota=null;
  ArrayList<cuotas> lista=
  (ArrayList<cuotas>)SQLite.select().from(cuotas.class).queryList();

  if(lista!=null&&lista.size()>0){
  for(int i=0;i<lista.size();i++){
  if(lista.get(i).getPrestamo_prestamoid()==prestamoid)
  cuota=lista.get(i);
  }
  }
  return cuota;
  }

public ArrayList<moras> consultaMORAByPrestamoID(int prestamoid){
  ArrayList<moras> listafiltrada=new ArrayList<moras>();
  ArrayList<moras> lista=
  (ArrayList<moras>)SQLite.select().from(moras.class)
  .orderBy(moras_Table.id,false)
  .queryList();
  if(lista!=null&&lista.size()>0){
  for(int i=0;i<lista.size();i++){
  if(lista.get(i).getIdprestamo()==prestamoid)
  listafiltrada.add(lista.get(i));
  }
  }
  return listafiltrada;
  }

public float totalcuota_by_idprestamo(int idprestamo){
  ArrayList<cuotas> listamora=consultacuotaByPrestamoID(idprestamo);
  float montomora=0;
  if(listamora.size()==0){
  return 0;
  }else{
  for(int i=0;i<listamora.size();i++){
  if(listamora.get(i).getMora()==0){
  montomora+=listamora.get(i).getMonto();
  }
  }
  return montomora;
  }
  }


public float totalMora_by_idprestamo(int idprestamo){
  ArrayList<moras> listamora=consultaMORAByPrestamoID(idprestamo);
  float montomora=0;
  if(listamora.size()==0){
  return 0;
  }else{
  for(int i=0;i<listamora.size();i++){
  montomora+=listamora.get(i).getMonto();
  }
  return montomora;
  }
  }

public ArrayList<cuotas> Cuotas_by_prestamo_SinMora(int prestamoid){
  ArrayList<cuotas> listafiltrada=new ArrayList<cuotas>();
  ArrayList<cuotas> lista=
  (ArrayList<cuotas>)SQLite.select().from(cuotas.class).queryList();
  if(lista!=null&&lista.size()>0){
  for(int i=0;i<lista.size();i++){
  if(lista.get(i).getPrestamo_prestamoid()==prestamoid&&lista.get(i).getMora()==0)
  listafiltrada.add(lista.get(i));
  }
  }
  return listafiltrada;
  }

public ArrayList<moras> Cuotas_by_prestamo_Mora(int prestamoid){
  ArrayList<moras> listafiltrada=new ArrayList<moras>();
  ArrayList<moras> lista=
  (ArrayList<moras>)SQLite.select().from(moras.class).queryList();
  if(lista!=null&&lista.size()>0){
  for(int i=0;i<lista.size();i++){
  if(lista.get(i).getIdprestamo()==prestamoid)
  listafiltrada.add(lista.get(i));
  }
  }
  return listafiltrada;

  }

public float total_moras_by_idprestamo(int prestamoid){
  float monto=0;
  ArrayList<moras> lista=
  (ArrayList<moras>)SQLite.select().from(moras.class).queryList();
  if(lista!=null&&lista.size()>0){
  for(int i=0;i<lista.size();i++){
  if(lista.get(i).getIdprestamo()==prestamoid)
  monto+=lista.get(i).getMonto();
  }
  }
  return monto;

  }

public ArrayList<cuotas> ConsultaTodas_CUOTAS(){
  ArrayList<cuotas> array=(ArrayList<cuotas>)SQLite.select().from(cuotas.class).queryList();
  return array;
  }

public ArrayList<cuotas> consultaCuotasNuevas(){
  ArrayList<cuotas> array=(ArrayList<cuotas>)SQLite.select().from(cuotas.class).where(cuotas_Table.cuentaid.is(0)).queryList();
  return array;
  }

public ArrayList<moras> ConsultaTodas_Moras(){
  ArrayList<moras> array=(ArrayList<moras>)SQLite.select().from(moras.class).queryList();
  return array;
  }


public ArrayList<cuotas> ConsultaCuotasByFecha(String fechaactual){

  fechaactual=fechaactual.replace('-','/');
  ArrayList<cuotas> lista=(ArrayList<cuotas>)SQLite.select()
  .from(cuotas.class)
  .queryList();

  ArrayList<cuotas> listafiltrada=new ArrayList<cuotas>();
  for(int j=lista.size()-1;j>=0;j--){

  if(fechaactual.trim().equalsIgnoreCase(lista.get(j).getfechaFormat().trim())){
  listafiltrada.add(lista.get(j));
  }
  }
  return listafiltrada;
  }

public ArrayList<cuotas> ConsultaCuotasByFecha_malos(String fechaactual){

  fechaactual=fechaactual.replace('-','/');
  ArrayList<cuotas> lista=(ArrayList<cuotas>)SQLite.select()
  .from(cuotas.class)
  .queryList();

  ArrayList<cuotas> listafiltrada=new ArrayList<cuotas>();

  for(int j=lista.size()-1;j>=0;j--){
  if(fechaactual.trim().equalsIgnoreCase(lista.get(j).getfechaFormat().trim())){

  creditocliente cre=get_credito_cliente(lista.get(j).getPrestamo_prestamoid());
  if(cre!=null&&cre.getMalo()==1){
  listafiltrada.add(lista.get(j));
  }
  }
  }
  Toast.makeText(context,"Cantidad de fechas encontradas:"+String.valueOf(listafiltrada.size()),Toast.LENGTH_SHORT).show();
  return listafiltrada;
  }

public ArrayList<cuotas> ConsultaCuotasByFecha_buenos(String fechaactual){

  fechaactual=fechaactual.replace('-','/');
  // ArrayList<cuotas> lista=(ArrayList<cuotas>) SQLite.select().from(cuotas.class).queryList();
  ArrayList<cuotas> lista=(ArrayList<cuotas>)SQLite.select()
  .from(cuotas.class)
  //.where(cuotas_Table.fecha.is(fechaactual))
  .queryList();

  ArrayList<cuotas> listafiltrada=new ArrayList<cuotas>();
  // Toast.makeText(context,"Fecha a buscar"+fechaactual,Toast.LENGTH_SHORT).show();
  //Toast.makeText(context,"Fechas encontradas==="+String.valueOf(lista.size()),Toast.LENGTH_SHORT).show();

  for(int j=lista.size()-1;j>=0;j--){
  //  Toast.makeText(context,fechaactual+" === "+lista.get(j).getfechaFormat(),Toast.LENGTH_SHORT).show();

  if(fechaactual.trim().equalsIgnoreCase(lista.get(j).getfechaFormat().trim())){
  //    Toast.makeText(context,"Fecha encontrada"+lista.get(j).getFecha(),Toast.LENGTH_SHORT).show();

  creditocliente cre=get_credito_cliente(lista.get(j).getPrestamo_prestamoid());
  if(cre!=null&&cre.getMalo()==0){
  listafiltrada.add(lista.get(j));
  }

  }
  }
  Toast.makeText(context,"Cantidad de fechas encontradas:"+String.valueOf(listafiltrada.size()),Toast.LENGTH_SHORT).show();


  return listafiltrada;
  }

public ArrayList<moras> ConsultaMorasByFecha(String fechaactual){

  fechaactual=fechaactual.replace('-','/');
  // ArrayList<cuotas> lista=(ArrayList<cuotas>) SQLite.select().from(cuotas.class).queryList();
  ArrayList<moras> lista=(ArrayList<moras>)SQLite.select()
  .from(moras.class)
  //.where(moras_Table.fecha.is(fechaactual))
  .queryList();

  ArrayList<moras> listafiltrada=new ArrayList<moras>();
  // Toast.makeText(context,"Fecha a buscar"+fechaactual,Toast.LENGTH_SHORT).show();
  //Toast.makeText(context,"Fechas encontradas==="+String.valueOf(lista.size()),Toast.LENGTH_SHORT).show();

  for(int j=lista.size()-1;j>=0;j--){
  //  Toast.makeText(context,fechaactual+" === "+lista.get(j).getfechaFormat(),Toast.LENGTH_SHORT).show();

  if(fechaactual.trim().equalsIgnoreCase(lista.get(j).getfechaFormat().trim())){
  //    Toast.makeText(context,"Fecha encontrada"+lista.get(j).getFecha(),Toast.LENGTH_SHORT).show();

  listafiltrada.add(lista.get(j));
  }
  }
  Toast.makeText(context,"Moras Encontradas:"+String.valueOf(listafiltrada.size()),Toast.LENGTH_SHORT).show();


  return listafiltrada;
  }

public ArrayList<planPago> obtenerPlanesPagoPorPrestamId(int prestamoid){
  ArrayList<planPago> listafiltrada=new ArrayList<planPago>();
  ArrayList<planPago> lista=
  (ArrayList<planPago>)SQLite.select().from(planPago.class)
  .orderBy(planPago_Table.id,false)
  .queryList();
  if(lista!=null&&lista.size()>0){
  for(int i=0;i<lista.size();i++){
  if(lista.get(i).prestamoid==prestamoid)
  listafiltrada.add(lista.get(i));
  }
  }
  return listafiltrada;
  }

public ArrayList<cobrador> obtenerCobradores(){
  return
  (ArrayList<cobrador>)SQLite.select().from(cobrador.class)
  .queryList();
  }

public void DeleteAllCobradores(){
  SQLite.delete().from(cobrador.class).execute();
  }
public void EliminarPlanesPago(){
  SQLite.delete().from(planPago.class).execute();
  }

  }
