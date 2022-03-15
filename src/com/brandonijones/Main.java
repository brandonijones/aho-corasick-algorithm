package com.brandonijones;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // a atc c cat gcg gc gca
        Trie ahoCorasick = new Trie();
        Scanner userInput = new Scanner(System.in);

        String text = "gcatcgcg";
        System.out.println("Text: " + text);

        System.out.print("Enter the string(s) you want to find: ");
        String dictionary = userInput.nextLine();
        String[] patterns = dictionary.split(" ");
        int[] counter = new int[patterns.length];

        ahoCorasick.buildTrie(patterns);
        ahoCorasick.buildFailureAndDictionaryLinks();
        ahoCorasick.search(patterns, text, counter);

        System.out.println("\nSTRING COUNT");
        for (int i = 0; i < patterns.length; i++) {
            System.out.printf("%5s: %-8d\n", patterns[i], counter[i]);
        }
    }
}
