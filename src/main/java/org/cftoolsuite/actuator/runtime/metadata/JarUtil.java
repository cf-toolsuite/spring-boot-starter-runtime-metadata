package org.cftoolsuite.actuator.runtime.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JarUtil {

    private static Logger log = LoggerFactory.getLogger(JarUtil.class);

    public static List<String> findMatchingFiles(File jarFile, String extension) {
        List<String> result = new ArrayList<>();
        try (JarInputStream jarStream = new JarInputStream(new FileInputStream(jarFile))) {
            JarEntry entry;

            while ((entry = jarStream.getNextJarEntry()) != null) {
                if (entry.getName().endsWith(extension)) {
                    result.add(entry.getName());
                }
            }
        } catch (IOException ioe) {
            log.error(String.format("Problems reading from %s to find matching embedded %s files", jarFile.getName(), extension), ioe);
        }
        return result;
    }

    public static String extractFileContent(File jarFile, String filename) {
        try (JarInputStream jarStream = new JarInputStream(new FileInputStream(jarFile))) {
            JarEntry entry;

            while ((entry = jarStream.getNextJarEntry()) != null) {
                if (entry.getName().endsWith(filename)) {
                    return readFromStream(jarStream);
                }
            }
        } catch (IOException ioe) {
            log.error(String.format("Problems reading from %s to extract contents of embedded %s file", jarFile.getName(), filename), ioe);
        }
        return null;
    }

    private static String readFromStream(JarInputStream jarStream) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(jarStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

}