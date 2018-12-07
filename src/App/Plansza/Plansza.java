package App.Plansza;

import java.util.ArrayList;
import App.InstancjaGry;

    public abstract class Plansza {
        public abstract ArrayList<PlanszaPola> constructBoard(InstancjaGry instancjaGry, int numberOfPlayers);
    }



