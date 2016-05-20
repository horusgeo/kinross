package horusgeo.eco101;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class NewSessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        ImageView img = (ImageView) findViewById(R.id.mapNewSession);
        Log.w("IMAGE SIZE", "Width = " + img.getWidth());
        Log.w("IMAGE SIZE", "Height = " + img.getHeight());



    }

//    private void createSections(){
//        ImageButton buttons[];
//
//        ImageButton aux = new ImageButton(this);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,400);
//        aux.setLayoutParams();
//
//    }

}



