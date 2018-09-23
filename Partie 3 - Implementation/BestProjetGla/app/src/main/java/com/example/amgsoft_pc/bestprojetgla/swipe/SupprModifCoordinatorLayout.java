package com.example.amgsoft_pc.bestprojetgla.swipe;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.example.amgsoft_pc.bestprojetgla.R;
import com.xenione.libs.swipemaker.AbsCoordinatorLayout;
import com.xenione.libs.swipemaker.SwipeLayout;

public class SupprModifCoordinatorLayout extends AbsCoordinatorLayout {

    private View mSupprimer;
    private View mModifier;
    private SwipeLayout mForegroundView;

    private ImageView fleche;

    public SupprModifCoordinatorLayout(Context context) {
        super(context);
    }

    public SupprModifCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SupprModifCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SupprModifCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void doInitialViewsLocation() {
        mForegroundView = (SwipeLayout) findViewById(R.id.foregroundView);
        mSupprimer = findViewById(R.id.supprimer);
        mModifier = findViewById(R.id.modifier);
        mForegroundView.anchor(0, mModifier.getRight(), mSupprimer.getRight());

        fleche = (ImageView) findViewById(R.id.image_fleche);
    }

    @Override
    public void onTranslateChange(float global, int index, float relative) {
        if (global != 0) {
            fleche.animate().rotation(180).start();
        } else {
            fleche.animate().rotation(0).start();
        }
    }
}
