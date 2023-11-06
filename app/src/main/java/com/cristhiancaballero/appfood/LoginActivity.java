package com.cristhiancaballero.appfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText et_usuario, et_contrasena;
    Button btn_iniciar_sesion, btn_registrarse;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        et_usuario = findViewById(R.id.et_usuario);
        et_contrasena = findViewById(R.id.et_contrasena);
        btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion);
        btn_registrarse = findViewById(R.id.btn_registrarse);

        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailUser = et_usuario.getText().toString().trim();
                String passUser = et_contrasena.getText().toString().trim();

                if(emailUser.isEmpty() && passUser.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Ingresa los datos", Toast.LENGTH_LONG).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()) {
                    et_usuario.setError("Ingresa un correo");
                    et_usuario.setFocusable(true);
                } else if (passUser.length() < 1) {
                    et_contrasena.setError("Ingresa la contraseña");
                    et_contrasena.setFocusable(true);
                }else {
                    loginUser(emailUser, passUser);
                }
            }
        });

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser(String emailUser, String passUser) {
        mAuth.signInWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));
                    Toast.makeText(LoginActivity.this, "Bienvenido!, haz iniciado sesion exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Correo o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error al iniciar sesion", Toast.LENGTH_LONG).show();
            }
        });
    }
}