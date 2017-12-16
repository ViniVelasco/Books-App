package cmp1144.pucgoias.com.booksapp.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import cmp1144.pucgoias.com.booksapp.DAO.ConfiguracaoFireBase;
import cmp1144.pucgoias.com.booksapp.Entidades.Usuario;
import cmp1144.pucgoias.com.booksapp.Fragment.CadastroLivros;
import cmp1144.pucgoias.com.booksapp.Fragment.CadastroUsuarios;
import cmp1144.pucgoias.com.booksapp.Fragment.ConsultarLivros;
import cmp1144.pucgoias.com.booksapp.Fragment.FragmentPerfilUsuario;
import cmp1144.pucgoias.com.booksapp.Fragment.LivrosUsuario;
import cmp1144.pucgoias.com.booksapp.Fragment.Login;
import cmp1144.pucgoias.com.booksapp.Fragment.MainApresentation;
import cmp1144.pucgoias.com.booksapp.Fragment.Sobre;
import cmp1144.pucgoias.com.booksapp.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth autenticao;
    private Usuario usuario;
    public static boolean usuarioLogado = false;
    static MenuItem cadastro, login, buscarLivros, cadastroLivros, meusLivros, perfil;
    public static String emailUsuario;

    /**
     * Método onCreate, iniciado quando a Activity Main é criada.
     * Criação do Drawable Bar.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new MainApresentation()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        cadastro =  menu.findItem(R.id.action_settings);
        cadastro.setVisible(false);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        navigationMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Método que organiza os itens do menu de acordo com o nível de permissão do usuário.
     */
    public void navigationMenu(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem meusLivros = menu.findItem(R.id.nav_meus_livros);
        MenuItem cadastro = menu.findItem(R.id.nav_cadastro);
        MenuItem login = menu.findItem(R.id.nav_login);
        MenuItem buscarLivros = menu.findItem(R.id.nav_buscar_livros);
        MenuItem cadastroLivros = menu.findItem(R.id.nav_cadastro_livros);
        MenuItem perfil = menu.findItem(R.id.nav_perfil);
        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = (TextView) headerView.findViewById(R.id.textEmailNavBar);
        if(usuarioLogado){
            navEmail.setText(emailUsuario);
            meusLivros.setVisible(true);
            cadastro.setVisible(false);
            login.setVisible(false);
            buscarLivros.setVisible(true);
            cadastroLivros.setVisible(true);
            perfil.setVisible(true);
        } else {
            meusLivros.setVisible(false);
            cadastro.setVisible(true);
            login.setVisible(true);
            buscarLivros.setVisible(false);
            cadastroLivros.setVisible(false);
            perfil.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new FragmentPerfilUsuario()).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        // Handle navigation view item clicks here.

        if (id == R.id.nav_login) {
            // Handle the camera action
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new Login()).commit();
        } else if (id == R.id.nav_cadastro) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new CadastroUsuarios()).commit();

        } else if (id == R.id.nav_sobre) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new Sobre()).commit();
        } else if (id == R.id.nav_cadastro_livros){
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new CadastroLivros()).commit();
        } else if (id == R.id.nav_meus_livros){
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new LivrosUsuario()).commit();
        } else if (id == R.id.nav_perfil){
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new FragmentPerfilUsuario()).commit();
        } else if (id == R.id.nav_buscar_livros){
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ConsultarLivros()).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void alerta(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
