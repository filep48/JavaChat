package srv.proyecto.functions;

import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import srv.proyecto.clases.DatabaseConnection;
import srv.proyecto.clases.Usuario;

public class functionsSQL {
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
    public static void splitDatosUsuario(DataOutputStream writer, DataInputStream reader,String input) throws IOException {
        try {
            String[] parts = input.split(";");
            if (parts.length == 3 && parts[0].equals("iniciarSesion")) {
                String comando = parts[0];
                String nombreUsuario = parts[1];
                String contrasena = parts[2];

                boolean inicioSesionExitoso = datosUsuario(nombreUsuario, contrasena, comando);

                if (inicioSesionExitoso) {
                    writer.writeBoolean(true);
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

    public static String llistarUsuariosCreados(Connection cn) {
        try {
            System.out.println("Listado de usuarios creados");
            System.out.println();
            String strSql = "SELECT nombre_usuario, contrasena FROM usuarios";
            PreparedStatement pst = cn.prepareStatement(strSql);

            // Resultados de la consulta
            ResultSet rs = pst.executeQuery();
            String resultado = "";
            while (rs.next()) {
                resultado += rs.getString("nombre_usuario") + " " + rs.getString("contrasena") + "\n";
            }
            return resultado;
        } catch (SQLException ex) {
            return "Error: " + ex.toString();
        }
    }

    public static String llistarGruposCreados(Connection cn) {
        try {
            System.out.println("Listado de usuarios creados");
            System.out.println();
            String strSql = "SELECT id, nombre_grupo FROM grupos";
            PreparedStatement pst = cn.prepareStatement(strSql);

            // Resultados de la consulta
            ResultSet rs = pst.executeQuery();
            String resultado = "";
            while (rs.next()) {
                resultado += rs.getString("id") + " " + rs.getString("nombre_grupo") + "\n";
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

    /**
     * Realiza una consulta a la base de datos para verificar si existe un usuario
     * con el nombre de usuario y la contraseña proporcionados. 
     * Antes comprueba el largo de contrasena
     *
     * @param nombre     El nombre de usuario proporcionado.
     * @param contrasena La contraseña proporcionada.
     * @return `true` si las credenciales son válidas, `false` en caso contrario.
     */
    public static boolean datosUsuario(String nombre, String contrasena, String comando) {
        try {
            // Verificar si la contraseña cumple con los requisitos en caso de que el comando sea iniciarSesion
            
            if (!comando.equals("iniciarSesion")) {
            validarContrasena(contrasena); // Verificar si la contraseña cumple con los requisitos
            }
            try (Connection cn = DatabaseConnection.getConnection()) {
                String strSql = "SELECT nombre_usuario FROM Usuarios WHERE nombre_usuario = ? AND contrasena = ?";
                try (PreparedStatement pst = cn.prepareStatement(strSql)) {
                    pst.setString(1, nombre);
                    pst.setString(2, contrasena);
    
                    try (ResultSet rs = pst.executeQuery()) {
                        return rs.next(); // Si hay resultados, las credenciales son válidas
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ContrasenaInvalidaException e) {
            // Manejar la excepción de contraseña inválida aquí, si es necesario
            e.printStackTrace();
            return false;
        }
    }

    // *********************************************
    public static void creacionGruposBBDD(Connection cn) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Introduce el nombre del grupo: ");
            String nombreGrupo = scanner.nextLine();

            String strSql = "INSERT INTO grupos (nombre_grupo) VALUES (?)";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setString(1, nombreGrupo);
            pst.executeUpdate();

            System.out.println("Grupo creado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un grupo de la base de datos utilizando su ID.
     *
     * @param cn La conexión a la base de datos.
     */
    public static void EliminacionGruposBBDD(Connection cn) {
        try {
            // Solicitar al usuario el ID del grupo
            String idGrupo = JOptionPane.showInputDialog(null, "Introduce el ID del grupo que deseas eliminar:");

            String strSql = "delete from grupos where id = ?";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setString(1, idGrupo);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*--------------------------ELIMINAR ESTA FUNCION PQ LO HAGO EN DATOSUSUARIO---------------
     * ----------------------LA DEJO QUOTEADA POR SI DE CAS LA RECUPERAMOS LUEGO 
     * 
     * 
     * Verifica si un usuario existe en la base de datos.
     *
     * @param cn    La conexión a la base de datos.
     * @param datos Los datos del usuario a verificar.
     * @return true si el usuario existe en la bbdd, o false si no existe.
     * @throws SQLException Si ocurre un error al acceder a la bbdd.
     * 
     * public static boolean usuarioExiste(Connection cn, Usuario datos) throws SQLException {
        String strSql = "SELECT nombre_usuario FROM Usuarios WHERE nombre_usuario = ?";
        try (PreparedStatement pst = cn.prepareStatement(strSql)) {
            pst.setString(1, datos.getNombreUsuarioo());
    
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // Si hay resultados el usuario existe en la base de datos
            }
        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());
            return false;
        }
    }
    
    
    
    /**
     * --------------OTRA FUNCION PARA BORRAR PQ HAGO LO MISMO EN EL RUN de ClientHandler
     * 
     * Función para derivar el flujo de acciones segun si la funcion usuarioExiste
     * devuelve true o false
     * 
     * public static String login(String nombreUsuario, String contrasena, Connection cn) {
            if (datosUsuario(nombreUsuario, contrasena, cn)) {
                String respuesta = "Inicio de sesión exitoso. ¡Bienvenido!";
                // Llamar aquí a la función o método para mostrar el menú al usuario
                return respuesta;
            } else {
                String respuestaNegativa = "Usuario o contraseña incorrectos. ¿Quieres registrarte?";
                return respuestaNegativa;
                }
        }
    
     */

    /** función que da de alta a un usuario en la bbdd */
    public static void darAltaUsuario(Connection cn, Usuario usuarioDatos) {
        Console console = System.console();
        if (console != null) {
            char[] contrasenaChars = console.readPassword("Introduce tu contraseña: ");
            String contrasena = new String(contrasenaChars);
            try {
                String strSql = "INSERT INTO Usuarios (nombre_usuario, contrasena) VALUES (?, ?)";
                PreparedStatement pst = cn.prepareStatement(strSql);
                pst.setString(1, usuarioDatos.getNombreUsuarioo());
                pst.setString(2, contrasena);

                int filasAfectadas = pst.executeUpdate();

                if (filasAfectadas == 1) {
                    System.out.println("Usuario dado de alta con éxito.");
                } else {
                    System.out.println("Error al dar de alta al usuario.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FuncionesServer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error al dar de alta al usuario.");
            }
        } else {
            System.out.println("La consola no está disponible. No se puede ingresar la contraseña.");
        }
    }

    /**
     * Función para validar la contraseña y lanzar una excepción personalizada si no
     * cumple
     */

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

}
