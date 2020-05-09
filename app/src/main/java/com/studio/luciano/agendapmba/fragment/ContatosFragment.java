package com.studio.luciano.agendapmba.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.studio.luciano.agendapmba.R;
import com.studio.luciano.agendapmba.activity.ContatoUnidadeActivity;
import com.studio.luciano.agendapmba.adapter.ContatosAdapter;
import com.studio.luciano.agendapmba.config.ConfiguracaoFirebase;
import com.studio.luciano.agendapmba.helper.RecyclerItemClickListener;
import com.studio.luciano.agendapmba.model.Unidade;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private RecyclerView recyclerViewListaContatos;
    private ContatosAdapter adapter;
    private ArrayList<Unidade> listaContatos = new ArrayList<>();
    private DatabaseReference unidadesRef;
    private ValueEventListener valueEventListenerContatos;

    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        //Configurações iniciais
        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaContatos);
        unidadesRef = ConfiguracaoFirebase.getFirebaseDatabase().child("unidade");

        //Configurar adapter
        adapter = new ContatosAdapter(listaContatos, getActivity());

        //Configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListaContatos.setLayoutManager(layoutManager);
        recyclerViewListaContatos.setHasFixedSize(true);
        recyclerViewListaContatos.setAdapter(adapter);

        //Configurar evento de clique no recyclerview
        recyclerViewListaContatos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewListaContatos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                List<Unidade> listaContatosAtualizada = adapter.getContatos();//Lista criada para na busca você pegar o item correto
                                Unidade unidadeSelecionada = listaContatosAtualizada.get(position);//Recupera o item clicado
                                Intent i = new Intent(getActivity(), ContatoUnidadeActivity.class);
                                i.putExtra("contatoUnidade", unidadeSelecionada);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        return view;
    }

    public void pesquisarContatos(String texto){
        Log.d("pesquisa", texto);
        List<Unidade> listaUnidadesBusca = new ArrayList<>();
        for (Unidade unidade: listaContatos) {
            String nomeUnidade = unidade.getNomeUnidade().toLowerCase();
            String nomeCmd = unidade.getNomeComandante().toLowerCase();
            if (nomeUnidade.contains(texto) || nomeCmd.contains(texto)){
                listaUnidadesBusca.add(unidade);
            }
        }
        adapter = new ContatosAdapter(listaUnidadesBusca, getActivity());
        recyclerViewListaContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recarregarUnidades(){
        adapter = new ContatosAdapter(listaContatos, getActivity());
        recyclerViewListaContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void recuperarUnidades(){
        valueEventListenerContatos = unidadesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Unidade unidade = dados.getValue(Unidade.class);
                    listaContatos.add(unidade);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarUnidades();
    }

    @Override
    public void onStop() {
        super.onStop();
        unidadesRef.removeEventListener(valueEventListenerContatos);
    }
}
