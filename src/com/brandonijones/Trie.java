package com.brandonijones;

import java.util.LinkedList;
import java.util.Queue;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();  // root is empty
    }

    static class TrieNode {
        private TrieNode[] children;
        private TrieNode failureLink;
        private TrieNode dictionaryLink;
        private boolean isWord;
        private int patternIndex;

        public TrieNode() {
            this.children = new TrieNode[26];
            this.failureLink = null;
            this.dictionaryLink = null;
            this.isWord = false;
            this.patternIndex = -1;
        }
    }

    public TrieNode getRoot() {
        return root;
    }

    public void buildTrie(String[] patterns) {
        // Adding each string in the patterns array
        for (int i = 0; i < patterns.length; i++) {
            // Start at the root, set string to lowercase
            TrieNode current = root;
            patterns[i] = patterns[i].toLowerCase();

            // Traverse through each letter of the current string
            for (int j = 0; j < patterns[i].length(); j++) {
                // Reads current character in string, sets as index
                char c = patterns[i].charAt(j);
                int index = c - 'a';    // based on ASCII values (ex: 'a' - 'a' = 97 - 97 = 0)

                // If current letter is not in the project01.Trie, add new node to children array
                if (current.children[index] == null) {
                    TrieNode node = new TrieNode();
                    current.children[index] = node;
                    current = node;
                } else {
                    // Traverses to appropriate node if it exists
                    current = current.children[index];
                }
            }

            // String was successfully added to the project01.Trie
            // patternIndex corresponds to string placement in array
            current.patternIndex = i;
            current.isWord = true;
        }
    }

    public void buildFailureAndDictionaryLinks() {
        // Using BFS to set links
        root.failureLink = root;    // root represents empty string
        Queue<TrieNode> queue = new LinkedList<>();

        // Starting nodes to be added to the queue
        for (int i = 0; i < root.children.length; i++) {
            if (root.children[i] != null) {
                queue.add(root.children[i]);
                root.children[i].failureLink = root;    // root's children failure link will point to root only
            }
        }

        // Visit children of all unvisited nodes
        while (queue.size() > 0) {
            TrieNode current = queue.peek();
            queue.remove();

            // Updates failure links of children of current node
            for (int j = 0; j < current.children.length; j++) {
                if (current.children[j] != null) {
                    TrieNode currentChild = current.children[j];
                    TrieNode temp = current.failureLink;    // the current longest possible suffix for the current node

                    if (temp.children[j] == null) {
                        currentChild.failureLink = root;    // suffix does not exist, link to empty string
                    } else {
                        currentChild.failureLink = temp.children[j];    // suffix does exist, link to temp's child
                    }

                    queue.add(currentChild);    // Adds children to queue
                }
            }

            if (current.failureLink.isWord) {
                current.dictionaryLink = current.failureLink;   // Setting dictionary links of current node
            } else {
                current.dictionaryLink = current.failureLink.dictionaryLink;
            }
        }
    }

    public void search(String[] patterns, String text, int[] counter) {
        // Start at the root, set text to lowercase
        TrieNode current = root;
        text = text.toLowerCase();

        // Searches through the given text
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int index = c - 'a';

            // Ignores any character that is not in the alphabet
            if (index >= 0 &&  index <= 25) {
                // Checks if letter is a child of the current node
                if (current.children[index] != null) {
                    current = current.children[index];

                    // Checks if current node is a word
                    if(current.isWord) {
                        counter[current.patternIndex]++;
                    }

                    // Follows dictionary links if there are any
                    TrieNode temp = current.dictionaryLink;
                    while (temp != null) {
                        counter[temp.patternIndex]++;
                        temp = temp.dictionaryLink;

                    }
                } else {
                    // Follows failure links
                    while (current != root && current.children[index] == null) {
                        current = current.failureLink;
                    }
                    // Decrement i to skipping past current letter in the text
                    if (current.children[index] != null) {
                        i--;
                    }
                }
            }
        }
    }
}
