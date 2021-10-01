package main;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.*;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TesterDeCasosConErrores {

    private static final Main init = null;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private static final String testFilesDirectoryPath = "resources/batteryTestSyntaxParser/conErrores/";
    private final String input;


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

    public TesterDeCasosConErrores(String input) {
        this.input = input;
    }


    @Test
    public void test1() {
        probarFallo(input);
    }

    private void probarFallo(String name) {
        String testCaseFilePath = testFilesDirectoryPath + name;
        String errorCode = getErrorCode(testCaseFilePath);
        String[] args = {testCaseFilePath};
        Main.main(args);

        assertThat("No se encontro el codigo: " + errorCode, outContent.toString(), CoreMatchers.containsString(errorCode));
    }


    String getErrorCode(String testCaseFilePath) {
        String lineWithTheCode = null;
        try {
            lineWithTheCode = (new BufferedReader(new FileReader(testCaseFilePath))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String errorCode = lineWithTheCode.substring(3);
        return errorCode;
    }


}
