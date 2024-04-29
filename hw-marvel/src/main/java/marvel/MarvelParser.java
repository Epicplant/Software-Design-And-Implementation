/*
 * Copyright (C) 2023 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2023 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package marvel;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parser utility to load the Marvel Comics dataset.
 */
public class MarvelParser {

    /**
     * Reads the Marvel Universe dataset. Each line of the input file contains a character
     * name and a comic book the character appeared in, separated by a comma character
     *
     * @param filename The file csv that will be read.
     * @throws IllegalArgumentException if the file doesn't exist, has an invalid name, or can't be read
     * @return Returns a map where the keys are comic books and the value is a list of all heroes that appear in that comic
     */
    public static HashMap<String, List<String>> parseData(String filename) {
        List<String> lines = readLines(filename);

        HashMap<String, List<String>> returner = new HashMap<>();

        //Goes line by line and splits the data by commas, adding new comics as keys and heroes as values of a list
        //associated with the keys
        for(String line : lines) {

            String[] lineData = line.split(",");

            //if the comic name doesn't already exist in the map create a new list as its value
            if(!returner.containsKey(lineData[1])) {
                returner.put(lineData[1], new ArrayList<>());
            }

            returner.get(lineData[1]).add(lineData[0]);


        }

        return returner;

    }

    /**
     * Reads all lines contained within the provided data file, which is located
     * relative to the data/ folder in this parser's classpath.
     *
     * @param filename The file to read.
     * @throws IllegalArgumentException if the file doesn't exist, has an invalid name,
                                        or can't be read
     * @return A new {@link List<String>} containing all lines in the file.
     */
    private static List<String> readLines(String filename) {
        // Note:
        // Most students won't re-write this code anywhere, this explanation is just for
        // completeness.
        //
        // You can use this code as an example for getting a file from the resources folder
        // in a project like this. If you access data files elsewhere in your code, you'll
        // need to use similar code. If you use this code elsewhere, don't forget:
        //   - Replace 'MarvelParser' in `MarvelParser.class' with the name of the class you
        //       write this in.
        //   - If the class is in src/main, it'll get resources from src/main/resources
        //   - If the class is in src/test, it'll get resources from src/test/resources
        //   - The "/" at the beginning of the path is important
        InputStream stream = MarvelParser.class.getResourceAsStream("/data/" + filename);
        if (stream == null) {
            // The file doesn't exist. We want to handle this case so we don't try to call
            // readLines and have a null pointer exception.
            throw new IllegalArgumentException("No such file: " + filename);
        }
        return new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.toList());
    }
}
