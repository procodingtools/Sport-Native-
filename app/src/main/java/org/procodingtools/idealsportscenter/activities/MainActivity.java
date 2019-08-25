package org.procodingtools.idealsportscenter.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.fragments.dialogs.CheckNetworkFragmentDialog;
import org.procodingtools.idealsportscenter.fragments.dialogs.LoginFragmentDialog;

import static org.procodingtools.idealsportscenter.R.id.list_sourses_card;
import static org.procodingtools.idealsportscenter.commons.FragmentsUtil.FIRST_OPEN;
import static org.procodingtools.idealsportscenter.commons.entity.Users.USER_ENTITY;

public class MainActivity extends AppCompatActivity {

    private CardView newsCard, reservedCard,coursesListCard,resumeCard,accountCard,aboutCard;
    private ImageView logo;
    private RelativeLayout btnsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hindding action bar
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        }

        if (!FIRST_OPEN){
            btnsLayout= (RelativeLayout) findViewById(R.id.btns_layout);
            logo= (ImageView) findViewById(R.id.logo);
            btnsLayout.setAlpha(0);
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            Animation translate=new TranslateAnimation(0,0,(height/2)-150,logo.getMeasuredHeight());
            translate.setStartOffset(1500);
            translate.setDuration(600);
            translate.setFillAfter(true);
            logo.setAnimation(translate);

            translate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    btnsLayout.setAlpha(1);
                    Animation alpha=new AlphaAnimation(0,1);
                    alpha.setStartOffset(100);
                    alpha.setDuration(200);
                    alpha.setFillAfter(true);
                    btnsLayout.setAnimation(alpha);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        //init views
        initViews();

        //setting click listeners
        clickListeners();

        //checking network
        checkNetwork();

    }

    //starting check network
    private void checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) { // connected to the internet
            CheckNetworkFragmentDialog dialog=new CheckNetworkFragmentDialog();
            dialog.show(getSupportFragmentManager(),null);
        }
    }

    //starting click listeners
    private void clickListeners() {
        //newsCard click listener
        newsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FragmentContainer.class).putExtra("fragment_name","news"));
            }
        });

        //courseslistCard listener
        coursesListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FragmentContainer.class).putExtra("fragment_name","courses_list"));
            }
        });

        //reserverCard listener
        reservedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryReserved();
            }

            private void tryReserved() {
                if (USER_ENTITY==null){
                    LoginFragmentDialog dialog=new LoginFragmentDialog();
                    dialog.show(getSupportFragmentManager(),null);
                }else
                    startActivity(new Intent(MainActivity.this,FragmentContainer.class).putExtra("fragment_name","reserved"));
            }
        });

        //resumeCard listener
        aboutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FragmentContainer.class).putExtra("fragment_name","about"));
            }
        });

        //aboutCard listener
        resumeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FragmentContainer.class).putExtra("fragment_name","resume"));
            }
        });

        //accountCard listener
        accountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryAccount();
            }

            private void tryAccount() {
                if (USER_ENTITY==null){
                    LoginFragmentDialog dialog=new LoginFragmentDialog();
                    dialog.show(getSupportFragmentManager(),null);
                }else
                    startActivity(new Intent(MainActivity.this,FragmentContainer.class).putExtra("fragment_name","account"));
            }
        });
    }

    //starting init viwes
    private void initViews() {
        newsCard= (CardView) findViewById(R.id.news_card);
        reservedCard = (CardView) findViewById(R.id.reservation_card);
        coursesListCard= (CardView) findViewById(list_sourses_card);
        resumeCard= (CardView) findViewById(R.id.resume_card);
        accountCard= (CardView) findViewById(R.id.account_card);
        aboutCard= (CardView) findViewById(R.id.about_card);
    }

    private long backPressed;
    @Override
    public void onBackPressed() {
        if (backPressed+1000>System.currentTimeMillis()){
            finish();
            return;
        }else
            Toast.makeText(this, getString(R.string.press_twice), Toast.LENGTH_SHORT).show();
        backPressed=System.currentTimeMillis();
    }
}
