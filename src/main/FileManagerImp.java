package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManagerImp implements FileManager {
    private Reader buffer;

    private List<Integer> currentLine;
    private int line;
    private int column;

    public FileManagerImp(String fileName) throws FileNotFoundException {
        FileInputStream fileStream = new FileInputStream(fileName);
        Reader reader = new InputStreamReader(fileStream);
        buffer = new BufferedReader(reader);
    }

    private void fetchLine() {
        try {
            int currentChar;
            boolean stop = false;
            currentLine.clear();

            while (!stop) {
                currentChar = buffer.read();
                if (!isEnter(currentChar)) {
                    currentLine.add(currentChar);
                }

                if (isEOL(currentChar) || isEOF(currentChar)) {
                    stop = true;
                }
            }
            column = 0;
            line++;
        } catch (IOException error) {
            System.out.println("Error reading the file");
        }

    }

    private boolean isEnter(int currentChar) {
        return currentChar == 13;
    }

    private boolean isEOF(int currentChar) {
        return currentChar == -1;
    }

    private boolean isEOL(int currentChar) {
        return currentChar == 10;
    }

    public String currentLine() {
        StringBuilder stringBuild = new StringBuilder();
        for (int c : currentLine) {
            if (!isEOL(c) || !isEOF(c)) {
                stringBuild.append((char) c);
            }
        }

        return stringBuild.toString();
    }

    @Override
    public int line() {
        return line;
    }

    @Override
    public int column() {
        return column;
    }

    @Override
    public int nextCharacter() {
        if (column >= currentLine.size()) {
            fetchLine();
        }
        return currentLine.get(column++);
    }


    @Override
    public void validate() {
        line = 0;
        currentLine = new ArrayList<>();
        fetchLine();
    }

    @Override
    public void invalidate() {
        try {
            buffer.close();
        } catch (IOException e) {
            System.out.println("Error closing the file \n");
        }
    }
}

