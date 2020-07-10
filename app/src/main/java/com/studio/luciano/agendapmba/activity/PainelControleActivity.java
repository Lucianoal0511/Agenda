package com.studio.luciano.agendapmba.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.studio.luciano.agendapmba.R;
import com.studio.luciano.agendapmba.config.ConfiguracaoFirebase;
import com.studio.luciano.agendapmba.fragment.ContatosEditarFragment;
import com.studio.luciano.agendapmba.fragment.CriminososEditarFragment;

public class PainelControleActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_controle);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Painel de Controle das Unidades");
        setSupportActionBar(toolbar);

        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Lista de Unidades", ContatosEditarFragment.class)
                .add("Em Branco", CriminososEditarFragment.class)
                .create()
        );

        final ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager(viewPager);

        //Configuração do search view
        searchView = findViewById(R.id.materialSearchPrincipal);
        //Listener para o search view
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ContatosEditarFragment fragmentContatosEdit = (ContatosEditarFragment) adapter.getPage(0);
                fragmentContatosEdit.recarregarUnidades();
            }
        });
        //Listener para caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {//Pesquisa após escrever e apertar pesquisar
                //Log.d("evento", "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {//Pesquisa a medida que vai escrevendo o texto
                //Log.d("evento", "onQueryTextChange");
                switch (viewPager.getCurrentItem()){
                    case 0:
                        ContatosEditarFragment fragmentContatosEdit = (ContatosEditarFragment) adapter.getPage(0);
                        if (newText != null && !newText.isEmpty()){
                            fragmentContatosEdit.pesquisarContatos(newText.toLowerCase());
                        }
                        break;
                    case 1:
                        CriminososEditarFragment fragmentCriminososEdit = (CriminososEditarFragment) adapter.getPage(1);
                        break;
                }

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_painel, menu);

        //Configurar botão de pesquisa
        MenuItem item = menu.findItem(R.id.menu_pesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_cadastrar:
                cadastrarUnidade();
                break;

            case R.id.menu_sair:
                deslogarUsuario();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cadastrarUnidade(){
        Intent intent = new Intent(this, CadastroUnidadeActivity.class);
        startActivity(intent);
    }

    public void deslogarUsuario(){
        try {
            autenticacao.signOut();
        }catch (Exception e){

        }
    }
}
