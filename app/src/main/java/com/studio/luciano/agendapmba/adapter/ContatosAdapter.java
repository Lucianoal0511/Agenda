package com.studio.luciano.agendapmba.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.studio.luciano.agendapmba.R;
import com.studio.luciano.agendapmba.model.Unidade;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder> {

    private List<Unidade> contatos;
    private Context context;

    public ContatosAdapter(List<Unidade> listaContatos, Context c) {
        this.contatos = listaContatos;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Unidade unidade = contatos.get(position);
        holder.nomeUnidade.setText(unidade.getNomeUnidade());
        holder.nomeCmd.setText(unidade.getNomeComandante());
        holder.funcaoCmd.setText(unidade.getFuncaoC());

        if (unidade.getFoto() != null){
            Uri uri = Uri.parse(unidade.getFoto());
            Glide.with(context).load(uri).into(holder.foto);
        }else {
            holder.foto.setImageResource(R.drawable.brasao_cipm);
        }
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nomeUnidade, nomeCmd, funcaoCmd;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.circleImageViewUnidade);
            nomeUnidade = itemView.findViewById(R.id.textNomeUnidade);
            nomeCmd = itemView.findViewById(R.id.textNomeCmd);
            funcaoCmd = itemView.findViewById(R.id.textFuncaoCmd);
        }
    }
}
