package App;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static javafx.geometry.Pos.CENTER;

public class ClientApp extends Application {

    // GameInstance gameInstance; - klasa w produkcji jeszcze
    private Stage mainWindow;
    private Scene startScene, gameScene, waitScene, playersWaitScene, winScene;
    private CheckBox checkbox1[] = new CheckBox[6];
    private CheckBox checkbox2[] = new CheckBox[6];
    private int players,boot;
    //   public ClientCommunicator clientCommunicator;
    TextField addressField;


    public static void main(String[] args) {
        launch(args);
    }

    public boolean checkPlayersNumber(Integer numberOfHuman, Integer numberOfBots) {
        int sum = numberOfHuman + numberOfBots;
        if (sum == 2 || sum == 3 || sum == 4 || sum == 6) {
            //startGame(numberOfHuman.intValue(), numberOfBots.intValue());
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setResizable(false);
            alert.setTitle("Ostrzeżenie");
            alert.setHeaderText("Nieprawidłowa ilość graczy");
            alert.setContentText("Podane wartości nie są zgodne z zasadami. \n Gra jest przeznaczona dla 2,3,4 lub 6 graczy.");
            alert.show();
            return false;
        }
    }

    void setCheckBoxNotSelected(int number, int table) {
        CheckBox temp[];
        if (table == 1) {
            temp = checkbox1;
            setPlayers(number);
        } else {
            temp = checkbox2;
            setBoot(number);
        }
        for (int i = 0; i < 6; i++) {
            if (i == number - 1) continue;
            temp[i].setSelected(false);

        }
        System.out.println(number);
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public void setBoot(int boot) {
        this.boot = boot;
    }

    public int getPlayers() {
        return players;
    }

    public int getBoot() {
        return boot;
    }


    //dopracować
//    public void startGame(int numberOfHuman, int numberOfBots) {
//        //startLocalGame(playerID, numberOfHuman+numberOfBots);
//        startPlayersWaiting();
//        clientCommunicator = new ClientCommunicator(this, numberOfHuman, numberOfBots, true, addressField.getText());
//    }
//
//    public void joinGame() {
//        clientCommunicator = new ClientCommunicator(this, 0, 0, false,addressField.getText());
//    }

    void startLocalGame(int playerID, int numberOfPlayers) {
        //    gameInstance = new GameInstance(this, playerID, numberOfPlayers);
        Platform.runLater(new Runnable() {
            public void run() {
                //   gameScene = new Scene(gameInstance.vbox, 500, 544);
                mainWindow.setScene(waitScene);
                mainWindow.setMinHeight(634);
                mainWindow.setMinWidth(600);
                mainWindow.setMaxHeight(834);
                mainWindow.setMaxWidth(800);
                System.out.println("abcd");
            }
        });
    }

    public void startPlayersWaiting() {
        Platform.runLater(new Runnable() {
            public void run() {
                mainWindow.setScene(playersWaitScene);
            }
        });
    }

    public void startWaiting() {
        Platform.runLater(new Runnable() {
            public void run() {
                mainWindow.setScene(waitScene);
            }
        });
    }

    public void stopWaiting() {
        Platform.runLater(new Runnable() {
            public void run() {
                mainWindow.setScene(gameScene);
            }
        });
    }

    public void winScreen() {
        Platform.runLater(new Runnable() {
            public void run() {
                mainWindow.setScene(gameScene);
            }
        });
    }

    public boolean onExit() {

//        try {
//            clientCommunicator.activityOfClient=false;
//            clientCommunicator.terminateServer();
//            clientCommunicator.interrupt();
//        } catch (Exception e) {
//            System.out.println("Ten klient nie rozpoczal komunikacji z serwerem");
//            return true;
//        }
//        Platform.exit();
        return false;
    }

    @Override
    public void start(Stage primaryStage) {
        mainWindow = primaryStage;
        mainWindow.setTitle("Chińskie warcaby ");
        mainWindow.setOnCloseRequest(e -> onExit());

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(30));

        Label label1 = new Label("Liczba graczy");
        Label label2 = new Label("Liczba botów");

        for (int i = 0; i < 6;i++) {
            int j = i + 1;
            checkbox1[i] = new CheckBox("" + (i + 1));
            checkbox1[i].setOnMouseClicked(event -> setCheckBoxNotSelected(j, 1));
            checkbox2[i] = new CheckBox("" + (i + 1));
            checkbox2[i].setOnMouseClicked(event -> setCheckBoxNotSelected(j, 2));
        }
        HBox hboxOfPlayers = new HBox(8);
        hboxOfPlayers.getChildren().addAll(checkbox1);
        HBox hboxOfBoot = new HBox(8);
        hboxOfBoot.getChildren().addAll(checkbox2);

        Button button1 = new Button("Utwórz nową grę");
        button1.setOnMouseClicked(event -> checkPlayersNumber(getPlayers(),getBoot()));

        Label label3 = new Label("Adres serwera");
        addressField = new TextField ();
        addressField.setText("localhost");

        Button button2 = new Button("Dołącz do trwającej gry");
        // button2.setOnMouseClicked(event -> joinGame());

        vbox.getChildren().addAll(label3, addressField,new Separator(), label1,hboxOfPlayers, label2, hboxOfBoot, button1, new Separator(), button2);

        Label playersWaitLabel = new Label("trwa\ndołączanie\ngraczy");
        playersWaitLabel.setFont(Font.font("Verdana", 30));
        playersWaitLabel.setTextAlignment(TextAlignment.CENTER);
        playersWaitLabel.setAlignment(CENTER);
        playersWaitScene = new Scene(playersWaitLabel, 300, 450);

        Label waitLabel = new Label("Trwa oczekiwanie \nna pozostałych graczy");
        waitLabel.setFont(Font.font("Verdana", 30));
        waitLabel.setTextAlignment(TextAlignment.CENTER);
        waitLabel.setAlignment(CENTER);
        waitScene = new Scene(waitLabel, 500, 544);

        Label winLabel = new Label("Gratulacje, udało się");
        winLabel.setFont(Font.font("Verdana", 30));
        winLabel.setTextAlignment(TextAlignment.CENTER);
        winLabel.setAlignment(CENTER);
        winScene = new Scene(winLabel, 500, 544);

        startScene = new Scene(vbox, 300, 450);
        mainWindow.setScene(startScene);
        mainWindow.setMinHeight(450);
        mainWindow.setMinWidth(300);
        mainWindow.setMaxHeight(450);
        mainWindow.setMaxWidth(300);
        mainWindow.show();
    }

}
