package cmp1144.pucgoias.com.booksapp.DAO;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vinic on 22/11/2017.
 */

public class ConfiguracaoFireBase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;


    /**
     * Obtem referencia do Firebase
     * @return
     */
    public static DatabaseReference getFireBase(){
        if(referenciaFirebase == null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    /**
     * Obtem referencia da autenticação do Firebase
     * @return
     */
    public static FirebaseAuth getFirebaseAutenticacao(){
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
