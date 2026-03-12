package fr.xephi.authme.libs.com.google.common.io;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Joiner;
import fr.xephi.authme.libs.com.google.common.base.Optional;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Predicate;
import fr.xephi.authme.libs.com.google.common.base.Splitter;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableSet;
import fr.xephi.authme.libs.com.google.common.collect.Lists;
import fr.xephi.authme.libs.com.google.common.graph.SuccessorsFunction;
import fr.xephi.authme.libs.com.google.common.graph.Traverser;
import fr.xephi.authme.libs.com.google.common.hash.HashCode;
import fr.xephi.authme.libs.com.google.common.hash.HashFunction;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.InlineMe;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class Files {
   private static final int TEMP_DIR_ATTEMPTS = 10000;
   private static final SuccessorsFunction<File> FILE_TREE = new SuccessorsFunction<File>() {
      public Iterable<File> successors(File file) {
         if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
               return Collections.unmodifiableList(Arrays.asList(files));
            }
         }

         return ImmutableList.of();
      }
   };

   private Files() {
   }

   public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
      Preconditions.checkNotNull(file);
      Preconditions.checkNotNull(charset);
      return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
   }

   public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
      Preconditions.checkNotNull(file);
      Preconditions.checkNotNull(charset);
      return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
   }

   public static ByteSource asByteSource(File file) {
      return new Files.FileByteSource(file);
   }

   public static ByteSink asByteSink(File file, FileWriteMode... modes) {
      return new Files.FileByteSink(file, modes);
   }

   public static CharSource asCharSource(File file, Charset charset) {
      return asByteSource(file).asCharSource(charset);
   }

   public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes) {
      return asByteSink(file, modes).asCharSink(charset);
   }

   public static byte[] toByteArray(File file) throws IOException {
      return asByteSource(file).read();
   }

   /** @deprecated */
   @Deprecated
   @InlineMe(
      replacement = "Files.asCharSource(file, charset).read()",
      imports = {"fr.xephi.authme.libs.com.google.common.io.Files"}
   )
   public static String toString(File file, Charset charset) throws IOException {
      return asCharSource(file, charset).read();
   }

   public static void write(byte[] from, File to) throws IOException {
      asByteSink(to).write(from);
   }

   /** @deprecated */
   @Deprecated
   @InlineMe(
      replacement = "Files.asCharSink(to, charset).write(from)",
      imports = {"fr.xephi.authme.libs.com.google.common.io.Files"}
   )
   public static void write(CharSequence from, File to, Charset charset) throws IOException {
      asCharSink(to, charset).write(from);
   }

   public static void copy(File from, OutputStream to) throws IOException {
      asByteSource(from).copyTo(to);
   }

   public static void copy(File from, File to) throws IOException {
      Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
      asByteSource(from).copyTo(asByteSink(to));
   }

   /** @deprecated */
   @Deprecated
   @InlineMe(
      replacement = "Files.asCharSource(from, charset).copyTo(to)",
      imports = {"fr.xephi.authme.libs.com.google.common.io.Files"}
   )
   public static void copy(File from, Charset charset, Appendable to) throws IOException {
      asCharSource(from, charset).copyTo(to);
   }

   /** @deprecated */
   @Deprecated
   @InlineMe(
      replacement = "Files.asCharSink(to, charset, FileWriteMode.APPEND).write(from)",
      imports = {"fr.xephi.authme.libs.com.google.common.io.FileWriteMode", "fr.xephi.authme.libs.com.google.common.io.Files"}
   )
   public static void append(CharSequence from, File to, Charset charset) throws IOException {
      asCharSink(to, charset, FileWriteMode.APPEND).write(from);
   }

   public static boolean equal(File file1, File file2) throws IOException {
      Preconditions.checkNotNull(file1);
      Preconditions.checkNotNull(file2);
      if (file1 != file2 && !file1.equals(file2)) {
         long len1 = file1.length();
         long len2 = file2.length();
         return len1 != 0L && len2 != 0L && len1 != len2 ? false : asByteSource(file1).contentEquals(asByteSource(file2));
      } else {
         return true;
      }
   }

   /** @deprecated */
   @Deprecated
   @Beta
   public static File createTempDir() {
      File baseDir = new File(System.getProperty("java.io.tmpdir"));
      long var2 = System.currentTimeMillis();
      String baseName = (new StringBuilder(21)).append(var2).append("-").toString();

      for(int counter = 0; counter < 10000; ++counter) {
         File tempDir = new File(baseDir, (new StringBuilder(11 + String.valueOf(baseName).length())).append(baseName).append(counter).toString());
         if (tempDir.mkdir()) {
            return tempDir;
         }
      }

      throw new IllegalStateException((new StringBuilder(66 + String.valueOf(baseName).length() + String.valueOf(baseName).length())).append("Failed to create directory within 10000 attempts (tried ").append(baseName).append("0 to ").append(baseName).append(9999).append(')').toString());
   }

   public static void touch(File file) throws IOException {
      Preconditions.checkNotNull(file);
      if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
         String var1 = String.valueOf(file);
         throw new IOException((new StringBuilder(38 + String.valueOf(var1).length())).append("Unable to update modification time of ").append(var1).toString());
      }
   }

   public static void createParentDirs(File file) throws IOException {
      Preconditions.checkNotNull(file);
      File parent = file.getCanonicalFile().getParentFile();
      if (parent != null) {
         parent.mkdirs();
         if (!parent.isDirectory()) {
            String var2 = String.valueOf(file);
            throw new IOException((new StringBuilder(39 + String.valueOf(var2).length())).append("Unable to create parent directories of ").append(var2).toString());
         }
      }
   }

   public static void move(File from, File to) throws IOException {
      Preconditions.checkNotNull(from);
      Preconditions.checkNotNull(to);
      Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
      if (!from.renameTo(to)) {
         copy(from, to);
         if (!from.delete()) {
            String var2;
            if (!to.delete()) {
               var2 = String.valueOf(to);
               throw new IOException((new StringBuilder(17 + String.valueOf(var2).length())).append("Unable to delete ").append(var2).toString());
            }

            var2 = String.valueOf(from);
            throw new IOException((new StringBuilder(17 + String.valueOf(var2).length())).append("Unable to delete ").append(var2).toString());
         }
      }

   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @InlineMe(
      replacement = "Files.asCharSource(file, charset).readFirstLine()",
      imports = {"fr.xephi.authme.libs.com.google.common.io.Files"}
   )
   public static String readFirstLine(File file, Charset charset) throws IOException {
      return asCharSource(file, charset).readFirstLine();
   }

   public static List<String> readLines(File file, Charset charset) throws IOException {
      return (List)asCharSource(file, charset).readLines(new LineProcessor<List<String>>() {
         final List<String> result = Lists.newArrayList();

         public boolean processLine(String line) {
            this.result.add(line);
            return true;
         }

         public List<String> getResult() {
            return this.result;
         }
      });
   }

   /** @deprecated */
   @Deprecated
   @ParametricNullness
   @InlineMe(
      replacement = "Files.asCharSource(file, charset).readLines(callback)",
      imports = {"fr.xephi.authme.libs.com.google.common.io.Files"}
   )
   @CanIgnoreReturnValue
   public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException {
      return asCharSource(file, charset).readLines(callback);
   }

   /** @deprecated */
   @Deprecated
   @ParametricNullness
   @InlineMe(
      replacement = "Files.asByteSource(file).read(processor)",
      imports = {"fr.xephi.authme.libs.com.google.common.io.Files"}
   )
   @CanIgnoreReturnValue
   public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException {
      return asByteSource(file).read(processor);
   }

   /** @deprecated */
   @Deprecated
   @InlineMe(
      replacement = "Files.asByteSource(file).hash(hashFunction)",
      imports = {"fr.xephi.authme.libs.com.google.common.io.Files"}
   )
   public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
      return asByteSource(file).hash(hashFunction);
   }

   public static MappedByteBuffer map(File file) throws IOException {
      Preconditions.checkNotNull(file);
      return map(file, MapMode.READ_ONLY);
   }

   public static MappedByteBuffer map(File file, MapMode mode) throws IOException {
      return mapInternal(file, mode, -1L);
   }

   public static MappedByteBuffer map(File file, MapMode mode, long size) throws IOException {
      Preconditions.checkArgument(size >= 0L, "size (%s) may not be negative", size);
      return mapInternal(file, mode, size);
   }

   private static MappedByteBuffer mapInternal(File file, MapMode mode, long size) throws IOException {
      Preconditions.checkNotNull(file);
      Preconditions.checkNotNull(mode);
      Closer closer = Closer.create();

      MappedByteBuffer var7;
      try {
         RandomAccessFile raf = (RandomAccessFile)closer.register(new RandomAccessFile(file, mode == MapMode.READ_ONLY ? "r" : "rw"));
         FileChannel channel = (FileChannel)closer.register(raf.getChannel());
         var7 = channel.map(mode, 0L, size == -1L ? channel.size() : size);
      } catch (Throwable var11) {
         throw closer.rethrow(var11);
      } finally {
         closer.close();
      }

      return var7;
   }

   public static String simplifyPath(String pathname) {
      Preconditions.checkNotNull(pathname);
      if (pathname.length() == 0) {
         return ".";
      } else {
         Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
         List<String> path = new ArrayList();
         Iterator var3 = components.iterator();

         while(var3.hasNext()) {
            String component = (String)var3.next();
            byte var6 = -1;
            switch(component.hashCode()) {
            case 46:
               if (component.equals(".")) {
                  var6 = 0;
               }
               break;
            case 1472:
               if (component.equals("..")) {
                  var6 = 1;
               }
            }

            switch(var6) {
            case 0:
               break;
            case 1:
               if (path.size() > 0 && !((String)path.get(path.size() - 1)).equals("..")) {
                  path.remove(path.size() - 1);
                  break;
               }

               path.add("..");
               break;
            default:
               path.add(component);
            }
         }

         String result = Joiner.on('/').join((Iterable)path);
         if (pathname.charAt(0) == '/') {
            String var10001 = String.valueOf(result);
            String var10000;
            if (var10001.length() != 0) {
               var10000 = "/".concat(var10001);
            } else {
               String var10002 = new String;
               var10000 = var10002;
               var10002.<init>("/");
            }

            result = var10000;
         }

         while(result.startsWith("/../")) {
            result = result.substring(3);
         }

         if (result.equals("/..")) {
            result = "/";
         } else if ("".equals(result)) {
            result = ".";
         }

         return result;
      }
   }

   public static String getFileExtension(String fullName) {
      Preconditions.checkNotNull(fullName);
      String fileName = (new File(fullName)).getName();
      int dotIndex = fileName.lastIndexOf(46);
      return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
   }

   public static String getNameWithoutExtension(String file) {
      Preconditions.checkNotNull(file);
      String fileName = (new File(file)).getName();
      int dotIndex = fileName.lastIndexOf(46);
      return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
   }

   @Beta
   public static Traverser<File> fileTraverser() {
      return Traverser.forTree(FILE_TREE);
   }

   public static Predicate<File> isDirectory() {
      return Files.FilePredicate.IS_DIRECTORY;
   }

   public static Predicate<File> isFile() {
      return Files.FilePredicate.IS_FILE;
   }

   private static enum FilePredicate implements Predicate<File> {
      IS_DIRECTORY {
         public boolean apply(File file) {
            return file.isDirectory();
         }

         public String toString() {
            return "Files.isDirectory()";
         }
      },
      IS_FILE {
         public boolean apply(File file) {
            return file.isFile();
         }

         public String toString() {
            return "Files.isFile()";
         }
      };

      private FilePredicate() {
      }

      // $FF: synthetic method
      private static Files.FilePredicate[] $values() {
         return new Files.FilePredicate[]{IS_DIRECTORY, IS_FILE};
      }

      // $FF: synthetic method
      FilePredicate(Object x2) {
         this();
      }
   }

   private static final class FileByteSink extends ByteSink {
      private final File file;
      private final ImmutableSet<FileWriteMode> modes;

      private FileByteSink(File file, FileWriteMode... modes) {
         this.file = (File)Preconditions.checkNotNull(file);
         this.modes = ImmutableSet.copyOf((Object[])modes);
      }

      public FileOutputStream openStream() throws IOException {
         return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
      }

      public String toString() {
         String var1 = String.valueOf(this.file);
         String var2 = String.valueOf(this.modes);
         return (new StringBuilder(20 + String.valueOf(var1).length() + String.valueOf(var2).length())).append("Files.asByteSink(").append(var1).append(", ").append(var2).append(")").toString();
      }

      // $FF: synthetic method
      FileByteSink(File x0, FileWriteMode[] x1, Object x2) {
         this(x0, x1);
      }
   }

   private static final class FileByteSource extends ByteSource {
      private final File file;

      private FileByteSource(File file) {
         this.file = (File)Preconditions.checkNotNull(file);
      }

      public FileInputStream openStream() throws IOException {
         return new FileInputStream(this.file);
      }

      public Optional<Long> sizeIfKnown() {
         return this.file.isFile() ? Optional.of(this.file.length()) : Optional.absent();
      }

      public long size() throws IOException {
         if (!this.file.isFile()) {
            throw new FileNotFoundException(this.file.toString());
         } else {
            return this.file.length();
         }
      }

      public byte[] read() throws IOException {
         Closer closer = Closer.create();

         byte[] var3;
         try {
            FileInputStream in = (FileInputStream)closer.register(this.openStream());
            var3 = ByteStreams.toByteArray(in, in.getChannel().size());
         } catch (Throwable var7) {
            throw closer.rethrow(var7);
         } finally {
            closer.close();
         }

         return var3;
      }

      public String toString() {
         String var1 = String.valueOf(this.file);
         return (new StringBuilder(20 + String.valueOf(var1).length())).append("Files.asByteSource(").append(var1).append(")").toString();
      }

      // $FF: synthetic method
      FileByteSource(File x0, Object x1) {
         this(x0);
      }
   }
}
