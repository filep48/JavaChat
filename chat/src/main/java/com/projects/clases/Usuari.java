package com.projects.clases;

public class Usuari {
    private int id;
    private String nombreUsuario;
    private String contrasena;
    private int grupoId;

    //Contructor InicioSession  
    public Usuari(int id, String nombreUsuario, String contrasena) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    //Contructor completo
    public Usuari(int id, String nombreUsuario, String contrasena, int grupoId) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.grupoId = grupoId;
    }

    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public int getGrupoId() {
        return grupoId;
    }
    public void setGrupoId(int grupoId) {
        this.grupoId = grupoId;
    }
    
}
