import java.io.*;
import javax.swing.filechooser.*;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.*;
import java.util.*;
public class USBCheck {
    private boolean condition = true;
    private File[] f;
    private String usbName;
    private String fileName;
    private String unHashedPassword;
    private Execute lock;
    public USBCheck(String usbName, String fileName, String unHashedPassword,Execute lock){
        this.usbName = usbName;
        this.fileName = fileName;
        this.unHashedPassword = unHashedPassword;
        this.lock = lock;
    }
    public void run(){
        int index = checkUSBName();
        if(index >= 0){
            String directory = this.f[index].getAbsolutePath();
            //Second Lager: Correct/Existing File Name
            File f = new File(directory+"/" + fileName +".txt");
            Scanner sc;
            try {
                sc = new Scanner(f);
                String hashString = sc.nextLine();
                //Third Layer: Text in File
                if(hashString.equals(sha512(unHashedPassword))){
//                    System.out.println("Success");
                    condition = false;
                    lock.stop();
                }
            }catch(FileNotFoundException e){
            }
        }
    }
    public int checkUSBName(){        //First layer: Check for USB name
        f = File.listRoots();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        int i;
        for(i = 0 ; i < f.length ; i++){
            String temp = fsv.getSystemDisplayName(f[i]);
            temp = !temp.equals("")?temp.substring(0,temp.indexOf(" ")):temp;
            if(temp.equals(usbName)){
                return i;
            }
        }
        return -1;
    }
    private String sha512(String input){
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA-512");
            return new HexBinaryAdapter().marshal(md5.digest(input.getBytes()));
        }catch (NoSuchAlgorithmException e){
            throw new NoSuchElementException();
        }
    }
    public boolean getCondition(){
        return condition;
    }
}
