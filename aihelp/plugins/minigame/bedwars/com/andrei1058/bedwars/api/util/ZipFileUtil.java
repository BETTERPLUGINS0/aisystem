/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class ZipFileUtil {
    public static void zipDirectory(File dir, File zipFile) throws IOException {
        FileOutputStream fout = new FileOutputStream(zipFile);
        ZipOutputStream zout = new ZipOutputStream(fout);
        ZipFileUtil.zipSubDirectory("", dir, zout);
        zout.close();
    }

    private static void zipSubDirectory(String basePath, File dir, ZipOutputStream zout) throws IOException {
        byte[] buffer = new byte[4096];
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            int length;
            if (file.isDirectory()) {
                String path = basePath + file.getName() + "/";
                zout.putNextEntry(new ZipEntry(path));
                ZipFileUtil.zipSubDirectory(path, file, zout);
                zout.closeEntry();
                continue;
            }
            FileInputStream fin = new FileInputStream(file);
            zout.putNextEntry(new ZipEntry(basePath + file.getName()));
            while ((length = fin.read(buffer)) > 0) {
                zout.write(buffer, 0, length);
            }
            zout.closeEntry();
            fin.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void unzipFileIntoDirectory(File file, File jiniHomeParentDir) throws IOException {
        if (!file.exists()) {
            return;
        }
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> files = zipFile.entries();
        FileOutputStream fos = null;
        while (files.hasMoreElements()) {
            try {
                int bytesRead;
                ZipEntry entry = files.nextElement();
                InputStream eis = zipFile.getInputStream(entry);
                byte[] buffer = new byte[1024];
                File f = new File(jiniHomeParentDir.getAbsolutePath(), entry.getName());
                if (entry.isDirectory()) {
                    f.mkdirs();
                    continue;
                }
                f.getParentFile().mkdirs();
                f.createNewFile();
                fos = new FileOutputStream(f);
                while ((bytesRead = eis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos == null) continue;
                try {
                    fos.close();
                } catch (IOException iOException) {}
            }
        }
    }
}

