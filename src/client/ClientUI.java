package client;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
/**
 * 
 * client user interface
 *
 */
public class ClientUI {

	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;

	protected JProgressBar progressBar;
	private JButton startButton;
	private JTextArea outputTextArea;

	private Client client;

	public ClientUI() {
		prepareGUI();
	}

	public static void main(String[] args) throws Exception {
		ClientUI swingControl = new ClientUI();
		swingControl.showProgressBar();
		swingControl.connectToServer();
	}

	private void connectToServer() throws IOException {
		client = new Client(this);
		client.connectToServer();
	}

	private void disconnectFromServer() throws IOException {
		client.disconnectFromServer();
	}

	private void prepareGUI() {
		mainFrame = new JFrame("Client Server App");
		mainFrame.setSize(400, 400);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				try {
					disconnectFromServer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		headerLabel = new JLabel("", JLabel.CENTER);
		statusLabel = new JLabel("", JLabel.CENTER);

		statusLabel.setSize(100, 100);

		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);
		mainFrame.setVisible(true);
	}
	/**
	 * progress bar update & processed word appended
	 * @param response processed word
	 * @param responseIndex	word count
	 */
	public void update(String response, int responseIndex) {
		final int currentProgress = responseIndex / 2 + 1;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressBar.setValue(currentProgress);
			}
		});
		outputTextArea.append(response + "\n");
	}

	public void update(String response) {
		outputTextArea.append(response + "\n");
	}
	/**
	 * disable button while words batch is beeing processed 
	 */
	public void finished() {
		startButton.setEnabled(true);
	}

	private void showProgressBar() {
		headerLabel.setText("Client Server App");

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		startButton = new JButton("Start");

		outputTextArea = new JTextArea("", 5, 30);

		JScrollPane scrollPane = new JScrollPane(outputTextArea);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				outputTextArea.setText("");
				progressBar.setValue(0);

				startButton.setEnabled(false);
				try {
					client.sendWords();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});

		controlPanel.add(startButton);
		controlPanel.add(progressBar);
		controlPanel.add(scrollPane);
		mainFrame.setVisible(true);
	}

}
