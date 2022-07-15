package org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
public class Client implements Runnable { 
	
	
	 // initialize socket and input output streams
	public static Socket socket            = null;
    public static DataInputStream  input   = null;
    public static DataOutputStream out     = null;
    public static DataInputStream  Datainput   = null;
	long Framecounter=0;
	static byte[] DateRecv= new byte[1024];
	static int byteRead1 = 0,SerialDatalength=0;
	static int  start2=0;
	OndemandParameters cmd = new OndemandParameters();
	class RequestCommand 
	{
	    public  int[] USOBIS = {0x01,0x00,0x5E,0x5B,0x00,0xFF};
	    public  int[] USIC = { 0x00, 0x07 };
	    public  int[] USATTB = { 0x02, 0x00 };
	}
	
	
	public Client(OndemandParameters cmd)
	{
	//	System.out.println(currentThread().getId() + " is stopping user thread");	 
          
        this.cmd=cmd;
     //   System.out.println(cmd.MeterPort + " is stopping user thread");
      //  System.out.println(cmd.MeterIP + " is stopping user thread");
      //  this.OndemandParameters.MeterPort= cmd.MeterPort;
	}
	

    

public void run()
{
		try
		{
			//new TcpClient("2402:3a80:1700:186::2",4059).buildHandler("Hello", rs -> {	System.out.println(rs);
			//}).execute();
			new TcpClient(cmd.MeterIP, cmd.MeterPort).buildHandler(cmd, rs -> {	System.out.println(rs);
			}).execute();
		//	System.out.println("Thread is Started....");

		}
		catch(Exception e) {
			//  Block of code to handle errors
		}
}
	 

}


