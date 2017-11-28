import java.io.*;
import java.util.*;
import java.net.*;

public class Main_Frame {
	public static void main(String[] args) throws IOException, InterruptedException {

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
		String username = console.next();
		if(username.equals("admin")){
			EncryptData.Encrypt();
		}
		f = new File("Data.txt");
        console = new Scanner(f);

		Person users = new Person(AES.decrypt(console.next(),macString), AES.decrypt(console.next(),macString));
		
		int r = console.nextInt();
		int g = console.nextInt();
		int b = console.nextInt();
		
        console.close();
		
        LoginScreen ls = new LoginScreen(users, r, g, b);
        ls.initiate();
        ls.listen(); 
        ls.setVisible(true);
	}
}
