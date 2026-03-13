package org.apache.commons.io.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.IOExceptionList;
import org.apache.commons.io.IOUtils;

public final class PathUtils {
   public static final CopyOption[] EMPTY_COPY_OPTIONS = new CopyOption[0];
   public static final DeleteOption[] EMPTY_DELETE_OPTION_ARRAY = new DeleteOption[0];
   public static final FileVisitOption[] EMPTY_FILE_VISIT_OPTION_ARRAY = new FileVisitOption[0];
   public static final LinkOption[] EMPTY_LINK_OPTION_ARRAY = new LinkOption[0];
   public static final LinkOption[] NOFOLLOW_LINK_OPTION_ARRAY;
   public static final OpenOption[] EMPTY_OPEN_OPTION_ARRAY;
   public static final Path[] EMPTY_PATH_ARRAY;

   private static AccumulatorPathVisitor accumulate(Path var0, int var1, FileVisitOption[] var2) {
      return (AccumulatorPathVisitor)visitFileTree(AccumulatorPathVisitor.withLongCounters(), var0, toFileVisitOptionSet(var2), var1);
   }

   public static Counters.PathCounters cleanDirectory(Path var0) {
      return cleanDirectory(var0, EMPTY_DELETE_OPTION_ARRAY);
   }

   public static Counters.PathCounters cleanDirectory(Path var0, DeleteOption... var1) {
      return ((CleaningPathVisitor)visitFileTree(new CleaningPathVisitor(Counters.longPathCounters(), var1, new String[0]), (Path)var0)).getPathCounters();
   }

   public static Counters.PathCounters copyDirectory(Path var0, Path var1, CopyOption... var2) {
      Path var3 = var0.toAbsolutePath();
      return ((CopyDirectoryVisitor)visitFileTree(new CopyDirectoryVisitor(Counters.longPathCounters(), var3, var1, var2), (Path)var3)).getPathCounters();
   }

   public static Path copyFile(URL var0, Path var1, CopyOption... var2) {
      InputStream var3 = var0.openStream();

      Path var4;
      try {
         Files.copy(var3, var1, var2);
         var4 = var1;
      } catch (Throwable var7) {
         if (var3 != null) {
            try {
               var3.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (var3 != null) {
         var3.close();
      }

      return var4;
   }

   public static Path copyFileToDirectory(Path var0, Path var1, CopyOption... var2) {
      return Files.copy(var0, var1.resolve(var0.getFileName()), var2);
   }

   public static Path copyFileToDirectory(URL var0, Path var1, CopyOption... var2) {
      InputStream var3 = var0.openStream();

      Path var4;
      try {
         Files.copy(var3, var1.resolve(var0.getFile()), var2);
         var4 = var1;
      } catch (Throwable var7) {
         if (var3 != null) {
            try {
               var3.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (var3 != null) {
         var3.close();
      }

      return var4;
   }

   public static Counters.PathCounters countDirectory(Path var0) {
      return ((CountingPathVisitor)visitFileTree(new CountingPathVisitor(Counters.longPathCounters()), (Path)var0)).getPathCounters();
   }

   public static Path createParentDirectories(Path var0, FileAttribute<?>... var1) {
      Path var2 = var0.getParent();
      return var2 == null ? null : Files.createDirectories(var2, var1);
   }

   public static Path current() {
      return Paths.get("");
   }

   public static Counters.PathCounters delete(Path var0) {
      return delete(var0, EMPTY_DELETE_OPTION_ARRAY);
   }

   public static Counters.PathCounters delete(Path var0, DeleteOption... var1) {
      return Files.isDirectory(var0, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}) ? deleteDirectory(var0, var1) : deleteFile(var0, var1);
   }

   public static Counters.PathCounters delete(Path var0, LinkOption[] var1, DeleteOption... var2) {
      return Files.isDirectory(var0, var1) ? deleteDirectory(var0, var1, var2) : deleteFile(var0, var1, var2);
   }

   public static Counters.PathCounters deleteDirectory(Path var0) {
      return deleteDirectory(var0, EMPTY_DELETE_OPTION_ARRAY);
   }

   public static Counters.PathCounters deleteDirectory(Path var0, DeleteOption... var1) {
      return ((DeletingPathVisitor)visitFileTree(new DeletingPathVisitor(Counters.longPathCounters(), NOFOLLOW_LINK_OPTION_ARRAY, var1, new String[0]), (Path)var0)).getPathCounters();
   }

   public static Counters.PathCounters deleteDirectory(Path var0, LinkOption[] var1, DeleteOption... var2) {
      return ((DeletingPathVisitor)visitFileTree(new DeletingPathVisitor(Counters.longPathCounters(), var1, var2, new String[0]), (Path)var0)).getPathCounters();
   }

   public static Counters.PathCounters deleteFile(Path var0) {
      return deleteFile(var0, EMPTY_DELETE_OPTION_ARRAY);
   }

   public static Counters.PathCounters deleteFile(Path var0, DeleteOption... var1) {
      return deleteFile(var0, NOFOLLOW_LINK_OPTION_ARRAY, var1);
   }

   public static Counters.PathCounters deleteFile(Path var0, LinkOption[] var1, DeleteOption... var2) {
      if (Files.isDirectory(var0, var1)) {
         throw new NoSuchFileException(var0.toString());
      } else {
         Counters.PathCounters var3 = Counters.longPathCounters();
         boolean var4 = Files.exists(var0, var1);
         long var5 = var4 && !Files.isSymbolicLink(var0) ? Files.size(var0) : 0L;
         if (overrideReadOnly(var2) && var4) {
            setReadOnly(var0, false, var1);
         }

         if (Files.deleteIfExists(var0)) {
            var3.getFileCounter().increment();
            var3.getByteCounter().add(var5);
         }

         return var3;
      }
   }

   public static boolean directoryAndFileContentEquals(Path var0, Path var1) {
      return directoryAndFileContentEquals(var0, var1, EMPTY_LINK_OPTION_ARRAY, EMPTY_OPEN_OPTION_ARRAY, EMPTY_FILE_VISIT_OPTION_ARRAY);
   }

   public static boolean directoryAndFileContentEquals(Path var0, Path var1, LinkOption[] var2, OpenOption[] var3, FileVisitOption[] var4) {
      if (var0 == null && var1 == null) {
         return true;
      } else if (var0 != null && var1 != null) {
         if (Files.notExists(var0, new LinkOption[0]) && Files.notExists(var1, new LinkOption[0])) {
            return true;
         } else {
            PathUtils.RelativeSortedPaths var5 = new PathUtils.RelativeSortedPaths(var0, var1, Integer.MAX_VALUE, var2, var4);
            if (!var5.equals) {
               return false;
            } else {
               List var6 = var5.relativeFileList1;
               List var7 = var5.relativeFileList2;
               Iterator var8 = var6.iterator();

               Path var9;
               do {
                  if (!var8.hasNext()) {
                     return true;
                  }

                  var9 = (Path)var8.next();
                  int var10 = Collections.binarySearch(var7, var9);
                  if (var10 <= -1) {
                     throw new IllegalStateException("Unexpected mismatch.");
                  }
               } while(fileContentEquals(var0.resolve(var9), var1.resolve(var9), var2, var3));

               return false;
            }
         }
      } else {
         return false;
      }
   }

   public static boolean directoryContentEquals(Path var0, Path var1) {
      return directoryContentEquals(var0, var1, Integer.MAX_VALUE, EMPTY_LINK_OPTION_ARRAY, EMPTY_FILE_VISIT_OPTION_ARRAY);
   }

   public static boolean directoryContentEquals(Path var0, Path var1, int var2, LinkOption[] var3, FileVisitOption[] var4) {
      return (new PathUtils.RelativeSortedPaths(var0, var1, var2, var3, var4)).equals;
   }

   public static boolean fileContentEquals(Path var0, Path var1) {
      return fileContentEquals(var0, var1, EMPTY_LINK_OPTION_ARRAY, EMPTY_OPEN_OPTION_ARRAY);
   }

   public static boolean fileContentEquals(Path var0, Path var1, LinkOption[] var2, OpenOption[] var3) {
      if (var0 == null && var1 == null) {
         return true;
      } else if (var0 != null && var1 != null) {
         Path var4 = var0.normalize();
         Path var5 = var1.normalize();
         boolean var6 = Files.exists(var4, var2);
         if (var6 != Files.exists(var5, var2)) {
            return false;
         } else if (!var6) {
            return true;
         } else if (Files.isDirectory(var4, var2)) {
            throw new IOException("Can't compare directories, only files: " + var4);
         } else if (Files.isDirectory(var5, var2)) {
            throw new IOException("Can't compare directories, only files: " + var5);
         } else if (Files.size(var4) != Files.size(var5)) {
            return false;
         } else if (var0.equals(var1)) {
            return true;
         } else {
            InputStream var7 = Files.newInputStream(var4, var3);

            boolean var9;
            try {
               InputStream var8 = Files.newInputStream(var5, var3);

               try {
                  var9 = IOUtils.contentEquals(var7, var8);
               } catch (Throwable var13) {
                  if (var8 != null) {
                     try {
                        var8.close();
                     } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                     }
                  }

                  throw var13;
               }

               if (var8 != null) {
                  var8.close();
               }
            } catch (Throwable var14) {
               if (var7 != null) {
                  try {
                     var7.close();
                  } catch (Throwable var11) {
                     var14.addSuppressed(var11);
                  }
               }

               throw var14;
            }

            if (var7 != null) {
               var7.close();
            }

            return var9;
         }
      } else {
         return false;
      }
   }

   public static Path[] filter(PathFilter var0, Path... var1) {
      Objects.requireNonNull(var0, "filter");
      return var1 == null ? EMPTY_PATH_ARRAY : (Path[])((List)filterPaths(var0, Stream.of(var1), Collectors.toList())).toArray(EMPTY_PATH_ARRAY);
   }

   private static <R, A> R filterPaths(PathFilter var0, Stream<Path> var1, Collector<? super Path, A, R> var2) {
      Objects.requireNonNull(var0, "filter");
      Objects.requireNonNull(var2, "collector");
      return var1 == null ? Stream.empty().collect(var2) : var1.filter((var1x) -> {
         try {
            return var1x != null && var0.accept(var1x, readBasicFileAttributes(var1x)) == FileVisitResult.CONTINUE;
         } catch (IOException var3) {
            return false;
         }
      }).collect(var2);
   }

   public static List<AclEntry> getAclEntryList(Path var0) {
      AclFileAttributeView var1 = (AclFileAttributeView)Files.getFileAttributeView(var0, AclFileAttributeView.class);
      return var1 == null ? null : var1.getAcl();
   }

   public static boolean isDirectory(Path var0, LinkOption... var1) {
      return var0 != null && Files.isDirectory(var0, var1);
   }

   public static boolean isEmpty(Path var0) {
      return Files.isDirectory(var0, new LinkOption[0]) ? isEmptyDirectory(var0) : isEmptyFile(var0);
   }

   public static boolean isEmptyDirectory(Path var0) {
      DirectoryStream var1 = Files.newDirectoryStream(var0);

      boolean var2;
      try {
         var2 = !var1.iterator().hasNext();
      } catch (Throwable var5) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (var1 != null) {
         var1.close();
      }

      return var2;
   }

   public static boolean isEmptyFile(Path var0) {
      return Files.size(var0) <= 0L;
   }

   public static boolean isNewer(Path var0, long var1, LinkOption... var3) {
      Objects.requireNonNull(var0, "file");
      if (Files.notExists(var0, new LinkOption[0])) {
         return false;
      } else {
         return Files.getLastModifiedTime(var0, var3).toMillis() > var1;
      }
   }

   public static boolean isRegularFile(Path var0, LinkOption... var1) {
      return var0 != null && Files.isRegularFile(var0, var1);
   }

   public static DirectoryStream<Path> newDirectoryStream(Path var0, PathFilter var1) {
      return Files.newDirectoryStream(var0, new DirectoryStreamFilter(var1));
   }

   private static boolean overrideReadOnly(DeleteOption... var0) {
      return var0 == null ? false : Stream.of(var0).anyMatch((var0x) -> {
         return var0x == StandardDeleteOption.OVERRIDE_READ_ONLY;
      });
   }

   public static BasicFileAttributes readBasicFileAttributes(Path var0) {
      return Files.readAttributes(var0, BasicFileAttributes.class);
   }

   public static BasicFileAttributes readBasicFileAttributesUnchecked(Path var0) {
      try {
         return readBasicFileAttributes(var0);
      } catch (IOException var2) {
         throw new UncheckedIOException(var2);
      }
   }

   static List<Path> relativize(Collection<Path> var0, Path var1, boolean var2, Comparator<? super Path> var3) {
      Stream var10000 = var0.stream();
      Objects.requireNonNull(var1);
      Stream var4 = var10000.map(var1::relativize);
      if (var2) {
         var4 = var3 == null ? var4.sorted() : var4.sorted(var3);
      }

      return (List)var4.collect(Collectors.toList());
   }

   public static Path setReadOnly(Path var0, boolean var1, LinkOption... var2) {
      ArrayList var3 = new ArrayList(2);
      DosFileAttributeView var4 = (DosFileAttributeView)Files.getFileAttributeView(var0, DosFileAttributeView.class, var2);
      if (var4 != null) {
         try {
            var4.setReadOnly(var1);
            return var0;
         } catch (IOException var10) {
            var3.add(var10);
         }
      }

      PosixFileAttributeView var5 = (PosixFileAttributeView)Files.getFileAttributeView(var0, PosixFileAttributeView.class, var2);
      if (var5 != null) {
         PosixFileAttributes var6 = var5.readAttributes();
         Set var7 = var6.permissions();
         var7.remove(PosixFilePermission.OWNER_WRITE);
         var7.remove(PosixFilePermission.GROUP_WRITE);
         var7.remove(PosixFilePermission.OTHERS_WRITE);

         try {
            return Files.setPosixFilePermissions(var0, var7);
         } catch (IOException var9) {
            var3.add(var9);
         }
      }

      if (!var3.isEmpty()) {
         throw new IOExceptionList(var0.toString(), var3);
      } else {
         throw new IOException(String.format("No DosFileAttributeView or PosixFileAttributeView for '%s' (linkOptions=%s)", var0, Arrays.toString(var2)));
      }
   }

   static Set<FileVisitOption> toFileVisitOptionSet(FileVisitOption... var0) {
      return (Set)(var0 == null ? EnumSet.noneOf(FileVisitOption.class) : (Set)Stream.of(var0).collect(Collectors.toSet()));
   }

   public static <T extends FileVisitor<? super Path>> T visitFileTree(T var0, Path var1) {
      Files.walkFileTree(var1, var0);
      return var0;
   }

   public static <T extends FileVisitor<? super Path>> T visitFileTree(T var0, Path var1, Set<FileVisitOption> var2, int var3) {
      Files.walkFileTree(var1, var2, var3, var0);
      return var0;
   }

   public static <T extends FileVisitor<? super Path>> T visitFileTree(T var0, String var1, String... var2) {
      return visitFileTree(var0, Paths.get(var1, var2));
   }

   public static <T extends FileVisitor<? super Path>> T visitFileTree(T var0, URI var1) {
      return visitFileTree(var0, Paths.get(var1));
   }

   public static Stream<Path> walk(Path var0, PathFilter var1, int var2, boolean var3, FileVisitOption... var4) {
      return Files.walk(var0, var2, var4).filter((var2x) -> {
         return var1.accept(var2x, var3 ? readBasicFileAttributesUnchecked(var2x) : null) == FileVisitResult.CONTINUE;
      });
   }

   private PathUtils() {
   }

   static {
      NOFOLLOW_LINK_OPTION_ARRAY = new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
      EMPTY_OPEN_OPTION_ARRAY = new OpenOption[0];
      EMPTY_PATH_ARRAY = new Path[0];
   }

   private static class RelativeSortedPaths {
      final boolean equals;
      final List<Path> relativeFileList1;
      final List<Path> relativeFileList2;

      private RelativeSortedPaths(Path var1, Path var2, int var3, LinkOption[] var4, FileVisitOption[] var5) {
         List var8 = null;
         List var9 = null;
         if (var1 == null && var2 == null) {
            this.equals = true;
         } else if (var1 == null ^ var2 == null) {
            this.equals = false;
         } else {
            boolean var10 = Files.notExists(var1, var4);
            boolean var11 = Files.notExists(var2, var4);
            if (!var10 && !var11) {
               AccumulatorPathVisitor var12 = PathUtils.accumulate(var1, var3, var5);
               AccumulatorPathVisitor var13 = PathUtils.accumulate(var2, var3, var5);
               if (var12.getDirList().size() == var13.getDirList().size() && var12.getFileList().size() == var13.getFileList().size()) {
                  List var6 = var12.relativizeDirectories(var1, true, (Comparator)null);
                  List var7 = var13.relativizeDirectories(var2, true, (Comparator)null);
                  if (!var6.equals(var7)) {
                     this.equals = false;
                  } else {
                     var8 = var12.relativizeFiles(var1, true, (Comparator)null);
                     var9 = var13.relativizeFiles(var2, true, (Comparator)null);
                     this.equals = var8.equals(var9);
                  }
               } else {
                  this.equals = false;
               }
            } else {
               this.equals = var10 && var11;
            }
         }

         this.relativeFileList1 = var8;
         this.relativeFileList2 = var9;
      }

      // $FF: synthetic method
      RelativeSortedPaths(Path var1, Path var2, int var3, LinkOption[] var4, FileVisitOption[] var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }
   }
}
