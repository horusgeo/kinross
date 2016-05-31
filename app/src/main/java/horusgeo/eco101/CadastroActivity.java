package horusgeo.eco101;

import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    DBHandler db;

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
        String text1 = intent.getStringExtra("text1");

        if(tipo.equals("new")){
            setTitle(text1);
        }else if(tipo.equals("edit")){

        }


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
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

                        switch(tab.getPosition()){
                            case 0:
                                loadCadastroRelatorio();
                                break;
                            case 1:
                                loadCadastroProp();
                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab){
                        super.onTabUnselected(tab);

                        switch(tab.getPosition()){
                            case 0:
                                saveCadastroRelatorio();
                                break;
                            case 1:
                                saveCadastroProp();
                                break;
//                            case 2:
//                                saveCadastroConj();
//                                break;
//                            case 3:
//                                saveCadastroEndRes();
//                                break;
//                            case 4:
//                                saveCadastroEndObj();
//                                break;
//                            case 5:
//                                saveCadastroIdProp();
//                                break;
//                            case 6:
//                                saveCadastroBenf();
//                                break;
//                            case 7:
//                                saveCadastroPlant();
//                                break;
//                            case 8:
//                                saveCadastroDesc();
//                                break;
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
            View rootView;

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.layout_relatorio, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.layout_dados_prop, container, false);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.layout_dados_conj, container, false);
                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.layout_end_residencial, container, false);
                    break;
                case 5:
                    rootView = inflater.inflate(R.layout.layout_end_obj, container, false);
                    break;
                case 6:
                    rootView = inflater.inflate(R.layout.layout_id_prop, container, false);
                    break;
                case 7:
                    rootView = inflater.inflate(R.layout.layout_benf, container, false);
                    break;
                case 8:
                    rootView = inflater.inflate(R.layout.layout_plant, container, false);
                    break;
                case 9:
                    rootView = inflater.inflate(R.layout.layout_descricao, container, false);
                    break;
                case 10:
                    rootView = inflater.inflate(R.layout.layout_map, container, false);
                    break;
                default:
                    rootView = inflater.inflate(R.layout.layout_relatorio, container, false);

            }

//            rootView = inflater.inflate(R.layout.fragment_cadastro, container, false);


            return rootView;
        }


//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_cadastro, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
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
                    return "Relatório";
                case 1:
                    return "Dados Proprietário";
                case 2:
                    return "Dados Cônjuge";
                case 3:
                    return "Endereço Residencial";
                case 4:
                    return "Endereço Objeto";
                case 5:
                    return "Identificação da Propriedade";
                case 6:
                    return "Benfeitoria";
                case 7:
                    return "Plantações";
                case 8:
                    return "Descrição da Visita";
                case 9:
                    return "Mapa";
            }
            return null;
        }
    }

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
        //TextView docph = (TextView) mView.findViewById(R.id.id);
        TextView cpf = (TextView) findViewById(R.id.cpfText);
        //TextView cpfph = (TextView) mView.findViewById(R.id.dataText);
        TextView tel1 = (TextView) findViewById(R.id.propTel1);
        TextView tel2 = (TextView) findViewById(R.id.propTel2);
        TextView email = (TextView) findViewById(R.id.emailText);
        TextView anot = (TextView) findViewById(R.id.anotacoesText);

        nome.setText(cadastro.get_nome_proprietario());
        nacio.setText(cadastro.get_nacionalidade_prop());
        prof.setText(cadastro.get_profissao_prop());
        doc.setText(cadastro.get_doc_id_prop());
        doctp.setText(cadastro.get_doc_id_tipo_prop());
        cpf.setText(cadastro.get_cpf_prop());
        tel1.setText(cadastro.get_tel_prop_1());
        tel2.setText(cadastro.get_tel_prop_2());
        email.setText(cadastro.get_email_prop());
        anot.setText(cadastro.get_anotacoes_prop());
    }


    public void saveCadastro(){

        db.addRegister(cadastro);
        db.addProp(cadastro);

        Intent intent = new Intent(CadastroActivity.this, InitialActivity.class);
        intent.putExtra("texto", "1 - Cadastro realizado com sucesso!");
        startActivity(intent);

    }



}
