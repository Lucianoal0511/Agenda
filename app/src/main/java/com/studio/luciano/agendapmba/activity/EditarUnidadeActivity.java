package com.studio.luciano.agendapmba.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.studio.luciano.agendapmba.R;
import com.studio.luciano.agendapmba.config.ConfiguracaoFirebase;
import com.studio.luciano.agendapmba.helper.Permissao;
import com.studio.luciano.agendapmba.model.Unidade;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class EditarUnidadeActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private TextInputEditText campoNomeUnidade, campoEmailUnidade, campoTelefoneFixo, campoNomeCmd, campoFuncaoCmd, campoTelefoneCmd,
            campoNomeScmd, campoFuncaoScmd, campoTelefoneScmd, campoNomeTerceiro, campoFuncaoTerceiro, campoTelefoneTerceiro,
            campoEnderecoUnidade, campoBairroUnidade, campoCidadeUnidade;
    private Button botaoEditar;
    private CircleImageView imagemUnidade;
    private ImageButton imageButtonCamera, imageButtonGaleria;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private DatabaseReference database;
    private Unidade unidade;
    private Unidade unidadeSelecao;
    private android.app.AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_unidade);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Colocar a seta de retornar

        //Configurações iniciais
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        unidade = new Unidade();
        database = ConfiguracaoFirebase.getFirebaseDatabase();

        //Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        //Configurar os campos
        campoNomeUnidade = findViewById(R.id.editNomeUnidade);
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
        imagemUnidade = findViewById(R.id.circleImageViewUnidade);
        imageButtonCamera = findViewById(R.id.imageButtonCamera);
        imageButtonGaleria = findViewById(R.id.imageButtonGaleria);

        botaoEditar = findViewById(R.id.bt_editarId);
        botaoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarCadastro(unidade);
            }
        });

        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) != null){//Testar se é possível utilizar a câmera
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });

        imageButtonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null){//Testar se é possível utilizar a câmera
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        //Recuperar dados da unidade
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            unidadeSelecao = (Unidade) bundle.getSerializable("contatoUnidade");
            campoNomeUnidade.setText(unidadeSelecao.getNomeUnidade());
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

            //Recuperar foto
            String foto = unidadeSelecao.getFoto();
            if (foto != null){
                Uri url = Uri.parse(unidadeSelecao.getFoto());
                Glide.with(EditarUnidadeActivity.this)
                        .load(url)
                        .into(imagemUnidade);
            }else {
                imagemUnidade.setImageResource(R.drawable.brasao_cipm);
            }
        }
    }

    //Vamos sobrescrever um método
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bitmap imagem = null;
            try {
                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                if (imagem != null){
                    imagemUnidade.setImageBitmap(imagem);
                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    byte[] dadosImagem = baos.toByteArray();
                    //Criar nome da imagem
                    //String nomeImagem = UUID.randomUUID().toString();
                    //Salvar imagem no Firebase no Storage
                    dialog = new SpotsDialog.Builder()
                            .setContext(this)
                            .setMessage("Salvando Imagem")
                            .setCancelable(false)
                            .build();//Bloqueia a tela com uma mensagem de salvar impedindo que seja realizada outra operação até quando for salvo
                    dialog.show();

                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("PerfilUnidade")
                            .child(unidadeSelecao.getId() + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);//Fazer o upload da imagem
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditarUnidadeActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditarUnidadeActivity.this,
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                            imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    unidadeSelecao.setFoto(url);

                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //Permitir a utilização do Cartão de memória e da Câmera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResultado: grantResults){
            if (permissaoResultado == PackageManager.PERMISSION_DENIED){

            }
        }
    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões solicitadas");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void atualizarCadastro(Unidade unidade){

        try {
            String textoNomeUnidade = campoNomeUnidade.getText().toString();
            String textoEmailUnidade = campoEmailUnidade.getText().toString();
            String textoTelefoneFixo = campoTelefoneFixo.getText().toString();
            String textoNomeCmd = campoNomeCmd.getText().toString();
            String textoTelefoneCmd = campoTelefoneCmd.getText().toString();
            String textoNomeScmd = campoNomeScmd.getText().toString();
            String textoTelefoneScmd = campoTelefoneScmd.getText().toString();
            String textoNomeTerceiro = campoNomeTerceiro.getText().toString();
            String textoTelefoneTerceiro = campoTelefoneTerceiro.getText().toString();

            unidadeSelecao.setNomeUnidade(textoNomeUnidade);
            unidadeSelecao.setEmailUnidade(textoEmailUnidade);
            unidadeSelecao.setTelefoneFixo(textoTelefoneFixo);
            unidadeSelecao.setNomeComandante(textoNomeCmd);
            unidadeSelecao.setTelefoneComandante(textoTelefoneCmd);
            unidadeSelecao.setNomeSubcomandante(textoNomeScmd);
            unidadeSelecao.setTelefoneSubcomandante(textoTelefoneScmd);
            unidadeSelecao.setOutroNome(textoNomeTerceiro);
            unidadeSelecao.setTelefone3(textoTelefoneTerceiro);
            unidadeSelecao.atualizarUnidade();
            finish();
            Toast.makeText(this, "Sucesso ao editar unidade.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
