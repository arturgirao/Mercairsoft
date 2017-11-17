package com.proj.lucas.mercairsoft.Activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.proj.lucas.mercairsoft.R;

import java.io.ByteArrayOutputStream;

public class NovoProdutoActivity extends AppCompatActivity {

    private ImageView imgFotoProdutoCadastro;
    private EditText edtProdutoCadastro, edtModeloCadastro, edtFabricanteCadastro, edtValorCadastro, edtDescricaoCadastro;
    private Button btnSalvarProdutoCadastro, btnLimparCamposProdutoCadastro;
    private String fotoProduto, nomeProduto, modeloProduto, fabricanteProduto, valorProduto, descricaoProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto);

        imgFotoProdutoCadastro = findViewById(R.id.imgFotoProdutoCadastro);
        edtProdutoCadastro = findViewById(R.id.edtProdutoCadastro);
        nomeProduto = edtProdutoCadastro.getText().toString().trim();
        edtModeloCadastro = findViewById(R.id.edtModeloCadastro);
        modeloProduto = edtModeloCadastro.getText().toString().trim();
        edtFabricanteCadastro = findViewById(R.id.edtFabricanteCadastro);
        fabricanteProduto = edtFabricanteCadastro.getText().toString().trim();
        edtValorCadastro = findViewById(R.id.edtValorCadastro);
        valorProduto = edtValorCadastro.getText().toString().trim();
        edtDescricaoCadastro = findViewById(R.id.edtDescricaoCadastro);
        descricaoProduto = edtDescricaoCadastro.getText().toString().trim();
        btnSalvarProdutoCadastro = findViewById(R.id.btnSalvarProdutoCadastro);
        btnLimparCamposProdutoCadastro = findViewById(R.id.btnLimparCamposProdutoCadastro);

        imgFotoProdutoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aqui implementação da foto
                dispatchTakePictureIntent();

            }
        });

        btnSalvarProdutoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtProdutoCadastro.getText().toString().equals("") &&
                        !edtModeloCadastro.getText().toString().equals("") &&
                            !edtFabricanteCadastro.getText().toString().equals("") &&
                                !edtValorCadastro.getText().toString().equals("") &&
                                    !edtDescricaoCadastro.getText().toString().equals("")){
                    salvarProduto();
                }else{
                    Toast.makeText(getApplicationContext(), "Preencha os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLimparCamposProdutoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFotoProdutoCadastro.setImageDrawable(getDrawable(R.drawable.addfoto));
                edtProdutoCadastro.setText("");
                edtModeloCadastro.setText("");
                edtFabricanteCadastro.setText("");
                edtValorCadastro.setText("");
                edtDescricaoCadastro.setText("");
            }
        });
    }

    private void dispatchTakePictureIntent() {
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("Requisicoes","BuildVersion alta");
            if (this.checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.i("Requisicoes","Tem os permissions");
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        100);
            } else {
                Log.i("Requisicoes","Não tem os permissions");

                startActivityForResult(takePictureIntent, 1);
            }
        } else {
            Log.i("Requisicoes","Não é verion code M");
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {

                startActivityForResult(takePictureIntent, 1);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == this.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgFotoProdutoCadastro.setImageBitmap(imageBitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String picture = Base64.encodeToString(b,Base64.DEFAULT);
            Log.i("NovoProduto","Picture >>>> " + picture + " <<<<<");
            //Colocar um método pra salvar a imagem no banco do firebase
        }

    }

    private void salvarProduto(){
        goToMenu();
    }

    private void goToMenu(){
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goToMenu();
    }
}
