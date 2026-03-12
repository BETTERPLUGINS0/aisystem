package fr.xephi.authme.libs.com.alessiodp.libby;

import fr.xephi.authme.libs.com.alessiodp.libby.classloader.IsolatedClassLoader;
import fr.xephi.authme.libs.com.alessiodp.libby.configuration.Configuration;
import fr.xephi.authme.libs.com.alessiodp.libby.configuration.ConfigurationFetcher;
import fr.xephi.authme.libs.com.alessiodp.libby.logging.LogLevel;
import fr.xephi.authme.libs.com.alessiodp.libby.logging.Logger;
import fr.xephi.authme.libs.com.alessiodp.libby.logging.adapters.LogAdapter;
import fr.xephi.authme.libs.com.alessiodp.libby.relocation.Relocation;
import fr.xephi.authme.libs.com.alessiodp.libby.relocation.RelocationHelper;
import fr.xephi.authme.libs.com.alessiodp.libby.transitive.TransitiveDependencyHelper;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class LibraryManager {
   protected final Logger logger;
   protected final Path saveDirectory;
   protected final Set<String> repositories = new LinkedHashSet();
   protected RelocationHelper relocator;
   protected TransitiveDependencyHelper transitiveDependencyHelper;
   protected ConfigurationFetcher configurationFetcher;
   protected final IsolatedClassLoader globalIsolatedClassLoader = new IsolatedClassLoader(new URL[0]);
   protected final Map<String, IsolatedClassLoader> isolatedLibraries = new HashMap();
   protected RepositoryResolutionMode repositoryResolutionMode;

   /** @deprecated */
   @Deprecated
   protected LibraryManager(@NotNull LogAdapter logAdapter, @NotNull Path dataDirectory) {
      this.repositoryResolutionMode = RepositoryResolutionMode.DEFAULT;
      this.logger = new Logger((LogAdapter)Objects.requireNonNull(logAdapter, "logAdapter"));
      this.saveDirectory = ((Path)Objects.requireNonNull(dataDirectory, "dataDirectory")).toAbsolutePath().resolve("lib");
   }

   protected LibraryManager(@NotNull LogAdapter logAdapter, @NotNull Path dataDirectory, @NotNull String directoryName) {
      this.repositoryResolutionMode = RepositoryResolutionMode.DEFAULT;
      this.logger = new Logger((LogAdapter)Objects.requireNonNull(logAdapter, "logAdapter"));
      this.saveDirectory = ((Path)Objects.requireNonNull(dataDirectory, "dataDirectory")).toAbsolutePath().resolve((String)Objects.requireNonNull(directoryName, "directoryName"));
   }

   protected abstract void addToClasspath(@NotNull Path var1);

   protected void addToIsolatedClasspath(@NotNull Library library, @NotNull Path file) {
      String loaderId = library.getLoaderId();
      IsolatedClassLoader classLoader;
      if (loaderId != null) {
         classLoader = (IsolatedClassLoader)this.isolatedLibraries.computeIfAbsent(loaderId, (s) -> {
            return new IsolatedClassLoader(new URL[0]);
         });
      } else {
         classLoader = this.globalIsolatedClassLoader;
      }

      classLoader.addPath(file);
   }

   @NotNull
   public IsolatedClassLoader getGlobalIsolatedClassLoader() {
      return this.globalIsolatedClassLoader;
   }

   @Nullable
   public IsolatedClassLoader getIsolatedClassLoaderById(@NotNull String loaderId) {
      return (IsolatedClassLoader)this.isolatedLibraries.get(loaderId);
   }

   @NotNull
   public LogLevel getLogLevel() {
      return this.logger.getLevel();
   }

   public void setLogLevel(@NotNull LogLevel level) {
      this.logger.setLevel(level);
   }

   @NotNull
   public Logger getLogger() {
      return this.logger;
   }

   @NotNull
   public Collection<String> getRepositories() {
      LinkedList urls;
      synchronized(this.repositories) {
         urls = new LinkedList(this.repositories);
      }

      return Collections.unmodifiableList(urls);
   }

   public void addRepository(@NotNull String url) {
      String repo = ((String)Objects.requireNonNull(url, "url")).endsWith("/") ? url : url + '/';
      synchronized(this.repositories) {
         this.repositories.add(repo);
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

   public RepositoryResolutionMode getRepositoryResolutionMode() {
      return this.repositoryResolutionMode;
   }

   public void setRepositoryResolutionMode(RepositoryResolutionMode repositoryResolutionMode) {
      this.repositoryResolutionMode = repositoryResolutionMode;
   }

   @NotNull
   public Collection<String> resolveLibrary(@NotNull Library library) {
      Set<String> urls = new LinkedHashSet(((Library)Objects.requireNonNull(library, "library")).getUrls());
      boolean snapshot = library.isSnapshot();
      Collection<String> repos = this.resolveRepositories(library);
      Iterator var5 = repos.iterator();

      while(var5.hasNext()) {
         String repository = (String)var5.next();
         if (snapshot) {
            String url = this.resolveSnapshot(repository, library);
            if (url != null) {
               urls.add(repository + url);
            }
         } else {
            urls.add(repository + library.getPath());
         }
      }

      return Collections.unmodifiableSet(urls);
   }

   protected Collection<String> resolveRepositories(@NotNull Library library) {
      switch(this.getRepositoryResolutionMode()) {
      case GLOBAL_FIRST:
         return (Collection)Stream.of(this.getRepositories(), library.getRepositories(), library.getFallbackRepositories()).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
      case LIBRARY_FIRST:
         return (Collection)Stream.of(library.getRepositories(), library.getFallbackRepositories(), this.getRepositories()).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
      case DEFAULT:
      default:
         return (Collection)Stream.of(library.getRepositories(), this.getRepositories(), library.getFallbackRepositories()).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
      }
   }

   @Nullable
   protected String resolveSnapshot(@NotNull String repository, @NotNull Library library) {
      String mavenMetadata = repository.startsWith("file") ? "maven-metadata-local.xml" : "maven-metadata.xml";
      String url = (String)Objects.requireNonNull(repository, "repository") + ((Library)Objects.requireNonNull(library, "library")).getPartialPath() + mavenMetadata;

      try {
         URLConnection connection = (new URL((String)Objects.requireNonNull(url, "url"))).openConnection();
         connection.setConnectTimeout(5000);
         connection.setReadTimeout(5000);
         connection.setRequestProperty("User-Agent", "libby/2.0.0-SNAPSHOT");
         InputStream in = connection.getInputStream();

         String var7;
         try {
            var7 = this.getURLFromMetadata(in, library);
         } catch (Throwable var10) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (in != null) {
            in.close();
         }

         return var7;
      } catch (MalformedURLException var11) {
         throw new IllegalArgumentException(var11);
      } catch (IOException var12) {
         if (var12 instanceof FileNotFoundException) {
            this.logger.debug("File not found: " + url);
         } else if (var12 instanceof SocketTimeoutException) {
            this.logger.debug("Connect timed out: " + url);
         } else if (var12 instanceof UnknownHostException) {
            this.logger.debug("Unknown host: " + url);
         } else {
            this.logger.debug("Unexpected IOException", var12);
         }

         return null;
      }
   }

   @Nullable
   protected String getURLFromMetadata(@NotNull InputStream inputStream, @NotNull Library library) throws IOException {
      Objects.requireNonNull(inputStream, "inputStream");
      Objects.requireNonNull(library, "library");
      String version = library.getVersion();

      try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputStream);
         doc.getDocumentElement().normalize();
         NodeList nodes = doc.getElementsByTagName("snapshot");
         if (nodes.getLength() == 0) {
            return null;
         }

         Node snapshot = nodes.item(0);
         if (snapshot.getNodeType() != 1) {
            return null;
         }

         Node localCopyNode = ((Element)snapshot).getElementsByTagName("localCopy").item(0);
         if (localCopyNode == null || localCopyNode.getNodeType() != 1) {
            Node timestampNode = ((Element)snapshot).getElementsByTagName("timestamp").item(0);
            if (timestampNode != null && timestampNode.getNodeType() == 1) {
               Node buildNumberNode = ((Element)snapshot).getElementsByTagName("buildNumber").item(0);
               if (buildNumberNode != null && buildNumberNode.getNodeType() == 1) {
                  Node timestampChild = timestampNode.getFirstChild();
                  if (timestampChild != null && timestampChild.getNodeType() == 3) {
                     Node buildNumberChild = buildNumberNode.getFirstChild();
                     if (buildNumberChild != null && buildNumberChild.getNodeType() == 3) {
                        String timestamp = timestampChild.getNodeValue();
                        String buildNumber = buildNumberChild.getNodeValue();
                        version = library.getVersion();
                        if (version.endsWith("-SNAPSHOT")) {
                           version = version.substring(0, version.length() - "-SNAPSHOT".length());
                        }

                        version = version + '-' + timestamp + '-' + buildNumber;
                        return Util.craftPath(library.getPartialPath(), library.getArtifactId(), version, library.getClassifier());
                     }

                     return null;
                  }

                  return null;
               }

               return null;
            }

            return null;
         }
      } catch (SAXException | ParserConfigurationException var16) {
         this.logger.debug("Invalid maven-metadata.xml", var16);
         return null;
      }

      return Util.craftPath(library.getPartialPath(), library.getArtifactId(), version, library.getClassifier());
   }

   protected byte[] downloadLibrary(@NotNull String url) {
      try {
         URLConnection connection = (new URL((String)Objects.requireNonNull(url, "url"))).openConnection();
         connection.setConnectTimeout(5000);
         connection.setReadTimeout(5000);
         connection.setRequestProperty("User-Agent", "libby/2.0.0-SNAPSHOT");
         InputStream in = connection.getInputStream();

         byte[] var7;
         label72: {
            Object var8;
            try {
               label73: {
                  byte[] buf = new byte[8192];
                  ByteArrayOutputStream out = new ByteArrayOutputStream();

                  int len;
                  try {
                     while((len = in.read(buf)) != -1) {
                        out.write(buf, 0, len);
                     }
                  } catch (SocketTimeoutException var10) {
                     this.logger.warn("Download timed out: " + connection.getURL());
                     var8 = null;
                     break label73;
                  }

                  this.logger.info("Downloaded library " + connection.getURL());
                  var7 = out.toByteArray();
                  break label72;
               }
            } catch (Throwable var11) {
               if (in != null) {
                  try {
                     in.close();
                  } catch (Throwable var9) {
                     var11.addSuppressed(var9);
                  }
               }

               throw var11;
            }

            if (in != null) {
               in.close();
            }

            return (byte[])var8;
         }

         if (in != null) {
            in.close();
         }

         return var7;
      } catch (MalformedURLException var12) {
         throw new IllegalArgumentException(var12);
      } catch (IOException var13) {
         if (var13 instanceof FileNotFoundException) {
            this.logger.debug("File not found: " + url);
         } else if (var13 instanceof SocketTimeoutException) {
            this.logger.debug("Connect timed out: " + url);
         } else if (var13 instanceof UnknownHostException) {
            this.logger.debug("Unknown host: " + url);
         } else {
            this.logger.debug("Unexpected IOException", var13);
         }

         return null;
      }
   }

   @NotNull
   public Path downloadLibrary(@NotNull Library library) {
      Path file = this.saveDirectory.resolve(((Library)Objects.requireNonNull(library, "library")).getPath());
      if (Files.exists(file, new LinkOption[0])) {
         if (!library.isSnapshot()) {
            if (library.hasRelocations()) {
               file = this.relocate(file, library.getRelocatedPath(), library.getRelocations());
            }

            return file;
         }

         try {
            Files.delete(file);
         } catch (IOException var22) {
            throw new UncheckedIOException(var22);
         }
      }

      Collection<String> urls = this.resolveLibrary(library);
      if (urls.isEmpty()) {
         throw new RuntimeException("Library '" + library + "' couldn't be resolved, add a repository");
      } else {
         MessageDigest md = null;
         if (library.hasChecksum()) {
            try {
               md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException var21) {
               throw new RuntimeException(var21);
            }
         }

         Path out = file.resolveSibling(file.getFileName() + ".tmp");
         out.toFile().deleteOnExit();

         Path var25;
         try {
            Files.createDirectories(file.getParent());
            Iterator var6 = urls.iterator();

            byte[] bytes;
            while(true) {
               if (!var6.hasNext()) {
                  throw new RuntimeException("Failed to download library '" + library + "'");
               }

               String url = (String)var6.next();
               bytes = this.downloadLibrary(url);
               if (bytes != null) {
                  if (md == null) {
                     break;
                  }

                  byte[] checksum = md.digest(bytes);
                  if (Arrays.equals(checksum, library.getChecksum())) {
                     break;
                  }

                  this.logger.warn("*** INVALID CHECKSUM ***");
                  this.logger.warn(" Library :  " + library);
                  this.logger.warn(" URL :  " + url);
                  this.logger.warn(" Expected :  " + Base64.getEncoder().encodeToString(library.getChecksum()));
                  this.logger.warn(" Actual :  " + Base64.getEncoder().encodeToString(checksum));
               }
            }

            Files.write(out, bytes, new OpenOption[0]);
            Files.move(out, file);
            if (library.hasRelocations()) {
               file = this.relocate(file, library.getRelocatedPath(), library.getRelocations());
            }

            var25 = file;
         } catch (IOException var23) {
            throw new UncheckedIOException(var23);
         } finally {
            try {
               Files.deleteIfExists(out);
            } catch (IOException var20) {
            }

         }

         return var25;
      }
   }

   @NotNull
   public Path relocate(@NotNull Path in, @NotNull String out, @NotNull Collection<Relocation> relocations) {
      Objects.requireNonNull(in, "in");
      Objects.requireNonNull(out, "out");
      Objects.requireNonNull(relocations, "relocations");
      Path file = this.saveDirectory.resolve(out);
      if (Files.exists(file, new LinkOption[0])) {
         return file;
      } else {
         Path tmpOut = file.resolveSibling(file.getFileName() + ".tmp");
         tmpOut.toFile().deleteOnExit();
         synchronized(this) {
            if (this.relocator == null) {
               this.relocator = new RelocationHelper(this);
            }
         }

         Path var6;
         try {
            this.relocator.relocate(in, tmpOut, relocations);
            Files.move(tmpOut, file);
            this.logger.info("Relocations applied to " + in.getFileName());
            var6 = file;
         } catch (IOException var16) {
            throw new UncheckedIOException(var16);
         } finally {
            try {
               Files.deleteIfExists(tmpOut);
            } catch (IOException var15) {
            }

         }

         return var6;
      }
   }

   protected void resolveTransitiveLibraries(@NotNull Library library) {
      Objects.requireNonNull(library, "library");
      synchronized(this) {
         if (this.transitiveDependencyHelper == null) {
            this.transitiveDependencyHelper = new TransitiveDependencyHelper(this, this.saveDirectory);
         }
      }

      Iterator var2 = this.transitiveDependencyHelper.findTransitiveLibraries(library).iterator();

      while(var2.hasNext()) {
         Library transitiveLibrary = (Library)var2.next();
         this.loadLibrary(transitiveLibrary);
      }

   }

   public void loadLibrary(@NotNull Library library) {
      this.logger.info("Loading library " + library.getArtifactId());
      Path file = this.downloadLibrary((Library)Objects.requireNonNull(library, "library"));
      if (library.resolveTransitiveDependencies()) {
         this.resolveTransitiveLibraries(library);
      }

      if (library.isIsolatedLoad()) {
         this.addToIsolatedClasspath(library, file);
      } else {
         this.addToClasspath(file);
      }

   }

   public void loadLibraries(@NotNull Library... libraries) {
      Library[] var2 = libraries;
      int var3 = libraries.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Library library = var2[var4];
         this.loadLibrary(library);
      }

   }

   public void configureFromJSON() {
      this.configureFromJSON("libby.json");
   }

   public void configureFromJSON(@NotNull String fileName) {
      this.configureFromJSON((InputStream)Objects.requireNonNull(this.getResourceAsStream(fileName), "resourceAsStream"));
   }

   public void configureFromJSON(@NotNull InputStream data) {
      synchronized(this) {
         if (this.configurationFetcher == null) {
            this.configurationFetcher = new ConfigurationFetcher(this);
         }
      }

      Configuration config = this.configurationFetcher.readJsonFile(data);
      Iterator var3 = config.getRepositories().iterator();

      while(var3.hasNext()) {
         String repo = (String)var3.next();
         this.addRepository(repo);
      }

      var3 = config.getLibraries().iterator();

      while(var3.hasNext()) {
         Library library = (Library)var3.next();
         this.loadLibrary(library);
      }

   }

   @Nullable
   protected InputStream getResourceAsStream(@NotNull String path) {
      return this.getClass().getClassLoader().getResourceAsStream(path);
   }
}
