package com.applied_math;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.sql.SQLOutput;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        Map<Character, Integer> map = new HashMap<>();
        Pattern p = Pattern.compile("[a-z1-9 \r\n]");
        try {
            System.out.println("Input file name");
            Scanner in = new Scanner(System.in);
            String fileName = in.next();
            FileReader reader = new FileReader(fileName);
            int c;
            int total = 0;
            while ((c = reader.read()) != -1) {
                Character ch = Character.toLowerCase((char) c);
                if (ch == '\n') continue;
                if (!p.matcher(ch.toString()).matches()) {
                    ch = '.';
                }
                if (map.containsKey(ch)) {
                    map.put(ch, map.get(ch) + 1);
                } else
                    map.put(ch, 1);
                total++;
            }
            int finalTotal = total;
            Map<Character, Double> probability = map.entrySet()
                    .stream()
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), (double) entry.getValue() / finalTotal))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            double entropy = 0;
            System.out.println(1);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Some IO exception");
        }


    }
}
