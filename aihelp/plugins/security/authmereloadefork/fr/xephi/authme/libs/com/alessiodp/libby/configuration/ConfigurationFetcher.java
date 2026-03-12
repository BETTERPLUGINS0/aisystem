package fr.xephi.authme.libs.com.alessiodp.libby.configuration;

import fr.xephi.authme.libs.com.alessiodp.libby.Library;
import fr.xephi.authme.libs.com.alessiodp.libby.LibraryManager;
import fr.xephi.authme.libs.com.alessiodp.libby.Util;
import fr.xephi.authme.libs.com.alessiodp.libby.classloader.IsolatedClassLoader;
import fr.xephi.authme.libs.com.alessiodp.libby.relocation.Relocation;
import fr.xephi.authme.libs.com.alessiodp.libby.transitive.ExcludedDependency;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigurationFetcher {
   public static final int CONFIGURATION_VERSION = 0;
   private static final String JSON_PARSER_CLASS = Util.replaceWithDots("com{}grack{}nanojson{}JsonParser");
   private static final String JSON_PARSER_CONTEXT_CLASS = Util.replaceWithDots("com{}grack{}nanojson{}JsonParser$JsonParserContext");
   private static final String JSON_PARSER_EXCEPTION_CLASS = Util.replaceWithDots("com{}grack{}nanojson{}JsonParserException");
   private static final String JSON_OBJECT_CLASS = Util.replaceWithDots("com{}grack{}nanojson{}JsonObject");
   private static final String JSON_ARRAY_CLASS = Util.replaceWithDots("com{}grack{}nanojson{}JsonArray");
   private final Method jsonParserObject;
   private final Method jsonParserFrom;
   private final Method jsonObjectGetArray;
   private final Method jsonObjectGetBoolean;
   private final Method jsonObjectGetString;
   private final Method jsonArrayGetObject;
   private final Class<?> jsonParserException;

   public ConfigurationFetcher(@NotNull LibraryManager libraryManager) {
      Objects.requireNonNull(libraryManager, "libraryManager");
      IsolatedClassLoader classLoader = new IsolatedClassLoader(new URL[0]);
      classLoader.addPath(libraryManager.downloadLibrary(Library.builder().groupId("com{}grack").artifactId("nanojson").version("1.8").checksumFromBase64("qyhAVZM8LYvqhGQrbmW2aHV4hRzn+2flPCV98wAimJo=").repository("https://repo1.maven.org/maven2/").build()));

      try {
         Class<?> jsonParser = classLoader.loadClass(JSON_PARSER_CLASS);
         Class<?> jsonParserContext = classLoader.loadClass(JSON_PARSER_CONTEXT_CLASS);
         Class<?> jsonObject = classLoader.loadClass(JSON_OBJECT_CLASS);
         Class<?> jsonArray = classLoader.loadClass(JSON_ARRAY_CLASS);
         this.jsonParserException = classLoader.loadClass(JSON_PARSER_EXCEPTION_CLASS);
         this.jsonParserObject = jsonParser.getMethod("object");
         this.jsonParserFrom = jsonParserContext.getMethod("from", InputStream.class);
         this.jsonObjectGetArray = jsonObject.getMethod("getArray", String.class);
         this.jsonObjectGetBoolean = jsonObject.getMethod("getBoolean", String.class);
         this.jsonObjectGetString = jsonObject.getMethod("getString", String.class);
         this.jsonArrayGetObject = jsonArray.getMethod("getObject", Integer.TYPE);
      } catch (ReflectiveOperationException var7) {
         throw new RuntimeException(var7);
      }
   }

   public Configuration readJsonFile(@NotNull InputStream data) {
      try {
         Map root;
         try {
            root = (Map)this.jsonParserFrom.invoke(this.jsonParserObject.invoke((Object)null), data);
         } catch (InvocationTargetException var7) {
            if (this.jsonParserException.isInstance(var7.getCause())) {
               throw new MalformedConfigurationException(var7.getCause().getMessage(), var7.getCause());
            }

            throw new RuntimeException(var7);
         }

         Integer version = this.fetchVersion(root);
         Set<String> repositories = this.fetchRepositories(root);
         Set<Relocation> globalRelocations = this.fetchRelocations(root);
         List<Library> libraries = this.fetchLibraries(root, globalRelocations);
         return new Configuration(version, repositories, globalRelocations, libraries);
      } catch (ReflectiveOperationException var8) {
         throw new RuntimeException(var8);
      }
   }

   private Integer fetchVersion(@NotNull Map<String, Object> configuration) {
      Object version = configuration.get("version");
      if (version instanceof Number) {
         int ver = ((Number)version).intValue();
         if (ver != 0) {
            throw new ConfigurationException("The json file is version " + version + " but this version of libby only supports version " + 0);
         } else {
            return ver;
         }
      } else {
         return null;
      }
   }

   private Set<String> fetchRepositories(@NotNull Map<String, Object> configuration) throws ReflectiveOperationException {
      Set<String> repos = new HashSet();
      ArrayList<Object> repositories = this.getArray(configuration, "repositories");
      if (repositories != null) {
         Iterator var4 = repositories.iterator();

         while(var4.hasNext()) {
            Object repository = var4.next();
            if (!(repository instanceof String)) {
               throw new ConfigurationException("Invalid repository: " + repository);
            }

            repos.add((String)repository);
         }
      }

      return repos;
   }

   private Set<Relocation> fetchRelocations(@NotNull Map<String, Object> configuration) throws ReflectiveOperationException {
      ArrayList<Object> relocations = this.getArray(configuration, "relocations");
      if (relocations == null) {
         return Collections.emptySet();
      } else {
         Set<Relocation> fetchedRelocations = new HashSet();

         for(int i = 0; i < relocations.size(); ++i) {
            Map<String, Object> relocation = this.getObject(relocations, i);
            if (relocation == null) {
               throw new ConfigurationException("Invalid relocation: " + relocations.get(i));
            }

            String pattern = this.getString(relocation, "pattern");
            if (pattern == null) {
               throw new ConfigurationException("The pattern property is required for all relocations");
            }

            String relocatedPattern = this.getString(relocation, "relocatedPattern");
            if (relocatedPattern == null) {
               throw new ConfigurationException("The relocatedPattern property is required for all relocations");
            }

            ArrayList<?> includes = this.getArray(relocation, "includes");
            if (includes != null) {
               Iterator var9 = includes.iterator();

               while(var9.hasNext()) {
                  Object include = var9.next();
                  if (!(include instanceof String)) {
                     throw new ConfigurationException("Invalid relocation include: " + include);
                  }
               }
            }

            ArrayList<?> excludes = this.getArray(relocation, "excludes");
            if (excludes != null) {
               Iterator var12 = excludes.iterator();

               while(var12.hasNext()) {
                  Object exclude = var12.next();
                  if (!(exclude instanceof String)) {
                     throw new ConfigurationException("Invalid relocation exclude: " + exclude);
                  }
               }
            }

            fetchedRelocations.add(new Relocation(pattern, relocatedPattern, includes, excludes));
         }

         return Collections.unmodifiableSet(fetchedRelocations);
      }
   }

   @NotNull
   private Set<ExcludedDependency> fetchExcludedTransitiveDependencies(@NotNull Map<String, Object> library) throws ReflectiveOperationException {
      ArrayList<Object> excludedDependencies = this.getArray(library, "excludedTransitiveDependencies");
      if (excludedDependencies != null) {
         Set<ExcludedDependency> fetchedExcludedDependencies = new HashSet();

         for(int i = 0; i < excludedDependencies.size(); ++i) {
            Map<String, Object> excludedDependency = this.getObject(excludedDependencies, i);
            if (excludedDependency == null) {
               throw new ConfigurationException("Invalid excluded transitive dependency: " + excludedDependencies.get(i));
            }

            String groupId = this.getString(excludedDependency, "groupId");
            if (groupId == null) {
               throw new ConfigurationException("The groupId property is required for all excluded transitive dependencies");
            }

            String artifactId = this.getString(excludedDependency, "artifactId");
            if (artifactId == null) {
               throw new ConfigurationException("The artifactId property is required for all excluded transitive dependencies");
            }

            fetchedExcludedDependencies.add(new ExcludedDependency(groupId, artifactId));
         }

         return Collections.unmodifiableSet(fetchedExcludedDependencies);
      } else {
         return Collections.emptySet();
      }
   }

   private List<Library> fetchLibraries(@NotNull Map<String, Object> configuration, @NotNull Set<Relocation> globalRelocations) throws ReflectiveOperationException {
      ArrayList<Object> libraries = this.getArray(configuration, "libraries");
      if (libraries == null) {
         return Collections.emptyList();
      } else {
         List<Library> fetchedLibraries = new ArrayList(libraries.size());

         for(int i = 0; i < libraries.size(); ++i) {
            Map<String, Object> library = this.getObject(libraries, i);
            if (library == null) {
               throw new ConfigurationException("Invalid library: " + libraries.get(i));
            }

            Library.Builder libraryBuilder = Library.builder();
            String groupId = this.getString(library, "groupId");
            if (groupId == null) {
               throw new ConfigurationException("The groupId property is required for all libraries");
            }

            String artifactId = this.getString(library, "artifactId");
            if (artifactId == null) {
               throw new ConfigurationException("The artifactId property is required for all libraries");
            }

            String artifactVersion = this.getString(library, "version");
            if (artifactVersion == null) {
               throw new ConfigurationException("The version property is required for all libraries");
            }

            libraryBuilder.groupId(groupId).artifactId(artifactId).version(artifactVersion);
            String checksum = this.getString(library, "checksum");
            if (checksum != null) {
               libraryBuilder.checksum(checksum);
            }

            String checksumFromBase64 = this.getString(library, "checksumFromBase64");
            if (checksumFromBase64 != null) {
               try {
                  libraryBuilder.checksumFromBase64(checksumFromBase64);
               } catch (IllegalArgumentException var16) {
                  throw new ConfigurationException("The checksum property must be a valid base64 encoded SHA-256 checksum");
               }
            }

            libraryBuilder.isolatedLoad(this.getBoolean(library, "isolatedLoad"));
            libraryBuilder.loaderId(this.getString(library, "loaderId"));
            libraryBuilder.classifier(this.getString(library, "classifier"));
            libraryBuilder.resolveTransitiveDependencies(this.getBoolean(library, "resolveTransitiveDependencies"));
            Set var10000 = this.fetchExcludedTransitiveDependencies(library);
            Objects.requireNonNull(libraryBuilder);
            var10000.forEach(libraryBuilder::excludeTransitiveDependency);
            var10000 = this.fetchRepositories(library);
            Objects.requireNonNull(libraryBuilder);
            var10000.forEach(libraryBuilder::repository);
            Set<Relocation> relocations = this.fetchRelocations(library);
            Iterator var14 = relocations.iterator();

            Relocation relocation;
            while(var14.hasNext()) {
               relocation = (Relocation)var14.next();
               libraryBuilder.relocate(relocation);
            }

            var14 = globalRelocations.iterator();

            while(var14.hasNext()) {
               relocation = (Relocation)var14.next();
               libraryBuilder.relocate(relocation);
            }

            fetchedLibraries.add(libraryBuilder.build());
         }

         return Collections.unmodifiableList(fetchedLibraries);
      }
   }

   private boolean getBoolean(@NotNull Map<String, Object> jsonObject, @NotNull String key) throws ReflectiveOperationException {
      return (Boolean)this.jsonObjectGetBoolean.invoke(jsonObject, key);
   }

   @Nullable
   private String getString(@NotNull Map<String, Object> jsonObject, @NotNull String key) throws ReflectiveOperationException {
      return (String)this.jsonObjectGetString.invoke(jsonObject, key);
   }

   @Nullable
   private ArrayList<Object> getArray(@NotNull Map<String, Object> jsonObject, @NotNull String key) throws ReflectiveOperationException {
      return (ArrayList)this.jsonObjectGetArray.invoke(jsonObject, key);
   }

   @Nullable
   private Map<String, Object> getObject(@NotNull ArrayList<Object> jsonArray, int index) throws ReflectiveOperationException {
      return (Map)this.jsonArrayGetObject.invoke(jsonArray, index);
   }
}
