package com.port;

import java.awt.Color; //��ĵ���
import java.awt.Container;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Scanner extends JFrame implements ActionListener {
     
	public static void main(String[] args) {
		new Scanner();		
	}

	// ����������
	static int j=0;
	public static JFrame Frame = new JFrame();
	public static Label labelIP = new Label("����IP");
	public static Label labelPortStart = new Label("��ʼ�˿ڣ�");
	public static Label labelPortEnd = new Label("�����˿ڣ�");
	public static Label labelThread = new Label("�߳�����");
	public static Label labelResult1 = new Label("ɨ����:");
	public static Label State = new Label("ɨ��״̬��");
	public static Label Scanning = new Label("ɨ�����");
	public static Label Timeout = new Label("����ʱ:");
	
	public static JTextField hostName = new JTextField("127.0.0.1");
	public static JTextField PortStart = new JTextField("130");
	public static JTextField PortEnd = new JTextField("140");
	public static JTextField ThreadNum = new JTextField("5");
	public static JTextField time = new JTextField("2000");

	// �ı�������ʾɨ����
	public static TextArea Result1 = new TextArea();
	public static TextArea Result2 = new TextArea();
	public static Label DLGINFO = new Label("ע��");
	public static JButton Start = new JButton("��ʼɨ��");
	public static JButton Exit = new JButton("ɨ���˳�");
	public static JButton save = new JButton("����");

	// ������ʾ�Ի���
	public static JDialog DLGError = new JDialog(Frame, "ERROR");
	public static JButton OK = new JButton("OK");
	
	public Scanner() {
		// ��������������
		Frame.setTitle("�˿�ɨ����--jax");
		// ����������λ�úʹ�С
		Frame.setSize(560, 510);
		Frame.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
		Frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// ����ɨ�谴ť���˳���ť
		Start.setBounds(160, 232, 90, 30);
		Start.setActionCommand("Start");
		Start.addActionListener(this);
		Exit.setBounds(455, 445, 90, 30);
		Exit.setActionCommand("Exit");
		Exit.addActionListener(this);
		save.setBounds(5,445,90,30);
		save.setActionCommand("save");
		save.addActionListener(this);
		
		labelIP.setBounds(17, 13, 50, 20);
		Timeout.setBounds(290,45,55,20);
		hostName.setBounds(67, 10, 92, 25);
		hostName.setHorizontalAlignment(JTextField.CENTER);

		labelPortStart.setBounds(162, 13, 60, 20);
		PortStart.setBounds(227, 10, 45, 25);
		PortStart.setHorizontalAlignment(JTextField.CENTER);

		labelPortEnd.setBounds(292, 13, 60, 20);
		PortEnd.setBounds(357, 10, 45, 25);
		PortEnd.setHorizontalAlignment(JTextField.CENTER);

		labelThread.setBounds(422, 13, 50, 20);
		ThreadNum.setBounds(477, 10, 45, 25);
		time.setBounds(350,44,55,20);
		ThreadNum.setHorizontalAlignment(JTextField.CENTER);

		labelResult1.setBounds(1, 240, 60, 30);
		Result1.setBounds(1, 65, 542, 160);
		Result1.setEditable(false);
		Result1.setBackground(Color.RED);
		Result2.setBounds(1, 280, 542, 160);
		Result2.setEditable(false);
		State.setBounds(1, 45, 55, 20);
		Scanning.setBounds(60, 40, 120, 30);

		// ���ô�����ʾ��
		Container displayPanel = DLGError.getContentPane();
		displayPanel.setLayout(null);
		displayPanel.add(DLGINFO);
		displayPanel.add(OK);
		OK.setActionCommand("OK");
		OK.addActionListener(this);
 
		// ��������������������
		Frame.setLayout(null);
		Frame.setResizable(false);
		Frame.setVisible(true);	
		Frame.add(Start);
		Frame.add(Timeout);
		Frame.add(time);
		Frame.add(Exit);
		Frame.add(save);
		Frame.add(labelIP);
		Frame.add(hostName);
		Frame.add(labelPortStart);
		Frame.add(labelPortEnd);
		Frame.add(PortStart);
		Frame.add(PortEnd); 
		Frame.add(labelThread);
		Frame.add(ThreadNum);
		Frame.add(labelResult1);
		Frame.add(Result1);
		Frame.add(Result2);
		Frame.add(State);
		Frame.add(Scanning);
		
	}

	// ��������ļ���ԭ��
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("save")){
			JFileChooser fc=new JFileChooser();
			int returnVal=fc.showSaveDialog(null);
			//��������桱
			if(returnVal == 0)
			{
				File saveFile=fc.getSelectedFile();
				try {
						FileWriter writeOut = new FileWriter(saveFile);
						writeOut.write(Result2.getText());
						writeOut.close();
					}catch (IOException ex) 
				        {
							System.out.println("����ʧ��");
						} 
			}
			//�����ȡ���� 
			else
			   return;
		}
	   
		if (cmd.equals("Start")) {
			Result1.setText(null);
			Result2.setText(null);
			j++;
			try {
				Scan.hostAddress = InetAddress.getByName(Scanner.hostName.getText());
				Scan.time=Integer.parseInt(Scanner.time.getText());
			}catch (UnknownHostException e1) {
				DLGError.setBounds(300, 280, 295, 120);
				DLGINFO.setText("��ע�⣬���Ǵ����IP��ַor����");
				DLGINFO.setBounds(10, 20, 280, 20);
				OK.setBounds(120, 50, 60, 30);
				DLGError.setVisible(true); 
				return;
			}

			int minPort; 
			int maxPort;
			int threadNum;

			try {
				// ��ȡ��������
				minPort = Integer.parseInt(PortStart.getText());
				maxPort = Integer.parseInt(PortEnd.getText());
				threadNum = Integer.parseInt(ThreadNum.getText());
			} catch (NumberFormatException e1) {
				DLGError.setBounds(300, 280, 295, 120);
				DLGINFO.setText("��ע�⣡����Ķ˿ںŻ��߳������˿ںź��߳�����Ϊ����!");
				DLGINFO.setBounds(10, 20, 280, 20);
				OK.setBounds(120, 50, 60, 30);
				DLGError.setVisible(true); 
				return;
			}

			// ������Ϣ������
			if ((minPort < 0) || (maxPort > 65535) || (minPort > maxPort)) {
				DLGError.setBounds(300, 280, 295, 120);
				DLGINFO.setText("��С�˿ڱ�����0-65535����С�����˿ڵ�����");
				DLGINFO.setBounds(10, 20, 280, 20);
				OK.setBounds(120, 50, 60, 30);
				DLGError.setVisible(true); 
				return;
			}
			if ((threadNum > 500) || (threadNum < 0)) {
				DLGError.setBounds(300, 280, 184, 120);
				DLGINFO.setText("������������1-500�е�����");
				DLGINFO.setBounds(10, 20, 200, 20);
				OK.setBounds(55, 50, 60, 30);
				DLGError.setVisible(true);
				return;
			}

			Result1.append("����ɨ�� " + hostName.getText() + " �߳���:" + threadNum+ "\n");
			Scanning.setText("��ʼɨ�� ....");
			Result1.append("��ʼ�˿� " + minPort + "�����˿� " + maxPort + " \n");

			ExecutorService threadPool = Executors.newCachedThreadPool();//�̳߳�
			for (int i = 0; i < threadNum; i++) {
				Scan scan = new Scan(minPort, maxPort,threadNum,i);
				threadPool.execute(scan);
			}
			threadPool.shutdown();
			try {
				Thread.sleep(1);// ���ô���ȴ�ʱ��
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Result1.append("ɨ�����!\n");
			Scanning.setText("ɨ����ɣ�"); 
		} else if (cmd.equals("ok")) {
			DLGError.dispose();
		} else if (cmd.equals("Exit")) {
			System.exit(1);
		}
	}

}