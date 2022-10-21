package edu.gatech.seclass.textsummary;

import org.apache.commons.cli.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public class Main {

/*
Empty Main class for compiling Assignment 6.
DO NOT ALTER THIS CLASS or implement it.
 */

    public static void main(String[] args) {
        Main main = new Main();
        if(args.length == 0) {
            usage();
            return;
        }
        String fileName = args[args.length-1];
        boolean exists = new File(fileName).exists();
        if(!exists) {
            System.err.println("File Not Found");
            return;
        }
        main.runCommand(args, fileName);

    }

    private static void usage() {
        System.err.println("Usage: textsummary [-d [int]] [-c string] [-l int | -s int | -u] <filename>");
    }

    public static BufferedReader getReader(String file) {
        try {
            return new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            System.err.print("There was a problem while accessing the file.");
            return null;
        }
    }

    public static BufferedWriter getWriter(String file) {
        try {
            return new BufferedWriter(new FileWriter(file, false));
        } catch (Exception e) {
            System.err.print("There was a problem while accessing the file.");
            return null;
        }
    }

    public List<String> readFileAndReturnList(String filename)   {
        int readChar = 0;
        char c;
        List<String> fileContents = new ArrayList<>();

        try {
                    BufferedReader br = Main.getReader(filename);
                    StringBuilder line = new StringBuilder();
            String tmp = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
                    List<String> contents = new ArrayList<String>(Arrays.asList(tmp.split("(?<=\\R)")));
                    fileContents = contents;
              } catch (Exception e)   {
            System.err.print("There was a problem while accessing the file.");
        }

        return fileContents;
    }

    public void getLongestLine(int n, String file) {
        List<String> listOfLines;
        try {
            listOfLines = readFileAndReturnList(file);
            List<String> originalList = new ArrayList<>(listOfLines);
            Collections.sort(listOfLines, Comparator.comparing(String::length).reversed());
            listOfLines = listOfLines.subList(0,n);
            int count = 0;
            BufferedWriter bw = Main.getWriter(file);
            while(count < originalList.size()) {
                if (listOfLines.contains(originalList.get(count))) {
                    n--;
                    if (n != 0 ) {
                        bw.write(originalList.get(count));
                    } else  {
                        bw.write(originalList.get(count).trim());
                    }
                }
                count++;
            }
            bw.close();
        } catch (Exception e) {
           System.err.print("There was a problem while accessing the file.");
        }
    }

    public void getShortestLine(int n, String file) {
        List<String> listOfLines;
        try {
            listOfLines = readFileAndReturnList(file);
            List<String> originalList = new ArrayList<>(listOfLines);
            Collections.sort(listOfLines, Comparator.comparing(String::length));
            listOfLines = listOfLines.subList(0,n);
            int count = 0;
            BufferedWriter bw = Main.getWriter(file);
            while(count < originalList.size()) {
                if (listOfLines.contains(originalList.get(count))) {
                    n--;
                    if (n != 0) {
                        bw.write(originalList.get(count));
                    } else  {
                        bw.write(originalList.get(count).replaceAll("\\R$", ""));
                    }
                }
                count++;
            }
            bw.close();
        } catch (Exception e) {
            System.err.print("There was a problem while accessing the file.");
        }
    }

    public void getUniqueWords(String file) {
        List<List<String>> listOuter = new ArrayList<>();
        List<String> fileInput = new ArrayList<>();
        Set<String> processedWords = new HashSet<>();

        try {
            BufferedReader br = Main.getReader(file);
            List<String> lines = readFileAndReturnList(file);
            for (String line : lines) {
                List<String> listInner = new ArrayList<>();
                String[] words = line.split(" ");
                for(String word : words) {
                    word = word.replaceAll("[^a-zA-Z0-9]", "");
                    if (!processedWords.contains(word)) {
                            listInner.add(word);
                            processedWords.add(word);
                    } else  {
                        Pattern pattern = Pattern.compile("(?<!\\w)" + word +"\\b");
                        Matcher matcher = pattern.matcher(line);
                        while (matcher.find()) {
                            int gCount = matcher.groupCount();
                            String s = matcher.group();
                            line = new StringBuilder(line).replace(matcher.start(gCount), matcher.end(gCount), "").toString();
                            matcher = pattern.matcher(line);
                        }
                    }
                }
                listOuter.add(listInner);
                fileInput.add(line);
            }
            br.close();
            BufferedWriter bw = Main.getWriter(file);
            /*for(List<String> sentence : listOuter) {
                bw.write(sentence.stream().map(String::valueOf).collect(Collectors.joining(" ")));
                bw.newLine();
            }*/
            int count = 0;
            int n = fileInput.size();
            for (String sentence: fileInput)    {
                n--;
                if (n != 0 || fileInput.size() == 1)   {
                    bw.write(sentence);
                } else  {
                    bw.write(sentence.replaceAll("\\R$", ""));
                }
                count++;
            }
        bw.close();
        } catch(Exception e) {
            System.err.print("There was a problem while accessing the file.");
        }
    }

    public void getDuplicateWords(String file, int n) {
        LinkedHashMap<String, Integer> countMap = new LinkedHashMap<>();
        if (n == 0) {
            System.err.print("Value of d should be greater than 0.");
            return;
        }
        try {
            String line;
            BufferedReader br = Main.getReader(file);

            while ((line = br.readLine()) != null) {
                line = line.replaceAll("[^a-zA-Z0-9]", " ");
                String[] words = line.split(" ");
                for(String word : words) {
                    //word = word.replaceAll("[^a-zA-Z0-9]", "");
                    if (word.isEmpty()) {
                        continue;
                    }
                    if(countMap.containsKey(word)) {
                        int count = countMap.get(word);
                        countMap.put(word, count+1);
                    } else {
                        countMap.put(word, 1);
                    }
                }
            }

            Map<String, Integer> result = countMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            int count = 0;
            while(count < n) {
                System.out.print(result.keySet().toArray()[count].toString() + " " + result.values().toArray()[count].toString() + " ");
                count++;
            }
        } catch(Exception e) {
               System.err.print("There was a problem while accessing the file.");
        }
    }

    public void countWords(String[] arguments, String file) {
        try {
            String word = arguments[0];
            int number = -1;
            int argsLength = arguments.length;
            if(argsLength > 1) {
                try {
                    number = Integer.parseInt(arguments[1]);
                } catch (NumberFormatException e)   {
                    argsLength = 1;
                }
            }
            BufferedReader br = Main.getReader(file);
            List<String> result = new ArrayList<>();
            List<String> lines = readFileAndReturnList(file);
            for (String line: lines) {
                int count = 0;
                boolean addLine = false;
                if(word.length() != 0) {
                    try {
                        Pattern pattern = Pattern.compile(word);
                        Matcher matcher = pattern.matcher(line);
                        while (matcher.find()) {
                            count++;
                        }
                    } catch (PatternSyntaxException e) {
                        count = 0;
                    }
                }
                if(argsLength == 1) {
                    addLine = true;
                }
                if (argsLength > 1 && count >= number)    {
                    addLine = true;
                }

                if (addLine)    {
                    result.add(count + " " + line);
                }
            }
            br.close();
            BufferedWriter bw = Main.getWriter(file);
            int count = 0;

            for(String sent : result) {
                if (count < result.size() - 1 || result.size() == 1)     {
                    bw.write(sent);
                } else  {
                    bw.write(sent.replaceAll("\\s*$", ""));
                }
                count += 1;
            }
        bw.close();
        } catch(Exception e) {
            System.err.print("There was a problem while accessing the file.");      
        }
    }

    public void showLongestLine(String file) {
        String line;
        List<String> listOfLines = new ArrayList<>();
        BufferedReader br = Main.getReader(file);

        try {
            while ((line = br.readLine()) != null) {
                listOfLines.add(line);
            }

            Collections.sort(listOfLines, Comparator.comparing(String::length).reversed());
            System.out.println(listOfLines.get(0));

        } catch (Exception e) {
             System.err.print("There was a problem while accessing the file.");
        }
    }

    public void runCommand(String[] args, String fileName) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        Options options = new Options();

        Option wordCount = new Option("c",true, "word to count");
        wordCount.setRequired(false);
        wordCount.setArgs(2);
        wordCount.setOptionalArg(true);
        options.addOption(wordCount);

        Option duplicateWords = new Option("d", true, "duplicate words");
        duplicateWords.setRequired(false);
        duplicateWords.setOptionalArg(true);
        options.addOption(duplicateWords);

        OptionGroup longestShortest = new OptionGroup();
        longestShortest.addOption(new Option("l",true, "longest sentence"));
        longestShortest.addOption(new Option("s",true, "shortest sentence"));
        longestShortest.addOption(new Option("u",false, "unique words"));
        longestShortest.setRequired(false);
        options.addOptionGroup(longestShortest);

        try {
            cmd = parser.parse(options, args);
            if(cmd.hasOption("d")) {
                try {
                    String dValue = cmd.getOptionValue("d");
                    if (dValue != null && !dValue.equals(fileName)) {
                        getDuplicateWords(fileName, Integer.parseInt(dValue));
                    } else  {
                        getDuplicateWords(fileName, 1);
                    }
                } catch (Exception e)   {
                   usage();
                }
            }
            if(cmd.hasOption("c")) {
                try {
                    String[] word = cmd.getOptionValues("c");
                    countWords(word, fileName);
                } catch(Exception e) {
                    usage();
                }
            }
            if(cmd.hasOption("l")) {
                if(cmd.hasOption("s") || cmd.hasOption("u")) {
                    usage();
                } else {
                    try {
                        getLongestLine(Integer.parseInt(cmd.getOptionValue("l")), fileName);
                    } catch(Exception e) {
                        usage();
                    }
                }
            }
            if(cmd.hasOption("s")) {
                if(cmd.hasOption("l") || cmd.hasOption("u")) {
                    usage();
                } else {
                    getShortestLine(Integer.parseInt(cmd.getOptionValue("s")), fileName);
                }
            }
            if(cmd.hasOption("u")) {
                if(cmd.hasOption("l") || cmd.hasOption("s")) {
                    usage();
                } else {
                    getUniqueWords(fileName);
                }
            }
            if(cmd.getOptions().length == 0) {
                showLongestLine(fileName);
            }

        } catch (UnrecognizedOptionException e) {
            usage();
        } catch (ParseException e) {
            //System.out.println(e.getMessage());
            //formatter.printHelp("textsummary", options);
            usage();

            //System.exit(1);
        }
    }
}