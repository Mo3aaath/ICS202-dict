package Project;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class Dictionary {
    private AVLTree<String> list = new AVLTree<>();

    public Dictionary() {
    }

    public Dictionary(String s) {
        list.insertAVL(s);
    }

    public Dictionary(File f) throws IOException, WordAlreadyExistsException {
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(f);
            sc = new Scanner(inputStream, "UTF-8");

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!findWord(line))
                    list.insertAVL(line);
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

    // method for adding new words to the dictionary
    public void addWord(String s) throws WordAlreadyExistsException {
        if (list.search(s)) {
            throw new WordAlreadyExistsException();
        } else {
            list.insertAVL(s);
        }
    }

    // method for searching the dictionary for a specific word
    public boolean findWord(String s) {
        return list.search(s);
    }

    // method for deleting a word from the dectionary
    public void deleteWord(String s) throws WordNotFoundException, Exception {
        if (list.search(s) == false) {
            throw new WordNotFoundException();
        } else {
            list.deleteAVL(s);
        }
    }

    // method for searching the dictionary for similiar words to a specified
    // parameter
    public String[] findSimilar(String s) {

        BTNode<String> p = list.root;
        StackUsingArray travStack = new StackUsingArray(100);
        String words = "";
        String[] array;

        if (p != null) {
            travStack.push(p);
            while (!travStack.isEmpty()) {
                p = travStack.pop();

                int len = p.data.length();
                int similarity = 0;
                if (s.length() - p.data.length() == 0 || // if the word has equal length or greater by 1
                        s.length() - p.data.length() == -1) {

                    for (int i = 0; i < s.length(); i++) {
                        if (s.charAt(i) == p.data.charAt(i)) {
                            similarity++;
                        }
                    }
                    if (similarity == len - 1) {
                        words += p.data + ",";
                    }
                }

                if (s.length() - p.data.length() == 1) { // if the word length is less by one
                    for (int i = 0; i < p.data.length(); i++) {
                        if (s.charAt(i) == p.data.charAt(i)) {
                            similarity++;
                        }
                    }
                    if (similarity == len - 1) {
                        words += p.data + ",";
                    }
                }

                if (p.right != null)
                    travStack.push(p.right);
                    
                if (p.left != null) // left child pushed after right
                    travStack.push(p.left);// to be on the top of the stack;
            }
        }
        if (!words.equals("")) {

            array = words.split(",");
        } else {
            array = new String[0];
        }
        return array;

    }

    // method for saving current dictionary content to a .txt file
    public void saveDict(String dictName) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File(dictName));
        BTNode<String> p = list.root;
        StackUsingArray travStack = new StackUsingArray(100);

        while (p != null) {
            while (p != null) { // stack the right child (if any)
                if (p.right != null) // and the node itself when going
                    travStack.push(p.right); // to the left;
                travStack.push(p);
                p = p.left;
            }
            p = travStack.pop(); // pop a node with no left child
            while (!travStack.isEmpty() && p.right == null) { // visit it and all
                writer.write(p.data + '\n'); // nodes with no right child;
                p = travStack.pop();
            }

            writer.write(p.data + '\n'); // visit also the first node with
            if (!travStack.isEmpty()) // nodes with no right child;
                p = travStack.pop(); // a right child (if any);
            else
                p = null;
        }
        writer.close();
    }

    ////////////////////////////////////
    // main driver

    public static void main(String[] args) throws Exception {
        Dictionary dict = new Dictionary();
        boolean stop = false;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Dictionary main driver.");

        while (!stop) {
            System.out.println();

            System.out.println("1. Load an existing dictionary from a text file");
            System.out.println("2. Find a word in the dictionary");
            System.out.println("3. Add a word to the dictionary");
            System.out.println("4. Remove a word from the dictionary");
            System.out.println("5. Search for similar words in the dictionary");
            System.out.println("6. Save the dicitionary to a text file");
            System.out.println("7. Exit");
            System.out.println("-----------------------------------------------");
            System.out.println();

            System.out.print("Choose an action> ");
            int input = scanner.nextInt();

            switch (input) {
                case 1 -> {
                    try {
                        System.out.print("Enter filename> ");
                        String fileName = scanner.next();

                        System.out.println("loading...");
                        dict = new Dictionary(new File(fileName));
                        System.out.println("dictionary loaded successfully");
                    } catch (IOException e) {
                        System.out.println("Error: file does not exist");
                    }
                }
                case 2 -> {
                    System.out.print("Check word> ");
                    String wordToFind = scanner.next();
                    if (dict.findWord(wordToFind)) {
                        System.out.println("word found");
                    } else
                        System.out.println("word not found");

                }
                case 3 -> {
                    System.out.print("Enter the word> ");
                    String word = scanner.next();
                    try {
                        dict.addWord(word);
                        System.out.println("Word added successfully");
                    } catch (WordAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                }
                case 4 -> {
                    System.out.print("Enter word to remove> ");
                    String removedWord = scanner.next();
                    try {
                        dict.deleteWord(removedWord);
                        System.out.println("Word removed successfully");
                    } catch (WordNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 5 -> {
                    System.out.print("Enter word> ");
                    String simWord = scanner.next();
                    String[] words = dict.findSimilar(simWord);

                    if (words.length == 0) {
                        System.out.println("No similar words found");
                    } else {
                        String line = "";
                        for (int i = 0; i < words.length - 1; i++) {
                            line += words[i] + ", ";
                        }
                        line += words[words.length - 1];
                        System.out.println(line);
                    }

                }
                case 6 -> {
                    System.out.print("Enter new file name> ");
                    String file = scanner.next();
                    try {
                        dict.saveDict(file);
                        System.out.println("dictionary saved in " + file + " successfully");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case 7 -> {
                    stop = true;
                    System.out.println("Program closed");
                    scanner.close();
                }
                default -> {
                    System.out.println("Error: Wrong input. Please try again");
                }
            }
        }
    }

}