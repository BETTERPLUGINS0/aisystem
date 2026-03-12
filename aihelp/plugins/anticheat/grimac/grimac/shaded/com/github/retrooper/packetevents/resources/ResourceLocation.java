package ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.key.KeyPattern;
import ac.grim.grimac.shaded.kyori.adventure.key.Keyed;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ResourceLocation implements Keyed {
   public static final NbtCodec<ResourceLocation> CODEC;
   public static final String VANILLA_NAMESPACE = "minecraft";
   private final Key key;

   public ResourceLocation(Key key) {
      this.key = key;
   }

   public ResourceLocation(@KeyPattern.Namespace String namespace, @KeyPattern.Value String key) {
      this.key = Key.key(namespace, key);
   }

   public ResourceLocation(String location) {
      this.key = Key.key(location);
   }

   public static ResourceLocation read(PacketWrapper<?> wrapper) {
      return wrapper.readIdentifier();
   }

   public static void write(PacketWrapper<?> wrapper, ResourceLocation resourceLocation) {
      wrapper.writeIdentifier(resourceLocation);
   }

   public static ResourceLocation decode(NBT nbt, PacketWrapper<?> wrapper) {
      return new ResourceLocation(((NBTString)nbt).getValue());
   }

   public static NBT encode(PacketWrapper<?> wrapper, ResourceLocation resourceLocation) {
      return new NBTString(resourceLocation.toString());
   }

   public static String getNamespace(String location) {
      int namespaceIdx = location.indexOf(58);
      return namespaceIdx > 0 ? location.substring(0, namespaceIdx) : "minecraft";
   }

   public static String getPath(String location) {
      int namespaceIdx = location.indexOf(58);
      return namespaceIdx != -1 ? location.substring(namespaceIdx + 1) : location;
   }

   @Contract("null -> null; !null -> !null")
   @Nullable
   public static String normString(@Nullable String location) {
      if (location == null) {
         return null;
      } else {
         int index = location.indexOf(58);
         if (index > 0) {
            return location;
         } else {
            return index == -1 ? "minecraft:" + location : "minecraft" + location;
         }
      }
   }

   public Key key() {
      return this.key;
   }

   public String getNamespace() {
      return this.key.namespace();
   }

   public String getKey() {
      return this.key.value();
   }

   public int hashCode() {
      return this.key.hashCode();
   }

   public boolean equals(Object obj) {
      if (obj instanceof ResourceLocation) {
         ResourceLocation other = (ResourceLocation)obj;
         return other.key.equals(this.key);
      } else {
         return false;
      }
   }

   public String toString() {
      return this.key.asString();
   }

   public static ResourceLocation minecraft(@KeyPattern.Value String key) {
      return new ResourceLocation("minecraft", key);
   }

   static {
      CODEC = NbtCodecs.STRING.apply(ResourceLocation::new, ResourceLocation::toString);
   }
}
