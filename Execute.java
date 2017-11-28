import java.io.*;

public class Execute {
	File temp = new File("");
	String directory = temp.getAbsolutePath();

	String loc = directory + "\\kill_keyboard.exe";
	Process process;
	
	public Execute() {}
	
	public void start() throws IOException {
		process = new ProcessBuilder(loc).start();
	}
	
	public void stop() {
		process.destroy();
	}	
}
