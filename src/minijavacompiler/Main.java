package minijavacompiler;


public class Main {
    public final static int MISSING_ARGUMENT = -1;
    public static MainModule module = null;
    public static void main(String[] args) {
        //MainModule m = new MainModule("D:\\UNS\\En curso\\4-Compiladores\\repo\\minijavacompiler_iglesias\\src\\minijavacompiler\\catbag\semError01.txt");
        if (args.length == 0) {
            System.out.println("Faltan argumentos");
            System.exit(MISSING_ARGUMENT);
        }

        module = new MainModule(args[0]);

    }
}
