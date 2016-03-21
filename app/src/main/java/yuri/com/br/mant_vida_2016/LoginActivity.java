package yuri.com.br.mant_vida_2016;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;



public class LoginActivity extends AppCompatActivity {

    private EditText et_login;
    private EditText et_senha;
    private Context ctx = this;
    private SharedPreferences.Editor editor;

    Context context = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        context = this;

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