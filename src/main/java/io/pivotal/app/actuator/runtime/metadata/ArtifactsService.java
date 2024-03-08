package io.pivotal.app.actuator.runtime.metadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArtifactsService {

    private static Logger log = LoggerFactory.getLogger(ArtifactsService.class);

    public Set<String> findJars() {
        Set<String> jarFiles = new HashSet<>();

        String  classpath = System.getProperty("java.class.path");
        log.trace("java.class.path is currently set to: {}", classpath);

        String[] classpathEntries = classpath.split(File.pathSeparator);

        if (classpath.contains("/home/vcap/app")) {
            try {
                jarFiles = findJarFiles(Path.of("/home", "vcap", "app"));
            } catch (IOException ioe) {
                log.warn("Trouble fetching .jar files from java.class.path", ioe);
            }
        } else {
            for (String classpathEntry : classpathEntries) {
                File entryFile = new File(classpathEntry);
                if (entryFile.isFile() && entryFile.getName().toLowerCase().endsWith(".jar")) {
                    jarFiles.addAll(JarUtil.findMatchingFiles(entryFile, ".jar"));
                }
            }
        }

        return jarFiles;
    }

    private Set<String> findJarFiles(Path directory) throws IOException {
        Set<String> jarFiles = new HashSet<>();
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".jar")) {
                    jarFiles.add(file.getFileName().toString());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        return jarFiles;
    }

    public String findPom() {
        String pom = "";

        var classpath = System.getProperty("java.class.path");
        var classpathEntries = classpath.split(File.pathSeparator);

        for (String classpathEntry : classpathEntries) {
            File entryFile = new File(classpathEntry);
            if (entryFile.isFile() && entryFile.getName().toLowerCase().endsWith(".jar")) {
                pom = JarUtil.extractFileContent(entryFile, "pom.xml");
                break;
            }
        }

        return pom;
    }
}


