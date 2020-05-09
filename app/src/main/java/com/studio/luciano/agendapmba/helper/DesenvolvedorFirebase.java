package com.studio.luciano.agendapmba.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.studio.luciano.agendapmba.config.ConfiguracaoFirebase;

public class DesenvolvedorFirebase {

    public static String getIdentificadorDesenvolvedor(){

        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String email = usuario.getCurrentUser().getEmail();
        String identificadorUsuario = Base64Custom.codificarBase64(email);

        return identificadorUsuario;
    }

    public static FirebaseUser getDesenvolvedorAtual(){

        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    /*public static boolean atualizarCadastroUnidade(String nome){

        try {
            FirebaseUser user = getDesenvolvedorAtual();
            DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDatabase();
            DatabaseReference unidadeRef = firebaseref.child("unidade");

        }catch (Exception e) {
            e.printStackTrace();
            return atualizarCadastroUnidade();
        }
    }*/

    public static boolean atualizaFotoUnidade(Uri url) {

        try {
            FirebaseUser desenvolve = getDesenvolvedorAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();
            desenvolve.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("PerfilUnidade", "Erro ao atualizar foto ");
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
