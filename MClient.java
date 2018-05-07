
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MClient extends SocketAgent {
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader consoleIn;
    private boolean replay;
    public static void main(String[] args) throws UnknownHostException, IOException {
        MClient client = new MClient();
        client.run();
    }

    public void run() throws IOException {
        
        Socket s = new Socket(SERVER_ADDRESS, PORT_NUMBER);
        consoleIn  = new BufferedReader(new InputStreamReader(System.in));
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
        sendingPlayerName();
        while(true){
            waitToJoinGame();
            gettingX();
            waitForSecretCode();
            System.out.println("Start guessing...");
            guesssing();
            waitForToEveryoneFinish();
            readingRanking();
            replayPrompt();
            if(!replay){
                return;
            }
            System.out.println("test reach");
        }
   
    }
    private void waitForToEveryoneFinish() throws IOException{
        String line;
        while(true){
            line = in.readLine();
            if(line.equals(EVERYONE_FINISHED)){
                System.out.println("Everyone has finished their game");
                return;
            }
            try{
                Thread.sleep(SLEEP_MILLISECOND);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }   
    }
    private void readingRanking() throws IOException{
        String line;
        while(true){
            line =in.readLine();
            if(line!= null){
                if(line.equals(FINAL_RANKING)){
                    System.out.println(in.readLine());
                    System.out.println(in.readLine());
                    int count = 0;
                    while(count<PLAYER_COUNT){
                        System.out.println(in.readLine());
                        count++;
                    }
                    System.out.println(in.readLine());
                    return;
                }
            }
            
        }
    }

    private void replayPrompt() throws IOException{
        String  line;
        while(true){
            line = in.readLine();
            if (line!=null && line.equals(REPLAY_PROMPT)){
                sendingReplayPromptFromConsole();
                return;
            }
        }
    }
    private void sendingReplayPromptFromConsole() throws IOException{
        String line;
        while(true){
            System.out.println("Enter p to replay, q to quit: ");
            if((line = consoleIn.readLine())!=null){
                if(line.equalsIgnoreCase("p")){
                    replay = true;
                    out.println(REPLAY);
                    System.out.println("Re-entered queue");
                    return;
                }
                if(line.equalsIgnoreCase("q")){
                    out.println(QUIT);
                    replay = false;
                    System.out.println("Quitting");
                    return;
                }
            }
        }
    }
    private void guesssing() throws IOException {
        String line;
        while (true) {
            line = in.readLine();
            if (line.equals(SUBMIT_GUESS)) {
                System.out.println("Please enter your unique guess code:");
                out.println(getGuessCodeString());
                ;
            }
            if (line.equals(FINISH_PLAYING)) {
                System.out.println("Finish game");
                System.out.println(in.readLine());
                return;
            }
            if (line.equals(HINT_MESSAGE)) {
                System.out.println(in.readLine());
            }
        }
    }

    // Getting guess code from console's input
    private String getGuessCodeString() throws IOException {
        String guessCodeString = null;
        guessCodeString = null;
        String line = null;
        BufferedReader in = consoleIn;
        while (true) {
            System.out.println("Please enter an unquie number guess code: ");
            if ((line = in.readLine()) != null) {
                if (isNumeric(line) && isUnquie(line)) {
                    guessCodeString = line;
                    System.out.println("Guess code an number entered: " + guessCodeString);
                    return guessCodeString;
                }
                if (line.equals(FORFEIT)) {
                    System.out.println("You choose to forfeit game");
                    guessCodeString = line;
                    return guessCodeString;
                }
            }
        }
    }

    // Starting guess after secret code is generated by server
    private void waitForSecretCode() throws IOException {
        while (true) {
            System.out.println("Waiting for secret code to be generated...");
            String line = in.readLine();
            if (line.equals(SECRET_CODE_GENERATED)) {
                System.out.println("Secret code is successfully generated. Starting game");
                return;
            }
        }

    }

    // getting X from first player
    private void gettingX() throws IOException {
        System.out.println("Getting X");
        while (true) {
            String line = in.readLine();
            if (line.equals(SUBMIT_X)) {
                String x = getXFromConsole();
                System.out.println("Getting X from first player...");
                out.println(getXFromConsole());
            }
            if (line.equals(X_ACCEPTED)) {
                System.out.println("Server accepted X");
                return;
            }
        }
    }

    private void waitToJoinGame() throws IOException {
        while (true) {
            String line = in.readLine();
            if (line.equals(JOIN_GAME)) {
                System.out.println("Joinning game... ");
                return;
            }
        }
    }

    private void sendingPlayerName() throws IOException {
        while (true) {
            String line = in.readLine();
            if (line.equals(SUBMIT_NAME)) {
                System.out.println("getNameFromConsole()");
                out.println(getNameFromConsole());
            }
            if (line.equals(NAME_ACCEPTED)) {
                System.out.println("Name accepted");
                break;
            }
        }
    }

    private String getNameFromConsole() throws IOException {
        System.out.println("Please enter a unquie player name:");
        String playerName = null;
        BufferedReader reader = consoleIn;
        while (true) {
            String line = reader.readLine();
            if (line != null && line.trim().length() > 0) {
                playerName = line;
                System.out.println("Name: " + playerName);
                break;
            }
        }
        return playerName;
    }

    public  String getXFromConsole() throws IOException {
        System.out.println("You are the first player. \nPlease enter a X integer from 3-8:");
        String xString = null;
        BufferedReader reader = consoleIn;
        String line = null;
        while (true) {
            if ((line = reader.readLine()) != null) {
                xString = line;
                break;
            }
        }
        return xString;
    }
}