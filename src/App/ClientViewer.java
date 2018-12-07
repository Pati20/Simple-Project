package App;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;


public class ClientViewer extends Thread {

    // Zmienne odpowiedzialne za komunikację z serwerem
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String input = "";
    List<String> comand = new ArrayList<>();

    //Zmienne odpowiedzialne za przebieg gry
    int clientID = 0;
    int playerID = 0;
    int numberOfHuman;
    int numberOfBots;
    int currentPlayerTurn;
    int numberOfPlayers;
    int witchBoardOnServer;
    boolean host;
    String address = new String();
    public Boolean activityOfClient = true;

    //reference to client
    ClientApp clientapp;


    public ClientViewer(ClientApp clientapp, int nnubmerOfHuman, int nnumberOfBots, boolean hhost, String address) {
        numberOfBots = nnumberOfBots;
        numberOfHuman = nnubmerOfHuman;
        this.address = address;
        System.out.println(nnubmerOfHuman + " " + nnumberOfBots);
        host = hhost;
        this.clientapp = clientapp;
        System.out.println("Client start");
        this.SocketListener();

    }

    public void threadEnd() {
        activityOfClient = false;
    }

    /**
     * Metoda odopwiedzialna za łączenie się z serwerem
     */
    public void SocketListener() {

        try {
            socket = new Socket(address, 9999);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
        } catch (IOException e) {
            System.out.println("No I/O");
        }
        System.out.println("Client socket " + socket.getLocalPort());
        conect();
    }

    public void conect() {
        out.println("CONECT");
        System.out.println("clientID " + clientID + " conect ");
        try {
            while (input.equals("")) {
                input = in.readLine();
            }
            //CLIENTID
            comand = parserOfCommand(input);
            if (clientID == 0) {
                clientID = parseInt(comand.get(0));
                System.out.println("client clientID " + clientID);
                socket = new Socket(address, parseInt(comand.get(1)));
                System.out.println("Client client id " + comand.get(0) + "  port  " + parseInt(comand.get(1)) + " ustawiono " + socket.getLocalPort());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (host) {
            begin();

        } else {
            getGames();

        }
    }


    public void begin() {
        System.out.println("BEGIN CLIENT");
        out.println("BEGIN" + " " + clientID + " " + numberOfHuman + " " + numberOfBots);
        try {
            input = "";
            while (input.equals("")) {
                input = in.readLine();
                if (input.equals("")) continue;
                //CLIENTID + BOARD_ON_SERVER_ID + COLOR
                comand = parserOfCommand(input);
                if (parseInt(comand.get(0)) == clientID) {
                    witchBoardOnServer = parseInt(comand.get(1));
                    //color = colorClass.color(parseInt(comand.get(2)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("START " + clientID);
        this.start();
    }


    public void run() {
        System.out.println("WAITNING ON START");
        try {
            input = "";
            while (input.equals("") && activityOfClient) {
                input = "";
                input = in.readLine();
                System.out.println(input);
                comand = parserOfCommand(input);
                System.out.println(comand);
                if ((comand.get(0).equals("START"))) {
                    System.out.println("CLIENT START " + clientID + " : " + input);
                    numberOfHuman = parseInt(comand.get(1));
                    numberOfBots = parseInt(comand.get(2));
                    clientapp.startLocalGame(playerID, numberOfHuman + numberOfBots);
                    game();
                    break;
                } else {

                    input = "";
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    ///////////////////////////////////////////XROBIC ZROBIC///////////////////////////////////////////////////////////////
    public void getGames() {
        out.println("GETGAMES");
        int conectBoardID = 0;
        int cclientIDOnBoard = 0;
        try {
            input = "";
            while (input.equals("") && activityOfClient) {
                input = in.readLine();
                comand = parserOfCommand(input); //BOARD_ID + NUMBER_OF._CURRENT_PLAYERS + FINAL_NUMBER_OF_PLAYERS + NUMBER_OF_BOTS
                if ((comand.size() > 3))
                    if ((comand.get(0).equals("BOARDS"))) {
                        /*
                         */
                        System.out.println("get games " + input);
                        conectBoardID = parseInt(comand.get(1));
                        cclientIDOnBoard = parseInt(comand.get(2)) + parseInt(comand.get(4));
                        conectToGame(conectBoardID, cclientIDOnBoard); //cclientIDOnBoard = numberOfBots + numberOfConectedPlayers
                        break;
                    } else {
                        input = "";
                    }
                else {
                    activityOfClient = false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void conectToGame(int conectBoardID, int cclientIDOnBoard) {
        this.playerID = cclientIDOnBoard;
        this.witchBoardOnServer = conectBoardID;
        System.out.println("conect to " + witchBoardOnServer + " " + playerID);
        out.println("CONECTTOGAME " + clientID + " " + conectBoardID);

        this.start();

    }

    public void terminateServer() {
        out.println("TERMINATE");
    }


    public void game() {
       //////////////////////napisać/////////////////////////////////////////

    }


    public List parserOfCommand(String line) {
        List<String> list = new ArrayList<>();
        while (line != "") {
            if (line.indexOf(" ") != -1) {
                list.add(line.substring(0, line.indexOf(" ")));
                line = line.substring(line.indexOf(" ") + 1);
            } else {
                list.add(line);
                line = "";
            }
        }
        return list;
    }

}