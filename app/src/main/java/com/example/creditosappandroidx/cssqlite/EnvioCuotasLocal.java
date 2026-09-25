package com.example.creditosappandroidx.cssqlite;

import com.example.creditosappandroidx.cswebservice.cuotas;
import com.example.creditosappandroidx.cswebservice.cuotas_Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;

import java.util.ArrayList;
import java.util.List;

public final class EnvioCuotasLocal {
  private EnvioCuotasLocal() {}

  public static long contarPendientes() {
    return SQLite.selectCountOf().from(cuotas.class)
      .where(cuotas_Table.cuentaid.is(0))
      .and(cuotas_Table.enviada.is(false))
      .and(cuotas_Table.envioSinConfirmar.is(false)).count();
  }

  public static long contarSinConfirmar() {
    return SQLite.selectCountOf().from(cuotas.class)
      .where(cuotas_Table.cuentaid.is(0))
      .and(cuotas_Table.enviada.is(false))
      .and(cuotas_Table.envioSinConfirmar.is(true)).count();
  }

  public static List<cuotas> reservarPendientes() {
    List<cuotas> lote = new ArrayList<>();
    FlowManager.getDatabase(dbprestamo.class).executeTransaction(database -> {
      lote.addAll(SQLite.select().from(cuotas.class)
        .where(cuotas_Table.cuentaid.is(0))
        .and(cuotas_Table.enviada.is(false))
        .and(cuotas_Table.envioSinConfirmar.is(false)).queryList(database));
      DatabaseStatement statement = database.compileStatement(
        "UPDATE cuotas SET envioSinConfirmar = 1 WHERE id = ? AND cuentaid = 0"
          + " AND enviada = 0 AND envioSinConfirmar = 0");
      try {
        for (cuotas cuota : lote) {
          statement.bindLong(1, cuota.getId());
          if (statement.executeUpdateDelete() != 1) {
            throw new IllegalStateException("No se pudo reservar la cuota " + cuota.getId());
          }
          cuota.envioSinConfirmar = true;
        }
      } finally {
        statement.close();
      }
    });
    return lote;
  }

  public static void confirmar(List<cuotas> lote) {
    FlowManager.getDatabase(dbprestamo.class).executeTransaction(database -> {
      DatabaseStatement statement = database.compileStatement(
        "UPDATE cuotas SET enviada = 1, envioSinConfirmar = 0 WHERE id = ?"
          + " AND cuentaid = 0 AND enviada = 0 AND envioSinConfirmar = 1");
      try {
        for (cuotas cuota : lote) {
          statement.bindLong(1, cuota.getId());
          if (statement.executeUpdateDelete() != 1) {
            throw new IllegalStateException("No se pudo confirmar la cuota " + cuota.getId());
          }
        }
      } finally {
        statement.close();
      }
    });
  }
}
