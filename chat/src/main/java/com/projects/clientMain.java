/**
 * The `clientMain` class serves as the entry point for the client application.
 * It connects to a server using the `ConexionClient` class and displays the
 * response received from the server.
 */
package com.projects;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.StringTokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class clientMain {

    public static void main(String[] args) throws IOException {

        System.out.println("Inicia cliente");

        Socket sk = new Socket("localhost", 8100); // accepta una conexión

        InputStream is = sk.getInputStream();
        OutputStream os = sk.getOutputStream();

        byte pon = 1;

        String s = "Hola";

        byte[] b = s.getBytes();

        int longitud = b.length;

        os.write(longitud); // funciona si es menos de 255 bytes
        os.write(b);

        System.out.println("Enviado " + s + " (bytes: " + longitud);

        sk.close(); // tanquem la connexió
    }

    static void espera(int tiempo) {
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException ex) {
            Logger.getLogger(clientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
