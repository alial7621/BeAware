package com.pariana.beaware;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    CurvedBottomNavigationView bottomNavigationView;
    VectorMasterView fab, fab1, fab2;
    RelativeLayout lin_id;
    PathModel outLine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View
        bottomNavigationView = (CurvedBottomNavigationView) findViewById(R.id.curved_bottom_nav);
        fab = (VectorMasterView) findViewById(R.id.fab);
        fab1 = (VectorMasterView) findViewById(R.id.fab1);
        fab2 = (VectorMasterView) findViewById(R.id.fab2);

        lin_id = (RelativeLayout) findViewById(R.id.lin_id);
        bottomNavigationView.inflateMenu(R.menu.main_menu);

        //Set event for bottom nav
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()) {

            case R.id.action_account :
                //Animation
                draw(6);

                //find current path using name
                lin_id.setX(bottomNavigationView.mFirstCurveControlPoint1.x);
                fab.setVisibility(View.VISIBLE);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.GONE);
                drawAnimation(fab);
                break;
                
            case R.id.action_weather :

                //Animation
                draw(2);

                //find current path using name
                lin_id.setX(bottomNavigationView.mFirstCurveControlPoint1.x);
                fab.setVisibility(View.GONE);
                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.GONE);
                drawAnimation(fab1);
                break;

            case R.id.action_translate :

                //Animation
                draw();

                //find current path using name
                lin_id.setX(bottomNavigationView.mFirstCurveControlPoint1.x);
                fab.setVisibility(View.GONE);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.VISIBLE);
                drawAnimation(fab2);
                break;

        }
        return false;
    }

    private void draw() {
        bottomNavigationView.mFirstCurveStartPoint.set((bottomNavigationView.mNavigationBarWidth * 10/12)
        - (bottomNavigationView.CURVE_CIRCLE_RADIUS*2)
        - (bottomNavigationView.CURVE_CIRCLE_RADIUS/3), 0);

        bottomNavigationView.mFirstCurveEndPoint.set(bottomNavigationView.mNavigationBarWidth *10/12,
                bottomNavigationView.CURVE_CIRCLE_RADIUS
                + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4));

        bottomNavigationView.mSecondCurveStartPoint = bottomNavigationView.mFirstCurveEndPoint;

        bottomNavigationView.mSecondCurveEndPoint.set((bottomNavigationView.mNavigationBarWidth*10/12)
                +(bottomNavigationView.CURVE_CIRCLE_RADIUS*2) + (bottomNavigationView.CURVE_CIRCLE_RADIUS/3), 0);

        bottomNavigationView.mFirstCurveControlPoint1.set(bottomNavigationView.mFirstCurveStartPoint.x
                        + bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4),
                bottomNavigationView.mFirstCurveStartPoint.y);

        bottomNavigationView.mFirstCurveControlPoint2.set(bottomNavigationView.mFirstCurveEndPoint.x -
                        (bottomNavigationView.CURVE_CIRCLE_RADIUS*2)+ bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mFirstCurveEndPoint.y);

        //Same with Second
        bottomNavigationView.mSecondCurveControlPoint1.set(bottomNavigationView.mSecondCurveStartPoint.x
                        +(bottomNavigationView.CURVE_CIRCLE_RADIUS*2) - bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mSecondCurveStartPoint.y);

        bottomNavigationView.mSecondCurveControlPoint2.set(bottomNavigationView.mSecondCurveEndPoint.x -
                        (bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4)),
                bottomNavigationView.mSecondCurveEndPoint.y);

    }

    private void drawAnimation(final VectorMasterView fab) {

        outLine = fab.getPathModelByName("outline");
        outLine.setStrokeColor(Color.WHITE);
        outLine.setTrimPathEnd(0.0f);
        //init valueAnimator
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                outLine.setTrimPathEnd((Float) valueAnimator.getAnimatedValue());
                fab.update();
            }
        });
        valueAnimator.start();
    }

    private void draw(int i) {
        bottomNavigationView.mFirstCurveStartPoint.set(bottomNavigationView.mNavigationBarWidth/i
                - (bottomNavigationView.CURVE_CIRCLE_RADIUS*2) - (bottomNavigationView.CURVE_CIRCLE_RADIUS/3), 0);

        bottomNavigationView.mFirstCurveEndPoint.set(bottomNavigationView.mNavigationBarWidth/i, bottomNavigationView.CURVE_CIRCLE_RADIUS
        + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4));

        bottomNavigationView.mSecondCurveStartPoint = bottomNavigationView.mFirstCurveEndPoint;

        bottomNavigationView.mSecondCurveEndPoint.set((bottomNavigationView.mNavigationBarWidth/i)
        +(bottomNavigationView.CURVE_CIRCLE_RADIUS*2) + (bottomNavigationView.CURVE_CIRCLE_RADIUS/3), 0);

        bottomNavigationView.mFirstCurveControlPoint1.set(bottomNavigationView.mFirstCurveStartPoint.x
        + bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4),
                bottomNavigationView.mFirstCurveStartPoint.y);

        bottomNavigationView.mFirstCurveControlPoint2.set(bottomNavigationView.mFirstCurveEndPoint.x -
                (bottomNavigationView.CURVE_CIRCLE_RADIUS*2)+ bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mFirstCurveEndPoint.y);

        //Same with Second
        bottomNavigationView.mSecondCurveControlPoint1.set(bottomNavigationView.mSecondCurveStartPoint.x
                        +(bottomNavigationView.CURVE_CIRCLE_RADIUS*2) - bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mSecondCurveStartPoint.y);

        bottomNavigationView.mSecondCurveControlPoint2.set(bottomNavigationView.mSecondCurveEndPoint.x -
                (bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4)),
                bottomNavigationView.mSecondCurveEndPoint.y);
    }
}
