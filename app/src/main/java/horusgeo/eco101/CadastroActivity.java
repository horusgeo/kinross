package horusgeo.eco101;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



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

    String idBck;

    DBHandler db;

    boolean editTable;

    int tabSelected;

//    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
//    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    private static final int CAPTURE_IMAGE_ID_PROP = 100;
    private static final int CAPTURE_IMAGE_CPF_PROP = 200;
    private static final int CAPTURE_IMAGE_ID_CONJ = 300;
    private static final int CAPTURE_IMAGE_CPF_CONJ = 400;

    private static Uri fileUri;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


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

        db = new DBHandler(this, null, null, 1);

        Intent intent = getIntent();

        String tipo = intent.getStringExtra("tipo");
        String text1 = intent.getStringExtra("string");

        if(tipo.equals("new")){
            setTitle(text1);
            editTable = false;
        }else if(tipo.equals("edit")){
            cadastro = db.getRegister(text1);
            idBck = cadastro.get_id_prop();
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
                                loadCadastroBenf();
                                break;
                            case 8:
                                loadCadastroPlant();
                                break;
                            case 9:
                                loadCadastroDesc();
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
                        }
                    }
                }
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCadastro();
            }
        });



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
                    break;
                case 9:
                    rootView = inflater.inflate(R.layout.layout_plant, container, false);
                    break;
                case 10:
                    rootView = inflater.inflate(R.layout.layout_descricao, container, false);
                    break;
                case 11:
                    rootView = inflater.inflate(R.layout.layout_map, container, false);
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
            return 10;
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

        cadastro.set_nome_projeto(nomeProjeto.getText().toString());
        cadastro.set_id_prop(idProp.getText().toString());
        cadastro.set_local_visita(localVisita.getText().toString());
        cadastro.set_data_visita(dataVisita.getText().toString());

    }

    void loadCadastroRelatorio(){
        TextView nomeProjeto = (TextView) findViewById(R.id.projetoText);
        TextView idProp = (TextView) findViewById(R.id.idPropText);
        TextView localVisita = (TextView) findViewById(R.id.localText);
        TextView dataVisita = (TextView) findViewById(R.id.dataText);

        nomeProjeto.setText(cadastro.get_nome_projeto());
        idProp.setText(cadastro.get_id_prop());
        localVisita.setText(cadastro.get_local_visita());
        dataVisita.setText(cadastro.get_data_visita());

    }

    void saveCadastroProp(){

        TextView nome = (TextView) findViewById(R.id.nomeText);
        TextView nacio = (TextView) findViewById(R.id.nacionalidadeText);
        TextView prof = (TextView) findViewById(R.id.profissaoText);
        TextView doc = (TextView) findViewById(R.id.identidadeText);
        TextView doctp = (TextView) findViewById(R.id.idTipoText);
        RadioGroup est = (RadioGroup) findViewById(R.id.estadoCivilGroup);
        //TextView docph = (TextView) mView.findViewById(R.id.id);
        TextView cpf = (TextView) findViewById(R.id.cpfText);
        //TextView cpfph = (TextView) mView.findViewById(R.id.dataText);
        TextView tel1 = (TextView) findViewById(R.id.propTel1);
        TextView tel2 = (TextView) findViewById(R.id.propTel2);
        TextView email = (TextView) findViewById(R.id.emailText);
        TextView anot = (TextView) findViewById(R.id.anotacoesText);


        cadastro.set_nome_proprietario(nome.getText().toString());
        cadastro.set_nacionalidade_prop(nacio.getText().toString());
        cadastro.set_profissao_prop(prof.getText().toString());
        cadastro.set_doc_id_prop(doc.getText().toString());
        cadastro.set_doc_id_tipo_prop(doctp.getText().toString());
        cadastro.set_estado_civil(String.valueOf(est.getCheckedRadioButtonId()));
        cadastro.set_cpf_prop(cpf.getText().toString());
        cadastro.set_tel_prop_1(tel1.getText().toString());
        cadastro.set_tel_prop_2(tel2.getText().toString());
        cadastro.set_email_prop(email.getText().toString());
        cadastro.set_anotacoes_prop(anot.getText().toString());

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
        TextView anot = (TextView) findViewById(R.id.anotacoesText);

        nome.setText(cadastro.get_nome_proprietario());
        nacio.setText(cadastro.get_nacionalidade_prop());
        prof.setText(cadastro.get_profissao_prop());
        doc.setText(cadastro.get_doc_id_prop());
        doctp.setText(cadastro.get_doc_id_tipo_prop());
        if(cadastro.get_estado_civil()!= null)
            est.check(Integer.valueOf(cadastro.get_estado_civil()));
        cpf.setText(cadastro.get_cpf_prop());
        tel1.setText(cadastro.get_tel_prop_1());
        tel2.setText(cadastro.get_tel_prop_2());
        email.setText(cadastro.get_email_prop());
        anot.setText(cadastro.get_anotacoes_prop());
        if(cadastro.getDocs()!=null) {
            for (Docs temp : cadastro.getDocs()) {
                if (temp.getType().equals(String.valueOf(CAPTURE_IMAGE_ID_PROP))) {
                    callAddThumb(CAPTURE_IMAGE_ID_PROP, temp.getPath());
                } else if (temp.getType().equals(String.valueOf(CAPTURE_IMAGE_CPF_PROP))) {
                    callAddThumb(CAPTURE_IMAGE_CPF_PROP, temp.getPath());
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
        TextView anot = (TextView) findViewById(R.id.conjAnotText);


        cadastro.set_nome_conj(nome.getText().toString());
        cadastro.set_nacionalidade_conj(nacio.getText().toString());
        cadastro.set_profissao_conj(prof.getText().toString());
        cadastro.set_doc_id_conj(doc.getText().toString());
        cadastro.set_doc_id_tipo_conj(doctp.getText().toString());
        cadastro.set_cpf_conj(cpf.getText().toString());
        cadastro.set_tel_conj_1(tel1.getText().toString());
        cadastro.set_tel_conj_2(tel2.getText().toString());
        cadastro.set_anotacoes_conj(anot.getText().toString());

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
        TextView anot = (TextView) findViewById(R.id.conjAnotText);

        nome.setText(cadastro.get_nome_conj());
        nacio.setText(cadastro.get_nacionalidade_conj());
        prof.setText(cadastro.get_profissao_conj());
        doc.setText(cadastro.get_doc_id_conj());
        doctp.setText(cadastro.get_doc_id_tipo_conj());
        cpf.setText(cadastro.get_cpf_conj());
        tel1.setText(cadastro.get_tel_conj_1());
        tel2.setText(cadastro.get_tel_conj_2());
        anot.setText(cadastro.get_anotacoes_conj());

        if(cadastro.getDocs()!=null) {
            for (Docs temp : cadastro.getDocs()) {
                if (temp.getType().equals(String.valueOf(CAPTURE_IMAGE_ID_CONJ))) {
                    callAddThumb(CAPTURE_IMAGE_ID_CONJ, temp.getPath());
                } else if (temp.getType().equals(String.valueOf(CAPTURE_IMAGE_CPF_CONJ))) {
                    callAddThumb(CAPTURE_IMAGE_CPF_CONJ, temp.getPath());
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

        cadastro.set_zoneamento(String.valueOf(zon.getCheckedRadioButtonId()));
        cadastro.set_topografia(String.valueOf(topo.getCheckedRadioButtonId()));
        cadastro.set_manacial(String.valueOf(man.getCheckedRadioButtonId()));
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

        if(cadastro.get_zoneamento() != null)
            zon.check(Integer.parseInt(cadastro.get_zoneamento()));
        if(cadastro.get_topografia() != null)
            topo.check(Integer.parseInt(cadastro.get_topografia()));
        if(cadastro.get_manacial() != null)
            man.check(Integer.parseInt(cadastro.get_manacial()));
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


    public void saveCadastro(){
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

        if(editTable){
            db.updateRegister(cadastro, idBck);
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
            db.addDoc(cadastro);
            intent.putExtra("tipo", "ok");
            intent.putExtra("texto", "Cadastro atualizado com sucesso!");
        }

        startActivity(intent);

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
        cadastro.getDocs().add(new Docs(fileUri.getPath(), String.valueOf(resultCode)));
    }

    public static void takePhoto(Activity activity, int type){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        // start the image capture Intent
        activity.startActivityForResult(intent, type);


    }

    public void callAddThumb(int type, String file){
        ImageButton img = new ImageButton(this);
        img.setImageBitmap(decodeSampledBitmapFromFile(file, 100, 100));
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

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String file, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Eco101");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        Log.d("Eco101",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() );
        Log.d("Eco101", mediaStorageDir.getPath() );
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Eco101", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


}
