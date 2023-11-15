public class Main {
    public static void main(String[] args) {
        JogoDaVelha jogo = new JogoDaVelha();
        try {
            jogo.jogar();
        } catch (Exception e){
            System.out.println("Ops, aconteceu alguma coisa inesperada. Desculpa :)");
        }
    }
}