package com.bergerkiller.bukkit.tc.properties;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.properties.api.IProperty;
import com.bergerkiller.bukkit.tc.properties.api.IPropertyRegistry;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParseResult;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyInputContext;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import org.bukkit.entity.Player;

public interface IProperties extends IParsable, TrainCarts.Provider {
   boolean isRemoved();

   <T> T get(IProperty<T> var1);

   <T> void set(IProperty<T> var1, T var2);

   default <T> T update(IProperty<T> property, Function<T, T> operation) {
      T old_value = this.get(property);
      T new_value = operation.apply(old_value);
      if (old_value != new_value) {
         this.set(property, new_value);
      }

      return new_value;
   }

   default PropertyParseResult<?> parseAndSet(String name, String input) {
      return IPropertyRegistry.instance().parseAndSet(this, name, input);
   }

   default PropertyParseResult<?> parseAndSet(String name, PropertyInputContext inputContext) {
      return IPropertyRegistry.instance().parseAndSet(this, name, inputContext);
   }

   ConfigurationNode getConfig();

   void load(ConfigurationNode var1);

   void save(ConfigurationNode var1);

   String getTypeName();

   boolean matchTag(String var1);

   boolean hasTags();

   void clearTags();

   void removeTags(String... var1);

   void addTags(String... var1);

   boolean hasOwnership(Player var1);

   boolean hasOwners();

   Set<String> getOwners();

   void setOwners(Set<String> var1);

   void addOwners(Collection<String> var1);

   void removeOwners(Collection<String> var1);

   boolean hasOwnerPermissions();

   Set<String> getOwnerPermissions();

   void setOwnerPermissions(Set<String> var1);

   void clearOwners();

   void clearOwnerPermissions();

   boolean isOwnedByEveryone();

   Collection<String> getTags();

   void setTags(String... var1);

   boolean isOwner(Player var1);

   void setPickup(boolean var1);

   boolean getCanOnlyOwnersEnter();

   void setCanOnlyOwnersEnter(boolean var1);

   boolean getPlayersEnter();

   void setPlayersEnter(boolean var1);

   boolean getPlayersExit();

   void setPlayersExit(boolean var1);

   boolean isInvincible();

   void setInvincible(boolean var1);

   boolean getSpawnItemDrops();

   void setSpawnItemDrops(boolean var1);

   void clearDestination();

   boolean hasDestination();

   String getLastPathNode();

   void setLastPathNode(String var1);

   String getDestination();

   void setDestination(String var1);

   List<String> getDestinationRoute();

   void setDestinationRoute(List<String> var1);

   void clearDestinationRoute();

   void addDestinationToRoute(String var1);

   void removeDestinationFromRoute(String var1);

   int getCurrentRouteDestinationIndex();

   default String getNextDestinationOnRoute() {
      return this.getNextDestinationOnRoute(this.getDestination());
   }

   String getNextDestinationOnRoute(String var1);

   void setEnterMessage(String var1);

   BlockLocation getLocation();

   IPropertiesHolder getHolder();

   CompletableFuture<Boolean> restore();

   boolean hasHolder();
}
