package org.procodingtools.idealsportscenter.commons;

import android.content.Context;
import android.content.SharedPreferences;

import org.procodingtools.idealsportscenter.commons.entity.UserEntity;

import static org.procodingtools.idealsportscenter.commons.entity.Users.USER_ENTITY;

/**
 * Created by djamiirr on 05/10/17.
 */

public class SharedUser {
    public static void setUser(Context context){

        SharedPreferences shared = context.getSharedPreferences("E-Sport", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=shared.edit();
        editor.putString("id",USER_ENTITY.getID());
        editor.putString("nom",USER_ENTITY.getNom());
        editor.putString("prenom",USER_ENTITY.getPrenom());
        editor.putString("type",USER_ENTITY.getType());
        editor.putString("date",USER_ENTITY.getDateInscrit());
        editor.commit();
    }

    public static void getUser(Context context){
        SharedPreferences shared = context.getSharedPreferences("E-Sport", Context.MODE_PRIVATE);
        String nom,prenom,id,date,type;
        id=shared.getString("id",null);
        nom=shared.getString("nom",USER_ENTITY.getNom());
        prenom=shared.getString("prenom",USER_ENTITY.getPrenom());
        type=shared.getString("type",USER_ENTITY.getType());
        date=shared.getString("date",USER_ENTITY.getDateInscrit());
        USER_ENTITY=new UserEntity(nom,prenom,id,date,type);

    }
}
