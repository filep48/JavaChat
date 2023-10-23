package srv.proyecto.clases;


/**
 * Esta clase define el usuario en la aplicación de chat. 
 * Contiene los atributos y métodos necesarios para el funcionamiento de la aplicación.
 * 
 * @author Gerard Albesa, Kevin Felipe Vasquez, Vanessa Pedrola.
 * @version 1.0
 */
public class Usuario {

    private int id;
    private String nombreUsuario;
    private String contrasena;
    private boolean isConectado;
    private int[] grupoId;

    // Contructor InicioSession
    public Usuario(String nombreUsuarioo, String contrasena) {
        this.nombreUsuario = nombreUsuarioo;
        this.contrasena = contrasena;
    }

    // Contructor completo
    public Usuario(int id, String nombreUsuarioo, boolean isConectado, int[] grupoId) {
        this.id = id;
        this.nombreUsuario = nombreUsuarioo;
        this.isConectado = isConectado;
        this.grupoId = grupoId;
    }

    public Usuario() {

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

    public void setNombreUsuario(String nombreUsuarioo) {
        this.nombreUsuario = nombreUsuarioo;
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

    public int[] getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(int[] grupoId) {
        this.grupoId = grupoId;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nombreUsuarioo=" + nombreUsuario + ", isConectado=" + isConectado
                + ", grupoId=" + grupoId + "]";
    }

}
