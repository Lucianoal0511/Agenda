package com.studio.luciano.agendapmba.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studio.luciano.agendapmba.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalFragment extends Fragment {

    public PrincipalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String descricao = "Este App tem como objetivo aprimorar a comunicação interna na coorporação " +
                "oferecendo uma lista de contatos de todas Unidades Administrativas e Operacionais da PMBA";

        //Colocar Versão do App
        Element versao = new Element();
        versao.setTitle("Versão 1.8");

        return new AboutPage(getActivity())
                .setImage(R.drawable.brasaopmba)//Coloca o brasão da PMBA
                .setDescription(descricao)
                .addGroup("Entre em contato")
                .addWebsite("www.pm.ba.gov.br", "Acesse nosso site")
                .addGroup("Redes Sociais")
                .addFacebook("pmdabahia", "Facebook")
                .addInstagram("pmdabahia", "Instagram")
                .addTwitter("pmdabahia", "Twitter")
                .addItem(versao)
                .create();

    }
}
