package com.studio.luciano.agendapmba.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.studio.luciano.agendapmba.R;
import com.studio.luciano.agendapmba.fragment.ContatosEditarFragment;
import com.studio.luciano.agendapmba.fragment.ContatosFragment;
import com.studio.luciano.agendapmba.fragment.PrincipalFragment;

public class MainActivity extends AppCompatActivity {

    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Agenda PMBA");
        setSupportActionBar(toolbar);

        //Configurar abas
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Contatos Unidades", ContatosFragment.class)
                .add("Contato PMBA", PrincipalFragment.class)
                .create()
        );
        ViewPager viewPager = findViewById(R.id.smartTab);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.smartTabLayout);
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
                ContatosFragment fragmentContatos = (ContatosFragment) adapter.getPage(0);
                fragmentContatos.recarregarUnidades();
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
                ContatosFragment fragmentContatos = (ContatosFragment) adapter.getPage(0);
                if (newText != null && !newText.isEmpty()){
                    fragmentContatos.pesquisarContatos(newText.toLowerCase());
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        //Configurar botão de pesquisa
        MenuItem item = menu.findItem(R.id.menu_pesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }
}
