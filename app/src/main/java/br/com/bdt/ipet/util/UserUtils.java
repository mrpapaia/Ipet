package br.com.bdt.ipet.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserUtils {

    /*
    * Retorna o usuário logado, caso não tiver nenhum: null
    * */
    public static FirebaseUser getUser(){

        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /*
     * Identifica o usuário atual e obtem o seu email
     * */
    public static String getEmail(){
        return getUser() != null ? getUser().getEmail() : null;
    }

    /*
    * Sai do usuário da ong logado
    * */
    public static void logoutUser(){
        FirebaseAuth.getInstance().signOut();
    }
}
