package com.example.alysson.mantvida2016;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Yuri on 01/12/2015.
 */

public class Devotional extends AppCompatActivity implements TextWatcher{

    String leitura;
    String parasha;
    int day, month;
    private android.support.v7.widget.ShareActionProvider mShareActionProvider;
    boolean auxBoolToSharedAction = false;

    @Override
    protected void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_devotional);

        //Pega a intent passada para pegar algumas informações.
        Intent intentParametros = getIntent();
        Bundle bundleParametros = intentParametros.getExtras();

        //Inserção dos dados vindos da intent, nas variáveis abaixo.
        leitura = bundleParametros.getString("Leitura");
        day = bundleParametros.getInt("day");
        month = bundleParametros.getInt("month");
        parasha = bundleParametros.getString("Parasha");

        //Seta o título e o sub-título (leitura do dia) na toolbar.
        Toolbar tb_devotional = (Toolbar) findViewById(R.id.tb_devotional);
        tb_devotional.setTitle("Devocional");
        tb_devotional.setSubtitle(leitura);

        //Seta o botão de voltar na toolbar e infla o menu com os icones de salvar e compartilhar.
        setSupportActionBar(tb_devotional);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb_devotional.inflateMenu(R.menu.menu_devocional);


        //Seta nas textviews abaixo a parashá da semana e a leitura do dia.
        TextView tv_parasha = (TextView) findViewById(R.id.tv_parasha_devotional);
        TextView tv_leitura = (TextView) findViewById(R.id.tv_leitura_diaria_devotional);

        tv_parasha.setText(parasha);
        tv_leitura.setText(leitura);

        //Pegar as textViews e carrega o devocional se tiver (SharedPreferences).
        EditText et_titulo = (EditText) findViewById(R.id.et_titulo_devotional);
        EditText et_versiculo_memorizar = (EditText) findViewById(R.id.et_versiculo_memorizar);
        EditText et_aprendizado = (EditText) findViewById(R.id.et_aprendizado_devotional);
        EditText et_mensagemDeDeus = (EditText) findViewById(R.id.et_mensagemDeDeus_devotional);

        //Seta nos EditTexts abaixo, o listener de quando o texto for mudado.
        et_mensagemDeDeus.addTextChangedListener(this);
        et_aprendizado.addTextChangedListener(this);


        //Shared Preference
        //Seta nas variaveis abaixo os textos de alguns campos do devocional já feito( se já tiver sido feito)
        //se não seta o valor vazio.

        SharedPreferences sharedPreferences = getSharedPreferences("Mes" + month + "Dia" + day, Context.MODE_PRIVATE);
        et_aprendizado.setText(sharedPreferences.getString("aprendizado", ""));
        et_mensagemDeDeus.setText(sharedPreferences.getString("mensagemDeDeus", ""));
        et_versiculo_memorizar.setText(sharedPreferences.getString("versiculo", ""));
        et_titulo.setText(sharedPreferences.getString("titulo", ""));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();

        } else if (id == R.id.share) {

        } else if (id == R.id.save) {

            //Mapeia os campos abaixo e armazena os valores nas variáveis abaixo.
            EditText et_titulo = (EditText) findViewById(R.id.et_titulo_devotional);
            EditText et_aprendizado = (EditText) findViewById(R.id.et_aprendizado_devotional);
            EditText et_mensagemDeDeus = (EditText) findViewById(R.id.et_mensagemDeDeus_devotional);
            EditText et_versiculo = (EditText) findViewById(R.id.et_versiculo_memorizar);

            String titulo = et_titulo.getText().toString();
            String aprendizado = et_aprendizado.getText().toString();
            String mensagemDeDeus = et_mensagemDeDeus.getText().toString();
            String versiculoMemorizar = et_versiculo.getText().toString();

            //Chama o método que salva o devocional passando como parâmetro as variáveis acima.
            saveDevotional(titulo, aprendizado, mensagemDeDeus, versiculoMemorizar);
            Toast.makeText(this, "Devocional salvo com sucesso", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Infla o menu do devocional.
        getMenuInflater().inflate(R.menu.menu_devocional, menu);

        //Deixa a variável auxiliar como verdadeira, sem isso, vai dar erro no método "afterTextChanged".
        auxBoolToSharedAction = true;

        //"item" recebe o item do menu de compartilhamento.
        MenuItem item = menu.findItem(R.id.share);

        //NOTA: Método "contentShareIntent" responsável por colocar os valores na intent.
        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);

        //O primeiro conteúdo do sharedIntent é o conteúdo salvo do devocional.
        SharedPreferences sharedPreferences = getSharedPreferences("Mes" + month + "Dia" + day, Context.MODE_PRIVATE);
        mShareActionProvider.setShareIntent(contentShareIntent(sharedPreferences.getString("titulo", ""), sharedPreferences.getString("aprendizado", ""),
                sharedPreferences.getString("mensagemDeDeus", ""), sharedPreferences.getString("versiculo", "")));


        //Sem esse método, vai dar erro.
        mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
            @Override
            public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    //Secondarys Methods.
    private void saveDevotional(String titulo, String aprendizado, String mensagemDeDeus, String versiculoMemorizar) {

        //Apenas guarda no SharedPreferences os novos valores do mesmo.

        //NOTA : cada dia tem seu sharedPreference
        SharedPreferences sharedPreferences = getSharedPreferences("Mes" + month + "Dia" + day, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("titulo", titulo);
        editor.putString("aprendizado", aprendizado);
        editor.putString("mensagemDeDeus", mensagemDeDeus);
        editor.putString("versiculo", versiculoMemorizar);


        //Salva as informações.
        editor.commit();

    }

    private Intent contentShareIntent(String titulo, String aprendizado, String mensagemDeDeus, String versiculoMemorizar) {

        //Objeto que será retornado preenchido com as informações do devocional.
        Intent sharedIntent = new Intent(Intent.ACTION_SEND);

        //Faz a mensagem do devocional.
        String msgDevocional = "Devocional - " + titulo+ " - MANT VIDA \n\n";
        msgDevocional += "Escondi a tua Palavra no meu coração para não pecar contra Ti. (Sl 119:11)\n\n";
        msgDevocional += "Parashá da semana: \n" + parasha + "\n\n";
        msgDevocional += "Leitura Diária : \n" + leitura + "\n";
        msgDevocional += "Versículo a memorizar:\n" + versiculoMemorizar + "\n\n";
        msgDevocional += "O que aprendi:\n" + aprendizado + "\n\n";
        msgDevocional += "Mensagem de Deus para mim:\n" + mensagemDeDeus + "\n\n";

        //seta o tipo da intent e o seu conteúdo.
        sharedIntent.setType("text/plain");
        sharedIntent.putExtra(Intent.EXTRA_TEXT, msgDevocional);

        return sharedIntent;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        //ignore
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        //ignore
    }

    @Override
    public void afterTextChanged(Editable s) {

        //Sem esse if, vai dar erro porque começa como false.
        if(auxBoolToSharedAction) {

            //Mapeia os campos abaixo e armazena os valores nas variáveis abaixo.
            EditText et_titulo = (EditText) findViewById(R.id.et_titulo_devotional);
            EditText et_aprendizado = (EditText) findViewById(R.id.et_aprendizado_devotional);
            EditText et_mensagemDeDeus = (EditText) findViewById(R.id.et_mensagemDeDeus_devotional);
            EditText et_versiculo = (EditText) findViewById(R.id.et_versiculo_memorizar);

            //Guarda os textos das TextViews nas variáveis abaixo.
            String titulo = et_titulo.getText().toString();
            String aprendizado = et_aprendizado.getText().toString();
            String mensagemDeDeus = et_mensagemDeDeus.getText().toString();
            String versiculoMemorizar = et_versiculo.getText().toString();

            //Seta o novo valor do ActionProvider.
            mShareActionProvider.setShareIntent(contentShareIntent(titulo, aprendizado, mensagemDeDeus, versiculoMemorizar));
        }
    }

}