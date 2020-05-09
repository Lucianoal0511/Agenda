package com.studio.luciano.agendapmba.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studio.luciano.agendapmba.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CriminososEditarFragment extends Fragment {

    public CriminososEditarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_criminosos_editar, container, false);
    }
}
