package com.cristhiancaballero.appfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText et_nombre, et_apellido, et_edad, et_correo, et_contrasena, et_verificar_contrasena;
    Button btn_registrarse, btn_regresar;
    FirebaseAuth mAuth;
    private FirebaseFirestore mfirestore;
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

        mAuth = FirebaseAuth.getInstance();

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    Registrar(correo, pass);
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

    private void Registrar(String correo, String pass) {
        mAuth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            assert user != null;
                            String uid = user.getUid();
                            String correo = et_correo.getText().toString();
                            String pass = et_contrasena.getText().toString();
                            String nombre = et_nombre.getText().toString();
                            String apellido = et_apellido.getText().toString();
                            String edad = et_edad.getText().toString();

                            HashMap<Object, String> DatosUsuario = new HashMap<>();

                            DatosUsuario.put("uid", uid);
                            DatosUsuario.put("pass", pass);
                            DatosUsuario.put("correo", correo);
                            DatosUsuario.put("nombre", nombre);
                            DatosUsuario.put("apellido", apellido);
                            DatosUsuario.put("edad", edad);
                            DatosUsuario.put("imagen", "");

                            mfirestore.collection("USUARIOS").add(DatosUsuario).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(RegisterActivity.this, "Se a guardado en la base de datos", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Error al registrar al usuario en la base de datos", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Toast.makeText(RegisterActivity.this, "Se a registrado exitosamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, PrincipalActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "A fallado exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}