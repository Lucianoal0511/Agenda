package com.studio.luciano.agendapmba.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.studio.luciano.agendapmba.R;
import com.studio.luciano.agendapmba.config.ConfiguracaoFirebase;
import com.studio.luciano.agendapmba.helper.Base64Custom;
import com.studio.luciano.agendapmba.model.Desenvolvedor;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoCodigo, campoSenha, campoConfSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Cadastro de Desenvolvedores");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Colocando a seta de retorno

        campoNome = findViewById(R.id.editLoginNome);
        campoEmail = findViewById(R.id.editLoginEmail);
        campoCodigo = findViewById(R.id.editLoginCodigo);
        campoSenha = findViewById(R.id.editLoginSenha);
        campoConfSenha = findViewById(R.id.editConfirSenha);

    }

    public void cadastrarDesenvolvedor(final Desenvolvedor desenvolvedor){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                desenvolvedor.getEmail(), desenvolvedor.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar desenvolvedor!!!", Toast.LENGTH_SHORT).show();
                    finish();
                    try {
                        String identificadorDesenvolvedor = Base64Custom.codificarBase64(desenvolvedor.getEmail());
                        desenvolvedor.setId(identificadorDesenvolvedor);
                        desenvolvedor.salvar();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor, digite um e-mail válido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já foi cadastrada!";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarCadastroUsuario(View view){//Utiliza View como parâmetro pois foi utilizado o onClick no xml

        //Recuperar textos dos campos
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoCodigo = campoCodigo.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        String textoConfSenha = campoConfSenha.getText().toString();

        if (!textoNome.isEmpty()){//Verifica nome
            if (!textoEmail.isEmpty()){//Verifica e-mail
                if (!textoCodigo.isEmpty()){//Verifica o código
                    if (textoCodigo.equals("admin_master2076")){//Valida o código
                        if (!textoSenha.isEmpty()){//Verifica a senha
                            if (!textoConfSenha.isEmpty()){//Confirma a senha
                                if (textoSenha.equals(textoConfSenha)){
                                    Desenvolvedor desenvolvedor = new Desenvolvedor();
                                    desenvolvedor.setNome(textoNome);
                                    desenvolvedor.setEmail(textoEmail);
                                    desenvolvedor.setCodigo(textoCodigo);
                                    desenvolvedor.setSenha(textoSenha);

                                    cadastrarDesenvolvedor(desenvolvedor);
                                }else {
                                    Toast.makeText(this, "Senha incorreta, repita por favor!!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(this, "Confirme sua senha!!", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(this, "Preencha sua senha!!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "Você Não Possui o Código do Desenvolvedor, Favor entrar em contato com o suporte!!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Preencha o campo código!!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Preencha seu e-mail!!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Preencha seu nome!!", Toast.LENGTH_SHORT).show();
        }
    }
}
