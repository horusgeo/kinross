package horusgeo.eco101;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import com.opencsv.CSVWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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
    String tipo;
    String texto;
    String user;



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
        tipo = intent.getStringExtra("tipo");
        texto = intent.getStringExtra("texto");
        user = intent.getStringExtra("user");

        warnInitialText = (TextView) findViewById(R.id.warnInitialText);

        if(tipo.equals("ok")) {
            warnInitialText.setText(texto);
            warnInitialText.setTextColor(Color.GREEN);
        }else if(tipo.equals("start")){
            warnInitialText.setText(texto);
            warnInitialText.setTextColor(Color.BLUE);
        }else if(tipo.equals("cancel")){
            warnInitialText.setText(texto);
            warnInitialText.setTextColor(Color.RED);
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
                intent.putExtra("user", user);
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
                SendDB sendDB = new SendDB(InitialActivity.this);
                sendDB.execute((Void) null);
            }
        });

        FloatingActionButton fab_cancel = (FloatingActionButton) findViewById(R.id.fab_cancel);
        assert fab_cancel != null;
        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InitialActivity.this);
                builder.setMessage("Você realmente deseja sair?")
                        .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                InitialActivity.this.finishAffinity();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });


    }

    @Override
    public void onBackPressed(){

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
                        intent.putExtra("user", user);
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




    private class SendDB extends AsyncTask<Void, Integer, Boolean> {

        private ProgressDialog dialogProgress;

        SendDB(Activity activity){
            this.dialogProgress = new ProgressDialog(activity);
        }

        protected void onPreExecute() {
            this.dialogProgress.setMessage("Enviando Cadastros! Por Favor, aguarde!");
            this.dialogProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.dialogProgress.setProgress(0);
            this.dialogProgress.show();
        }



//        private void updateProgress(int count, int numFiles){
//            this.dialogProgress.setMessage("Enviando arquivos! Por Favor, aguarde!\n" +
//                    count + " / " + numFiles);
//            this.dialogProgress.setMax(numFiles);
//            this.dialogProgress.incrementProgressBy(100/numFiles);
//        }


        @Override
        protected Boolean doInBackground(Void... params){

            //Log.d("HorusGeo", "Antes do try");

            FTPClient client = new FTPClient();
            Boolean teste;
            Log.d("HorusGeo", "Entrando...");
            ArrayList<String> list = db.getAllDocs();
            int ntables = db.getNTables();
            int numFiles = list.size() + ntables;

            try{
                Log.d("HorusGeo", "Conectando...");
                client.connect("ftp.horusgeo.com.br", 21);
                Log.d("HorusGeo", "Login...");
                teste = client.login("horusgeo", "eco101web");

                client.enterLocalPassiveMode();

                client.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
                client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
                int count = 0;
                Boolean result = false;
                while(count < ntables){
                    File file = db.generateCSV(count);
                    Log.d("HorusGeo", String.valueOf(count));
                    Log.d("HorusGeo", file.getPath());
                    FileInputStream inputStream = new FileInputStream(file);
                    String[] name = file.getPath().split("/");
                    result = client.storeFile("/public_ftp/incoming/"+ user + "_" + name[name.length-1], inputStream);
                    inputStream.close();
                    count++;
//                    updateProgress(count, numFiles);
                    publishProgress((int)(count/numFiles));
                }
                count = 0;

                while(count < list.size()){
                    File file = new File(list.get(count));
                    Log.d("HorusGeo", String.valueOf(count));
                    Log.d("HorusGeo", file.getPath());
                    FileInputStream inputStream = new FileInputStream(file);
                    String[] name = file.getPath().split("/");
                    result = client.storeFile("/public_ftp/incoming/img/"+ name[name.length-1], inputStream);
                    inputStream.close();
                    count++;
//                    updateProgress(count+ntables, numFiles);
                    publishProgress((int)((count+ntables)/numFiles));
                }


                client.logout();

                return result;

            }catch (IOException ex) {
                ex.printStackTrace();
            }
            Log.d("HorusGeo", "Return False");
            return false;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            this.dialogProgress.setProgress(progress[0]);
        }

//        @Override
//        public void onProgressUpdate(String... args){
//            dialogProgress.setProgress(args[0]);
//        }

        protected void onPostExecute(Boolean result) {
            if (dialogProgress.isShowing()) {
                dialogProgress.dismiss();
            }
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
