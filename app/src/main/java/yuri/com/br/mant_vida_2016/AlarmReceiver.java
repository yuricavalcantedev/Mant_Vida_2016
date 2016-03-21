package yuri.com.br.mant_vida_2016;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yuri on 10/12/2015.
 */


public class AlarmReceiver extends BroadcastReceiver {

    private Context myContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        myContext = context;
        schedule();
    }

    private void schedule() {

        Intent it = new Intent(PushNotificationService.NOTIFICATION_INTENT);
        PendingIntent pi = PendingIntent.getService(myContext, 0, it, PendingIntent.FLAG_NO_CREATE);

        //if "pi" equals "null" is for it isn't created. So, it will be created.
        if(pi == null){

            SharedPreferences sharedPreferencesSetting = myContext.getSharedPreferences("SettingsPreferences", Context.MODE_PRIVATE);

            //if the user has checked the alarm, is setted a new alarm.
            //else, is for he don't want the alarm, so it's not created.
            if(sharedPreferencesSetting.getBoolean("SwitchAlarm1", true)){

                pi = PendingIntent.getService(myContext, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
                setAlarm(getAlarmTime(), pi);
            }
        }

    }

    private long getAlarmTime() {

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(myContext);
        databaseAccess.open();
        String[] horario = databaseAccess.getTimeAlarm();
        databaseAccess.close();

        //The hour default.
        if (horario[0] == null) {

            horario[0] = "07";
            horario[1] = "00";
        }


        int hour = Integer.parseInt(horario[0]);
        int minute = Integer.parseInt(horario[1]);

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        long currTime = System.currentTimeMillis();

        //HERE IS THE PROBLEM!!!!
        //WHEN THE HOUR IT'S < HOUR_OF_DAY, IT START ALARM REPEAT TIMES, AT COME IN THE SAME HOUR.
        //if the time has passed, add 24 hours in the time.
        if (currentDate.get(Calendar.HOUR_OF_DAY) == hour && currentDate.get(Calendar.MINUTE) >= minute ||
            currentDate.get(Calendar.HOUR_OF_DAY) > hour){

            currTime += 24 * 60 * 60 * 1000;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currTime);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        //Return the time for "setAlarm". When this time come, the pending intent, will call "PushNotificationService".
        return calendar.getTimeInMillis();
    }

    private void setAlarm(long time, PendingIntent pi) {

        //Set the alarm in the time returned for "getAlarmTime".
        //It's will a pending intent. When the time come, a pending intent will call the "PushNotificationService".
        AlarmManager alarm = (AlarmManager) myContext.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, time, pi);
    }

}
