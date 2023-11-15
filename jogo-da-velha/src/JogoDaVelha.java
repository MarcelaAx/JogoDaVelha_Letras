import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class JogoDaVelha {
    private char[][] tabuleiro;
    private String jogadorUm;
    private String jogadorDois;
    private int tamanhoTabuleiro;

    private String jogadorAtual;


    private void iniciarTabuleiro() {
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            for (int j = 0; j < tamanhoTabuleiro; j++) {
                tabuleiro[i][j] = '-';
            }
        }
    }

    private void imprimirTabuleiro() {
        System.out.println();
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            for (int j = 0; j < tamanhoTabuleiro; j++) {
                System.out.print(tabuleiro[i][j]);
                if (j < (tamanhoTabuleiro - 1)) {
                    System.out.print("|");
                }
            }
            System.out.println();
        }
    }

    private boolean jogadaValida(int linha, int coluna) {
        if (linha >= 0 && linha < tamanhoTabuleiro && coluna >= 0 && coluna < tamanhoTabuleiro && tabuleiro[linha][coluna] == '-') {
            tabuleiro[linha][coluna] = jogadorAtual == jogadorUm ? 'X' : 'O';
            return true;
        }
        return false;
    }

    private boolean verificarVitoria() {
        char jogador = jogadorAtual == jogadorUm ? 'X' : 'O';
        boolean ganhouLinha;
        boolean ganhouColuna;
        boolean ganhouDiagonal = true;
        boolean ganhouDiagonalInversa = true;
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            ganhouColuna = true;
            ganhouLinha = true;
            for (int j = 0; j < tamanhoTabuleiro; j++) {
                if (tabuleiro[i][j] != jogador) {
                    ganhouLinha = false;
                }

                if (i == j && tabuleiro[i][j] != jogador) {
                    ganhouDiagonal = false;
                }

                if (tabuleiro[j][i] != jogador) {
                    ganhouColuna = false;
                }
            }

            if (tabuleiro[i][tamanhoTabuleiro - 1 - i] != jogador){
                ganhouDiagonalInversa = false;
            }

            if (ganhouLinha || ganhouColuna)
                return true;
        }

        return  ganhouDiagonal || ganhouDiagonalInversa;
    }

    private boolean tabuleiroCheio() {
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            for (int j = 0; j < tamanhoTabuleiro; j++) {
                if (tabuleiro[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean jogadaRobo(Random random, FileWriter writer, BufferedWriter bufferWriter) throws IOException {
        int linha;
        int coluna;
        System.out.println("É a vez do robô...");
        do {
            linha = random.nextInt(tamanhoTabuleiro - 1);
            coluna = random.nextInt(tamanhoTabuleiro - 1);
        } while (!jogadaValida(linha, coluna));

        bufferWriter.write("Jogador " + jogadorAtual + " - Coordenadas. Linha " + (linha + 1) + " - Coluna " + (coluna + 1));
        bufferWriter.newLine();
        if (verificarVitoria()) {
            System.out.println("O robô venceu!");
            bufferWriter.write("O robô venceu!");
            return true;
        } else if (tabuleiroCheio()) {
            System.out.println("O jogo empatou!");
            bufferWriter.write("O jogo empatou!");
            return true;
        }

        imprimirTabuleiro();
        return false;
    }

    private boolean jogadaPessoa(Scanner scanner, FileWriter writer, BufferedWriter bufferWriter) throws IOException {
        int coluna;
        int linha;
        System.out.println("Jogador atual: " + jogadorAtual);
        imprimirTabuleiro();

        do {
            System.out.print("Jogador(a) " + jogadorAtual + ", entre com as coordenadas da próxima jogada: ");
            linha = scanner.nextInt() - 1;
            coluna = scanner.nextInt() - 1;
        } while (!jogadaValida(linha, coluna));

        bufferWriter.write("Jogador(a) " + jogadorAtual + " - Coordenadas. Linha " + (linha + 1) + " - Coluna " + (coluna + 1));
        bufferWriter.newLine();
        if (verificarVitoria()) {
            System.out.println("Jogador(a) " + jogadorAtual + " venceu!");
            bufferWriter.write("Jogador(a) " + jogadorAtual + " venceu!");
            imprimirTabuleiro();
            return true;
        } else if (tabuleiroCheio()) {
            System.out.println("O jogo empatou!");
            bufferWriter.write("O jogo empatou!");
            imprimirTabuleiro();
            return true;
        } else {
            jogadorAtual = jogadorAtual == jogadorUm ? jogadorDois : jogadorUm;
        }
        return false;
    }

    private void iniciarTabuleiroEJogo(Scanner scanner, BufferedWriter bufferWriter) throws IOException {
        System.out.println("Informe o tamanho do jogo desejado (3,4,5,6,7,8,9,10): ");
        tamanhoTabuleiro = scanner.nextInt();
        bufferWriter.newLine();
        bufferWriter.write("- NOVO JOGO -");
        bufferWriter.newLine();
        bufferWriter.write("Jogadores: " + jogadorUm + " e " + jogadorDois + ". Tamanho do tabuleiro: " + tamanhoTabuleiro);
        bufferWriter.newLine();
        tabuleiro = new char[tamanhoTabuleiro][tamanhoTabuleiro];
        iniciarTabuleiro();
    }

    private static boolean fimJogo(Scanner scanner) {
        System.out.println("Deseja iniciar um novo jogo ou ir para o menu?");
        System.out.println("1. Iniciar novo jogo");
        System.out.println("2. Voltar menu principal");
        int opcao = scanner.nextInt();
        return opcao == 1;
    }

    public void jogar() throws IOException {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        String nomeArquivo = "historico.txt";
        File arquivo = new File(nomeArquivo);

        System.out.println("Bem-vindo(a) ao Jogo da Velha!");
        boolean repetirMenu = false;
        do {
            System.out.println("Menu");
            System.out.println("1. Iniciar jogo");
            System.out.println("2. Ver historico");
            System.out.println("3. Sair");
            int opcaoMenu = scanner.nextInt();

            if (opcaoMenu == 1) {
                if (!arquivo.exists()) {
                    arquivo.createNewFile();
                }

                FileWriter writer = new FileWriter(arquivo, true);
                BufferedWriter bufferWriter = new BufferedWriter(writer);

                System.out.println("Informe o tipo do jogo desejado: ");
                System.out.println("1. Vs player");
                System.out.println("2. Vs robô");
                int tipoJogo = scanner.nextInt();
                if (tipoJogo > 2) {
                    System.out.println("Tipo de jogo ainda não implementado. Tente outra vez.");
                    repetirMenu = false;
                    break;
                }

                scanner.nextLine();
                System.out.println("Por favor, informe o nome do jogador 1: ");
                jogadorUm = scanner.nextLine();

                boolean repetirJogo;
                if (tipoJogo == 1) {
                    System.out.println("Por favor, informe o nome do jogador 2: ");
                    jogadorDois = scanner.nextLine();

                    do {
                        iniciarTabuleiroEJogo(scanner, bufferWriter);
                        jogadorAtual = jogadorUm;
                        while (true) {
                            if (jogadaPessoa(scanner, writer, bufferWriter)) break;
                        }

                        repetirJogo = fimJogo(scanner);
                    } while (repetirJogo);
                } else {
                    jogadorDois = "Robo";
                    do {
                        iniciarTabuleiroEJogo(scanner, bufferWriter);
                        while (true) {
                            jogadorAtual = jogadorUm;
                            if (jogadaPessoa(scanner, writer, bufferWriter)) break;
                            if (jogadaRobo(random, writer, bufferWriter)) break;
                        }

                        repetirJogo = fimJogo(scanner);
                    } while (repetirJogo);

                    repetirMenu = true;
                    bufferWriter.close();
                    writer.close();
                }
            } else {
                if (opcaoMenu == 2) {
                    if (!arquivo.exists()) {
                        System.out.println("Nenhum histórico encontrado!");
                    }

                    System.out.println("-- INICIO HISTORICO --");
                    FileReader reader = new FileReader(arquivo);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String linha;

                    while ((linha = bufferedReader.readLine()) != null) {
                        System.out.println(linha);
                    }

                    bufferedReader.close();
                    reader.close();
                    System.out.println("-- FIM HISTORICO --");
                    repetirMenu = false;
                } else {
                    if (opcaoMenu == 3) {
                        break;
                    } else {
                        System.out.println("Opção não disponivel.");
                        repetirMenu = false;
                    }
                }
            }
        } while (repetirMenu);
        scanner.close();
    }
}