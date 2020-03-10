package com.j5int.challenge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {
    private String resourcesDirPath;
    private List<Student> mockList;

    @BeforeEach
    void setUp() {
        Path resourceDirectory = Paths.get("src", "test", "resources");
        resourcesDirPath = resourceDirectory.toFile().getAbsolutePath();

        mockList = List.of(new Student("Bright", "Dodo", 96.0),
                new Student("John", "Doe", 96.0));
    }

    @Test
    public void givenValidArgs_whenMain_thenOpensFileAndWrites() throws IOException {
        var inputFile = resourcesDirPath.concat("/testInput.csv");
        var outputFile = resourcesDirPath.concat("/testOutput3.csv");
        var outputFilePath = Paths.get(outputFile);
        var args = new String[]{inputFile, outputFile};
        FileProcessor.main(args);

        assertAll(
                () -> assertTrue(Files.exists(outputFilePath)),
                () -> assertEquals(1, Files.readAllLines(outputFilePath).size()));
    }

    @Test
    public void givenNoArg_whenMain_thenThrowsIllegalArgumentException() {
        var args = new String[]{};
        assertThrows(IllegalArgumentException.class, () -> FileProcessor.main(args));
    }

    @Test
    public void givenOneArg_whenMain_thenThrowsIllegalArgumentException() {
        var args = new String[]{"input"};
        assertThrows(IllegalArgumentException.class, () -> FileProcessor.main(args));
    }

    @Test
    public void givenInvalidInputFile_whenMain_thenThrowsIOException() {
        var args = new String[]{"input", "output"};
        assertThrows(IOException.class, () -> FileProcessor.main(args));
    }

    @Test
    public void givenValidInputFile_whenParseFileToStream_thenReturnsStudentStream() throws IOException {
        var inputFile = resourcesDirPath.concat("/testInput.csv");

        var studentStream = FileProcessor.parseFileToStream(inputFile);
        assertEquals(3, studentStream.count());
    }

    @Test
    public void givenStreamWithNoTie_whenGetTopScoringStudents_thenReturnOneTopStudent() {
        var mockStream = Stream.of(new Student("John", "Doe", 96.0), new Student("John", "Barnes", 90.0));

        var topStudents = FileProcessor.getTopScoringStudents(mockStream);

        assertAll(
                () -> assertEquals(1, topStudents.size()),
                () -> assertEquals("John", topStudents.get(0).getName()),
                () -> assertEquals("Doe", topStudents.get(0).getSurname()),
                () -> assertEquals(96.0, topStudents.get(0).getScore())
        );
    }

    @Test
    public void givenStreamWithTie_whenGetTopScoringStudents_thenReturnOneTopStudent() {
        var student1 = new Student("Joe", "Doe", 96.5);
        var student2 = new Student("John", "Barnes", 90.0);
        var student3 = new Student("Jack", "Daniels", 96.5);
        var mockStream = Stream.of(student1, student2, student3);

        var topStudents = FileProcessor.getTopScoringStudents(mockStream);

        assertAll(
                () -> assertEquals(2, topStudents.size()),
                () -> assertTrue(topStudents.contains(student1)),
                () -> assertTrue(topStudents.contains(student3)),
                () -> assertFalse(topStudents.contains(student2)),
                () -> assertEquals(96.5, topStudents.get(0).getScore())
        );
    }

    @Test
    public void givenStreamWithTie_whenGetTopScoringStudents_thenReturnOneTopStudentTwo() {
        var student1 = new Student("Joe", "Doe", 96.5);
        var student2 = new Student("John", "Barnes", 90.0);
        var student3 = new Student("Jack", "Daniels", 96.5);
        var mockStream = Stream.of(student1, student2, student3);

        var topStudents = FileProcessor.getTopScoringStudents(mockStream);

        assertAll(
                () -> assertEquals(2, topStudents.size()),
                () -> assertTrue(topStudents.contains(student1)),
                () -> assertTrue(topStudents.contains(student3)),
                () -> assertFalse(topStudents.contains(student2)),
                () -> assertEquals(96.5, topStudents.get(0).getScore())
        );
    }

    @Test
    public void givenInvalidInputFile_whenParseFileToStream_thenThrowsIOException() {
        var inputFile = resourcesDirPath.concat("/testInput123.csv");
        assertThrows(IOException.class, () -> FileProcessor.parseFileToStream(inputFile));
    }

    @Test
    public void givenListOfTopStudents_whenWriteTopStudentsToCsv_thenOpensFileAndWrites() throws FileNotFoundException {
        var outputFile = resourcesDirPath.concat("/output.csv");
        var outputFilePath = Paths.get(outputFile);
        FileProcessor.writeTopStudentsToCsv(mockList, outputFile);

        assertAll(
                () -> assertTrue(Files.exists(outputFilePath)),
                () -> assertEquals(2, Files.readAllLines(outputFilePath).size()));
    }

    @Test
    public void givenNonExistentPath_whenWriteTopStudentsToCsv_thenThrowsFileNotFoundException() {
        Path resourceDirectory = Paths.get("src", "test", "resource");
        var resourceDirPath = resourceDirectory.toFile().getAbsolutePath();
        var outputFile = resourceDirPath.concat("/testOutput.csv");

        assertThrows(FileNotFoundException.class, () -> FileProcessor.writeTopStudentsToCsv(mockList, outputFile));
    }
}