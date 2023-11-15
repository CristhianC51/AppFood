package com.cristhiancaballero.appfood.Modelos;

public class Producto {

    private String nombre_producto, precio, foto, descripcion, uid, check;

    public Producto(String nombre_producto, String precio, String foto, String descripcion, String uid, String check) {
        this.nombre_producto = nombre_producto;
        this.precio = precio;
        this.foto = foto;
        this.descripcion = descripcion;
        this.uid = uid;
        this.check = check;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
