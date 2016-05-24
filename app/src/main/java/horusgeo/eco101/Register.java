package horusgeo.eco101;

/**
 * Created by rafael on 24/05/2016.
 */
public class Register {

    private int _id;
    private String _nome_proprietario;
    private String _latlng;

    public Register(){

    }

    public Register(int id, String nome_proprietario, String latlng){

        this._id = id;
        this._nome_proprietario = nome_proprietario;
        this._latlng = latlng;

    }

    public Register(String nome_proprietario, String latlng){

        this._nome_proprietario = nome_proprietario;
        this._latlng = latlng;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public String get_nome_proprietario() {
        return _nome_proprietario;
    }

    public void set_nome_proprietario(String nome_proprietario) {
        this._nome_proprietario = nome_proprietario;
    }


    public String get_latlng() {
        return _latlng;
    }

    public void set_latlng(String latlng) {
        this._latlng = latlng;
    }
}
