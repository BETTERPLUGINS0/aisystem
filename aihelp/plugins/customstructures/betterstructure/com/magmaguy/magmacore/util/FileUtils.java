/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.magmacore.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileUtils {
    private FileUtils() {
    }

    public static void deleteDirectory(Path directory) throws IOException {
        if (directory == null || !Files.exists(directory, new LinkOption[0])) {
            return;
        }
        Files.walkFileTree(directory, (FileVisitor<? super Path>)new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) {
                    throw exc;
                }
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void deleteDirectory(File directory) throws IOException {
        if (directory == null) {
            return;
        }
        FileUtils.deleteDirectory(directory.toPath());
    }
}

