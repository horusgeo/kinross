package horusgeo.eco101;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.Arrays;


public class MappingActivity extends AppCompatActivity {

    WebView myWebView;

    LatLngPoints points[];
    int pointCount = 1;

    FloatingActionButton fabPoints;
    FloatingActionButton fabRegua;
    FloatingActionButton fabPin;
    FloatingActionButton fabReguaCancel;
    FloatingActionButton fabPointsCancel;
    FloatingActionButton fabPointsNew;
    FloatingActionButton fabPointsOk;
    FloatingActionButton fabPinOk;
    FloatingActionButton fabPinCancel;

    FloatingActionsMenu fabMenu;

    EditText pinText;
    EditText nameText;
    EditText idText;

    String idProp;

    DBHandler db;

    Register cadastro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        idProp = intent.getStringExtra("id");

        db = new DBHandler(this, null, null, 1);

        cadastro = new Register();

        if(!idProp.equals("-1"))
            cadastro = db.getRegister(idProp);


        //WebView
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


        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        fabMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        fabPoints = (FloatingActionButton) findViewById(R.id.actionPoint);
        fabRegua = (FloatingActionButton) findViewById(R.id.actionRegua);
        fabReguaCancel = (FloatingActionButton) findViewById(R.id.fabReguaCancel);
        fabPointsCancel = (FloatingActionButton) findViewById(R.id.fabPointsCancel);
        fabPointsNew = (FloatingActionButton) findViewById(R.id.fabPointsNew);
        fabPointsOk = (FloatingActionButton) findViewById(R.id.fabPointsOk);
        fabPin = (FloatingActionButton) findViewById(R.id.actionPin);
        fabPinOk = (FloatingActionButton) findViewById(R.id.fabPinOk);
        fabPinCancel = (FloatingActionButton) findViewById(R.id.fabPinCancel);

        fabPoints.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                fabMenu.setVisibility(View.INVISIBLE);
                fabMenu.setClickable(false);

                fabPointsCancel.setVisibility(View.VISIBLE);
                fabPointsCancel.setClickable(true);

                fabPointsNew.setVisibility(View.VISIBLE);
                fabPointsNew.setClickable(true);

                fabPointsOk.setVisibility(View.VISIBLE);
                fabPointsOk.setClickable(true);

                clickPoints(0);
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
                fabMenu.setVisibility(View.VISIBLE);
                fabMenu.setClickable(true);

                fabPointsCancel.setVisibility(View.INVISIBLE);
                fabPointsCancel.setClickable(false);

                fabPointsNew.setVisibility(View.INVISIBLE);
                fabPointsNew.setClickable(false);

                fabPointsOk.setVisibility(View.INVISIBLE);
                fabPointsOk.setClickable(false);
                if(cadastro.getLat().size()>0)
                    callCancelDialog();
                else
                    callPropDialog();
//                clickPoints(2);
            }
        });

        fabPointsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.setVisibility(View.VISIBLE);
                fabMenu.setClickable(true);

                fabPointsCancel.setVisibility(View.INVISIBLE);
                fabPointsCancel.setClickable(false);

                fabPointsNew.setVisibility(View.INVISIBLE);
                fabPointsNew.setClickable(false);

                fabPointsOk.setVisibility(View.INVISIBLE);
                fabPointsOk.setClickable(false);

                clickPoints(3);
            }
        });

        fabRegua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                fabMenu.setClickable(false);
                fabReguaCancel.setVisibility(View.VISIBLE);
                fabReguaCancel.setClickable(true);
                clickRegua(true);
            }
        });

        fabReguaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabReguaCancel.setVisibility(View.INVISIBLE);
                fabReguaCancel.setClickable(false);
                fabMenu.setClickable(true);
                clickRegua(false);
            }
        });

        fabPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                fabMenu.setClickable(false);

                fabPinOk.setVisibility(View.VISIBLE);
                fabPinOk.setClickable(true);

                fabPinCancel.setVisibility(View.VISIBLE);
                fabPinCancel.setClickable(true);

                callPinDialog();

            }
        });

        fabPinOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPin(true);
            }
        });

        fabPinCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPin(false);
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
        public void callBackPropriedade(Double[] lat, Double[] lng){

            ArrayList<Double> lats = new ArrayList<Double>(Arrays.asList(lat));
            ArrayList<Double> lngs = new ArrayList<Double>(Arrays.asList(lng));

            cadastro.setLat(lats);
            cadastro.setLng(lngs);

            db.addLatLng(cadastro);

            if(idProp.equals("-1")){
                db.addRegister(cadastro);
                db.addProp(cadastro);
                db.addConj(cadastro);
                db.addEndRes(cadastro);
                db.addEndObj(cadastro);
                db.addIdProp(cadastro);
                db.addDesc(cadastro);

            }
        }

        @JavascriptInterface
        public void callBackPins(Double lat, Double lng, String texto){
            db.addPinToLatLngTable(lat, lng, texto);
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
            if(ids.equals("-1")){
                myWebView.loadUrl("javascript:populatePin('" + texts.get(i) + "', " + lat.get(i) + ", " + lng.get(i) + " )");
            }else{
                if(ids.get(i).equals(idBck)) {
                    myWebView.loadUrl("javascript:continueProp(" + lat.get(i) + ", " + lng.get(i) + " )");
                }else{

                    idBck = ids.get(i);
                    myWebView.loadUrl("javascript:newProp('" + ids.get(i) + "', " + lat.get(i) + ", " + lng.get(i) + ", '" + texts.get(i) + "', 3)");
                }
            }
        }

        myWebView.loadUrl("javascript:addProp()");

    }

    private void callCancelDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MappingActivity.this);

        builder.setMessage("Proprietário já possui uma propriedade cadastrada!")
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        TextView title = new TextView(MappingActivity.this);
        title.setText("!!! ATENÇÃO !!!");
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setTextSize(30);

        builder.setCustomTitle(title);
    }

    private void callPropDialog(){
        if(idProp.equals("-1")) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(MappingActivity.this);
            builderSingle.setTitle("Defina o nome e o número de identificação do proprietário:");
            builderSingle.setCancelable(false);
            LayoutInflater inflater = this.getLayoutInflater();
            final View rootView = inflater.inflate(R.layout.latlng_dialog, null);
            nameText = (EditText) rootView.findViewById(R.id.latlngNameText);
            idText = (EditText) rootView.findViewById(R.id.latlngIdText);

            builderSingle.setView(rootView);

            builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (nameText.getText().toString().equals("")) {
                        callPropDialog();
                    } else if (idText.getText().toString().equals("")) {
                        callPropDialog();
                    } else {
                        cadastro.set_nome_proprietario(nameText.getText().toString());
                        cadastro.set_id_prop(idText.getText().toString());
                        myWebView.loadUrl("javascript:createProperty(" + cadastro.get_id_prop() + ", '" + cadastro.get_nome_proprietario() + "', " + " 1)");
                    }
                }
            });
            builderSingle.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clickPoints(3);
                }
            });
            builderSingle.show();
        }else{
            myWebView.loadUrl("javascript:createProperty(" + cadastro.get_id_prop() + ", '" + cadastro.get_nome_proprietario() + "', " + " 1)");
        }

    }


    private void clickPoints(Integer which){
        switch (which){
            case 0:
                myWebView.loadUrl("javascript:startPoints()");
                break;
            case 1:
                myWebView.loadUrl("javascript:clickPoints()");
                break;
            case 2:
                myWebView.loadUrl("javascript:createProperty()");
                break;
            case 3:
                myWebView.loadUrl("javascript:clearPoints()");
                break;
        }

    }

    private void callPinDialog(){

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MappingActivity.this);
        builderSingle.setTitle("Escreva uma nota para o marcador:");

        LayoutInflater inflater = this.getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.pin_dialog, null);
        pinText = (EditText) rootView.findViewById(R.id.pinText);

        builderSingle.setView(rootView);

        builderSingle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myWebView.loadUrl("javascript:setPin('" + pinText.getText().toString() + "')");
            }
        });
        builderSingle.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callPin(false);
            }
        });
        builderSingle.show();
    }

    private void callPin(Boolean bool){
        fabMenu.setClickable(true);

        fabPinOk.setVisibility(View.INVISIBLE);
        fabPinOk.setClickable(false);

        fabPinCancel.setVisibility(View.INVISIBLE);
        fabPinCancel.setClickable(false);

        if(bool)
            myWebView.loadUrl("javascript:keepPin()");
        else
            myWebView.loadUrl("javascript:cancelPin()");


    }



    private void clickRegua(Boolean which){
        if(which)
            myWebView.loadUrl("javascript:clickRegua()");
        else
            myWebView.loadUrl("javascript:closeRegua()");
    }

    public class LatLngPoints{
        public double lat;
        public double lng;
    }

}
