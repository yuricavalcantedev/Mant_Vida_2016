package com.example.alysson.mantvida2016;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


/**
 * Created by Yuri on 27/11/2015.
 */


public class Settings extends AppCompatActivity{

    SharedPreferences sp_settings;
    SharedPreferences.Editor editor;
    Context ctx = this;
    EditText alarm1,alarm2;
    boolean enterDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_settings);

        //Toolbar da Activity
        Toolbar tb_settings = (Toolbar) findViewById(R.id.tb_settings);
        setSupportActionBar(tb_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb_settings.setSubtitle("Configurações");
        tb_settings.setSubtitleTextColor(Color.WHITE);

        sp_settings = getSharedPreferences("SettingsPreferences", Context.MODE_PRIVATE);
        editor = sp_settings.edit();

        // <!---  Lógica dos Alarmes --->
        //Alarme 1

        Switch switch_alarme1 = (Switch)findViewById(R.id.switch_alarme1);
        alarm1 = (EditText)findViewById(R.id.et_alarme1);

        if(sp_settings.getBoolean("SwitchAlarm1", true)){
            switch_alarme1.setChecked(true);
            alarm1.setEnabled(true);
        }

        alarm1.setText(sp_settings.getString("ValueAlarm1", "07:00"));
        alarm1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){

                    int minutes = Integer.parseInt(alarm1.getText().toString().substring(3));
                    int hours = Integer.parseInt(alarm1.getText().toString().substring(0,2));

                    if(minutes >= 60) {
                        if (hours < 10)
                            alarm1.setText("0" + hours + ":" + 59);
                        else
                            alarm1.setText(hours + ":" + 59);
                    }


                }
            }
        });

        //Listener do Switch do Alarme 1
        switch_alarme1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    alarm1.setEnabled(true);
            }
        });

        //Alarme 2

        Switch switch_alarme2 = (Switch)findViewById(R.id.switch_alarme2);
        alarm2 = (EditText)findViewById(R.id.et_alarme2);

        if(sp_settings.getBoolean("SwitchAlarm2",false)){
            switch_alarme2.setChecked(true);
            alarm2.setEnabled(true);
        }

        alarm2.setText(sp_settings.getString("ValueAlarm2","22:00"));
        alarm2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    int minutes = Integer.parseInt(alarm2.getText().toString().substring(3));
                    int hours = Integer.parseInt(alarm2.getText().toString().substring(0, 2));

                    if (minutes >= 60) {
                        if (hours < 10)
                            alarm2.setText("0" + hours + ":" + 59);
                        else
                            alarm2.setText(hours + ":" + 59);
                    }


                }
            }
        });


        //Listener do Switch do Alarme 2
        switch_alarme2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    alarm2.setEnabled(true);
            }
        });

        Switch switch_reading = (Switch) findViewById(R.id.switch_reading);

        /*Configurações do Modo Noturno da leitura*/
        if (sp_settings.getBoolean("ModoNoturno", false))
            switch_reading.setChecked(true);

        switch_reading.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked)
                    editor.putBoolean("ModoNoturno", true);
                else
                    editor.putBoolean("ModoNoturno", false);

                editor.commit();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();
        }else if(id == R.id.saveSettings) {

            //Se o retorno for true é porque a hora foi alterada, assim sendo, cancelo a antiga pending intent e chamo de novo "AlarmReceiver"
            //Com isso, ele vai criar uma nova intent, agora com o novo horário.
            if(saveSettingsAlarm()){

                cancelPreviousIntent();
                sendBroadcast(new Intent(this, AlarmReceiver.class));
                Toast.makeText(this,"Alerta definido com sucesso.",Toast.LENGTH_SHORT).show();
            }

        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Infla o menu do devocional.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);

    }

    private boolean saveSettingsAlarm(){

            //<!--- Salva a preferência do horário do usuário e se ele quer o alarme ou não --->
            alarm1 = (EditText)findViewById(R.id.et_alarme1);


            Switch switch_alarme1 = (Switch)findViewById(R.id.switch_alarme1);

            SharedPreferences.Editor editor = sp_settings.edit();

            //Compara o horário do alarme antigo com o novo horário.
            //Se forem diferentes, eu cancelo a pending intent do alarme antigo e lanço uma nova.

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ctx);
            databaseAccess.open();
            String[] horario = databaseAccess.getTimeAlarm();
            databaseAccess.close();

            //The hour default.
            if (horario[0] == null || horario[0].equals("0")) {

                horario[0] = "07";
                horario[1] = "00";
            }

            //DataBase's hour
            int hour = Integer.parseInt(horario[0]);
            int minute = Integer.parseInt(horario[1]);


            //Activity's hour
           try{

               String[] hourAlarmArray = alarm1.getText().toString().split(":");

               //Se entrar nesse if, é porque um dos dois deu problema, ou seja, o usuário colocou algum caractere em vez de número.
               //E ele vai retornar false. Ou seja, o alarme não vai ser definido.


               if(!isInt(hourAlarmArray[0]) || !isInt(hourAlarmArray[1]) || alarm1.getText().toString().contains("::") || hourAlarmArray.length >2 || hourAlarmArray.length == 0) {

                   Toast.makeText(this, "Alerta não definido. Digite apenas números para informar o horário desejado.", Toast.LENGTH_SHORT).show();
                   alarm1.setText("" + hour + ":" + minute);

               }else{
                   //Se entrar aqui é porque forneceu números que passam do valor mínimo.
                   if(Integer.parseInt(hourAlarmArray[0]) > 23 || Integer.parseInt(hourAlarmArray[1]) > 59){

                       Toast.makeText(this,"Formato de horário inválido. Forneça um horário válido (Ex.07:00).",Toast.LENGTH_SHORT).show();

                       alarm1.setText(""+hour+":"+minute);

                   }else{

                       //Se entrar aqui é porque deu tudo certo e é um novo horário de alarme.
                       if(hour != Integer.parseInt(hourAlarmArray[0]) || minute != Integer.parseInt(hourAlarmArray[1])){

                           databaseAccess.open();
                           databaseAccess.setTimeAlarm(Integer.parseInt(hourAlarmArray[0]), Integer.parseInt(hourAlarmArray[1]));
                           databaseAccess.close();

                           editor.putString("ValueAlarm1", "" + hourAlarmArray[0] + ":" + hourAlarmArray[1]);
                           editor.putBoolean("SwitchAlarm1", switch_alarme1.isChecked());

                           alarm1.setText("" + hourAlarmArray[0]+":"+hourAlarmArray[1]);

                           editor.commit();

                           return true;
                       }

                   }

               }

           }catch(Exception ex){

               Toast.makeText(this,"Alerta não definido.Informe um horário válido.",Toast.LENGTH_SHORT).show();
               alarm1.setText(""+hour+":"+minute);
               return false;
           }

        return false;
    }

    private void cancelPreviousIntent(){

        //Cancela a antiga PendingIntent porque o horário foi mudado.
        PendingIntent pi = PendingIntent.getService(this,0,new Intent(PushNotificationService.NOTIFICATION_INTENT),PendingIntent.FLAG_NO_CREATE);
        if(pi != null)
            pi.cancel();

    }

    //Testa se o número é inteiro ou não.
    public boolean isInt(String str) {
        boolean isInteger = true;

        try {
            int i = Integer.parseInt(str);
        } catch(NumberFormatException nfe) {
            isInteger = false;
        }

        return isInteger;
    }

}
