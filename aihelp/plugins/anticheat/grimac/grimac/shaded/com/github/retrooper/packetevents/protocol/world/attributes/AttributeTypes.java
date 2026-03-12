package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.AlphaColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.Particle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.modifiers.AttributeModifier;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.BiomeEffects;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.util.TriState;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class AttributeTypes {
   private static final VersionedRegistry<AttributeType<?>> REGISTRY = new VersionedRegistry("attribute_type");
   public static final AttributeType<Boolean> BOOLEAN;
   @ApiStatus.Obsolete
   public static final AttributeType<TriState> TRI_STATE;
   public static final AttributeType<Float> FLOAT;
   public static final AttributeType<Float> ANGLE_DEGREES;
   public static final AttributeType<Color> RGB_COLOR;
   public static final AttributeType<AlphaColor> ARGB_COLOR;
   public static final AttributeType<MoonPhase> MOON_PHASE;
   @ApiStatus.Obsolete
   public static final AttributeType<?> ACTIVITY;
   @ApiStatus.Obsolete
   public static final AttributeType<?> BED_RULE;
   public static final AttributeType<Particle<?>> PARTICLE;
   public static final AttributeType<List<BiomeEffects.ParticleSettings>> AMBIENT_PARTICLES;
   public static final AttributeType<BackgroundMusic> BACKGROUND_MUSIC;
   public static final AttributeType<AmbientSounds> AMBIENT_SOUNDS;

   private AttributeTypes() {
   }

   @ApiStatus.Internal
   public static <T> AttributeType<T> defineUnsynced(String name) {
      return define(name, (NbtCodec)null, Collections.emptyMap());
   }

   @ApiStatus.Internal
   public static <T> AttributeType<T> define(String name, NbtCodec<T> codec) {
      return define(name, codec, Collections.emptyMap());
   }

   @ApiStatus.Internal
   public static <T> AttributeType<T> define(String name, @Nullable NbtCodec<T> codec, Map<AttributeModifier.Operation, AttributeModifier<T, ?>> modifiers) {
      return (AttributeType)REGISTRY.define(name, (data) -> {
         return new StaticAttributeType(data, codec, createModifierCodec(modifiers));
      });
   }

   @ApiStatus.Internal
   public static <T> NbtCodec<AttributeModifier<T, ?>> createModifierCodec(Map<AttributeModifier.Operation, AttributeModifier<T, ?>> modifiers) {
      final Map<AttributeModifier.Operation, AttributeModifier<T, ?>> allModifiers = new HashMap(modifiers.size() + 1);
      allModifiers.put(AttributeModifier.Operation.OVERRIDE, AttributeModifier.override());
      allModifiers.putAll(modifiers);
      final Map<AttributeModifier<T, ?>, AttributeModifier.Operation> allInverseModifiers = new HashMap(modifiers.size() + 1);
      Iterator var3 = allModifiers.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<AttributeModifier.Operation, AttributeModifier<T, ?>> entry = (Entry)var3.next();
         allInverseModifiers.put((AttributeModifier)entry.getValue(), (AttributeModifier.Operation)entry.getKey());
      }

      return new NbtCodec<AttributeModifier<T, ?>>() {
         public AttributeModifier<T, ?> decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
            AttributeModifier.Operation op = (AttributeModifier.Operation)AttributeModifier.Operation.CODEC.decode(nbt, wrapper);
            AttributeModifier<T, ?> modifier = (AttributeModifier)allModifiers.get(op);
            if (modifier == null) {
               throw new NbtCodecException("Unsupported operation " + op + " for " + modifiers);
            } else {
               return modifier;
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, AttributeModifier<T, ?> value) throws NbtCodecException {
            AttributeModifier.Operation op = (AttributeModifier.Operation)allInverseModifiers.get(value);
            if (op == null) {
               throw new NbtCodecException("Unsupported modifier " + value + " for " + modifiers);
            } else {
               return AttributeModifier.Operation.CODEC.encode(wrapper, op);
            }
         }
      };
   }

   public static VersionedRegistry<AttributeType<?>> getRegistry() {
      return REGISTRY;
   }

   static {
      BOOLEAN = define("boolean", NbtCodecs.BOOLEAN, AttributeModifier.BOOLEAN_LIBRARY);
      TRI_STATE = defineUnsynced("tri_state");
      FLOAT = define("float", NbtCodecs.FLOAT, AttributeModifier.FLOAT_LIBRARY);
      ANGLE_DEGREES = define("angle_degrees", NbtCodecs.FLOAT, AttributeModifier.FLOAT_LIBRARY);
      RGB_COLOR = define("rgb_color", NbtCodecs.RGB_COLOR, AttributeModifier.RGB_COLOR_LIBRARY);
      ARGB_COLOR = define("argb_color", NbtCodecs.ARGB_COLOR, AttributeModifier.ARGB_COLOR_LIBRARY);
      MOON_PHASE = define("moon_phase", MoonPhase.CODEC);
      ACTIVITY = defineUnsynced("activity");
      BED_RULE = defineUnsynced("bed_rule");
      PARTICLE = define("particle", Particle.CODEC);
      AMBIENT_PARTICLES = define("ambient_particles", BiomeEffects.ParticleSettings.CODEC.applyList());
      BACKGROUND_MUSIC = define("background_music", BackgroundMusic.CODEC);
      AMBIENT_SOUNDS = define("ambient_sounds", AmbientSounds.CODEC);
      REGISTRY.unloadMappings();
   }
}
