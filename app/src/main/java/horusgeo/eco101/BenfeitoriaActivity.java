package horusgeo.eco101;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BenfeitoriaActivity extends AppCompatActivity {

    String idProp;
    ArrayList<Benfeitoria> benfs;
    ArrayList<Docs> docs;
    TextView tipoText;
    TextView idadeText;
    TextView consvText;

    LinearLayout benfsLayout;

    ImageButton benfAddButton;

    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benfeitoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        idProp = intent.getStringExtra("idProp");

        db = new DBHandler(this, null, null, 1);

        benfs = db.getBenfeitoria(idProp);
        docs = db.getDocs(idProp);

        benfAddButton = (ImageButton) findViewById(R.id.addBenfButton);

        benfsLayout = (LinearLayout) findViewById(R.id.benfsVertLayout);


        assert benfAddButton != null;
        benfAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddBenfDialog();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void callAddBenfDialog(){

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(BenfeitoriaActivity.this);
        builderSingle.setTitle("Selecione o Proprietário");

        LayoutInflater inflater = this.getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.benf_dialog, null);
        builderSingle.setView(rootView)
            .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    tipoText = (TextView) rootView.findViewById(R.id.benfTipoText);
                    idadeText = (TextView) rootView.findViewById(R.id.benfIdadeText);
                    consvText = (TextView) rootView.findViewById(R.id.benfConservText);

                    benfs.add(new Benfeitoria(tipoText.getText().toString(), idadeText.getText().toString(),
                            consvText.getText().toString(), String.valueOf(1000 + benfs.size()), idProp));

                    addBenf(benfs.get(benfs.size()-1));
                }
            })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
        });
        builderSingle.show();
    }

    public void addBenf(final Benfeitoria benf){

        LayoutInflater inflater = this.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dados_benf, null);

        TextView tipo = (TextView) rootView.findViewById(R.id.benfTipoLabel);
        TextView idade = (TextView) rootView.findViewById(R.id.benfIdadeLabel);
        TextView conserv = (TextView) rootView.findViewById(R.id.benfConservacaoLabel);
        TextView idBenf = (TextView) rootView.findViewById(R.id.idBenfInvText);

        ImageButton photoButton = (ImageButton) rootView.findViewById(R.id.benfPhotoButton);
        ImageButton editButton = (ImageButton) rootView.findViewById(R.id.benfEditButton);
        ImageButton delButton = (ImageButton) rootView.findViewById(R.id.benfDelButton);

        tipo.setText("Tipo: " + benf.getTipo());
        idade.setText("Idade Aparente: " + benf.getIdade());
        conserv.setText("Conservação: " + benf.getConservacao());
        idBenf.setText(benf.getIdBenf());

        benfsLayout.addView(rootView, benfsLayout.getChildCount()-1);

        delButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDelBenf(benf.getIdBenf());
            }
        });


    }


    public void callDelBenf(String id){
        int viewId = -1;
        int benfId = -1;

        for(int i = 0; i < benfsLayout.getChildCount(); i++){
            View v = benfsLayout.getChildAt(i);
            TextView idBenf = (TextView) v.findViewById(R.id.idBenfInvText);
            if(id.equals(idBenf.getText().toString())){
                viewId = i;
                break;
            }
        }
        if(viewId!=-1){
            benfsLayout.removeViewAt(viewId);
        }

        for(int i = 0; i < benfs.size(); i++){
            if(id.equals(benfs.get(i).getIdBenf())){
                benfId = i;
                break;
            }
        }

        if(benfId!=-1){
            benfs.remove(benfId);
        }


    }

}
