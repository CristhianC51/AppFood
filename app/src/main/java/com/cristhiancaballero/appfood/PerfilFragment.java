package com.cristhiancaballero.appfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.cristhiancaballero.appfood.Modelos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {

    Button btn_salir, btn_editar_perfil;
    CircleImageView profileImage;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    List<Usuario> usuario = new ArrayList<>();
    DatabaseReference databaseReference;
    FirebaseUser user;
    TextView email, edad_modificado, username;

    public PerfilFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        email = view.findViewById(R.id.email);
        edad_modificado = view.findViewById(R.id.edad_modificado);
        username = view.findViewById(R.id.username);

        btn_salir = view.findViewById(R.id.btn_salir);
        btn_editar_perfil = view.findViewById(R.id.btn_editar_perfil);

        profileImage = view.findViewById(R.id.profileImage);

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        cargarDatosUsuarios(email, username, edad_modificado, profileImage);

        btn_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualizarPerfilFragment actualizarPerfilFragment = new ActualizarPerfilFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contenedor, actualizarPerfilFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                cerrarSesionYAbrirLoginActivity();
            }
        });

        return view;
    }

    private void cerrarSesionYAbrirLoginActivity() {
        FirebaseAuth.getInstance().signOut();

        Toast.makeText(getActivity(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void cargarDatosUsuarios(TextView correo, TextView name, TextView edad_modificado, CircleImageView profileImage) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("USUARIOS")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombre = document.getString("nombre");
                            String apellido = document.getString("apellido");
                            String edad = document.getString("edad");
                            String email = document.getString("correo");

                            correo.setText(email);
                            name.setText(nombre + " " + apellido);
                            edad_modificado.setText(edad);

                            String urlImagen = "foto";

                            Glide.with(this)
                                    .load(urlImagen)
                                    .into(profileImage);
                        }
                    } else {
                        // Errores
                    }
                });
    }
}