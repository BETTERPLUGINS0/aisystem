package fr.xephi.authme.libs.com.alessiodp.libby.configuration;

import fr.xephi.authme.libs.com.alessiodp.libby.Library;
import fr.xephi.authme.libs.com.alessiodp.libby.relocation.Relocation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Configuration {
   @Nullable
   private final Integer version;
   @NotNull
   private final Set<String> repositories;
   @NotNull
   private final Set<Relocation> globalRelocations;
   @NotNull
   private final List<Library> libraries;

   public Configuration(@Nullable Integer version, @Nullable Set<String> repositories, @Nullable Set<Relocation> globalRelocations, @Nullable List<Library> libraries) {
      this.version = version;
      this.repositories = repositories != null ? Collections.unmodifiableSet(new HashSet(repositories)) : Collections.emptySet();
      this.globalRelocations = globalRelocations != null ? Collections.unmodifiableSet(new HashSet(globalRelocations)) : Collections.emptySet();
      this.libraries = libraries != null ? Collections.unmodifiableList(new ArrayList(libraries)) : Collections.emptyList();
   }

   @Nullable
   public Integer getVersion() {
      return this.version;
   }

   @NotNull
   public Set<String> getRepositories() {
      return this.repositories;
   }

   @NotNull
   public Set<Relocation> getGlobalRelocations() {
      return this.globalRelocations;
   }

   @NotNull
   public List<Library> getLibraries() {
      return this.libraries;
   }
}
