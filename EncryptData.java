import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Scanner;

public class EncryptData {
    public static void Encrypt() throws FileNotFoundException{
        String macString = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface nwi = NetworkInterface.getByInetAddress(address);
            byte mac[] = nwi.getHardwareAddress();
            macString = Arrays.toString(mac);
        } catch(Exception e) {}

        AES aes = new AES();

        File f = new File("Data.txt");
        Scanner console = new Scanner(f);

        String name = console.next();
        String pass = console.next();
        String r = console.next();
        String g = console.next();
        String b = console.next();

        FileWriter fw = null;
        try {
            fw = new FileWriter(f.getAbsolutePath());
        } catch (IOException e1) {}
        PrintWriter pw = new PrintWriter(fw);

        pw.println(AES.encrypt(name,macString));
        pw.println(AES.encrypt(pass,macString));
        pw.println(r);
        pw.println(g);
        pw.println(b);

        pw.close();
    }
}
