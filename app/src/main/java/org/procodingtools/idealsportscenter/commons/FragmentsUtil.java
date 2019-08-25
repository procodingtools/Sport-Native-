package org.procodingtools.idealsportscenter.commons;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by djamiirr on 05/09/17.
 */

public class FragmentsUtil {
    public static void refreshFragment(FragmentManager fragmentManager, Fragment fragment, int frame, boolean addToHistory){
        if(!addToHistory)
            fragmentManager.beginTransaction().replace(frame,fragment).commit();
        else
            fragmentManager.beginTransaction().replace(frame,fragment).addToBackStack(null).commit();
    }

    public static boolean FIRST_OPEN=false;
}
