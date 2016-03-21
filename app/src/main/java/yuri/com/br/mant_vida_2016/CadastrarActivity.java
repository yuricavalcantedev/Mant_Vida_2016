package yuri.com.br.mant_vida_2016;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CadastrarActivity extends AppCompatActivity {

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

    }

    // ----- Methods calleds by xml file. ----- //

    //Register Button
    public void callMainActivityCadastrar(View view){

        EditText et_senha = (EditText) findViewById(R.id.et_actCadastrar_senha);
        EditText et_senhaRepetida = (EditText) findViewById(R.id.et_actCadastrar_repSenha);
        EditText et_login = (EditText) findViewById(R.id.et_actCadastrar_login);
        EditText et_email = (EditText) findViewById(R.id.et_actCadastrar_email);
        EditText et_nome = (EditText) findViewById(R.id.et_actCadastrar_name);


        boolean emailValidado,loginValidado, cadastrar = true;

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ctx);
        databaseAccess.open();

        emailValidado = databaseAccess.validarEmail(et_email.getText().toString());
        loginValidado = databaseAccess.validarLogin(et_login.getText().toString());

        //Verificações: Nome
        if(et_nome.getText().toString().length() < 2) {
            Toast.makeText(ctx, "O nome do usuário deve conter mais que 1 dígito.", Toast.LENGTH_SHORT).show();
            cadastrar = false;
            //Verificações: Email
        }

        if(et_email.getText().toString().length() < 10 || !et_email.getText().toString().contains("@") || !et_email.getText().toString().contains(".com")) {
            Toast.makeText(ctx, "Email inválido. Digite um email correto e tente novamente", Toast.LENGTH_SHORT).show();
            cadastrar = false;

        }else if (!emailValidado) {
            Toast.makeText(ctx, "Email já cadastrado.Insira outro email válido e tente novamente.", Toast.LENGTH_SHORT).show();
            cadastrar = false;
            //Verificações: login
        }

        if(et_login.getText().toString().length() < 4) {
            Toast.makeText(ctx, "O login deve conter mais que 3 dígitos.", Toast.LENGTH_SHORT).show();
            cadastrar = false;

        }else if(!loginValidado) {
            Toast.makeText(ctx, "Login já cadastrado.Insira outro login válido e tente novamente.", Toast.LENGTH_SHORT).show();
            cadastrar = false;
            //Verificações: Senha
        }

        if(et_senha.getText().toString().length() <4) {
            Toast.makeText(ctx, "O tamanho da senha deve conter mais que 3 dígitos.", Toast.LENGTH_SHORT).show();
            et_senha.setText("");
            cadastrar = false;
        }else if (et_senha.getText() == et_senhaRepetida.getText()){
            Toast.makeText(CadastrarActivity.this, "Senhas estão diferentes.\n Corrija e tente novamente.", Toast.LENGTH_SHORT).show();
            et_senhaRepetida.setText("");
            cadastrar = false;
        }

        //Se todos os dados estiverem corretos, o usuário é registrado.
        if(cadastrar) {
            String resultadoCadastro = databaseAccess.cadastrarUsuario(et_login.getText().toString(), et_senha.getText().toString(), et_email.getText().toString(), et_nome.getText().toString());
            databaseAccess.close();


            AlertDialog.Builder builderDialog = new AlertDialog.Builder(ctx);
            builderDialog.setTitle("MANT VIDA");

            if (resultadoCadastro.equals("ok")) {

                builderDialog.setMessage("Cadastro Realizado com Sucesso");

                builderDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(ctx, Main.class));

                    }
                });

            } else {

                builderDialog.setMessage(resultadoCadastro);
            }

            AlertDialog dialog = builderDialog.create();
            dialog.show();

        }

    }

    //Cancel Button
    public void finishCadastrarActivity(View view){

        finish();
    }
 }
