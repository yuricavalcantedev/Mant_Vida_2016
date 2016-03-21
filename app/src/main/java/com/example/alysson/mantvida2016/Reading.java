package com.example.alysson.mantvida2016;


import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Reading extends AppCompatActivity {


    private String reference;
    private String[] biblical_texto;
    private int readingPosition = 0;
    private ImageButton btPreviousReading;
    private ImageButton btNextReading;
    private ListView lv;
    private int qntdLeituraDia;
    private Bundle parametrosPassados;
    private TextView tvTituloLeitura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitura_plano_leitura);

        cancelNotificationIfNeeded(getIntent());


        Calendar calendar  = Calendar.getInstance();

        //pega os valores dos textos bíblicos que foram passados como referência.
        parametrosPassados = getIntent().getExtras();

        SharedPreferences sharedPrefsLeitura = getSharedPreferences(SelectMonthName(calendar.get(Calendar.MONTH) + 1)+ "Leitura", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefsLeitura.edit();

        editor.putInt("Leitura" + calendar.get(Calendar.DAY_OF_MONTH), 1);
        editor.commit();


        reference = parametrosPassados.getString("titulo" + readingPosition);
        biblical_texto = parametrosPassados.getStringArray("textoBiblico" + readingPosition);
        qntdLeituraDia = parametrosPassados.getInt("qntdLeituras");

        //Mapeia os botões de avançar ou voltar leitura.
        btPreviousReading = (ImageButton) findViewById(R.id.buttonPreviousReading);
        btNextReading = (ImageButton) findViewById(R.id.buttonNextReading);


        tvTituloLeitura = (TextView) findViewById(R.id.tv_tituloLeituraCalendar);
        tvTituloLeitura.setText(reference);

        //Esse botão deve iniciar sem estar habilitado.
        btPreviousReading.setEnabled(false);

        //Se a leitura for só uma, eu deixo os botões invisíveis e inabilítados
        if (qntdLeituraDia == 1) {

            btNextReading.setEnabled(false);
            btNextReading.setVisibility(View.INVISIBLE);
        }


        ArrayAdapter listViewAdapter = new ArrayAdapter(getBaseContext(), R.layout.itens_plano_leitura, biblical_texto);
        lv = (ListView) findViewById(R.id.lv_leitura);

        //MODO NOTURNO LEITURA -
        SharedPreferences sp_settings = getSharedPreferences("SettingsPreferences", Context.MODE_PRIVATE);
        if (sp_settings.getBoolean("ModoNoturno", false)) {

            listViewAdapter = new ArrayAdapter(getBaseContext(), R.layout.itens_plano_leitura_mn, biblical_texto);
            lv.setBackgroundColor(Color.BLACK);
        }

        lv.setAdapter(listViewAdapter);

        //Listener OnItemLongClickListener (Função Ctrl C)

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (parent.isSelected())
                    view.setBackgroundColor(Color.parseColor("#757575"));


                //Divide o titulo da leitura para pegar apenas a abreviação do livro.
                String[] arrayAux = tvTituloLeitura.getText().toString().split("\\.");
                String abreviacao = arrayAux[0];
                String capitulo;
                String nameBook;


                //This code block check if the book starts with number or letter.
                if (isInt(abreviacao.substring(0, 1))) {

                    nameBook = fullBookName(abreviacao.substring(0, 4));

                } else {

                    nameBook = fullBookName(abreviacao.substring(0, 2));
                }

                //Esse bloco de código serve para colocar o capítulo certo no ClipBoardManager.
                if (isInt(abreviacao.substring(3))) {
                    capitulo = abreviacao.substring(3);
                } else {
                    capitulo = abreviacao.substring(5);
                }


                String textoCopiado = nameBook + " " + capitulo + "\n" + lv.getItemAtPosition(position).toString();

                android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                ClipData clipData = ClipData.newPlainText("text", textoCopiado);
                clipboardManager.setPrimaryClip(clipData);


                Toast.makeText(getBaseContext(), "Versículo copiado", Toast.LENGTH_SHORT).show();

                return false;
            }
        });


    }

    // ---- Methods calleds by xml file.
    public void callDevotionalActivityReading(View view){

        Intent intentDevotional = new Intent(getBaseContext(), Devotional.class);
        intentDevotional.putExtra("Leitura",parametrosPassados.getString("Leitura"));
        intentDevotional.putExtra("Parasha",parametrosPassados.getString("Parasha"));
        intentDevotional.putExtra("day",    parametrosPassados.getInt("day"));
        intentDevotional.putExtra("month",  parametrosPassados.getInt("month"));

        startActivity(intentDevotional);

    }

    public void listenerPreviousReading(View view){

        readingPosition--;

        btNextReading.setEnabled(true);
        btNextReading.setVisibility(View.VISIBLE);

        if (readingPosition == 0) {
            btPreviousReading.setEnabled(false);
            btPreviousReading.setVisibility(View.INVISIBLE);
        }

        reference = parametrosPassados.getString("titulo" + readingPosition);
        biblical_texto = parametrosPassados.getStringArray("textoBiblico" + readingPosition);

        ArrayAdapter listViewAdapter = new ArrayAdapter(getBaseContext(), R.layout.itens_plano_leitura, biblical_texto);

        //MODO NOTURNO LEITURA -
        SharedPreferences sp_settings = getSharedPreferences("SettingsPreferences", Context.MODE_PRIVATE);
        if (sp_settings.getBoolean("ModoNoturno", false)) {

            listViewAdapter = new ArrayAdapter(getBaseContext(), R.layout.itens_plano_leitura_mn, biblical_texto);
            lv.setBackgroundColor(Color.BLACK);
        }

        lv.invalidateViews();
        listViewAdapter.notifyDataSetChanged();
        lv = (ListView) findViewById(R.id.lv_leitura);
        lv.setAdapter(listViewAdapter);

        tvTituloLeitura.setText(reference);

    }

    public void listenerNextReading(View view){

        btPreviousReading.setVisibility(View.VISIBLE);
        btPreviousReading.setEnabled(true);

        readingPosition++;
        if (readingPosition < qntdLeituraDia - 1) {

            btNextReading.setVisibility(View.VISIBLE);
            btNextReading.setEnabled(true);
        } else {
            btNextReading.setEnabled(false);
            btNextReading.setVisibility(View.INVISIBLE);

        }

        reference = parametrosPassados.getString("titulo" + readingPosition);
        biblical_texto = parametrosPassados.getStringArray("textoBiblico" + readingPosition);

        ArrayAdapter listViewAdapter = new ArrayAdapter(getBaseContext(), R.layout.itens_plano_leitura, biblical_texto);

        //MODO NOTURNO LEITURA -
        SharedPreferences sp_settings = getSharedPreferences("SettingsPreferences", Context.MODE_PRIVATE);
        if (sp_settings.getBoolean("ModoNoturno", false)) {

            listViewAdapter = new ArrayAdapter(getBaseContext(), R.layout.itens_plano_leitura_mn, biblical_texto);
            lv.setBackgroundColor(Color.BLACK);
            View viewLayoutButton = findViewById(R.id.buttonLayout);
            viewLayoutButton.setBackgroundColor(Color.BLACK);

        }

        lv.invalidateViews();
        listViewAdapter.notifyDataSetChanged();
        lv = (ListView) findViewById(R.id.lv_leitura);
        lv.setAdapter(listViewAdapter);

        tvTituloLeitura.setText(reference);

    }

    // --

    //Secondarys methods
    private String fullBookName(String abrev) {

        if (abrev.equals("Gn"))
            return "Gênesis";
        if (abrev.equals("Ex"))
            return "Êxodo";
        if (abrev.equals("Lv"))
            return "Levítico";
        if (abrev.equals("Nm"))
            return "Números";
        if (abrev.equals("Dt"))
            return "Deuteronômio";
        if (abrev.equals("Js"))
            return "Josué";
        if (abrev.equals("Jz"))
            return "Juízes";
        if (abrev.equals("Rt"))
            return "Rute";
        if (abrev.equals("1Sm"))
            return "1 Samuel";
        if (abrev.equals("2Sm"))
            return "2 Samuel";
        if (abrev.equals("1Rs"))
            return "1 Reis";
        if (abrev.equals("2Rs"))
            return "2 Reis";
        if (abrev.equals("1Cr"))
            return "1 Crônicas";
        if (abrev.equals("2Cr"))
            return "2 Crônicas";
        if (abrev.equals("Ed"))
            return "Esdras";
        if (abrev.equals("Ne"))
            return "Neemias";
        if (abrev.equals("Es"))
            return "Ester";
        if (abrev.equals("Jó"))
            return "Jó";
        if (abrev.equals("Sl"))
            return "Salmos";
        if (abrev.equals("Pv"))
            return "Provérbios";
        if (abrev.equals("Ec"))
            return "Eclesiastes";
        if (abrev.equals("Ct"))
            return "Cantares de Salomão";
        if (abrev.equals("Is"))
            return "Isaías";
        if (abrev.equals("Jr"))
            return "Jeremias";
        if (abrev.equals("Lm"))
            return "Lamentações de Jeremias";
        if (abrev.equals("Ez"))
            return "Ezequiel";
        if (abrev.equals("Dn"))
            return "Daniel";
        if (abrev.equals("Os"))
            return "Oséias";
        if (abrev.equals("Jl"))
            return "Joel";
        if (abrev.equals("Am"))
            return "Amós";
        if (abrev.equals("Ob"))
            return "Obadias";
        if (abrev.equals("Jn"))
            return "Jonas";
        if (abrev.equals("Mq"))
            return "Miquéias";
        if (abrev.equals("Na"))
            return "Naum";
        if (abrev.equals("Hc"))
            return "Habacuque";
        if (abrev.equals("Sf"))
            return "Sofonias";
        if (abrev.equals("Ag"))
            return "Ageu";
        if (abrev.equals("Zc"))
            return "Zacarias";
        if (abrev.equals("Ml"))
            return "Malaquias";
        if (abrev.equals("Mt"))
            return "Mateus";
        if (abrev.equals("Mr"))
            return "Marcos";
        if (abrev.equals("Lc"))
            return "Lucas";
        if (abrev.equals("Jo"))
            return "João";
        if (abrev.equals("At"))
            return "Atos dos Apostólos";
        if (abrev.equals("Rm"))
            return "Romanos";
        if (abrev.equals("1Co"))
            return "1 Coríntios";
        if (abrev.equals("2Co"))
            return "2 Coríntios";
        if (abrev.equals("Gl"))
            return "Gálatas";
        if (abrev.equals("Ef"))
            return "Efésios";
        if (abrev.equals("Fp"))
            return "Filipenses";
        if (abrev.equals("Cl"))
            return "Colossenses";
        if (abrev.equals("1 Ts"))
            return "1 Tessalonicenses";
        if (abrev.equals("2 Ts"))
            return "2 Tessalonicenses";
        if (abrev.equals("1 Tm"))
            return "1 Timóteo";
        if (abrev.equals("2 Tm"))
            return "2 Timóteo";
        if (abrev.equals("Tt"))
            return "Tito";
        if (abrev.equals("Fm"))
            return "Filemon";
        if (abrev.equals("Hb"))
            return "Hebreus";
        if (abrev.equals("Tg"))
            return "Tiago";
        if (abrev.equals("1 Pe"))
            return "1 Pedro";
        if (abrev.equals("2 Pe"))
            return "2 Pedro";
        if (abrev.equals("1 Jo"))
            return "1 João";
        if (abrev.equals("2 Jo"))
            return "2 João";
        if (abrev.equals("3 Jo"))
            return "3 João";
        if (abrev.equals("Jd"))
            return "Judas";
        if (abrev.equals("Ap"))
            return "Apocalipse";

        return "";
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

    private void cancelNotificationIfNeeded(Intent it){

        if(it.hasExtra(PushNotificationService.FROM_NOTIFICATION_EXTRA_KEY)){

            NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nm.cancel(R.mipmap.lg_icant72x72);

        }

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

}