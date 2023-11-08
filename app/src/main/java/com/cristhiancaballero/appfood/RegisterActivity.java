package com.cristhiancaballero.appfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cristhiancaballero.appfood.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    ImageView iv_imagen_perfil;
    EditText et_nombre, et_apellido, et_edad, et_correo, et_contrasena, et_verificar_contrasena;
    Button btn_registrarse, btn_regresar, btn_editar_foto, btn_borrar_foto;
    FirebaseAuth mAuth;
    private Uri imageUri;
    private FirebaseFirestore mfirestore;
    private boolean isCreatingUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mfirestore = FirebaseFirestore.getInstance();

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
                selectImage();
            }
        });

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = et_nombre.getText().toString();
                String apellido = et_apellido.getText().toString();
                String edad = et_edad.getText().toString();
                String correo = et_correo.getText().toString();
                String pass = et_contrasena.getText().toString();
                String veripass = et_verificar_contrasena.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    et_correo.setError("Correo no valido");
                    et_correo.setFocusable(true);
                } else if(pass.length()<6) {
                    et_contrasena.setError("La contraseña debe ser mayor a 6 digitos");
                    et_contrasena.setFocusable(true);
                } else if(!pass.equals(veripass)) {
                    et_verificar_contrasena.setError("La contraseña no es igual");
                    et_verificar_contrasena.setFocusable(true);
                } else {
                    uploadImageToStorage(nombre, apellido, edad, imageUri, correo, "USUARIOS");
                    //Registrar(correo, pass);
                }
            }
        });

        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            iv_imagen_perfil.setImageURI(imageUri);
        }
    }

    private void uploadImageToStorage(final String nombre, String apellido, String edad, Uri imageUri, String correo, String onActivity) {
        String imageName = onActivity + "/" + System.currentTimeMillis() + ".jpg";

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imageName);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                String imageUrl = downloadUrl.toString();

                                Usuario usuario = new Usuario(nombre, apellido, edad, imageUrl, correo);

                                mfirestore.collection(onActivity)
                                            .add(usuario)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                  Intent intent = new Intent(RegisterActivity.this, PrincipalActivity.class);
                                                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                  startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                isCreatingUser = false;
                                                btn_registrarse.setEnabled(true);
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        isCreatingUser = false;
                        btn_registrarse.setEnabled(true);
                    }
                });
    }


}