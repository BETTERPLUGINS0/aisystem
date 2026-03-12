package fr.xephi.authme.libs.net.kyori.adventure.platform.facet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FacetBase<V> implements Facet<V> {
   protected final Class<? extends V> viewerClass;

   protected FacetBase(@Nullable final Class<? extends V> viewerClass) {
      this.viewerClass = viewerClass;
   }

   public boolean isSupported() {
      return this.viewerClass != null;
   }

   public boolean isApplicable(@NotNull final V viewer) {
      return this.viewerClass != null && this.viewerClass.isInstance(viewer);
   }
}
