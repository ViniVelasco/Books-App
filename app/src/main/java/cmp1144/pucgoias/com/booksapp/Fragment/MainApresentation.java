package cmp1144.pucgoias.com.booksapp.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import cmp1144.pucgoias.com.booksapp.R;

/**
 * Created by vinic on 13/12/2017.
 */

public class MainApresentation extends Fragment {

    View myView;
    EditText textoBase;

    /**
     * Cria o fragmento de Apresentação do aplicativo
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.main_apresentation, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().findViewById(R.id.textoBase).setEnabled(false);
    }
}
