package fr.xephi.authme.libs.com.google.common.io;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Optional;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Predicate;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.common.collect.Iterables;
import fr.xephi.authme.libs.com.google.common.graph.SuccessorsFunction;
import fr.xephi.authme.libs.com.google.common.graph.Traverser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SecureDirectoryStream;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class MoreFiles {
   private static final SuccessorsFunction<Path> FILE_TREE = new SuccessorsFunction<Path>() {
      public Iterable<Path> successors(Path path) {
         return MoreFiles.fileTreeChildren(path);
      }
   };

   private MoreFiles() {
   }

   public static ByteSource asByteSource(Path path, OpenOption... options) {
      return new MoreFiles.PathByteSource(path, options);
   }

   public static ByteSink asByteSink(Path path, OpenOption... options) {
      return new MoreFiles.PathByteSink(path, options);
   }

   public static CharSource asCharSource(Path path, Charset charset, OpenOption... options) {
      return asByteSource(path, options).asCharSource(charset);
   }

   public static CharSink asCharSink(Path path, Charset charset, OpenOption... options) {
      return asByteSink(path, options).asCharSink(charset);
   }

   public static ImmutableList<Path> listFiles(Path dir) throws IOException {
      try {
         DirectoryStream stream = java.nio.file.Files.newDirectoryStream(dir);

         ImmutableList var2;
         try {
            var2 = ImmutableList.copyOf((Iterable)stream);
         } catch (Throwable var5) {
            if (stream != null) {
               try {
                  stream.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (stream != null) {
            stream.close();
         }

         return var2;
      } catch (DirectoryIteratorException var6) {
         throw var6.getCause();
      }
   }

   public static Traverser<Path> fileTraverser() {
      return Traverser.forTree(FILE_TREE);
   }

   private static Iterable<Path> fileTreeChildren(Path dir) {
      if (java.nio.file.Files.isDirectory(dir, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
         try {
            return listFiles(dir);
         } catch (IOException var2) {
            throw new DirectoryIteratorException(var2);
         }
      } else {
         return ImmutableList.of();
      }
   }

   public static Predicate<Path> isDirectory(LinkOption... options) {
      final LinkOption[] optionsCopy = (LinkOption[])options.clone();
      return new Predicate<Path>() {
         public boolean apply(Path input) {
            return java.nio.file.Files.isDirectory(input, optionsCopy);
         }

         public String toString() {
            String var1 = Arrays.toString(optionsCopy);
            return (new StringBuilder(23 + String.valueOf(var1).length())).append("MoreFiles.isDirectory(").append(var1).append(")").toString();
         }
      };
   }

   private static boolean isDirectory(SecureDirectoryStream<Path> dir, Path name, LinkOption... options) throws IOException {
      return ((BasicFileAttributeView)dir.getFileAttributeView(name, BasicFileAttributeView.class, options)).readAttributes().isDirectory();
   }

   public static Predicate<Path> isRegularFile(LinkOption... options) {
      final LinkOption[] optionsCopy = (LinkOption[])options.clone();
      return new Predicate<Path>() {
         public boolean apply(Path input) {
            return java.nio.file.Files.isRegularFile(input, optionsCopy);
         }

         public String toString() {
            String var1 = Arrays.toString(optionsCopy);
            return (new StringBuilder(25 + String.valueOf(var1).length())).append("MoreFiles.isRegularFile(").append(var1).append(")").toString();
         }
      };
   }

   public static boolean equal(Path path1, Path path2) throws IOException {
      Preconditions.checkNotNull(path1);
      Preconditions.checkNotNull(path2);
      if (java.nio.file.Files.isSameFile(path1, path2)) {
         return true;
      } else {
         ByteSource source1 = asByteSource(path1);
         ByteSource source2 = asByteSource(path2);
         long len1 = (Long)source1.sizeIfKnown().or((Object)0L);
         long len2 = (Long)source2.sizeIfKnown().or((Object)0L);
         return len1 != 0L && len2 != 0L && len1 != len2 ? false : source1.contentEquals(source2);
      }
   }

   public static void touch(Path path) throws IOException {
      Preconditions.checkNotNull(path);

      try {
         java.nio.file.Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
      } catch (NoSuchFileException var4) {
         try {
            java.nio.file.Files.createFile(path);
         } catch (FileAlreadyExistsException var3) {
         }
      }

   }

   public static void createParentDirectories(Path path, FileAttribute<?>... attrs) throws IOException {
      Path normalizedAbsolutePath = path.toAbsolutePath().normalize();
      Path parent = normalizedAbsolutePath.getParent();
      if (parent != null) {
         if (!java.nio.file.Files.isDirectory(parent, new LinkOption[0])) {
            java.nio.file.Files.createDirectories(parent, attrs);
            if (!java.nio.file.Files.isDirectory(parent, new LinkOption[0])) {
               String var4 = String.valueOf(path);
               throw new IOException((new StringBuilder(39 + String.valueOf(var4).length())).append("Unable to create parent directories of ").append(var4).toString());
            }
         }

      }
   }

   public static String getFileExtension(Path path) {
      Path name = path.getFileName();
      if (name == null) {
         return "";
      } else {
         String fileName = name.toString();
         int dotIndex = fileName.lastIndexOf(46);
         return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
      }
   }

   public static String getNameWithoutExtension(Path path) {
      Path name = path.getFileName();
      if (name == null) {
         return "";
      } else {
         String fileName = name.toString();
         int dotIndex = fileName.lastIndexOf(46);
         return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
      }
   }

   public static void deleteRecursively(Path path, RecursiveDeleteOption... options) throws IOException {
      Path parentPath = getParentPath(path);
      if (parentPath == null) {
         throw new FileSystemException(path.toString(), (String)null, "can't delete recursively");
      } else {
         Collection exceptions = null;

         try {
            boolean sdsSupported = false;
            DirectoryStream parent = java.nio.file.Files.newDirectoryStream(parentPath);

            try {
               if (parent instanceof SecureDirectoryStream) {
                  sdsSupported = true;
                  exceptions = deleteRecursivelySecure((SecureDirectoryStream)parent, (Path)Objects.requireNonNull(path.getFileName()));
               }
            } catch (Throwable var9) {
               if (parent != null) {
                  try {
                     parent.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (parent != null) {
               parent.close();
            }

            if (!sdsSupported) {
               checkAllowsInsecure(path, options);
               exceptions = deleteRecursivelyInsecure(path);
            }
         } catch (IOException var10) {
            if (exceptions == null) {
               throw var10;
            }

            exceptions.add(var10);
         }

         if (exceptions != null) {
            throwDeleteFailed(path, exceptions);
         }

      }
   }

   public static void deleteDirectoryContents(Path path, RecursiveDeleteOption... options) throws IOException {
      Collection exceptions = null;

      try {
         DirectoryStream stream = java.nio.file.Files.newDirectoryStream(path);

         try {
            if (stream instanceof SecureDirectoryStream) {
               SecureDirectoryStream<Path> sds = (SecureDirectoryStream)stream;
               exceptions = deleteDirectoryContentsSecure(sds);
            } else {
               checkAllowsInsecure(path, options);
               exceptions = deleteDirectoryContentsInsecure(stream);
            }
         } catch (Throwable var7) {
            if (stream != null) {
               try {
                  stream.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (stream != null) {
            stream.close();
         }
      } catch (IOException var8) {
         if (exceptions == null) {
            throw var8;
         }

         exceptions.add(var8);
      }

      if (exceptions != null) {
         throwDeleteFailed(path, exceptions);
      }

   }

   @CheckForNull
   private static Collection<IOException> deleteRecursivelySecure(SecureDirectoryStream<Path> dir, Path path) {
      Collection exceptions = null;

      try {
         if (isDirectory(dir, path, LinkOption.NOFOLLOW_LINKS)) {
            SecureDirectoryStream childDir = dir.newDirectoryStream(path, LinkOption.NOFOLLOW_LINKS);

            try {
               exceptions = deleteDirectoryContentsSecure(childDir);
            } catch (Throwable var7) {
               if (childDir != null) {
                  try {
                     childDir.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (childDir != null) {
               childDir.close();
            }

            if (exceptions == null) {
               dir.deleteDirectory(path);
            }
         } else {
            dir.deleteFile(path);
         }

         return exceptions;
      } catch (IOException var8) {
         return addException(exceptions, var8);
      }
   }

   @CheckForNull
   private static Collection<IOException> deleteDirectoryContentsSecure(SecureDirectoryStream<Path> dir) {
      Collection exceptions = null;

      try {
         Path path;
         for(Iterator var2 = dir.iterator(); var2.hasNext(); exceptions = concat(exceptions, deleteRecursivelySecure(dir, path.getFileName()))) {
            path = (Path)var2.next();
         }

         return exceptions;
      } catch (DirectoryIteratorException var4) {
         return addException(exceptions, var4.getCause());
      }
   }

   @CheckForNull
   private static Collection<IOException> deleteRecursivelyInsecure(Path path) {
      Collection exceptions = null;

      try {
         if (java.nio.file.Files.isDirectory(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
            DirectoryStream stream = java.nio.file.Files.newDirectoryStream(path);

            try {
               exceptions = deleteDirectoryContentsInsecure(stream);
            } catch (Throwable var6) {
               if (stream != null) {
                  try {
                     stream.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (stream != null) {
               stream.close();
            }
         }

         if (exceptions == null) {
            java.nio.file.Files.delete(path);
         }

         return exceptions;
      } catch (IOException var7) {
         return addException(exceptions, var7);
      }
   }

   @CheckForNull
   private static Collection<IOException> deleteDirectoryContentsInsecure(DirectoryStream<Path> dir) {
      Collection exceptions = null;

      try {
         Path entry;
         for(Iterator var2 = dir.iterator(); var2.hasNext(); exceptions = concat(exceptions, deleteRecursivelyInsecure(entry))) {
            entry = (Path)var2.next();
         }

         return exceptions;
      } catch (DirectoryIteratorException var4) {
         return addException(exceptions, var4.getCause());
      }
   }

   @CheckForNull
   private static Path getParentPath(Path path) {
      Path parent = path.getParent();
      if (parent != null) {
         return parent;
      } else {
         return path.getNameCount() == 0 ? null : path.getFileSystem().getPath(".");
      }
   }

   private static void checkAllowsInsecure(Path path, RecursiveDeleteOption[] options) throws InsecureRecursiveDeleteException {
      if (!Arrays.asList(options).contains(RecursiveDeleteOption.ALLOW_INSECURE)) {
         throw new InsecureRecursiveDeleteException(path.toString());
      }
   }

   private static Collection<IOException> addException(@CheckForNull Collection<IOException> exceptions, IOException e) {
      if (exceptions == null) {
         exceptions = new ArrayList();
      }

      ((Collection)exceptions).add(e);
      return (Collection)exceptions;
   }

   @CheckForNull
   private static Collection<IOException> concat(@CheckForNull Collection<IOException> exceptions, @CheckForNull Collection<IOException> other) {
      if (exceptions == null) {
         return other;
      } else {
         if (other != null) {
            exceptions.addAll(other);
         }

         return exceptions;
      }
   }

   private static void throwDeleteFailed(Path path, Collection<IOException> exceptions) throws FileSystemException {
      NoSuchFileException pathNotFound = pathNotFound(path, exceptions);
      if (pathNotFound != null) {
         throw pathNotFound;
      } else {
         FileSystemException deleteFailed = new FileSystemException(path.toString(), (String)null, "failed to delete one or more files; see suppressed exceptions for details");
         Iterator var4 = exceptions.iterator();

         while(var4.hasNext()) {
            IOException e = (IOException)var4.next();
            deleteFailed.addSuppressed(e);
         }

         throw deleteFailed;
      }
   }

   @CheckForNull
   private static NoSuchFileException pathNotFound(Path path, Collection<IOException> exceptions) {
      if (exceptions.size() != 1) {
         return null;
      } else {
         IOException exception = (IOException)Iterables.getOnlyElement(exceptions);
         if (!(exception instanceof NoSuchFileException)) {
            return null;
         } else {
            NoSuchFileException noSuchFileException = (NoSuchFileException)exception;
            String exceptionFile = noSuchFileException.getFile();
            if (exceptionFile == null) {
               return null;
            } else {
               Path parentPath = getParentPath(path);
               if (parentPath == null) {
                  return null;
               } else {
                  Path pathResolvedFromParent = parentPath.resolve((Path)Objects.requireNonNull(path.getFileName()));
                  return exceptionFile.equals(pathResolvedFromParent.toString()) ? noSuchFileException : null;
               }
            }
         }
      }
   }

   private static final class PathByteSink extends ByteSink {
      private final Path path;
      private final OpenOption[] options;

      private PathByteSink(Path path, OpenOption... options) {
         this.path = (Path)Preconditions.checkNotNull(path);
         this.options = (OpenOption[])options.clone();
      }

      public OutputStream openStream() throws IOException {
         return java.nio.file.Files.newOutputStream(this.path, this.options);
      }

      public String toString() {
         String var1 = String.valueOf(this.path);
         String var2 = Arrays.toString(this.options);
         return (new StringBuilder(24 + String.valueOf(var1).length() + String.valueOf(var2).length())).append("MoreFiles.asByteSink(").append(var1).append(", ").append(var2).append(")").toString();
      }

      // $FF: synthetic method
      PathByteSink(Path x0, OpenOption[] x1, Object x2) {
         this(x0, x1);
      }
   }

   private static final class PathByteSource extends ByteSource {
      private static final LinkOption[] FOLLOW_LINKS = new LinkOption[0];
      private final Path path;
      private final OpenOption[] options;
      private final boolean followLinks;

      private PathByteSource(Path path, OpenOption... options) {
         this.path = (Path)Preconditions.checkNotNull(path);
         this.options = (OpenOption[])options.clone();
         this.followLinks = followLinks(this.options);
      }

      private static boolean followLinks(OpenOption[] options) {
         OpenOption[] var1 = options;
         int var2 = options.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            OpenOption option = var1[var3];
            if (option == LinkOption.NOFOLLOW_LINKS) {
               return false;
            }
         }

         return true;
      }

      public InputStream openStream() throws IOException {
         return java.nio.file.Files.newInputStream(this.path, this.options);
      }

      private BasicFileAttributes readAttributes() throws IOException {
         return java.nio.file.Files.readAttributes(this.path, BasicFileAttributes.class, this.followLinks ? FOLLOW_LINKS : new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
      }

      public Optional<Long> sizeIfKnown() {
         BasicFileAttributes attrs;
         try {
            attrs = this.readAttributes();
         } catch (IOException var3) {
            return Optional.absent();
         }

         return !attrs.isDirectory() && !attrs.isSymbolicLink() ? Optional.of(attrs.size()) : Optional.absent();
      }

      public long size() throws IOException {
         BasicFileAttributes attrs = this.readAttributes();
         if (attrs.isDirectory()) {
            throw new IOException("can't read: is a directory");
         } else if (attrs.isSymbolicLink()) {
            throw new IOException("can't read: is a symbolic link");
         } else {
            return attrs.size();
         }
      }

      public byte[] read() throws IOException {
         SeekableByteChannel channel = java.nio.file.Files.newByteChannel(this.path, this.options);

         byte[] var2;
         try {
            var2 = ByteStreams.toByteArray(Channels.newInputStream(channel), channel.size());
         } catch (Throwable var5) {
            if (channel != null) {
               try {
                  channel.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (channel != null) {
            channel.close();
         }

         return var2;
      }

      public CharSource asCharSource(Charset charset) {
         return (CharSource)(this.options.length == 0 ? new ByteSource.AsCharSource(charset) {
            public Stream<String> lines() throws IOException {
               return java.nio.file.Files.lines(PathByteSource.this.path, this.charset);
            }
         } : super.asCharSource(charset));
      }

      public String toString() {
         String var1 = String.valueOf(this.path);
         String var2 = Arrays.toString(this.options);
         return (new StringBuilder(26 + String.valueOf(var1).length() + String.valueOf(var2).length())).append("MoreFiles.asByteSource(").append(var1).append(", ").append(var2).append(")").toString();
      }

      // $FF: synthetic method
      PathByteSource(Path x0, OpenOption[] x1, Object x2) {
         this(x0, x1);
      }
   }
}
