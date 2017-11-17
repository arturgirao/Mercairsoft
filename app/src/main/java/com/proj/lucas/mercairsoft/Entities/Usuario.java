package com.proj.lucas.mercairsoft.Entities;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Lucas on 11/11/2017.
 */

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }

    public Usuario(String id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    private void setNameInMap(Map<String, Object> map) {
        if (getNome() != null) {
            map.put("name", getNome());
        }
    }
    public void setNameIfNull(String name) {
        if (this.nome == null) {
            this.nome = name;
        }
    }

    private void setEmailInMap(Map<String, Object> map) {
        if (getEmail() != null) {
            map.put("email", getEmail());
        }
    }
    public void setEmailIfNull(String email) {
        if (this.email == null) {
            this.email = email;
        }
    }

    public void saveDB(DatabaseReference.CompletionListener... completionListener) {
        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child(getId());
        if (completionListener.length == 0) {
            firebase.setValue(this);
        } else {
            firebase.setValue(this, completionListener[0]);
        }
    }



}
