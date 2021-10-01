package main;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(Parameterized.class)
public class TesterDeCasosSinErrores {

    private static final String msgExito = "[SinErrores]";
    private static final String testFilesDirectoryPath = "resources/batteryTestSyntaxParser/sinErrores/";

    //TODO: el tipo de esta variable init tiene que ser la clase que tiene el main
    private static final Main init = null;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    @Before
    public void setUpClass() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDownClass() {
        System.setOut(originalOut);
    }

    @Parameters(name = "{0}")
    public static Iterable<? extends Object> data() {
        File folder = new File(testFilesDirectoryPath);
        ArrayList<String> names = new ArrayList();
        for (File f : folder.listFiles()) {
            names.add(f.getName());
        }
        names.sort(String::compareTo);
        return names;

    }

    private final String input;

    public TesterDeCasosSinErrores(String input) {
        this.input = input;
    }


    @Test
    public void testIterado() {
        probarExito(input);
    }


    void probarExito(String name) {
        String path = testFilesDirectoryPath + name;
        String[] args = {path};
        Main.main(args);

        assertThat("Mensaje Incorrecto en: " + path, outContent.toString(), CoreMatchers.containsString(msgExito));

    }


}
