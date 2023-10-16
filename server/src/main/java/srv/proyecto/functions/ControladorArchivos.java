package srv.proyecto.functions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControladorArchivos {
    // Función para crear la carpeta y archivo de texto en el servidor

    public static void crearCarpetaServidor(Connection cn, int usuarioId) {
        try {
            // Obtener información del usuario de la base de datos
            String query = "SELECT nombre_usuario, id, grupos FROM Usuarios WHERE id = ?";
            try (PreparedStatement pst = cn.prepareStatement(query)) {
                pst.setInt(1, usuarioId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String nombreUsuario = rs.getString("nombre_usuario");
                        int idUsuario = rs.getInt("id");
                        String grupos = rs.getString("grupos");
    
                        // Generar el contenido del archivo de texto
                        String contenido = "Nombre de usuario: " + nombreUsuario + "\n";
                        contenido += "ID de usuario: " + idUsuario + "\n";
                        contenido += "Grupos: " + grupos + "\n";
    
                        
                        String serverFolderPath = "Directorios/Grupos"; 
    
                        // Crear la carpeta si no existe (dentro de la carpeta base del proyecto)
                        File serverFolder = new File(serverFolderPath);
                        if (!serverFolder.exists()) {
                            serverFolder.mkdirs();
                        }
    
                        // Crear el archivo de texto y escribir el contenido
                        String filePath = serverFolderPath + File.separator + nombreUsuario + ".txt";
                        FileWriter fileWriter = new FileWriter(filePath);
                        fileWriter.write(contenido);
                        fileWriter.close();
    
                        System.out.println("Carpeta y archivo de datos del usuario creados en el servidor.");
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    
}
