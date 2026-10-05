package com.example.creditosappandroidx.actividades;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class ActivityPreferences extends Activity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getFragmentManager().beginTransaction()
      .replace(android.R.id.content, new PreferenciaFragment())
      .commit();

  }
}
