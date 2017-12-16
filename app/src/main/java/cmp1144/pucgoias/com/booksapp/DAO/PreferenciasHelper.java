package cmp1144.pucgoias.com.booksapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cmp1144.pucgoias.com.booksapp.Entidades.Usuario;

/**
 * Created by vinic on 07/12/2017.
 */

public class PreferenciasHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "cmp1144.pucgoias.com.booksapp.preferencias2";
    private static final int VERSAO_BANCO = 1;
    private static final String TAG = "PreferenciasBD";

    /**
     * Construtor do banco local
     * Tabela Preferencias
     * id int
     * exibirEmail int
     * exibirNotificacoes int
     * @param context
     */
    public PreferenciasHelper(Context context){
        super(context,NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE IF NOT EXISTS preferencias (id int primary key,")
                .append("exibirEmail int, ")
                .append("exibirNotificacoes int);");

        db.execSQL(sql.toString());


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Método que salva preferências de um usuário no banco local
     * @param usuario
     * @throws Exception
     */
    public void salvar(Usuario usuario) throws Exception {
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("exibirEmail", String.valueOf(usuario.getExibirEmail()));
            values.put("exibirNotificacoes", String.valueOf(usuario.getExibirNotificacoes()));
            values.put("id", ConfiguracaoFireBase.getFirebaseAutenticacao().getCurrentUser().getUid());

            if(consultar(usuario)){
                //faz o update
                String[] whereArgs = new String[]{(String.valueOf(usuario.getId()))};
                if(db.update("preferencias", values, "id=?", whereArgs)==0){
                    throw new Exception("Registro não encontrado!");
                }

            } else {
                //insert
                db.insert("preferencias", "", values);
            }
        } catch(Exception e){
            e.printStackTrace();
            throw e;

        } finally {
            db.close();
        }
    }

    /**
     * Método que consulta um usuário no banco local
     * @param user
     * @return
     * @throws Exception
     */
    public Boolean consultar(Usuario user) throws Exception{
        SQLiteDatabase db = getReadableDatabase();

        try {
            String[] whereArgs = new String[]{(String.valueOf(user.getId()))};
            String[] columnsArgs = new String[]{"id", "exibirEmail", "exibirNotificacoes"};
            Cursor result = db.query("Preferencias", columnsArgs, "id=?", whereArgs, null, null, null);

            if(result.moveToFirst()){
                user.setExibirNotificacoes(Short.valueOf(result.getString(result.getColumnIndex("exibirNotificacoes"))));
                user.setExibirEmail(Short.valueOf(result.getString(result.getColumnIndex("exibirEmail"))));
                return true;
            } else {
                return false;
            }

        } catch(Exception e){

            e.printStackTrace();
            throw e;
        } finally {
            //db.close();
        }
    }

    /**
     * Método que verificar o exibirNotificações de um determinado usuário
     * @param uid
     * @return
     * @throws Exception
     */
    public Boolean exibirNotificacoes(String uid) throws Exception{
        SQLiteDatabase db = getReadableDatabase();

        try {
            String[] whereArgs = new String[]{(String.valueOf(uid))};
            String[] columnsArgs = new String[]{"id", "exibirNotificacoes"};
            Cursor result = db.query("Preferencias", columnsArgs, "id=?", whereArgs, null, null, null);

            if(result.moveToFirst()){
                Short valorNot = result.getShort(result.getColumnIndex("exibirNotificacoes"));
                if(valorNot == (short) 0){
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }

        } catch(Exception e){

            e.printStackTrace();
            throw e;
        } finally {
            db.close();
        }
    }

    /**
     * Lista o cursor do banco local
     * @return
     * @throws Exception
     */
    public Cursor listaCursor() throws Exception{
        SQLiteDatabase db = getWritableDatabase();
        try {
            String[] projections = {"id _id", "exibirEmail", "exibirNotificacoes"};
            return db.query("preferencias", projections, null, null, null, null, null);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
