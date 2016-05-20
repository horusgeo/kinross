package horusgeo.eco101;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class MapActivity extends AppCompatActivity {

    FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;

    //Save the FAB's active status
    //false -> fab = close
    //true -> fab = open
    private boolean FAB_Status = false;

    //Animations
    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;
    Animation show_fab_3;
    Animation hide_fab_3;

    WebView myWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myWebView = (WebView) findViewById(R.id.mapView);
        assert myWebView != null;
        myWebView.loadUrl("file:///android_asset/www/teste.html");
//        myWebView.loadUrl("http://www.globo.com");

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient());


        fab = (FloatingActionButton) findViewById(R.id.toolsMenu);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_1);

        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!FAB_Status) {
                    //Display FAB menu
                    showFABMenu();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFABMenu();
                    FAB_Status = false;
                }
            }
        });



    }



    private void showFABMenu(){

        fab1 = (FloatingActionButton) findViewById(R.id.fab_1);

        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
//        layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
//        layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
        layoutParams.rightMargin += (int) (fab1.getWidth() * 0.25);
        layoutParams.bottomMargin += (int) (fab1.getHeight() * 1.7);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);

    }

    private void hideFABMenu(){



        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
//        layoutParams.rightMargin -= (int) (fab1.getWidth() * 1.7);
//        layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.25);
        layoutParams.rightMargin -= (int) (fab1.getWidth() * 0.25);
        layoutParams.bottomMargin -= (int) (fab1.getHeight() * 1.7);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(hide_fab_1);
        fab1.setClickable(false);
    }


}
