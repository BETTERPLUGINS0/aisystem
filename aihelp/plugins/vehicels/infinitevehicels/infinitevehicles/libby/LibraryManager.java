package me.PM2.infinitevehicles.libby;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import me.PM2.infinitevehicles.libby.classloader.IsolatedClassLoader;
import me.PM2.infinitevehicles.libby.logging.LogLevel;
import me.PM2.infinitevehicles.libby.logging.Logger;
import me.PM2.infinitevehicles.libby.logging.adapters.LogAdapter;
import me.PM2.infinitevehicles.libby.relocation.Relocation;
import me.PM2.infinitevehicles.libby.relocation.RelocationHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class LibraryManager {
   protected final Logger logger;
   protected final Path saveDirectory;
   private final Set<String> repositories = new LinkedHashSet();
   private RelocationHelper relocator;
   private final Map<String, IsolatedClassLoader> isolatedLibraries = new HashMap();

   /** @deprecated */
   @Deprecated
   protected LibraryManager(LogAdapter logAdapter, Path dataDirectory) {
      this.logger = new Logger((LogAdapter)Objects.requireNonNull(var1, "logAdapter"));
      this.saveDirectory = ((Path)Objects.requireNonNull(var2, "dataDirectory")).toAbsolutePath().resolve("lib");
   }

   protected LibraryManager(LogAdapter logAdapter, Path dataDirectory, String directoryName) {
      this.logger = new Logger((LogAdapter)Objects.requireNonNull(var1, "logAdapter"));
      this.saveDirectory = ((Path)Objects.requireNonNull(var2, "dataDirectory")).toAbsolutePath().resolve((String)Objects.requireNonNull(var3, "directoryName"));
   }

   protected abstract void addToClasspath(Path file);

   protected void addToIsolatedClasspath(Library library, Path file) {
      String var4 = var1.getId();
      IsolatedClassLoader var3;
      if (var4 != null) {
         var3 = (IsolatedClassLoader)this.isolatedLibraries.computeIfAbsent(var4, (var0) -> {
            return new IsolatedClassLoader(new URL[0]);
         });
      } else {
         var3 = new IsolatedClassLoader(new URL[0]);
      }

      var3.addPath(var2);
   }

   public IsolatedClassLoader getIsolatedClassLoaderOf(String libraryId) {
      return (IsolatedClassLoader)this.isolatedLibraries.get(var1);
   }

   public LogLevel getLogLevel() {
      return this.logger.getLevel();
   }

   public void setLogLevel(LogLevel level) {
      this.logger.setLevel(var1);
   }

   public Collection<String> getRepositories() {
      LinkedList var1;
      synchronized(this.repositories) {
         var1 = new LinkedList(this.repositories);
      }

      return Collections.unmodifiableList(var1);
   }

   public void addRepository(String url) {
      String var2 = ((String)Objects.requireNonNull(var1, "url")).endsWith("/") ? var1 : var1 + '/';
      synchronized(this.repositories) {
         this.repositories.add(var2);
      }
   }

   public void addMavenLocal() {
      this.addRepository(Paths.get(System.getProperty("user.home")).resolve(".m2/repository").toUri().toString());
   }

   public void addMavenCentral() {
      this.addRepository("https://repo1.maven.org/maven2/");
   }

   public void addSonatype() {
      this.addRepository("https://oss.sonatype.org/content/groups/public/");
   }

   public void addJCenter() {
      this.addRepository("https://jcenter.bintray.com/");
   }

   public void addJitPack() {
      this.addRepository("https://jitpack.io/");
   }

   public Collection<String> resolveLibrary(Library library) {
      LinkedHashSet var2 = new LinkedHashSet(((Library)Objects.requireNonNull(var1, "library")).getUrls());
      boolean var3 = var1.isSnapshot();
      Iterator var4 = var1.getRepositories().iterator();

      String var5;
      String var6;
      while(var4.hasNext()) {
         var5 = (String)var4.next();
         if (var3) {
            var6 = this.resolveSnapshot(var5, var1);
            if (var6 != null) {
               var2.add(var5 + var6);
            }
         } else {
            var2.add(var5 + var1.getPath());
         }
      }

      var4 = this.getRepositories().iterator();

      while(var4.hasNext()) {
         var5 = (String)var4.next();
         if (var3) {
            var6 = this.resolveSnapshot(var5, var1);
            if (var6 != null) {
               var2.add(var5 + var6);
            }
         } else {
            var2.add(var5 + var1.getPath());
         }
      }

      return Collections.unmodifiableSet(var2);
   }

   private String resolveSnapshot(String repository, Library library) {
      String var3 = (String)Objects.requireNonNull(var1, "repository") + ((Library)Objects.requireNonNull(var2, "library")).getPartialPath() + "maven-metadata.xml";

      try {
         URLConnection var4 = (new URL((String)Objects.requireNonNull(var3, "url"))).openConnection();
         var4.setConnectTimeout(5000);
         var4.setReadTimeout(5000);
         var4.setRequestProperty("User-Agent", "libby/1.3.1");
         InputStream var5 = var4.getInputStream();

         String var6;
         try {
            var6 = this.getURLFromMetadata(var5, var2);
         } catch (Throwable var9) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (var5 != null) {
            var5.close();
         }

         return var6;
      } catch (MalformedURLException var10) {
         throw new IllegalArgumentException(var10);
      } catch (IOException var11) {
         if (var11 instanceof FileNotFoundException) {
            this.logger.debug("File not found: " + var3);
         } else if (var11 instanceof SocketTimeoutException) {
            this.logger.debug("Connect timed out: " + var3);
         } else if (var11 instanceof UnknownHostException) {
            this.logger.debug("Unknown host: " + var3);
         } else {
            this.logger.debug("Unexpected IOException", var11);
         }

         return null;
      }
   }

   private String getURLFromMetadata(InputStream inputStream, Library library) {
      Objects.requireNonNull(var1, "inputStream");
      Objects.requireNonNull(var2, "library");

      String var3;
      String var4;
      try {
         label77: {
            DocumentBuilderFactory var5 = DocumentBuilderFactory.newInstance();
            DocumentBuilder var6 = var5.newDocumentBuilder();
            Document var7 = var6.parse(var1);
            var7.getDocumentElement().normalize();
            NodeList var8 = var7.getElementsByTagName("snapshot");
            if (var8.getLength() == 0) {
               return null;
            }

            Node var9 = var8.item(0);
            if (var9.getNodeType() != 1) {
               return null;
            }

            Node var10 = ((Element)var9).getElementsByTagName("timestamp").item(0);
            if (var10 != null && var10.getNodeType() == 1) {
               Node var11 = ((Element)var9).getElementsByTagName("buildNumber").item(0);
               if (var11 != null && var11.getNodeType() == 1) {
                  Node var12 = var10.getFirstChild();
                  if (var12 == null || var12.getNodeType() != 3) {
                     return null;
                  }

                  Node var13 = var11.getFirstChild();
                  if (var13 == null || var13.getNodeType() != 3) {
                     return null;
                  }

                  var3 = var12.getNodeValue();
                  var4 = var13.getNodeValue();
                  break label77;
               }

               return null;
            }

            return null;
         }
      } catch (SAXException | ParserConfigurationException var14) {
         this.logger.debug("Invalid maven-metadata.xml", var14);
         return null;
      }

      String var15 = var2.getVersion();
      if (var15.endsWith("-SNAPSHOT")) {
         var15 = var15.substring(0, var15.length() - "-SNAPSHOT".length());
      }

      String var16 = var2.getPartialPath() + var2.getArtifactId() + '-' + var15 + '-' + var3 + '-' + var4;
      if (var2.hasClassifier()) {
         var16 = var16 + '-' + var2.getClassifier();
      }

      return var16 + ".jar";
   }

   private byte[] downloadLibrary(String url) {
      try {
         URLConnection var2 = (new URL((String)Objects.requireNonNull(var1, "url"))).openConnection();
         var2.setConnectTimeout(5000);
         var2.setReadTimeout(5000);
         var2.setRequestProperty("User-Agent", "libby/1.3.1");
         InputStream var3 = var2.getInputStream();

         byte[] var7;
         label72: {
            Object var8;
            try {
               label73: {
                  byte[] var5 = new byte[8192];
                  ByteArrayOutputStream var6 = new ByteArrayOutputStream();

                  int var4;
                  try {
                     while((var4 = var3.read(var5)) != -1) {
                        var6.write(var5, 0, var4);
                     }
                  } catch (SocketTimeoutException var10) {
                     this.logger.warn("Download timed out: " + var2.getURL());
                     var8 = null;
                     break label73;
                  }

                  this.logger.info("Downloaded library " + var2.getURL());
                  var7 = var6.toByteArray();
                  break label72;
               }
            } catch (Throwable var11) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var9) {
                     var11.addSuppressed(var9);
                  }
               }

               throw var11;
            }

            if (var3 != null) {
               var3.close();
            }

            return (byte[])var8;
         }

         if (var3 != null) {
            var3.close();
         }

         return var7;
      } catch (MalformedURLException var12) {
         throw new IllegalArgumentException(var12);
      } catch (IOException var13) {
         if (var13 instanceof FileNotFoundException) {
            this.logger.debug("File not found: " + var1);
         } else if (var13 instanceof SocketTimeoutException) {
            this.logger.debug("Connect timed out: " + var1);
         } else if (var13 instanceof UnknownHostException) {
            this.logger.debug("Unknown host: " + var1);
         } else {
            this.logger.debug("Unexpected IOException", var13);
         }

         return null;
      }
   }

   public Path downloadLibrary(Library library) {
      Path var2 = this.saveDirectory.resolve(((Library)Objects.requireNonNull(var1, "library")).getPath());
      if (Files.exists(var2, new LinkOption[0])) {
         if (!var1.isSnapshot()) {
            return var2;
         }

         try {
            Files.delete(var2);
         } catch (IOException var22) {
            throw new UncheckedIOException(var22);
         }
      }

      Collection var3 = this.resolveLibrary(var1);
      if (var3.isEmpty()) {
         throw new RuntimeException("Library '" + var1 + "' couldn't be resolved, add a repository");
      } else {
         MessageDigest var4 = null;
         if (var1.hasChecksum()) {
            try {
               var4 = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException var21) {
               throw new RuntimeException(var21);
            }
         }

         Path var5 = var2.resolveSibling(var2.getFileName() + ".tmp");
         var5.toFile().deleteOnExit();

         try {
            Files.createDirectories(var2.getParent());
            Iterator var6 = var3.iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               byte[] var8 = this.downloadLibrary(var7);
               if (var8 != null) {
                  if (var4 != null) {
                     byte[] var9 = var4.digest(var8);
                     if (!Arrays.equals(var9, var1.getChecksum())) {
                        this.logger.warn("*** INVALID CHECKSUM ***");
                        this.logger.warn(" Library :  " + var1);
                        this.logger.warn(" URL :  " + var7);
                        this.logger.warn(" Expected :  " + Base64.getEncoder().encodeToString(var1.getChecksum()));
                        this.logger.warn(" Actual :  " + Base64.getEncoder().encodeToString(var9));
                        continue;
                     }
                  }

                  Files.write(var5, var8, new OpenOption[0]);
                  Files.move(var5, var2);
                  Path var25 = var2;
                  return var25;
               }
            }
         } catch (IOException var23) {
            throw new UncheckedIOException(var23);
         } finally {
            try {
               Files.deleteIfExists(var5);
            } catch (IOException var20) {
            }

         }

         throw new RuntimeException("Failed to download library '" + var1 + "'");
      }
   }

   private Path relocate(Path in, String out, Collection<Relocation> relocations) {
      Objects.requireNonNull(var1, "in");
      Objects.requireNonNull(var2, "out");
      Objects.requireNonNull(var3, "relocations");
      Path var4 = this.saveDirectory.resolve(var2);
      if (Files.exists(var4, new LinkOption[0])) {
         return var4;
      } else {
         Path var5 = var4.resolveSibling(var4.getFileName() + ".tmp");
         var5.toFile().deleteOnExit();
         synchronized(this) {
            if (this.relocator == null) {
               this.relocator = new RelocationHelper(this);
            }
         }

         Path var6;
         try {
            this.relocator.relocate(var1, var5, var3);
            Files.move(var5, var4);
            this.logger.info("Relocations applied to " + this.saveDirectory.getParent().relativize(var1));
            var6 = var4;
         } catch (IOException var16) {
            throw new UncheckedIOException(var16);
         } finally {
            try {
               Files.deleteIfExists(var5);
            } catch (IOException var15) {
            }

         }

         return var6;
      }
   }

   public void loadLibrary(Library library) {
      Path var2 = this.downloadLibrary((Library)Objects.requireNonNull(var1, "library"));
      if (var1.hasRelocations()) {
         var2 = this.relocate(var2, var1.getRelocatedPath(), var1.getRelocations());
      }

      if (var1.isIsolatedLoad()) {
         this.addToIsolatedClasspath(var1, var2);
      } else {
         this.addToClasspath(var2);
      }

   }
}
