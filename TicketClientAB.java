import java.io.*;
import java.net.*;
public class TicketClientAB
{
	final static int SAport=2000;
	final static int SBport=5000;
	public static void main(String args[])
	{
		int z=0;
		while(z<7)
		{
		try
		{
			Socket socket=new Socket(InetAddress.getLocalHost(),SAport);
			DataOutputStream out=new DataOutputStream(socket.getOutputStream());
			out.writeInt(5000);
			for(int j=0;j<6;j++)
			{
				int x=(int)(Math.random()*40);
				out.writeInt(x);
				if(j%2==0)
				{
					try
					{
						Thread.sleep(100);
					}
					catch(InterruptedException e)
					{
					}
				}
			}
			out.writeInt(-1);
			socket.close();
		}
		catch(IOException e)
		{
		}
		z=z+1;
		}
		try
		{
			Thread.sleep(500);
		}
		catch(InterruptedException e)
		{
		}
		int y=0;
		while(y<7)
		{
		try
		{
			Socket socket1=new Socket(InetAddress.getLocalHost(),SBport);
			DataOutputStream out1=new DataOutputStream(socket1.getOutputStream());
			out1.writeInt(2000);
			for(int m=0;m<6;m++)
			{
				int x1=(int)(Math.random()*40);
				out1.writeInt(x1);
				if(m%2==0)
				{
					try
					{
						Thread.sleep(100);
					}
					catch(InterruptedException e)
					{
					}
				}
			}
			out1.writeInt(-1);
			socket1.close();
		}
		catch(IOException e)
		{
		}
		y=y+1;
		}
		try
		{
		}
		finally
		{
			System.out.println("THANK YOU FOR CHOOSING LOTTERY TICKETS");
		}
		}
}
