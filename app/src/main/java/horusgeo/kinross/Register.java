package horusgeo.eco101;

import java.util.ArrayList;

/**
 * Created by rafael on 24/05/2016.
 */



class Docs{

    private String path;
    private String type;
    private String name;
    private String idProp;

    Docs(){

    }

    Docs(String path, String type, String name, String idProp){
        this.path = path;
        this.type = type;
        this.name = name;
        this.idProp = idProp;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdProp() {
        return idProp;
    }

    public void setIdProp(String idProp) {
        this.idProp = idProp;
    }
}

class Benfeitoria{

    private String tipo;
    private String idade;
    private String conservacao;
    private String idBenf;
    private String idProp;


    Benfeitoria(){
    }

    Benfeitoria(String tipo, String idade, String conservacao, String idBenf, String idProp){
        this.tipo = tipo;
        this.idade = idade;
        this.conservacao = conservacao;
        this.idBenf = idBenf;
        this.idProp = idProp;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getConservacao() {
        return conservacao;
    }

    public void setConservacao(String conservacao) {
        this.conservacao = conservacao;
    }

    public String getIdBenf() {
        return idBenf;
    }

    public void setIdBenf(String idBenf) {
        this.idBenf = idBenf;
    }

    public String getIdProp() {
        return idProp;
    }

    public void setIdProp(String idProp) {
        this.idProp = idProp;
    }
}

public class Register {

    private int _id;
    private int registerStatus;

    private String _nome_projeto;
    private String _id_prop;
    private String _local_visita;
    private String _data_visita;
    private String _status;

    private String _nome_proprietario;
    private String _nacionalidade_prop;
    private String _profissao_prop;
    private String _estado_civil;
    private String _doc_id_prop;
    private String _doc_id_tipo_prop;
    private String _cpf_prop;
    private String _tel_prop_1;
    private String _tel_prop_2;
    private String _email_prop;
    private String _possProp;
    private String _anotacoes_prop;

    private String _nome_conj;
    private String _sobrenome_conj;
    private String _nacionalidade_conj;
    private String _profissao_conj;
    private String _doc_id_conj;
    private String _doc_id_tipo_conj;
    private String _cpf_conj;
    private String _tel_conj_1;
    private String _tel_conj_2;
    private String _anotacoes_conj;

    private String _rua_end_res;
    private String _n_end_res;
    private String _compl_end_res;
    private String _bairro_end_res;
    private String _cep_end_res;
    private String _municipio_end_res;
    private String _uf_mun_end_res;
    private String _comarca_end_res;
    private String _uf_com_end_res;
    private String _p_ref_end_res;

    private String _rua_end_obj;
    private String _n_end_obj;
    private String _compl_end_obj;
    private String _bairro_end_obj;
    private String _cep_end_obj;
    private String _p_ref_end_obj;

    private String _zoneamento;
    private String _topografia;
    private String _infraredeel;
    private String _infrasintel;
    private String _infraabasagua;
    private String _manacial;
    private String _obs_id_prop;

    private String _horario_chegada;
    private String _horario_saida;
    private String _descricao_visita;
    private String _responsavel;

    private ArrayList<Double> lat = new ArrayList<Double>();
    private ArrayList<Double> lng = new ArrayList<Double>();
    private String tipoLatLng;

    private String observacao;

    public Register() {

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

    public String get_nome_projeto() {
        return _nome_projeto;
    }

    public void set_nome_projeto(String _nome_projeto) {
        this._nome_projeto = _nome_projeto;
    }

    public String get_id_prop() {
        return _id_prop;
    }

    public void set_id_prop(String _id_prop) {
        this._id_prop = _id_prop;
    }

    public String get_local_visita() {
        return _local_visita;
    }

    public void set_local_visita(String _local_visita) {
        this._local_visita = _local_visita;
    }

    public String get_data_visita() {
        return _data_visita;
    }

    public void set_data_visita(String _data_visita) {
        this._data_visita = _data_visita;
    }

    public String get_profissao_prop() {
        return _profissao_prop;
    }

    public void set_profissao_prop(String _profissao_prop) {
        this._profissao_prop = _profissao_prop;
    }

    public String get_nacionalidade_prop() {
        return _nacionalidade_prop;
    }

    public void set_nacionalidade_prop(String _nacionalidade_prop) {
        this._nacionalidade_prop = _nacionalidade_prop;
    }

    public String get_estado_civil() {
        return _estado_civil;
    }

    public void set_estado_civil(String _estado_civil) {
        this._estado_civil = _estado_civil;
    }

    public String get_doc_id_prop() {
        return _doc_id_prop;
    }

    public void set_doc_id_prop(String _doc_id_prop) {
        this._doc_id_prop = _doc_id_prop;
    }

    public String get_doc_id_tipo_prop() {
        return _doc_id_tipo_prop;
    }

    public void set_doc_id_tipo_prop(String _doc_id_tipo_prop) {
        this._doc_id_tipo_prop = _doc_id_tipo_prop;
    }

    public String get_cpf_prop() {
        return _cpf_prop;
    }

    public void set_cpf_prop(String _cpf_prop) {
        this._cpf_prop = _cpf_prop;
    }

    public String get_tel_prop_1() {
        return _tel_prop_1;
    }

    public void set_tel_prop_1(String _tel_prop_1) {
        this._tel_prop_1 = _tel_prop_1;
    }

    public String get_tel_prop_2() {
        return _tel_prop_2;
    }

    public void set_tel_prop_2(String _tel_prop_2) {
        this._tel_prop_2 = _tel_prop_2;
    }

    public String get_email_prop() {
        return _email_prop;
    }

    public void set_email_prop(String _email_prop) {
        this._email_prop = _email_prop;
    }

    public String get_anotacoes_prop() {
        return _anotacoes_prop;
    }

    public void set_anotacoes_prop(String _anotacoes_prop) {
        this._anotacoes_prop = _anotacoes_prop;
    }

    public String get_nome_conj() {
        return _nome_conj;
    }

    public void set_nome_conj(String _nome_conj) {
        this._nome_conj = _nome_conj;
    }

    public String get_sobrenome_conj() {
        return _sobrenome_conj;
    }

    public void set_sobrenome_conj(String _sobrenome_conj) {
        this._sobrenome_conj = _sobrenome_conj;
    }

    public String get_nacionalidade_conj() {
        return _nacionalidade_conj;
    }

    public void set_nacionalidade_conj(String _nacionalidade_conj) {
        this._nacionalidade_conj = _nacionalidade_conj;
    }

    public String get_profissao_conj() {
        return _profissao_conj;
    }

    public void set_profissao_conj(String _profissao_conj) {
        this._profissao_conj = _profissao_conj;
    }

    public String get_doc_id_conj() {
        return _doc_id_conj;
    }

    public void set_doc_id_conj(String _doc_id_conj) {
        this._doc_id_conj = _doc_id_conj;
    }


    public String get_doc_id_tipo_conj() {
        return _doc_id_tipo_conj;
    }

    public void set_doc_id_tipo_conj(String _doc_id_tipo_conj) {
        this._doc_id_tipo_conj = _doc_id_tipo_conj;
    }

    public String get_cpf_conj() {
        return _cpf_conj;
    }

    public void set_cpf_conj(String _cpf_conj) {
        this._cpf_conj = _cpf_conj;
    }

    public String get_tel_conj_1() {
        return _tel_conj_1;
    }

    public void set_tel_conj_1(String _tel_conj_1) {
        this._tel_conj_1 = _tel_conj_1;
    }

    public String get_tel_conj_2() {
        return _tel_conj_2;
    }

    public void set_tel_conj_2(String _tel_conj_2) {
        this._tel_conj_2 = _tel_conj_2;
    }

    public String get_anotacoes_conj() {
        return _anotacoes_conj;
    }

    public void set_anotacoes_conj(String _anotacoes_conj) {
        this._anotacoes_conj = _anotacoes_conj;
    }

    public String get_rua_end_res() {
        return _rua_end_res;
    }

    public void set_rua_end_res(String _rua_end_res) {
        this._rua_end_res = _rua_end_res;
    }

    public String get_n_end_res() {
        return _n_end_res;
    }

    public void set_n_end_res(String _n_end_res) {
        this._n_end_res = _n_end_res;
    }

    public String get_compl_end_res() {
        return _compl_end_res;
    }

    public void set_compl_end_res(String _compl_end_res) {
        this._compl_end_res = _compl_end_res;
    }

    public String get_bairro_end_res() {
        return _bairro_end_res;
    }

    public void set_bairro_end_res(String _bairro_end_res) {
        this._bairro_end_res = _bairro_end_res;
    }

    public String get_cep_end_res() {
        return _cep_end_res;
    }

    public void set_cep_end_res(String _cep_end_res) {
        this._cep_end_res = _cep_end_res;
    }

    public String get_municipio_end_res() {
        return _municipio_end_res;
    }

    public void set_municipio_end_res(String _municipio_end_res) {
        this._municipio_end_res = _municipio_end_res;
    }

    public String get_uf_mun_end_res() {
        return _uf_mun_end_res;
    }

    public void set_uf_mun_end_res(String _uf_mun_end_res) {
        this._uf_mun_end_res = _uf_mun_end_res;
    }

    public String get_comarca_end_res() {
        return _comarca_end_res;
    }

    public void set_comarca_end_res(String _comarca_end_res) {
        this._comarca_end_res = _comarca_end_res;
    }

    public String get_uf_com_end_res() {
        return _uf_com_end_res;
    }

    public void set_uf_com_end_res(String _uf_com_end_res) {
        this._uf_com_end_res = _uf_com_end_res;
    }

    public String get_p_ref_end_res() {
        return _p_ref_end_res;
    }

    public void set_p_ref_end_res(String _p_ref_end_res) {
        this._p_ref_end_res = _p_ref_end_res;
    }

    public String get_rua_end_obj() {
        return _rua_end_obj;
    }

    public void set_rua_end_obj(String _rua_end_obj) {
        this._rua_end_obj = _rua_end_obj;
    }

    public String get_n_end_obj() {
        return _n_end_obj;
    }

    public void set_n_end_obj(String _n_end_obj) {
        this._n_end_obj = _n_end_obj;
    }

    public String get_compl_end_obj() {
        return _compl_end_obj;
    }

    public void set_compl_end_obj(String _compl_end_obj) {
        this._compl_end_obj = _compl_end_obj;
    }

    public String get_bairro_end_obj() {
        return _bairro_end_obj;
    }

    public void set_bairro_end_obj(String _bairro_end_obj) {
        this._bairro_end_obj = _bairro_end_obj;
    }

    public String get_cep_end_obj() {
        return _cep_end_obj;
    }

    public void set_cep_end_obj(String _cep_end_obj) {
        this._cep_end_obj = _cep_end_obj;
    }

    public String get_zoneamento() {
        return _zoneamento;
    }

    public void set_zoneamento(String _zoneamento) {
        this._zoneamento = _zoneamento;
    }

    public String get_topografia() {
        return _topografia;
    }

    public void set_topografia(String _topografia) {
        this._topografia = _topografia;
    }

    public String get_manacial() {
        return _manacial;
    }

    public void set_manacial(String _manacial) {
        this._manacial = _manacial;
    }

    public String get_obs_id_prop() {
        return _obs_id_prop;
    }

    public void set_obs_id_prop(String _obs_id_prop) {
        this._obs_id_prop = _obs_id_prop;
    }

    public String get_horario_chegada() {
        return _horario_chegada;
    }

    public void set_horario_chegada(String _horario_chegada) {
        this._horario_chegada = _horario_chegada;
    }

    public String get_horario_saida() {
        return _horario_saida;
    }

    public void set_horario_saida(String _horario_saida) {
        this._horario_saida = _horario_saida;
    }

    public String get_descricao_visita() {
        return _descricao_visita;
    }

    public void set_descricao_visita(String _descricao_visita) {
        this._descricao_visita = _descricao_visita;
    }

    public String get_responsavel() {
        return _responsavel;
    }

    public void set_responsavel(String _responsavel) {
        this._responsavel = _responsavel;
    }

    public String get_p_ref_end_obj() {
        return _p_ref_end_obj;
    }

    public void set_p_ref_end_obj(String _p_ref_end_obj) {
        this._p_ref_end_obj = _p_ref_end_obj;
    }

    public String get_infraredeel() {
        return _infraredeel;
    }

    public void set_infraredeel(String _infraredeel) {
        this._infraredeel = _infraredeel;
    }

    public String get_infrasintel() {
        return _infrasintel;
    }

    public void set_infrasintel(String _infrasintel) {
        this._infrasintel = _infrasintel;
    }

    public String get_infraabasagua() {
        return _infraabasagua;
    }

    public void set_infraabasagua(String _infraabasagua) {
        this._infraabasagua = _infraabasagua;
    }

    public ArrayList<Double> getLat() {
        return lat;
    }

    public void setLat(ArrayList<Double> lat) {
        this.lat = lat;
    }

    public ArrayList<Double> getLng() {
        return lng;
    }

    public void setLng(ArrayList<Double> lng) {
        this.lng = lng;
    }

    public String getTipoLatLng() {
        return tipoLatLng;
    }

    public void setTipoLatLng(String tipoLatLng) {
        this.tipoLatLng = tipoLatLng;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public String get_possProp() {
        return _possProp;
    }

    public void set_possProp(String _possProp) {
        this._possProp = _possProp;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(int registerStatus) {
        this.registerStatus = registerStatus;
    }
}



