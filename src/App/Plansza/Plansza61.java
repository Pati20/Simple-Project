package App.Plansza;

import java.util.ArrayList;
import App.InstancjaGry;


public class Plansza61 extends Plansza {

    private ArrayList<PlanszaPola> planszaPola = new ArrayList<>();

    public ArrayList<PlanszaPola> constructBoard(InstancjaGry instancjaGry, int numberOfPlayers) {
        switch (numberOfPlayers) {
            case 2:
                setPawns(instancjaGry, 1, 0, 0, 2, 0, 0);
                break;
            case 3:
                setPawns(instancjaGry, 1, 0, 2, 0, 3, 0);
                break;
            case 4:
                setPawns(instancjaGry, 0, 1, 2, 0, 3, 4);
                break;
            case 6:
                setPawns(instancjaGry, 1, 2, 3, 4, 5, 6);
                break;
            default:
                //System.out.println("błąd!");
                break;
        }
        return planszaPola;
    }

    void setPawns(InstancjaGry instancjaGry , int corn1, int corn2, int corn3, int corn4, int corn5, int corn6) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j <= i; j++) {
                planszaPola.add(new PlanszaPola(instancjaGry, corn1, corn4, (12 + 2 * j - i), i));
            }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4 - i; j++) {
                planszaPola.add(new PlanszaPola(instancjaGry, corn6, corn3, (2 * j + i), 4 + i));
            }
            for (int j = 0; j < 5 + i; j++) {
                planszaPola.add(new PlanszaPola(instancjaGry, 0, 0, (8 + 2 * j - i), 4 + i));
            }
            for (int j = 0; j < 4 - i; j++) {
                planszaPola.add(new PlanszaPola(instancjaGry, corn2, corn5, (18 + 2 * j + i), 4 + i));
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < i + 1; j++) {
                planszaPola.add(new PlanszaPola(instancjaGry, corn5, corn2, (3 + 2 * j - i), 9 + i));
            }
            for (int j = 0; j < 8 - i; j++) {
                planszaPola.add(new PlanszaPola(instancjaGry, 0, 0, (5 + 2 * j + i), 9 + i));
            }
            for (int j = 0; j < i + 1; j++) {
                planszaPola.add(new PlanszaPola(instancjaGry, corn3, corn6, (21 + 2 * j - i), 9 + i));
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4 - i; j++) {
                planszaPola.add(new PlanszaPola(instancjaGry, corn4, corn1, (9 + 2 * j + i), i + 13));
            }
        }
    }
}
