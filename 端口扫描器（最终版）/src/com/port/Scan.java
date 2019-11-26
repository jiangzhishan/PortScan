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

	// 构造器
	Scan(int minPort, int maxPort,int threadNum,int serial) {
		this.minPort = minPort;
		this.maxPort = maxPort; 
		this.threadNum = threadNum;
		this.serial = serial;
	}

	public void run() {   
		int i=0;
		// 扫描指定端口  
		for (i = minPort+serial; i < maxPort; i+= threadNum) {
			Scanner.Result1.append("主机:" + Scanner.hostName.getText()+ " TCP端口:" + i + "\n");
			try {
				hostAddress =InetAddress.getByName(Scanner.hostName.getText());
				Socket socket=new Socket();
				Scanner.Scanning.setText("正在扫描" + i + "端口");
				// 根据主机名和端口号创建套接字地址。
				SocketAddress sockaddr = new InetSocketAddress(hostAddress, i);
                
                socket.connect(sockaddr, time);// 将此套接字连接到具有指定超时值的服务器。
             
				Scanner.Result2.append("主机:" + Scanner.hostName.getText()+ " TCP:" + i + "- - - - 端口开放" + "\n");
				 socket.close();
				 str += "主机:" + Scanner.hostName.getText()+ " TCP:" + i +"端口开放" + "\r\n";
			}catch (ConnectException e) {
				Scanner.Result2.append("主机:" + Scanner.hostName.getText()+ " TCP:" + i + "        端口关闭" + "\n");
				
				str += "主机:" + Scanner.hostName.getText()+ " TCP:" + i+"端口关闭"  + "\r\n";
			} 
			catch (SocketTimeoutException e) {
				Scanner.Result2.append("主机:" + Scanner.hostName.getText()+ " TCP:" + i + "请求超时" + "\n");
				
				str += "主机:" + Scanner.hostName.getText()+ " TCP:" + i +"请求超时" + "\r\n";
			}  
			catch (IOException e) {
				e.printStackTrace();
			}		
		 }
		for (i = minPort+serial; i < maxPort; i+= threadNum) {
			Scanner.Result1.append("主机:" + Scanner.hostName.getText()+ " UDP端口:" + i + "\n");
			try {
				hostAddress =InetAddress.getByName(Scanner.hostName.getText());
				Scanner.Scanning.setText("正在扫描" + i + "端口");
				Scanner.Result2.append("主机:" + Scanner.hostName.getText()+ " UDP:" + i + "        端口关闭" + "\n");
				str += "主机:" + Scanner.hostName.getText()+ " UDP:" + i +"端口关闭" + "\r\n";
			    DatagramSocket server=new DatagramSocket(i, hostAddress);
			    server.close();
			    }
			   catch(SocketException e) {
				   Scanner.Result2.append("主机:" + Scanner.hostName.getText()+ " UDP:" + i + "- - - - 端口开放" + "\n");
					
					str += "主机:" + Scanner.hostName.getText()+ " UDP:" + i+"端口开放"  + "\r\n";
			    } catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
     }	
}
