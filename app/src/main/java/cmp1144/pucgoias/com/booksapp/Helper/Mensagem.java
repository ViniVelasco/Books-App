package cmp1144.pucgoias.com.booksapp.Helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by vinic on 02/12/2017.
 */

public class Mensagem {

    /**
     * Cria um Toast
     * @param msg
     * @param context
     */
    public static void alerta(String msg, Context context){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
