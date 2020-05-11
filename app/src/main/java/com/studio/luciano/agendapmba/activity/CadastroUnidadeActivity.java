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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.studio.luciano.agendapmba.R;
import com.studio.luciano.agendapmba.config.ConfiguracaoFirebase;
import com.studio.luciano.agendapmba.helper.Permissao;
import com.studio.luciano.agendapmba.model.Unidade;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroUnidadeActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private TextInputEditText campoNomeUnidade, campoEmailUnidade, campoTelefoneFixo, campoNomeCmd, campoFuncaoCmd, campoTelefoneCmd,
            campoNomeScmd, campoFuncaoScmd, campoTelefoneScmd, campoNomeTerceiro, campoFuncaoTerceiro, campoTelefoneTerceiro,
            campoEnderecoUnidade, campoBairroUnidade, campoCidadeUnidade;
    private Button botaoSalvar;
    private CircleImageView imagemUnidade;
    private ImageButton imageButtonCamera, imageButtonGaleria;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private Unidade unidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_unidade);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Cadastro de Unidades");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Colocar a seta de retorno

        //Configurações iniciais
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        //identificadorUnidade = UnidadeFirebase.getIdentificadorUnidade();
        unidade = new Unidade();

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

        botaoSalvar = findViewById(R.id.bt_salvarId);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNomeUnidade = campoNomeUnidade.getText().toString();
                String textoEmailUnidade = campoEmailUnidade.getText().toString();
                String textoTelefoneFixo = campoTelefoneFixo.getText().toString();
                String textoNomeCmd = campoNomeCmd.getText().toString();
                String textoFuncaoCmd = campoFuncaoCmd.getText().toString();
                String textoTelefoneCmd = campoTelefoneCmd.getText().toString();
                String textoNomeScmd = campoNomeScmd.getText().toString();
                String textoFuncaoScmd = campoFuncaoScmd.getText().toString();
                String textoTelefoneScmd = campoTelefoneScmd.getText().toString();
                String textoNomeTerceiro = campoNomeTerceiro.getText().toString();
                String textoFuncaoTerceiro = campoFuncaoTerceiro.getText().toString();
                String textoTelefoneTerceiro = campoTelefoneTerceiro.getText().toString();
                String textoEnderecoUnidade = campoEnderecoUnidade.getText().toString();
                String textoBairroUnidade = campoBairroUnidade.getText().toString();
                String textoCidadeUnidade = campoCidadeUnidade.getText().toString();

                unidade.setNomeUnidade(textoNomeUnidade);
                unidade.setEmailUnidade(textoEmailUnidade);
                unidade.setTelefoneFixo(textoTelefoneFixo);
                unidade.setNomeComandante(textoNomeCmd);
                unidade.setFuncaoC(textoFuncaoCmd);
                unidade.setTelefoneComandante(textoTelefoneCmd);
                unidade.setNomeSubcomandante(textoNomeScmd);
                unidade.setFuncaoSC(textoFuncaoScmd);
                unidade.setTelefoneSubcomandante(textoTelefoneScmd);
                unidade.setOutroNome(textoNomeTerceiro);
                unidade.setFuncao3(textoFuncaoTerceiro);
                unidade.setTelefone3(textoTelefoneTerceiro);
                unidade.setEndereco(textoEnderecoUnidade);
                unidade.setBairro(textoBairroUnidade);
                unidade.setCidade(textoCidadeUnidade);

                try {
                    unidade.salvarUnidade();
                    Toast.makeText(CadastroUnidadeActivity.this, "Sucesso ao salvar unidade.", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(CadastroUnidadeActivity.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                //validarCamposUnidade();
                //Limpar campos após cadastrar
                campoNomeUnidade.setText("");
                campoEmailUnidade.setText("");
                campoNomeCmd.setText("");
                campoTelefoneFixo.setText("");
                campoFuncaoCmd.setText("");
                campoTelefoneCmd.setText("");
                campoNomeScmd.setText("");
                campoFuncaoScmd.setText("");
                campoTelefoneScmd.setText("");
                campoNomeTerceiro.setText("");
                campoFuncaoTerceiro.setText("");
                campoTelefoneTerceiro.setText("");
                campoEnderecoUnidade.setText("");
                campoBairroUnidade.setText("");
                campoCidadeUnidade.setText("");
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
                    //Salvar imagem no Firebase Storage
                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("PerfilUnidade")
                            .child(unidade.getId() + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);//Fazer o upload da imagem
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CadastroUnidadeActivity.this,
                                    "Erro ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CadastroUnidadeActivity.this,
                                    "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                            imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    unidade.setFoto(url);
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

    //Não precisou ser utilizado
    public void validarCamposUnidade(){
        //Recuperar textos dos campos
        String textoNomeUnidade = campoNomeUnidade.getText().toString();
        String textoEmailUnidade = campoEmailUnidade.getText().toString();
        String textoTelefoneFixo = campoTelefoneFixo.getText().toString();
        String textoNomeCmd = campoNomeCmd.getText().toString();
        String textoFuncaoCmd = campoFuncaoCmd.getText().toString();
        String textoTelefoneCmd = campoTelefoneCmd.getText().toString();
        String textoNomeScmd = campoNomeScmd.getText().toString();
        String textoFuncaoScmd = campoFuncaoScmd.getText().toString();
        String textoTelefoneScmd = campoTelefoneScmd.getText().toString();
        String textoNomeTerceiro = campoNomeTerceiro.getText().toString();
        String textoFuncaoTerceiro = campoFuncaoTerceiro.getText().toString();
        String textoTelefoneTerceiro = campoTelefoneTerceiro.getText().toString();
        String textoEnderecoUnidade = campoEnderecoUnidade.getText().toString();
        String textoBairroUnidade = campoBairroUnidade.getText().toString();
        String textoCidadeUnidade = campoCidadeUnidade.getText().toString();

        if (!textoNomeUnidade.isEmpty()){
            if (!textoEmailUnidade.isEmpty()){
                if (!textoTelefoneFixo.isEmpty()){
                    if (!textoNomeCmd.isEmpty()){
                        if (!textoFuncaoCmd.isEmpty()){
                            if (!textoTelefoneCmd.isEmpty()){
                                if (!textoNomeScmd.isEmpty()){
                                    if (!textoFuncaoScmd.isEmpty()){
                                        if (!textoTelefoneScmd.isEmpty()){
                                            if (!textoEnderecoUnidade.isEmpty()){
                                                if (!textoBairroUnidade.isEmpty()){
                                                    if (!textoCidadeUnidade.isEmpty()){
                                                        if (!textoNomeTerceiro.isEmpty()){
                                                            if (!textoFuncaoTerceiro.isEmpty()){
                                                                if (!textoTelefoneTerceiro.isEmpty()){
                                                                    Unidade unidade = new Unidade();
                                                                    unidade.setNomeUnidade(textoNomeUnidade);
                                                                    unidade.setEmailUnidade(textoEmailUnidade);
                                                                    unidade.setTelefoneFixo(textoTelefoneFixo);
                                                                    unidade.setNomeComandante(textoNomeCmd);
                                                                    unidade.setFuncaoC(textoFuncaoCmd);
                                                                    unidade.setTelefoneComandante(textoTelefoneCmd);
                                                                    unidade.setNomeSubcomandante(textoNomeScmd);
                                                                    unidade.setFuncaoSC(textoFuncaoScmd);
                                                                    unidade.setTelefoneSubcomandante(textoTelefoneScmd);
                                                                    unidade.setOutroNome(textoNomeTerceiro);
                                                                    unidade.setFuncao3(textoFuncaoTerceiro);
                                                                    unidade.setTelefone3(textoTelefoneTerceiro);
                                                                    unidade.setEndereco(textoEnderecoUnidade);
                                                                    unidade.setBairro(textoBairroUnidade);
                                                                    unidade.setCidade(textoCidadeUnidade);

                                                                    //cadastrarUnidade(unidade);
                                                                    //Limpar campos após cadastrar
                                                                    campoNomeUnidade.setText("");
                                                                    campoEmailUnidade.setText("");
                                                                    campoNomeCmd.setText("");
                                                                    campoTelefoneFixo.setText("");
                                                                    campoFuncaoCmd.setText("");
                                                                    campoTelefoneCmd.setText("");
                                                                    campoNomeScmd.setText("");
                                                                    campoFuncaoScmd.setText("");
                                                                    campoTelefoneScmd.setText("");
                                                                    campoNomeTerceiro.setText("");
                                                                    campoFuncaoTerceiro.setText("");
                                                                    campoTelefoneTerceiro.setText("");
                                                                    campoEnderecoUnidade.setText("");
                                                                    campoBairroUnidade.setText("");
                                                                    campoCidadeUnidade.setText("");
                                                                }else {
                                                                    textoTelefoneTerceiro.isEmpty();
                                                                }
                                                            }else {
                                                                textoTelefoneTerceiro.isEmpty();
                                                            }
                                                        }else {
                                                            textoTelefoneTerceiro.isEmpty();
                                                        }
                                                    }else {
                                                        Toast.makeText(this, "Preencha a cidade da unidade policial", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else {
                                                    Toast.makeText(this, "Preencha o bairro da unidade policial", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(this, "Preencha o endereço da unidade policial", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(this, "Preencha o número de telefone do Subcomandante", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(this, "Preencha a função do Subcomandante", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(this, "Preencha o nome do Subcomandante", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(this, "Preencha o número de telefone do Comandante", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(this, "Preencha a função do Comandante", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "Preencha o nome do comandante da unidade.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Preencha o número do telefone fixo", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Preencha o email da unidade policial.", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Preencha o nome da unidade policial.", Toast.LENGTH_SHORT).show();
        }
    }
}
