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
        System.out.println("End of processing.");
    }
}

class Sentence {
    private String text = "";
    private int wordCount = 0;

    public Sentence() {
    }

    public Sentence(String text) {
        this.text = text;
    }

    public void setBody(String text) {
        this.text = text;
    }

    public void add(String word) {
        text.concat("" + word);
        wordCount++;
    }

    public int getWordCount() {
        return wordCount;
    }

    public String getBody() {
        return text;
    }
}