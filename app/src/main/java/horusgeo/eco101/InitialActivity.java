package horusgeo.eco101;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

public class InitialActivity extends AppCompatActivity {

    Button addCadastro;
    Button editCadastro;
    Button deleteCadastro;
    Button sendCadastro;
    FloatingActionButton fab;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addCadastro = (Button) findViewById(R.id.newRegister);
        editCadastro = (Button) findViewById(R.id.editRegister);
        deleteCadastro = (Button) findViewById(R.id.deleteRegister);
        sendCadastro = (Button) findViewById(R.id.sendRegister);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        db = new DBHandler(this, null, null, 1);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InitialActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        addCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitialActivity.this, CadastroActivity.class);
                intent.putExtra("tipo", "new");
                intent.putExtra("string", "Novo Cadatro");
                startActivity(intent);
            }
        });

        editCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditPropertyDialog();
            }
        });

        deleteCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDeletePropertyDialog();
            }
        });

        sendCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void callEditPropertyDialog(){
        final List<Register> registers = db.getAllIdNameRegisters();

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(InitialActivity.this);
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Selecione o Proprietario");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                InitialActivity.this,
                android.R.layout.select_dialog_singlechoice);

        for(Register temp : registers){
            arrayAdapter.add(temp.get_nome_proprietario());
        }

        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = registers.get(which).get_id_prop();
                        Intent intent = new Intent(InitialActivity.this, RegisterActivity.class);
                        intent.putExtra("tipo", "edit");
                        intent.putExtra("string", strName);
                        startActivity(intent);
                    }
                });
        builderSingle.show();

    }

    public void callDeletePropertyDialog(){
        List<Register> registers = db.getAllRegisters();

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(InitialActivity.this);
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Selecione a Propriedade");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                InitialActivity.this,
                android.R.layout.select_dialog_singlechoice);

        for(Register temp : registers){
            arrayAdapter.add(temp.get_nome_proprietario());
        }
        registers = null;
        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        db.deleteRegisterByName(strName);
                        dialog.dismiss();
                    }
                });
        builderSingle.show();

    }

}
