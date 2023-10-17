package srv.proyecto.clases;

public class Usuario{
    
    public Usuario() {
    }
    private int id;
    private String nombreUsuarioo;
    private String contrasena;
    private boolean isConectado;
    private int grupoId;
    
    //Contructor InicioSession  
    public Usuario(String nombreUsuarioo, String contrasena) {
        this.nombreUsuarioo = nombreUsuarioo;
        this.contrasena = contrasena;
    }

    //Contructor completo
    public Usuario(int id, String nombreUsuarioo, boolean isConectado, int grupoId) {
        this.id = id;
        this.nombreUsuarioo = nombreUsuarioo;
        this.isConectado = isConectado;
        this.grupoId = grupoId;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombreUsuario() {
        return nombreUsuarioo;
    }
    public void setNombreUsuarioo(String nombreUsuarioo) {
        this.nombreUsuarioo = nombreUsuarioo;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public boolean isConectado() {
        return isConectado;
    }

    public void setConectado(boolean conectado) {
        this.isConectado = conectado;
    }
    public int getGrupoId() {
        return grupoId;
    }
    public void setGrupoId(int grupoId) {
        this.grupoId = grupoId;
    }
    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nombreUsuarioo=" + nombreUsuarioo + ", isConectado=" + isConectado
                + ", grupoId=" + grupoId + "]";
    }

    
}
