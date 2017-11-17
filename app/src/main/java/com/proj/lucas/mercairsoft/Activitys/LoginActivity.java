package com.proj.lucas.mercairsoft.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.proj.lucas.mercairsoft.Entities.Usuario;
import com.proj.lucas.mercairsoft.R;

public class LoginActivity extends Activity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText edtEmailLogin, edtSenhaLogin;
    private Button btnEntrarLogin;
    private TextView txtLinkCadastroLogin;
    private String emailLogin, senhaLogin;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = getFirebaseAuthResultHandler();

        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtSenhaLogin = findViewById(R.id.edtSenhaLogin);
        emailLogin = edtEmailLogin.getText().toString().trim();
        senhaLogin = edtSenhaLogin.getText().toString().trim();

        txtLinkCadastroLogin = findViewById(R.id.txtLinkCadastroLogin);
        txtLinkCadastroLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(it);
                finish();
            }
        });

        btnEntrarLogin = findViewById(R.id.btnEntrarLogin);
        btnEntrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtEmailLogin.getText().toString().equals("") && !edtSenhaLogin.getText().toString().equals("")){
                    usuario = new Usuario();
                    usuario.setEmail(edtEmailLogin.getText().toString());
                    usuario.setSenha(edtSenhaLogin.getText().toString());
                    validarLogin();
                }else{
                    Toast.makeText(getApplicationContext(), "Preencha os campos.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void verifyLogged() {

        if (firebaseAuth.getCurrentUser() != null) {
            goToMenu();
        } else {
            firebaseAuth.addAuthStateListener(authStateListener);
        }
    }

    private void validarLogin(){
        firebaseAuth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {


                            btnEntrarLogin.setEnabled(true);
                            txtLinkCadastroLogin.setEnabled(true);

                            return;
                        }
                    }
                });

    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    private void goToMenu(){
        Intent it = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        verifyLogged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private FirebaseAuth.AuthStateListener getFirebaseAuthResultHandler() {
        FirebaseAuth.AuthStateListener callback = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();

                if (userFirebase == null) {
                    return;
                }

                if (usuario.getId() == null && isNameOk(usuario, userFirebase)) {

                    usuario.setId(userFirebase.getUid());
                    usuario.setNameIfNull(userFirebase.getDisplayName());
                    usuario.setEmailIfNull(userFirebase.getEmail());
                    usuario.saveDB();
                }

                goToMenu();
            }
        };
        return (callback);
    }

    private boolean isNameOk(Usuario usuario, FirebaseUser firebaseUser) {
        return (usuario.getNome() != null || firebaseUser.getDisplayName() != null);
    }
}
