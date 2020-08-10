import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.*;
public class TicketServerB
{
	final static int portup=5000;
	public static void main(String args[])
	{
		System.out.println("WELCOME TO TICKET COUNTER B:");
		Buffer<DataPackage>store=new Buffer<DataPackage>(50);
		new Writer(store).start();
		try
		{
			ServerSocket servesock=new ServerSocket(portup);
			while(true)
			{
				Socket socket=servesock.accept();
				new Uploader(socket,store).start();
			}
			}
			catch(IOException e)
		{
		}
		}
}
class Uploader extends Thread
{
	Socket socket;
	Buffer<DataPackage> buffer;
	Uploader(Socket s,Buffer<DataPackage> d)
	{
		socket=s;
		buffer=d;
	}
	public void run()
	{
		try
		{
System.out.println("\nTICKET:");
			DataInputStream in=new DataInputStream(socket.getInputStream());
			int port=in.readInt();
			System.out.print("SellerCode:"+port+"\n");
			DataPackage dp=new DataPackage(port);
			int k=in.readInt();
			while(k!=-1)
			{
				dp.add(k);
System.out.print(k+",");
				k=in.readInt();
			}
			socket.close();
			buffer.put(dp);
		}
		catch(IOException e)
		{
		}
	}
}
class Writer extends Thread
{
	Buffer<DataPackage> buffer;
	Writer(Buffer<DataPackage> d)
	{
		buffer=d;
	}
	public void run()
	{
		while(true)
		{
			DataPackage dp=buffer.get();
			dp.writeClient();
		}
	}
}
final class DataPackage
{
	private final int port;
	private final ArrayList<Integer> data=new ArrayList<Integer>();
	DataPackage(int p)
	{
		port=p;
	}
	void add(Integer k)
	{
		data.add(k);
	}
	synchronized void writeClient()
	{
		try
		{
			Socket socket;
			socket=new Socket(InetAddress.getLocalHost(),port);
			DataOutputStream out=new DataOutputStream(socket.getOutputStream());
			out.writeInt(port);
			for(Integer k:data)
			{
				out.writeInt(k);
			}
			out.flush();
			socket.close();
		}
		catch(IOException e)
		{
		}
	}
}
class Buffer<E>
{
 private int max;
 private int size = 0;
 private ArrayList<E> buffer;
 private Semaphore empty; // control consumer
 private Semaphore full; // control producer
 private Lock lock = new ReentrantLock();
 public Buffer(int s)
 {
 buffer = new ArrayList<E>();
 max = s;
 empty = new Semaphore(0);
 full = new Semaphore(max);
}
 public void put(E x)
 {
 try
 {
 full.acquire();
 }
 catch(InterruptedException e){}
 lock.lock();
 try
 {
 buffer.add(x);
 size++;
 empty.release();
 }
 finally
 {
 lock.unlock();
 }
 }
 public E get()
 {
 try
 {
 empty.acquire();
 }
 catch(InterruptedException e){}
 lock.lock();
 try
 {
 E temp = buffer.get(0);
 buffer.remove(0);
 size--;
 full.release();
 return temp;
 }
 finally
 {
 lock.unlock();
 }
 }
}
