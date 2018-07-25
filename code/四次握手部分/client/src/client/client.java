
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.io.*;
class Network2 extends Thread
{
    Socket socket;
    String s="123";
   
    String ANounce="";
    String CNounce="";
    PrintWriter out;
    int Nonce=0;
    public BufferedReader is;
    public void dataout(String text) {
    	if(text.length()>16) {
    		String s1=text.substring(0, 16);
    		sen(ccmp.xo(s1, Nonce));
    		Nonce++;
    		dataout(text.substring(16));
    	}
    	else {
    		sen(ccmp.xo(text, Nonce));
    		Nonce++;
    	}
    }
    public void sets(String s1){
        s=s1;
    }
    public void sen(String s){
    	System.out.println("Sending :"+s);
      	out.println(s);
      	out.flush(); 
    }
    public void shut() {
    	try {
    		out.close();
    		socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void run()
    {
        try {
        	//================================================================================
        	//此处握手
        	client.flag=1;
            socket = new Socket("localhost",4416);//注意修改ip地址

        	is=new BufferedReader(new InputStreamReader(socket.getInputStream()));    	
            out=new PrintWriter(socket.getOutputStream());
            Scanner in1;
            sen("REQ");
            String s=is.readLine();
            System.out.println("rec : #"+s+"#");
            String[] s1=s.split(" ");
            ANounce=s1[0];
            CNounce=(int)(Math.random()*100000)+"";
            ccmp.ANounce=ANounce;
            ccmp.CNounce=CNounce;
            
            System.out.println("ANounce : "+ANounce+" CNounce = "+CNounce);
            sen(CNounce+" "+s1[1]);
            s=is.readLine();
            String msg2=s; 
            System.out.println("rec : #"+s+"#");
            System.out.print("输入以继续 ,1代表正常工作，2代表丢失msg4: ");
            in1=new Scanner(System.in);
            String s11="1";
            		//in1.nextLine();
            if(s11.equals("1")) {            	
            	sen(s);
            }
            else {
            	for(int j=0;j<Integer.valueOf(s11);j++) {
                	s=is.readLine();
                	System.out.println("Receiving : #"+s+"#");
            	}
                sen(s);
            }
            
                         	
            System.out.println("out : #"+s+"#");



            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("input.txt")));
            String str = null;
            while ((str =in.readLine()) != null) {
               dataout(str);
            }
            sen("OUTPUTCOMPLETE");
            // dataout("OUTPUTCOMPLETE");//加密输出

    		
    		shut();
        } catch (IOException e) {
            System.out.println("Connected fail !");
        }
    }
}
public class client {
    
    public static Network2 network2;
    public static int flag=0;
	public static void down(){
		System.out.println("Shutdown !");
		System.exit(0);
	}
	
	public static void work1() {
		network2 = new Network2();
	    network2.start();
	    flag=1;
	    System.out.println("Connected !");
	}	

	
	public static void main(String args[]) {
		work1(); 		
	}
}


