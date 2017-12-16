package cmp1144.pucgoias.com.booksapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmp1144.pucgoias.com.booksapp.DAO.ConfiguracaoFireBase;
import cmp1144.pucgoias.com.booksapp.Entidades.Livro;
import cmp1144.pucgoias.com.booksapp.Entidades.Usuario;
import cmp1144.pucgoias.com.booksapp.Fragment.ConsultarLivros;
import cmp1144.pucgoias.com.booksapp.Helper.DatabaseHelper;
import cmp1144.pucgoias.com.booksapp.Helper.OnGetDataListener;
import cmp1144.pucgoias.com.booksapp.R;

/**
 * Created by vinic on 11/12/2017.
 */

public class ConsultaLivrosAdapter extends ArrayAdapter<Livro> {

    private ArrayList<Livro> livros;
    private Context context;
    private DatabaseReference firebase;

    /**
     * Construtor do Adaptador
     * @param context
     * @param livros
     */
    public ConsultaLivrosAdapter(Context context, ArrayList<Livro> livros) {
        super(context, 0, livros);
        this.context = context;
        this.livros = livros;
    }

    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        View view = null;
        if(livros != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_consulta_livros, parent, false);

            //buscaTelefoneDonoLivro(livros.get(position).getEmailProprietario(), view);

            ImageView imageLivro = (ImageView) view.findViewById(R.id.imagemLivro);
            TextView txtTitulo = (TextView) view.findViewById(R.id.tituloLivro);
            TextView txtAutor = (TextView) view.findViewById(R.id.autorLivro);
            TextView txtEditora = (TextView) view.findViewById(R.id.editoraLivro);
            TextView txtEdicao = (TextView) view.findViewById(R.id.edicao);
            TextView txtProprietario = (TextView) view.findViewById(R.id.emailProprietario);
            TextView txtTelefone = (TextView) view.findViewById(R.id.telefoneProprietario);

            final int positionFinal = position;

            final Livro livroAtual = livros.get(position);

            if(livroAtual.getTelefoneProprietario() == null){
                buscaTelefoneDonoLivro(position);
            } else {
                txtTitulo.setText(livroAtual.getTitulo());
                txtAutor.setText(livroAtual.getAutor());
                txtEditora.setText(livroAtual.getEditora());
                txtEdicao.setText(String.valueOf(livroAtual.getEdicao()));
                txtProprietario.setText(livroAtual.getEmailProprietario());
                if(livroAtual.getCapaBase64() != null){
                    base64ToImgView(livroAtual.getCapaBase64(), imageLivro);
                }

                txtTelefone.setText(livroAtual.getTelefoneProprietario());
                ConsultarLivros.carregamentoFinalizado();
            }

        }
        return view;
    }

    /**
     * Método que busca o telefone do proprietário do livro
     * @param position
     */
    public void buscaTelefoneDonoLivro(final int position){
        Query query = ConfiguracaoFireBase.getFireBase().child("usuario");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                for(DataSnapshot dados : dataSnapshot.getChildren()) {
                        Usuario novoUsuario = dados.getValue(Usuario.class);
                        String email = livros.get(position).getEmailProprietario();
                        if (email.equals(novoUsuario.getEmail())) {
                            livros.get(position).setTelefoneProprietario(novoUsuario.getTelefone());
                            notifyDataSetChanged();
                        }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    /**
     * Conversor de um base64 para Bitmap
     * @param bs64
     * @param img
     */
    public static void base64ToImgView(String bs64, ImageView img){
        byte[] decodedString = Base64.decode(bs64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img.setImageBitmap(decodedByte);

    }
}
