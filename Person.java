import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.*;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Person {
    private String hexUsername;
    private String hexPassword;
    public Person(String username, String password){
        hexUsername = sha512(username);
        hexPassword = sha512(password);
    }
    private String sha512(String input){
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA-512");
            return new HexBinaryAdapter().marshal(md5.digest(input.getBytes()));
        }catch (NoSuchAlgorithmException e){
            throw new NoSuchElementException();
        }
    }
    public String getHexUsername(){
        return hexUsername;
    }
    public String getHexPassword(){
        return hexPassword;
    }
    public boolean equals(Object n){
        return (n instanceof Person && ((Person) n).getHexUsername().equals(this.hexUsername) && ((Person) n).getHexPassword().equals(this.hexPassword));
    }
    public int hashCode(){
        return Objects.hashCode(hexUsername);
    }
}
