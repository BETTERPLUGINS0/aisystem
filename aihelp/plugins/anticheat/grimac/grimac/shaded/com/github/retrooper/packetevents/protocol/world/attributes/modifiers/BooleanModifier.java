package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.modifiers;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum BooleanModifier implements AttributeModifier<Boolean, Boolean> {
   AND {
      public Boolean apply(Boolean value, Boolean arg) {
         return arg && value;
      }
   },
   NAND {
      public Boolean apply(Boolean value, Boolean arg) {
         return !arg || !value;
      }
   },
   OR {
      public Boolean apply(Boolean value, Boolean arg) {
         return arg || value;
      }
   },
   NOR {
      public Boolean apply(Boolean value, Boolean arg) {
         return !arg && !value;
      }
   },
   XOR {
      public Boolean apply(Boolean value, Boolean arg) {
         return arg ^ value;
      }
   },
   XNOR {
      public Boolean apply(Boolean value, Boolean arg) {
         return arg == value;
      }
   };

   private BooleanModifier() {
   }

   public NbtCodec<Boolean> argumentCodec(EnvironmentAttribute<Boolean> attribute) {
      return NbtCodecs.BOOLEAN;
   }

   // $FF: synthetic method
   private static BooleanModifier[] $values() {
      return new BooleanModifier[]{AND, NAND, OR, NOR, XOR, XNOR};
   }

   // $FF: synthetic method
   BooleanModifier(Object x2) {
      this();
   }
}
