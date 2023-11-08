package com.cristhiancaballero.appfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;

public class UpdatePerfilActivity extends AppCompatActivity {

    ImageView iv_imagen_perfil;
    EditText et_nombre, et_apellido, et_edad, et_correo, et_contrasena, et_verificar_contrasena;
    Button btn_registrarse, btn_regresar, btn_editar_foto, btn_borrar_foto;
    StorageReference storageReference;
    String storage_path = "fotos_perfil/*";

    private static final int COD_DEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;
    private Uri image_url;
    String photo = "photo";
    String idd;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    private FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_perfil);

        storageReference = FirebaseStorage.getInstance().getReference();

        et_nombre = findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_edad = findViewById(R.id.et_edad);
        et_correo = findViewById(R.id.et_correo);
        et_contrasena = findViewById(R.id.et_contrasena);
        et_verificar_contrasena = findViewById(R.id.et_verificar_contrasena);

        btn_registrarse = findViewById(R.id.btn_registrarse);
        btn_regresar = findViewById(R.id.btn_regresar);
        btn_editar_foto = findViewById(R.id.btn_editar_foto);
        btn_borrar_foto = findViewById(R.id.btn_borrar_foto);

        iv_imagen_perfil = findViewById(R.id.iv_imagen_perfil);

        mAuth = FirebaseAuth.getInstance();

        btn_editar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });
    }

    private void uploadPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, COD_SEL_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("image_url", "requestCode - RESULT_OK: "+requestCode+" "+RESULT_OK);
        if(resultCode == RESULT_OK) {
            if(requestCode == COD_SEL_IMAGE) {
                image_url = data.getData();
                subirPhoto(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri image_url) {
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();
        String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() + "" + idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if(uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", download_uri);
                            mfirestore.collection("pet").document(idd).update(map);
                            Toast.makeText(UpdatePerfilActivity.this, "Foto actualizada", Toast.LENGTH_SHORT);
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdatePerfilActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT);
            }
        });
    }

    private void getUser(String id) {
        mfirestore.collection("USUARIOS").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DecimalFormat format = new DecimalFormat("0.00");
                String nombre = documentSnapshot.getString("nombre");
                String apellido = documentSnapshot.getString("apellido");
                String edad = documentSnapshot.getString("edad");
                String correo = documentSnapshot.getString("correo");
                String imagen = documentSnapshot.getString("imagen");

                et_nombre.setText(nombre);
                et_apellido.setText(apellido);
                et_edad.setText(edad);
                et_correo.setText(correo);
                try {
                    if(!iv_imagen_perfil.equals("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();
                        Picasso.get()
                                .load(imagen)
                                .resize(150, 150)
                                .into(iv_imagen_perfil);
                    }
                } catch (Exception e) {
                    Log.v("Error", "e: " + e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT);
            }
        });
    }
}