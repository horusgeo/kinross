package horusgeo.eco101;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 24/05/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "registersDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_REGISTERS = "properties";

    private static final String ID = "_id";

    private static final String NOME_PROPRIETARIO = "nome_proprietario";

    private static final String LATLNG = "latlng";


    public DBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REGISTER_TABLE =  "CREATE TABLE " + TABLE_REGISTERS + "("+
                ID + " INTEGER PRIMARY KEY,"+
                NOME_PROPRIETARIO + " TEXT,"+
                LATLNG + " TEXT" + ")";
        db.execSQL(CREATE_REGISTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTERS);
        onCreate(db);
    }

    public void addRegister(Register cadastro) {

        ContentValues values = new ContentValues();
        values.put(NOME_PROPRIETARIO, cadastro.get_nome_proprietario());
        values.put(LATLNG, cadastro.get_latlng());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_REGISTERS, null, values);
        db.close();
    }

    public Register findRegisterByName(String nomeProprietario) {
        String query = "Select * FROM " + TABLE_REGISTERS + " WHERE " + NOME_PROPRIETARIO + " =  \"" + nomeProprietario + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Register cadastro = new Register();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            cadastro.set_id(Integer.parseInt(cursor.getString(0)));
            cadastro.set_nome_proprietario(cursor.getString(1));
            cadastro.set_latlng(cursor.getString(2));
            cursor.close();
        } else {
            cadastro = null;
        }
        db.close();
        return cadastro;
    }

    public boolean deleteRegisterByName(String nomeProprietario) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_REGISTERS + " WHERE " + NOME_PROPRIETARIO + " =  \"" + nomeProprietario + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Register cadastro = new Register();

        if (cursor.moveToFirst()) {
            cadastro.set_id(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_REGISTERS, ID + " = ?",
                    new String[] { String.valueOf(cadastro.get_id()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public List<Register> getAllRegisters() {
        List<Register> registerList = new ArrayList<Register>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REGISTERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Register register = new Register(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2));
                registerList.add(register);
            } while (cursor.moveToNext());
        }
        db.close();
        // return contact list
        return registerList;
    }

    public int getRegistersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REGISTERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        db.close();
        return cursor.getCount();
    }


}
