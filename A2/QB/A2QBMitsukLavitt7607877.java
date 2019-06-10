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

        sentences = loadSentences("a2b.txt");

        System.out.println("First five sentences:");
        outputSentences(sentences, 0, 5);

        System.out.println();

        System.out.println("Last five sentences:");
        outputSentences(sentences, sentences.size() - 5, sentences.size());

        System.out.println();
        System.out.println("Summary statistics: ");
        System.out.println("Letters: " + getAllLetterCount(sentences));
        System.out.println("Words: " + getAllWordCount(sentences));
        System.out.println("Sentences: " + (sentences.size() - 1));

        System.out.println("Readability: " + computeARI(sentences));

        System.out.println("End of processing.");
    }

    private static void outputSentences(ArrayList<Sentence> sentences, int start, int end) {
        for(int i = start; i < end; i++) {
            System.out.println("(" + (i + 1) + "): " + sentences.get(i).getText());
        }
    }

    private static int getAllLetterCount(ArrayList<Sentence> sentences) {
        int letterCount = 0;

        for(Sentence sentence : sentences) {
            letterCount += sentence.getLetterCount();
        }

        return letterCount;
    }

    private static int getAllWordCount(ArrayList<Sentence> sentences) {
        int wordCount = 0;

        for(Sentence sentence : sentences) {
            wordCount += sentence.getWordCount();
        }

        return wordCount;
    }

    private static double computeARI(ArrayList<Sentence> sentences) {
        double ARI        = 0.0;
        double wordCount     = 0.0;
        double letterCount   = 0.0;

        for(Sentence sentence : sentences) {
            wordCount += sentence.getWordCount();
            letterCount += sentence.getLetterCount();
        }
        ARI = ((4.71 * (letterCount/wordCount)) 
            + (0.5 * (wordCount/sentences.size())) 
            - 21.43);

        ARI = (double) Math.round(ARI * 10d) / 10d;
        return ARI;
    }

    private static ArrayList<Sentence> loadSentences(String filename) {
        BufferedReader input;
        ArrayList<Sentence> sentences = new ArrayList<>();

        try {
            input = new BufferedReader(new FileReader(filename));
            String currentLine;
            char[] currentLineChars;
            String word = "";
            int    currentSentence = 0;

            currentLine = input.readLine();

            sentences.add(new Sentence());
            while(currentLine != null) {
                currentLineChars = currentLine.toCharArray();
                word += " ";

                for(char c : currentLineChars) {
                    if(!Character.isWhitespace(c)) {
                        word += c;

                        if(c == '?' || c == '!' || c == '.') {
                            sentences.get(currentSentence).add(word);
                            sentences.get(currentSentence).trim();
                            sentences.add(new Sentence());
                            currentSentence++;
                            word = " ";
                        }
                    }
                    else {
                        sentences.get(currentSentence).add(word);
                        word = " ";
                    }
                }
                        
                currentLine = input.readLine();
            }

            input.close();

            // Remove a blank sentence added at the end of file
            if(sentences.get(sentences.size() - 1).getText().equals("")) {
                sentences.remove(sentences.size() - 1);
            }
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

    public void add(String word) {
        for(int i = 0; i < word.length(); i++) {
            if(Character.isLetter(word.charAt(i))) {
                letterCount++;
            }
        }
        text = text.concat(word);
        wordCount++;
    }

    public int getWordCount() {
        return wordCount;
    }

    public int getLetterCount() {
        return letterCount;
    }

    public void trim() {
        text = text.trim();
    }

    public String getText() {
        return text;
    }
}