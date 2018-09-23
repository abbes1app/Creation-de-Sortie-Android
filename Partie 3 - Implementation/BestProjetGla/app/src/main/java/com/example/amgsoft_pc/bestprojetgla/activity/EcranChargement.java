package com.example.amgsoft_pc.bestprojetgla.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.database.DBHelper;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ManipulationDatabase;

public class EcranChargement extends Activity {
    Animation animationSlideInLeft, animationSlideOutRight;
    ImageView curSlidingImage;
    ImageView  imageView,imageView1;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.layout_chargement);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView1 = (ImageView) findViewById(R.id.imageView1);


        animationSlideInLeft = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        animationSlideOutRight = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        animationSlideInLeft.setDuration(750);
        animationSlideOutRight.setDuration(750);
        animationSlideInLeft.setAnimationListener(animationSlideInLeftListener);
        animationSlideOutRight.setAnimationListener(animationSlideOutRightListener);

        curSlidingImage = imageView;
        imageView.startAnimation(animationSlideInLeft);
    }

    Animation.AnimationListener animationSlideInLeftListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {
            if (curSlidingImage == imageView ) {
                imageView.startAnimation(animationSlideOutRight);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };

    Animation.AnimationListener animationSlideOutRightListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            imageView.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.VISIBLE);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    DatabaseManager.initializeInstance(new DBHelper(getBaseContext()));

                    SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                    DBHelper.initialiseIcones(db);
                    ManipulationDatabase.supprimerSortiesNonValide(db);
                    DatabaseManager.getInstance().closeDatabase();

                    Intent mainIntent = new Intent(EcranChargement.this, MainActivity.class);
                    EcranChargement.this.startActivity(mainIntent);
                    EcranChargement.this.finish();
                }
            });
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationStart(Animation animation) {}
    };
}