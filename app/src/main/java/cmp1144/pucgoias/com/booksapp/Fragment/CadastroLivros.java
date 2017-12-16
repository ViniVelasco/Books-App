package cmp1144.pucgoias.com.booksapp.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cmp1144.pucgoias.com.booksapp.Adapter.LivrosAdapter;
import cmp1144.pucgoias.com.booksapp.DAO.ConfiguracaoFireBase;
import cmp1144.pucgoias.com.booksapp.DAO.PreferenciasHelper;
import cmp1144.pucgoias.com.booksapp.Entidades.Livro;
import cmp1144.pucgoias.com.booksapp.Helper.Base64Custom;
import cmp1144.pucgoias.com.booksapp.Helper.Mensagem;
import cmp1144.pucgoias.com.booksapp.R;

/**
 * Created by vinic on 22/11/2017.
 */

public class CadastroLivros extends Fragment {

    private Button btnSalvar, btnEscolherCapa;
    private ImageView imagemCapaLivro;
    private EditText textISBN, textTitulo, textAutor, textEditora, textEdicao;
    private Spinner spinnerAssunto;
    private String[] assuntos = { "Didáticos", "Autoajuda", "Biografia", "Computação",
            "Crônicas e Humor", "Esportes", "Fantasia e Horror", "Ficcção", "HQS",
            "Religião", "Romance", "Inglês e Outras Línguas"
    };

    private Livro livro;
    Livro livroUpdate;
    private DatabaseReference firebase;
    private FirebaseAuth autenticacao;
    private String jsonObject;
    ArrayAdapter<String> adapter;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstance) {
        myView = inflater.inflate(R.layout.cadastro_livros, container, false);

        Bundle bundle = getArguments();
        if(bundle != null){
            jsonObject = bundle.getString("livro");
        }

        if(jsonObject != null) {
            livroUpdate = new Gson().fromJson(jsonObject, Livro.class);
            Log.v("teste", livroUpdate.getTitulo());
        }
        return myView;
    }

    /**
     * Atualiza o formulário para edição de um livro
     */
    public void atualizaFormulario(){
        if(livroUpdate.getCapaBase64() != null){
            LivrosAdapter.base64ToImgView(livroUpdate.getCapaBase64(), imagemCapaLivro);
        }

        textISBN.setText(livroUpdate.getISBN());
        textTitulo.setText(livroUpdate.getTitulo());
        textAutor.setText(livroUpdate.getAutor());
        textEditora.setText(livroUpdate.getEditora());
        textEdicao.setText(String.valueOf(livroUpdate.getEdicao()));


        if (!livroUpdate.getAssunto().equals(null)) {
            int spinnerPosition = adapter.getPosition(livroUpdate.getAssunto());
            spinnerAssunto.setSelection(spinnerPosition);
        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindingElements();
        if(livroUpdate != null){
            atualizaFormulario();
        }
    }

    /**
     * Associa elementos da activity CadastroLivros
     */
    private void bindingElements(){
        btnSalvar = (Button) getView().findViewById(R.id.btnSalvar);
        textISBN = (EditText) getView().findViewById(R.id.textISBN);
        textTitulo = (EditText) getView().findViewById(R.id.textTitulo);
        textAutor = (EditText) getView().findViewById(R.id.textAutor);
        textEditora = (EditText) getView().findViewById(R.id.textEditora);
        textEdicao = (EditText) getView().findViewById(R.id.textEdicao);
        spinnerAssunto = (Spinner) getView().findViewById(R.id.spinnerAssunto);
        btnEscolherCapa = (Button) getView().findViewById(R.id.btnEscolherImagemLivro);
        imagemCapaLivro = (ImageView) getView().findViewById(R.id.imgCapaLivro);

        adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, assuntos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssunto.setAdapter(adapter);
        listenerButtons();

        btnEscolherCapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    private static final int SELECT_PHOTO = 100;

    /**
     * Método responsável por selecionar uma imagem da galeria do usuário
     */
    public void pickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == Activity.RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    imagemCapaLivro.setImageBitmap(yourSelectedImage);
                }
        }
    }

    /**
     * Associa listeners dos buttons
     */
    private void listenerButtons(){
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textISBN.getText().toString().isEmpty() || textTitulo.getText().toString().isEmpty() ||
                        textAutor.getText().toString().isEmpty() || textEditora.getText().toString().isEmpty() ||
                        textEdicao.getText().toString().isEmpty()){
                    Mensagem.alerta("Por favor, preencha todos os campos antes de salvar", getActivity());
                } else {
                    autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

                    if(livroUpdate == null){
                        Livro livro = new Livro();
                        livro.setAutor(textAutor.getText().toString());
                        livro.setEdicao(Integer.valueOf(textEdicao.getText().toString()));
                        livro.setEditora(textEditora.getText().toString());
                        livro.setISBN(textISBN.getText().toString());
                        livro.setTitulo(textTitulo.getText().toString());
                        livro.setIdUsuario(autenticacao.getCurrentUser().getUid());
                        livro.setAssunto(spinnerAssunto.getSelectedItem().toString());
                        livro.setCapaBase64(imgViewToBase64(imagemCapaLivro));
                        livro.setEmailProprietario(ConfiguracaoFireBase.getFirebaseAutenticacao().getCurrentUser().getEmail());

                        String identificadorLivro = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getUid() + livro.getISBN());
                        livro.setUid(identificadorLivro);
                        salvarLivro(livro);
                    } else {
                        livroUpdate.setAutor(textAutor.getText().toString());
                        livroUpdate.setEdicao(Integer.valueOf(textEdicao.getText().toString()));
                        livroUpdate.setEditora(textEditora.getText().toString());
                        livroUpdate.setISBN(textISBN.getText().toString());
                        livroUpdate.setTitulo(textTitulo.getText().toString());
                        livroUpdate.setIdUsuario(autenticacao.getCurrentUser().getUid());
                        livroUpdate.setAssunto(spinnerAssunto.getSelectedItem().toString());
                        livroUpdate.setCapaBase64(imgViewToBase64(imagemCapaLivro));
                        updateLivro(livroUpdate);
                        Mensagem.alerta("Alterado com sucesso", getActivity());
                    }

                    PreferenciasHelper ph = new PreferenciasHelper(getContext());
                    try {
                        if(ph.exibirNotificacoes(ConfiguracaoFireBase.getFirebaseAutenticacao().getCurrentUser().getUid())){
                            exibirNotificacoes();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    limparCampos();
                }
            }
        });
    }

    /**
     * Converte uma imageView para base64
     * @param img
     * @return
     */
    private String imgViewToBase64(ImageView img){
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bmap = drawable.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.PNG,100,bos);
        byte[] bb = bos.toByteArray();
        String image = Base64.encodeToString(bb, Base64.DEFAULT);
        return image;
    }

    /**
     * Salva um livro no WebService
     * @param livro
     * @return
     */
    private Boolean salvarLivro(Livro livro) {
        try {

            firebase = ConfiguracaoFireBase.getFireBase().child("livros");
            firebase.child(livro.getUid()).setValue(livro);
            Mensagem.alerta("Livro cadastrado com sucesso", getActivity());
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;

        }
    }

    /**
     * Atualiza um livro no WebService
     * @param livro
     * @return
     */
    private Boolean updateLivro(Livro livro){
        try {
            firebase = ConfiguracaoFireBase.getFireBase().child("livros");

            Map<String, Object> livrosValues = livro.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(livro.getUid(), livrosValues);

            firebase.updateChildren(childUpdates);
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Exibe uma notificação com o livro cadastrado
     */
    private void exibirNotificacoes(){
        String tituloLivro = textTitulo.getText().toString();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.ic_info)
                        .setContentTitle(tituloLivro + " Cadastrado")
                        .setContentText("Seu livro " + tituloLivro + " foi cadastrado com sucesso.");

        int mId = 0;
        NotificationManager mNotificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, mBuilder.build());
    }

    /**
     * Limpa os campos do formulário
     */
    private void limparCampos(){
        textTitulo.getText().clear();
        textISBN.getText().clear();
        textAutor.getText().clear();
        textEdicao.getText().clear();
        textEditora.getText().clear();
        spinnerAssunto.setSelection(0);
        Drawable myDrawable = getContext().getDrawable(R.drawable.mockup);
        imagemCapaLivro.setImageDrawable(myDrawable);
    }

}
