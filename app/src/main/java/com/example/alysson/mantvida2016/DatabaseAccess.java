package com.example.alysson.mantvida2016;

 import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Alysson on 22/11/2015.
 */

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {

        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public String getUserName(){

        String retorno = null;

        try {

            Cursor cursor = database.rawQuery("SELECT nome FROM tblUsuario", null);

            if(cursor.moveToFirst())
                retorno = cursor.getString(0);
            cursor.close();

            return retorno;

        }catch (Exception ex){

        }

        return "";
    }

    public String setMetas(String nomeTabela, int numbermeta, String textoConcatenado){

        ContentValues cValues = new ContentValues();

        String nomeColuna = "meta_"+numbermeta;

        cValues.put(nomeColuna, textoConcatenado);


        try {

            database.update(nomeTabela, cValues, "pk = 1", null);
            return  "ok";

        }catch (Exception ex){

            return ex.getMessage();
        }

    }

    public String[] getMetas(String nomeTabela, int numbermeta){

        String[] retorno = null;
        String nomeColuna = "meta_"+numbermeta;

        try {

            Cursor cursor = database.rawQuery("SELECT "+nomeColuna+" FROM "+nomeTabela + " WHERE pk = 1", null);

            if(cursor.moveToFirst())
                retorno = cursor.getString(0).split("%");

            cursor.close();

            return retorno;

        }catch (Exception ex){

        }

        return retorno;

    }

    public boolean validarEmail(String email){

        //true = email não existe, então pode cadastrar.
        //false = email exite, não pode cadastrar.

        try {

            Cursor cursor = database.rawQuery("SELECT email FROM tblusuario WHERE email = '"+email+"'",null);

            if (cursor.moveToFirst()){
                cursor.close();
                return false;
            }

            else
                return true;

        }catch(Exception ex){

            return false;
        }

    }

    public boolean validarLogin(String login){

        //true = login não existe, então pode cadastrar.
        //false = login exite, não pode cadastrar.

        try {

            Cursor cursor = database.rawQuery("SELECT email FROM tblusuario WHERE login = '"+login+"'",null);

            if (cursor.moveToFirst()) {
                cursor.close();
                return false;
            } else
                return true;

        }catch(Exception ex){

            return false;
        }
    }

    public String cadastrarUsuario(String login, String senha, String email,String nome) {

        ContentValues cValues = new ContentValues();

        cValues.put("nome",nome);
        cValues.put("login", login);
        cValues.put("senha", senha);
        cValues.put("email", email);
        cValues.put("id",1);

        try {
                database.insert("tblusuario", null, cValues);
                return "ok";

        } catch (Exception ex) {

            return ex.getMessage();
        }

    }

    public String[] recuperarSenha(String email){


        try {

            Cursor cursor = database.rawQuery("SELECT senha, login FROM tblusuario WHERE email = '"+email+"'",null);

            if (cursor.moveToFirst()) {

                String[] retorno = {cursor.getString(0),cursor.getString(1)};
                cursor.close();
                return retorno;

            } else
                return null;

        }catch(Exception ex){

            return null;
        }

    }

    public String getEventos(int mesEvento, int diaEvento){

        try {
            String retorno;
            Cursor cursor = database.rawQuery("SELECT nomeEvento FROM tblEventos WHERE numMes = "+mesEvento+" AND diaEvento ="+ diaEvento,null);

            if (cursor.moveToFirst()) {

                retorno = cursor.getString(0);
                if(!cursor.isNull(1))
                    retorno += ";"+cursor.getString(1);

                cursor.close();

                return retorno;
            } else
                return "";

        }catch(Exception ex){

            return ex.getMessage();
        }


    }

    public String loginUsuario(String login, String senha){

        String stringRetorno;

        try {

            Cursor cursor = database.rawQuery("SELECT login, senha FROM tblusuario WHERE login = '"+login+"' AND senha = '"+senha+"'",null);
            
            if (cursor.moveToFirst())

                stringRetorno = "ok";
            else
                stringRetorno = "Login não encontrado. Verifique as informações e tente novamente.";

            cursor.close();

        }catch(Exception ex){

            return ex.getMessage();
        }

        return stringRetorno;
    }

    public ArrayList<ClassTblLeitura> buscarTextosDia(int mes, int dia){

        String nomeTabela = chooseTrimetre(mes);

        ArrayList<ClassTblLeitura> listaVersiculos = new ArrayList< >();


        Cursor cursor = database.rawQuery("SELECT titulo, textoBiblico FROM "+nomeTabela+ " WHERE mes = "+mes+" AND dia ="+ dia,null);

        while(cursor.moveToNext()){

            ClassTblLeitura objLeitura = new ClassTblLeitura();
            objLeitura.setTitulo(cursor.getString(0));
            objLeitura.setTextoBiblico(cursor.getString(1));

            listaVersiculos.add(objLeitura);
        }

        cursor.close();
        return listaVersiculos;

    }

    private String chooseTrimetre(int mes){

        String nomeTabela;

        if(mes < 4)
            nomeTabela = "tblleitura_1tri";
        else if(mes < 7)
            nomeTabela = "tblleitura_2tri";
        else if (mes < 10)
            nomeTabela = "tblleitura_3tri";
        else
            nomeTabela = "tblleitura_4tri";

        return nomeTabela;
    }

    public void setTimeAlarm(int hours, int minutes){

        ContentValues cValues = new ContentValues();

        cValues.put("horaAlarme", hours);
        cValues.put("minutoAlarme", minutes);


        try {

            database.update("tblusuario", cValues,"id = 1",null);

        }catch (Exception ex){

        }

    }

    public String[] getTimeAlarm(){

        try {
            String[] retorno;
            Cursor cursor = database.rawQuery("SELECT horaAlarme, minutoAlarme FROM tblusuario WHERE id = 1",null);

            if (cursor.moveToFirst()) {

                retorno = new String[]{cursor.getString(0),cursor.getString(1)};
                cursor.close();

                return retorno;
            } else
                return null;

        }catch(Exception ex){

            return null;
        }


    }


}
