package horusgeo.eco101;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class InitialActivity extends AppCompatActivity {

    Button addCadastro;
    Button editCadastro;
    Button deleteCadastro;
    Button sendCadastro;
    FloatingActionButton fab;
    DBHandler db;
    TextView warnInitialText;



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

        Intent intent = getIntent();
        String tipo = intent.getStringExtra("tipo");
        String texto = intent.getStringExtra("texto");

        warnInitialText = (TextView) findViewById(R.id.warnInitialText);

        if(tipo.equals("ok")){
            warnInitialText.setText(texto);
            warnInitialText.setTextColor(Color.GREEN);
        }else if(tipo.equals("start")){
            warnInitialText.setText(texto);
            warnInitialText.setTextColor(Color.BLUE);
        }


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
                SendDB sendDB = new SendDB();
                sendDB.execute((Void) null);
            }
        });


    }

    public void callEditPropertyDialog(){
        final List<Register> registers = db.getAllIdNameRegisters();

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(InitialActivity.this);
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Selecione o Proprietário");


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
                        Intent intent = new Intent(InitialActivity.this, CadastroActivity.class);
                        intent.putExtra("tipo", "edit");
                        intent.putExtra("string", strName);
                        startActivity(intent);
                    }
                });
        builderSingle.show();

    }

    public void callDeletePropertyDialog(){
        final List<Register> registers = db.getAllIdNameRegisters();

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(InitialActivity.this);
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Selecione o Proprietário");

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
                        db.deleteRegister(strName);
                        dialog.dismiss();
                    }
                });
        builderSingle.show();

    }

    private class SendDB extends AsyncTask<Void, Void, Boolean> {

        SendDB(){

        }

        @Override
        protected Boolean doInBackground(Void... params){

            //Log.d("HorusGeo", "Antes do try");

            FTPClient client = new FTPClient();
            Boolean teste;
            try{
                client.connect("ftp.horusgeo.com.br", 21);

                teste = client.login("horusgeo", "eco101web");

                client.enterLocalPassiveMode();

                client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
                client.setFileTransferMode(FTP.BINARY_FILE_TYPE);

                File file = new File("/data/data/horusgeo.eco101/databases/eco101.db");
                FileInputStream inputStream = new FileInputStream(file);

                Boolean result = client.storeFile("/public_ftp/incoming/eco101.db", inputStream);

                inputStream.close();
                client.logout();

                return result;

            }catch (IOException ex) {
                ex.printStackTrace();
            }
            Log.d("HorusGeo", "Return False");
            return false;
        }

        protected void onPostExecute(Boolean result) {
            Toast toast;
            Log.d("HorusGeo", "Toast");
            if(result){
                Log.d("HorusGeo", "Toast True");
                toast = Toast.makeText(getApplicationContext(), "Upload ok!", Toast.LENGTH_LONG);
            }else{
                Log.d("HorusGeo", "Toast False");
                toast = Toast.makeText(getApplicationContext(), "Upload not ok!", Toast.LENGTH_LONG);
            }
            toast.show();

        }

    }

}
