package com.studio.luciano.agendapmba.helper;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.studio.luciano.agendapmba.config.ConfiguracaoFirebase;
import com.studio.luciano.agendapmba.model.Unidade;

public class UnidadeFirebase {

    private Unidade unidade;

    public static String getIdentificadorUnidade(){

        Unidade unidade = new Unidade();
        unidade.setId(getIdentificadorUnidade());
        /*String identificadorUnidade = Base64Custom.codificarBase64(unidade.getEmailUnidade());
        unidade.setId(identificadorUnidade);*/

        return getIdentificadorUnidade();
    }

    public static FirebaseDatabase getUnidadeSelecao(){

        DatabaseReference unidade = ConfiguracaoFirebase.getFirebaseDatabase();
        Unidade unidadeSelecionada = new Unidade();
        unidadeSelecionada.setId(unidade.getKey());

        return getUnidadeSelecao();
    }

    public static boolean atualizarFotoUnidade(){

        Unidade unidade = new Unidade();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference unidadeRef = database.child("unidade");
        unidadeRef.updateChildren(unidade.converterMap());

        return atualizarFotoUnidade();
    }
}
