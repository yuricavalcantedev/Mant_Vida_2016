package yuri.com.br.mant_vida_2016;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RecuperarSenha extends AppCompatActivity {

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        Button bt_recSenha = (Button) findViewById(R.id.bt_actRecuperarSenha_Recuperar);
        bt_recSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_recuperarSenha = (EditText) findViewById(R.id.et_actRecSenha_email);

                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ctx);
                databaseAccess.open();
                String[] retornoBD = databaseAccess.recuperarSenha(et_recuperarSenha.getText().toString());
                databaseAccess.close();


                if (retornoBD != null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {

                            startActivity(new Intent(ctx, LoginActivity.class));
                        }

                    });

                    builder.setTitle("Informações Recuperadas");
                    builder.setMessage("Sua senha é : "+retornoBD[0]+"\n"+ "Seu login é: "+retornoBD[1]);
                    AlertDialog alerta = builder.create();
                    alerta.show();

                }else
                    Toast.makeText(RecuperarSenha.this, "Conta não cadastrada ou o email está incorreto. Verifique as informações e tente novamente.", Toast.LENGTH_SHORT).show();

            }
        });


    }


}