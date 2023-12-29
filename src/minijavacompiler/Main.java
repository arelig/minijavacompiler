package minijavacompiler;


public class Main {
    public final static int MISSING_ARGUMENT = -1;
    public static MainModule module = null;
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Faltan argumentos en la invocación del compilador.");
            System.exit(MISSING_ARGUMENT);
        }

        module = new MainModule(args);

    }
}
