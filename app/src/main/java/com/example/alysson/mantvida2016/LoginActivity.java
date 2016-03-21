package com.example.alysson.mantvida2016;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import org.apache.commons.mail.SimpleEmail;

import java.net.SocketImpl;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class LoginActivity extends AppCompatActivity {

    private EditText et_login;
    private EditText et_senha;
    private Context ctx = this;
    private SharedPreferences.Editor editor;


    //Email Test
    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    EditText reciep, sub, msg;
    String rec, subject, textMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        context = this;


        rec = "yuricavalcante17@gmail.com";
        subject = "Teste Email";
        textMessage = "Aqui tem alguma coisa e tals e glória a Deus pois Ele é bom e sua misericórdia dura para sempre.";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("yuricavalcante17@gmail.com", "perfectworld");
            }
        });

        pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();



        /*
        * Seta os valores gravados no SharedPreferences nos campos*/
        SharedPreferences sharedPrefsLogin = getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        editor = sharedPrefsLogin.edit();

        //Mapeia as textviews e coloca valores nelas se tiver valor no sharedPrefereces.

        //Login
        et_login = (EditText) findViewById(R.id.et_actLogin_login);
        et_login.setText(sharedPrefsLogin.getString("Login", ""));

        //Senha
        et_senha = (EditText) findViewById(R.id.et_actLogin_senha);
        et_senha.setText(sharedPrefsLogin.getString("Senha", ""));

        //CheckBox
        if(sharedPrefsLogin.getBoolean("Checked", false)){

            CheckBox cbPreferencias = (CheckBox) findViewById(R.id.cb_preferencias);
            cbPreferencias.setChecked(true);
        }

    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("yuricavalcante17@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            reciep.setText("");
            msg.setText("");
            sub.setText("");
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }


    // ----- Methods Callleds by XML File ----- //
    public void callMainActivityLogin(View view){

        //BANCO DE DADOS: se o login estiver correto o retorno será "ok";
        //se estiver errado, será uma mensagem alertando o usuário sobre o erro;
        //e se der uma exception, retorna a mensagem de exception para o usuário.
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ctx);
        databaseAccess.open();
        String retornoBD = databaseAccess.loginUsuario(et_login.getText().toString(), et_senha.getText().toString());
        databaseAccess.close();

        //Se o login estiver correto, então irei mapear novamente as textviews e a checkbox
        //E setar os valores dela no sharedPreferences.

        if (retornoBD.equals("ok")) {

            //Mapeia novamente os objetos abaixos.
            //se não mapear de novo, iria pegar os valores antigos.
            CheckBox cbPreferencias = (CheckBox) findViewById(R.id.cb_preferencias);
            et_login = (EditText) findViewById(R.id.et_actLogin_login);
            et_senha = (EditText) findViewById(R.id.et_actLogin_senha);

            //Se a checkBox estiver checada, seta os valores do usuário no sharedPreferences
            if(cbPreferencias.isChecked()){

                editor.putString("Login", et_login.getText().toString());
                editor.putString("Senha", et_senha.getText().toString());
                editor.putBoolean("Checked", true);
            }//Se não, seta valores vazios e false para a checkBox
            else{

                editor.putString("Login", "");
                editor.putString("Senha", "");
                editor.putBoolean("Checked", false);
            }

            //Aplica as mudanças no sharedPreferences.
            editor.commit();

            //Chama a activity principal do app.
            startActivity(new Intent(ctx, Main.class));
        }//Se não, foi ok, exibe a mensagem de erro ou de exception para o usuário. E também esvazia os campos.
        else {
            Toast.makeText(LoginActivity.this, retornoBD, Toast.LENGTH_SHORT).show();
            et_login.setText("");
            et_senha.setText("");
        }

    }

    public void callCadastrarActivityLogin(View view){

        startActivity(new Intent(this,CadastrarActivity.class));
    }

    public void callRecuperarSenhaLogin(View view){

        startActivity(new Intent(this,RecuperarSenha.class));
    }
}