package ac.grim.grimac.utils.data.tags;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTags;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

public final class SyncedTag<T> {
   private final ResourceLocation location;
   private final Set<T> values;
   private final Function<Integer, T> remapper;
   private final boolean supported;

   private SyncedTag(ResourceLocation location, Function<Integer, T> remapper, Set<T> defaultValues, boolean supported) {
      this.location = location;
      this.supported = supported;
      this.values = Collections.newSetFromMap(new IdentityHashMap());
      this.remapper = remapper;
      this.values.addAll(defaultValues);
   }

   public static <T> SyncedTag.Builder<T> builder(ResourceLocation location) {
      return new SyncedTag.Builder(location);
   }

   public ResourceLocation location() {
      return this.location;
   }

   public boolean contains(T value) {
      return this.values.contains(value);
   }

   public void readTagValues(WrapperPlayServerTags.Tag tag) {
      if (this.supported) {
         this.values.clear();
         Iterator var2 = tag.getValues().iterator();

         while(var2.hasNext()) {
            int id = (Integer)var2.next();
            this.values.add(this.remapper.apply(id));
         }

      }
   }

   public static final class Builder<T> {
      private final ResourceLocation location;
      private Function<Integer, T> remapper;
      private Set<T> defaultValues;
      private boolean supported = true;

      private Builder(ResourceLocation location) {
         this.location = location;
      }

      public SyncedTag.Builder<T> remapper(Function<Integer, T> remapper) {
         this.remapper = remapper;
         return this;
      }

      public SyncedTag.Builder<T> supported(boolean supported) {
         this.supported = supported;
         return this;
      }

      public SyncedTag.Builder<T> defaults(Set<T> defaultValues) {
         this.defaultValues = defaultValues;
         return this;
      }

      public SyncedTag<T> build() {
         return new SyncedTag(this.location, this.remapper, this.defaultValues, this.supported);
      }
   }
}
