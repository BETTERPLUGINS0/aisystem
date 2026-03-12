package fr.xephi.authme.libs.com.google.common.reflect;

import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.CharMatcher;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Splitter;
import fr.xephi.authme.libs.com.google.common.base.StandardSystemProperty;
import fr.xephi.authme.libs.com.google.common.collect.FluentIterable;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableSet;
import fr.xephi.authme.libs.com.google.common.collect.Maps;
import fr.xephi.authme.libs.com.google.common.collect.UnmodifiableIterator;
import fr.xephi.authme.libs.com.google.common.io.ByteSource;
import fr.xephi.authme.libs.com.google.common.io.CharSource;
import fr.xephi.authme.libs.com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
public final class ClassPath {
   private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
   private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
   private static final String CLASS_FILE_NAME_EXTENSION = ".class";
   private final ImmutableSet<ClassPath.ResourceInfo> resources;

   private ClassPath(ImmutableSet<ClassPath.ResourceInfo> resources) {
      this.resources = resources;
   }

   public static ClassPath from(ClassLoader classloader) throws IOException {
      ImmutableSet<ClassPath.LocationInfo> locations = locationsFrom(classloader);
      Set<File> scanned = new HashSet();
      UnmodifiableIterator var3 = locations.iterator();

      while(var3.hasNext()) {
         ClassPath.LocationInfo location = (ClassPath.LocationInfo)var3.next();
         scanned.add(location.file());
      }

      ImmutableSet.Builder<ClassPath.ResourceInfo> builder = ImmutableSet.builder();
      UnmodifiableIterator var7 = locations.iterator();

      while(var7.hasNext()) {
         ClassPath.LocationInfo location = (ClassPath.LocationInfo)var7.next();
         builder.addAll((Iterable)location.scanResources(scanned));
      }

      return new ClassPath(builder.build());
   }

   public ImmutableSet<ClassPath.ResourceInfo> getResources() {
      return this.resources;
   }

   public ImmutableSet<ClassPath.ClassInfo> getAllClasses() {
      return FluentIterable.from((Iterable)this.resources).filter(ClassPath.ClassInfo.class).toSet();
   }

   public ImmutableSet<ClassPath.ClassInfo> getTopLevelClasses() {
      return FluentIterable.from((Iterable)this.resources).filter(ClassPath.ClassInfo.class).filter(ClassPath.ClassInfo::isTopLevel).toSet();
   }

   public ImmutableSet<ClassPath.ClassInfo> getTopLevelClasses(String packageName) {
      Preconditions.checkNotNull(packageName);
      ImmutableSet.Builder<ClassPath.ClassInfo> builder = ImmutableSet.builder();
      UnmodifiableIterator var3 = this.getTopLevelClasses().iterator();

      while(var3.hasNext()) {
         ClassPath.ClassInfo classInfo = (ClassPath.ClassInfo)var3.next();
         if (classInfo.getPackageName().equals(packageName)) {
            builder.add((Object)classInfo);
         }
      }

      return builder.build();
   }

   public ImmutableSet<ClassPath.ClassInfo> getTopLevelClassesRecursive(String packageName) {
      Preconditions.checkNotNull(packageName);
      String packagePrefix = (new StringBuilder(1 + String.valueOf(packageName).length())).append(packageName).append('.').toString();
      ImmutableSet.Builder<ClassPath.ClassInfo> builder = ImmutableSet.builder();
      UnmodifiableIterator var4 = this.getTopLevelClasses().iterator();

      while(var4.hasNext()) {
         ClassPath.ClassInfo classInfo = (ClassPath.ClassInfo)var4.next();
         if (classInfo.getName().startsWith(packagePrefix)) {
            builder.add((Object)classInfo);
         }
      }

      return builder.build();
   }

   static ImmutableSet<ClassPath.LocationInfo> locationsFrom(ClassLoader classloader) {
      ImmutableSet.Builder<ClassPath.LocationInfo> builder = ImmutableSet.builder();
      UnmodifiableIterator var2 = getClassPathEntries(classloader).entrySet().iterator();

      while(var2.hasNext()) {
         Entry<File, ClassLoader> entry = (Entry)var2.next();
         builder.add((Object)(new ClassPath.LocationInfo((File)entry.getKey(), (ClassLoader)entry.getValue())));
      }

      return builder.build();
   }

   @VisibleForTesting
   static ImmutableSet<File> getClassPathFromManifest(File jarFile, @CheckForNull Manifest manifest) {
      if (manifest == null) {
         return ImmutableSet.of();
      } else {
         ImmutableSet.Builder<File> builder = ImmutableSet.builder();
         String classpathAttribute = manifest.getMainAttributes().getValue(Name.CLASS_PATH.toString());
         if (classpathAttribute != null) {
            Iterator var4 = CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute).iterator();

            while(true) {
               URL url;
               while(true) {
                  if (!var4.hasNext()) {
                     return builder.build();
                  }

                  String path = (String)var4.next();

                  try {
                     url = getClassPathEntry(jarFile, path);
                     break;
                  } catch (MalformedURLException var8) {
                     Logger var10000 = logger;
                     String var10002 = String.valueOf(path);
                     String var10001;
                     if (var10002.length() != 0) {
                        var10001 = "Invalid Class-Path entry: ".concat(var10002);
                     } else {
                        String var10003 = new String;
                        var10001 = var10003;
                        var10003.<init>("Invalid Class-Path entry: ");
                     }

                     var10000.warning(var10001);
                  }
               }

               if (url.getProtocol().equals("file")) {
                  builder.add((Object)toFile(url));
               }
            }
         } else {
            return builder.build();
         }
      }
   }

   @VisibleForTesting
   static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classloader) {
      LinkedHashMap<File, ClassLoader> entries = Maps.newLinkedHashMap();
      ClassLoader parent = classloader.getParent();
      if (parent != null) {
         entries.putAll(getClassPathEntries(parent));
      }

      UnmodifiableIterator var3 = getClassLoaderUrls(classloader).iterator();

      while(var3.hasNext()) {
         URL url = (URL)var3.next();
         if (url.getProtocol().equals("file")) {
            File file = toFile(url);
            if (!entries.containsKey(file)) {
               entries.put(file, classloader);
            }
         }
      }

      return ImmutableMap.copyOf((Map)entries);
   }

   private static ImmutableList<URL> getClassLoaderUrls(ClassLoader classloader) {
      if (classloader instanceof URLClassLoader) {
         return ImmutableList.copyOf((Object[])((URLClassLoader)classloader).getURLs());
      } else {
         return classloader.equals(ClassLoader.getSystemClassLoader()) ? parseJavaClassPath() : ImmutableList.of();
      }
   }

   @VisibleForTesting
   static ImmutableList<URL> parseJavaClassPath() {
      ImmutableList.Builder<URL> urls = ImmutableList.builder();
      Iterator var1 = Splitter.on(StandardSystemProperty.PATH_SEPARATOR.value()).split(StandardSystemProperty.JAVA_CLASS_PATH.value()).iterator();

      while(var1.hasNext()) {
         String entry = (String)var1.next();

         try {
            try {
               urls.add((Object)(new File(entry)).toURI().toURL());
            } catch (SecurityException var4) {
               urls.add((Object)(new URL("file", (String)null, (new File(entry)).getAbsolutePath())));
            }
         } catch (MalformedURLException var5) {
            Logger var10000 = logger;
            Level var10001 = Level.WARNING;
            String var10003 = String.valueOf(entry);
            String var10002;
            if (var10003.length() != 0) {
               var10002 = "malformed classpath entry: ".concat(var10003);
            } else {
               String var10004 = new String;
               var10002 = var10004;
               var10004.<init>("malformed classpath entry: ");
            }

            var10000.log(var10001, var10002, var5);
         }
      }

      return urls.build();
   }

   @VisibleForTesting
   static URL getClassPathEntry(File jarFile, String path) throws MalformedURLException {
      return new URL(jarFile.toURI().toURL(), path);
   }

   @VisibleForTesting
   static String getClassName(String filename) {
      int classNameEnd = filename.length() - ".class".length();
      return filename.substring(0, classNameEnd).replace('/', '.');
   }

   @VisibleForTesting
   static File toFile(URL url) {
      Preconditions.checkArgument(url.getProtocol().equals("file"));

      try {
         return new File(url.toURI());
      } catch (URISyntaxException var2) {
         return new File(url.getPath());
      }
   }

   static final class LocationInfo {
      final File home;
      private final ClassLoader classloader;

      LocationInfo(File home, ClassLoader classloader) {
         this.home = (File)Preconditions.checkNotNull(home);
         this.classloader = (ClassLoader)Preconditions.checkNotNull(classloader);
      }

      public final File file() {
         return this.home;
      }

      public ImmutableSet<ClassPath.ResourceInfo> scanResources() throws IOException {
         return this.scanResources(new HashSet());
      }

      public ImmutableSet<ClassPath.ResourceInfo> scanResources(Set<File> scannedFiles) throws IOException {
         ImmutableSet.Builder<ClassPath.ResourceInfo> builder = ImmutableSet.builder();
         scannedFiles.add(this.home);
         this.scan(this.home, scannedFiles, builder);
         return builder.build();
      }

      private void scan(File file, Set<File> scannedUris, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
         try {
            if (!file.exists()) {
               return;
            }
         } catch (SecurityException var7) {
            Logger var10000 = ClassPath.logger;
            String var5 = String.valueOf(file);
            String var6 = String.valueOf(var7);
            var10000.warning((new StringBuilder(16 + String.valueOf(var5).length() + String.valueOf(var6).length())).append("Cannot access ").append(var5).append(": ").append(var6).toString());
            return;
         }

         if (file.isDirectory()) {
            this.scanDirectory(file, builder);
         } else {
            this.scanJar(file, scannedUris, builder);
         }

      }

      private void scanJar(File file, Set<File> scannedUris, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
         JarFile jarFile;
         try {
            jarFile = new JarFile(file);
         } catch (IOException var14) {
            return;
         }

         try {
            UnmodifiableIterator var5 = ClassPath.getClassPathFromManifest(file, jarFile.getManifest()).iterator();

            while(var5.hasNext()) {
               File path = (File)var5.next();
               if (scannedUris.add(path.getCanonicalFile())) {
                  this.scan(path, scannedUris, builder);
               }
            }

            this.scanJarFile(jarFile, builder);
         } finally {
            try {
               jarFile.close();
            } catch (IOException var13) {
            }

         }

      }

      private void scanJarFile(JarFile file, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) {
         Enumeration entries = file.entries();

         while(entries.hasMoreElements()) {
            JarEntry entry = (JarEntry)entries.nextElement();
            if (!entry.isDirectory() && !entry.getName().equals("META-INF/MANIFEST.MF")) {
               builder.add((Object)ClassPath.ResourceInfo.of(new File(file.getName()), entry.getName(), this.classloader));
            }
         }

      }

      private void scanDirectory(File directory, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
         Set<File> currentPath = new HashSet();
         currentPath.add(directory.getCanonicalFile());
         this.scanDirectory(directory, "", currentPath, builder);
      }

      private void scanDirectory(File directory, String packagePrefix, Set<File> currentPath, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
         File[] files = directory.listFiles();
         if (files == null) {
            Logger var14 = ClassPath.logger;
            String var12 = String.valueOf(directory);
            var14.warning((new StringBuilder(22 + String.valueOf(var12).length())).append("Cannot read directory ").append(var12).toString());
         } else {
            File[] var6 = files;
            int var7 = files.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               File f = var6[var8];
               String name = f.getName();
               if (f.isDirectory()) {
                  File deref = f.getCanonicalFile();
                  if (currentPath.add(deref)) {
                     this.scanDirectory(deref, (new StringBuilder(1 + String.valueOf(packagePrefix).length() + String.valueOf(name).length())).append(packagePrefix).append(name).append("/").toString(), currentPath, builder);
                     currentPath.remove(deref);
                  }
               } else {
                  String var10000 = String.valueOf(packagePrefix);
                  String var10001 = String.valueOf(name);
                  if (var10001.length() != 0) {
                     var10000 = var10000.concat(var10001);
                  } else {
                     String var10002 = new String;
                     var10001 = var10000;
                     var10000 = var10002;
                     var10002.<init>(var10001);
                  }

                  String resourceName = var10000;
                  if (!resourceName.equals("META-INF/MANIFEST.MF")) {
                     builder.add((Object)ClassPath.ResourceInfo.of(f, resourceName, this.classloader));
                  }
               }
            }

         }
      }

      public boolean equals(@CheckForNull Object obj) {
         if (!(obj instanceof ClassPath.LocationInfo)) {
            return false;
         } else {
            ClassPath.LocationInfo that = (ClassPath.LocationInfo)obj;
            return this.home.equals(that.home) && this.classloader.equals(that.classloader);
         }
      }

      public int hashCode() {
         return this.home.hashCode();
      }

      public String toString() {
         return this.home.toString();
      }
   }

   public static final class ClassInfo extends ClassPath.ResourceInfo {
      private final String className;

      ClassInfo(File file, String resourceName, ClassLoader loader) {
         super(file, resourceName, loader);
         this.className = ClassPath.getClassName(resourceName);
      }

      public String getPackageName() {
         return Reflection.getPackageName(this.className);
      }

      public String getSimpleName() {
         int lastDollarSign = this.className.lastIndexOf(36);
         String packageName;
         if (lastDollarSign != -1) {
            packageName = this.className.substring(lastDollarSign + 1);
            return CharMatcher.inRange('0', '9').trimLeadingFrom(packageName);
         } else {
            packageName = this.getPackageName();
            return packageName.isEmpty() ? this.className : this.className.substring(packageName.length() + 1);
         }
      }

      public String getName() {
         return this.className;
      }

      public boolean isTopLevel() {
         return this.className.indexOf(36) == -1;
      }

      public Class<?> load() {
         try {
            return this.loader.loadClass(this.className);
         } catch (ClassNotFoundException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public String toString() {
         return this.className;
      }
   }

   public static class ResourceInfo {
      private final File file;
      private final String resourceName;
      final ClassLoader loader;

      static ClassPath.ResourceInfo of(File file, String resourceName, ClassLoader loader) {
         return (ClassPath.ResourceInfo)(resourceName.endsWith(".class") ? new ClassPath.ClassInfo(file, resourceName, loader) : new ClassPath.ResourceInfo(file, resourceName, loader));
      }

      ResourceInfo(File file, String resourceName, ClassLoader loader) {
         this.file = (File)Preconditions.checkNotNull(file);
         this.resourceName = (String)Preconditions.checkNotNull(resourceName);
         this.loader = (ClassLoader)Preconditions.checkNotNull(loader);
      }

      public final URL url() {
         URL url = this.loader.getResource(this.resourceName);
         if (url == null) {
            throw new NoSuchElementException(this.resourceName);
         } else {
            return url;
         }
      }

      public final ByteSource asByteSource() {
         return Resources.asByteSource(this.url());
      }

      public final CharSource asCharSource(Charset charset) {
         return Resources.asCharSource(this.url(), charset);
      }

      public final String getResourceName() {
         return this.resourceName;
      }

      final File getFile() {
         return this.file;
      }

      public int hashCode() {
         return this.resourceName.hashCode();
      }

      public boolean equals(@CheckForNull Object obj) {
         if (!(obj instanceof ClassPath.ResourceInfo)) {
            return false;
         } else {
            ClassPath.ResourceInfo that = (ClassPath.ResourceInfo)obj;
            return this.resourceName.equals(that.resourceName) && this.loader == that.loader;
         }
      }

      public String toString() {
         return this.resourceName;
      }
   }
}
