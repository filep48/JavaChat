package srv.proyecto.clases;

public class Fichero {
    private String nombreFichero;
    private String rutaFichero;
    private String tipodeArchivo;
    private int idPropietario;
    private int idGrupoPropietario;

    public Fichero() {

    }

    public Fichero(String nombreFichero, String rutaFichero, String tipodeArchivo, int idPropietario,
            int idGrupoPropietario) {
        this.nombreFichero = nombreFichero;
        this.rutaFichero = rutaFichero;
        this.tipodeArchivo = tipodeArchivo;
        this.idPropietario = idPropietario;
        this.idGrupoPropietario = idGrupoPropietario;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    public String getRutaFichero() {
        return rutaFichero;
    }

    public void setRutaFichero(String rutaFichero) {
        this.rutaFichero = rutaFichero;
    }

    public String getTipodeArchivo() {
        return tipodeArchivo;
    }

    public void setTipodeArchivo(String tipodeArchivo) {
        this.tipodeArchivo = tipodeArchivo;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public int getIdGrupoPropietario() {
        return idGrupoPropietario;
    }

    public void setIdGrupoPropietario(int idGrupoPropietario) {
        this.idGrupoPropietario = idGrupoPropietario;
    }

}
