package com.example.creditosappandroidx.cssqlite;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.FlowCursor;

@Migration(version = 6, database = dbprestamo.class)
public class EstadoEnvioCuotasMigration extends BaseMigration {
  @Override
  public void migrate(DatabaseWrapper database) {
    agregarColumna(database, "enviada");
    agregarColumna(database, "envioSinConfirmar");
  }

  private void agregarColumna(DatabaseWrapper database, String columna) {
    // DBFlow también puede ejecutar migraciones después de crear tablas nuevas.
    try (FlowCursor cursor = database.rawQuery("PRAGMA table_info(cuotas)", null)) {
      while (cursor.moveToNext()) {
        if (columna.equals(cursor.getString(cursor.getColumnIndexOrThrow("name")))) return;
      }
    }
    database.execSQL("ALTER TABLE cuotas ADD COLUMN " + columna + " INTEGER NOT NULL DEFAULT 0");
  }
}
