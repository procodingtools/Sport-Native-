package org.procodingtools.idealsportscenter.commons;

import android.content.Context;
import android.content.SharedPreferences;

import org.procodingtools.idealsportscenter.commons.entity.UserEntity;

import static org.procodingtools.idealsportscenter.commons.entity.Users.USER_ENTITY;

/**
 * Created by djamiirr on 20/10/17.
 */

public class SharedImportExport {
    public static boolean checkExistance(Context context){
        SharedPreferences preferences=context.getSharedPreferences("users", Context.MODE_PRIVATE);
        if (preferences.contains("id")){
            String nom,prenom,dateInscrit,id,type;
            nom=preferences.getString("nom",null);
            prenom=preferences.getString("prennom",null);
            dateInscrit=preferences.getString("dateInscrit",null);
            id=preferences.getString("id",null);
            type=preferences.getString("type",null);
            USER_ENTITY=new UserEntity(nom,prenom,id,dateInscrit,type);
            return true;
        }
        return false;
    }

    public static void setUser(UserEntity user,Context context){
        SharedPreferences preferences=context.getSharedPreferences("users", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("nom",user.getNom());
        editor.putString("prenom",user.getPrenom());
        editor.putString("dateInscrit",user.getDateInscrit());
        editor.putString("id",user.getID());
        editor.putString("type",user.getType());
        editor.commit();
    }

    public static void deleteData(Context context){
        SharedPreferences preferences=context.getSharedPreferences("users", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear().commit();
    }
}
