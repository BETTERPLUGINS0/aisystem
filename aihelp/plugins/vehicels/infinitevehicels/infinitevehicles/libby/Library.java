package me.PM2.infinitevehicles.libby;

import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;
import me.PM2.infinitevehicles.libby.relocation.Relocation;

public class Library {
   private final Collection<String> urls;
   private final Collection<String> repositories;
   private final String id;
   private final String groupId;
   private final String artifactId;
   private final String version;
   private final String classifier;
   private final byte[] checksum;
   private final Collection<Relocation> relocations;
   private final String path;
   private final String partialPath;
   private final String relocatedPath;
   private final boolean isolatedLoad;

   private Library(Collection<String> urls, String id, String groupId, String artifactId, String version, String classifier, byte[] checksum, Collection<Relocation> relocations, boolean isolatedLoad) {
      this(var1, (Collection)null, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   private Library(Collection<String> urls, Collection<String> repositories, String id, String groupId, String artifactId, String version, String classifier, byte[] checksum, Collection<Relocation> relocations, boolean isolatedLoad) {
      this.urls = var1 != null ? Collections.unmodifiableList(new LinkedList(var1)) : Collections.emptyList();
      this.id = var3 != null ? var3 : UUID.randomUUID().toString();
      this.groupId = ((String)Objects.requireNonNull(var4, "groupId")).replace("{}", ".");
      this.artifactId = (String)Objects.requireNonNull(var5, "artifactId");
      this.version = (String)Objects.requireNonNull(var6, "version");
      this.classifier = var7;
      this.checksum = var8;
      this.relocations = var9 != null ? Collections.unmodifiableList(new LinkedList(var9)) : Collections.emptyList();
      this.partialPath = this.groupId.replace('.', '/') + '/' + var5 + '/' + var6 + '/';
      String var11 = this.partialPath + var5 + '-' + var6;
      if (this.hasClassifier()) {
         var11 = var11 + '-' + var7;
      }

      this.path = var11 + ".jar";
      this.repositories = var2 != null ? Collections.unmodifiableList(new LinkedList(var2)) : Collections.emptyList();
      this.relocatedPath = this.hasRelocations() ? var11 + "-relocated.jar" : null;
      this.isolatedLoad = var10;
   }

   public Collection<String> getUrls() {
      return this.urls;
   }

   public Collection<String> getRepositories() {
      return this.repositories;
   }

   public String getId() {
      return this.id;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getArtifactId() {
      return this.artifactId;
   }

   public String getVersion() {
      return this.version;
   }

   public String getClassifier() {
      return this.classifier;
   }

   public boolean hasClassifier() {
      return this.classifier != null;
   }

   public byte[] getChecksum() {
      return this.checksum;
   }

   public boolean hasChecksum() {
      return this.checksum != null;
   }

   public Collection<Relocation> getRelocations() {
      return this.relocations;
   }

   public boolean hasRelocations() {
      return !this.relocations.isEmpty();
   }

   public String getPath() {
      return this.path;
   }

   public String getPartialPath() {
      return this.partialPath;
   }

   public String getRelocatedPath() {
      return this.relocatedPath;
   }

   public boolean isIsolatedLoad() {
      return this.isolatedLoad;
   }

   public boolean isSnapshot() {
      return this.version.endsWith("-SNAPSHOT");
   }

   public String toString() {
      String var1 = this.groupId + ':' + this.artifactId + ':' + this.version;
      if (this.hasClassifier()) {
         var1 = var1 + ':' + this.classifier;
      }

      return var1;
   }

   public static Library.Builder builder() {
      return new Library.Builder();
   }

   // $FF: synthetic method
   Library(Collection var1, Collection var2, String var3, String var4, String var5, String var6, String var7, byte[] var8, Collection var9, boolean var10, Object var11) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public static class Builder {
      private final Collection<String> urls = new LinkedList();
      private final Collection<String> repositories = new LinkedList();
      private String id;
      private String groupId;
      private String artifactId;
      private String version;
      private String classifier;
      private byte[] checksum;
      private boolean isolatedLoad;
      private final Collection<Relocation> relocations = new LinkedList();

      public Library.Builder url(String url) {
         this.urls.add((String)Objects.requireNonNull(var1, "url"));
         return this;
      }

      public Library.Builder repository(String url) {
         this.repositories.add(((String)Objects.requireNonNull(var1, "repository")).endsWith("/") ? var1 : var1 + '/');
         return this;
      }

      public Library.Builder id(String id) {
         this.id = var1 != null ? var1 : UUID.randomUUID().toString();
         return this;
      }

      public Library.Builder groupId(String groupId) {
         this.groupId = (String)Objects.requireNonNull(var1, "groupId");
         return this;
      }

      public Library.Builder artifactId(String artifactId) {
         this.artifactId = (String)Objects.requireNonNull(var1, "artifactId");
         return this;
      }

      public Library.Builder version(String version) {
         this.version = (String)Objects.requireNonNull(var1, "version");
         return this;
      }

      public Library.Builder classifier(String classifier) {
         this.classifier = (String)Objects.requireNonNull(var1, "classifier");
         return this;
      }

      public Library.Builder checksum(byte[] checksum) {
         this.checksum = (byte[])Objects.requireNonNull(var1, "checksum");
         return this;
      }

      public Library.Builder checksum(String checksum) {
         return this.checksum(Base64.getDecoder().decode((String)Objects.requireNonNull(var1, "checksum")));
      }

      public Library.Builder isolatedLoad(boolean isolatedLoad) {
         this.isolatedLoad = var1;
         return this;
      }

      public Library.Builder relocate(Relocation relocation) {
         this.relocations.add((Relocation)Objects.requireNonNull(var1, "relocation"));
         return this;
      }

      public Library.Builder relocate(String pattern, String relocatedPattern) {
         return this.relocate(new Relocation(var1, var2));
      }

      public Library build() {
         return new Library(this.urls, this.repositories, this.id, this.groupId, this.artifactId, this.version, this.classifier, this.checksum, this.relocations, this.isolatedLoad);
      }
   }
}
