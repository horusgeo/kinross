function Prop(poly, id, tipo){
    this.poly = poly;
    this.id = id;
    this.tipo = tipo;
}

Prop.prototype.getPoly = function() {
    return this.poly;
};

Prop.prototype.getId = function() {
    return this.id;
};

Prop.prototype.getTipo = function() {
    return this.tipo;
};
