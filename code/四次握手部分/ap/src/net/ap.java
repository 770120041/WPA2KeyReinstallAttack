import java.io.*;  
import java.nio.*;  
import java.nio.channels.*;  
import java.nio.charset.*;  
import java.net.*;  
import java.util.*; 
import java.io.*;
import java.util.Random;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
class All{
    public static Network2[] net=new Network2[1000];
    public static int num=0,sum=0;
}

class Network1 extends Thread
{
    public String rec="";
    public int flag=0;
    public BufferedReader is;
     public void run()
    {
        try {   
           rec=is.readLine();
           flag=1;
        } catch (IOException e) {
            flag=0;
        }
    }
}

class Network2 extends Thread{
	public static String s="";
	public static int r=9527;
	String masterkey="z3dg35dg";
	String ANounce="";
    String CNounce="";
    public BufferedReader is;
    public PrintWriter  os ;
    Socket ssocket = null;
    int Nonce=0;
    public Network2() {
 
    }
    public Network2(Socket ssocket1) {
        this.ssocket = ssocket1;
    }
   
    public void sen(String s) {
    	System.out.println("sending : "+s);
    	os.println(s);
    	os.flush();
	}
	
	public void stt(){
    	sen("close");
    	os.close(); //关闭Socket输出流
		try {
			is.close();
			System.out.println("Service down");
			ssocket.close(); //关闭Socket
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //关闭Socket输入流
    
    }
	public String inS() {
		String s;
		try {
			s = is.readLine();

			System.out.println("Receiving : "+s);
			return s;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fault";
		}
		
	}
	@SuppressWarnings("static-access")
	public void bui() {
		ANounce=(int)(Math.random()*100000)+"";
		String msg1=ANounce+" "+r;
		sen(msg1);
		s=inS();//获取Cnone和r
		String[] ss=s.split(" ");
		CNounce=ss[0];
		ccmp.ANounce=ANounce;
		ccmp.CNounce=CNounce;
		String msg2=s;
		r++;

        //Step 8 here
		sen("ACK "+r);
		int flag=1;
		int i=0;
		Network1 net1=new Network1();
		net1.is=is;
		net1.start();
    	while (net1.flag==0) {
    		//sen("hello???");
    		try {
				this.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}       
    		
    		i++;
    		if(i>30) {
    			r++;
    			i-=30;
    			sen("ACK "+r);
    		}
    	}
    	System.out.println("Net 1: "+net1.rec);
    	System.out.println("waiting "+i);
    	
    	net1=null;
	}
	public String enc(String s) {
		String s1="";
		for(int i=0;i<16;i++) {
			int k=0;
			for(int j=0;j<8;j++) {
				k=k*2+(int)(s.charAt(i*8+j))-'0';
			}
			s1+=(char)(k);
		}
		return s1;
	}
    public void run()
    {
    	try {
    		is=new BufferedReader(new InputStreamReader(ssocket.getInputStream()));    	
    		os=new PrintWriter(ssocket.getOutputStream());
    		//=========================================================
    		//此处握手
    		String s=inS();
    		if(s.equals("REQ")) {
    			bui();
    		}
    		else {
    			
    		}

    		s=inS();
    		while(s.equals("OUTPUTCOMPLETE") == false) {
				System.out.println("Normal receiving : "+(ccmp.xo(s, Nonce)));
				System.out.println("Trans receiving : "+enc(ccmp.xo(s, Nonce)));
				Nonce++;
				// break;
				s=inS();
			}
        	//=========================================================
        	//此处收发消息
     //   	sen("wokaishile");
       // 	s=inS();
      //  	sen("wojieshu");
        	//=========================================================
    //		All.num--;
    		os.close(); //关闭Socket输出流
    		is.close(); //关闭Socket输入流
    		ssocket.close(); //关闭Socket
        }catch(Exception e){
          System.out.println("Error:"+e);
          System.out.println("2");
        }
    }
}

public class ap {
	public static int count=0;

    public static void stt() {
        for(int i=0;i<=count;i++)
        	if(All.net[count]!=null){
        		All.net[count].sen("close");
        		All.net[count].interrupt();
        		
        	}
        hot();
    }    
    public static void show(String s) {
        System.out.println("showing : "+s);
    }
    public static void hot() {
        System.out.println("***Shut Down!****");
        System.exit(0);
    }
	@SuppressWarnings({ "resource"})
	
	public static void main(String args[]) {
		
		try {
            ServerSocket serverSocket=new ServerSocket(4417);
                       Socket socket=null;           

            System.out.println("***服务器即将启动，等待客户端的连接***");
            while(true){
                socket=serverSocket.accept();
                All.num++;
                All.sum++;
                All.net[count]=new Network2(socket);
                System.out.println("客户端："+count+" socket= "+socket.getPort());
                InetAddress address=socket.getInetAddress();
                System.out.println("当前客户端的IP："+address.getHostAddress());
                All.net[count].start();
                count++;//统计客户端的数量
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("1");
        }

        
	}

}

