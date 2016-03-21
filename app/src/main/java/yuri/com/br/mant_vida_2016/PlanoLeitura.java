package yuri.com.br.mant_vida_2016;

/**
 * Created by Yuri on 21/11/2015.
 */

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class PlanoLeitura extends AppCompatActivity {

    private AlertDialog alerta;
    private int month;
    final Context ctx = this;
    public String titulo = "";
    String monthName = "";
    private Calendar calendar = Calendar.getInstance();
    private GridView gv;
    private TextView tv;
    private ImageButton previousMonth;
    private Intent intent;

    ArrayList<ClassTblLeitura> versiclesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_plano_leitura);

        versiclesArray = new ArrayList<>();

        month = calendar.get(Calendar.MONTH) + 1;

        // -----For when the user hold in "Adiar" in the daily alert. ----- //
        cancelNotificationIfNeeded(getIntent());

/************************************** SETA ADAPTER E LISTENER NO GRID VIEW ***********************************************************/

        String[] diasMes = ChooseMonthArray(month);
        monthName = SelectMonthName(month);

        tv = (TextView) findViewById(R.id.tv_tituloCalendar);
        tv.setText(monthName);

        gv = (GridView) findViewById(R.id.grid);

        final ImageButton nextMonth = (ImageButton) findViewById(R.id.buttonNextCalendarMonth);
        previousMonth = (ImageButton) findViewById(R.id.buttonPreviousCalendarMonth);

        if (month == 12) {
            nextMonth.setVisibility(View.INVISIBLE);
            nextMonth.setEnabled(false);
        } else if (month == 1) {
            previousMonth.setVisibility(View.INVISIBLE);
            previousMonth.setEnabled(false);

        }


        final TextView tv_parasha = (TextView) findViewById(R.id.tv_parasha);
        tv_parasha.setText(selectParashaName(calendar.get(Calendar.WEEK_OF_YEAR)));


        AdapterGV_PlanoLeitura adapterGridView = new AdapterGV_PlanoLeitura(getBaseContext(), diasMes, monthName, month);
        gv.setAdapter(adapterGridView);

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int monthAux = month + 1;
                String[] dias = ChooseMonthArray(monthAux);
                String nameMonth = SelectMonthName(monthAux);
                monthName = nameMonth;

                refreshGridView(dias, nameMonth, monthAux);

                tv.setText(nameMonth);

                if (monthAux > 0 && monthAux < 12) {
                    nextMonth.setEnabled(true);
                    nextMonth.setVisibility(View.VISIBLE);
                    previousMonth.setEnabled(true);
                    previousMonth.setVisibility(View.VISIBLE);

                } else {
                    nextMonth.setEnabled(false);
                    nextMonth.setVisibility(View.INVISIBLE);
                }

                month = monthAux;
            }
        });

        previousMonth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int monthAux = month - 1;
                String[] dias = ChooseMonthArray(monthAux);
                String nameMonth = SelectMonthName(monthAux);
                monthName = nameMonth;

                refreshGridView(dias, nameMonth, monthAux);
                tv.setText(nameMonth);


                if (monthAux > 1) {
                    previousMonth.setEnabled(true);
                    previousMonth.setVisibility(View.VISIBLE);
                    nextMonth.setEnabled(true);
                    nextMonth.setVisibility(View.VISIBLE);
                } else {

                    previousMonth.setEnabled(false);
                    previousMonth.setVisibility(View.INVISIBLE);
                }

                month = monthAux;

            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**************************** SEMPRE QUE UM ITEM DO GRID VIEW FOR CLICADO VAI SER CHAMADO ESSE MÉTODO **********************************/
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                intent = new Intent(view.getContext(), Reading.class);

                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ctx);
                databaseAccess.open();
                versiclesArray = databaseAccess.buscarTextosDia(changeNameToNumberMonth(monthName), position + 1);
                String[] eventos = databaseAccess.getEventos(month, position + 1).split(";");
                databaseAccess.close();

                //Se não setar o titulo como vazio toda vida ele vai ficar acumulando sempre que apertar algum item do gridView.
                titulo = "";

                int countForAux = 0;
                int tamanhoArrayVersicles = versiclesArray.size();

                //Passo pela intent, todos os títulos e textos que vinheram do banco de dados.
                while (tamanhoArrayVersicles > 0) {

                    String[] versiclesSplit = versiclesArray.get(countForAux).getTextoBiblico().split("%");

                    intent.putExtra("titulo" + countForAux, versiclesArray.get(countForAux).getTitulo());
                    intent.putExtra("textoBiblico" + countForAux, versiclesSplit);

                    countForAux++;
                    tamanhoArrayVersicles--;

                }

/************* CONSTRUTOR DO ALERT DIALOG QUE VAI APARECER SEMPRE QUE O USUÁRIO CLICAR EM UM DIA DO GRIDVIEW (CALENDÁRIO) **************/
                AlertDialog.Builder construtorDialog = new AlertDialog.Builder(ctx);
                construtorDialog.setTitle(tv.getText().toString() + " - Dia " + (position + 1));

                //if para resolver o problema da leitura do dia 2 de janeiro e também do dia 31 de dezembro.
                if(countForAux == 4){

                    if(versiclesArray.get(1).getTitulo().contains("Gn 48.1-22")) {
                        versiclesArray.get(1).setTitulo("Gn 48.1-22");
                    }
                }

                for (int i = 0; i < countForAux; i++) {

                    titulo += versiclesArray.get(i).getTitulo() + "\n ";

                }

                String setMessageDialog = "";

                //Texto do evento colocado aqui.
                if (eventos[0] != "") {

                    setMessageDialog = "Evento do dia: " + "\n";
                    if (eventos.length == 1)

                        setMessageDialog += eventos[0] + "\n";

                    else
                        setMessageDialog += eventos[0] + "\n" + eventos[1] + "\n";
                }

                construtorDialog.setMessage(setMessageDialog + "Leitura do dia : " + titulo);

                //Coloca na intent o número de leitura que exitem no dia.
                //Serve para a lógica dos botões.
                intent.putExtra("qntdLeituras", countForAux);

/****************************************************** LER BUTTON *********************************************************************/
                construtorDialog.setPositiveButton("Ler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences sharedPrefsLeitura = getSharedPreferences(tv.getText().toString() + "Leitura", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefsLeitura.edit();

                        editor.putInt("Leitura" + (position + 1), 1);
                        editor.commit();

                        String[] dias = ChooseMonthArray(month);
                        String nameMonth = SelectMonthName(month);

                        refreshGridView(dias, nameMonth, month);

                        //Passa esses dados para a activity da leitura, de lá, vai ser passado para a activity do devocional.

                        intent.putExtra("Leitura", titulo);
                        intent.putExtra("Parasha", tv_parasha.getText().toString());
                        intent.putExtra("day", position + 1);
                        intent.putExtra("month", month);

                        //abre uma nova activity com a leitura do dia e seta a cor verde no textview da leitura.
                        startActivity(intent);

                    }
                });

/****************************************************** ADIAR BUTTON *******************************************************************/
                construtorDialog.setNegativeButton("Adiar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences sharedPrefsLeitura = getSharedPreferences(tv.getText().toString() + "Leitura", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefsLeitura.edit();

                        editor.putInt("Leitura" + (position + 1), -1);
                        editor.commit();


                        String[] dias = ChooseMonthArray(month);
                        String nameMonth = SelectMonthName(month);

                        refreshGridView(dias, nameMonth, month);

                        dialog.cancel();
                        //Seta a cor vermelha no arquivo correspondente ao dia e mes da leitura.
                    }
                });

/****************************************************** CANCEL BUTTON ****************************************************************/
                construtorDialog.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
/****************************************************** CRIA E MOSTRA O ALERT DIALOG ***************************************************/
                alerta = construtorDialog.create();
                alerta.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }


    /******************************************************
     * MÉTODOS AUXLIARES
     ******************************************************/

    private int changeNameToNumberMonth(String name) {

        if (name.equals("Janeiro"))
            return 1;
        if (name.equals("Fevereiro"))
            return 2;
        if (name.equals("Março"))
            return 3;
        if (name.equals("Abril"))
            return 4;
        if (name.equals("Maio"))
            return 5;
        if (name.equals("Junho"))
            return 6;
        if (name.equals("Julho"))
            return 7;
        if (name.equals("Agosto"))
            return 8;
        if (name.equals("Setembro"))
            return 9;
        if (name.equals("Outubro"))
            return 10;
        if (name.equals("Novembro"))
            return 11;
        if (name.equals("Dezembro"))
            return 12;

        return 0;
    }

    private void refreshGridView(String[] arrayDias, String nomeMes, int numberMonth) {

        AdapterGV_PlanoLeitura adapterGridView = new AdapterGV_PlanoLeitura(getBaseContext(), arrayDias, nomeMes, numberMonth);
        gv.invalidateViews();
        adapterGridView.notifyDataSetChanged();
        gv.setAdapter(adapterGridView);

    }

    private String[] ChooseMonthArray(int numberMonth) {

        String[] daysArray;

        if (numberMonth == 1 || numberMonth == 3 || numberMonth == 5 || numberMonth == 7 || numberMonth == 8 || numberMonth == 10 || numberMonth == 12) {
            daysArray = new String[]{"1", "2", "3", "4", "5", "6", "7",
                    "8", "9", "10", "11", "12", "13", "14",
                    "15", "16", "17", "18", "19", "20", "21",
                    "22", "23", "24", "25", "26", "27", "28",
                    "29", "30", "31"};
        } else if (numberMonth == 2) {

            daysArray = new String[]{"1", "2", "3", "4", "5", "6", "7",
                    "8", "9", "10", "11", "12", "13", "14",
                    "15", "16", "17", "18", "19", "20", "21",
                    "22", "23", "24", "25", "26", "27", "28",
                    "29"};

        } else {

            daysArray = new String[]{"1", "2", "3", "4", "5", "6", "7",
                    "8", "9", "10", "11", "12", "13", "14",
                    "15", "16", "17", "18", "19", "20", "21",
                    "22", "23", "24", "25", "26", "27", "28",
                    "29", "30"};

        }

        return daysArray;
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

    private String selectParashaName(int WEEK_OF_YEAR) {

        String[] parashas =
                {       "Parashá 12 - Vayêchí (E viveu)",
                        "Parashá 13 - Shêmot (Nomes)", "Parashá 14 - Vaêrá (E apareci)", "Parashá 15 - Bô (Vá)",
                        "Parashá 16 - Bêshalách (Ao enviar)", "Parashá 17 - Ytrô (Jetro)", "Parashá 18 - Mishpatim (Juizos)",
                        "Parashá 19 - Têrumah (oferta)", "Parashá 20 - Têtsávêh (Ordena!)", "Parashá 21 - Ki tissá (Quando realizar)",
                        "Parashá 22 - Vayakêhel (E reuniu); Parashá 23 - Pêkudei (Registro)", "Parashá 24 - Vayakrá (Ele chamou",
                        "Parashá 25 - Tsav (Ordene!)", "Parashá 26 - Shêmini (Oitavo)",
                        "Parashá 27 - Tazria (Conceber); Parashá 28 - Mêtsôrá (leproso)", "Parashá 29 - Acharêi môt (Depois da morte); Parashá 30 - Kêdoshim (Santos)",
                        "Parashá 31 - Êmor (Fale!)","Pêsach (Festa da Páscoa)", "Parashá 32 - Bêhar (No monte)", "Parashá 33 - Bêchukôtai (Nos meus estatutos)",
                        "Parashá 34 - Bêmidbar (No deserto)", "Parashá 35 - Násô (Levanta)", "Parashá 36 - Bêháalôtêchá (Quando acenderes)", "Parashá 37 - Shlách lêchá (Envie para você)",
                        "Parashá 38 - Kôrách (Coré)", "Parashá 39 - Chukat (Estatutos)", "Parashá 40 - Balák (Balaque)", "Parashá 41 - Pinchás (Finéias)",
                        "Parashá 42 - Matôt (Tribos); Parashá 43 - Masêi (Jornadas)", "Parashá 44 - Dêvarim (דברים) (Palavras)", "Parashá 45 - Vaêtchanan (Supliquei)",
                        "Parashá 46 - Êkev (Por causa)", "Parashá 47 - Rêeh (Veja!)", "Parashá 48 - Shôftim (Juízes)",
                        "Parashá 49 - Ki têtsê (Quando saíres)", "Parashá 50 - Ki tavô (Quando vieres)", "Parashá 51 - Nitzavim (Encontram-se)",
                        "Parashá 52 - Vayeléch (E Ele foi)", "Parashá 53 - Háazinu (Ouçam)", "Parashá 54 - Vêzot HaBracháh (Esta é a Bênção)",
                        "Parashá 1 - Bêreshit ((בראשית) No Princípio)", "Parashá 2 - Nôach (Noé)","Sokôt (Festa dos Tabernáculos)", "Parashá 3 - Lêch lêchá (Sai para ti",
                        "Parashá 4 - Vayêrá (E apareceu)", "Parashá 5 - Chayê Sarah (A vida de Sara)", "Parashá 6 - Tôlêdôt (Gerações)",
                        "Parashá 7 - Vayêtsê (E saiu)", "Parashá 8 - Vaishlách (E enviou)", "Parashá 9 - Vayêshev (E habitou)",
                        "Parashá 10 - Miquêts (Ao fim)", "Parashá 11 - Vaygásh (E aproximou-se)",
                        "Parashá 12 - Vayêchí (E viveu)"
                };

        switch (WEEK_OF_YEAR) {

            case 1:
                return parashas[0];
            case 2:
                return parashas[1];
            case 3:
                return parashas[2];
            case 4:
                return parashas[3];
            case 5:
                return parashas[4];
            case 6:
                return parashas[5];
            case 7:
                return parashas[6];
            case 8:
                return parashas[7];
            case 9:
                return parashas[8];
            case 10:
                return parashas[9];
            case 11:
                return parashas[10];
            case 12:
                return parashas[11];
            case 13:
                return parashas[12];
            case 14:
                return parashas[13];
            case 15:
                return parashas[14];
            case 16:
                return parashas[15];
            case 17:
                return parashas[16];
            case 18:
                return parashas[17];
            case 19:
                return parashas[18];
            case 20:
                return parashas[19];
            case 21:
                return parashas[20];
            case 22:
                return parashas[21];
            case 23:
                return parashas[22];
            case 24:
                return parashas[23];
            case 25:
                return parashas[24];
            case 26:
                return parashas[25];
            case 27:
                return parashas[26];
            case 28:
                return parashas[27];
            case 29:
                return parashas[28];
            case 30:
                return parashas[29];
            case 31:
                return parashas[30];
            case 32:
                return parashas[31];
            case 33:
                return parashas[32];
            case 34:
                return parashas[33];
            case 35:
                return parashas[34];
            case 36:
                return parashas[35];
            case 37:
                return parashas[36];
            case 38:
                return parashas[37];
            case 39:
                return parashas[38];
            case 40:
                return parashas[39];
            case 41:
                return parashas[40];
            case 42:
                return parashas[41];
            case 43:
                return parashas[42];
            case 44:
                return parashas[43];
            case 45:
                return parashas[44];
            case 46:
                return parashas[45];
            case 47:
                return parashas[46];
            case 48:
                return parashas[47];
            case 49:
                return parashas[48];
            case 50:
                return parashas[49];
            case 51:
                return parashas[50];
            case 52:
                return parashas[51];
            case 53:
                return parashas[52];
            case 54:
                return parashas[53];


        }

        return "";
    }

    private void cancelNotificationIfNeeded(Intent it){

        if(it.hasExtra(PushNotificationService.FROM_NOTIFICATION_EXTRA_KEY)){


            SharedPreferences sharedPrefsLeitura = getSharedPreferences(SelectMonthName(calendar.get(Calendar.MONTH) + 1)+ "Leitura", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefsLeitura.edit();

            editor.putInt("Leitura" + calendar.get(Calendar.DAY_OF_MONTH), -1);
            editor.commit();

            Toast.makeText(this,"Leitura adiada.",Toast.LENGTH_SHORT).show();

            NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nm.cancel(R.mipmap.lg_icant72x72);

        }

    }





}
