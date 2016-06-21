package horusgeo.eco101;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlantacaoActivity extends AppCompatActivity {

    String idProp;

    ArrayList<Benfeitoria> plants;
    ArrayList<Docs> docs;
    TextView tipoText;
    TextView idadeText;
    TextView consvText;

    LinearLayout plantsLayout;

    ImageButton plantAddButton;

    ImageHelper imgPhoto;

    Uri fileUri;

    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        idProp = intent.getStringExtra("idProp");

        db = new DBHandler(this, null, null, 1);

        plants = db.getPlants(idProp);
        docs = db.getDocs(idProp);

        plantAddButton = (ImageButton) findViewById(R.id.addBenfButton);

        plantsLayout = (LinearLayout) findViewById(R.id.benfsVertLayout);

        imgPhoto = new ImageHelper();

        assert plantAddButton != null;
        plantAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddPlantDialog(-1, -1, null);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToCadastro();
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();

        if(plants.size() > 0){
            plantsLayout.removeAllViews();
            for(Benfeitoria temp : plants){
                db.removePlant(temp.getIdBenf());
                addPlant(temp, plantsLayout.getChildCount()-1);
                for(Docs temp2 : docs){
                    if(temp2.getType().equals(temp.getIdBenf())){
                        db.removeDocs(temp2.getType());
                        Log.d("HorusGeo", "OnCreate " + temp2.getPath());
                        callAddThumb(Integer.parseInt(temp2.getType()), temp2.getPath());
                    }
                }
            }
        }
    }

    public void callAddPlantDialog(final int pose, final int arrayPose, final String ID){

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(PlantacaoActivity.this);
        builderSingle.setTitle("Selecione o Proprietário");

        LayoutInflater inflater = this.getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.plant_dialog, null);
        tipoText = (TextView) rootView.findViewById(R.id.plantTipoText);
        idadeText = (TextView) rootView.findViewById(R.id.plantIdadeText);
        consvText = (TextView) rootView.findViewById(R.id.plantComplText);

        if(ID!=null){
            tipoText.setText(plants.get(arrayPose).getTipo());
            idadeText.setText(plants.get(arrayPose).getIdade());
            consvText.setText(plants.get(arrayPose).getConservacao());
        }

        builderSingle.setView(rootView)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String idplant;

                        if(ID==null) {
                            int lastId;
                            if (plants.size() > 0) {
                                lastId = Integer.parseInt(plants.get(plants.size() - 1).getIdBenf());
                            } else {
                                lastId = 3000;
                            }
                            idplant = String.valueOf(lastId+1);
                        }else{
                            idplant = ID;
                        }
                        plants.add(new Benfeitoria(tipoText.getText().toString(), idadeText.getText().toString(),
                                consvText.getText().toString(), idplant, idProp));

                        addPlant(plants.get(plants.size()-1), pose);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builderSingle.show();
    }

    public void addPlant(final Benfeitoria plant, int pose){

        LayoutInflater inflater = this.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dados_plants, null);

        TextView tipo = (TextView) rootView.findViewById(R.id.plantTipoLabel);
        TextView idade = (TextView) rootView.findViewById(R.id.plantIdadeLabel);
        TextView conserv = (TextView) rootView.findViewById(R.id.plantComplLabel);
        TextView idplant = (TextView) rootView.findViewById(R.id.idPlantInvText);

        ImageButton photoButton = (ImageButton) rootView.findViewById(R.id.plantPhotoButton);
        ImageButton editButton = (ImageButton) rootView.findViewById(R.id.plantEditButton);
        ImageButton delButton = (ImageButton) rootView.findViewById(R.id.plantDelButton);

        tipo.setText("Tipo: " + plant.getTipo());
        idade.setText("Idade Aparente: " + plant.getIdade());
        conserv.setText("Conservação: " + plant.getConservacao());
        idplant.setText(plant.getIdBenf());
        final int place;
        if(pose == -1)
            place = plantsLayout.getChildCount()-1;
        else
            place = pose;

        plantsLayout.addView(rootView, place);

        delButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDelPlant(plant.getIdBenf());
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditPlant(plant.getIdBenf());
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(Integer.parseInt(plant.getIdBenf()));
            }
        });


    }

    public void callEditPlant(String id){
        int viewId = -1;
        int plantId = -1;

        for(int i = 0; i < plantsLayout.getChildCount(); i++){
            View v = plantsLayout.getChildAt(i);
            TextView idplant = (TextView) v.findViewById(R.id.idPlantInvText);
            if(id.equals(idplant.getText().toString())){
                viewId = i;
                break;
            }
        }

        if(viewId!=-1){
            plantsLayout.removeViewAt(viewId);
        }

        for(int i = 0; i < plants.size(); i++){
            if(id.equals(plants.get(i).getIdBenf())){
                plantId = i;
                break;
            }
        }

        callAddPlantDialog(viewId, plantId, id);
    }

    public void callDelPlant(String id){
        int viewId = -1;
        int plantId = -1;

        for(int i = 0; i < plantsLayout.getChildCount(); i++){
            View v = plantsLayout.getChildAt(i);
            TextView idplant = (TextView) v.findViewById(R.id.idPlantInvText);
            if(id.equals(idplant.getText().toString())){
                viewId = i;
                break;
            }
        }
        if(viewId!=-1){
            plantsLayout.removeViewAt(viewId);
        }
        for(int i = 0; i < plants.size(); i++){
            if(id.equals(plants.get(i).getIdBenf())){
                plantId = i;
                break;
            }
        }

        if(plantId!=-1){
            plants.remove(plantId);
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

        int viewId = -1;
        ImageButton img = new ImageButton(this);
        String id = String.valueOf(type);

        for(int i = 0; i < plantsLayout.getChildCount(); i++){
            View v = plantsLayout.getChildAt(i);
            TextView idplant = (TextView) v.findViewById(R.id.idPlantInvText);
            if(id.equals(idplant.getText().toString())){
                viewId = i;
                break;
            }
        }

        img.setImageBitmap(imgPhoto.decodeSampledBitmapFromFile(file, 100, 100));

        View view = plantsLayout.getChildAt(viewId);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.plantPhotoLayout);
        layout.addView(img);

    }

    public void returnToCadastro(){

        db.addPlant(plants);
        db.addDoc(docs);

        Intent intent = new Intent(PlantacaoActivity.this, CadastroActivity.class);
        intent.putExtra("tipo", "edit");
        intent.putExtra("string", idProp);
        startActivity(intent);
    }

}
