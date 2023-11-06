package com.cristhiancaballero.appfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.security.Principal;

public class PrincipalActivity extends AppCompatActivity {

    InicioFragment inicioFragment = new InicioFragment();
    CestaFragment cestaFragment = new CestaFragment();
    FavoritosFragment favoritosFragment = new FavoritosFragment();
    PerfilFragment perfilFragment = new PerfilFragment();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelected);

        loadFragment(inicioFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelected = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if(item.getItemId() == R.id.nav_inicio){
                loadFragment(inicioFragment);
                return true;
            } else if (item.getItemId() == R.id.nav_cesta) {
                loadFragment(cestaFragment);
                return true;
            } else if (item.getItemId() == R.id.nav_favoritos) {
                loadFragment(favoritosFragment);
                return true;
            } else if (item.getItemId() == R.id.nav_perfil) {
                loadFragment(perfilFragment);
                return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedor, fragment);
        transaction.commit();
    }
}