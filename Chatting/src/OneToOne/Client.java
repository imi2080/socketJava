package OneToOne;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener {
	JTextArea display;
	JTextField text;
	JLabel lword;
	Socket client;
	BufferedWriter output;
	BufferedReader input;
	String clientData = "";
	String serverData = "";
	
	public Client() {
		super("클라이언트");
		display = new JTextArea();
		display.setEditable(false);
		add(display, BorderLayout.CENTER);
		
		JPanel pword = new JPanel(new BorderLayout());
		lword = new JLabel("대화말");
		text = new JTextField(30);
		text.addActionListener(this);		//입력된 데이터를 송신
		pword.add(lword, BorderLayout.WEST);
		pword.add(text, BorderLayout.EAST);
		add(pword, BorderLayout.SOUTH);
		addWindowListener(new WinListener());
		setSize(300, 200);
		setVisible(true);
		
	}
	
	public void runClient() {
		try {
			client = new Socket(InetAddress.getLocalHost(), 5000);
			InputStream is = client.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			input = new BufferedReader(isr);		//클라이언트가 전송한 대화말을 수신
			OutputStream os = client.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			output = new BufferedWriter(osw);		// 서버에 대화말을 전송
			while(true) {
				serverData = input.readLine();
				if(serverData.equals("quit")) {
					display.append("\n서버와의 접속 중단");
					output.flush();
					break;
				} else {
					display.append("\n서버 메시지: "+ serverData);
					output.flush();
				}
			}
			client.close();
			
		} catch(IOException e) {
			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		clientData = text.getText();
		try {
			display.append("\n클라이언트 : " + clientData);
			output.write(clientData+"\r\n");
			output.flush();
			text.setText("");
			if(clientData.equals("quit")) {
				client.close();
			}
			
		}catch(IOException e1) {
			e1.printStackTrace();
			
		}
	}
	
	
	public static void main(String[] args) {
		Client c = new Client();
		c.runClient();
		
	}
	
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}
