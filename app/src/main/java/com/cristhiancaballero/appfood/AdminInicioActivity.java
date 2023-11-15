package com.cristhiancaballero.appfood;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminInicioActivity extends AppCompatActivity {

    PerfilAdminFragment perfilAdminFragment = new PerfilAdminFragment();
    PedidosFragment pedidosFragment = new PedidosFragment();
    PRealizadosFragment pRealizadosFragment = new PRealizadosFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminitrador_inicio);

        BottomNavigationView navigationView = findViewById(R.id.navigationAdmin);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelected);

        loadFragment(pedidosFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelected = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if(item.getItemId() == R.id.nav_pedidos_admin){
                loadFragment(pedidosFragment);
                return true;
            } else if (item.getItemId() == R.id.nav_realizados_admin) {
                loadFragment(pRealizadosFragment);
                return true;
            } else if (item.getItemId() == R.id.nav_perfi_admin) {
                loadFragment(perfilAdminFragment);
                return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedorAdmin, fragment);
        transaction.commit();
    }
}