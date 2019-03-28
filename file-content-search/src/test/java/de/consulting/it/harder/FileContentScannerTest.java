package de.consulting.it.harder;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class FileContentScannerTest {


    @Test
    public void determineSearchTerms(){
        //given/when
        List<String> strings = FileContentScanner.determineSearchTerms(new String[]{"test"});

        //then
        assertThat(strings.get(0), equalTo("test"));
    }

    @Test
    public void findFilePathsInDirectory() throws IOException {
        //given/when
        List<Path> fileNamesInDirectory = FileContentScanner.findFilePathsInDirectory(FileContentScanner.CONFIG_FILE_PATH);

        //then
        assertThat("2 files in directory.", fileNamesInDirectory.size(), equalTo(2));
    }

    @Test
    public void searchForArgsInAllFilesOfDirectory() throws IOException {
        //given
        int expectedFileNameListSize = 1;
        String givenTestFileName = "testFile.xml";
        List<String> givenSearchTerms = FileContentScanner.determineSearchTerms(new String[]{"test"});

        //when
        List<String> fileNamesInDirectory = FileContentScanner.searchForArgsInAllFilesOfDirectory(givenSearchTerms);

        //then
        assertThat("1 file which contains a search term.", fileNamesInDirectory.size(), equalTo(expectedFileNameListSize));
        assertThat("Name of test file is correct.", fileNamesInDirectory.get(0), equalTo(givenTestFileName));
    }
}