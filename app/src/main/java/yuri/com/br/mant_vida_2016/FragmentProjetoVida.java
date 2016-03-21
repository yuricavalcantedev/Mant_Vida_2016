package yuri.com.br.mant_vida_2016;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by Alysson on 10/11/2015.
 */

//Chamada apenas 1 vez (a primeira tela do projeto de vida)

public class FragmentProjetoVida extends Fragment {

    public FragmentProjetoVida() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.activity_fragment_projetovida, container, false);

        //Mapeio os campos necessários para setar o valor vindos do banco.
        EditText oque = (EditText) fragmentView.findViewById(R.id.et_oque);
        EditText como = (EditText) fragmentView.findViewById(R.id.et_como);
        EditText objetivo = (EditText) fragmentView.findViewById(R.id.et_objetivo);
        TextView inicio = (TextView) fragmentView.findViewById(R.id.tv_inicio);
        TextView termino = (TextView) fragmentView.findViewById(R.id.tv_termino);


        //Banco de dados
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();

        String[] retornoBD = databaseAccess.getMetas("Familia", 1);

        //Seta os valores vindos do banco de dados.
        oque.setText(retornoBD[0]);
        como.setText(retornoBD[1]);
        objetivo.setText(retornoBD[2]);
        inicio.setText(retornoBD[3]);
        termino.setText(retornoBD[4]);


        //Fecha a conexão do BD.
        databaseAccess.close();


        //retorna a view.
        return fragmentView;


    }

}
