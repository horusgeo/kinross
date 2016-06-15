package horusgeo.eco101;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BenfeitoriaActivity extends AppCompatActivity {

    String idProp;
    Register cadastro;
    TextView tipoText;
    TextView idadeText;
    TextView consvText;

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
        cadastro = new Register();

        cadastro = db.getRegister(idProp);

        ImageButton benfAddButton = (ImageButton) findViewById(R.id.addBenfButton);

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

        builderSingle.setView(inflater.inflate(R.layout.benf_dialog, null))
            .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    tipoText = (TextView) findViewById(R.id.benfTipoText);
                    idadeText = (TextView) findViewById(R.id.benfIdadeText);
                    consvText = (TextView) findViewById(R.id.benfConservText);

                    cadastro.getBenf().add(new Benfeitoria(tipoText.getText().toString(), idadeText.getText().toString(),
                            consvText.getText().toString(), String.valueOf(1000 + db.getBenfCount())));

                }
            })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
        });
        builderSingle.show();


    }

}
