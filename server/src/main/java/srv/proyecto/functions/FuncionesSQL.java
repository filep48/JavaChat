package srv.proyecto.functions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import srv.proyecto.clases.DatabaseConnection;
import srv.proyecto.clases.Usuario;

public class FuncionesSQL {
    /**
     * Divide y procesa los datos de inicio de sesión.
     * Lee la cadena de entrada del cliente, la divide en partes y verifica si se
     * trata de una solicitud de inicio de sesión válida. Si es válida, llama a la
     * función `datosUsuario` para verificar las credenciales y envía una respuesta
     * al cliente.
     *
     * @param writer El flujo de salida para enviar una respuesta al cliente.
     * @param reader El flujo de entrada para recibir datos del cliente.
     */
    public static void splitDatosUsuario(DataOutputStream writer, DataInputStream reader, String input)
            throws IOException {
        try {
            String[] parts = FuncionesServer.slplitMensaje(input);

            String commando = parts[0];
            String nombreUsuario = parts[1];
            String contrasena = parts[2];
            if ("iniciarSesion".equals(commando) || "registrarse".equals(commando)) {

                boolean inicioSesionExitoso = RegistroBBDD(nombreUsuario, contrasena, commando);

                if (inicioSesionExitoso) {
                    writer.writeBoolean(true);
                    FuncionesServer.conectarUsuario(nombreUsuario, contrasena);

                } else {
                    writer.writeBoolean(false);
                }
            } else {
                System.out.println("Mensaje de inicio de sesión incorrecto.");
            }
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al comunicarse con el cliente: " + e.getMessage());
        }
    }

    public static String llistarUsuariosCreados() {
        try {
            Connection cn = DatabaseConnection.getConnection();
            System.out.println("Listado de usuarios creados");
            System.out.println();
            String strSql = "SELECT nombre_usuario FROM usuarios";
            PreparedStatement pst = cn.prepareStatement(strSql);

            // Resultados de la consulta
            ResultSet rs = pst.executeQuery();
            String resultado = "";
            while (rs.next()) {
                resultado += rs.getString("nombre_usuario") + "\n";
            }
            return resultado;
        } catch (SQLException ex) {
            return "Error: " + ex.toString();
        }
    }

    public static String llistarGruposCreados(Usuario usuario) {
        try {
            Connection cn = DatabaseConnection.getConnection();
            System.out.println("Listado de usuarios creados");
            System.out.println();
            String strSql = "SELECT g.id, g.nombre_grupo " +
                    "FROM grupos g " +
                    "JOIN miembrosgrupos mg ON g.id = mg.grupo_id " +
                    "WHERE mg.usuario_id = ?";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setInt(1, usuario.getId());

            // Resultados de la consulta
            ResultSet rs = pst.executeQuery();
            String resultado = "";
            while (rs.next()) {
                resultado += rs.getString("nombre_grupo") + "\n"; // Solo añadimos el nombre del grupo
            }
            return resultado;
        } catch (SQLException ex) {
            return "Error: " + ex.toString();
        }
    }

    // Este metodo que manda el mensaje de x cliente a la base de datos
    public static PreparedStatement EnviarMensajesBBDD(Connection cn, String mensaje) {
        try {
            String strSql = "insert into mensajes (contenido) values (?)";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setString(1, mensaje);
            pst.executeUpdate();
            return pst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Creacion del registro de usuarios en la base de datos
    public static boolean RegistroBBDD(String nombre, String contrasena, String comando) {
        try {
            // Si el comando es iniciarSesion, validamos la contraseña
            if ("registrarse".equals(comando)) {
                validarContrasena(contrasena);
            }

            try (Connection cn = DatabaseConnection.getConnection()) {
                String strSql;
                if ("registrarse".equals(comando)) {
                    // Si el comando es registrar, insertamos el usuario
                    strSql = "INSERT INTO usuarios (nombre_usuario, contrasena) VALUES (?, ?);";
                } else {
                    // Si el comando es otro (por ejemplo, iniciarSesion), verificamos las
                    // credenciales
                    strSql = "SELECT nombre_usuario FROM Usuarios WHERE nombre_usuario = ? AND contrasena = ?;";
                }

                try (PreparedStatement pst = cn.prepareStatement(strSql)) {
                    pst.setString(1, nombre);
                    pst.setString(2, contrasena);

                    if ("registrarse".equals(comando)) {
                        int affectedRows = pst.executeUpdate();
                        return affectedRows > 0;
                    } else {
                        try (ResultSet rs = pst.executeQuery()) {
                            return rs.next(); // Si hay resultados, las credenciales son válidas
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // *********************************************
    public static boolean creacionGruposBBDD(Usuario usuario, String[] mensaje, DataInputStream reader) {
        try {
            Connection cn = DatabaseConnection.getConnection();
            String strSql = "INSERT INTO grupos (nombre_grupo) VALUES (?)";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setString(1, mensaje[1]);
            pst.executeUpdate();
            meterCreadorAlGrupo(cn, usuario, mensaje, reader);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public static void meterCreadorAlGrupo(Connection cn, Usuario usuario, String[] mensaje, DataInputStream reader) {
        try {
            int idGrupo = obtenerIdGrupo(mensaje[1]);
            System.out.println("vamos a meter el admin antes del if");
            if(idGrupo != -1) {  // Asegurarse de que el ID del grupo es válido
                String strSql = "INSERT INTO miembrosgrupos (usuario_id, grupo_id, rol) VALUES (?, ?, 'admin')";
                System.out.println("despues del insert");
                PreparedStatement pst = cn.prepareStatement(strSql);
                pst.setInt(1, usuario.getId());
                pst.setInt(2, idGrupo);

                pst.executeUpdate();
                System.out.println("despues de todo funciona?????????????????????");
            } else {
                System.out.println("No se pudo obtener el ID del grupo para: " + mensaje[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    






    static void validarContrasena(String contrasena) throws ContrasenaInvalidaException {
        if (contrasena == null || !contrasena.matches("^.{6,32}$")) {
            throw new ContrasenaInvalidaException("La contraseña no cumple con los requisitos.");
        }
    }

    /**
     * Excepción personalizada para manejar contraseñas inválidas
     * lanza mensaje predefenido por nosotros en validarContrasena
     */
    public static class ContrasenaInvalidaException extends Exception {
        public ContrasenaInvalidaException(String mensaje) {
            super(mensaje);
        }
    }

    public static int obtenerIdUsuario(String nombreUsuario) {
        int idUsuario = -1; // Valor predeterminado en caso de que no se encuentre el usuario

        try {
            Connection cn = DatabaseConnection.getConnection();
            String strSql = "SELECT id FROM Usuarios WHERE nombre_usuario = ?";
            try (PreparedStatement pst = cn.prepareStatement(strSql)) {
                pst.setString(1, nombreUsuario);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        idUsuario = rs.getInt("id");
                    }
                }
            }
            return idUsuario;
        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return idUsuario;
    }

    // Eliminar al usuario de los grupos a los que pertenece
    public static void deleteMiembrosGrupos(Connection cn, int idUsuario) {
        String deleteMiembrosGrupos = "DELETE FROM MiembrosGrupos WHERE usuario_id = ?";
        try (PreparedStatement pst = cn.prepareStatement(deleteMiembrosGrupos)) {
            pst.setInt(1, idUsuario);
            pst.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al eliminar ");
        }
    }

    // Eliminar los mensajes del usuario
    public static void deleteMensajes(Connection cn, int idUsuario) {
        String deleteMensajes = "DELETE FROM Mensajes WHERE usuario_id = ?";
        try {
            PreparedStatement pst = cn.prepareStatement(deleteMensajes);
            pst.setInt(1, idUsuario);
            pst.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al eliminar ");
        }
    }

    // Eliminar los archivos del usuario
    public static void deleteArchivos(Connection cn, int idUsuario) {
        String deleteArchivos = "DELETE FROM Archivos WHERE usuario_id = ?";
        try {
            PreparedStatement pst = cn.prepareStatement(deleteArchivos);
            pst.setInt(1, idUsuario);
            pst.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al eliminar ");
        }
    }

    // Eliminar el registro del usuario
    public static void deleteUsuario(Connection cn, int idGrupo) {
        String deleteUsuario = "DELETE FROM Usuarios WHERE id = ?";
        try {
            PreparedStatement pst = cn.prepareStatement(deleteUsuario);
            pst.setInt(1, idGrupo);
            pst.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al eliminar ");
        }
    }

    public static boolean darseDeBajaUsuario(Connection cn, int usuarioId) {
        try {
            deleteMiembrosGrupos(cn, usuarioId);
            deleteMensajes(cn, usuarioId);
            deleteArchivos(cn, usuarioId);
            deleteUsuario(cn, usuarioId);
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar ");
            return false;
        }
    }

    // funcion que en la bbbd elimina un grupo si el que lo pide es admin del grupo

    public static String eliminarGrupo(Usuario usuario, String nombreGrupo, DataInputStream reader) {
        try {
            int idGrupo = FuncionesSQL.obtenerIdGrupo(nombreGrupo);
            Connection cn = DatabaseConnection.getConnection();
            String selectRol = "SELECT rol FROM MiembrosGrupos WHERE usuario_id = ? AND grupo_id = (SELECT id FROM Grupos WHERE id = ?)";
            PreparedStatement pst = cn.prepareStatement(selectRol);
            pst.setInt(1, usuario.getId());
            pst.setInt(2, idGrupo);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");

                // Comparamos si el rol es "admin"
                if ("admin".equals(rol)) {
                    // Eliminar
                    String deleteMiembrosGrupos = "DELETE FROM MiembrosGrupos WHERE grupo_id = (SELECT id FROM Grupos WHERE id = ?)";
                    pst = cn.prepareStatement(deleteMiembrosGrupos);
                    pst.setInt(1, idGrupo);
                    pst.executeUpdate();

                    String deleteMensajes = "DELETE FROM Mensajes WHERE grupo_id = (SELECT id FROM Grupos WHERE id = ?)";
                    pst = cn.prepareStatement(deleteMensajes);
                    pst.setInt(1, idGrupo);
                    pst.executeUpdate();

                    String deleteArchivos = "DELETE FROM Archivos WHERE grupo_id = (SELECT id FROM Grupos WHERE id = ?)";
                    pst = cn.prepareStatement(deleteArchivos);
                    pst.setInt(1, idGrupo);
                    pst.executeUpdate();

                    String deleteGrupo = "DELETE FROM Grupos WHERE id = ?";
                    pst = cn.prepareStatement(deleteGrupo);
                    pst.setInt(1, idGrupo);
                    pst.executeUpdate();

                    return "Grupo eliminado con éxito.";
                } else {
                    return "El usuario no es administrador del grupo.";
                }
            } else {
                return "El usuario no pertenece al grupo o el grupo no existe.";
            }
        } catch (Exception e) {
            return "Error al eliminar el grupo: " + e.getMessage();
        }
    }

    public static int obtenerIdGrupo(String nombreGrupo) {
        int idGrupo = -1; // Valor predeterminado en caso de que no se encuentre el usuario

        try {
            Connection cn = DatabaseConnection.getConnection();
            String strSql = "SELECT id FROM grupos WHERE nombre_grupo = ?";
            try (PreparedStatement pst = cn.prepareStatement(strSql)) {
                pst.setString(1, nombreGrupo);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        idGrupo = rs.getInt("id");
                    }
                }
            }
            return idGrupo;
        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return idGrupo;
    }

    public static boolean isAdmin(Usuario usuario, String nombreGrupo) {
        try {
            Connection cn = DatabaseConnection.getConnection();
            String selectRol = "SELECT rol FROM MiembrosGrupos WHERE usuario_id = ? AND grupo_id = (SELECT id FROM Grupos WHERE nombre_grupo = ?)";
            PreparedStatement pst = cn.prepareStatement(selectRol);
            pst.setInt(1, usuario.getId());
            pst.setString(2, nombreGrupo);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");

                // Comparamos si el rol es "admin"
                if ("admin".equals(rol)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static String anadirUsuarioAGrupo(Usuario usuario, String usuarioAnadir, String nombreGrupo,
            DataInputStream reader) {
        // Llama a la función isAdmin
        if (isAdmin(usuario, nombreGrupo)) {
            try {
                Connection cn = DatabaseConnection.getConnection();
                int idUsuarioAnadido = FuncionesSQL.obtenerIdUsuario(usuarioAnadir);
                int idGrupo = FuncionesSQL.obtenerIdGrupo(nombreGrupo);

                // Corregir la creación de PreparedStatement con la consulta SQL
                String insertMiembrosGrupos = "INSERT INTO miembrosgrupos (usuario_id, grupo_id, rol) VALUES (?, ?, 'miembro')";
                PreparedStatement pst = cn.prepareStatement(insertMiembrosGrupos);
                pst.setInt(1, idUsuarioAnadido);
                pst.setInt(2, idGrupo);
                pst.executeUpdate();
                return "Usuario añadido con éxito.";
            } catch (Exception e) {
                return "Error al añadir el usuario: " + e.getMessage();
            }
        } else {
            return "El usuario no es administrador del grupo.";
        }
    }

    public static String LlistarUsuariosPorGrupo(String string) {
        try {
            Connection cn = DatabaseConnection.getConnection();
            System.out.println("Listado de usuarios creados");
            System.out.println();
            String strSql = "SELECT u.nombre_usuario " +
                    "FROM usuarios u " +
                    "JOIN miembrosgrupos mg ON u.id = mg.usuario_id " +
                    "JOIN grupos g ON g.id = mg.grupo_id " +
                    "WHERE g.nombre_grupo = ?";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setString(1, string);

            // Resultados de la consulta
            ResultSet rs = pst.executeQuery();
            String resultado = "";
            while (rs.next()) {
                resultado += rs.getString("nombre_usuario") + "\n"; // Solo añadimos el nombre del grupo
            }
            return resultado;
        } catch (SQLException ex) {
            return "Error: " + ex.toString();
        }
    }

    public static String listarMiembrosGrupo(Usuario usuario, String nombreGrupo, DataInputStream reader) {
        int idGrupo = FuncionesSQL.obtenerIdGrupo(nombreGrupo); 
        String listaMiembros = "";
    
        try  {
            Connection cn = DatabaseConnection.getConnection();
            String strSql = "SELECT nombre_usuario " +
                        "FROM Usuarios " +
                        "WHERE id IN (SELECT usuario_id FROM MiembrosGrupos WHERE grupo_id = ?)";
    
            try (PreparedStatement pst = cn.prepareStatement(strSql)) {
                pst.setInt(1, idGrupo);
    
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        listaMiembros += rs.getString("nombre_usuario") + "\n";
                    }
                }
            }
    
            return listaMiembros;
    
        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());
        } catch (Exception e) {
        }
    
        return "No se pudieron listar los miembros del grupo.";
    }
    
    


















}