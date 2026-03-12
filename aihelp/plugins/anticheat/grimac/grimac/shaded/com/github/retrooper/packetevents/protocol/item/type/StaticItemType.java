package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class StaticItemType extends AbstractMappedEntity implements ItemType {
   private final int maxAmount;
   private final int maxDurability;
   private final ItemType craftRemainder;
   @Nullable
   private final StateType placedType;
   private final Set<ItemTypes.ItemAttribute> attributes;
   private final Map<ClientVersion, StaticComponentMap> components;

   @ApiStatus.Internal
   public StaticItemType(@Nullable TypesBuilderData data, int maxAmount, int maxDurability, ItemType craftRemainder, @Nullable StateType placedType, Set<ItemTypes.ItemAttribute> attributes) {
      super(data);
      this.maxAmount = maxAmount;
      this.maxDurability = maxDurability;
      this.craftRemainder = craftRemainder;
      this.placedType = placedType;
      this.attributes = attributes;
      this.components = new EnumMap(ClientVersion.class);
   }

   public int getMaxAmount() {
      return this.maxAmount;
   }

   public int getMaxDurability() {
      return this.maxDurability;
   }

   public ItemType getCraftRemainder() {
      return this.craftRemainder;
   }

   @Nullable
   public StateType getPlacedType() {
      return this.placedType;
   }

   public Set<ItemTypes.ItemAttribute> getAttributes() {
      return this.attributes;
   }

   public StaticComponentMap getComponents(ClientVersion version) {
      if (!version.isRelease()) {
         throw new IllegalArgumentException("Unsupported version for getting components of " + this.getName() + ": " + version);
      } else {
         return (StaticComponentMap)this.components.getOrDefault(version, StaticComponentMap.SHARED_ITEM_COMPONENTS);
      }
   }

   void setComponents(ClientVersion version, StaticComponentMap components) {
      if (this.components.containsKey(version)) {
         throw new IllegalStateException("Components are already defined for " + this.getName() + " in version " + version);
      } else if (!version.isRelease()) {
         throw new IllegalArgumentException("Unsupported version for setting components of " + this.getName() + ": " + version);
      } else {
         this.components.put(version, components);
      }
   }

   boolean hasComponents(ClientVersion version) {
      return this.components.containsKey(version);
   }

   void fillComponents() {
      StaticComponentMap lastComponents = null;
      ClientVersion[] var2 = ClientVersion.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ClientVersion version = var2[var4];
         if (version.isRelease()) {
            StaticComponentMap components = (StaticComponentMap)this.components.get(version);
            if (components == null) {
               if (lastComponents != null) {
                  this.components.put(version, lastComponents);
               }
            } else {
               if (lastComponents == null) {
                  ClientVersion[] var7 = ClientVersion.values();
                  int var8 = var7.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     ClientVersion beforeVersion = var7[var9];
                     if (beforeVersion == version) {
                        break;
                     }

                     this.components.put(beforeVersion, components);
                  }
               }

               lastComponents = components;
            }
         }
      }

   }
}
