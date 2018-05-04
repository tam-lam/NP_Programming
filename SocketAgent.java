import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public  abstract class SocketAgent{
    final static String SERVER_ADDRESS = "127.0.0.1";
    final static int PORT_ADDRESS = 1324;
    // Socket cSocket;
    public static void readMessage(Socket cSocket){
        try{
            BufferedReader inMessage = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            String line  = null;
            if((line = inMessage.readLine())!=null){
                System.out.println("Server: "+ line);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void sendMessage(Socket s, String message){
        try{
            PrintWriter out = new PrintWriter(s.getOutputStream(),true);
            out.println(message);
            System.out.println("Message sent: "+ message);
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }

}