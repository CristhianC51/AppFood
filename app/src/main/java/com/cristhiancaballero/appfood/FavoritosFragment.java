package com.cristhiancaballero.appfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cristhiancaballero.appfood.Adaptadores.ProductoAdapter;
import com.cristhiancaballero.appfood.Modelos.Producto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class FavoritosFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ProductoAdapter adaptador;
    private List<Producto> productoList;
    private FirebaseUser user;

    public FavoritosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        db = FirebaseFirestore.getInstance();

        // Inicializar RecyclerView y lista de mensajes
        recyclerView = view.findViewById(R.id.RecyclerViewFavoritos);
        productoList = new ArrayList<>();
        adaptador = new ProductoAdapter(productoList);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptador);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        // Obtener datos de Firestore
        obtenerDatosDesdeFirestore(uid);

        return view;
    }

    private void obtenerDatosDesdeFirestore(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("PRODUCTOS");

        collectionRef
                .whereEqualTo("uid", id)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Producto> productos = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String nombre = document.getString("nombre");
                            String precio = document.getString("precio");
                            String descripcion = document.getString("descripcion");
                            String foto = document.getString("foto");
                            String uid = document.getString("uid");
                            String check = document.getString("check");
                            productos.add(new Producto(nombre, precio, foto, descripcion, uid, check));
                        }
                        mostrarDatosEnRecyclerView(productos);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejar errores
                    }
                });
    }

    private void mostrarDatosEnRecyclerView(List<Producto> datos) {
        adaptador = new ProductoAdapter(datos);
        recyclerView.setAdapter(adaptador);
    }
}