package App;

import App.Plansza.PlanszaFabryka;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import App.Plansza.Plansza;
import App.Plansza.PlanszaPola;

import java.util.ArrayList;
import java.util.List;

import static javafx.geometry.Pos.CENTER;


import static java.lang.Math.abs;

public class InstancjaGry {
    //declaration of board elements
    public Color[] playerColors = {Color.LIGHTGRAY, Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.ORANGE};
    public ArrayList<PlanszaPola> boardFields;
    public PlanszaPola selectedPawn = null;
    public PlanszaPola movedPawn;
    public Boolean activityOfBoard = false;
    public List<String> moveRegister = new ArrayList<>();
    public Plansza board;
    int playerID;

    //declaration of GUI elements
    GridPane gridpane;
    VBox vbox;
    ClientApp clientapp;

    public InstancjaGry(ClientApp clientapp, int playerID, int numberOfPlayers) {
        this.clientapp = clientapp;
        this.playerID = playerID + 1;
        boardFields = PlanszaFabryka.createLocalBoard(61).constructBoard(this, numberOfPlayers);
        generateGUI();
    }

    //create fields of board on ArrayList
    public void unlockGame() {
        activityOfBoard = true;
        selectedPawn = null;
        movedPawn = null;
        clientapp.stopWaiting();
        if (checkWin()) lockGame();
        //System.out.println("activity true " + activityOfBoard);
    }

    public void lockGame() {
        activityOfBoard = false;
        clientapp.startWaiting();
        //System.out.println("activity false " + activityOfBoard);
    }

    public Boolean getActivityOfBoard() {
        //System.out.println("activity return " + activityOfBoard);
        return activityOfBoard;
    }

    public Boolean checkWin() {
        for (PlanszaPola field : boardFields) {
            if (field.pawn == playerID && field.winID != playerID) {
                return false;
            }
        }
        clientapp.winScreen();
        return true;
    }
    ///draw buttons and fields of board
    void generateGUI() {

        //create new table in window to put fields
        gridpane = new GridPane();
        gridpane.setAlignment(CENTER);
        for (int i = 0; i < 25; i++) {
            ColumnConstraints column = new ColumnConstraints();
            //full symmetry with MinWidth=17 (multiplier 0.87), but 1-1 aspect ratio looks better
            column.setMinWidth(20);
            column.setPrefWidth(20);
            column.setMaxWidth(30);
            column.setHgrow(Priority.ALWAYS);
            gridpane.getColumnConstraints().add(column);
        }
        for (int i = 0; i < 17; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(30);
            row.setPrefHeight(30);
            row.setMaxHeight(45);
            row.setVgrow(Priority.ALWAYS);
            gridpane.getRowConstraints().add(row);
        }

        //declare positions of fields in gui table
        for (PlanszaPola i : boardFields) {
            GridPane.setConstraints(i, i.col, i.row);
        }

        //add all fields to gui table
        gridpane.getChildren().addAll(boardFields);

        //set margins of whole table
        gridpane.setPadding(new Insets(30));

        //create button bar
        Button buttonEndTurn = new Button("Zakończ turę");
        buttonEndTurn.setOnMouseClicked(event -> lockGame());
        Button buttonHowToPlay = new Button("Jak grać?");
        buttonHowToPlay.setOnMouseClicked(event -> showHowToPlay());
        Button buttonAboutUs = new Button("O programie");
        buttonAboutUs.setOnMouseClicked(event -> showAboutUs());
        Label colorLabel = new Label("Twój kolor: ");
        Circle circle = new Circle();
        circle.setRadius(10);
        circle.setFill(playerColors[playerID]);
        circle.setStroke(Color.GRAY);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStrokeWidth(2);
        ToolBar toolbar = new ToolBar(buttonEndTurn, new Separator(), buttonHowToPlay, buttonAboutUs, new Separator(),colorLabel,circle);


        //this.gameReady = true;
        //create vbox with button bar and table (board)
        vbox = new VBox();
        vbox.getChildren().addAll(toolbar, gridpane);
        vbox.setVgrow(gridpane, Priority.ALWAYS);

    }

    //show information about authors
    void showAboutUs() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("Autorzy programu");
        alert.setContentText("Mikołaj Pietrek, Mateusz Dąbek");
        alert.show();
    }

    //show information about game rules
    void showHowToPlay() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("Skrócona instrukcja");
        alert.setContentText("Celem gry jest ustawienie wszystkich swoich pionów w przeciwległym promieniu. " +
                "Podczas tury można poruszyć się jednym pionem na sąsiadujące pole lub przeskoczyć dowolną ilość innych pionów.");
        alert.show();
    }

    //select or deselect pawn in gui, draw stroke
    public void selectPawn(PlanszaPola pos) {
        if (selectedPawn != null) {
            selectedPawn.setStroke(Color.GRAY);
            selectedPawn.setStrokeWidth(2);
            moveAndSendPawn(selectedPawn, pos);
            selectedPawn = null;
        } else {
            if ((pos.pawn) == playerID) {
                selectedPawn = pos;
                selectedPawn.setStroke(Color.ORANGE);
                selectedPawn.setStrokeWidth(5);
            }
        }
    }

    //move pawn from oldPos to newPos
    public boolean movePawn(PlanszaPola oldPos, PlanszaPola newPos) {
        if (testMove(oldPos, newPos)) {
            newPos.pawn = oldPos.pawn;
            oldPos.pawn = 0;
            newPos.setFill(playerColors[newPos.pawn]);
            oldPos.setFill(playerColors[oldPos.pawn]);
            movedPawn = newPos;
            return true;
        }
        return false;
    }

    //move pawn by server_do_not_use //by Akageneko
    public void movePawnServer(PlanszaPola oldPos, PlanszaPola newPos) {
        newPos.pawn = oldPos.pawn;
        oldPos.pawn = 0;
        newPos.setFill(playerColors[newPos.pawn]);
        oldPos.setFill(playerColors[oldPos.pawn]);
    }

    //move pawn from oldPos to newPos
    public boolean moveAndSendPawn(PlanszaPola oldPos, PlanszaPola newPos) {
        if (testMove(oldPos, newPos)) {
            movePawn(oldPos, newPos);
            moveRegister.add(Integer.toString(oldPos.col));
            moveRegister.add(Integer.toString(oldPos.row));
            moveRegister.add(Integer.toString(newPos.col));
            moveRegister.add(Integer.toString(newPos.row));
            return true;
        }
        return false;
    }

    //return field with specified column and row
    public PlanszaPola findField(int col, int row) {
        for (PlanszaPola field : boardFields) {
            if (field.col == col && field.row == row) {
                return field;
            }
        }
        return null;
    }

    //check correctness of move
    public boolean testMove(PlanszaPola oldPos, PlanszaPola newPos) {
        if (newPos.pawn == 0 && oldPos != newPos) {
            if (movedPawn == null) {
                if ((abs(oldPos.col - newPos.col) <= 2) && (abs(oldPos.row - newPos.row) <= 1)) {
                    return true;
                }
            }
            if (movedPawn == null || movedPawn == oldPos) {
                if (oldPos.row == newPos.row) {
                    //right
                    if (newPos.col == oldPos.col + 4) {
                        if (findField(oldPos.col + 2, oldPos.row).pawn > 0) {
                            return true;
                        }
                    }
                    //left
                    if (newPos.col == oldPos.col - 4) {
                        if (findField(oldPos.col - 2, oldPos.row).pawn > 0) {
                            return true;
                        }
                    }
                }

                if (newPos.row == oldPos.row + 2) {
                    //right up
                    if (newPos.col == oldPos.col + 2) {
                        if (findField(oldPos.col + 1, oldPos.row + 1).pawn > 0) {
                            return true;
                        }
                    }
                    //left up
                    if (newPos.col == oldPos.col - 2) {
                        if (findField(oldPos.col - 1, oldPos.row + 1).pawn > 0) {
                            return true;
                        }
                    }
                }

                if (newPos.row == oldPos.row - 2) {
                    //right down
                    if (newPos.col == oldPos.col + 2) {
                        if (findField(oldPos.col + 1, oldPos.row - 1).pawn > 0) {
                            return true;
                        }
                    }
                    //left down
                    if (newPos.col == oldPos.col - 2) {
                        if (findField(oldPos.col - 1, oldPos.row - 1).pawn > 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
