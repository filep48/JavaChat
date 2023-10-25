package srv.proyecto.functions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.google.protobuf.Timestamp;

import srv.proyecto.clases.Usuario;
import srv.proyecto.functions.FuncionesServer.ContrasenaInvalidaException;

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
    public static boolean splitDatosUsuario(DataOutputStream writer, DataInputStream reader, String input)
            throws IOException {
        try {
            String[] parts = FuncionesServer.slplitMensaje(input);

            String commando = parts[0];
            String nombreUsuario = parts[1];
            String contrasena = parts[2];
            if ("iniciarSesion".equals(commando) || "registrarse".equals(commando)) {

                boolean inicioSesionExitoso = registroBBDD(nombreUsuario, contrasena, commando);

                if (inicioSesionExitoso) {
                    writer.writeBoolean(true);
                    FuncionesServer.conectarUsuario(nombreUsuario, contrasena);
                    return true;

                } else {
                    writer.writeBoolean(false);
                    return false;
                }
            } else {
                System.out.println("Mensaje de inicio de sesión incorrecto.");

            }
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al comunicarse con el cliente: " + e.getMessage());
        }
        return false;
    }

    /**
     * Obtiene una lista de usuarios creados en la base de datos, excluyendo al
     * usuario actual.
     *
     * @param usuario El objeto Usuario actual del cliente.
     * @return Una cadena que contiene la lista de usuarios creados.
     *         En caso de error, se retorna un mensaje de error.
     */
    public static String listarUsuariosCreados(Usuario usuario) {
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
                if (!rs.getString("nombre_usuario").equals(usuario.getNombreUsuario())) {
                    resultado += rs.getString("nombre_usuario") + "\n";
                }
            }
            return resultado;
        } catch (SQLException ex) {
            return "Error: " + ex.toString();
        }
    }

    /**
     * Obtiene una lista de grupos creados a los que el usuario actual pertenece.
     *
     * @param usuario El objeto Usuario actual del cliente.
     * @return Una cadena que contiene la lista de nombres de grupos a los que el
     *         usuario pertenece.
     *         En caso de error, se retorna un mensaje de error.
     */
    public static String listarGruposCreados(Usuario usuario) {
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

    /**
     * Registra un usuario en la base de datos o verifica las credenciales del
     * usuario.
     *
     * @param nombre     El nombre de usuario que se va a registrar o verificar.
     * @param contrasena La contraseña asociada al nombre de usuario.
     * @param comando    El comando que indica la operación a realizar.
     *                   - "registrarse": para el proceso de registro.
     *                   - "iniciarSesion": para verificar las credenciales y
     *                   realizar el inicio de sesión.
     * @return `true` si el proceso seleccionado (registro/inicio de sesión) es
     *         exitoso,
     *         `false` en caso contrario.
     * @throws SQLException             En caso de errores relacionados con la base
     *                                  de datos.
     * @throws IllegalArgumentException En caso de argumentos inválidos.
     * @throws Exception                Para otros errores no específicos que puedan
     *                                  surgir.
     */

    public static boolean registroBBDD(String nombre, String contrasena, String comando) {
        try {
            // Si el comando es iniciarSesion, validamos la contraseña (funcion
            // validarContrasena)
            if ("registrarse".equals(comando)) {
                boolean contrasenaValida = false;
                try {
                    contrasenaValida = FuncionesServer.validarContrasena(contrasena);
                } catch (ContrasenaInvalidaException e) {
                    System.err.println("Error al validar la contraseña: " + e.getMessage());
                    return false;
                }
                if (!contrasenaValida) {
                    return false;
                }
            }

            Connection cn = DatabaseConnection.getConnection();
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
            }
        } catch (SQLException e) {
            System.err.println("Error relacionado con la base de datos: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("Argumento inválido: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error desconocido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crea un grupo en la base de datos y agrega al usuario como creador del grupo.
     *
     * @param usuario El usuario que se añadirá como creador del grupo.
     * @param mensaje Un array con mensajes del cliente. Se espera que el nombre del
     *                grupo esté en mensaje[1].
     * @return `true` si el grupo se creó exitosamente y el usuario se agregó como
     *         creador, o `false` en caso de error.
     * 
     * @exception SQLException Si hay un error relacionado con la operación en la
     *                         base
     *                         de datos.
     */
    public static boolean creacionGruposBBDD(Usuario usuario, String[] mensaje) {
        String nombreGrupo = mensaje[1];
        PreparedStatement pst = null;
        try {
            Connection cn = DatabaseConnection.getConnection();
            pst = cn.prepareStatement("INSERT INTO grupos (nombre_grupo) VALUES (?)");
            pst.setString(1, nombreGrupo);
            pst.executeUpdate();

            meterCreadorAlGrupo(cn, usuario, mensaje);
            return true;
        } catch (SQLException e) {
            System.out.println("Error al crear el grupo y agregar al creador en la base de datos: " + e.getMessage());
            return false;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el pst " + e.getMessage());
                }
            }
        }
    }

    /**
     * Agrega al usuario especificado como administrador de un grupo en la base de
     * datos.
     * Se basa en el nombre del grupo proporcionado en el mensaje para determinar a
     * qué grupo agregar al usuario como administrador.
     *
     * @param cn      La conexión activa a la base de datos.
     * @param usuario El usuario que se añadirá como administrador del grupo.
     * @param mensaje Un array con mensajes del cliente. Se espera que el nombre del
     *                grupo esté en mensaje[1].
     * 
     * @exception SQLException Si hay un error relacionado con la operación en la
     *                         base
     *                         de datos.
     */
    public static void meterCreadorAlGrupo(Connection cn, Usuario usuario, String[] mensaje) {
        try {
            int idGrupo = obtenerIdGrupo(mensaje[1]);
            if (idGrupo != -1) {
                String strSql = "INSERT INTO miembrosgrupos (usuario_id, grupo_id, rol) VALUES (?, ?, 'admin')";

                try (PreparedStatement pst = cn.prepareStatement(strSql)) {
                    pst.setInt(1, usuario.getId());
                    pst.setInt(2, idGrupo);
                    pst.executeUpdate();
                } // PreparedStatement se cierra automáticamente aquí

            } else {
                System.out.println("No se pudo obtener el ID del grupo para: " + mensaje[1]);
            }
        } catch (SQLException e) {
            System.out.println(
                    "Error al agregar al usuario como administrador del grupo en la base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Obtiene el ID de un usuario a partir de su nombre de usuario.
     *
     * @param nombreUsuario El nombre de usuario del cual se desea obtener el ID.
     * @return El ID del usuario si se encuentra en la base de datos, o -1 si el
     *         usuario no se encuentra.
     */
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

    /**
     * Elimina al usuario de los grupos a los que pertenece en la base de datos.
     *
     * @param cn        La conexión a la base de datos.
     * @param idUsuario El ID del usuario que se eliminará de los grupos.
     */
    public static void deleteMiembrosGrupos(Connection cn, int idUsuario) {
        String deleteMiembrosGrupos = "DELETE FROM MiembrosGrupos WHERE usuario_id = ?";
        try (PreparedStatement pst = cn.prepareStatement(deleteMiembrosGrupos)) {
            pst.setInt(1, idUsuario);
            pst.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al eliminar ");
        }
    }

    /**
     * Elimina los mensajes del usuario de la base de datos.
     *
     * @param cn        La conexión a la base de datos.
     * @param idUsuario El ID del usuario cuyos mensajes se eliminarán.
     */
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

    /**
     * Elimina los archivos del usuario de la base de datos.
     *
     * @param cn        La conexión a la base de datos.
     * @param idUsuario El ID del usuario cuyos archivos se eliminarán.
     */
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

    /**
     * Elimina el registro de un usuario de la base de datos.
     *
     * @param cn        La conexión a la base de datos.
     * @param idUsuario El ID del usuario cuyo registro se eliminará.
     */
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

    /**
     * Realiza el proceso de dar de baja a un usuario, eliminando sus registros y
     * pertenencia a grupos.
     *
     * @param cn        La conexión a la base de datos.
     * @param usuarioId El ID del usuario que se dará de baja.
     * @return `true` si el proceso se realiza con éxito, `false` en caso de error.
     */
    public static boolean darseDeBajaUsuario(Usuario usuario) {
        try {
            Connection cn = DatabaseConnection.getConnection();
            deleteMiembrosGrupos(cn, usuario.getId());
            deleteMensajes(cn, usuario.getId());
            deleteArchivos(cn, usuario.getId());
            deleteUsuario(cn, usuario.getId());
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar ");
            return false;
        }
    }

    /**
     * Elimina un grupo de la base de datos si el usuario que realiza la solicitud
     * es administrador de ese grupo.
     *
     * @param usuario     El usuario que realiza la solicitud y cuyo rol se
     *                    verificará.
     * @param nombreGrupo El nombre del grupo que se eliminará.
     * @param reader      El flujo de entrada para recibir datos adicionales si es
     *                    necesario.
     * @return Un mensaje que indica si el grupo se eliminó con éxito o si se
     *         produjo un error.
     */

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

    /**
     * Obtiene el identificador (ID) de un grupo en la base de datos a partir de su
     * nombre.
     *
     * @param nombreGrupo El nombre del grupo del cual se desea obtener el ID.
     * @return El ID del grupo si se encuentra en la base de datos, o -1 si el grupo
     *         no existe.
     */

    public static int obtenerIdGrupo(String nombreGrupo) {
        int idGrupo = -1; // es el return si no existe el grupo

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

    /**
     * Verifica si un usuario es administrador (admin) de un grupo específico en la
     * base de datos.
     *
     * @param usuario     El usuario del cual se desea verificar el rol de
     *                    administrador.
     * @param nombreGrupo El nombre del grupo en el cual se verifica el rol de
     *                    administrador.
     * @return `true` si el usuario es administrador del grupo, `false` en caso
     *         contrario o si ocurre un error.
     */
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

    /**
     * Añade un usuario a un grupo específico en la base de datos si el usuario
     * actual es administrador del grupo.
     *
     * @param usuario       El usuario que realiza la acción de añadir al nuevo
     *                      usuario.
     * @param usuarioAnadir El nombre del usuario que se añadirá al grupo.
     * @param nombreGrupo   El nombre del grupo al cual se añadirá el usuario.
     * @param reader        El flujo de entrada de datos, utilizado para lectura
     *                      adicional si es necesario.
     * @return Un mensaje que indica si la operación se realizó con éxito o si se
     *         produjo un error.
     */
    public static String anadirUsuarioAGrupo(Usuario usuario, String usuarioAnadir, String nombreGrupo,
            DataInputStream reader) {
        // Llama a la función isAdmin
        if (isAdmin(usuario, nombreGrupo)) {
            try {
                Connection cn = DatabaseConnection.getConnection();
                int idUsuarioAnadido = FuncionesSQL.obtenerIdUsuario(usuarioAnadir);
                int idGrupo = FuncionesSQL.obtenerIdGrupo(nombreGrupo);
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

    /**
     * Obtiene una lista de usuarios pertenecientes a un grupo específico en la base
     * de datos.
     *
     * @param nombreGrupo El nombre del grupo del cual se desea obtener la lista de
     *                    usuarios.
     * @return Una cadena de texto que contiene los nombres de usuario de los
     *         miembros del grupo.
     *         En caso de error, se devuelve un mensaje de error.
     */

    public static String listarUsuariosPorGrupo(String string) {
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

    /**
     * Obtiene una lista de miembros de un grupo específico en la base de datos.
     *
     * @param usuario     El objeto de usuario que realiza la solicitud.
     * @param nombreGrupo El nombre del grupo del cual se desea obtener la lista de
     *                    miembros.
     * @param reader      El flujo de entrada para recibir datos del cliente.
     * @return Una cadena de texto que contiene los nombres de usuario de los
     *         miembros del grupo.
     *         Cada miembro se muestra en una línea separada, y el usuario
     *         solicitante se identifica como "(tú)".
     *         En caso de error, se devuelve un mensaje de error.
     */
    public static String listarMiembrosGrupo(Usuario usuario, String nombreGrupo, DataInputStream reader) {
        int idGrupo = FuncionesSQL.obtenerIdGrupo(nombreGrupo);
        String listaMiembros = "";

        try {
            Connection cn = DatabaseConnection.getConnection();
            String strSql = "SELECT nombre_usuario " +
                    "FROM Usuarios " +
                    "WHERE id IN (SELECT usuario_id FROM MiembrosGrupos WHERE grupo_id = ?)";

            try (PreparedStatement pst = cn.prepareStatement(strSql)) {
                pst.setInt(1, idGrupo);

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        if (rs.getString("nombre_usuario").equals(usuario.getNombreUsuario()))
                            listaMiembros += rs.getString("nombre_usuario") + " (tú)" + "\n";
                        else
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

    /**
     * Elimina a un miembro de un grupo específico si el usuario que realiza la
     * solicitud
     * es administrador del grupo.
     *
     * @param usuario        El objeto de usuario que realiza la solicitud.
     * @param usuarioBorrado El nombre de usuario que se desea eliminar del grupo.
     * @param nombreGrupo    El nombre del grupo del cual se eliminará al miembro.
     * @param reader         El flujo de entrada para recibir datos del cliente.
     * @return Un mensaje que indica si el usuario se eliminó con éxito o si ocurrió
     *         un error.
     *         En caso de error, se proporciona información sobre el problema.
     */
    public static String eliminarMiebro(Usuario usuario, String usuarioBorrado, String nombreGrupo,
            DataInputStream reader) {
        if (isAdmin(usuario, nombreGrupo)) {
            try {
                Connection cn = DatabaseConnection.getConnection();
                int idUsuarioBorrado = FuncionesSQL.obtenerIdUsuario(usuarioBorrado);
                int idGrupo = FuncionesSQL.obtenerIdGrupo(nombreGrupo);

                String borrarMiembrosGrupos = "DELETE FROM miembrosGrupos WHERE usuario_id = ? AND grupo_id = ?";
                PreparedStatement pst = cn.prepareStatement(borrarMiembrosGrupos);
                pst.setInt(1, idUsuarioBorrado);
                pst.setInt(2, idGrupo);
                pst.executeUpdate();
                return "Usuario eliminado con éxito.";
            } catch (Exception e) {
                return "Error al eliminar el usuario: " + e.getMessage();
            }
        } else {
            return "El usuario no es administrador del grupo.";
        }
    }

    /**
     * Envía un mensaje al chat especificado y lo registra en la base de datos.
     *
     * @param usuario     El usuario que envía el mensaje.
     * @param nombreGrupo El nombre del grupo de chat al cual se envía el mensaje.
     * @param mensajeChat El contenido del mensaje a enviar.
     * @param reader      El flujo de datos de entrada para recibir información.
     *
     *                    <p>
     *                    Este método crea una conexión a la base de datos, obtiene
     *                    el ID del grupo
     *                    de chat basado en su nombre y luego inserta el mensaje en
     *                    la tabla de mensajes
     *                    con el usuario, grupo, contenido del mensaje y la fecha y
     *                    hora actuales.
     *                    </p>
     *
     *                    <p>
     *                    Si ocurre algún error al enviar el mensaje o interactuar
     *                    con la base de datos,
     *                    se imprimirá un mensaje de error. Finalmente, se cierra el
     *                    PreparedStatement.
     *                    </p>
     */
    public static boolean enviarMensaje(Usuario usuario, String nombreGrupo, String mensajeChat,
            DataInputStream reader) {
        PreparedStatement pst = null;
        boolean enviado = false;
        try {
            Connection cn = DatabaseConnection.getConnection();
            int idGrupo = FuncionesSQL.obtenerIdGrupo(nombreGrupo);
            // creación de una variable que guarda el dia, hora, minuto y segundo actual
            java.sql.Timestamp timeStamp = convertirProtobufASqlTimestamp();
            String insertMensaje = "INSERT INTO mensajes (usuario_id, grupo_id, contenido, fecha_envio) VALUES (?, ?, ?, ?)";
            pst = cn.prepareStatement(insertMensaje);
            pst.setInt(1, usuario.getId());
            pst.setInt(2, idGrupo);
            pst.setString(3, mensajeChat);
            pst.setTimestamp(4, timeStamp);
            pst.executeUpdate();
            enviado = true;
            return enviado;
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje: " + e.getMessage());
            return enviado;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Convierte un Timestamp de Protocol Buffers a un java.sql.Timestamp.
     * 
     * @return java.sql.Timestamp correspondiente.
     */
    public static java.sql.Timestamp convertirProtobufASqlTimestamp() {
        // Crear el Timestamp como anteriormente
        Timestamp marcaTemporalProtobuf = Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000).build();

        // Convertir el Timestamp de Protocol Buffers a java.sql.Timestamp
        long tiempoEnMilisegundos = marcaTemporalProtobuf.getSeconds() * 1000
                + marcaTemporalProtobuf.getNanos() / 1_000_000; // Convertir segundos a milisegundos y nanosegundos a
                                                                // milisegundos
        return new java.sql.Timestamp(tiempoEnMilisegundos);
    }

    /**
     * Recupera y lista los mensajes de un grupo específico desde la base de datos.
     * Esta función
     * consulta la base de datos para obtener todos los mensajes enviados dentro de
     * un grupo,
     * ordenados por fecha de envío en orden ascendente. Los mensajes se devuelven
     * en un formato
     * de cadena que muestra el nombre del usuario, el contenido del mensaje y la
     * fecha de envío.
     * 
     * @param nombreGrupo El nombre del grupo del cual se quieren listar los
     *                    mensajes.
     * @return Una cadena de texto con la lista de mensajes, o un mensaje de error
     *         en caso de
     *         que haya un problema con la consulta SQL.
     * 
     * @throws SQLException En caso de que haya un error al realizar la consulta a
     *                      la base de datos.
     */
    public static String listarMensajes(String nombreGrupo) {
        PreparedStatement pst = null;
        try {
            Connection cn = DatabaseConnection.getConnection();
            System.out.println("Listado de mensajes");
            System.out.println();
            String strSql = "SELECT u.nombre_usuario, m.contenido, m.fecha_envio " +
                    "FROM mensajes m " +
                    "JOIN usuarios u ON u.id = m.usuario_id " +
                    "JOIN grupos g ON g.id = m.grupo_id " +
                    "WHERE g.nombre_grupo = ? " +
                    "ORDER BY m.fecha_envio ASC";
            pst = cn.prepareStatement(strSql);
            pst.setString(1, nombreGrupo);

            // Resultados de la consulta
            ResultSet rs = pst.executeQuery();
            String resultado = "";
            while (rs.next()) {
                resultado += rs.getString("nombre_usuario") + ": " + rs.getString("contenido") + "\n ("
                        + rs.getString("fecha_envio") + ")\n\n";
            }
            return resultado;
        } catch (SQLException ex) {
            return "Error: " + ex.toString();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el pst " + e.getMessage());
                }
            }
        }
    }

    public static String salirGrupo(Usuario usuario, String string, DataInputStream reader) throws SQLException {
        Connection cn = DatabaseConnection.getConnection();
        String deleteMiembrosGrupos = "DELETE FROM miembrosGrupos WHERE usuario_id = ?";
        try (PreparedStatement pst = cn.prepareStatement(deleteMiembrosGrupos)) {
            pst.setInt(1, usuario.getId());
            pst.executeUpdate();
            return "Has sido eliminado con éxito.";
        } catch (Exception e) {
            System.err.println("Error al eliminar ");
            return "Error al eliminar el usuario: " + e.getMessage();
        }
        // Crea la logica para que la con la id del usuario se salga del usuario
        // indicado

    }
}
