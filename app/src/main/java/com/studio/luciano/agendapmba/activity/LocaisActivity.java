package com.studio.luciano.agendapmba.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.studio.luciano.agendapmba.R;
import com.studio.luciano.agendapmba.model.Unidade;

public class LocaisActivity extends AppCompatActivity {

    private TextView textViewLocais;
    private Unidade unidadeSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locais);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Região de Atuação");
        setSupportActionBar(toolbar);

        textViewLocais = findViewById(R.id.textViewLocais);

        //Recuperar as regiões de atuação da unidade
        unidadeSelecionada = (Unidade) getIntent().getSerializableExtra("unidadeLocais");
        if (unidadeSelecionada != null){
            textViewLocais.setText(unidadeSelecionada.getLocais());
        }
    }
}