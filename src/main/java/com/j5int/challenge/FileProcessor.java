package com.j5int.challenge;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class FileProcessor {
    public static void main(String[] args) throws IOException {
        if(args.length >1) {
            String inputFileName = args[0];
            String outputFileName = args[1];

            var students = parseFileToStream(inputFileName);
            var topStudents = getTopScoringStudents(students);
            writeTopStudentsToCsv(topStudents, outputFileName);
        } else {
            log.error("Input or Output file name missing, please ensure you provide both");
            throw new IllegalArgumentException("Input or Output file name missing, please ensure you provide both");
        }
    }

    static Stream<Student> parseFileToStream(String fileName) throws IOException {
        return lines(Paths.get(fileName))
                .map(line -> new Student(Arrays.asList(line.split(","))));
    }

    static List<Student> getTopScoringStudents(Stream<Student> studentStream) {
        return studentStream
                .collect(groupingBy(Student::getScore))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .orElseThrow()
                .getValue();
    }


    static void writeTopStudentsToCsv(List<Student> topStudents, String outputFileName) throws FileNotFoundException {
        File csvOutputFile = new File(outputFileName);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            topStudents
                    .forEach(pw::println);
        } catch (FileNotFoundException fne) {
            log.error("The output file was not found.", fne);
            throw fne;
        }
    }
}
