package horusgeo.eco101;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.Arrays;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class CreatePropCadastroActivity extends AppCompatActivity {

    WebView myWebView;

    FloatingActionButton fabPointsCancel;
    FloatingActionButton fabPointsNew;
    FloatingActionButton fabPointsOk;
    FloatingActionButton fabPose;

    String idProp;

    DBHandler db;

    Register cadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_prop_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        idProp = intent.getStringExtra("id");

        db = new DBHandler(this, null, null, 1);

        cadastro = new Register();

        if(!idProp.equals("-1"))
            cadastro = db.getRegister(idProp);

        myWebView = (WebView) findViewById(R.id.mapView);
        assert myWebView != null;
        myWebView.loadUrl("file:///android_asset/www/map.html");

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url){
                populateMap();
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        fabPointsNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPoints(1);
            }
        });

        fabPointsOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPoints(2);
            }
        });

        fabPointsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPoints(3);
            }
        });



    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void callBackPropriedade(float[] latjs, float[] lngjs) {

            Double[] lat = new Double[latjs.length];
            Double[] lng = new Double[lngjs.length];
            for (int i = 0; i < lat.length; i++) {
                lat[i] = Double.valueOf(String.valueOf(latjs[i]));
                lng[i] = Double.valueOf(String.valueOf(lngjs[i]));
            }
            ArrayList<Double> lats = new ArrayList<Double>(Arrays.asList(lat));
            ArrayList<Double> lngs = new ArrayList<Double>(Arrays.asList(lng));

            cadastro.setLat(lats);
            cadastro.setLng(lngs);

            db.addLatLng(cadastro);
        }
    }


    public void populateMap(){

        ArrayList<Double> lat = db.getLats();
        ArrayList<Double> lng = db.getLngs();
        ArrayList<String> ids = db.getIds();
        ArrayList<String> texts = db.getTexts();
        String idBck = "a";


        int tam = lat.size();

        for (int i = 0; i < tam; i++){
            if(ids.get(i).equals("-1")){
                myWebView.loadUrl("javascript:populatePin('" + texts.get(i) + "', " + lat.get(i) + ", " + lng.get(i) + " )");
            }else{
                if(ids.get(i).equals(idBck)) {
                    myWebView.loadUrl("javascript:continueProp(" + lat.get(i) + ", " + lng.get(i) + " )");
                }else{
                    idBck = ids.get(i);
                    myWebView.loadUrl("javascript:newProp('" + ids.get(i) + "', " + lat.get(i) + ", " + lng.get(i) + ", '" + texts.get(i) + "'," + db.getStatus(ids.get(i)) + ")");
                }
            }
        }

        myWebView.loadUrl("javascript:loadImg('/storage/extSdCard/www')");
        myWebView.loadUrl("javascript:addProp()");

        myWebView.loadUrl("javascript:startPoints()");

    }

    private void clickPoints(Integer which){

        Integer tipo;

        if(cadastro.getTipoLatLng()!=null)
            tipo = Integer.parseInt(cadastro.getTipoLatLng());
        else
            tipo = 1;

        switch (which){
            case 1:
                myWebView.loadUrl("javascript:clickPoints()");
                break;
            case 2:
                myWebView.loadUrl("javascript:createProperty(" + cadastro.get_id_prop() + ", '" + cadastro.get_nome_proprietario() + "', " + tipo + ")");
                break;
            case 3:
                myWebView.loadUrl("javascript:clearPoints()");
                break;
        }

    }

}


