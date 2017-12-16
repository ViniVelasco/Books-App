package cmp1144.pucgoias.com.booksapp.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

import cmp1144.pucgoias.com.booksapp.DAO.ConfiguracaoFireBase;
import cmp1144.pucgoias.com.booksapp.Entidades.Usuario;
import cmp1144.pucgoias.com.booksapp.Helper.Base64Custom;
import cmp1144.pucgoias.com.booksapp.Helper.Mensagem;
import cmp1144.pucgoias.com.booksapp.Helper.Preferencias;
import cmp1144.pucgoias.com.booksapp.R;

public class CadastroUsuarios extends Fragment {

    View myView;
    EditText textNome, textSobrenome, textEmail, textSenha, textDataNascimento, textTelefone;
    RadioButton radioMasc, radioFem;
    Button btnSelecionarData, btnCadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private int ano, mes, dia;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.cadastro_usuarios, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingComponents();
        selecionarData();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textNome.getText().toString().isEmpty() || textEmail.getText().toString().isEmpty()
                        || textSenha.getText().toString().isEmpty() || textSobrenome.getText().toString().isEmpty()
                        || textSobrenome.getText().toString().isEmpty() || textDataNascimento.getText().toString().isEmpty()
                        || textTelefone.getText().toString().isEmpty()){
                    Mensagem.alerta("Por favor, preencha todos os campos", getContext());
                } else {
                    usuario = new Usuario();
                    usuario.setNome(textNome.getText().toString());
                    usuario.setEmail(textEmail.getText().toString());
                    usuario.setSenha(textSenha.getText().toString());
                    usuario.setSobrenome(textSobrenome.getText().toString());
                    usuario.setDataNascimento(textDataNascimento.getText().toString());
                    usuario.setTelefone(textTelefone.getText().toString());
                    if(radioMasc.isChecked()){
                        usuario.setSexo("Masculino");
                    } else {
                        usuario.setSexo("Feminino");
                    }

                    cadastrarUsuario();

                }
            }
        });
    }

    /**
     * Associa os elementos da activity às variáveis
     */
    public void bindingComponents(){
        textNome = (EditText) getView().findViewById(R.id.textNome);
        textSobrenome = (EditText) getView().findViewById(R.id.textSobrenome);
        textEmail = (EditText) getView().findViewById(R.id.textEmail);
        textSenha = (EditText) getView().findViewById(R.id.textSenha);
        textTelefone = (EditText) getView().findViewById(R.id.textTelefone);
        textDataNascimento = (EditText) getView().findViewById(R.id.textDataNascimento);
        textDataNascimento.setEnabled(false);

        radioMasc = (RadioButton) getView().findViewById(R.id.radioMasc);
        radioFem = (RadioButton) getView().findViewById(R.id.radioFem);

        btnSelecionarData = (Button) getView().findViewById(R.id.btnSelecionarData);
        btnCadastrar = (Button) getView().findViewById(R.id.btnCadastrar);
    }

    /**
     * Cadastra o usuário no WebService
     */
    private void cadastrarUsuario(){

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                   alerta("Usuário cadastrado com sucesso!");

                   String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());

                   FirebaseUser usuarioFirebase = task.getResult().getUser();
                   usuario.setId(identificadorUsuario);
                   usuario.salvar();

                    Preferencias preferencias = new Preferencias(getContext());
                    preferencias.salvarUsuarioPreferencias(identificadorUsuario, usuario.getNome());

                } else {
                    String erro = "";
                    try {
                         throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha forte, contendo no mínimo 8 caracteres de letras e números.";
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        erro = "O e-mail digitado é inválido, digite um novo e-mail.";
                    } catch(FirebaseAuthUserCollisionException e) {
                        erro = "Esse e-mail já está cadastrado.";
                    } catch(Exception e) {
                        erro = "Erro ao efetuar o cadastrado, verifique a conexão com a internet.";
                        e.printStackTrace();
                    }

                    alerta(erro);
                }
            }
        });

    }

    /**
     * Método que cria o DatePicker para a seleção de data de nascimento
     */
    private void selecionarData() {
        btnSelecionarData.setOnClickListener(new View.OnClickListener() {
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
     * Método que formata a data do DatePicker
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

    private void alerta(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
