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
import android.view.WindowManager;
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        idProp = intent.getStringExtra("idProp");
        db = new DBHandler(this, null, null, 1);

        benfs = db.getBenfeitoria(idProp);
        docs = db.getDocs(idProp);

        benfAddButton = (ImageButton) findViewById(R.id.addBenfButton);

        benfsLayout = (LinearLayout) findViewById(R.id.benfsVertLayout);

        imgPhoto = new ImageHelper();

        assert benfAddButton != null;
        benfAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddBenfDialog(-1,-1, null);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_ok);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        while(benfsLayout.getChildCount() > 1)
            benfsLayout.removeViewAt(0);


        if(benfs.size() > 0){
            for(Benfeitoria temp : benfs){
                addBenf(temp, benfsLayout.getChildCount()-1, -1);
                for(Docs temp2 : docs){
                    if(temp2.getType().equals(temp.getIdBenf())){
                        callAddThumb(Integer.parseInt(temp2.getType()), temp2.getPath());
                    }
                }
            }
        }
    }


    public void callAddBenfDialog(final int pose, final int arrayPose, final String ID){

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(BenfeitoriaActivity.this);
        builderSingle.setTitle("Adicionar Benfeitoria:");
        builderSingle.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.benf_dialog, null);
        tipoText = (TextView) rootView.findViewById(R.id.benfTipoText);
        idadeText = (TextView) rootView.findViewById(R.id.benfIdadeText);
        consvText = (TextView) rootView.findViewById(R.id.benfConservText);

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
                        if (benfs.size() > 0) {
                            lastId = Integer.parseInt(benfs.get(benfs.size() - 1).getIdBenf());
                        } else {
                            lastId = 1000;
                        }
                        idBenf = String.valueOf(lastId+1);
                        benfs.add(new Benfeitoria(tipoText.getText().toString(), idadeText.getText().toString(),
                                consvText.getText().toString(), idBenf, idProp));
                        addBenf(benfs.get(benfs.size()-1), pose, -1);
                    }else{
                        idBenf = ID;

                        benfs.get(arrayPose).setTipo(tipoText.getText().toString());
                        benfs.get(arrayPose).setIdade(idadeText.getText().toString());
                        benfs.get(arrayPose).setConservacao(consvText.getText().toString());
                        addBenf(benfs.get(arrayPose), pose, arrayPose);
                    }



                }
            });

        if(arrayPose == -1) {
            builderSingle.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
        builderSingle.show();
    }

    public void addBenf(final Benfeitoria benf, int pose, int arrayPose){

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
                takePhoto(Integer.parseInt(benf.getIdBenf()));
            }
        });


    }

    public void callEditBenf(String id){
        int viewId = -1;
        int benfId = -1;

        for(int i = 0; i < benfsLayout.getChildCount(); i++){
            View v = benfsLayout.getChildAt(i);
            TextView idBenf = (TextView) v.findViewById(R.id.idBenfInvText);

            if(id.equals(idBenf.getText())){
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

        callAddBenfDialog(viewId, benfId, id);
    }

    public void callAddPhotosAfterEdit(String id){

        for(Docs temp : docs){
            if(temp.getType().equals(id)){
                callAddThumb(Integer.parseInt(temp.getType()), temp.getPath());
            }
        }
    }


    public void callDelBenf(String id){
        int viewId = -1;
        int benfId = -1;

        for(int i = 0; i < benfsLayout.getChildCount(); i++){
            View v = benfsLayout.getChildAt(i);
            TextView idBenf = (TextView) v.findViewById(R.id.idBenfInvText);
            if(id.equals(idBenf.getText())){
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
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for(int i = 0; i < docs.size(); i++){
            if(id.equals(docs.get(i).getType())){
                indices.add(i);
            }
        }

        for(int i = indices.size()-1; i >= 0; i--){
            docs.remove(indices.get(i));
        }


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
        Log.d("HorusGeo", file);
        int viewId = -1;
        ImageButton img = new ImageButton(this);
        String id = String.valueOf(type);

        for(int i = 0; i < benfsLayout.getChildCount(); i++){
            View v = benfsLayout.getChildAt(i);
            TextView idBenf = (TextView) v.findViewById(R.id.idBenfInvText);
            if(id.equals(idBenf.getText().toString())){
                viewId = i;
                break;
            }
        }

        img.setImageBitmap(imgPhoto.decodeSampledBitmapFromFile(file, 100, 100));

        View view = benfsLayout.getChildAt(viewId);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.benfPhotoLayout);
        layout.addView(img);
    }

    public void returnToCadastro(){


        db.updateRegisterDoc(docs, idProp);
        db.updateRegisterBenf(benfs, idProp);

        Intent intent = new Intent(BenfeitoriaActivity.this, CadastroActivity.class);
        intent.putExtra("tipo", "edit");
        intent.putExtra("string", idProp);
        startActivity(intent);
    }

}
