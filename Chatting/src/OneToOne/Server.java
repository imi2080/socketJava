package OneToOne;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class Server extends JFrame implements ActionListener{
	JTextArea display;
	JTextField text;
	JLabel lword;
	Socket connection;
	BufferedWriter output;
	BufferedReader input;
	String clientData = "";
	String serverData = "";
	
	public Server() {
		super("서버");
		display = new JTextArea();
		display.setEditable(false);
		add(display, BorderLayout.CENTER);
		
		JPanel pword = new JPanel(new BorderLayout());
		lword = new JLabel("chatting");
		text = new JTextField(30);
		text.addActionListener(this);//입력된 데이터를 송신
		pword.add(lword, BorderLayout.WEST);
		pword.add(text, BorderLayout.EAST);
		add(pword, BorderLayout.SOUTH);
		
		addWindowListener(new WinListener());
		setSize(300, 200);
		setVisible(true);
		
	}
	
	public void runServer() {
		ServerSocket server;
		
		try {
			server = new ServerSocket(5000, 100);
			connection = server.accept();
			InputStream is = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			input = new BufferedReader(isr);		//서버가 전송한 대화말을 수신
			OutputStream os = connection.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			output = new BufferedWriter(osw);		// 클라이언트에 대화말을 전송
			while(true) {
				clientData = input.readLine();
				if(clientData.equals("quit")) {
					display.append("\n클라이언트와의 접속 중단");
					output.flush();
					break;
				} else {
					display.append("\n클라이언트 메시지: "+ clientData);
					output.flush();
				}
			}
			connection.close();
			
		} catch(IOException e) {
			
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		serverData = text.getText();
		try {
			display.append("\n서버 : " + serverData);
			output.write(serverData+"\r\n");
			output.flush();
			text.setText("");
			if(serverData.equals("quit")) {
				connection.close();
			}
			
		}catch(IOException e1) {
			e1.printStackTrace();
			
		}
		
	}
	
	public static void main(String[] args) {
		Server s = new Server();
		s.runServer();
	}
	
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

}
