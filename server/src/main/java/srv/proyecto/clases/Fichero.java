package srv.proyecto.clases;

import java.sql.Date;

public class Fichero extends Usuario{
    
        private int id;
        private String nombre;
        private String nombreUnico;
        private String ruta;
        private String tipo;
        private Date fecha;
        private int usuarioIdFichero;
        private int grupoIdFichero;
    
        public Fichero() {
    
        }
    
        public Fichero(int id, String nombre, String nombreUnico, String ruta, String tipo, Date fecha, int usuarioId, int grupoId) {
            this.id = id;
            this.nombre = nombre;
            this.nombreUnico = nombreUnico;
            this.ruta = ruta;
            this.tipo = tipo;
            this.fecha = fecha;
            this.usuarioIdFichero = usuarioId;
            this.grupoIdFichero = grupoId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getNombreUnico() {
            return nombreUnico;
        }

        public void setNombreUnico(String nombreUnico) {
            this.nombreUnico = nombreUnico;
        }

        public String getRuta() {
            return ruta;
        }

        public void setRuta(String ruta) {
            this.ruta = ruta;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public Date getFecha() {
            return fecha;
        }

        public void setFecha(Date fecha) {
            this.fecha = fecha;
        }

        public int getUsuarioIdFichero() {
            return usuarioIdFichero;
        }

        public void setUsuarioIdFichero(int usuarioId) {
            this.usuarioIdFichero = usuarioId;
        }

        public int getGrupoIdFichero() {
            return grupoIdFichero;
        }

        public void setGrupoIdFichero(int grupoId) {
            this.grupoIdFichero = grupoId;
        }
    

    
}
