package org.apache.commons.io.file.spi;

import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class FileSystemProviders {
   private static final FileSystemProviders INSTALLED = new FileSystemProviders(FileSystemProvider.installedProviders());
   private final List<FileSystemProvider> providers;

   public static FileSystemProvider getFileSystemProvider(Path var0) {
      return ((Path)Objects.requireNonNull(var0, "path")).getFileSystem().provider();
   }

   public static FileSystemProviders installed() {
      return INSTALLED;
   }

   private FileSystemProviders(List<FileSystemProvider> var1) {
      this.providers = var1;
   }

   public FileSystemProvider getFileSystemProvider(String var1) {
      Objects.requireNonNull(var1, "scheme");
      if (var1.equalsIgnoreCase("file")) {
         return FileSystems.getDefault().provider();
      } else {
         if (this.providers != null) {
            Iterator var2 = this.providers.iterator();

            while(var2.hasNext()) {
               FileSystemProvider var3 = (FileSystemProvider)var2.next();
               if (var3.getScheme().equalsIgnoreCase(var1)) {
                  return var3;
               }
            }
         }

         return null;
      }
   }

   public FileSystemProvider getFileSystemProvider(URI var1) {
      return this.getFileSystemProvider(((URI)Objects.requireNonNull(var1, "uri")).getScheme());
   }

   public FileSystemProvider getFileSystemProvider(URL var1) {
      return this.getFileSystemProvider(((URL)Objects.requireNonNull(var1, "url")).getProtocol());
   }
}
