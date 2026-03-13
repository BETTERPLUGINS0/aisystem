/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.magmacore.util;

import com.magmaguy.magmacore.util.Logger;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFile {
    private ZipFile() {
    }

    public static boolean zip(File directory, String targetZipPath) {
        if (!directory.exists()) {
            Logger.warn("Failed to zip directory " + directory.getPath() + " because it does not exist!");
            return false;
        }
        try {
            ZipUtility.zip(directory, targetZipPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static File unzip(File zippedFile, File destinationUnzippedFile) throws IOException {
        byte[] buffer = new byte[8192];
        try (ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zippedFile)));){
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                long entryTime;
                boolean isDirectory;
                File newFile = ZipFile.newFile(destinationUnzippedFile, zipEntry);
                String entryName = zipEntry.getName();
                boolean bl = isDirectory = zipEntry.isDirectory() || entryName.endsWith("\\") || entryName.endsWith("/");
                if (isDirectory) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + String.valueOf(newFile));
                    }
                } else {
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + String.valueOf(parent));
                    }
                    try (FileOutputStream fileOutputStream = new FileOutputStream(newFile);){
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                    }
                }
                if ((entryTime = zipEntry.getTime()) >= 0L) {
                    newFile.setLastModified(entryTime);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        }
        return destinationUnzippedFile;
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        String entryName = zipEntry.getName().replace('\\', '/');
        if (entryName.endsWith("/")) {
            entryName = entryName.substring(0, entryName.length() - 1);
        }
        File destFile = new File(destinationDir, entryName);
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separatorChar)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    public static class ZipUtility {
        private static final int BUFFER_SIZE = 4096;

        public static void zip(File file, String destZipFile) throws FileNotFoundException, IOException {
            FileOutputStream fileOutputStream = new FileOutputStream(destZipFile);
            ZipOutputStream zos = new ZipOutputStream(fileOutputStream);
            if (file.isDirectory()) {
                for (File file1 : file.listFiles()) {
                    if (file1.isDirectory()) {
                        ZipUtility.zipDirectory(file1, file1.getName(), zos);
                        continue;
                    }
                    ZipUtility.zipFile(file1, zos);
                }
            } else {
                ZipUtility.zipFile(file, zos);
            }
            zos.flush();
            zos.close();
            fileOutputStream.close();
        }

        private static void zipDirectory(File folder, String parentFolder, ZipOutputStream zos) throws FileNotFoundException, IOException {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    ZipUtility.zipDirectory(file, parentFolder + "/" + file.getName(), zos);
                    continue;
                }
                ZipEntry zipEntry = new ZipEntry(parentFolder + "/" + file.getName());
                ZipUtility.zippedySplit(zos, file, zipEntry);
            }
        }

        private static void zipFile(File file, ZipOutputStream zos) throws FileNotFoundException, IOException {
            if (file.getName().endsWith(".zip")) {
                return;
            }
            ZipEntry zipEntry = new ZipEntry(file.getName());
            ZipUtility.zippedySplit(zos, file, zipEntry);
        }

        private static void zippedySplit(ZipOutputStream zos, File file, ZipEntry zipEntry) throws IOException {
            int read;
            zipEntry.setTime(0L);
            zos.putNextEntry(zipEntry);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] bytesIn = new byte[4096];
            while ((read = bis.read(bytesIn)) != -1) {
                zos.write(bytesIn, 0, read);
            }
            zos.closeEntry();
            bis.close();
        }
    }
}

