package org.procodingtools.idealsportscenter.commons.entity;

/**
 * Created by djamiirr on 17/09/17.
 */

public class UserEntity {
    String nom,prenom,id,dateInscrit,type;
    public UserEntity(String nom, String prenom, String id, String dateInscrit, String type){
        this.nom=nom;
        this.prenom=prenom;
        this.id=id;
        this.dateInscrit=dateInscrit;
        this.type=type;
    }


    public String getNom(){
        return nom;
    }

    public String getPrenom(){
        return prenom;
    }

    public String getDateInscrit(){
        return dateInscrit;
    }

    public String getID(){
        return id;
    }

    public String getType(){
        return type;
    }
}
