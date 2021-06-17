package br.com.bdt.ipet.singleton;

import android.net.Uri;

import br.com.bdt.ipet.data.model.Ong;

public class CadastroSingleton {

    private static CadastroSingleton cadastroSingleton;
    private Ong ong;
    private String senha;
    private Uri uri;

    private CadastroSingleton() {
    }

    public static CadastroSingleton getCadastroSingleton() {
        if (cadastroSingleton == null) {
            cadastroSingleton = new CadastroSingleton();
        }
        return cadastroSingleton;
    }

    public Ong getOng() {
        return ong;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
