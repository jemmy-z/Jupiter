public class TaskKiller implements Runnable{
    private boolean condition = false;
    public TaskKiller() {
    	condition = true;
    }
    public void stop() {
    	condition = false;
    }
    
    public void run(){
        while(condition){
            try {
                Runtime.getRuntime().exec("cmd /c start shortcut.lnk").waitFor();
                Thread.sleep(200);
            } catch (Exception e) {
            }
        }
    }
}