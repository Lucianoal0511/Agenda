package com.studio.luciano.agendapmba.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.studio.luciano.agendapmba.R;
import com.studio.luciano.agendapmba.activity.EditarUnidadeActivity;
import com.studio.luciano.agendapmba.adapter.ContatosAdapter;
import com.studio.luciano.agendapmba.config.ConfiguracaoFirebase;
import com.studio.luciano.agendapmba.helper.RecyclerItemClickListener;
import com.studio.luciano.agendapmba.model.Unidade;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosEditarFragment extends Fragment {

    private RecyclerView recyclerViewListaContatos;
    private ContatosAdapter adapter;
    private ArrayList<Unidade> listaContatos = new ArrayList<>();
    private DatabaseReference unidadesRef;
    private ValueEventListener valueEventListenerContatos;

    public ContatosEditarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos_editar, container, false);

        //Configurações iniciais
        recyclerViewListaContatos = view.findViewById(R.id.recyclerViewListaContatosEditar);
        unidadesRef = ConfiguracaoFirebase.getFirebaseDatabase().child("unidade");

        //Configurar o adapter
        adapter = new ContatosAdapter(listaContatos, getActivity());

        //Configurar o recyclerView
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
                                Unidade unidadeSelecionada = listaContatos.get(position);
                                Intent i = new Intent(getActivity(), EditarUnidadeActivity.class);
                                i.putExtra("contatoUnidade", unidadeSelecionada);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, final int position) {

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                //Configurar o AlertDialog
                                alertDialog.setTitle("Excluir Unidade da Lista");
                                alertDialog.setMessage("Você tem certeza que deseja excluir essa Unidade da sua lista?");
                                alertDialog.setCancelable(false);
                                alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Unidade unidadeSelecionada = listaContatos.get(position);
                                        unidadeSelecionada.remover();
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), "Produto Excluído com sucesso", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                AlertDialog alert = alertDialog.create();
                                alert.show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        return view;
    }

    public void remove(){


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
