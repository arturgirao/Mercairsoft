package com.proj.lucas.mercairsoft.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.proj.lucas.mercairsoft.Entities.Usuario;
import com.proj.lucas.mercairsoft.R;

public class CadastroActivity extends Activity implements DatabaseReference.CompletionListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ImageView imgFotoUsuario;
    private EditText edtNomeUsuarioCadastro, edtEmailUsuarioCadastro, edtSenhaUsuarioCadastro;
    private Button btnSalvarUsuarioCadastro, btnLimparCamposCadastro;
    private String fotoUsuario, nomeUsuario, emailUsuario, senhaUsuario;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        imgFotoUsuario = findViewById(R.id.imgFotoUsuario);
        edtNomeUsuarioCadastro = findViewById(R.id.edtNomeUsuarioCadastro);
        nomeUsuario = edtNomeUsuarioCadastro.getText().toString().trim();
        edtEmailUsuarioCadastro = findViewById(R.id.edtEmailUsuarioCadastro);
        emailUsuario = edtEmailUsuarioCadastro.getText().toString().trim();
        edtSenhaUsuarioCadastro = findViewById(R.id.edtSenhaUsuarioCadastro);
        senhaUsuario = edtSenhaUsuarioCadastro.getText().toString().trim();
        btnSalvarUsuarioCadastro = findViewById(R.id.btnSalvarUsuarioCadastro);
        btnLimparCamposCadastro = findViewById(R.id.btnLimparCamposCadastro);

        imgFotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aqui vai carregar a foto
            }
        });

        btnSalvarUsuarioCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtNomeUsuarioCadastro.getText().toString().equals("") &&
                        !edtEmailUsuarioCadastro.getText().toString().equals("") &&
                            !edtSenhaUsuarioCadastro.getText().toString().equals("")){
                    salvarUsuario();
                }else{
                    Toast.makeText(getApplicationContext(), "Preencha os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLimparCamposCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFotoUsuario.setImageDrawable(getDrawable(R.drawable.addfoto));
                edtNomeUsuarioCadastro.setText("");
                edtEmailUsuarioCadastro.setText("");
                edtSenhaUsuarioCadastro.setText("");
            }
        });
    }

    private void salvarUsuario(){
        initUsuario();

        String NOME = edtNomeUsuarioCadastro.getText().toString();
        String EMAIL = edtEmailUsuarioCadastro.getText().toString();
        String SENHA = edtSenhaUsuarioCadastro.getText().toString();

        boolean ok = true;

        if (NOME.isEmpty()) {
            edtNomeUsuarioCadastro.setError("Escreva o seu nome!");
            ok = false;
        }

        if (EMAIL.isEmpty()) {
            edtEmailUsuarioCadastro.setError("Escreva seu email!");
            ok = false;
        }


        if (SENHA.isEmpty()) {
            edtSenhaUsuarioCadastro.setError("Digite uma senha!");
            ok = false;
        }

        if (ok) {
            btnSalvarUsuarioCadastro.setEnabled(false);

            salvarFirebase();
        } else {

        }


        goToLogin();
    }

    private void salvarFirebase() {

        mAuth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {

                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                btnSalvarUsuarioCadastro.setEnabled(true);
            }
        });
    }

    private void goToLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goToLogin();
    }

    protected void initUsuario() {
        usuario = new Usuario();
        usuario.setNome(edtNomeUsuarioCadastro.getText().toString());
        usuario.setEmail(edtEmailUsuarioCadastro.getText().toString());
        usuario.setSenha(edtSenhaUsuarioCadastro.getText().toString());
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        mAuth.signOut();

        Toast.makeText(this,"Conta criada com sucesso!",Toast.LENGTH_LONG).show();

        finish();
    }


    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
