
package seguridad;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Logger;
import javax.crypto.Cipher;

/**
 * Esta clase se encargará de la encriptación de datos.
 *
 * @author Fredy
 */
public class Seguridad {

    private static PublicKey publicKey;
    private static Cipher cipher;
    private static final char[] HEXADECIMAL_ARRAY = "0123456789ABCDEF".toCharArray();
    private static final Logger LOGGER = Logger.getLogger(Seguridad.class.getName());

    /**
     * Cargar clave pública
     *
     * @param fileName
     * @return clave publica
     * @throws Exception
     */
    private static byte[] fileReader(String path) {
        byte ret[] = null;
        File file = new File(path);
        try {
            ret = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    // Convierte Array de Bytes en hexadecimal
    private static String bytesToHexadecimal(byte[] bytes) {
        char[] caracter = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            caracter[j * 2] = HEXADECIMAL_ARRAY[v >>> 4];
            caracter[j * 2 + 1] = HEXADECIMAL_ARRAY[v & 0x0F];
        }
        return new String(caracter);
    }

    /**
     * Contraseña cifrada con clave pública y convertida a hexadecimal
     *
     * @param contrasenia
     * @return contrasenia cifrada
     */
    public static String encriptarContrasenia(String contrasenia) {
        String pass = "";

        try {
            //Calve pública
            byte fileKey[] = fileReader("Public.key");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(fileKey);
            publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encriptado = cipher.doFinal(contrasenia.getBytes());
            pass = bytesToHexadecimal(encriptado);

        } catch (Exception ex) {
            LOGGER.severe("Error al encriptar con clave pública");
        }
        return pass;
    }
}
