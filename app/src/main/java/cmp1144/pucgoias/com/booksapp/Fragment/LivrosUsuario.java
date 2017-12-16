package cmp1144.pucgoias.com.booksapp.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import cmp1144.pucgoias.com.booksapp.Adapter.LivrosAdapter;
import cmp1144.pucgoias.com.booksapp.DAO.ConfiguracaoFireBase;
import cmp1144.pucgoias.com.booksapp.Entidades.Livro;
import cmp1144.pucgoias.com.booksapp.R;

/**
 * Created by vinic on 30/11/2017.
 */

public class LivrosUsuario extends Fragment {

    private ListView listaLivros;
    private ArrayAdapter<Livro> livrosAdapter;
    private ArrayList<Livro> livros;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_livros, container, false);
        return myView;
    }

    /**
     * Faz a transação para o fragment de CadastroLivros para edição do livro
     * @param livro
     */
    public void novoCadastroLivros(Livro livro){
        Bundle bundle = new Bundle();
        bundle.putString("livro", new Gson().toJson(livro));
        CadastroLivros fragment = new CadastroLivros();
        fragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        livros = new ArrayList<>();

        listaLivros = (ListView) getView().findViewById(R.id.listaLivros);
        livrosAdapter = new LivrosAdapter(getContext(), livros);

        listaLivros.setAdapter(livrosAdapter);

        firebase = ConfiguracaoFireBase.getFireBase().child("livros");
        final String uidUsuario = ConfiguracaoFireBase.getFirebaseAutenticacao().getCurrentUser().getUid();

        listaLivros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Livro livroSelecionado = (Livro) listaLivros.getItemAtPosition(position);
                novoCadastroLivros(livroSelecionado);

            }
        });

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                livros.clear();
                Log.d("DEBUG", "SUCCESS");
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Livro novoLivro = dados.getValue(Livro.class);

                    if(uidUsuario.equals(novoLivro.getIdUsuario())){
                        livros.add(novoLivro);
                    }

                }

                livrosAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               Log.e("DEBUG", databaseError.getMessage());

            }
        };

    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }
}
