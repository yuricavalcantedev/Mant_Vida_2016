package com.example.alysson.mantvida2016;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Yuri on 10/11/2015.
 */

public class ProjetoVida extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String nomeArquivo = "Familia";
    private Toolbar toolbar;
    private TabLayout.Tab tabSelected;
    private EditText oque;
    private EditText como;
    private EditText objetivo;
    private TextView inicio;
    private TextView termino;
    private Context ctx;
    private int tvAuxSetDateTime;
    private int positionActuallyTab;
    private TabLayout tabLayout;

    String[] nomesTabelasBD = new String[]{"Familia", "Ministerio", "Restituicao", "Formacao", "Financas"};
    String[] nomesAreasPDF = new String[]{"Família", "Ministério", "Restituição", "Formação", "Finanças"};
    Document document;
    final int PERMISSION_CODE = 200;
    AlertDialog alerta;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_projetovida);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Projeto de Vida");
        toolbar.setSubtitle("Família");

        setSupportActionBar(toolbar);


        ctx = this;

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        tabSelected = tabLayout.getTabAt(0);

        // tabLayout tem o seu listener.
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            //

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                positionActuallyTab = tab.getPosition();
                methodAuxOnTabsSelected(positionActuallyTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                positionActuallyTab = tab.getPosition();
                methodAuxOnTabsUnSelected(positionActuallyTab);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


        //DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onDestroy(){
        methodAuxOnTabsUnSelected(positionActuallyTab);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_life_project) {

            methodAuxOnTabsUnSelected(positionActuallyTab);
            Toast.makeText(ctx, "Meta salva com sucesso", Toast.LENGTH_SHORT).show();
        }


        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /** Inflating the current activity's menu with res/menu/menu_devotional.xml */
        //getMenuInflater().inflate(R.menu.menu_projeto_vida, menu);

        return super.onCreateOptionsMenu(menu);

    }

    //Métodos auxiliares chamados quando o usuário clica para escolher a data das metas
    public void chooseDate(View v) {

        Calendar calendarInstance = Calendar.getInstance();
        int mes = calendarInstance.get(Calendar.MONTH);
        int dia = calendarInstance.get(Calendar.DAY_OF_MONTH);
        int ano = calendarInstance.get(Calendar.YEAR);


        if (v.getId() == R.id.bt_inicio)
            tvAuxSetDateTime = 0;
        else
            tvAuxSetDateTime = 1;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, ano, mes, dia);
        datePickerDialog.show();

    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            //0 - TextView ligada ao Início.
            //1 - TextView ligada ao Término.

            TextView tv_inicio = (TextView) findViewById(R.id.tv_inicio);

            selectedMonth += 1;

            //Se for igual a textView do Início
            if (tvAuxSetDateTime == 0) {
                String day = "", month = "";

                if (selectedDay < 10)
                    day = "0" + selectedDay;
                else
                    day = "" + selectedDay;

                if (selectedMonth < 10)
                    month = "0" + (selectedMonth);
                else
                    month = "" + (selectedMonth);

                String dataSelecionada = day + "/" + month + "/" + selectedYear;
                tv_inicio.setText(dataSelecionada);

            } else {

                TextView tv_termino = (TextView) findViewById(R.id.tv_termino);

                String dataInicio = tv_inicio.getText().toString();
                String dataSelecionada = selectedDay + "/" + (selectedMonth) + "/" + selectedYear;

                try {

                    int dia = Integer.parseInt(dataInicio.substring(0, 2));
                    int mes = Integer.parseInt(dataInicio.substring(3, 5));
                    int ano = Integer.parseInt(dataInicio.substring(6, dataInicio.length()));

                    if (selectedYear < ano || (selectedYear <= ano && selectedMonth < mes) || (selectedYear <= ano && selectedMonth <= mes && selectedDay < dia))
                        dataSelecionada = "data inválida";

                } catch (Exception ex) {

                    dataSelecionada = "data inválida";

                }

                tv_termino.setText(dataSelecionada);

            }

        }
    };

    public void methodAuxOnTabsSelected(int posTab) {

        oque = (EditText) findViewById(R.id.et_oque);
        como = (EditText) findViewById(R.id.et_como);
        objetivo = (EditText) findViewById(R.id.et_objetivo);
        inicio = (TextView) findViewById(R.id.tv_inicio);
        termino = (TextView) findViewById(R.id.tv_termino);


        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ctx);
        databaseAccess.open();

        String[] retornoBD = databaseAccess.getMetas(nomeArquivo, posTab + 1);

        oque.setText(retornoBD[0]);
        como.setText(retornoBD[1]);
        objetivo.setText(retornoBD[2]);
        inicio.setText(retornoBD[3]);
        termino.setText(retornoBD[4]);

        databaseAccess.close();

    }

    public void methodAuxOnTabsUnSelected(int posTab) {

        oque = (EditText) findViewById(R.id.et_oque);
        como = (EditText) findViewById(R.id.et_como);
        objetivo = (EditText) findViewById(R.id.et_objetivo);
        inicio = (TextView) findViewById(R.id.tv_inicio);
        termino = (TextView) findViewById(R.id.tv_termino);

        String inicioTexto, terminoTexto;

        //Verificação se as datas estão vazias.
        if (inicio.getText().toString().length() < 3)
            inicioTexto = "data inválida";
        else
            inicioTexto = inicio.getText().toString();


        if (termino.getText().toString().length() < 3)
            terminoTexto = "data inválida";
        else
            terminoTexto = termino.getText().toString();


        String textoConcatenado = oque.getText().toString() + "%" + como.getText().toString() + "%" + objetivo.getText().toString() + "%" + inicioTexto + "%" + terminoTexto;

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ctx);
        databaseAccess.open();
        databaseAccess.setMetas(nomeArquivo, (posTab + 1), textoConcatenado);
        databaseAccess.close();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            drawer.setFocusable(true);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_familia) {

            methodAuxOnTabsUnSelected(positionActuallyTab);

            nomeArquivo = "Familia";
            toolbar.setSubtitle("Família");

            methodAuxOnTabsSelected(positionActuallyTab);

        } else if (id == R.id.nav_financas) {
            toolbar.setSubtitle("Finanças");

            methodAuxOnTabsUnSelected(positionActuallyTab);

            nomeArquivo = "Financas";

            methodAuxOnTabsSelected(positionActuallyTab);

        } else if (id == R.id.nav_formacao) {
            toolbar.setSubtitle("Formação");

            methodAuxOnTabsUnSelected(positionActuallyTab);

            nomeArquivo = "Formacao";

            methodAuxOnTabsSelected(positionActuallyTab);

        } else if (id == R.id.nav_restituicao) {
            toolbar.setSubtitle("Restituição");

            methodAuxOnTabsUnSelected(positionActuallyTab);

            nomeArquivo = "Restituicao";

            methodAuxOnTabsSelected(positionActuallyTab);

        } else if (id == R.id.nav_ministerio) {
            toolbar.setSubtitle("Ministério");

            methodAuxOnTabsUnSelected(positionActuallyTab);

            nomeArquivo = "Ministerio";

            methodAuxOnTabsSelected(positionActuallyTab);

        } else if (id == R.id.nav_planoleitura) {

            methodAuxOnTabsUnSelected(positionActuallyTab);
            startActivity(new Intent(ctx, PlanoLeitura.class));
        } else if (id == R.id.nav_gerarimpressao) {

            methodAuxOnTabsUnSelected(positionActuallyTab);
            callWriteOnSDCard();

        } else if (id == R.id.nav_submenu_configuracao) {

            methodAuxOnTabsUnSelected(positionActuallyTab);
            startActivity(new Intent(ctx, Settings.class));

        } else if (id == R.id.nav_submenu_sobre) {

            methodAuxOnTabsUnSelected(positionActuallyTab);
            startActivity(new Intent(ctx, Sobre.class));

        } else if (id == R.id.nav_submenu_site) {

            String url = "http://www.icantmaraba.com.br/site2014/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            startActivity(intent);
        } else if (id == R.id.nav_submenu_contato) {

            methodAuxOnTabsUnSelected(positionActuallyTab);
            startActivity(new Intent(ctx, Contato.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentProjetoVida(), "Meta 1");
        adapter.addFragment(new FragmentProjetoVida(), "Meta 2");
        adapter.addFragment(new FragmentProjetoVida(), "Meta 3");
        adapter.addFragment(new FragmentProjetoVida(), "Meta 4");
        adapter.addFragment(new FragmentProjetoVida(), "Meta 5");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE:
                for (int i = 0; i < permissions.length; i++) {

                    if (permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        createDeleteFolder();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void callWriteOnSDCard() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                callDialog(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
            }
        } else {
            createDeleteFolder();
        }

    }

    // FILE
    public void createDeleteFolder() {
        String path = Environment.getExternalStorageDirectory().toString() + "/Mant Vida";
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/Mant Vida");

        if (file.exists()) {
            new File(Environment.getExternalStorageDirectory().toString() + "/Mant Vida", "Projeto de Vida.pdf").delete();
            if (file.delete()) {

                Toast.makeText(this,"Aperte mais uma vez para gerar o Projeto de Vida em pdf.",Toast.LENGTH_SHORT).show();
            }
        } else {
            if (file.mkdir()) {
                createFile(path);
            } else {
            }
        }
    }

    public void createFile(final String path) {

        try {

            String filename = "Projeto de Vida.pdf";
            String projetoVida;

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ctx);
            databaseAccess.open();

            document = new Document(PageSize.A4);

            File dir = new File(path, filename);
            if (!dir.exists()) {
                dir.getParentFile().mkdirs();
            }

            FileOutputStream fOut = new FileOutputStream(dir);
            fOut.flush();

            //Fontes
            Font fontTitulo = new Font(Font.FontFamily.COURIER, 25, Font.BOLD);
            Font fontArea = new Font(Font.FontFamily.TIMES_ROMAN, 18);
            Font fontMetas = new Font(Font.FontFamily.HELVETICA, 14);
            Font fontHazak = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLDITALIC);

            Paragraph titulo = new Paragraph("Projeto de Vida 2016", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);

            PdfWriter.getInstance(document, fOut);
            document.open();

            document.add(titulo);

            document.addTitle("Projeto de Vida 2016");

            for (int i = 0; i < nomesTabelasBD.length; i++) {

                projetoVida = "";
                Paragraph paragraphAreas = new Paragraph(nomesAreasPDF[i] + "\n\n", fontArea);
                paragraphAreas.setAlignment(Element.CHAPTER);
                document.add(paragraphAreas);

                Paragraph paragraphMetas;

                for (int countAux = 1; countAux <= 5; countAux++) {

                    String[] retornoBD = databaseAccess.getMetas(nomesTabelasBD[i], countAux);

                    if (retornoBD[0].length() > 2) {
                        projetoVida += ("     - " + retornoBD[0] + " até " + retornoBD[4] + "\n");
                    }
                }

                paragraphMetas = new Paragraph(projetoVida, fontMetas);
                paragraphMetas.setSpacingAfter(20);
                document.add(paragraphMetas);

            }


            Paragraph hazak = new Paragraph("Hazak, hazak, v'nit'chazek! \n Seja forte, seja forte, e sejamos fortalecidos!\n", fontHazak);
            hazak.setAlignment(Element.ALIGN_CENTER);
            document.add(hazak);

            Toast.makeText(this,"Projeto de vida.pdf criado com sucesso na pasta Mant Vida",Toast.LENGTH_SHORT).show();

            //Image logoIgreja = Image.getInstance(getClass().getResource("/com/example/alysson/mantvida2016/lg_icant72x72_gold.png"));
            //logoIgreja.setAlignment(Element.ALIGN_BOTTOM);
            //document.add(logoIgreja);

            databaseAccess.close();


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    // UTIL
    public void callDialog(final String[] permissions) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseja criar o pdf do seu Projeto de Vida 2016 ?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                ActivityCompat.requestPermissions(ProjetoVida.this, permissions, PERMISSION_CODE);
                Toast.makeText(ProjetoVida.this, "Pdf criado com sucesso!", Toast.LENGTH_SHORT).show();

                alerta.cancel();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.cancel();
            }
        });


        alerta = builder.create();
        alerta.show();

    }


}
