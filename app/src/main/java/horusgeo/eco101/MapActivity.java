package horusgeo.eco101;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapActivity extends AppCompatActivity {

    FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    FloatingActionButton okFAB;
    FloatingActionButton cancelFAB;


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
    Animation show_okFAB;
    Animation hide_okFAB;
    Animation show_cancelFAB;
    Animation hide_cancelFAB;
    Animation show_fab;
    Animation hide_fab;

    WebView myWebView;

    LatLngPoints points[];
    int pointCount = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //WebView
        myWebView = (WebView) findViewById(R.id.mapView);
        assert myWebView != null;
        myWebView.loadUrl("file:///android_asset/www/map.html");

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient());

        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        //FloatingButtons
        fab = (FloatingActionButton) findViewById(R.id.toolsMenu);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab_3);
        okFAB = (FloatingActionButton) findViewById(R.id.okFAB);
        cancelFAB = (FloatingActionButton) findViewById(R.id.cancelFAB);

        //Animations
        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
        show_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_show);
        hide_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_hide);
        show_okFAB = AnimationUtils.loadAnimation(getApplication(), R.anim.ok_show);
        hide_okFAB = AnimationUtils.loadAnimation(getApplication(), R.anim.ok_hide);
        show_cancelFAB = AnimationUtils.loadAnimation(getApplication(), R.anim.cancel_show);
        hide_cancelFAB = AnimationUtils.loadAnimation(getApplication(), R.anim.cancel_hide);
        show_fab = AnimationUtils.loadAnimation(getApplication(), R.anim.pricipal_show);
        hide_fab = AnimationUtils.loadAnimation(getApplication(), R.anim.principal_hide);

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

//        fab1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog.Builder inputPointsDialog = new AlertDialog.Builder(MapActivity.this);
//
//                inputPointsDialog.setTitle("Defina os pontos:");
//
//                final LayoutInflater inflater = MapActivity.this.getLayoutInflater();
//                AlertDialog dialog = inputPointsDialog.create();
//                View dialogView = inflater.inflate(R.layout.inputpointslayout, dialogView);
//                inputPointsDialog.setView(inflater.inflate(R.layout.inputpointslayout, null))
//                    .setPositiveButton("Criar propriedade", new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//
//                        }
//                    })
//                    .setNeutralButton("Novo ponto", new DialogInterface.OnClickListener(){
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            LatLngPoints aux=null;
//                            TextView latInput = (TextView) findViewById(R.id.latInput);
//                            TextView lngInput = (TextView) findViewById(R.id.lngInput);
//                            TextView pointLabel = (TextView) findViewById(R.id.pointLabel);
//
//                            aux.lat = Double.parseDouble(latInput.getText().toString());
//                            aux.lng = Double.parseDouble(lngInput.getText().toString());
//
//                            points[points.length] = aux;
//
//                            pointCount+=1;
//                            pointLabel.setText("Ponto "+ pointCount);
//                            latInput.setText(R.string._0_0);
//                            lngInput.setText(R.string._0_0);
//                        }
//                    })
//                    .setNegativeButton("Editar Ponto", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            LoginDialogFragment.this.getDialog().cancel();
//                        }
//                });
//
//                inputPointsDialog.show();
//
//            }
//
//        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                builder.setMessage("Escolha os pontos no mapa");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clickPoints();
                    }
                });
                builder.setNegativeButton("Cancelar", null);

                AlertDialog dialog = builder.create();

                dialog.show();
            }


        });

        okFAB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideEndPointInputFAB();
                myWebView.loadUrl("javascript:savePropertiesPoints()");
            }
        });

        cancelFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEndPointInputFAB();

            }
        });

    }


    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void paintProperty() {

        }
    }

    public class LatLngPoints{
        public double lat;
        public double lng;
    }


    private void clickPoints(){
        hideFABMenu();
        showEndPointInputFAB();
        myWebView.loadUrl("javascript:clickPoints()");
    }


    private void showFABMenu(){

        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams1.rightMargin += (int) (fab1.getWidth() * 0.25);
        layoutParams1.bottomMargin += (int) (fab1.getHeight() * 1.7);
        fab1.setLayoutParams(layoutParams1);
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);

        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin += (int) (fab2.getWidth() * 0.25);
        layoutParams2.bottomMargin += (int) (fab2.getHeight() * 3.4);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(show_fab_2);
        fab2.setClickable(true);

        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        layoutParams3.rightMargin += (int) (fab3.getWidth() * 0.25);
        layoutParams3.bottomMargin += (int) (fab3.getHeight() * 5.1);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(show_fab_3);
        fab3.setClickable(true);

    }

    private void hideFABMenu(){

        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams1.rightMargin -= (int) (fab1.getWidth() * 0.25);
        layoutParams1.bottomMargin -= (int) (fab1.getHeight() * 1.7);
        fab1.setLayoutParams(layoutParams1);
        fab1.startAnimation(hide_fab_1);
        fab1.setClickable(false);

        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin -= (int) (fab2.getWidth() * 0.25);
        layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 3.4);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(hide_fab_2);
        fab2.setClickable(false);

        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        layoutParams3.rightMargin -= (int) (fab3.getWidth() * 0.25);
        layoutParams3.bottomMargin -= (int) (fab3.getHeight() * 5.1);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(hide_fab_3);
        fab3.setClickable(false);

    }

    private void showEndPointInputFAB(){

        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) okFAB.getLayoutParams();
        layoutParams1.rightMargin += (int) (fab1.getWidth() * 1.7);
        layoutParams1.bottomMargin += (int) (fab1.getHeight() * 0);
        okFAB.setLayoutParams(layoutParams1);
        okFAB.startAnimation(show_okFAB);
        okFAB.setClickable(true);

        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) cancelFAB.getLayoutParams();
        layoutParams2.rightMargin += (int) (fab1.getWidth() * 3.4);
        layoutParams2.bottomMargin += (int) (fab1.getHeight() * 0);
        cancelFAB.setLayoutParams(layoutParams2);
        cancelFAB.startAnimation(show_cancelFAB);
        cancelFAB.setClickable(true);

//        TODO: change fab color to reflect being false clickable
        fab.setClickable(false);
    }

    private void hideEndPointInputFAB(){

        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) okFAB.getLayoutParams();
        layoutParams1.rightMargin += (int) (fab1.getWidth() * -1.7);
        layoutParams1.bottomMargin += (int) (fab1.getHeight() * 0);
        okFAB.setLayoutParams(layoutParams1);
        okFAB.startAnimation(hide_okFAB);
        okFAB.setClickable(false);

        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) cancelFAB.getLayoutParams();
        layoutParams2.rightMargin += (int) (fab1.getWidth() * -3.4);
        layoutParams2.bottomMargin += (int) (fab1.getHeight() * 0);
        cancelFAB.setLayoutParams(layoutParams2);
        cancelFAB.startAnimation(hide_cancelFAB);
        cancelFAB.setClickable(false);

//        TODO: change fab color to reflect being true clickable
        fab.setClickable(true);
    }


}
