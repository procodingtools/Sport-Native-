package org.procodingtools.idealsportscenter.fragments.news;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import org.procodingtools.idealsportscenter.R;


/**
 * Created by djamiirr on 17/09/17.
 */

@SuppressLint("ValidFragment")
public class NewsDetailsDialog extends DialogFragment {

    private TextView title, text;
    private String titleStr,textStr;
    private Dialog dialog;

   @SuppressLint("ValidFragment")
   public NewsDetailsDialog(String title, String text){

       this.titleStr=title;
       this.textStr=text;
   }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //init dialog
        dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_news_details);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //init TextViews
        initTexts();


        return dialog;
    }


    private void initTexts() {
        //init
        title= (TextView) dialog.findViewById(R.id.title);
        text = (TextView) dialog.findViewById(R.id.text_tv);

        text.setText(textStr);
        title.setText(titleStr);


    }


}
