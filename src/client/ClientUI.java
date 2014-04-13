package client;



import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

import util.ClientServerUtil;
 
public class ClientUI {
    
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;
   private BufferedReader in;
   private PrintWriter out;
   
   public ClientUI(){
      prepareGUI();
   }

   public static void main(String[] args) throws Exception{
      ClientUI  swingControl = new ClientUI();      
      swingControl.showProgressBar();
	  swingControl.connectToServer();
   }

   private void prepareGUI(){
      mainFrame = new JFrame("Client Server App");
      mainFrame.setSize(400,400);
      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      headerLabel = new JLabel("", JLabel.CENTER);        
      statusLabel = new JLabel("",JLabel.CENTER);    

      statusLabel.setSize(100,100);

      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);  
   }

   protected JProgressBar progressBar;
   protected Task task;
   private JButton startButton;
   private JTextArea outputTextArea;
   
    public void connectToServer() throws IOException {
        Socket socket = new Socket("localhost", 9898);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }
   
   private void showProgressBar(){
      headerLabel.setText("Client Server App"); 

      progressBar = new JProgressBar(0, 100);
      progressBar.setValue(0);
      progressBar.setStringPainted(true);
      startButton = new JButton("Start");

      outputTextArea = new JTextArea("",5,30);

      JScrollPane scrollPane = new JScrollPane(outputTextArea);    
         startButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
	            
            task = new Task();                
            task.start();
         }});

      controlPanel.add(startButton);
      controlPanel.add(progressBar);
      controlPanel.add(scrollPane);
      mainFrame.setVisible(true);  
   }
	public class Task extends Thread {    
	    public Task(){
	    }
	
	    public void run(){
			String[] s = ClientServerUtil.generateWords(200);
			for (int i = 0; i < s.length; i++) {
				out.println(s[i]);
			}

			String response;
			
			for (int i = 0; i < s.length; i++) {
				try {
					response = in.readLine();
					if (response == null || response.equals("")) {
						System.exit(0);
					}
				} catch (IOException ex) {
					response = "Error: " + ex;
				}
				final int progress = i/2+1;
				SwingUtilities.invokeLater(new Runnable() {
		             public void run() {
		                progressBar.setValue(progress);
		             }
		          });
				outputTextArea.append(response + "\n");
			}

				
		
	          
	          
	    }
	 }
      
}


