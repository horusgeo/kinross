package horusgeo.eco101;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //WebView
        myWebView = (WebView) findViewById(R.id.mapView);
        assert myWebView != null;
        myWebView.loadUrl("file:///android_asset/www/map.html");

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);

        myWebView.setWebViewClient(new WebViewClient());

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

                clickPoints(2);
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
        public void paintProperty() {

        }

        @JavascriptInterface
        public void toastTest(String texto){
            Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG).show();
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
