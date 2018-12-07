package App.Plansza;

public class PlanszaFabryka {

    public static Plansza createLocalBoard(int boardType) {
        switch (boardType) {
            case 61:
                return new Plansza61();
        }
        return new Plansza61();
    }
}
