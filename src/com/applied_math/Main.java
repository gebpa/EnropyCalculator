package com.applied_math;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        Map<Character, Integer> frequency = new HashMap<>();
        Map<String, Integer> pairFrequency = new HashMap<>();
        Pattern p = Pattern.compile("[a-z1-9 \r\n]");
        try {
            System.out.println("Input file name");
            Scanner in = new Scanner(System.in);
            String fileName = in.next();
            FileReader reader = new FileReader(fileName);
            int c;
            int total = 0;
            boolean isItFirst = true;
            Character previousCh = ' ';
            while ((c = reader.read()) != -1) {
                Character ch = Character.toLowerCase((char) c);
                if (ch == '\n') continue;
                if (!p.matcher(ch.toString()).matches()) {
                    ch = '.';
                }
                if (frequency.containsKey(ch)) {
                    frequency.put(ch, frequency.get(ch) + 1);
                } else
                    frequency.put(ch, 1);
                if (!isItFirst) {
                    String chain = ch + "" + previousCh;
                    if (pairFrequency.containsKey(chain)) {
                        pairFrequency.put(chain, pairFrequency.get(chain) + 1);
                    } else
                        pairFrequency.put(chain, 1);
                } else
                    isItFirst = false;
                previousCh = ch;
                total++;
            }
            int finalTotal = total;
            Map<Character, Double> probability = frequency.entrySet()
                    .stream()
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), (double) entry.getValue() / finalTotal))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Map<Character, Double> symbolEntropy = probability.entrySet()
                    .stream()
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), (double) (-1) * Math.log(entry.getValue()) / Math.log(2)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Map<String, Double> probabilityChain = pairFrequency.entrySet()
                    .stream()
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), (double) entry.getValue() / frequency.get(entry.getKey().charAt(1))))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Map<String, Double> chainEntropy = probabilityChain.entrySet()
                    .stream()
                    .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), (double) (-1) * Math.log(entry.getValue()) / Math.log(2)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            double symbolEntropyTotal = 0;
            double chainEntropyTotal = 0;
            System.out.println("Вероятность каждого символа:");
            for (Character character : probability.keySet()) {
                System.out.printf("%s: %.4f\n", character, probability.get(character));
            }
            System.out.println("Энтропия каждого символа:");
            for (Character character : probability.keySet()) {
                Double value = probability.get(character);
                Double log2 = Math.log(value) / Math.log(2);
                symbolEntropyTotal += value * log2;
                System.out.printf("%s: %.4f\n", character, symbolEntropy.get(character));
            }
            for (String chain : probabilityChain.keySet()) {
                Double value = probabilityChain.get(chain);
                Double probabilityOfPrevious = probability.get(chain.charAt(1));
                Double log2 = Math.log(value) / Math.log(2);
                chainEntropyTotal += value * probabilityOfPrevious * log2;
            }
            chainEntropyTotal *= -1;
            symbolEntropyTotal *= -1;
            System.out.printf("Энтропия: %.4f\n", symbolEntropyTotal);
            System.out.printf("Энтропия с использованием пар: %.4f", chainEntropyTotal);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (IOException ex) {
            System.out.println("Some IO exception");
        }
    }
}
