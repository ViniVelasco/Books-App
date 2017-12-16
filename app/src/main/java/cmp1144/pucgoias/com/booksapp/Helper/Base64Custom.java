package cmp1144.pucgoias.com.booksapp.Helper;

import android.util.Base64;

/**
 * Created by vinic on 23/11/2017.
 */

public class Base64Custom {

    /**
     * Codifica uma String em Base64
     * @param texto
     * @return
     */
    public static String codificarBase64(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    /**
     * Decodifica uma Base64 para String
     * @param textoCodificado
     * @return
     */
    public static String decodificarBase64(String textoCodificado){
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
