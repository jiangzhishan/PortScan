package com.port;

import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

class Scan extends Thread {
	static int time;
	static String str = "";
	int maxPort, minPort,threadNum, serial;
	public static InetAddress hostAddress;

	// ������
	Scan(int minPort, int maxPort,int threadNum,int serial) {
		this.minPort = minPort;
		this.maxPort = maxPort; 
		this.threadNum = threadNum;
		this.serial = serial;
	}

	public void run() {   
		int i=0;
		// ɨ��ָ���˿�  
		for (i = minPort+serial; i < maxPort; i+= threadNum) {
			Scanner.Result1.append("����:" + Scanner.hostName.getText()+ " TCP�˿�:" + i + "\n");
			try {
				hostAddress =InetAddress.getByName(Scanner.hostName.getText());
				Socket socket=new Socket();
				Scanner.Scanning.setText("����ɨ��" + i + "�˿�");
				// �����������Ͷ˿ںŴ����׽��ֵ�ַ��
				SocketAddress sockaddr = new InetSocketAddress(hostAddress, i);
                
                socket.connect(sockaddr, time);// �����׽������ӵ�����ָ����ʱֵ�ķ�������
             
				Scanner.Result2.append("����:" + Scanner.hostName.getText()+ " TCP:" + i + "- - - - �˿ڿ���" + "\n");
				 socket.close();
				 str += "����:" + Scanner.hostName.getText()+ " TCP:" + i +"�˿ڿ���" + "\r\n";
			}catch (ConnectException e) {
				Scanner.Result2.append("����:" + Scanner.hostName.getText()+ " TCP:" + i + "        �˿ڹر�" + "\n");
				
				str += "����:" + Scanner.hostName.getText()+ " TCP:" + i+"�˿ڹر�"  + "\r\n";
			} 
			catch (SocketTimeoutException e) {
				Scanner.Result2.append("����:" + Scanner.hostName.getText()+ " TCP:" + i + "����ʱ" + "\n");
				
				str += "����:" + Scanner.hostName.getText()+ " TCP:" + i +"����ʱ" + "\r\n";
			}  
			catch (IOException e) {
				e.printStackTrace();
			}		
		 }
		for (i = minPort+serial; i < maxPort; i+= threadNum) {
			Scanner.Result1.append("����:" + Scanner.hostName.getText()+ " UDP�˿�:" + i + "\n");
			try {
				hostAddress =InetAddress.getByName(Scanner.hostName.getText());
				Scanner.Scanning.setText("����ɨ��" + i + "�˿�");
				Scanner.Result2.append("����:" + Scanner.hostName.getText()+ " UDP:" + i + "        �˿ڹر�" + "\n");
				str += "����:" + Scanner.hostName.getText()+ " UDP:" + i +"�˿ڹر�" + "\r\n";
			    DatagramSocket server=new DatagramSocket(i, hostAddress);
			    server.close();
			    }
			   catch(SocketException e) {
				   Scanner.Result2.append("����:" + Scanner.hostName.getText()+ " UDP:" + i + "- - - - �˿ڿ���" + "\n");
					
					str += "����:" + Scanner.hostName.getText()+ " UDP:" + i+"�˿ڿ���"  + "\r\n";
			    } catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
     }	
}
