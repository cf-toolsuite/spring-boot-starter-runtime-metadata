package io.pivotal.app.actuator.runtime.metadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArtifactsService {

    private Logger log = LoggerFactory.getLogger(ArtifactsService.class);

    public List<String> findJars() {
        List<String> jarFiles = new ArrayList<>();

        var classpath = System.getProperty("java.class.path");
        var classpathEntries = classpath.split(File.pathSeparator);

        for (String classpathEntry : classpathEntries) {
            File entryFile = new File(classpathEntry);
            if (entryFile.isFile() && entryFile.getName().toLowerCase().endsWith(".jar")) {
                jarFiles.addAll(JarUtil.findMatchingFiles(entryFile, ".jar"));
            }
        }

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


