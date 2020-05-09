package com.studio.luciano.agendapmba.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.studio.luciano.agendapmba.R;
import com.studio.luciano.agendapmba.config.ConfiguracaoFirebase;
import com.studio.luciano.agendapmba.model.Desenvolvedor;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoSenha;
    private Button botaoUsuario, botaoLogar;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
        botaoUsuario = findViewById(R.id.bt_tela_principal);
        botaoLogar = findViewById(R.id.bt_logar);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        botaoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarAutenticacao();
            }
        });
    }

    public void logarDesenvolvedor(Desenvolvedor desenvolvedor){

        autenticacao.signInWithEmailAndPassword(
                desenvolvedor.getEmail(),
                desenvolvedor.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    abrirPainelControle();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!! ;)", Toast.LENGTH_SHORT).show();
                    //Limpar campos após logar
                    campoEmail.setText("");
                    campoSenha.setText("");
                }else {
                    //Lançar exceção
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao ="Usuário não está cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();;
                    }
                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarAutenticacao(){
        //Recuperar textos dos campos
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        //Validar se e-mail e senha foram digitados
        if (!textoEmail.isEmpty()){//verifica e-mail
            if (!textoSenha.isEmpty()){//Verifica senha
                Desenvolvedor desenvolvedor = new Desenvolvedor();
                desenvolvedor.setEmail(textoEmail);
                desenvolvedor.setSenha(textoSenha);

                logarDesenvolvedor(desenvolvedor);
            }else {
                Toast.makeText(this, "Preencha o campo senha!!!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Preencha seu e-mail!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //autenticacao.signOut();
        FirebaseUser desenvolvedorAtual = autenticacao.getCurrentUser();
        if (desenvolvedorAtual != null){
            Log.i("CreateUser", "Usuario logado!");
            abrirPainelControle();
        }else {
            Log.i("CreateUser", "Usuario não logado!");
        }
    }

    public void abrirPainelControle(){
        Intent intent = new Intent(this, PainelControleActivity.class);
        startActivity(intent);
    }

    public void abrirTelaCadastro(View view){
        startActivity(new Intent(this,CadastroActivity.class));
        finish();
    }
}
