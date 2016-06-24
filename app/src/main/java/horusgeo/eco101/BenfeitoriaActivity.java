package horusgeo.eco101;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    ImageHelper imgPhoto;

    Uri fileUri;

    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benfeitoria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        idProp = intent.getStringExtra("idProp");
        Log.d("Benfeitorias", "***** Called onCreate Benfeitorias *****");
        Log.d("Benfeitorias", "idProp = " + idProp);
        db = new DBHandler(this, null, null, 1);

        benfs = db.getBenfeitoria(idProp);
        docs = db.getDocs(idProp);

        Log.d("Benfeitorias", "benfs size = " + benfs.size());
        Log.d("Benfeitorias", "docs size = " + docs.size());

        db.removeBenfeitorias(idProp);
        db.removeDocs(idProp);

        benfAddButton = (ImageButton) findViewById(R.id.addBenfButton);

        benfsLayout = (LinearLayout) findViewById(R.id.benfsVertLayout);

        imgPhoto = new ImageHelper();

        assert benfAddButton != null;
        benfAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Benfeitorias", "***** Add Benf Clicked *****");
                callAddBenfDialog(-1,-1, null);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_ok);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Benfeitorias", "***** Return to Cadastro Clicked *****");
                returnToCadastro();
            }
        });


    }

    @Override
    public void onBackPressed(){

    }

    @Override
    public void onStart(){
        super.onStart();

        Log.d("Benfeitorias", "***** Called onStart Benfeitorias *****");
        Log.d("Benfeitorias", "benfsLayout size antes while = " + String.valueOf(benfsLayout.getChildCount()));
        Log.d("Benfeitorias", "benfs size = " + String.valueOf(benfs.size()));

        while(benfsLayout.getChildCount() > 1)
            benfsLayout.removeViewAt(0);

        Log.d("Benfeitorias", "benfsLayout size depois while = " + String.valueOf(benfsLayout.getChildCount()));

        if(benfs.size() > 0){
            for(Benfeitoria temp : benfs){
                addBenf(temp, benfsLayout.getChildCount()-1, -1);
                Log.d("Benfeitorias", "benfsLayout após add dentro do if = " + String.valueOf(benfsLayout.getChildCount()));
                for(Docs temp2 : docs){
                    Log.d("Benfeitorias", "idBenfs -> benfsLayout = " + temp.getIdBenf() + " benfs = " + temp2.getType());
                    if(temp2.getType().equals(temp.getIdBenf())){
                        Log.d("Benfeitorias", "paths = " + temp2.getPath());
                        callAddThumb(Integer.parseInt(temp2.getType()), temp2.getPath());
                    }
                }
            }
        }
    }


    public void callAddBenfDialog(final int pose, final int arrayPose, final String ID){

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(BenfeitoriaActivity.this);
        builderSingle.setTitle("Selecione o Proprietário");

        LayoutInflater inflater = this.getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.benf_dialog, null);
        tipoText = (TextView) rootView.findViewById(R.id.benfTipoText);
        idadeText = (TextView) rootView.findViewById(R.id.benfIdadeText);
        consvText = (TextView) rootView.findViewById(R.id.benfConservText);

        Log.d("Benfeitorias", "***** Add Benf Dialog *****");
        Log.d("Benfeitorias", "pose = " + pose);
        Log.d("Benfeitorias", "arrayPose = " + arrayPose);
        Log.d("Benfeitorias", "ID = " + ID);
        if(ID!=null){
            tipoText.setText(benfs.get(arrayPose).getTipo());
            idadeText.setText(benfs.get(arrayPose).getIdade());
            consvText.setText(benfs.get(arrayPose).getConservacao());
        }

        builderSingle.setView(rootView);

        builderSingle.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    String idBenf;

                    if(ID==null) {
                        int lastId;
                        Log.d("Benfeitorias", "IF True");
                        Log.d("Benfeitorias", "benfs size = " + benfs.size());
                        if (benfs.size() > 0) {
                            Log.d("Benfeitorias", "benfs idBenf = " + benfs.get(benfs.size() - 1).getIdBenf());
                            lastId = Integer.parseInt(benfs.get(benfs.size() - 1).getIdBenf());
                        } else {
                            lastId = 1000;
                        }
                        Log.d("Benfeitorias", "lastId = " + lastId);
                        idBenf = String.valueOf(lastId+1);
                        Log.d("Benfeitorias", "id Benf = " + idBenf);
                        benfs.add(new Benfeitoria(tipoText.getText().toString(), idadeText.getText().toString(),
                                consvText.getText().toString(), idBenf, idProp));
                        addBenf(benfs.get(benfs.size()-1), pose, -1);
                    }else{
                        Log.d("Benfeitorias", "IF False");
                        idBenf = ID;
                        Log.d("Benfeitorias", "idBenf" + idBenf);

                        benfs.get(arrayPose).setTipo(tipoText.getText().toString());
                        benfs.get(arrayPose).setIdade(idadeText.getText().toString());
                        benfs.get(arrayPose).setConservacao(consvText.getText().toString());
                        addBenf(benfs.get(arrayPose), pose, arrayPose);
                    }


//                    Log.d("Benfeitorias", "benfs size = " + benfs.size());

                }
            });

        if(arrayPose == -1) {
            builderSingle.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.d("Benfeitorias", "***** Cancel Dialog *****");
                    dialog.dismiss();
                }
            });
        }
        builderSingle.show();
    }

    public void addBenf(final Benfeitoria benf, int pose, int arrayPose){

        Log.d("Benfeitorias", "***** ADD BENF *****");
        Log.d("Benfeitorias", "benf = " + benf.getIdBenf());
        Log.d("Benfeitorias", "pose = " + pose);

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
        final int place;
        if(pose == -1)
            place = benfsLayout.getChildCount()-1;
        else
            place = pose;

        Log.d("Benfeitorias", "place = " + place);

        benfsLayout.addView(rootView, place);

        if(arrayPose!=-1)
            callAddPhotosAfterEdit(benf.getIdBenf());

        delButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDelBenf(benf.getIdBenf());
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditBenf(benf.getIdBenf());
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Benfeitorias", "***** TAKE PHOTO *****");
                takePhoto(Integer.parseInt(benf.getIdBenf()));
            }
        });


    }

    public void callEditBenf(String id){
        int viewId = -1;
        int benfId = -1;

        Log.d("Benfeitorias", "***** EDIT BENF *****");
        Log.d("Benfeitorias", "id = " + id);


        Log.d("Benfeitorias", "benfsLayout size = " + benfsLayout.getChildCount());
        for(int i = 0; i < benfsLayout.getChildCount(); i++){
            View v = benfsLayout.getChildAt(i);
            TextView idBenf = (TextView) v.findViewById(R.id.idBenfInvText);

            Log.d("Benfeitorias", "id = " + id + " idBenf = " + idBenf.getText());

            if(id.equals(idBenf.getText())){
                viewId = i;
                break;
            }
        }
        Log.d("Benfeitorias", "viewID = " + viewId);

        if(viewId!=-1){
            Log.d("Benfeitorias", "benfsLayout size = " + benfsLayout.getChildCount());
            benfsLayout.removeViewAt(viewId);
            Log.d("Benfeitorias", "benfsLayout size = " + benfsLayout.getChildCount());
        }

        for(int i = 0; i < benfs.size(); i++){
            Log.d("Benfeitorias", "id = " + id + " benfs id = " + benfs.get(i).getIdBenf());
            if(id.equals(benfs.get(i).getIdBenf())){
                benfId = i;
                break;
            }
        }

        Log.d("Benfeitorias", "benfId = " + benfId);

        callAddBenfDialog(viewId, benfId, id);

    }

    public void callAddPhotosAfterEdit(String id){

        Log.d("Benfeitorias", "***** CALL ADD PHOTOS AFTER EDIT *****");
        Log.d("Benfeitorias", "docs size = " + docs.size());
        for(Docs temp : docs){
            Log.d("Benfeitorias", "temp Type = " + temp.getType() + " idBenf = " + id);
            if(temp.getType().equals(id)){
                callAddThumb(Integer.parseInt(temp.getType()), temp.getPath());
            }
        }
    }


    public void callDelBenf(String id){
        int viewId = -1;
        int benfId = -1;

        Log.d("Benfeitorias", "***** DELETE BENF *****");
        Log.d("Benfeitorias", "id = " + id);

        Log.d("Benfeitorias", "benfsLayout size = " + benfsLayout.getChildCount());
        for(int i = 0; i < benfsLayout.getChildCount(); i++){
            View v = benfsLayout.getChildAt(i);
            TextView idBenf = (TextView) v.findViewById(R.id.idBenfInvText);
            Log.d("Benfeitorias", "id = " + id + " idBenf = " + idBenf.getText());
            if(id.equals(idBenf.getText())){
                viewId = i;
                break;
            }
        }

        Log.d("Benfeitorias", "viewID = " + viewId);

        if(viewId!=-1){
            Log.d("Benfeitorias", "benfsLayout size = " + benfsLayout.getChildCount());
            benfsLayout.removeViewAt(viewId);
            Log.d("Benfeitorias", "benfsLayout size = " + benfsLayout.getChildCount());
        }


        for(int i = 0; i < benfs.size(); i++){
            Log.d("Benfeitorias", "id = " + id + " benfs id = " + benfs.get(i).getIdBenf());
            if(id.equals(benfs.get(i).getIdBenf())){
                benfId = i;
                break;
            }
        }

        Log.d("Benfeitorias", "benfId = " + benfId);

        if(benfId!=-1){
            Log.d("Benfeitorias", "benfs size = " + benfs.size());
            benfs.remove(benfId);
            Log.d("Benfeitorias", "benfs size = " + benfs.size());
        }
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for(int i = 0; i < docs.size(); i++){
            Log.d("Benfeitorias", "i = " + i + " id = " + id + " docs type = " + docs.get(i).getType());
            if(id.equals(docs.get(i).getType())){
                indices.add(i);
            }
        }

        Log.d("Benfeitorias", "docs size = " + docs.size());
        Log.d("Benfeitorias", "indices size = " + indices.size());
        for(int i = indices.size()-1; i >= 0; i--){
            docs.remove(indices.get(i));
        }

        Log.d("Benfeitorias", "docs size = " + docs.size());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            callAddThumb(requestCode, fileUri.getPath());

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Foto cancelada!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Alguma coisa deu errado!", Toast.LENGTH_LONG).show();
        }
        Log.d("HorusGeo", "onActivityResult " + fileUri.getPath());
        String[] name = fileUri.getPath().split("/");
        docs.add(new Docs(fileUri.getPath(), String.valueOf(requestCode), name[name.length-1], idProp));
    }

    public void takePhoto(int type){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = imgPhoto.getOutputMediaFileUri(imgPhoto.MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        // start the image capture Intent
        startActivityForResult(intent, type);
    }

    public void callAddThumb(int type, String file){

        Log.d("Benfeitorias", "***** CALL ADD THUMB *****");
        Log.d("Benfeitorias", "type = " + type);
        Log.d("Benfeitorias", "file = " + file);


        int viewId = -1;
        ImageButton img = new ImageButton(this);
        String id = String.valueOf(type);


        Log.d("Benfeitorias", "benfsLayout size = " + benfsLayout.getChildCount());
        for(int i = 0; i < benfsLayout.getChildCount(); i++){
            View v = benfsLayout.getChildAt(i);
            TextView idBenf = (TextView) v.findViewById(R.id.idBenfInvText);
            Log.d("Benfeitorias", "id = " + id + " idBenf = " + idBenf.getText());
            if(id.equals(idBenf.getText().toString())){
                viewId = i;
                break;
            }
        }

        Log.d("Benfeitorias", "viewId = " + viewId);

        img.setImageBitmap(imgPhoto.decodeSampledBitmapFromFile(file, 100, 100));

        View view = benfsLayout.getChildAt(viewId);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.benfPhotoLayout);
        layout.addView(img);

    }

    public void returnToCadastro(){

        db.addBenf(benfs);
        db.addDoc(docs);

        Intent intent = new Intent(BenfeitoriaActivity.this, CadastroActivity.class);
        intent.putExtra("tipo", "edit");
        intent.putExtra("string", idProp);
        startActivity(intent);
    }

}
