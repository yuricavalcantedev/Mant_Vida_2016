package com.example.alysson.mantvida2016;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class Main extends AppCompatActivity {

    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Só vai chamar o receiver aqui apenas uma vez.
        SharedPreferences SPAlarmReceiver = getSharedPreferences("SPAlarmReceiver",Context.MODE_PRIVATE);
        if(!SPAlarmReceiver.getBoolean("FirstTimeEnter",false)){

            //Chama a class do BroadCastReceiver
            sendBroadcast(new Intent(this, AlarmReceiver.class));

            SharedPreferences.Editor editor = SPAlarmReceiver.edit();
            editor.putBoolean("FirstTimeEnter",true);
            editor.commit();


        }
    }

    // ----- Methods Calleds by XML File ----- //

    public void callLifeProjectActivityMain(View view){

        startActivity(new Intent(ctx, ProjetoVida.class));
    }

    public void callReadingPlainActivityMain(View view){

        startActivity(new Intent(ctx, PlanoLeitura.class));
    }

    public void callAboutActivityMain(View view){

        startActivity(new Intent(ctx, Sobre.class));
    }

    public void callSettingsActivityMain(View view){

        startActivity(new Intent(ctx,Settings.class));
    }



    public void callContactsActivityMain(View view){

        startActivity(new Intent(ctx, Contato.class));
    }

    public void callSiteActivityMain(View view){

        String url = "http://www.icantmaraba.com.br/site2014/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        startActivity(intent);



    }

}