/**
 * A2QAMitsukLavitt7607877
 *
 * COMP 1020 SECTION D01
 * INSTRUCTOR    Safiur Mahdi
 * ASSIGNMENT    Assignment 2, Question A
 * @author       Richard Mitsuk Lavitt, 7607877
 * @version      2019-05-21
 *
 * PURPOSE: Tallies  and outputs Olympic medals categorized by event and
 *          by country.
 */

import java.io.*;
import java.util.ArrayList;

class A2QBMitsukLavitt7607877 {
    public static void main(String args[]) {
        ArrayList<Sentence> sentences = new ArrayList<>();

        sentences = loadSentences("aqb.txt");

        System.out.println("End of processing.");
    }

    private static ArrayList<Sentence> loadSentences(String filename) {
        BufferedReader input;
        ArrayList<Sentence> sentences = new ArrayList<>();

        sentences.add(new Sentence());

        try {
            input = new BufferedReader(new FileReader(filename));

            lineContents = input.readLine();
            while(lineContents != null) {
                lineContents = input.readLine();
            }

            input.close();
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        }

        return sentences;
    }
}

class Sentence {
    private String text = "";
    private int wordCount = 0;
    private int letterCount = 0;

    public Sentence() {
    }

    public Sentence(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void add(String word, int letterCount) {
        text.concat("" + word);
        this.letterCount += letterCount;
        wordCount++;
    }

    public int getWordCount() {
        return wordCount;
    }

    public int getLetterCount() {
        return letterCount;
    }

    public String getText() {
        return text;
    }
}