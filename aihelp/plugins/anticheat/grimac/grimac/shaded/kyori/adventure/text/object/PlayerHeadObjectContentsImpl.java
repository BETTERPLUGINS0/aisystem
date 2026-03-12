package ac.grim.grimac.shaded.kyori.adventure.text.object;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

final class PlayerHeadObjectContentsImpl implements PlayerHeadObjectContents {
   @Nullable
   private final String name;
   @Nullable
   private final UUID id;
   private final List<PlayerHeadObjectContents.ProfileProperty> properties;
   private final boolean hat;
   @Nullable
   private final Key texture;

   PlayerHeadObjectContentsImpl(@Nullable final String name, @Nullable final UUID id, @NotNull final List<PlayerHeadObjectContents.ProfileProperty> properties, final boolean hat, @Nullable final Key texture) {
      this.name = name;
      this.id = id;
      if (properties.isEmpty()) {
         this.properties = Collections.emptyList();
      } else {
         this.properties = Collections.unmodifiableList(new ArrayList((Collection)Objects.requireNonNull(properties, "properties")));
      }

      this.hat = hat;
      this.texture = texture;
   }

   @Nullable
   public String name() {
      return this.name;
   }

   @Nullable
   public UUID id() {
      return this.id;
   }

   @NotNull
   public List<PlayerHeadObjectContents.ProfileProperty> profileProperties() {
      return this.properties;
   }

   public boolean hat() {
      return this.hat;
   }

   @Nullable
   public Key texture() {
      return this.texture;
   }

   @NotNull
   public PlayerHeadObjectContents.Builder toBuilder() {
      return new PlayerHeadObjectContentsImpl.BuilderImpl(this);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof PlayerHeadObjectContents)) {
         return false;
      } else {
         PlayerHeadObjectContentsImpl that = (PlayerHeadObjectContentsImpl)other;
         return Objects.equals(this.name, that.name) && Objects.equals(this.id, that.id) && Objects.equals(this.properties, that.properties) && this.hat == that.hat && Objects.equals(this.texture, that.texture);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.name, this.id, this.properties, this.hat, this.texture});
   }

   public String toString() {
      return Internals.toString(this);
   }

   static final class BuilderImpl implements PlayerHeadObjectContents.Builder {
      @Nullable
      private String name;
      @Nullable
      private UUID id;
      private final List<PlayerHeadObjectContents.ProfileProperty> properties = new ArrayList();
      private boolean hat = true;
      @Nullable
      private Key texture;

      BuilderImpl() {
      }

      BuilderImpl(@NotNull final PlayerHeadObjectContentsImpl playerHeadObjectContents) {
         this.name = playerHeadObjectContents.name;
         this.id = playerHeadObjectContents.id;
         this.properties.addAll(playerHeadObjectContents.properties);
         this.hat = playerHeadObjectContents.hat;
         this.texture = playerHeadObjectContents.texture;
      }

      @NotNull
      public PlayerHeadObjectContents.Builder name(@Nullable final String name) {
         this.name = name;
         return this;
      }

      @NotNull
      public PlayerHeadObjectContents.Builder id(@Nullable final UUID id) {
         this.id = id;
         return this;
      }

      @NotNull
      public PlayerHeadObjectContents.Builder profileProperty(@NotNull final PlayerHeadObjectContents.ProfileProperty property) {
         this.properties.add((PlayerHeadObjectContents.ProfileProperty)Objects.requireNonNull(property, "property"));
         return this;
      }

      @NotNull
      public PlayerHeadObjectContents.Builder profileProperties(@NotNull final Collection<PlayerHeadObjectContents.ProfileProperty> properties) {
         Iterator var2 = ((Collection)Objects.requireNonNull(properties, "properties")).iterator();

         while(var2.hasNext()) {
            PlayerHeadObjectContents.ProfileProperty property = (PlayerHeadObjectContents.ProfileProperty)var2.next();
            this.profileProperty(property);
         }

         return this;
      }

      private void clearProfile() {
         this.name = null;
         this.id = null;
         this.properties.clear();
         this.texture = null;
      }

      @NotNull
      public PlayerHeadObjectContents.Builder skin(@NotNull final PlayerHeadObjectContents.SkinSource skinSource) {
         this.clearProfile();
         ((PlayerHeadObjectContents.SkinSource)Objects.requireNonNull(skinSource, "skinSource")).applySkinToPlayerHeadContents(this);
         return this;
      }

      @NotNull
      public PlayerHeadObjectContents.Builder hat(final boolean hat) {
         this.hat = hat;
         return this;
      }

      @NotNull
      public PlayerHeadObjectContents.Builder texture(@Nullable final Key texture) {
         this.texture = texture;
         return this;
      }

      @NotNull
      public PlayerHeadObjectContents build() {
         return new PlayerHeadObjectContentsImpl(this.name, this.id, this.properties, this.hat, this.texture);
      }
   }

   static final class ProfilePropertyImpl implements PlayerHeadObjectContents.ProfileProperty {
      private final String name;
      private final String value;
      @Nullable
      private final String signature;

      ProfilePropertyImpl(@NotNull final String name, @NotNull final String value, @Nullable final String signature) {
         this.name = name;
         this.value = value;
         this.signature = signature;
      }

      @NotNull
      public String name() {
         return this.name;
      }

      @NotNull
      public String value() {
         return this.value;
      }

      @Nullable
      public String signature() {
         return this.signature;
      }

      public boolean equals(@Nullable final Object other) {
         if (this == other) {
            return true;
         } else if (!(other instanceof PlayerHeadObjectContentsImpl.ProfilePropertyImpl)) {
            return false;
         } else {
            PlayerHeadObjectContentsImpl.ProfilePropertyImpl that = (PlayerHeadObjectContentsImpl.ProfilePropertyImpl)other;
            return Objects.equals(this.name, that.name) && Objects.equals(this.value, that.value) && Objects.equals(this.signature, that.signature);
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.name, this.value, this.signature});
      }

      public String toString() {
         return Internals.toString(this);
      }
   }
}
