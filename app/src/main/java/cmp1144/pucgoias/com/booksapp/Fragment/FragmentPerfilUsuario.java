package cmp1144.pucgoias.com.booksapp.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cmp1144.pucgoias.com.booksapp.DAO.ConfiguracaoFireBase;
import cmp1144.pucgoias.com.booksapp.DAO.PreferenciasHelper;
import cmp1144.pucgoias.com.booksapp.Entidades.Livro;
import cmp1144.pucgoias.com.booksapp.Entidades.Usuario;
import cmp1144.pucgoias.com.booksapp.Helper.Base64Custom;
import cmp1144.pucgoias.com.booksapp.Helper.Mensagem;
import cmp1144.pucgoias.com.booksapp.R;

/**
 * Created by vinic on 06/12/2017.
 */

public class FragmentPerfilUsuario extends Fragment {

    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private Usuario usuario;
    private EditText textEmail, textSenha, textNome, textSobrenome, textDataNascimento, textTelefone;
    private RadioButton radioMasc, radioFem;
    private Button btnSelData, btnSalvar;
    private CheckBox desativarNotificacoes, exibirEmail;
    private FirebaseAuth autenticacao;
    private int ano, mes, dia;

    View myView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingElements();

        firebase = ConfiguracaoFireBase.getFireBase().child("usuario");
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        final FirebaseUser firebaseUser = autenticacao.getCurrentUser();

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Carregando informações, aguarde.");
        dialog.show();
        selecionarData();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DEBUG", "SUCCESS");
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Usuario novoUsuario = dados.getValue(Usuario.class);

                    if(firebaseUser.getEmail().equals(novoUsuario.getEmail())){
                        usuario = novoUsuario;
                        textEmail.setText(usuario.getEmail());
                        textDataNascimento.setText(usuario.getDataNascimento());
                        textNome.setText(usuario.getNome());
                        textSenha.setText(usuario.getSenha());
                        textSobrenome.setText(usuario.getSobrenome());
                        textTelefone.setText(usuario.getTelefone());

                        if(usuario.getSexo().equals("Masculino")){
                            radioMasc.setChecked(true);
                        } else {
                            radioFem.setChecked(true);
                        }
                        dialog.dismiss();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DEBUG", databaseError.getMessage());

            }
        };

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(     textEmail.getText().toString().isEmpty() || textDataNascimento.getText().toString().isEmpty() ||
                        textNome.getText().toString().isEmpty() || textSenha.getText().toString().isEmpty() ||
                        textSobrenome.getText().toString().isEmpty() || textTelefone.getText().toString().isEmpty()){
                    Mensagem.alerta("Por favor, preencha todos os campos.", getContext());
                } else {
                    usuario.setTelefone(textTelefone.getText().toString());
                    usuario.setDataNascimento(textDataNascimento.getText().toString());
                    usuario.setSobrenome(textSobrenome.getText().toString());
                    usuario.setNome(textNome.getText().toString());
                    usuario.setEmail(textEmail.getText().toString());
                    usuario.setSenha(textSenha.getText().toString());

                    if(radioMasc.isChecked()){
                        usuario.setSexo("Masculino");
                    } else {
                        usuario.setSexo("Feminino");
                    }

                    updateUsuario(usuario);

                    //Salvando na memória interna
                    if(exibirEmail.isChecked()){
                        usuario.setExibirEmail(Short.valueOf("1"));
                    } else {
                        usuario.setExibirEmail(Short.valueOf("0"));
                    }

                    if(desativarNotificacoes.isChecked()){
                        usuario.setExibirNotificacoes(Short.valueOf("0"));
                    } else {
                        usuario.setExibirNotificacoes(Short.valueOf("1"));
                    }

                    PreferenciasHelper ph = new PreferenciasHelper(getActivity());
                    try {
                        ph.salvar(usuario);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Erro:", e.getMessage());
                    }

                    Mensagem.alerta("Perfil atualizado com successo!", getActivity());

                }

            }
        });


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

    /**
     * Associa os elementos da activity às variáveis
     */
    private void bindingElements(){
        textEmail = (EditText) getView().findViewById(R.id.textEmail);
        textDataNascimento = (EditText) getView().findViewById(R.id.textDataNascimento);
        textNome = (EditText) getView().findViewById(R.id.textNome);
        textSenha = (EditText) getView().findViewById(R.id.textSenha);
        textSobrenome = (EditText) getView().findViewById(R.id.textSobrenome);
        textTelefone = (EditText) getView().findViewById(R.id.textTelefone);
        radioFem = (RadioButton) getView().findViewById(R.id.radioFem);
        radioMasc = (RadioButton) getView().findViewById(R.id.radioMasc);
        btnSelData = (Button) getView().findViewById(R.id.btnSelecionarDataNascimento);
        btnSalvar = (Button) getView().findViewById(R.id.btnSalvar);
        desativarNotificacoes = (CheckBox) getView().findViewById(R.id.checkDesativarNotificacoes);
        exibirEmail = (CheckBox) getView().findViewById(R.id.checkExibirEmail);
        textDataNascimento.setEnabled(false);

        textEmail.setEnabled(false);
    }

    /**
     * Atualiza o usuário
     * @param user
     * @return
     */
    private Boolean updateUsuario(Usuario user){
        try {
            firebase = ConfiguracaoFireBase.getFireBase().child("usuario");

            Map<String, Object> usuarioValues = user.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(user.getId(), usuarioValues);

            firebase.updateChildren(childUpdates);
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cria o datePicker
     */
    private void selecionarData() {
        btnSelData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                ano = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog  = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        textDataNascimento.setText(formatDate(year, month , dayOfMonth));
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
            }
        });
    }

    /**
     * Formata a data para o DatePicker
     * @param year
     * @param month
     * @param day
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(date);
    }

}
