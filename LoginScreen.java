import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class LoginScreen extends JFrame{
    private static final long serialVersionUID = 1L;
    private int counterTillLock = 3;
    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    
    //get screen width & height
    public final int SCREEN_X = gd.getDisplayMode().getWidth();
    public final int SCREEN_Y = gd.getDisplayMode().getHeight();

    //create JPanel
    private JPanel panel;

    //labels and buttons
    private JLabel Jupiter;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton submitButton;
    private JTextField username;
    private JPasswordField password;
    
    //after login buttons
    private JButton enter = new JButton("Enter");
    private JButton settings = new JButton("Settings");
    
    //variables
    private Person users;
    private int i_r = 0;
    private int i_g = 0;
    private int i_b = 0;
    
    private AltTabStopper ats;
    private TaskKiller tk;

    private GridBagConstraints c = new GridBagConstraints();

	AES aes = new AES();
    
    public LoginScreen(Person u, int r, int g, int b) {
    	//creates JFrame through inheritance
    	super("");
    	
    	users = u;
    	i_r = r;
    	i_g = g;
    	i_b = b;

    }
    private void setFrame() {
        setSize(SCREEN_X, SCREEN_Y);
        //set starting location
        setLocation(0, 0);
        //cannot close window
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //properties
        setAlwaysOnTop(true);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
    }
    private void createPanel() {
    	usernameLabel = new JLabel("Username: ", SwingConstants.LEFT);
        usernameLabel.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));

        passwordLabel = new JLabel("Password: ", SwingConstants.LEFT);
        passwordLabel.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
        getRootPane().setDefaultButton(submitButton);
        
        username = new JTextField("");
        username.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
        username.setColumns(SCREEN_X/100);

        password = new JPasswordField("");
        password.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
        password.setColumns(SCREEN_X/100);
        password.setEchoChar('*');
        
        Jupiter = new JLabel("JUPITER", SwingConstants.CENTER);
        Jupiter.setFont(new Font("Existence-Light", Font.PLAIN, SCREEN_X/10));      
    }
    private void addToPanel() {
    	c.gridx = 0;
    	c.gridy = 0;
    	c.gridwidth = 3;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.insets = new Insets(0, 0, SCREEN_Y/10, 0);
    	panel.add(Jupiter, c);
    	
    	c.fill = GridBagConstraints.NONE;
    	c.anchor = GridBagConstraints.CENTER;
    	c.gridwidth = 1;
    	
    	c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, SCREEN_Y/80, SCREEN_X/60);
        panel.add(usernameLabel, c);
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(0, SCREEN_X/60, SCREEN_Y/80, 0);
        panel.add(username, c);
        
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(SCREEN_Y/80, 0, SCREEN_Y/80, SCREEN_X/60);
        panel.add(passwordLabel, c);
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(SCREEN_Y/80, SCREEN_X/60, SCREEN_Y/80, 0);
        panel.add(password, c);
        
        c.gridx = 1;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(submitButton, c);

        add(panel);
    }
    private void exit() {
    	panel.removeAll();
    	panel.revalidate();
    	repaint();
    }

    public void initiate() {
        setFrame();
        
        //panel on frame
        panel = new JPanel();
        //set panel properties
        panel.setBackground(new Color(i_r, i_g, i_b));  
        panel.setLayout(new GridBagLayout());
        
        //creates everything on the panel
        createPanel();
        addToPanel();
		
		//starts new threads blocking alt + tab and TaskMgr
		tk = new TaskKiller();
        Thread t = new Thread(tk);
        t.start();

        ats = new AltTabStopper();
        Thread k = new Thread(ats);
        k.start();
    }
    public void listen() {
        submitButton.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
                String enteredPassword = password.getText();
                String enteredUsername = username.getText();
                if(!enteredPassword.equals("") && !enteredUsername.equals("")){
                    Person temp = new Person(enteredUsername, enteredPassword);
                    if (users.equals(temp)) {
                    	ats.stop();
						tk.stop();
                        exit();
                        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        setAlwaysOnTop(false);
                        welcome();
                    } else if (counterTillLock > 1) {
                        username.setText("");
                        password.setText("");
                        counterTillLock--;
                        setAlwaysOnTop(false);
                        
                        JLabel[] label = {new JLabel("Unrecognized Username or Password."),
                        		new JLabel("Please Try Again."),
                        		new JLabel("Attempts Left: " + counterTillLock)};
                        for(JLabel l: label) {
                        	l.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
                        }
                        JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.ERROR_MESSAGE);
                        setAlwaysOnTop(true);
                    } else {
                        username.setText("");
                        password.setText("");
                        setAlwaysOnTop(false);
                        JLabel[] label = {new JLabel("Unrecognized Username or Password."),
                        		new JLabel("COMPUTER LOCKED.")};
                        for(JLabel l: label) {
                        	l.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
                        }
                        JOptionPane.showMessageDialog(null, label, "LOCKED", JOptionPane.ERROR_MESSAGE);
                        setAlwaysOnTop(true);

                        Execute lock = new Execute();
                        try {
                            lock.start();
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        USBCheck uc = new USBCheck("JEMS","Hashword","266b0YoHunW9j9NVG1X3", lock);
                        while(uc.getCondition()){
                            uc.run();
                            LoginScreen.super.requestFocus();
                        }
                        try{
                            Runtime.getRuntime().exec("cmd /c taskkill /f /im explorer.exe").waitFor();
                            Runtime.getRuntime().exec("cmd /c start explorer.exe").waitFor();
                        }catch(Exception k){}
						ats.stop();
						tk.stop();
						
                        exit();
                        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        setAlwaysOnTop(false);
                        welcome();
                    }
                }
            }
        });
    }
       
    private void welcome() {
    	JLabel welcome = new JLabel("Welcome");
//    	welcome.setPreferredSize(new Dimension(SCREEN_X/3, SCREEN_Y/10));
    	welcome.setFont(new Font("CinzelDecorative-Regular", Font.PLAIN, SCREEN_Y/8));
    	
    	getRootPane().setDefaultButton(enter);
    	enter.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
    	enter.setPreferredSize(new Dimension(SCREEN_X/5, SCREEN_Y/20));
    	
    	settings.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
    	settings.setPreferredSize(new Dimension(SCREEN_X/5, SCREEN_Y/20));

    	c.insets = new Insets(SCREEN_Y/20, 0, 0, 0);
    	c.gridx = 0;
    	c.gridy = 0;
    	c.gridwidth = 3;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	panel.add(welcome, c);
    	
    	c.insets = new Insets(SCREEN_Y/30, 0, SCREEN_Y/30, 0);
    	c.gridx = 2;
    	c.gridy = 1;
    	c.gridwidth = 1;
    	c.fill = GridBagConstraints.NONE;
    	panel.add(enter, c);
    	
    	c.insets = new Insets(SCREEN_Y/30, 0, 0, 0);
    	c.gridx = 2;
    	c.gridy = 2;
    	c.gridwidth = 1;
    	c.fill = GridBagConstraints.NONE;
    	panel.add(settings, c);
    	
    	enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }
    	});
    	settings.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			exit();
    			menu();
    		}
    	});    	
    }
    private void menu() {
    	JLabel title = new JLabel("Settings", SwingConstants.CENTER);
    	title.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/30));
    	title.setPreferredSize(new Dimension(SCREEN_X/5, SCREEN_Y/20));
    	
    	JButton Profile = new JButton("Edit Profile");
    	Profile.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
    	Profile.setPreferredSize(new Dimension(SCREEN_X/5, SCREEN_Y/20));
    	
    	JButton UI = new JButton("Edit UI");
    	UI.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
    	UI.setPreferredSize(new Dimension(SCREEN_X/5, SCREEN_Y/20));
    	
    	JButton back = new JButton("Back");
    	getRootPane().setDefaultButton(back);
    	back.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
    	back.setPreferredSize(new Dimension(SCREEN_X/5, SCREEN_Y/20));
    	
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.insets = new Insets(0, 0, SCREEN_Y/20, 0);
    	c.gridx = 0;
    	c.gridy = 0;
    	panel.add(title, c);
    	
    	c.insets = new Insets(SCREEN_Y/40, 0, SCREEN_Y/40, 0);
    	c.gridx = 0;
    	c.gridy = 1;
    	panel.add(Profile, c);
    	
    	c.insets = new Insets(SCREEN_Y/40, 0, SCREEN_Y/40, 0);
    	c.gridx = 0;
    	c.gridy = 2;
    	panel.add(UI, c);
    	
    	c.insets = new Insets(SCREEN_Y/40, 0, 0, 0);
    	c.gridx = 0;
    	c.gridy = 3;
    	panel.add(back, c);
    	
    	
    	Profile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	exit();
            	editProfile();
            }
    	});
    	UI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	exit();
            	editUI();
            }
    	});
    	back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	exit();
            	welcome();
            }
    	});	
    }
    
    private void editProfile() {
    	JLabel title = new JLabel("Edit Profile:", SwingConstants.LEFT);
    	title.setFont(new Font("Calibri", Font.ITALIC, SCREEN_X/38));
    	title.setPreferredSize(new Dimension(SCREEN_X/30, SCREEN_Y/10));
    	
    	JLabel usernameText = new JLabel("Change Username:", SwingConstants.LEFT);
    	usernameText.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
    	
    	JLabel passwordText = new JLabel("Change Password:", SwingConstants.LEFT);
    	passwordText.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
    	
    	JTextField usernameInput = new JTextField("");
        usernameInput.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
        usernameInput.setColumns(SCREEN_X/100);

        JTextField passwordInput = new JTextField("");
        passwordInput.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
        passwordInput.setColumns(SCREEN_X/100);
        
        JButton save = new JButton("Save");
    	save.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
    	save.setPreferredSize(new Dimension(SCREEN_X/10, SCREEN_Y/20));
    	getRootPane().setDefaultButton(save);
        
        JButton back = new JButton("Back");
    	back.setFont(new Font("Calibri", Font.PLAIN, 50));
    	back.setPreferredSize(new Dimension(SCREEN_X/10, SCREEN_Y/20));
    	
    	c.insets = new Insets(0, 0, SCREEN_Y/40, 0);
    	c.gridx = 0;
    	c.gridy = 0;
    	c.gridwidth = 1;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	panel.add(title, c);
    	
    	c.insets = new Insets(SCREEN_Y/40, 0, SCREEN_Y/40, 0);
    	c.gridx = 0;
    	c.gridy = 1;
    	c.gridwidth = 1;
    	panel.add(usernameText, c);
    
    	c.insets = new Insets(SCREEN_Y/40, 0, SCREEN_Y/40, 0);
    	c.gridx = 0;
    	c.gridy = 2;
    	c.gridwidth = 1;
    	panel.add(passwordText, c);
    		
    	c.insets = new Insets(SCREEN_Y/40, SCREEN_X/30, SCREEN_Y/40, 0);
    	c.gridx = 1;
    	c.gridy = 1;
    	c.gridwidth = 1;
    	panel.add(usernameInput, c);
    	
    	c.insets = new Insets(SCREEN_Y/40, SCREEN_X/30, SCREEN_Y/40, 0);
    	c.gridx = 1;
    	c.gridy = 2;
    	c.gridwidth = 1;
    	panel.add(passwordInput, c);
    	
    	c.insets = new Insets(SCREEN_Y/40, 0, 0, 0);
    	c.gridx = 0;
    	c.gridy = 3;
    	c.gridwidth = 1;
    	c.fill = GridBagConstraints.NONE;
    	c.anchor = GridBagConstraints.WEST;
    	panel.add(save, c);
    	
    	c.insets = new Insets(SCREEN_Y/40, 0, 0, 0);
    	c.gridx = 1;
    	c.gridy = 3;
    	c.gridwidth = 1;
    	c.anchor = GridBagConstraints.EAST;
    	panel.add(back, c);
    	
    	c.anchor = GridBagConstraints.CENTER;
    	
    	save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	@SuppressWarnings("deprecation")
				String pass = passwordInput.getText();
                String user = usernameInput.getText();
                if(!pass.equals("") && !user.equals("")) {
                	try {
						updateFile(user, pass, i_r, i_g, i_b);
					} catch (FileNotFoundException e1) {}

                	exit();
                	welcome();
                }
            }
    	});

    	back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	exit();
            	menu();
            }
    	});	
    }
    private void editUI() throws NumberFormatException {
    	JLabel title = new JLabel("Edit UI", SwingConstants.LEFT);
    	title.setFont(new Font("Calibri", Font.ITALIC, SCREEN_X/38));
    	title.setPreferredSize(new Dimension(SCREEN_X/3, SCREEN_Y/10));
    	
    	JLabel t_rgb_R = new JLabel("Change background RGB r-value:", SwingConstants.LEFT);
    	t_rgb_R.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
    	
    	JLabel t_rgb_G = new JLabel("Change background RGB g-value:", SwingConstants.LEFT);
    	t_rgb_G.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
    	
    	JLabel t_rgb_B = new JLabel("Change background RGB b-value:", SwingConstants.LEFT);
    	t_rgb_B.setFont(new Font("Calibri", Font.PLAIN, SCREEN_X/60));
    	
    	JTextField i_rgb_R = new JTextField("");
        i_rgb_R.setFont(new Font("Calibri", Font.PLAIN, 50));
        i_rgb_R.setColumns(SCREEN_X/2000);
        
        JTextField i_rgb_G = new JTextField("");
        i_rgb_G.setFont(new Font("Calibri", Font.PLAIN, 50));
        i_rgb_G.setColumns(SCREEN_X/2000);
        
        JTextField i_rgb_B = new JTextField("");
        i_rgb_B.setFont(new Font("Calibri", Font.PLAIN, 50));
        i_rgb_B.setColumns(SCREEN_X/2000);
        
        JButton save = new JButton("Save");
        getRootPane().setDefaultButton(save);
    	save.setFont(new Font("Calibri", Font.PLAIN, 50));
    	save.setPreferredSize(new Dimension(SCREEN_X/10, SCREEN_Y/20));
        
        JButton back = new JButton("Back");
    	back.setFont(new Font("Calibri", Font.PLAIN, 50));
    	back.setPreferredSize(new Dimension(SCREEN_X/10, SCREEN_Y/20));
    	

    	c.insets = new Insets(0, 0, SCREEN_Y/40, 0);
    	c.gridx = 0;
    	c.gridy = 0;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	panel.add(title, c);
    	
    	c.insets = new Insets(SCREEN_Y/40, 0, SCREEN_Y/40, 0);
    	c.gridx = 0;
    	c.gridy = 2;
    	panel.add(t_rgb_R, c);
    
    	c.insets = new Insets(SCREEN_Y/40, 0, SCREEN_Y/40, 0);
    	c.gridx = 0;
    	c.gridy = 3;
    	panel.add(t_rgb_G, c);

    	c.insets = new Insets(SCREEN_Y/40, 0, SCREEN_Y/40, 0);
    	c.gridx = 0;
    	c.gridy = 4;
    	panel.add(t_rgb_B, c);
    	
    	c.insets = new Insets(SCREEN_Y/40, 0, SCREEN_Y/40, 0);
    	c.gridx = 1;
    	c.gridy = 2;
    	c.anchor = GridBagConstraints.EAST;
    	panel.add(i_rgb_R, c);

    	c.insets = new Insets(SCREEN_Y/40, 0, SCREEN_Y/40, 0);
    	c.gridx = 1;
    	c.gridy = 3;
    	panel.add(i_rgb_G, c);
    	
    	c.insets = new Insets(SCREEN_Y/40, 0, SCREEN_Y/40, 0);
    	c.gridx = 1;
    	c.gridy = 4;
    	panel.add(i_rgb_B, c);

    	c.insets = new Insets(SCREEN_Y/30, 0, 0, 0);
    	c.gridx = 0;
    	c.gridy = 5;
    	c.fill = GridBagConstraints.NONE;
    	c.anchor = GridBagConstraints.WEST;
    	panel.add(save, c);
    	
    	c.insets = new Insets(SCREEN_Y/30, 0, 0, 0);
    	c.gridx = 1;
    	c.gridy = 5;
    	c.anchor = GridBagConstraints.EAST;
    	panel.add(back, c);
    	
    	c.anchor = GridBagConstraints.CENTER;
    	
    	save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) throws NumberFormatException {
            	String r = i_rgb_R.getText();
                String g = i_rgb_G.getText();
                String b = i_rgb_B.getText();
                
                if(!r.equals("") && !g.equals("") && !b.equals("")) {
                	int r1 = Integer.valueOf(r);
                	int g1 = Integer.valueOf(g);
                	int b1 = Integer.valueOf(b);
                	
                	if((r1>=0 && r1<256) && (g1>=0 && g1<256) && (b1>=0 && b1<256)) {
                		try {
							updateFile("", "", r1, g1, b1);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    	
                    	i_r = r1;
                    	i_g = g1;
                    	i_b = b1;
                    	panel.setBackground(new Color(i_r, i_g, i_b));
                    	panel.repaint();
                    	
                    	exit();
                    	welcome();
                	} else {
                		JLabel warning = new JLabel("INVALID VALUES. RGB values must be between 0 and 255.", SwingConstants.LEFT);
                    	warning.setFont(new Font("Calibri", Font.ITALIC, SCREEN_X/80));
                    	
                		i_rgb_R.setText("");
                		i_rgb_G.setText("");
                		i_rgb_B.setText("");
                		
                		c.insets = new Insets(SCREEN_X/60, 0, SCREEN_X/60, 0);
                    	c.gridx = 0;
                    	c.gridy = 1;
                    	c.gridwidth = 2;
                    	c.anchor = GridBagConstraints.WEST;
                		panel.add(warning, c);
                		panel.revalidate();
                		repaint();
                		c.anchor = GridBagConstraints.CENTER;
                	}                	
                }
            }
    	});
    	back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	exit();
            	menu();
            }
    	});
    }
    private void updateFile(String u, String p, int r1, int g1, int b1) throws FileNotFoundException {
    	File f = new File("Data.txt");
    	Scanner console = new Scanner(f);
    	String name = console.next();
    	String pass = console.next();
    	console.close();
    	if(!f.exists()){
    		try {
				f.createNewFile();
			} catch (IOException e1) {}
    	}
    	
    	FileWriter fw = null;
		try {
			fw = new FileWriter(f.getAbsolutePath());
		} catch (IOException e1) {}

		PrintWriter pw = new PrintWriter(fw);

		if(!u.equals("") && !p.equals("")) {
			pw.println(u);
			pw.println(p);
		}else {
			pw.println(name);
			pw.println(pass);
		}
		pw.println(r1);
    	pw.println(g1);
    	pw.println(b1);
    	pw.close();

		if(!u.equals("") && !p.equals("")){
			EncryptData.Encrypt();
		}
    }
}