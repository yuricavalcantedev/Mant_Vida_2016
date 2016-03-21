package com.example.alysson.mantvida2016;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Yuri on 22/12/2015.
 */

public class LaterAlarmClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        cancelNotificationIfNeeded(getIntent());

        Toast.makeText(this,"Leitura adiada",Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance();

        SharedPreferences sharedPrefsLeitura = getSharedPreferences(SelectMonthName(calendar.get(Calendar.MONTH) + 1)+ "Leitura", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefsLeitura.edit();

        editor.putInt("Leitura" + calendar.get(Calendar.DAY_OF_MONTH),  -1);
        editor.commit();


    }

    private String SelectMonthName(int monthNumber) {

        String monthName;

        if (monthNumber == 1)
            monthName = "Janeiro";
        else if (monthNumber == 2)
            monthName = "Fevereiro";
        else if (monthNumber == 3)
            monthName = "Março";
        else if (monthNumber == 4)
            monthName = "Abril";
        else if (monthNumber == 5)
            monthName = "Maio";
        else if (monthNumber == 6)
            monthName = "Junho";
        else if (monthNumber == 7)
            monthName = "Julho";
        else if (monthNumber == 8)
            monthName = "Agosto";
        else if (monthNumber == 9)
            monthName = "Setembro";
        else if (monthNumber == 10)
            monthName = "Outubro";
        else if (monthNumber == 11)
            monthName = "Novembro";
        else
            monthName = "Dezembro";

        return monthName;
    }

    private void cancelNotificationIfNeeded(Intent it){

        if(it.hasExtra(PushNotificationService.FROM_NOTIFICATION_EXTRA_KEY)){

            NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nm.cancel(R.mipmap.lg_icant72x72);
        }

    }


}
