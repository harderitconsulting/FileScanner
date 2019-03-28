package de.consulting.it.harder;
/*
 * This program FileContentScanner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * @author Tim Harder - Harder IT Consulting - www.harder-it-consulting.de
 * <p>
 * Finds all files in the DIRECTORY named "testFiles". Searches the content of these files for specific search terms.
 * If no arguments are given on start-up, the program will use "test" as the default search term.
 * You may replace the default search terms to launch the program from your IDE. Otherwise simply use the jar and provide searchterms on start-up.
 */
public class FileContentScanner {

    private final static BiPredicate<String, String> IS_SEARCH_TERM_CONTAINED = (line, searchTerm) ->
            Optional.ofNullable(line).isPresent() &&
            Optional.ofNullable(searchTerm).isPresent() &&
            line.contains(searchTerm);

    private final static String SEPARATOR = File.separator;
    private final static File DIRECTORY = new File("./");
    private final static String ABSOLUTE_PATH = DIRECTORY.getAbsolutePath().substring(0, DIRECTORY.getAbsolutePath().length() - 1) + "src" + SEPARATOR + "test" + SEPARATOR + "resources" + SEPARATOR + "testFiles" + SEPARATOR;
    public final static Path CONFIG_FILE_PATH = Paths.get(ABSOLUTE_PATH);


    public static void main(String[] args) throws IOException {
        List<String> searchArgs = determineSearchTerms(args);
        System.out.println("Files with search content contained: " + searchForArgsInAllFilesOfDirectory(searchArgs));
    }

    public static List<String> determineSearchTerms(String[] args) {
        if (Optional.ofNullable(args).isPresent() && args.length > 0) {
            return Arrays.stream(args).collect(Collectors.toList());
        }
        return Arrays.asList("test");
    }

    public static List<Path> findFilePathsInDirectory(Path configFilePath) throws IOException {
        return Files.walk(configFilePath)
                .filter(s -> s.toString().endsWith(".xml"))
                .map(Path::getFileName)
                .sorted()
                .collect(Collectors.toList());
    }

    public static List<String> searchForArgsInAllFilesOfDirectory(List<String> searchArgs) throws IOException {
        List<String> filesWithSearchContent = new ArrayList<>();

        for (Path filePath : findFilePathsInDirectory(CONFIG_FILE_PATH)) {
            System.out.println("Scanning File: " + ABSOLUTE_PATH + filePath.toString());
            filesWithSearchContent = searchForArgs(filesWithSearchContent, filePath, searchArgs);
        }

        return filesWithSearchContent;
    }


    private static List<String> searchForArgs(List<String> filePaths, Path filePath, List<String> searchArgs) throws FileNotFoundException {

        //TODO Tim Harder - Scanner sc = new Scanner(Paths.get("test.txt")), without File
        Scanner scanner = new Scanner(new FileInputStream(ABSOLUTE_PATH + filePath.toString()));

        for (String searchTerm : searchArgs) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (IS_SEARCH_TERM_CONTAINED.test(line, searchTerm)) {
                    filePaths.add(filePath.toString());
                }
            }
        }
        scanner.close();
        return filePaths;
    }
}
