package src.Interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {
    private List<String[]> tokens;

    public Interpreter() {
        tokens = new ArrayList<>();
    }

    public void analiseLexica(String expressao) {
        tokens.clear();

        Matcher matcher = Pattern.compile("\\bif\\b|[a-hj-zA-Z]|i(?![a-zA-Z])|f(?![a-zA-Z])|[g]|\\d+|[{}()]|;")
                .matcher(expressao);
        while (matcher.find()) {
            String token = matcher.group();
            String tipo;
            if (token.matches("[a-hj-zA-Z]|i(?![a-zA-Z])|f(?![a-zA-Z])|[g]")) {
                tipo = "ID";
            } else if (token.matches("\\bif\\b")) {
                tipo = "IF";
            } else if (token.matches("\\d+")) {
                tipo = "Num";
            } else {
                tipo = "Delimitador";
            }
            tokens.add(new String[]{token, tipo});
        }
    }

    public void exibirTokens() {
        System.out.println("Tokens gerados:");
        for (String[] token : tokens) {
            System.out.println(token[0] + " : " + token[1]);
        }
    }

    public boolean analiseSintatica(String expressao) {
        Stack<Character> pilha = new Stack<>();
        boolean dentroDoIf = false;

        for (int i = 0; i < expressao.length(); i++) {
            char caractere = expressao.charAt(i);

            if (caractere == '(') {
                pilha.push(caractere);
            } else if (caractere == ')') {
                if (pilha.isEmpty() || pilha.pop() != '(') {
                    return false;
                }
            } else if (caractere == 'i' && i + 2 < expressao.length() && expressao.substring(i, i + 2).equals("if")) {
                dentroDoIf = true;
            } else if (dentroDoIf && caractere == '{') {
                pilha.push(caractere);
            } else if (dentroDoIf && caractere == '}') {
                if (pilha.isEmpty() || pilha.pop() != '{') {
                    return false;
                }
                dentroDoIf = false;
            }
        }

        return pilha.isEmpty();
    }

    public boolean analiseSemantica(String expressao) {
        int indexIf = expressao.indexOf("if");
        int indexOpenParenthesis = expressao.indexOf("(", indexIf);
        int indexCloseParenthesis = expressao.indexOf(")", indexOpenParenthesis);
        int indexOpenBrace = expressao.indexOf("{", indexCloseParenthesis);

        if (indexIf == -1 || indexOpenParenthesis == -1 || indexCloseParenthesis == -1 || indexOpenBrace == -1) {
            return false;
        }

        String condicao = expressao.substring(indexOpenParenthesis + 1, indexCloseParenthesis).trim();
        if (condicao.isEmpty()) {
            return false;
        }

        int indexCloseBrace = expressao.indexOf("}", indexOpenBrace + 1);
        if (indexCloseBrace == -1) {
            return false;
        }

        return true;
    }

    public void exibirMenu(String expressao) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Análise Léxica");
            System.out.println("2 - Análise Sintática");
            System.out.println("3 - Análise Semântica");
            System.out.println("4 - Executar todas as análises");
            System.out.println("5 - Sair");

            int escolha = scanner.nextInt();
            scanner.nextLine();

            switch (escolha) {
                case 1:
                    analiseLexica(expressao);
                    exibirTokens();
                    break;
                case 2:
                    boolean sintatica = analiseSintatica(expressao);
                    if (sintatica) {
                        System.out.println("Análise sintática aprovada!");
                    } else {
                        System.out.println("Erro na análise sintática.");
                    }
                    break;
                case 3:
                    boolean semantica = analiseSemantica(expressao);
                    if (semantica) {
                        System.out.println("Análise semântica aprovada!");
                    } else {
                        System.out.println("Erro na análise semântica.");
                    }
                    break;
                case 4:
                    analiseLexica(expressao);
                    exibirTokens();
                    boolean sintaticaCompleta = analiseSintatica(expressao);
                    boolean semanticaCompleta = analiseSemantica(expressao);
                    if (sintaticaCompleta && semanticaCompleta) {
                        System.out.println("Análise sintática aprovada!");
                        System.out.println("Análise semântica aprovada!");
                    } else {
                        if (!sintaticaCompleta) {
                            System.out.println("Erro na análise sintática.");
                        } else {
                            System.out.println("Análise sintática aprovada!");
                        }
                        if (!semanticaCompleta) {
                            System.out.println("Erro na análise semântica.");
                        } else {
                            System.out.println("Análise semântica aprovada!");
                        }
                    }
                    break;
                case 5:
                    System.out.println("Saindo do programa...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Opção inválida. Escolha novamente.");
                    break;
            }
        }
    }

    public String lerArquivo(String nomeArquivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
            String linha;
            StringBuilder expressao = new StringBuilder();

            while ((linha = br.readLine()) != null) {
                expressao.append(linha);
            }

            br.close();

            return expressao.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Interpreter interpretador = new Interpreter();
        String expressao = interpretador.lerArquivo("fonte.txt");

        if (expressao != null) {
            interpretador.exibirMenu(expressao);
        } else {
            System.out.println("Erro ao ler o arquivo fonte.txt.");
        }
    }
}
