package cmp1144.pucgoias.com.booksapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import cmp1144.pucgoias.com.booksapp.DAO.ConfiguracaoFireBase;
import cmp1144.pucgoias.com.booksapp.Entidades.Livro;
import cmp1144.pucgoias.com.booksapp.R;

/**
 * Created by vinic on 30/11/2017.
 */

public class LivrosAdapter extends ArrayAdapter<Livro> {

    private ArrayList<Livro> livros;
    private Context context;
    private DatabaseReference firebase;

    public LivrosAdapter(Context context, ArrayList<Livro> livros) {
        super(context, 0, livros);
        this.context = context;
        this.livros = livros;
    }

    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        View view = null;
        if(livros != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_livros, parent, false);

            ImageView imageLivro = (ImageView) view.findViewById(R.id.imagemLivro);
            ImageView imageApagaLivro = (ImageView) view.findViewById(R.id.deletaItem);
            TextView txtTitulo = (TextView) view.findViewById(R.id.tituloLivro);
            TextView txtAutor = (TextView) view.findViewById(R.id.autorLivro);
            TextView txtEditora = (TextView) view.findViewById(R.id.editoraLivro);

            final int positionFinal = position;

            Livro livroAtual = livros.get(position);
            txtTitulo.setText(livroAtual.getTitulo());
            txtAutor.setText(livroAtual.getAutor());
            txtEditora.setText(livroAtual.getEditora());
            if(livroAtual.getCapaBase64() != null){
                base64ToImgView(livroAtual.getCapaBase64(), imageLivro);
            }

            imageApagaLivro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Livro livroExcluir = livros.get(positionFinal);
                    //Cria o gerador do alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Excluindo livro");
                    builder.setMessage("Deseja confirmar a exclusão do livro: " + livroExcluir.getTitulo() + "?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            firebase = ConfiguracaoFireBase.getFireBase().child("livros");
                            firebase.child(livroExcluir.getUid()).removeValue();

                            Toast.makeText(getContext(), "Exclusão Efetuada!", Toast.LENGTH_SHORT).show();

                        }
                    });

                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Exclusão Cancelada!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog alerta = builder.create();
                    alerta.show();

                }
            });



        }
        return view;
    }

    /**
     * Conversor de base64 para imagem.
     * @param bs64
     * @param img
     */
    public static void base64ToImgView(String bs64, ImageView img){
        byte[] decodedString = Base64.decode(bs64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img.setImageBitmap(decodedByte);

    }

}
