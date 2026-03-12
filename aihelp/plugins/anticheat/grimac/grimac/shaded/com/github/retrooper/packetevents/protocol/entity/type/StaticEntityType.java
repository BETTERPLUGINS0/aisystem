package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

public class StaticEntityType extends AbstractMappedEntity implements EntityType {
   private final Optional<EntityType> parent;
   private final Map<EntityType, Boolean> parents;
   @Nullable
   private TypesBuilderData legacyData;

   @ApiStatus.Internal
   public StaticEntityType(@Nullable TypesBuilderData data, @Nullable EntityType parent) {
      super(data);
      this.parent = Optional.ofNullable(parent);
      this.parents = new IdentityHashMap();
      this.parents.put(this, true);

      while(parent != null) {
         this.parents.put(parent, true);
         parent = (EntityType)parent.getParent().orElse((Object)null);
      }

   }

   StaticEntityType setLegacyData(@Nullable TypesBuilderData legacyData) {
      this.legacyData = legacyData;
      return this;
   }

   public boolean isInstanceOf(EntityType parent) {
      return parent != null && this.parents.containsKey(parent);
   }

   public Optional<EntityType> getParent() {
      return this.parent;
   }

   public int getLegacyId(ClientVersion version) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_14)) {
         return -1;
      } else if (this.legacyData != null) {
         return this.legacyData.getId(version);
      } else {
         throw new UnsupportedOperationException();
      }
   }
}
