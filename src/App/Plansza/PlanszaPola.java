package App.Plansza;

import App.InstancjaGry;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;


public class PlanszaPola extends Circle {
    public int pawn;
    public int col;
    public int row;
    public int winID;
    InstancjaGry localboard;
    private PlanszaPola tempRef;
    private int buffer;

    public PlanszaPola(int id, int winID, int col, int row) {
        pawn = id;

        this.localboard = localboard;
        this.col = col;
        this.row = row;
        this.winID = winID;

        tempRef = this;
    }

    public PlanszaPola(InstancjaGry localboard, int id, int winID, int col, int row) {
        pawn = id;

        this.localboard = localboard;
        this.col = col;
        this.row = row;
        this.winID = winID;

        tempRef = this;

        setFill(localboard.playerColors[pawn]);
        setStroke(Color.ALICEBLUE);
        setStrokeType(StrokeType.INSIDE);
        setStrokeWidth(2);
        setRadius(15);

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                localboard.selectPawn(tempRef);
            }
        });
    }
}
