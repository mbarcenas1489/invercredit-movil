package com.example.creditosappandroidx.actividades;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.creditosappandroidx.R;

import java.util.Calendar;

public final   class  DatePickerDialogTheme1 extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);

        return datepickerdialog;
    }

    public final  void onDateSet(DatePicker view, int year, int month, int day){

        EditText textview = (EditText) getActivity().findViewById( R.id.et_fechaRecaudacion);

        if((month+1)<10)
        {
            if(day<10)
            textview.setText( year+"/0"+(month+1)+"/0"+day );
            else
                textview.setText( year+"/0"+(month+1)+"/"+day );

        }

        else
        {
            if(day<10)
                textview.setText( year+"/"+(month+1)+"/0"+day );
            else
            textview.setText( year+"/"+(month+1)+"/"+day );

        }



    }
}
