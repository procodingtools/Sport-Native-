package org.procodingtools.idealsportscenter.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.fragments.about.AboutFragment;
import org.procodingtools.idealsportscenter.fragments.account.AccountFragment;
import org.procodingtools.idealsportscenter.fragments.cours.ListCours;
import org.procodingtools.idealsportscenter.fragments.news.NewsFragment;
import org.procodingtools.idealsportscenter.fragments.reserved.ReservedFragment;
import org.procodingtools.idealsportscenter.fragments.resume.ResumeFragment;

import test.jinesh.easypermissionslib.EasyPermission;

import static org.procodingtools.idealsportscenter.commons.FragmentsUtil.refreshFragment;

public class FragmentContainer extends AppCompatActivity implements EasyPermission.OnPermissionResult {

    private static EasyPermission easyPermission;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        activity=this;

        String fragmentName=getIntent().getExtras().getString("fragment_name");
        Fragment fragment=null;
        switch (fragmentName){
            case "courses_list": fragment=new ListCours();break;

            case "news":fragment=new NewsFragment();getSupportActionBar().hide();break;

            case "reserved": fragment=new ReservedFragment();getSupportActionBar().hide();break;

            case "resume": fragment=new ResumeFragment();getSupportActionBar().hide();break;

            case "account": fragment=new AccountFragment();getSupportActionBar().hide();break;

            case "about": fragment=new AboutFragment(); getSupportActionBar().show();setTitle(getString(R.string.about));break;
        }

        refreshFragment(getSupportFragmentManager(),fragment,R.id.main_frame,false);
    }

    @Override
    public void onPermissionResult(String s, boolean b) {
        switch (s) {
            case Manifest.permission.CALL_PHONE:
                if (b) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.tel_str)));
                    startActivity(intent);
                } else {
                    easyPermission.requestPermission(this, Manifest.permission.CALL_PHONE);
                }
                break;
        }
    }

    public static void makeCall() {
        easyPermission=new EasyPermission();
        easyPermission.requestPermission(activity,Manifest.permission.CALL_PHONE);
    }

}
