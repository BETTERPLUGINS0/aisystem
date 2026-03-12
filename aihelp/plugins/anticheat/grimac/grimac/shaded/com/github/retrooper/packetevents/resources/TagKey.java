package ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TagKey {
   public static final NbtCodec<TagKey> CODEC;
   private final ResourceLocation id;

   public TagKey(ResourceLocation id) {
      this.id = id;
   }

   public static TagKey parse(String name) {
      TagKey tagKey = tryParse(name);
      if (tagKey == null) {
         throw new IllegalArgumentException("Not a tag: " + name);
      } else {
         return tagKey;
      }
   }

   @Nullable
   public static TagKey tryParse(String name) {
      return !name.isEmpty() && name.charAt(0) == '#' ? new TagKey(new ResourceLocation(name.substring(1))) : null;
   }

   public ResourceLocation getId() {
      return this.id;
   }

   public String toString() {
      return '#' + this.id.toString();
   }

   static {
      CODEC = NbtCodecs.STRING.apply((name) -> {
         TagKey tagKey = tryParse(name);
         if (tagKey == null) {
            throw new NbtCodecException("Not a tag: " + name);
         } else {
            return tagKey;
         }
      }, TagKey::toString);
   }
}
