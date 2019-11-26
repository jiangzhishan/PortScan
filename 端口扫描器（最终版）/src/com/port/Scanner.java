package com.port;

import java.awt.Color; //类的调用
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

	// 创建主窗口
	static int j=0;
	public static JFrame Frame = new JFrame();
	public static Label labelIP = new Label("主机IP");
	public static Label labelPortStart = new Label("起始端口：");
	public static Label labelPortEnd = new Label("结束端口：");
	public static Label labelThread = new Label("线程数：");
	public static Label labelResult1 = new Label("扫描结果:");
	public static Label State = new Label("扫描状态：");
	public static Label Scanning = new Label("扫描待命");
	public static Label Timeout = new Label("请求超时:");
	
	public static JTextField hostName = new JTextField("127.0.0.1");
	public static JTextField PortStart = new JTextField("130");
	public static JTextField PortEnd = new JTextField("140");
	public static JTextField ThreadNum = new JTextField("5");
	public static JTextField time = new JTextField("2000");

	// 文本区域，显示扫描结果
	public static TextArea Result1 = new TextArea();
	public static TextArea Result2 = new TextArea();
	public static Label DLGINFO = new Label("注意");
	public static JButton Start = new JButton("开始扫描");
	public static JButton Exit = new JButton("扫描退出");
	public static JButton save = new JButton("保存");

	// 错误提示对话框
	public static JDialog DLGError = new JDialog(Frame, "ERROR");
	public static JButton OK = new JButton("OK");
	
	public Scanner() {
		// 设置主窗体名称
		Frame.setTitle("端口扫描器--jax");
		// 设置主窗体位置和大小
		Frame.setSize(560, 510);
		Frame.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
		Frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// 设置扫描按钮和退出按钮
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

		// 设置错误提示框
		Container displayPanel = DLGError.getContentPane();
		displayPanel.setLayout(null);
		displayPanel.add(DLGINFO);
		displayPanel.add(OK);
		OK.setActionCommand("OK");
		OK.addActionListener(this);
 
		// 在主窗体中添加其他组件
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

	// 错误产生的几个原因
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("save")){
			JFileChooser fc=new JFileChooser();
			int returnVal=fc.showSaveDialog(null);
			//点击“保存”
			if(returnVal == 0)
			{
				File saveFile=fc.getSelectedFile();
				try {
						FileWriter writeOut = new FileWriter(saveFile);
						writeOut.write(Result2.getText());
						writeOut.close();
					}catch (IOException ex) 
				        {
							System.out.println("保存失败");
						} 
			}
			//点击“取消” 
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
				DLGINFO.setText("请注意，这是错误的IP地址or域名");
				DLGINFO.setBounds(10, 20, 280, 20);
				OK.setBounds(120, 50, 60, 30);
				DLGError.setVisible(true); 
				return;
			}

			int minPort; 
			int maxPort;
			int threadNum;

			try {
				// 获取输入数据
				minPort = Integer.parseInt(PortStart.getText());
				maxPort = Integer.parseInt(PortEnd.getText());
				threadNum = Integer.parseInt(ThreadNum.getText());
			} catch (NumberFormatException e1) {
				DLGError.setBounds(300, 280, 295, 120);
				DLGINFO.setText("请注意！错误的端口号或线程数，端口号和线程数须为整数!");
				DLGINFO.setBounds(10, 20, 280, 20);
				OK.setBounds(120, 50, 60, 30);
				DLGError.setVisible(true); 
				return;
			}

			// 输入信息错误处理
			if ((minPort < 0) || (maxPort > 65535) || (minPort > maxPort)) {
				DLGError.setBounds(300, 280, 295, 120);
				DLGINFO.setText("最小端口必须是0-65535并且小于最大端口的整数");
				DLGINFO.setBounds(10, 20, 280, 20);
				OK.setBounds(120, 50, 60, 30);
				DLGError.setVisible(true); 
				return;
			}
			if ((threadNum > 500) || (threadNum < 0)) {
				DLGError.setBounds(300, 280, 184, 120);
				DLGINFO.setText("进程数必须是1-500中的整数");
				DLGINFO.setBounds(10, 20, 200, 20);
				OK.setBounds(55, 50, 60, 30);
				DLGError.setVisible(true);
				return;
			}

			Result1.append("正在扫描 " + hostName.getText() + " 线程数:" + threadNum+ "\n");
			Scanning.setText("开始扫描 ....");
			Result1.append("开始端口 " + minPort + "结束端口 " + maxPort + " \n");

			ExecutorService threadPool = Executors.newCachedThreadPool();//线程池
			for (int i = 0; i < threadNum; i++) {
				Scan scan = new Scan(minPort, maxPort,threadNum,i);
				threadPool.execute(scan);
			}
			threadPool.shutdown();
			try {
				Thread.sleep(1);// 设置处理等待时间
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Result1.append("扫描完成!\n");
			Scanning.setText("扫描完成！"); 
		} else if (cmd.equals("ok")) {
			DLGError.dispose();
		} else if (cmd.equals("Exit")) {
			System.exit(1);
		}
	}

}