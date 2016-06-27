package horusgeo.eco101;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
    FloatingActionsMenu fabMenu;

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

        fabPoints.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPoints();
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

    private void clickPoints(){
        myWebView.loadUrl("javascript:clickPoints()");
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
