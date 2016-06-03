package horusgeo.eco101;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 24/05/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eco101DB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_REGISTERS = "registers";
    private static final String TABLE_PROP = "proprietarios";
    private static final String TABLE_CONJ = "conjuges";
    private static final String TABLE_END_RES = "end_res";
    private static final String TABLE_END_OBJ = "end_obj";
    private static final String TABLE_ID_PROP = "id_prop";
    private static final String TABLE_BENF = "benfeitorias";
    private static final String TABLE_PLANT = "plantacoes";
    private static final String TABLE_DESC = "descricao";


    private static final String ID = "_id";

    private static final String NOME_PROJETO = "nome_projeto";
    private static final String ID_PROPRIETARIO = "id_proprietario";
    private static final String LOCAL_VISITA = "local_visita";
    private static final String DATA_VISITA = "data_visita";

    private static final String ID_PROP = "_id_prop";
    private static final String NOME_PROPRIETARIO = "nome_proprietario";
    private static final String NACIONALIDADE_PROPRIETARIO = "nacionalidade_proprietario";
    private static final String PROFISSAO_PROPRIETARIO = "profissao_proprietario";
    private static final String ESTADO_CIVIL = "estado_civil";
    private static final String DOC_ID_PROPRIETARIO = "doc_id_proprietario";
    private static final String DOC_ID_PHOTO_PROPRIETARIO = "doc_id_photo_proprietario";
    private static final String DOC_ID_TIPO_PROPRIETARIO = "doc_id_tipo_proprietario";
    private static final String CPF_PROPRIETARIO = "cpf_proprietario";
    private static final String CPF_PHOTO_PROPRIETARIO = "cpf_photo_proprietario";
    private static final String TEL_PROPRIETARIO_1 = "tel_proprietario_1";
    private static final String TEL_PROPRIETARIO_2 = "tel_proprietario_2";
    private static final String EMAIL_PROPRIETARIO = "email_proprietario";
    private static final String ANOTACOES_PROPRIETARIO = "anotacoes_proprietario";
    private static final String ARQUIVOS_PROPRIETARIO = "arquivos_proprietario";

    private static final String ID_CONJ = "_id_conj";
    private static final String NOME_CONJ = "nome_conj";
    private static final String SOBRENOME_CONJ = "sobrenome_conj";
    private static final String NACIONALIDADE_CONJ = "nacionalidade_conj";
    private static final String PROFISSAO_CONJ = "profissao_conj";
    private static final String DOC_ID_CONJ = "doc_id_conj";
    private static final String DOC_ID_PHOTO_CONJ = "doc_id_photo_conj";
    private static final String DOC_ID_TIPO_CONJ = "doc_id_tipo_conj";
    private static final String CPF_CONJ = "cpf_conj";
    private static final String CPF_PHOTO_CONJ = "cpf_photo_conj";
    private static final String TEL_CONJ_1 = "tel_conj_1";
    private static final String TEL_CONJ_2 = "tel_conj_2";
    private static final String ANOTACOES_CONJ = "anotacoes_conj";
    private static final String ARQUIVOS_CONJ = "arquivos_conj";

    private static final String ID_END_RES = "_id_end_res";
    private static final String RUA_END_RES = "rua_end_res";
    private static final String N_END_RES = "n_end_res";
    private static final String COMPL_END_RES = "compl_end_res";
    private static final String BAIRRO_END_RES = "bairro_end_res";
    private static final String CEP_END_RES = "cep_end_res";
    private static final String MUNICIPIO_END_RES = "municipio_end_res";
    private static final String UF_MUN_END_RES = "uf_mun_end_res";
    private static final String COMARCA_END_RES = "comarca_end_res";
    private static final String UF_COM_END_RES = "uf_com_end_res";
    private static final String P_REF_END_RES = "p_ref_end_res";
    private static final String ARQUIVOS_END_RES = "arquivos_end_res";

    private static final String ID_END_OBJ = "_id_end_obj";
    private static final String RUA_END_OBJ = "rua_end_obj";
    private static final String N_END_OBJ = "n_end_obj";
    private static final String COMPL_END_OBJ = "compl_end_obj";
    private static final String BAIRRO_END_OBJ = "bairro_end_obj";
    private static final String CEP_END_OBJ = "cep_end_obj";
    private static final String P_REF_END_OBJ = "p_ref_end_obj";
    private static final String ARQUIVOS_END_OBJ = "arquivos_end_obj";

    private static final String ID_ID_PROP = "_id_id_prop";
    private static final String ZONEAMENTO = "zoneamento";
    private static final String TOPOGRAFIA = "topografia";
    private static final String INFRAESTRUTURA = "infraestrutura";
    private static final String MANANCIAL = "manancial";
    private static final String OBS_ID_PROP = "obs_id_prop";
    private static final String ARQUIVOS_ID_PROP = "arquivos_id_prop";

    private static final String ID_BENF = "_id_benf";
    private static final String TIPO_BENF = "tipo_benf";
    private static final String IDADE_BENF = "idade_benf";
    private static final String CONSERVACAO_BENF = "conservacao_benf";
    private static final String PHOTO_BENF = "photo_benf";
    private static final String OBS_BENF = "obs_benf";
    private static final String ARQUIVOS_BENF = "arquivos_benf";

    private static final String ID_PLANT = "_id_plant";
    private static final String TIPO_PLANT = "tipo_plant";
    private static final String IDADE_PLANT = "idade_plant";
    private static final String COMPLEMENTO_PLANT = "conservacao_plant";
    private static final String PHOTO_PLANT = "photo_plant";
    private static final String OBS_PLANT = "obs_plant";
    private static final String ARQUIVOS_PLANT = "arquivos_plant";

    private static final String ID_DESC = "_id_desc";
    private static final String HORARIO_CHEGADA = "horario_chegada";
    private static final String HORARIO_SAIDA = "horario_saida";
    private static final String DESCRICAO_VISITA = "descricao_visita";
    private static final String RESPONSAVEL = "responsavel";

    private static final String LATLNG = "latlng";

    public DBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("SQL", "onCreate");
        String CREATE_REGISTER_TABLE = "CREATE TABLE " + TABLE_REGISTERS + "(" +
                ID + " INTEGER PRIMARY KEY," +
                NOME_PROJETO + " TEXT," +
                ID_PROPRIETARIO + " TEXT," +
                LOCAL_VISITA + " TEXT," +
                DATA_VISITA + " TEXT" +
                ")";
        db.execSQL(CREATE_REGISTER_TABLE);

        String CREATE_PROP_TABLE = "CREATE TABLE " + TABLE_PROP + "(" +
                ID + " INTEGER PRIMARY KEY," +
                NOME_PROPRIETARIO + " TEXT," +
                NACIONALIDADE_PROPRIETARIO + " TEXT," +
                PROFISSAO_PROPRIETARIO + " TEXT," +
                ESTADO_CIVIL + " TEXT," +
                DOC_ID_PROPRIETARIO + " TEXT," +
                DOC_ID_PHOTO_PROPRIETARIO + " TEXT," +
                DOC_ID_TIPO_PROPRIETARIO + " TEXT," +
                CPF_PROPRIETARIO + " TEXT," +
                CPF_PHOTO_PROPRIETARIO + " TEXT," +
                TEL_PROPRIETARIO_1 + " TEXT," +
                TEL_PROPRIETARIO_2 + " TEXT," +
                EMAIL_PROPRIETARIO + " TEXT," +
                ANOTACOES_PROPRIETARIO + " TEXT," +
                ID_PROP + " TEXT" +
                ")";


        db.execSQL(CREATE_PROP_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROP);
        onCreate(db);
    }

    //Add Registers

    public void addRegister(Register cadastro) {

        ContentValues values = new ContentValues();

        values.put(NOME_PROJETO, cadastro.get_nome_projeto());
        values.put(ID_PROPRIETARIO, cadastro.get_id_prop());
        values.put(LOCAL_VISITA, cadastro.get_local_visita());
        values.put(DATA_VISITA, cadastro.get_data_visita());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_REGISTERS, null, values);
        db.close();
    }

    public void addProp(Register cadastro) {

        ContentValues values = new ContentValues();

        values.put(NOME_PROPRIETARIO, cadastro.get_nome_proprietario());
        values.put(NACIONALIDADE_PROPRIETARIO, cadastro.get_nacionalidade_prop());
        values.put(PROFISSAO_PROPRIETARIO, cadastro.get_profissao_prop());
        values.put(ESTADO_CIVIL, cadastro.get_estado_civil());
        values.put(DOC_ID_PROPRIETARIO, cadastro.get_doc_id_prop());
        values.put(DOC_ID_PHOTO_PROPRIETARIO, cadastro.get_doc_id_photo_prop());
        values.put(DOC_ID_TIPO_PROPRIETARIO, cadastro.get_doc_id_tipo_prop());
        values.put(CPF_PROPRIETARIO, cadastro.get_cpf_prop());
        values.put(CPF_PHOTO_PROPRIETARIO, cadastro.get_cpf_photo_prop());
        values.put(TEL_PROPRIETARIO_1, cadastro.get_tel_prop_1());
        values.put(TEL_PROPRIETARIO_2, cadastro.get_tel_prop_2());
        values.put(EMAIL_PROPRIETARIO, cadastro.get_email_prop());
        values.put(ANOTACOES_PROPRIETARIO, cadastro.get_anotacoes_prop());
        values.put(ID_PROP, cadastro.get_id_prop());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PROP, null, values);
        db.close();
    }

    //Update Registers

    public void updateRegister(Register cadastro, String idBck){

        ContentValues values = new ContentValues();

        values.put(NOME_PROJETO, cadastro.get_nome_projeto());
        values.put(ID_PROPRIETARIO, cadastro.get_id_prop());
        values.put(LOCAL_VISITA, cadastro.get_local_visita());
        values.put(DATA_VISITA, cadastro.get_data_visita());

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_REGISTERS, values, ID + "=" + cadastro.get_id(), null);
        db.close();

        updateRegisterProp(cadastro, idBck);

    }

    public void updateRegisterProp(Register cadastro, String idBck){

        ContentValues values = new ContentValues();

        values.put(NOME_PROPRIETARIO, cadastro.get_nome_proprietario());
        values.put(NACIONALIDADE_PROPRIETARIO, cadastro.get_nacionalidade_prop());
        values.put(PROFISSAO_PROPRIETARIO, cadastro.get_profissao_prop());
        values.put(ESTADO_CIVIL, cadastro.get_estado_civil());
        values.put(DOC_ID_PROPRIETARIO, cadastro.get_doc_id_prop());
        values.put(DOC_ID_PHOTO_PROPRIETARIO, cadastro.get_doc_id_photo_prop());
        values.put(DOC_ID_TIPO_PROPRIETARIO, cadastro.get_doc_id_tipo_prop());
        values.put(CPF_PROPRIETARIO, cadastro.get_cpf_prop());
        values.put(CPF_PHOTO_PROPRIETARIO, cadastro.get_cpf_photo_prop());
        values.put(TEL_PROPRIETARIO_1, cadastro.get_tel_prop_1());
        values.put(TEL_PROPRIETARIO_2, cadastro.get_tel_prop_2());
        values.put(EMAIL_PROPRIETARIO, cadastro.get_email_prop());
        values.put(ANOTACOES_PROPRIETARIO, cadastro.get_anotacoes_prop());
        values.put(ID_PROP, cadastro.get_id_prop());

        SQLiteDatabase db = this.getWritableDatabase();

        if(cadastro.get_id_prop().equals(idBck)){
            db.update(TABLE_PROP, values, ID_PROP + "=" + cadastro.get_id_prop(), null);
        }else{
            db.update(TABLE_PROP, values, ID_PROP + "=" + idBck, null);
        }

        db.close();
    }


    //Get Registers

    public List<Register> getAllIdNameRegisters(){
        List<Register> registerList = new ArrayList<Register>();
        String selectQuery = "SELECT * FROM " + TABLE_REGISTERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Register register = new Register();
                register.set_id_prop(cursor.getString(2));

                String query = "SELECT " + NOME_PROPRIETARIO +" FROM " + TABLE_PROP + " WHERE " + register.get_id_prop() + " = " + ID_PROP;
                Cursor cursorProp = db.rawQuery(query, null);
                cursorProp.moveToFirst();
                register.set_nome_proprietario(cursorProp.getString(0));
                registerList.add(register);
            }while(cursor.moveToNext());
        }
        db.close();
        return registerList;
    }

    public Register getRegister(String idProp){
        Register register = new Register();

        String query = "SELECT * FROM " + TABLE_REGISTERS + " WHERE " + ID_PROPRIETARIO + " = " + idProp;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            register.set_id(Integer.parseInt(cursor.getString(0)));
            register.set_nome_projeto(cursor.getString(1));
            register.set_id_prop(cursor.getString(2));
            register.set_local_visita(cursor.getString(3));
            register.set_data_visita(cursor.getString(4));
            cursor.close();
        }

        query = "SELECT * FROM " + TABLE_PROP + " WHERE " + ID_PROP + " = " + idProp;

        cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            register.set_nome_proprietario(cursor.getString(1));
            register.set_nacionalidade_prop(cursor.getString(2));
            register.set_profissao_prop(cursor.getString(3));
            register.set_estado_civil(cursor.getString(4));
            register.set_doc_id_prop(cursor.getString(5));
            register.set_doc_id_tipo_prop(cursor.getString(6));
            register.set_cpf_prop(cursor.getString(7));
            register.set_tel_prop_1(cursor.getString(8));
            register.set_tel_prop_2(cursor.getString(9));
            register.set_email_prop(cursor.getString(10));
            register.set_anotacoes_prop(cursor.getString(11));
            cursor.close();
        }
        db.close();

        return register;
    }

    //Delete Registers

    public void deleteRegister(String idProp) {

        boolean result = false;

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_REGISTERS, ID_PROPRIETARIO + " = " + idProp, null);
        db.delete(TABLE_PROP, ID_PROP + " = " + idProp, null);

        db.close();

    }


    //Useful Functions

    public boolean isRegisterEmpty(){
        String countQuery = "SELECT  * FROM " + TABLE_REGISTERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if(count == 0)
            return true;
        else
            return false;
    }

    public int getRegistersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REGISTERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        db.close();
        return count;
    }

}
