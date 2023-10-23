package srv.proyecto.clases;

import java.util.ArrayList;
import java.util.List;
/**
 * Esta clase define el grupo en la aplicación de chat. 
 * 
 * @author Gerard Albesa, Kevin Felipe Vasquez, Vanessa Pedrola.
 * @version 1.0
 */
public class Grupo {
    private int idGrupo;
    private String nombreGrupo;
    private List<Usuario> usuarios = new ArrayList<Usuario>();

    public Grupo(int idGrupo, String nombreGrupo, List<Usuario> usuarios) {
        this.idGrupo = idGrupo;
        this.nombreGrupo = nombreGrupo;
        this.usuarios = usuarios;
    }

    public Grupo() {
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}
