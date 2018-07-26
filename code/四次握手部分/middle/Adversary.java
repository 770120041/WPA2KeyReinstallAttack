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
    public Network1(BufferedReader is) {
        this.is = is;
    }
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
    public boolean receive(){
        if(flag == 1){
            return true;
        }
        return false;
    }
    public String msg(){
        return rec;
    }
}

class Network2 extends Thread{
    public static String s="";
    public static int r=9527;
    String masterkey="z3dg35dg";
    String ANounce="";
    String CNounce="";
    String RAndom = "";
    public BufferedReader is,APSource;
    public PrintWriter  os ,APOut;
    Socket socket = null;
    Socket ssocket = null;
    int Nonce=0;
    public Network2() {
 
    }
    public Network2(Socket ssocket1) {
        this.ssocket = ssocket1;
    }
   
    public void sen(String s) {
        System.out.println("Sending to Client : "+s);
        os.println(s);
        os.flush();
    }
    public void senAP(String s) {
        System.out.println("Sending to AP: "+s);
        APOut.println(s);
        APOut.flush();
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

    //输入
    public String inS() {
        String s;
        try {
            s = is.readLine();

            System.out.println("Middleman Receiving : "+s);
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
        Network1 net1=new Network1(is);
        // net1.is=is;
        net1.start();
        while (net1.flag==0) {
            //sen("hello???");
               
            
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
            

            socket = new Socket("localhost",4417);//访问AP
            APSource=new BufferedReader(new InputStreamReader(socket.getInputStream()));      
            APOut=new PrintWriter(socket.getOutputStream());
            System.out.println("中间人和AP连接创建！");

            String clientMsg = null;
            String APMsg = null;

            Network1 clientNet=new Network1(is);;
            Network1 APNet= new Network1(APSource);;
            boolean receiveClient = true;
            boolean receiveAP = true;
            int state = 0;
            int sameNounceCounter = 0;
            while(true){
                //每次收到一个信息以后，重新开启一个线程
                if(receiveClient){
                    clientNet=new Network1(is);
                    clientNet.start();
                    receiveClient = false;
                    if(clientNet.receive()){
                        clientNet.flag = 0;
                    }
                }
                if(receiveAP){
                    APNet = new Network1(APSource);
                    APNet.start();
                    receiveAP = false;
                    if(APNet.receive()){
                        APNet.flag = 0;
                    }
                }
                while(clientNet.receive() == false  && APNet.receive() == false ){
                    // System.out.print(".");
                    this.sleep(10);
                }
                if(APNet.receive()){
                    APMsg = APNet.msg();
                    if(APMsg != null){
                       System.out.println("Receiving from AP:"+APMsg);
                       
                    }
                    if(state == 5){
                        String[] s1=APMsg.split(" ");
                        System.out.println("AP 再次发送给 Client Msg3");
                        sen(APMsg);
                        Nonce =0;
                        state = 6;
                    }

                    if(state == 3){
                        String[] s1=APMsg.split(" ");
                        if(s1[0].equals("ACK")){
                            System.out.println("中间人从AP获取到Msg3");                               
                        }
                        sen(APMsg);//给Client Msg3
                        state = 4;
                    }
                    if(state == 1){
                        String[] s1=APMsg.split(" ");
                        ANounce=s1[0];
                        RAndom = s1[1];
                        System.out.println("中间人从AP获得ANounce:"+ANounce+",和Random:"+RAndom);
                        ccmp.ANounce=ANounce;

                        state = 2;
                        sen(APMsg);//给Client Anounce和Random
                    }






                     APMsg = null;
                     receiveAP = true;
                     // clientNet.flagBack();
                }









                if(clientNet.receive()){
                     clientMsg = clientNet.msg();
                     if(clientMsg!=null){
                         System.out.println("Receiving from client:"+clientMsg);
                         if(state == 6){
                            String[] s1=clientMsg.split(" ");
                            if(s1[0].equals("ACK") == false && s1[0].equals("OUTPUTCOMPLETE") == false && s1[0] != null){
                                if(sameNounceCounter>0){
                                    System.out.println("这是Nounce被重置以后发送信息密文和明文");
                                    sameNounceCounter--;
                                }
                                System.out.println("nounce is :"+Nonce);
                                // System.out.println("Normal receiving : "+(ccmp.xo(clientMsg, Nonce)));
                                System.out.println("Plain text : "+enc(ccmp.xo(clientMsg, Nonce)));
                                Nonce++;
                            }
                            senAP(clientMsg);
                         }
                         if(state == 5){
                            String[] s1=clientMsg.split(" ");
                            System.out.println("获取到Msg4丢失的消息内容");
                            senAP(clientMsg);
                            if(s1[0].equals("ACK") == false && clientMsg!=null){
                                System.out.println("nounce is :"+Nonce);
                                System.out.println("Plain text : "+enc(ccmp.xo(clientMsg, Nonce)));
                                Nonce++;
                            }
                            sameNounceCounter ++;
                         }
                         if(state == 4){
                            String[] s1=clientMsg.split(" ");
                            //不发送给AP MSG4 一次
                            System.out.println("中间人从Client获取到MSg4,截取不发送给AP");
                            // senAP(clientMsg);
                            state =5;
                         }

                         if(state == 2){
                            String[] s1=clientMsg.split(" ");
                            CNounce = s1[0];
                            System.out.println("中间人从Client获得CNounce:"+CNounce);
                            ccmp.CNounce=CNounce;
                            state = 3;
                            senAP(clientMsg);      //发送给AP CNOUNE RANDOM
                         }
                        


                         if(clientMsg.equals("REQ")){
                            System.out.println("中间人发现认证开始");
                            state = 1; //change state to detect authenticate
                            senAP(clientMsg);      //发送给AP REQ
                         }


                                           
                     }
                     clientMsg = null;
                     receiveClient = true;
                     // clientNet.flagBack();
                }
                
                
            }

            
            // os.close(); //关闭Socket输出流
            // is.close(); //关闭Socket输入流
            // APSource.close();
            // APOut.close();
            // socket.close();
            // ssocket.close(); //关闭Socket
        }catch(Exception e){
          System.out.println("Error:"+e);
          System.out.println("2");
        }
    }
}

public class Adversary {
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
            ServerSocket serverSocket=new ServerSocket(4416);
            Socket socket=null;           

            System.out.println("***中间人，等待客户端的连接***");
            while(true){
                socket=serverSocket.accept();
                All.num++;
                All.sum++;
                All.net[count]=new Network2(socket);
                System.out.println("客户端："+count+" socket= "+socket.getPort());
                InetAddress address=socket.getInetAddress();
                System.out.println("当前客户端的 IP："+address.getHostAddress());
                All.net[count].start();
                count++;//统计客户端的数量
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("1");
        }

        
    }

}

