package fr.xephi.authme.libs.com.alessiodp.libby;

import fr.xephi.authme.libs.com.alessiodp.libby.relocation.Relocation;
import fr.xephi.authme.libs.com.alessiodp.libby.transitive.ExcludedDependency;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Library {
   @NotNull
   private final Collection<String> urls;
   @NotNull
   private final Collection<String> repositories;
   @NotNull
   private final Collection<String> fallbackRepositories;
   @NotNull
   private final String groupId;
   @NotNull
   private final String artifactId;
   @NotNull
   private final String version;
   @Nullable
   private final String classifier;
   @Nullable
   private final byte[] checksum;
   @NotNull
   private final Collection<Relocation> relocations;
   @NotNull
   private final String path;
   @NotNull
   private final String partialPath;
   @Nullable
   private final String relocatedPath;
   private final boolean isolatedLoad;
   @Nullable
   private final String loaderId;
   private final boolean resolveTransitiveDependencies;
   @NotNull
   private final Collection<ExcludedDependency> excludedTransitiveDependencies;

   private Library(@Nullable Collection<String> urls, @Nullable Collection<String> repositories, @Nullable Collection<String> fallbackRepositories, @NotNull String groupId, @NotNull String artifactId, @NotNull String version, @Nullable String classifier, @Nullable byte[] checksum, @Nullable Collection<Relocation> relocations, boolean isolatedLoad, @Nullable String loaderId, boolean resolveTransitiveDependencies, @Nullable Collection<ExcludedDependency> excludedTransitiveDependencies) {
      this.urls = urls != null ? Collections.unmodifiableList(new LinkedList(urls)) : Collections.emptyList();
      this.groupId = Util.replaceWithDots((String)Objects.requireNonNull(groupId, "groupId"));
      this.artifactId = Util.replaceWithDots((String)Objects.requireNonNull(artifactId, "artifactId"));
      this.version = (String)Objects.requireNonNull(version, "version");
      this.classifier = classifier;
      this.checksum = checksum;
      this.relocations = relocations != null ? Collections.unmodifiableList(new LinkedList(relocations)) : Collections.emptyList();
      this.partialPath = Util.craftPartialPath(this.artifactId, this.groupId, version);
      this.path = Util.craftPath(this.partialPath, this.artifactId, this.version, this.classifier);
      this.repositories = repositories != null ? Collections.unmodifiableList(new LinkedList(repositories)) : Collections.emptyList();
      this.fallbackRepositories = fallbackRepositories != null ? Collections.unmodifiableList(new LinkedList(fallbackRepositories)) : Collections.emptyList();
      this.relocatedPath = this.hasRelocations() ? this.path + "-relocated-" + Math.abs(this.relocations.hashCode()) + ".jar" : null;
      this.isolatedLoad = isolatedLoad;
      this.loaderId = loaderId;
      this.resolveTransitiveDependencies = resolveTransitiveDependencies;
      this.excludedTransitiveDependencies = excludedTransitiveDependencies != null ? Collections.unmodifiableList(new LinkedList(excludedTransitiveDependencies)) : Collections.emptyList();
   }

   @NotNull
   public Collection<String> getUrls() {
      return this.urls;
   }

   @NotNull
   public Collection<String> getRepositories() {
      return this.repositories;
   }

   @NotNull
   public Collection<String> getFallbackRepositories() {
      return this.fallbackRepositories;
   }

   @NotNull
   public String getGroupId() {
      return this.groupId;
   }

   @NotNull
   public String getArtifactId() {
      return this.artifactId;
   }

   @NotNull
   public String getVersion() {
      return this.version;
   }

   @Nullable
   public String getClassifier() {
      return this.classifier;
   }

   public boolean hasClassifier() {
      return this.classifier != null && !this.classifier.isEmpty();
   }

   @Nullable
   public byte[] getChecksum() {
      return this.checksum;
   }

   public boolean hasChecksum() {
      return this.checksum != null;
   }

   @NotNull
   public Collection<Relocation> getRelocations() {
      return this.relocations;
   }

   public boolean hasRelocations() {
      return !this.relocations.isEmpty();
   }

   @NotNull
   public String getPath() {
      return this.path;
   }

   @NotNull
   public String getPartialPath() {
      return this.partialPath;
   }

   @Nullable
   public String getRelocatedPath() {
      return this.relocatedPath;
   }

   public boolean isIsolatedLoad() {
      return this.isolatedLoad;
   }

   @Nullable
   public String getLoaderId() {
      return this.loaderId;
   }

   public boolean isSnapshot() {
      return this.version.endsWith("-SNAPSHOT");
   }

   public boolean resolveTransitiveDependencies() {
      return this.resolveTransitiveDependencies;
   }

   @NotNull
   public Collection<ExcludedDependency> getExcludedTransitiveDependencies() {
      return this.excludedTransitiveDependencies;
   }

   public String toString() {
      String name = this.groupId + ':' + this.artifactId + ':' + this.version;
      if (this.hasClassifier()) {
         name = name + ':' + this.classifier;
      }

      return name;
   }

   public static Library.Builder builder() {
      return new Library.Builder();
   }

   // $FF: synthetic method
   Library(Collection x0, Collection x1, Collection x2, String x3, String x4, String x5, String x6, byte[] x7, Collection x8, boolean x9, String x10, boolean x11, Collection x12, Object x13) {
      this(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12);
   }

   public static class Builder {
      private final Collection<String> urls = new LinkedList();
      private final Collection<String> repositories = new LinkedList();
      private final Collection<String> fallbackRepositories = new LinkedList();
      private String groupId;
      private String artifactId;
      private String version;
      private String classifier;
      private byte[] checksum;
      private boolean isolatedLoad;
      private String loaderId;
      private final Collection<Relocation> relocations = new LinkedList();
      private boolean resolveTransitiveDependencies;
      private final Collection<ExcludedDependency> excludedTransitiveDependencies = new LinkedList();

      @NotNull
      public Library.Builder url(@NotNull String url) {
         this.urls.add((String)Objects.requireNonNull(url, "url"));
         return this;
      }

      @NotNull
      public Library.Builder repository(@NotNull String url) {
         this.repositories.add(((String)Objects.requireNonNull(url, "repository")).endsWith("/") ? url : url + '/');
         return this;
      }

      @NotNull
      public Library.Builder fallbackRepository(@NotNull String url) {
         this.fallbackRepositories.add(((String)Objects.requireNonNull(url, "fallbackRepository")).endsWith("/") ? url : url + '/');
         return this;
      }

      @NotNull
      public Library.Builder groupId(@NotNull String groupId) {
         this.groupId = (String)Objects.requireNonNull(groupId, "groupId");
         return this;
      }

      @NotNull
      public Library.Builder artifactId(@NotNull String artifactId) {
         this.artifactId = (String)Objects.requireNonNull(artifactId, "artifactId");
         return this;
      }

      @NotNull
      public Library.Builder version(@NotNull String version) {
         this.version = (String)Objects.requireNonNull(version, "version");
         return this;
      }

      @NotNull
      public Library.Builder classifier(@Nullable String classifier) {
         this.classifier = classifier;
         return this;
      }

      @NotNull
      public Library.Builder checksum(@Nullable byte[] checksum) {
         this.checksum = checksum;
         return this;
      }

      @NotNull
      public Library.Builder checksum(@Nullable String checksum) {
         return checksum != null ? this.checksum(Util.hexStringToByteArray(checksum)) : this;
      }

      @NotNull
      public Library.Builder checksumFromBase64(@Nullable String checksum) {
         return checksum != null ? this.checksum(Base64.getDecoder().decode(checksum)) : this;
      }

      @NotNull
      public Library.Builder isolatedLoad(boolean isolatedLoad) {
         this.isolatedLoad = isolatedLoad;
         return this;
      }

      @NotNull
      public Library.Builder loaderId(@Nullable String loaderId) {
         this.loaderId = loaderId;
         return this;
      }

      @NotNull
      public Library.Builder relocate(@NotNull Relocation relocation) {
         Objects.requireNonNull(relocation, "relocation");
         if (!relocation.getPattern().equals(relocation.getRelocatedPattern())) {
            this.relocations.add(relocation);
         }

         return this;
      }

      @NotNull
      public Library.Builder relocate(@NotNull String pattern, @NotNull String relocatedPattern) {
         return this.relocate(new Relocation(pattern, relocatedPattern));
      }

      @NotNull
      public Library.Builder resolveTransitiveDependencies(boolean resolveTransitiveDependencies) {
         this.resolveTransitiveDependencies = resolveTransitiveDependencies;
         return this;
      }

      @NotNull
      public Library.Builder excludeTransitiveDependency(@NotNull ExcludedDependency excludedDependency) {
         this.excludedTransitiveDependencies.add((ExcludedDependency)Objects.requireNonNull(excludedDependency, "excludedDependency"));
         return this;
      }

      @NotNull
      public Library.Builder excludeTransitiveDependency(@NotNull String groupId, @NotNull String artifactId) {
         return this.excludeTransitiveDependency(new ExcludedDependency(groupId, artifactId));
      }

      @NotNull
      public Library build() {
         return new Library(this.urls, this.repositories, this.fallbackRepositories, this.groupId, this.artifactId, this.version, this.classifier, this.checksum, this.relocations, this.isolatedLoad, this.loaderId, this.resolveTransitiveDependencies, this.excludedTransitiveDependencies);
      }
   }
}
