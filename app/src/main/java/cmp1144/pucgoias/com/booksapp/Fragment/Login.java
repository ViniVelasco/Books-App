package cmp1144.pucgoias.com.booksapp.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import cmp1144.pucgoias.com.booksapp.Activity.MainActivity;
import cmp1144.pucgoias.com.booksapp.DAO.ConfiguracaoFireBase;
import cmp1144.pucgoias.com.booksapp.Entidades.Usuario;
import cmp1144.pucgoias.com.booksapp.R;

/**
 * Created by vinic on 22/11/2017.
 */

public class Login extends Fragment {

    View myView;

    private EditText textEmail, textSenha;
    private Button btnLogin;
    private FirebaseAuth autenticao;
    private Usuario usuario;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textEmail = (EditText) getView().findViewById(R.id.textEmail);
        textSenha = (EditText) getView().findViewById(R.id.textSenha);
        btnLogin = (Button) getView().findViewById(R.id.btnLogin);
        bindingComponentes();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_login, container, false);
        return myView;
    }

    /**
     * Associa os elementos da activity às variáveis
     */
    private void bindingComponentes(){

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textEmail.getText().toString().isEmpty() && !textSenha.getText().toString().isEmpty()){
                    usuario = new Usuario();
                    usuario.setEmail(textEmail.getText().toString());
                    usuario.setSenha(textSenha.getText().toString());
                    validarLogin();

                } else {
                    alerta("Preencha os campos de e-mail e senha!");
                }
            }
        });
    }

    /**
     * Valida o login
     */
    private void validarLogin(){

        autenticao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        autenticao.signInWithEmailAndPassword(usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    alerta("Login efetuado com sucesso!");
                    MainActivity.usuarioLogado = true;
                    getActivity().invalidateOptionsMenu();
                    MainActivity.emailUsuario = textEmail.getText().toString();

                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.content_frame, new LivrosUsuario()).commit();
                } else {
                    alerta("Usuário ou senha inválidos");
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Exibe um Toast
     * @param msg
     */
    private void alerta(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
