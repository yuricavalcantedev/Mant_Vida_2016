package com.example.alysson.mantvida2016;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class Contato extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        //Toolbar da Activity
        Toolbar tb_settings = (Toolbar) findViewById(R.id.tb_contact);
        setSupportActionBar(tb_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb_settings.setSubtitle("Contatos");
        tb_settings.setSubtitleTextColor(Color.WHITE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        return true;
    }
}
