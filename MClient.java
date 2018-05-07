
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

    public static void main(String[] args) throws UnknownHostException, IOException {
        MClient client = new MClient();
        client.run();
    }

    public void run() throws IOException {
        Socket s = new Socket(SERVER_ADDRESS, PORT_NUMBER);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
        sendingPlayerName();
        waitToJoinGame();
        gettingX();
        waitForSecretCode();
        System.out.println("Start guessing...");
        guesssing();

    }

    // //making, sending guesses and getting responses from server until winning or
    // lose
    // public static void guessing(Socket s) {
    // int count;
    // for( count = 0; count<10; count++){
    // String guess = getGuessCodeString();
    // sendMessage(s, guess);
    // String message = null;
    // if((message = readMessage(s))!=null){
    // if (message.contains(WIN_MESSAGE)|| message.contains(LOSE_MESSAGE)){
    // break;
    // }
    // }
    // }
    // if(count == 10){
    // readMessage(s);
    // }
    // }
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
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
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
        InputStream in = System.in;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
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

    public static String getXFromConsole() throws IOException {
        System.out.println("You are the first player. \nPlease enter a X integer from 3-8:");
        String xString = null;
        InputStream in = System.in;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
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