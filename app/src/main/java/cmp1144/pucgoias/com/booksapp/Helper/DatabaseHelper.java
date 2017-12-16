package cmp1144.pucgoias.com.booksapp.Helper;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cmp1144.pucgoias.com.booksapp.Adapter.ConsultaLivrosAdapter;
import cmp1144.pucgoias.com.booksapp.DAO.ConfiguracaoFireBase;
import cmp1144.pucgoias.com.booksapp.Entidades.Usuario;

/**
 * Created by vinic on 11/12/2017.
 */

public class DatabaseHelper {

    public void buscaTelefoneDonoLivro(final String email, final OnGetDataListener listener){
        listener.onStart();
        Query query = ConfiguracaoFireBase.getFireBase().child("usuario");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DEBUG", "Sucesso Usuario");
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }
}
