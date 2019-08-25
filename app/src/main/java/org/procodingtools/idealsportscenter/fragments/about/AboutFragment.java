package org.procodingtools.idealsportscenter.fragments.about;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.procodingtools.idealsportscenter.R;

import static org.procodingtools.idealsportscenter.activities.FragmentContainer.makeCall;


/**
 * Created by djamiirr on 04/11/17.
 */

public class AboutFragment extends Fragment {

    private View v;
    private TextView tel,mail,adr,fbTv;
    private LinearLayout telLayout,mailLayout,fb;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_about,null);

        //init views
        initViews();

        //setting texts values
        setTxts();

        //click listeners
        //tel clickListener
       telLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

        //mail clickListener
        mailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setData(Uri.parse("mailto:"));
                final String[] to=new String[] {getString(R.string.mail_str)};
                emailIntent.putExtra(Intent.EXTRA_EMAIL,to);
                emailIntent.setType("text/plain");
                try {
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.send_mail)));
                }catch(Exception e){
                    Toast.makeText(getContext(), getString(R.string.no_mail_app), Toast.LENGTH_LONG).show();
                }
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/316093978531827"));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/IdealSportsCenter")));
                }
            }
        });

        return v;
    }



    //setting texts
    private void setTxts() {
        tel.setText(getString(R.string.tel_str));

        mail.setText(getString(R.string.mail_str));

        adr.setText(getString(R.string.adr_str));

        fbTv.setText(getString(R.string.fb_page_display));
    }

    //start init views
    private void initViews() {
        adr=v.findViewById(R.id.adr);
        mail=v.findViewById(R.id.mail);
        tel=v.findViewById(R.id.tel);
        telLayout=v.findViewById(R.id.call_layout);
        mailLayout=v.findViewById(R.id.mail_layout);
        fb=v.findViewById(R.id.fb_layout);
        fbTv=v.findViewById(R.id.fb_tv);
    }






}
