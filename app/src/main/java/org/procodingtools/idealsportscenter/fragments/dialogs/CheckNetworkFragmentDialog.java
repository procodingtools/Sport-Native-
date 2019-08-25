package org.procodingtools.idealsportscenter.fragments.dialogs;

import android.annotation.SuppressLint;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import org.procodingtools.idealsportscenter.R;

@SuppressLint("ValidFragment")
public class CheckNetworkFragmentDialog extends DialogFragment {

    private View v;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_dialog_check_network,null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }


}