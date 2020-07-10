package com.studio.luciano.agendapmba.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.studio.luciano.agendapmba.R;
import com.studio.luciano.agendapmba.model.Unidade;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContatoUnidadeActivity extends AppCompatActivity {

    private TextInputEditText campoNomeUnidade, campoEmailUnidade, campoTelefoneFixo, campoNomeCmd, campoFuncaoCmd, campoTelefoneCmd,
            campoNomeScmd, campoFuncaoScmd, campoTelefoneScmd, campoNomeTerceiro, campoFuncaoTerceiro, campoTelefoneTerceiro,
            campoEnderecoUnidade, campoBairroUnidade, campoCidadeUnidade, campoLocal;
    private CircleImageView imagemUnidade;
    private TextView textViewTitulo;
    private Unidade unidadeSelecao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato_unidade);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Colocar o botão voltar

        //Configurar os campos
        textViewTitulo = findViewById(R.id.editNomeUnidade);
        //campoNomeUnidade = findViewById(R.id.editNomeUnidade);
        campoEmailUnidade = findViewById(R.id.editEmailUnidade);
        campoTelefoneFixo = findViewById(R.id.editTelefoneFixo);
        campoNomeCmd = findViewById(R.id.editNomeComandante);
        campoFuncaoCmd = findViewById(R.id.editFuncaoCmd);
        campoTelefoneCmd = findViewById(R.id.editPhoneCmd);
        campoNomeScmd = findViewById(R.id.editNomeScmd);
        campoFuncaoScmd = findViewById(R.id.editFuncaoScmd);
        campoTelefoneScmd = findViewById(R.id.editPhoneScmd);
        campoNomeTerceiro = findViewById(R.id.editNome3);
        campoFuncaoTerceiro = findViewById(R.id.editfuncao3);
        campoTelefoneTerceiro = findViewById(R.id.editPhone3);
        campoEnderecoUnidade = findViewById(R.id.editEndereco);
        campoBairroUnidade = findViewById(R.id.editBairro);
        campoCidadeUnidade = findViewById(R.id.editCidade);
        campoLocal = findViewById(R.id.textoLocal);
        imagemUnidade = findViewById(R.id.circleImageViewUnidade);

        //Recuperar dados da unidade
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            unidadeSelecao = (Unidade) bundle.getSerializable("contatoUnidade");
            //campoNomeUnidade.setText(unidadeSelecao.getNomeUnidade());
            textViewTitulo.setText(unidadeSelecao.getNomeUnidade());
            campoEmailUnidade.setText(unidadeSelecao.getEmailUnidade());
            campoTelefoneFixo.setText(unidadeSelecao.getTelefoneFixo());
            campoNomeCmd.setText(unidadeSelecao.getNomeComandante());
            campoFuncaoCmd.setText(unidadeSelecao.getFuncaoC());
            campoTelefoneCmd.setText(unidadeSelecao.getTelefoneComandante());
            campoNomeScmd.setText(unidadeSelecao.getNomeSubcomandante());
            campoFuncaoScmd.setText(unidadeSelecao.getFuncaoSC());
            campoTelefoneScmd.setText(unidadeSelecao.getTelefoneSubcomandante());
            campoNomeTerceiro.setText(unidadeSelecao.getOutroNome());
            campoFuncaoTerceiro.setText(unidadeSelecao.getFuncao3());
            campoTelefoneTerceiro.setText(unidadeSelecao.getTelefone3());
            campoEnderecoUnidade.setText(unidadeSelecao.getEndereco());
            campoBairroUnidade.setText(unidadeSelecao.getBairro());
            campoCidadeUnidade.setText(unidadeSelecao.getCidade());
            campoLocal.setText(unidadeSelecao.getLocais());

            //Recuperar foto
            String foto = unidadeSelecao.getFoto();
            if (foto != null){
                Uri url = Uri.parse(unidadeSelecao.getFoto());
                Glide.with(ContatoUnidadeActivity.this)
                        .load(url)
                        .into(imagemUnidade);
            }else {
                imagemUnidade.setImageResource(R.drawable.brasao_cipm);
            }
        }
    }

    public void discarCmd(View view){

        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + unidadeSelecao.getTelefoneComandante()));
        startActivity(i);
    }

    public void discarScmd(View view){

        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + unidadeSelecao.getTelefoneSubcomandante()));
        startActivity(i);
    }

    public void discarFuncional(View view){

        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + unidadeSelecao.getTelefone3()));
        startActivity(i);
    }

    public void discarFixo(View view){

        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + unidadeSelecao.getTelefoneFixo()));
        startActivity(i);
    }

    public void chamarEmail(View view){

        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{unidadeSelecao.getEmailUnidade()});
        //i.setType("message/rfc822");//Tipo e-mail
        i.setType("text/plain");
        startActivity(i.createChooser(i, "Compartilhar"));
    }

    /*public void abrirRegiao(View view){

        //Intent i = new Intent()
    }*/
}
