package com.example.alysson.mantvida2016;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Alysson on 21/11/2015.
 */
public class AdapterGV_PlanoLeitura extends BaseAdapter {

    Context ctx;
    String[] itens;
    String nomeMes;
    int corLeitura = -1;
    private Calendar calendar = Calendar.getInstance();
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    int numberOfMonth;
    SharedPreferences sharedPrefsLeitura;
    String[]eventosMes = null;
    int tamEventosMes = 0;


    public AdapterGV_PlanoLeitura(Context ctx, String[] itens, String nomeMes, int numberOfMonth) {

        this.ctx = ctx;
        this.itens = itens;
        this.nomeMes = nomeMes;
        this.numberOfMonth = numberOfMonth;
        sharedPrefsLeitura = ctx.getSharedPreferences(nomeMes + "Leitura", Context.MODE_PRIVATE);
        eventosMes = chooseMonthEventArray(nomeMes);
        tamEventosMes = eventosMes.length;

    }

    @Override
    public int getCount() {
        return itens.length;
    }

    @Override
    public Object getItem(int position) {
        return itens[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /***   Menu de Cores   ***/
        /***  -1 Vermelho      ***/
        /***   0 Transparente  ***/
        /***   1 Verde / Azul  ***/

        View grid;
        LayoutInflater inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = inflater.inflate(R.layout.mobile_plano_leitura, null);
            grid.setBackgroundColor(Color.parseColor("#F5F5F5"));


            if (numberOfMonth == (calendar.get(Calendar.MONTH) + 1)) {

                if (position + 1 < dayOfMonth)

                    corLeitura = sharedPrefsLeitura.getInt("Leitura" + (position + 1), -1);
                else
                    corLeitura = sharedPrefsLeitura.getInt("Leitura" + (position + 1), 0);

            } else if (numberOfMonth < (calendar.get(Calendar.MONTH) + 1)) {

                corLeitura = sharedPrefsLeitura.getInt("Leitura" + (position + 1), -1);

            } else {

                corLeitura = sharedPrefsLeitura.getInt("Leitura" + (position + 1), 0);
            }

            TextView tv_dias = (TextView) grid.findViewById(R.id.grid_item_data);
            TextView tv_color = (TextView) grid.findViewById(R.id.grid_item_reading);
            TextView tv_evento = (TextView) grid.findViewById(R.id.grid_item_evento);
            tv_dias.setText(itens[position]);
            tv_dias.setGravity(Gravity.TOP | Gravity.RIGHT);

            if (position + 1 == dayOfMonth && (calendar.get(Calendar.MONTH) + 1) == numberOfMonth)
                tv_dias.setTextColor(Color.parseColor("#536DFE"));

            tv_color.setHeight(10);
            tv_color.setWidth(180);


            tv_color.setBackgroundColor(chooseColorReading(corLeitura));

            tv_evento.setHeight(10);
            tv_evento.setWidth(180);


            if(tamEventosMes > 1)
                for (int i = 0; i < tamEventosMes; i++)
                    if (position+1 == Integer.parseInt(eventosMes[i]))
                         tv_evento.setBackgroundColor(Color.BLUE);

        } else {
            grid = convertView;
        }

        return grid;
    }

    private int chooseColorReading(int value) {

        int returnedColor = 0;

        switch (value) {

            case -1:
                returnedColor = Color.RED;
                break;
            case 0:
                returnedColor = Color.TRANSPARENT;
                break;
            case 1:
                returnedColor = Color.GREEN;
                break;

        }

        return returnedColor;

    }

    private String[] chooseMonthEventArray(String monthName) {

        String[] arrayReturned;

        if(monthName.equals("Janeiro")){

            arrayReturned = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","12","16"};
            return arrayReturned;
        }

        if(monthName.equals("Fevereiro")){

            arrayReturned = new String[]{"07","08","09","10","27"};
            return arrayReturned;
        }
        if(monthName.equals("Março")){

            arrayReturned = new String[]{"05","24","25","26"};
            return arrayReturned;
        }
        if(monthName.equals("Abril")){

            arrayReturned = new String[]{"14","15","16","19","22","23","24"};
            return arrayReturned;
        }

        if(monthName.equals("Maio")){

            arrayReturned = new String[]{"13","14","15","27","28"};
            return arrayReturned;

        }
        if(monthName.equals("Junho")) {
            arrayReturned = new String[]{""};
            return arrayReturned;
        }
        if(monthName.equals("Julho")){
            arrayReturned = new String[]{"07","08","09"};
            return arrayReturned;
        }

        if(monthName.equals("Agosto")){
            arrayReturned = new String[]{"18","19","20","21"};
            return arrayReturned;
        }
        if(monthName.equals("Setembro")){
            arrayReturned = new String[]{"23","24","25"};
            return arrayReturned;
        }

        if(monthName.equals("Outubro")){
            arrayReturned = new String[]{"12","13","14","15","16","17","18","19","20","21","22","23","24","25"};
            return arrayReturned;
        }

        if(monthName.equals("Novembro")){
            arrayReturned = new String[]{""};
            return arrayReturned;
        }

        if(monthName.equals("Dezembro")){
            arrayReturned = new String[]{"08","09","10"};
            return arrayReturned;
        }

        return null;
    }
}


