package src.Automato;
import java.util.Scanner;

public class Automato {

    private static final int STATE_1 = 1;
    private static final int STATE_2 = 2;
    private static final int STATE_3 = 3;
    private static final int STATE_4 = 4;
    private static final int STATE_5 = 5;
    private static final int STATE_6 = 6;
    private static final int STATE_7 = 7;
    private static final int STATE_8 = 8;
    private static final int STATE_9 = 9;
    private static final int STATE_10 = 10;
    private static final int STATE_11 = 11;
    private static final int STATE_12 = 12;
    private static final int STATE_13 = 13;

    private static int transition(int currentState, char input) {
        switch (currentState) {
            case STATE_1:
                if (input == 'i') return STATE_2;
                if ((input >= 'a' && input <= 'h') || (input >= 'j' && input <= 'z')) return STATE_4;
                if (input == '*') return STATE_5;
                if (Character.isDigit(input)) return STATE_7;
                if (input == '-') return STATE_9;
                if (Character.isWhitespace(input)) return STATE_12;
                return STATE_13;
            case STATE_2:
                if (input == 'f') return STATE_3;
                if ((input >= 'a' && input <= 'e') || (input >= 'g' && input <= 'z') || Character.isDigit(input))
                    return STATE_4;
                return STATE_13;
            case STATE_3:
                if (Character.isDigit(input) || (input >= 'a' && input <= 'z')) return STATE_4;
                return STATE_13;
            case STATE_4:
                if (Character.isDigit(input) || (input >= 'a' && input <= 'z')) return STATE_4;
                return STATE_13;
            case STATE_5:
                if (Character.isDigit(input)) return STATE_6;
                return STATE_13;
            case STATE_6:
                if (Character.isDigit(input)) return STATE_6;
                return STATE_13;
            case STATE_7:
                if (Character.isDigit(input)) return STATE_7;
                if (input == '.') return STATE_8;
                return STATE_13;
            case STATE_8:
                if (Character.isDigit(input)) return STATE_8;
                return STATE_13;
            case STATE_9:
                if (input == '-') return STATE_10;
                if ((input >= 'a' && input <= 'z')) return STATE_10;
                if (input == '\n') return STATE_11;
                return STATE_13;
            case STATE_10:
                if ((input >= 'a' && input <= 'z')) return STATE_10;
                if (input == '\n') return STATE_11;
                return STATE_13;
            case STATE_11:
                if (Character.isWhitespace(input)) return STATE_12;
                return STATE_13;
            case STATE_12:
                if (Character.isWhitespace(input)) return STATE_12;
                return STATE_13;
            default:
                return STATE_13; // Estado de erro
        }
    }

    public static String processInput(String inputSequence) {
        int currentState = STATE_1;
        for (char input : inputSequence.toCharArray()) {
            currentState = transition(currentState, input);
        }

        switch (currentState) {
            case STATE_2:
                return "ID";
            case STATE_3:
                return "IF";
            case STATE_4:
                return "ID";
            case STATE_5:
                return "ERROR";
            case STATE_6:
                return "REAL";
            case STATE_7:
                return "NUM";
            case STATE_8:
                return "REAL";
            case STATE_11:
                return "COMMENT";
            case STATE_12:
                return "WHITE SPACE";
            case STATE_13:
            default:
                return "ERROR";
        }
    }

    public static void main(String[] args) {
    System.out.println("Por favor, insira uma sequência de entrada: ");
            Scanner scan = new Scanner(System.in);

            String input = scan.nextLine();
            String result = processInput(input);
            System.out.println("Tipo resultante: " + result);

            scan.close();
    }
}
