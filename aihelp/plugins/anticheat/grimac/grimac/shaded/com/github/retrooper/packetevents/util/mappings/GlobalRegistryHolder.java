package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class GlobalRegistryHolder implements IRegistryHolder {
   public static final IRegistryHolder INSTANCE = new GlobalRegistryHolder();

   private GlobalRegistryHolder() {
   }

   public static Object getGlobalRegistryCacheKey(@Nullable User user, ClientVersion version) {
      return version;
   }

   @Nullable
   public IRegistry<?> getRegistry(ResourceLocation registryKey, ClientVersion version) {
      SynchronizedRegistriesHandler.RegistryEntry<?> registryEntry = SynchronizedRegistriesHandler.getRegistryEntry(registryKey);
      if (registryEntry == null) {
         return null;
      } else {
         Object cacheKey = getGlobalRegistryCacheKey((User)null, version);
         return registryEntry.getSyncedRegistry(cacheKey);
      }
   }
}
