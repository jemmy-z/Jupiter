import java.awt.*;
import java.awt.event.KeyEvent;

public class AltTabStopper implements Runnable {
    private boolean working;

    public AltTabStopper() {
    	working = true;
    }

    public void stop() {
        working = false;
    }

    public void run() {
        try {
            Robot robot = new Robot();
            while (working) {
                robot.keyRelease(KeyEvent.VK_ALT);
                robot.keyRelease(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_DELETE);
                robot.keyRelease(KeyEvent.VK_WINDOWS);
                //frame.requestFocus();
                try { Thread.sleep(10); } catch(Exception e) {}
            }
        } catch (Exception e) { e.printStackTrace(); System.exit(-1); }
    }
}