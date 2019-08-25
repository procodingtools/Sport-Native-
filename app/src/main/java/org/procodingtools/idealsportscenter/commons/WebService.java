package org.procodingtools.idealsportscenter.commons;

/**
 * Created by djamiirr on 17/09/17.
 */

public class WebService {
    public static final String BASE_URL="http://192.168.1.5:8080/"
            ,COURS=BASE_URL+"Cours"
            ,RESERVED=BASE_URL+"Reservation/Personne"
            ,RESUME=BASE_URL+"Cours/Seance/"
            ,NEWS=BASE_URL+"Actualite"
            ,LOGIN=BASE_URL+"Personne/Login"
            ,RESERVE=BASE_URL+"Cours/Reservation/$iduser/$idsceance/0 => add{} 1=>delete{}"
            ,PAYEMENT=BASE_URL+"Personne/Abonnement/";
}
