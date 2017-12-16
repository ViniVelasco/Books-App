package cmp1144.pucgoias.com.booksapp.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmp1144.pucgoias.com.booksapp.Adapter.ConsultaLivrosAdapter;
import cmp1144.pucgoias.com.booksapp.DAO.ConfiguracaoFireBase;
import cmp1144.pucgoias.com.booksapp.Entidades.Livro;
import cmp1144.pucgoias.com.booksapp.R;

/**
 * Created by vinic on 11/12/2017.
 */

public class ConsultarLivros extends Fragment {

    private EditText textPesquisa;
    private Button btnConsultar;
    private ListView listaConsultaLivros;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    private ArrayAdapter<Livro> livrosAdapter;
    private ArrayList<Livro> livros;

    View myView;
    static ProgressDialog dialog = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.consultar_livros, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingElements();

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Carregando informações, aguarde.");

        livros = new ArrayList<>();
        livrosAdapter = new ConsultaLivrosAdapter(getContext(), livros);

        listaConsultaLivros.setAdapter(livrosAdapter);
        firebase = ConfiguracaoFireBase.getFireBase().child("livros");

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                livros.clear();
                dialog.show();
                //consultarLivros(textPesquisa.getText().toString());
                BuscarLivros bs = new BuscarLivros();
                bs.execute();
            }
        });

    }

    /**
     * Método que consulta os livros no WebService
     * @param pesquisa
     */
    private void consultarLivros(final String pesquisa){
        final String uidUsuario = ConfiguracaoFireBase.getFirebaseAutenticacao().getCurrentUser().getUid();
        Query query = ConfiguracaoFireBase.getFireBase().child("livros").orderByChild("titulo");
                //.startAt(pesquisa).endAt(pesquisa + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DEBUG", "Sucesso");
                if(dataSnapshot.exists()){
                    for(DataSnapshot dados : dataSnapshot.getChildren()){
                        Livro novoLivro = dados.getValue(Livro.class);

                        if(!uidUsuario.equals(novoLivro.getIdUsuario())){
                            if(pesquisa.isEmpty()){
                                livros.add(novoLivro);
                            } else {
                                if(novoLivro.getTitulo().toLowerCase().contains(pesquisa.toLowerCase())){
                                    livros.add(novoLivro);
                                }
                            }
                        }
                        livrosAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        //firebase.removeEventListener(valueEventListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        //firebase.addValueEventListener(valueEventListener);
    }

    /**
     * Associa os elementos da activity às variáveis
     */
    private void bindingElements(){
        textPesquisa = (EditText) getView().findViewById(R.id.textPesquisa);
        btnConsultar = (Button) getView().findViewById(R.id.btnConsulta);
        listaConsultaLivros = (ListView) getView().findViewById(R.id.listaConsultaLivros);
    }

    /**
     * Classe assíncrona que cuida de carregar os livros na tela
     */
    private class BuscarLivros extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                consultarLivros(textPesquisa.getText().toString());
            } catch (Exception e){
                Log.e("Erro grave: ", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void n){
            livrosAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Método que oculta a dialog
     */
    public static void carregamentoFinalizado(){
        dialog.dismiss();
    }

}
