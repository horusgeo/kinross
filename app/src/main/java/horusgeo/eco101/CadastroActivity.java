package horusgeo.eco101;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.LinkAddress;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CadastroActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    Register cadastro;
    ArrayList<Docs> docs;
    ArrayList<Benfeitoria> benfs;
    ArrayList<Benfeitoria> plants;

    String idBck;
    static String idPropBenf;

    DBHandler db;

    static boolean editTable;

    int tabSelected;

    private static final int CAPTURE_IMAGE_ID_PROP = 100;
    private static final int CAPTURE_IMAGE_CPF_PROP = 101;
    private static final int CAPTURE_IMAGE_ID_CONJ = 102;
    private static final int CAPTURE_IMAGE_CPF_CONJ = 103;
    private static final int CAPTURE_IMAGE_END_RES = 104;
    private static final int CAPTURE_IMAGE_END_OBJ = 105;
    private static final int CAPTURE_IMAGE_IDENT_PROP = 106;
    private static final int CAPTURE_IMAGE_COMP_END_PROP = 107;
    private static final int CAPTURE_IMAGE_CAS_PROP = 108;
    private static final int CAPTURE_IMAGE_CTPS_PROP = 109;
    private static final int CAPTURE_IMAGE_CNH_PROP = 110;
    private static final int CAPTURE_IMAGE_RG_HERD = 111;
    private static final int CAPTURE_IMAGE_CPF_HERD = 112;
    private static final int CAPTURE_IMAGE_PART_HERD = 113;
    private static final int CAPTURE_IMAGE_OBT_HERD = 114;
    private static final int CAPTURE_IMAGE_OBT_ESPO = 115;
    private static final int CAPTURE_IMAGE_PROC_ESPO = 116;
    private static final int CAPTURE_IMAGE_CPF_ESPO = 117;
    private static final int CAPTURE_IMAGE_RG_ESPO = 118;
    private static final int CAPTURE_IMAGE_CONT_PJ = 119;
    private static final int CAPTURE_IMAGE_ULT_PJ = 120;
    private static final int CAPTURE_IMAGE_CNPJ = 121;
    private static final int CAPTURE_IMAGE_RG_PJ = 122;
    private static final int CAPTURE_IMAGE_RG_PJ_CONJ = 123;
    private static final int CAPTURE_IMAGE_END_PJ = 124;
    private static final int CAPTURE_IMAGE_CERT_REG = 125;
    private static final int CAPTURE_IMAGE_CERT_NEG = 126;
    private static final int CAPTURE_IMAGE_ESCR = 127;
    private static final int CAPTURE_IMAGE_CONT_COMP_VEND = 128;
    private static final int CAPTURE_IMAGE_IPTU = 129;
    private static final int CAPTURE_IMAGE_CCIR = 130;
    private static final int CAPTURE_IMAGE_ITR = 131;


    private static Uri fileUri;

    static ImageHelper imgPhoto;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cadastro = new Register();
        benfs = new ArrayList<Benfeitoria>();
        docs = new ArrayList<Docs>();
        plants = new ArrayList<Benfeitoria>();

        db = new DBHandler(this, null, null, 1);

        imgPhoto = new ImageHelper();

        Intent intent = getIntent();

        String tipo = intent.getStringExtra("tipo");
        String text1 = intent.getStringExtra("string");
        String user = intent.getStringExtra("user");

        if(tipo.equals("new")){
            setTitle(text1);
            cadastro.set_data_visita(DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE).format(new Date()));
            cadastro.set_horario_chegada(DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE).format(new Date()));
            cadastro.set_responsavel(Responsavel.TESTE);
            editTable = false;
        }else if(tipo.equals("edit")){
            cadastro = db.getRegister(text1);
            benfs = db.getBenfeitoria(text1);
            docs = db.getDocs(text1);
            plants = db.getPlants(text1);
            idPropBenf = cadastro.get_id_prop();

            Log.d("HorusGeo", String.valueOf(docs.size()));
            setTitle(cadastro.get_nome_proprietario());
            idBck = cadastro.get_id_prop();
            db.removeBenfeitorias(text1);
            db.removePlant(text1);
            db.removeDocs(text1);
            editTable = true;
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager){
                    @Override
                    public void onTabSelected(TabLayout.Tab tab){
                        super.onTabSelected(tab);
                        tabSelected = tab.getPosition();
                        switch(tabSelected){
                            case 1:
                                loadCadastroRelatorio();
                                break;
                            case 2:
                                loadCadastroProp();
                                break;
                            case 3:
                                loadCadastroConj();
                                break;
                            case 4:
                                loadCadastroEndRes();
                                break;
                            case 5:
                                loadCadastroEndObj();
                                break;
                            case 6:
                                loadCadastroIdProp();
                                break;
                            case 7:
                                saveCadastroToBenfsPlants();
                                loadCadastroBenf();
                                break;
                            case 8:
                                saveCadastroToBenfsPlants();
                                loadCadastroPlant();
                                break;
                            case 9:
                                loadCadastroDesc();
                                break;
                            case 10:
                                loadCadastroCheckList();
                                break;
                            case 11:
                                loadCadastroObs();
                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab){
                        super.onTabUnselected(tab);

                        switch(tab.getPosition()){
                            case 1:
                                saveCadastroRelatorio();
                                break;
                            case 2:
                                saveCadastroProp();
                                break;
                            case 3:
                                saveCadastroConj();
                                break;
                            case 4:
                                saveCadastroEndRes();
                                break;
                            case 5:
                                saveCadastroEndObj();
                                break;
                            case 6:
                                saveCadastroIdProp();
                                break;
                            case 7:
                                saveCadastroBenf();
                                break;
                            case 8:
                                saveCadastroPlant();
                                break;
                            case 9:
                                saveCadastroDesc();
                                break;
                            case 10:
                                saveCadastrocheckList();
                                break;
                            case 11:
                                saveCadastroObs();
                                break;
                        }
                    }
                }
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_ok);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tabSelected==1)
                    saveCadastroRelatorio();
                if(tabSelected==2)
                    saveCadastroProp();

                if(cadastro.get_id_prop()!=null && cadastro.get_nome_proprietario()!=null) {
                    saveCadastro();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadastroActivity.this);
                    builder.setMessage("Por Favor, Defina um número de identificação e \n" +
                            "um nome de proprietátio! ")
                            .setPositiveButton("Retornar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
            }
        });

        FloatingActionButton fab_cancel = (FloatingActionButton) findViewById(R.id.fab_cancel);
        assert fab_cancel != null;
        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroActivity.this, InitialActivity.class);
                if(cadastro.get_id_prop()!=null) {
                    if(editTable){
                        intent.putExtra("tipo", "cancel");
                        intent.putExtra("texto", "Edição cancelada!");
                    }else{
                        intent.putExtra("tipo", "cancel");
                        intent.putExtra("texto", "Cadastro cancelado!");
                        if(db.checkIfPropExists(cadastro.get_id_prop())){
                            db.deleteRegister(cadastro.get_id_prop());
                        }
                    }
                }else{
                    intent.putExtra("tipo", "cancel");
                    intent.putExtra("texto", "Cadastro cancelado!");
                }
                startActivity(intent);
            }
        });

        FloatingActionButton fab_photo = (FloatingActionButton) findViewById(R.id.fab_photo);
        assert fab_photo != null;
        fab_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhotos();
            }
        });

        assert toolbar != null;
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(CadastroActivity.this, DocumentosActivity.class);
                intent.putExtra("idProp", cadastro.get_id_prop());
                db.addDoc(docs);
                startActivity(intent);

                return false;
            }
        });


    }


    public void callPhotos(){

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CadastroActivity.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                CadastroActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(FotoTypes.NAMES);

        builderSingle.setNegativeButton(
                "Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        if(cadastro.get_id_prop() == null){
            builderSingle.setTitle("Por Favor, Selecione um número de identificação do proprietário!");

        }else{
            builderSingle.setTitle("Selecione o documento");

            builderSingle.setAdapter(
                    arrayAdapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            takePhoto(CadastroActivity.this, FotoTypes.CODES.get(which));

                        }
                    });
        }
        builderSingle.show();



    }

    @Override
    public void onBackPressed(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView;

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.layout_initial_tab, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.layout_relatorio, container, false);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.layout_dados_prop, container, false);
                    ImageButton idPropPhoto = (ImageButton) rootView.findViewById(R.id.identPhotoButton);
                    ImageButton cpfPropPhoto = (ImageButton) rootView.findViewById(R.id.cpfPhotoButton);

                    idPropPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            takePhoto(getActivity(), CAPTURE_IMAGE_ID_PROP);
                        }
                    });

                    cpfPropPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            takePhoto(getActivity(), CAPTURE_IMAGE_CPF_PROP);
                        }
                    });
                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.layout_dados_conj, container, false);
                    ImageButton idConjPhoto = (ImageButton) rootView.findViewById(R.id.conjDocIdButton);
                    ImageButton cpfConjPhoto = (ImageButton) rootView.findViewById(R.id.conjCPFButton);

                    idConjPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            takePhoto(getActivity(), CAPTURE_IMAGE_ID_CONJ);
                        }
                    });

                    cpfConjPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            takePhoto(getActivity(), CAPTURE_IMAGE_CPF_CONJ);
                        }
                    });
                    break;
                case 5:
                    rootView = inflater.inflate(R.layout.layout_end_residencial, container, false);
                    break;
                case 6:
                    rootView = inflater.inflate(R.layout.layout_end_obj, container, false);
                    break;
                case 7:
                    rootView = inflater.inflate(R.layout.layout_id_prop, container, false);
                    break;
                case 8:
                    rootView = inflater.inflate(R.layout.layout_benf, container, false);
                    Button benfButton = (Button) rootView.findViewById(R.id.benfButton);
                    benfButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), BenfeitoriaActivity.class);
                            intent.putExtra("idProp", idPropBenf);
                            startActivity(intent);
                        }
                    });
                    break;
                case 9:
                    rootView = inflater.inflate(R.layout.layout_plant, container, false);
                    Button plantButton = (Button) rootView.findViewById(R.id.plantButton);
                    plantButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PlantacaoActivity.class);
                            intent.putExtra("idProp", idPropBenf);
                            startActivity(intent);
                        }
                    });
                    break;
                case 10:
                    rootView = inflater.inflate(R.layout.layout_descricao, container, false);
                    break;
                case 11:
                    rootView = inflater.inflate(R.layout.layout_checklist, container, false);
                    break;
                case 12:
                    rootView = inflater.inflate(R.layout.layout_obs, container, false);
                    break;
                case 13:
                    rootView = inflater.inflate(R.layout.layout_map, container, false);
                    Button mapButton = (Button) rootView.findViewById(R.id.mapButtonCadastro);
                    mapButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), MappingActivity.class);
                            intent.putExtra("id", idPropBenf);
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    rootView = inflater.inflate(R.layout.layout_relatorio, container, false);

            }
            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 13;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Eco - 101";
                case 1:
                    return "Relatório";
                case 2:
                    return "Dados Proprietário";
                case 3:
                    return "Dados Cônjuge";
                case 4:
                    return "Endereço Residencial";
                case 5:
                    return "Endereço Objeto";
                case 6:
                    return "Identificação da Propriedade";
                case 7:
                    return "Benfeitoria";
                case 8:
                    return "Plantações";
                case 9:
                    return "Descrição da Visita";
                case 10:
                    return "Check list";
                case 11:
                    return "Observações";
                case 12:
                    return "Mapa";
            }
            return null;
        }
    }

    //Cadastros Save/Load

    void saveCadastroRelatorio(){
        TextView nomeProjeto = (TextView) findViewById(R.id.projetoText);
        TextView idProp = (TextView) findViewById(R.id.idPropText);
        TextView localVisita = (TextView) findViewById(R.id.localText);
        TextView dataVisita = (TextView) findViewById(R.id.dataText);
        RadioGroup statusVisita = (RadioGroup) findViewById(R.id.statusGroup);

        cadastro.set_nome_projeto(nomeProjeto.getText().toString());
        cadastro.set_id_prop(idProp.getText().toString());
        cadastro.set_local_visita(localVisita.getText().toString());
        cadastro.set_data_visita(dataVisita.getText().toString());

        switch(statusVisita.getCheckedRadioButtonId()){
            case R.id.accRadioButton:
                cadastro.set_status("1");
                break;
            case R.id.negRadioButton:
                cadastro.set_status("2");
                break;
            case R.id.rejRadioButton:
                cadastro.set_status("3");
                break;
            case R.id.indRadioButton:
                cadastro.set_status("0");
                break;
        }

        idPropBenf = cadastro.get_id_prop();

    }

    void loadCadastroRelatorio(){
        TextView nomeProjeto = (TextView) findViewById(R.id.projetoText);
        TextView idProp = (TextView) findViewById(R.id.idPropText);
        TextView localVisita = (TextView) findViewById(R.id.localText);
        TextView dataVisita = (TextView) findViewById(R.id.dataText);
        RadioGroup statusVisita = (RadioGroup) findViewById(R.id.statusGroup);

        nomeProjeto.setText(cadastro.get_nome_projeto());
        idProp.setText(cadastro.get_id_prop());
        localVisita.setText(cadastro.get_local_visita());
        dataVisita.setText(cadastro.get_data_visita());

        if(cadastro.get_status() == null){
            statusVisita.check(R.id.indRadioButton);
        }else{
            switch(Integer.valueOf(cadastro.get_status())){
                case 1:
                    statusVisita.check(R.id.accRadioButton);
                    break;
                case 2:
                    statusVisita.check(R.id.negRadioButton);
                    break;
                case 3:
                    statusVisita.check(R.id.rejRadioButton);
                    break;
                case 4:
                    statusVisita.check(R.id.indRadioButton);
                    break;
            }
        }

    }

    void saveCadastroProp(){

        TextView nome = (TextView) findViewById(R.id.nomeText);
        TextView nacio = (TextView) findViewById(R.id.nacionalidadeText);
        TextView prof = (TextView) findViewById(R.id.profissaoText);
        TextView doc = (TextView) findViewById(R.id.identidadeText);
        TextView doctp = (TextView) findViewById(R.id.idTipoText);
        RadioGroup est = (RadioGroup) findViewById(R.id.estadoCivilGroup);
        TextView cpf = (TextView) findViewById(R.id.cpfText);
        TextView tel1 = (TextView) findViewById(R.id.propTel1);
        TextView tel2 = (TextView) findViewById(R.id.propTel2);
        TextView email = (TextView) findViewById(R.id.emailText);
        RadioGroup poss = (RadioGroup) findViewById(R.id.posPropRadioGroup);

        cadastro.set_nome_proprietario(nome.getText().toString());
        cadastro.set_nacionalidade_prop(nacio.getText().toString());
        cadastro.set_profissao_prop(prof.getText().toString());
        cadastro.set_doc_id_prop(doc.getText().toString());
        cadastro.set_doc_id_tipo_prop(doctp.getText().toString());
        cadastro.set_cpf_prop(cpf.getText().toString());
        cadastro.set_tel_prop_1(tel1.getText().toString());
        cadastro.set_tel_prop_2(tel2.getText().toString());
        cadastro.set_email_prop(email.getText().toString());

        switch(est.getCheckedRadioButtonId()){
            case R.id.casadoRadio:
                cadastro.set_estado_civil("1");
                break;
            case R.id.divorciadoRadio:
                cadastro.set_estado_civil("2");
                break;
            case R.id.viuvoRadio:
                cadastro.set_estado_civil("3");
                break;
            case R.id.solteiroRadio:
                cadastro.set_estado_civil("4");
                break;
            default:
                cadastro.set_estado_civil("0");
                break;
        }

        switch(poss.getCheckedRadioButtonId()){
            case R.id.possRadioButton:
                cadastro.set_possProp("1");
                break;
            case R.id.propRadioButton:
                cadastro.set_possProp("2");
                break;
            default:
                cadastro.set_possProp("0");
                break;
        }



    }

    public void loadCadastroProp(){
        TextView nome = (TextView) findViewById(R.id.nomeText);
        TextView nacio = (TextView) findViewById(R.id.nacionalidadeText);
        TextView prof = (TextView) findViewById(R.id.profissaoText);
        TextView doc = (TextView) findViewById(R.id.identidadeText);
        TextView doctp = (TextView) findViewById(R.id.idTipoText);
        RadioGroup est = (RadioGroup) findViewById(R.id.estadoCivilGroup);
        TextView cpf = (TextView) findViewById(R.id.cpfText);
        TextView tel1 = (TextView) findViewById(R.id.propTel1);
        TextView tel2 = (TextView) findViewById(R.id.propTel2);
        TextView email = (TextView) findViewById(R.id.emailText);
        RadioGroup pos = (RadioGroup) findViewById(R.id.posPropRadioGroup);

        nome.setText(cadastro.get_nome_proprietario());
        nacio.setText(cadastro.get_nacionalidade_prop());
        prof.setText(cadastro.get_profissao_prop());
        doc.setText(cadastro.get_doc_id_prop());
        doctp.setText(cadastro.get_doc_id_tipo_prop());

        if(cadastro.get_estado_civil()!= null){
            switch (cadastro.get_estado_civil()){
                case "1":
                    est.check(R.id.casadoRadio);
                    break;
                case "2":
                    est.check(R.id.divorciadoRadio);
                    break;
                case "3":
                    est.check(R.id.viuvoRadio);
                    break;
                case "4":
                    est.check(R.id.solteiroRadio);
                    break;
            }

        }

        if(cadastro.get_possProp()!=null){
            switch (cadastro.get_possProp()){
                case "1":
                    est.check(R.id.possRadioButton);
                    break;
                case "2":
                    est.check(R.id.propRadioButton);
                    break;
            }
        }

        cpf.setText(cadastro.get_cpf_prop());
        tel1.setText(cadastro.get_tel_prop_1());
        tel2.setText(cadastro.get_tel_prop_2());
        email.setText(cadastro.get_email_prop());
        Log.d("HorusGeo", "Docs size = " + String.valueOf(docs.size()));

        LinearLayout layout = (LinearLayout) findViewById(R.id.idPropLayout);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.cpfPropLayout);

        layout.removeAllViews();
        layout2.removeAllViews();

        if(docs.size() > 0) {
            for (Docs temp : docs) {
                Log.d("HorusGeo", temp.getIdProp() + " " + cadastro.get_id_prop());
                if(temp.getIdProp().equals(cadastro.get_id_prop())) {
                    Log.d("HorusGeo", temp.getType() + " " + String.valueOf(CAPTURE_IMAGE_ID_PROP));
                    if (temp.getType().equals(String.valueOf(CAPTURE_IMAGE_ID_PROP))) {
                        callAddThumb(CAPTURE_IMAGE_ID_PROP, temp.getPath());
                    } else if (temp.getType().equals(String.valueOf(CAPTURE_IMAGE_CPF_PROP))) {
                        callAddThumb(CAPTURE_IMAGE_CPF_PROP, temp.getPath());
                    }
                }
            }
        }
    }

    void saveCadastroConj(){

        TextView nome = (TextView) findViewById(R.id.conjNomeText);
        TextView nacio = (TextView) findViewById(R.id.conjNacText);
        TextView prof = (TextView) findViewById(R.id.conjProfText);
        TextView doc = (TextView) findViewById(R.id.conjDocIdText);
        TextView doctp = (TextView) findViewById(R.id.conjTpOrgText);
        TextView cpf = (TextView) findViewById(R.id.conjCPFText);
        TextView tel1 = (TextView) findViewById(R.id.conjTel1Text);
        TextView tel2 = (TextView) findViewById(R.id.conjTel2Text);

        cadastro.set_nome_conj(nome.getText().toString());
        cadastro.set_nacionalidade_conj(nacio.getText().toString());
        cadastro.set_profissao_conj(prof.getText().toString());
        cadastro.set_doc_id_conj(doc.getText().toString());
        cadastro.set_doc_id_tipo_conj(doctp.getText().toString());
        cadastro.set_cpf_conj(cpf.getText().toString());
        cadastro.set_tel_conj_1(tel1.getText().toString());
        cadastro.set_tel_conj_2(tel2.getText().toString());

    }

    public void loadCadastroConj(){
        TextView nome = (TextView) findViewById(R.id.conjNomeText);
        TextView nacio = (TextView) findViewById(R.id.conjNacText);
        TextView prof = (TextView) findViewById(R.id.conjProfText);
        TextView doc = (TextView) findViewById(R.id.conjDocIdText);
        TextView doctp = (TextView) findViewById(R.id.conjTpOrgText);
        TextView cpf = (TextView) findViewById(R.id.conjCPFText);
        TextView tel1 = (TextView) findViewById(R.id.conjTel1Text);
        TextView tel2 = (TextView) findViewById(R.id.conjTel2Text);

        nome.setText(cadastro.get_nome_conj());
        nacio.setText(cadastro.get_nacionalidade_conj());
        prof.setText(cadastro.get_profissao_conj());
        doc.setText(cadastro.get_doc_id_conj());
        doctp.setText(cadastro.get_doc_id_tipo_conj());
        cpf.setText(cadastro.get_cpf_conj());
        tel1.setText(cadastro.get_tel_conj_1());
        tel2.setText(cadastro.get_tel_conj_2());

        LinearLayout layout = (LinearLayout) findViewById(R.id.idConjLayout);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.cpfConjLayout);

        layout.removeAllViews();
        layout2.removeAllViews();

        if(docs.size() > 0) {
            for (Docs temp : docs) {
                Log.d("HorusGeo", temp.getIdProp() + " " + cadastro.get_id_prop());
                if(temp.getIdProp().equals(cadastro.get_id_prop())) {
                    Log.d("HorusGeo", temp.getType() + " " + String.valueOf(CAPTURE_IMAGE_ID_CONJ));
                    if (temp.getType().equals(String.valueOf(CAPTURE_IMAGE_ID_CONJ))) {
                        callAddThumb(CAPTURE_IMAGE_ID_CONJ, temp.getPath());
                    } else if (temp.getType().equals(String.valueOf(CAPTURE_IMAGE_CPF_CONJ))) {
                        callAddThumb(CAPTURE_IMAGE_CPF_CONJ, temp.getPath());
                    }
                }
            }
        }
    }

    public void saveCadastroEndRes(){
        TextView rua = (TextView) findViewById(R.id.ruaEndResText);
        TextView num= (TextView) findViewById(R.id.numEndResText);
        TextView cmpl = (TextView) findViewById(R.id.complEndResText);
        TextView bairro = (TextView) findViewById(R.id.bairroEndResText);
        TextView cep = (TextView) findViewById(R.id.cepEndResText);
        TextView mun = (TextView) findViewById(R.id.munEndResText);
        TextView uf = (TextView) findViewById(R.id.ufEndResText);
        TextView com = (TextView) findViewById(R.id.comEndResText);
        TextView comuf = (TextView) findViewById(R.id.comUfEndResText);
        TextView pto = (TextView) findViewById(R.id.ptoRefEndResText);

        cadastro.set_rua_end_res(rua.getText().toString());
        cadastro.set_n_end_res(num.getText().toString());
        cadastro.set_compl_end_res(cmpl.getText().toString());
        cadastro.set_bairro_end_res(bairro.getText().toString());
        cadastro.set_cep_end_res(cep.getText().toString());
        cadastro.set_municipio_end_res(mun.getText().toString());
        cadastro.set_uf_mun_end_res(uf.getText().toString());
        cadastro.set_comarca_end_res(com.getText().toString());
        cadastro.set_uf_com_end_res(comuf.getText().toString());
        cadastro.set_p_ref_end_res(pto.getText().toString());
    }

    public void loadCadastroEndRes(){
        TextView rua = (TextView) findViewById(R.id.ruaEndResText);
        TextView num= (TextView) findViewById(R.id.numEndResText);
        TextView cmpl = (TextView) findViewById(R.id.complEndResText);
        TextView bairro = (TextView) findViewById(R.id.bairroEndResText);
        TextView cep = (TextView) findViewById(R.id.cepEndResText);
        TextView mun = (TextView) findViewById(R.id.munEndResText);
        TextView uf = (TextView) findViewById(R.id.ufEndResText);
        TextView com = (TextView) findViewById(R.id.comEndResText);
        TextView comuf = (TextView) findViewById(R.id.comUfEndResText);
        TextView pto = (TextView) findViewById(R.id.ptoRefEndResText);

        rua.setText(cadastro.get_rua_end_res());
        num.setText(cadastro.get_n_end_res());
        cmpl.setText(cadastro.get_compl_end_res());
        bairro.setText(cadastro.get_bairro_end_res());
        cep.setText(cadastro.get_cep_end_res());
        mun.setText(cadastro.get_municipio_end_res());
        uf.setText(cadastro.get_uf_mun_end_res());
        com.setText(cadastro.get_comarca_end_res());
        comuf.setText(cadastro.get_uf_com_end_res());
        pto.setText(cadastro.get_p_ref_end_res());


    }

    public void saveCadastroEndObj(){
        TextView rua = (TextView) findViewById(R.id.ruaEndObjText);
        TextView num= (TextView) findViewById(R.id.numEndObjText);
        TextView cmpl = (TextView) findViewById(R.id.complEndObjText);
        TextView bairro = (TextView) findViewById(R.id.bairroEndObjText);
        TextView cep = (TextView) findViewById(R.id.cepEndObjText);
        TextView pto = (TextView) findViewById(R.id.ptoRefEndObjText);

        cadastro.set_rua_end_obj(rua.getText().toString());
        cadastro.set_n_end_obj(num.getText().toString());
        cadastro.set_compl_end_obj(cmpl.getText().toString());
        cadastro.set_bairro_end_obj(bairro.getText().toString());
        cadastro.set_cep_end_obj(cep.getText().toString());
        cadastro.set_p_ref_end_obj(pto.getText().toString());
    }

    public void loadCadastroEndObj(){
        TextView rua = (TextView) findViewById(R.id.ruaEndObjText);
        TextView num= (TextView) findViewById(R.id.numEndObjText);
        TextView cmpl = (TextView) findViewById(R.id.complEndObjText);
        TextView bairro = (TextView) findViewById(R.id.bairroEndObjText);
        TextView cep = (TextView) findViewById(R.id.cepEndObjText);
        TextView pto = (TextView) findViewById(R.id.ptoRefEndObjText);

        rua.setText(cadastro.get_rua_end_obj());
        num.setText(cadastro.get_n_end_obj());
        cmpl.setText(cadastro.get_compl_end_obj());
        bairro.setText(cadastro.get_bairro_end_obj());
        cep.setText(cadastro.get_cep_end_obj());
        pto.setText(cadastro.get_p_ref_end_obj());


    }

    public void saveCadastroIdProp(){
        RadioGroup zon = (RadioGroup) findViewById(R.id.zoneGroup);
        RadioGroup topo = (RadioGroup) findViewById(R.id.topoGroup);
        RadioGroup man = (RadioGroup) findViewById(R.id.mananGroup);
        CheckBox infra1 = (CheckBox) findViewById(R.id.infraRedeEleRadio);
        CheckBox infra2 = (CheckBox) findViewById(R.id.infraSinTelRadio);
        CheckBox infra3 = (CheckBox) findViewById(R.id.infraAbasAguaRadio);
        TextView obs = (TextView) findViewById(R.id.idPropObsText);

        switch(zon.getCheckedRadioButtonId()){
            case R.id.zoneRuralRadio:
                cadastro.set_zoneamento("1");
                break;
            case R.id.zoneUrbRadio:
                cadastro.set_zoneamento("2");
                break;
            case R.id.zoneExpRadio:
                cadastro.set_zoneamento("3");
                break;
            default:
                cadastro.set_zoneamento("0");
                break;
        }

        switch(topo.getCheckedRadioButtonId()){
            case R.id.topoPlanoRadio:
                cadastro.set_topografia("1");
                break;
            case R.id.topoOndRadio:
                cadastro.set_topografia("2");
                break;
            case R.id.topoMontRadio:
                cadastro.set_topografia("3");
                break;
            default:
                cadastro.set_topografia("0");
                break;
        }

        switch(man.getCheckedRadioButtonId()){
            case R.id.manaSimRadio:
                cadastro.set_manacial("1");
                break;
            case R.id.mananNaoRadio:
                cadastro.set_manacial("2");
                break;
            default:
                cadastro.set_topografia("0");
                break;
        }

        cadastro.set_infraredeel(String.valueOf(infra1.isChecked()));
        cadastro.set_infrasintel(String.valueOf(infra2.isChecked()));
        cadastro.set_infraabasagua(String.valueOf(infra3.isChecked()));
        cadastro.set_obs_id_prop(obs.getText().toString());
    }

    public void loadCadastroIdProp(){

        RadioGroup zon = (RadioGroup) findViewById(R.id.zoneGroup);
        RadioGroup topo = (RadioGroup) findViewById(R.id.topoGroup);
        RadioGroup man = (RadioGroup) findViewById(R.id.mananGroup);
        CheckBox infra1 = (CheckBox) findViewById(R.id.infraRedeEleRadio);
        CheckBox infra2 = (CheckBox) findViewById(R.id.infraSinTelRadio);
        CheckBox infra3 = (CheckBox) findViewById(R.id.infraAbasAguaRadio);
        TextView obs = (TextView) findViewById(R.id.idPropObsText);

        if(cadastro.get_zoneamento() != null){
            switch(cadastro.get_zoneamento()){
                case "1":
                    zon.check(R.id.zoneRuralRadio);
                    break;
                case "2":
                    zon.check(R.id.zoneUrbRadio);
                    break;
                case "3":
                    zon.check(R.id.zoneExpRadio);
                    break;
            }
        }

        if(cadastro.get_topografia() != null){
            switch(cadastro.get_topografia()){
                case "1":
                    topo.check(R.id.topoPlanoRadio);
                    break;
                case "2":
                    topo.check(R.id.topoOndRadio);
                    break;
                case "3":
                    topo.check(R.id.topoMontRadio);
                    break;
            }
        }

        if(cadastro.get_manacial() != null){
            switch(cadastro.get_manacial()){
                case "1":
                    man.check(R.id.manaSimRadio);
                    break;
                case "2":
                    man.check(R.id.mananNaoRadio);
                    break;
            }
        }

        if(cadastro.get_infraredeel() != null)
            infra1.setChecked(Boolean.parseBoolean(cadastro.get_infraredeel()));
        if(cadastro.get_infrasintel() != null)
            infra2.setChecked(Boolean.parseBoolean(cadastro.get_infrasintel()));
        if(cadastro.get_infraabasagua() != null)
            infra3.setChecked(Boolean.parseBoolean(cadastro.get_infraabasagua()));
        obs.setText(cadastro.get_obs_id_prop());

    }

    public void saveCadastroBenf(){

    }

    public void loadCadastroBenf(){

    }

    public void saveCadastroPlant(){

    }

    public void loadCadastroPlant(){

    }

    public void saveCadastroDesc(){
        TextView h_chegada = (TextView) findViewById(R.id.horarioChegada);
        TextView h_saida = (TextView) findViewById(R.id.horarioSaida);
        TextView desc = (TextView) findViewById(R.id.descricaoText);
        TextView resp = (TextView) findViewById(R.id.responsavelText);

        cadastro.set_horario_chegada(h_chegada.getText().toString());
        cadastro.set_horario_saida(h_saida.getText().toString());
        cadastro.set_descricao_visita(desc.getText().toString());
        cadastro.set_responsavel(resp.getText().toString());
    }

    public void loadCadastroDesc(){
        TextView h_chegada = (TextView) findViewById(R.id.horarioChegada);
        TextView h_saida = (TextView) findViewById(R.id.horarioSaida);
        TextView desc = (TextView) findViewById(R.id.descricaoText);
        TextView resp = (TextView) findViewById(R.id.responsavelText);

        h_chegada.setText(cadastro.get_horario_chegada());
        h_saida.setText(cadastro.get_horario_saida());
        desc.setText(cadastro.get_descricao_visita());
        resp.setText(cadastro.get_responsavel());
    }

    public void saveCadastroObs(){
        TextView obsText = (TextView) findViewById(R.id.obsText);

        cadastro.setObservacao(obsText.getText().toString());
    }

    public void loadCadastroObs(){
        TextView obsText = (TextView) findViewById(R.id.obsText);

        obsText.setText(cadastro.getObservacao());
    }

    public void saveCadastrocheckList(){

    }

    public void loadCadastroCheckList(){

        CheckBox checkBox = null;

        for(Docs temp : docs){
            switch(Integer.parseInt(temp.getType())){

                case CAPTURE_IMAGE_ID_PROP:
                    checkBox = (CheckBox) findViewById(R.id.checkListIDProp);
                    break;
                case CAPTURE_IMAGE_CPF_PROP:
                    checkBox = (CheckBox) findViewById(R.id.checkListCPFProp);
                    break;
                case CAPTURE_IMAGE_ID_CONJ:
                    checkBox = (CheckBox) findViewById(R.id.checkListIDProp);
                    break;
                case CAPTURE_IMAGE_CPF_CONJ:
                    checkBox = (CheckBox) findViewById(R.id.checkListCPFProp);
                    break;
                case CAPTURE_IMAGE_COMP_END_PROP:
                    checkBox = (CheckBox) findViewById(R.id.checkListEnd);
                    break;
                case CAPTURE_IMAGE_CAS_PROP:
                    checkBox = (CheckBox) findViewById(R.id.checkListCas);
                    break;
                case CAPTURE_IMAGE_CTPS_PROP:
                    checkBox = (CheckBox) findViewById(R.id.checkListCTPS);
                    break;
                case CAPTURE_IMAGE_CNH_PROP:
                    checkBox = (CheckBox) findViewById(R.id.checkListCNH);
                    break;
                case CAPTURE_IMAGE_RG_HERD:
                    checkBox = (CheckBox) findViewById(R.id.checkListIDHerd);
                    break;
                case CAPTURE_IMAGE_CPF_HERD:
                    checkBox = (CheckBox) findViewById(R.id.checkListCPFProp);
                    break;
                case CAPTURE_IMAGE_PART_HERD:
                    checkBox = (CheckBox) findViewById(R.id.checkListPartHerd);
                    break;
                case CAPTURE_IMAGE_OBT_HERD:
                    checkBox = (CheckBox) findViewById(R.id.checkListObHerd);
                    break;
                case CAPTURE_IMAGE_OBT_ESPO:
                    checkBox = (CheckBox) findViewById(R.id.checkListObtEspolio);
                    break;
                case CAPTURE_IMAGE_PROC_ESPO:
                    checkBox = (CheckBox) findViewById(R.id.checkListProcEspolio);
                    break;
                case CAPTURE_IMAGE_CPF_ESPO:
                    checkBox = (CheckBox) findViewById(R.id.checkListCPFEspolio);
                    break;
                case CAPTURE_IMAGE_RG_ESPO:
                    checkBox = (CheckBox) findViewById(R.id.checkListRGEspolio);
                    break;
                case CAPTURE_IMAGE_CONT_PJ:
                    checkBox = (CheckBox) findViewById(R.id.checkListContrato);
                    break;
                case CAPTURE_IMAGE_ULT_PJ:
                    checkBox = (CheckBox) findViewById(R.id.checkListUltimaAlteracao);
                    break;
                case CAPTURE_IMAGE_CNPJ:
                    checkBox = (CheckBox) findViewById(R.id.checkListCNPJ);
                    break;
                case CAPTURE_IMAGE_RG_PJ:
                    checkBox = (CheckBox) findViewById(R.id.checkListRGPJ);
                    break;
                case CAPTURE_IMAGE_RG_PJ_CONJ:
                    checkBox = (CheckBox) findViewById(R.id.checkListRGPJConj);
                    break;
                case CAPTURE_IMAGE_END_PJ:
                    checkBox = (CheckBox) findViewById(R.id.checkListEndPJ);
                    break;
                case CAPTURE_IMAGE_CERT_REG:
                    checkBox = (CheckBox) findViewById(R.id.checkListRegImovel);
                    break;
                case CAPTURE_IMAGE_CERT_NEG:
                    checkBox = (CheckBox) findViewById(R.id.checkListNegImovel);
                    break;
                case CAPTURE_IMAGE_ESCR:
                    checkBox = (CheckBox) findViewById(R.id.checkListEscImovel);
                    break;
                case CAPTURE_IMAGE_CONT_COMP_VEND:
                    checkBox = (CheckBox) findViewById(R.id.checkListContImovel);
                    break;
                case CAPTURE_IMAGE_IPTU:
                    checkBox = (CheckBox) findViewById(R.id.checkListIPTUImovel);
                    break;
                case CAPTURE_IMAGE_CCIR:
                    checkBox = (CheckBox) findViewById(R.id.checkListCCIRImovel);
                    break;
                case CAPTURE_IMAGE_ITR:
                    checkBox = (CheckBox) findViewById(R.id.checkListITRImovel);
                    break;
            }
            checkBox.setChecked(true);
        }

    }

    public void saveCadastro(){
        cadastro.set_horario_saida(DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE).format(new Date()));
        if(tabSelected==1)
            saveCadastroRelatorio();
        if(tabSelected==2)
            saveCadastroProp();
        if(tabSelected==3)
            saveCadastroConj();
        if(tabSelected==4)
            saveCadastroEndRes();
        if(tabSelected==5)
            saveCadastroEndObj();
        if(tabSelected==6)
            saveCadastroIdProp();
        if(tabSelected==9)
            saveCadastroDesc();
        Intent intent = new Intent(CadastroActivity.this, InitialActivity.class);

        Log.d("HorusGeo", "save: " + String.valueOf(docs.size()));

        if(editTable){
            db.updateRegister(cadastro, benfs, plants, docs, idBck);
            intent.putExtra("tipo", "ok");
            intent.putExtra("texto", "Cadastro realizado com sucesso!");
        }else {
            db.addRegister(cadastro);
            db.addProp(cadastro);
            db.addConj(cadastro);
            db.addEndRes(cadastro);
            db.addEndObj(cadastro);
            db.addIdProp(cadastro);
            db.addDesc(cadastro);
            db.addBenf(benfs);
            db.addPlant(plants);
            db.addDoc(docs);
            intent.putExtra("tipo", "ok");
            intent.putExtra("texto", "Cadastro atualizado com sucesso!");
        }

        startActivity(intent);

    }

    public void saveCadastroToBenfsPlants(){
        if(db.checkIfPropExists(cadastro.get_id_prop())) {
            db.updateRegister(cadastro, benfs, plants, docs, idBck);
        }else{
            db.addRegister(cadastro);
            db.addProp(cadastro);
            db.addConj(cadastro);
            db.addEndRes(cadastro);
            db.addEndObj(cadastro);
            db.addIdProp(cadastro);
            db.addDesc(cadastro);
            db.addBenf(benfs);
            db.addPlant(plants);
            db.addDoc(docs);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode <= CAPTURE_IMAGE_CPF_CONJ)
                callAddThumb(requestCode, fileUri.getPath());

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Foto cancelada!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Alguma coisa deu errado!", Toast.LENGTH_LONG).show();
        }
        String[] name = fileUri.getPath().split("/");
        docs.add(new Docs(fileUri.getPath(), String.valueOf(requestCode), name[name.length-1], cadastro.get_id_prop()));
    }

    public static void takePhoto(Activity activity, int type){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = imgPhoto.getOutputMediaFileUri(imgPhoto.MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        // start the image capture Intent
        activity.startActivityForResult(intent, type);


    }

    public void callAddThumb(int type, final String file){
        ImageButton img = new ImageButton(this);
        img.setImageBitmap(imgPhoto.decodeSampledBitmapFromFile(file, 100, 100));

        LinearLayout layout = null;

        switch (type){
            case CAPTURE_IMAGE_ID_PROP:
                layout = (LinearLayout) findViewById(R.id.idPropLayout);
                break;
            case CAPTURE_IMAGE_CPF_PROP:
                layout = (LinearLayout) findViewById(R.id.cpfPropLayout);
                break;
            case CAPTURE_IMAGE_ID_CONJ:
                layout = (LinearLayout) findViewById(R.id.idConjLayout);
                break;
            case CAPTURE_IMAGE_CPF_CONJ:
                layout = (LinearLayout) findViewById(R.id.cpfConjLayout);
                break;

        }
        layout.addView(img);

    }


}
