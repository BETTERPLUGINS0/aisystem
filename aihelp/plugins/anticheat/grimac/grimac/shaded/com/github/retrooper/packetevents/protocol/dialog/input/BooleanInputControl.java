package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class BooleanInputControl implements InputControl {
   private final Component label;
   private final boolean initial;
   private final String onTrue;
   private final String onFalse;

   public BooleanInputControl(Component label, boolean initial, String onTrue, String onFalse) {
      this.label = label;
      this.initial = initial;
      this.onTrue = onTrue;
      this.onFalse = onFalse;
   }

   public static BooleanInputControl decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      Component label = (Component)compound.getOrThrow("label", AdventureSerializer.serializer(wrapper), wrapper);
      boolean initial = compound.getBoolean("initial");
      String onTrue = compound.getStringTagValueOrDefault("on_true", "true");
      String onFalse = compound.getStringTagValueOrDefault("on_false", "false");
      return new BooleanInputControl(label, initial, onTrue, onFalse);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, BooleanInputControl control) {
      compound.set("label", control.label, AdventureSerializer.serializer(wrapper), wrapper);
      if (control.initial) {
         compound.setTag("initial", new NBTByte(true));
      }

      if (!"true".equals(control.onTrue)) {
         compound.setTag("on_true", new NBTString(control.onTrue));
      }

      if (!"false".equals(control.onFalse)) {
         compound.setTag("on_false", new NBTString(control.onFalse));
      }

   }

   public InputControlType<?> getType() {
      return InputControlTypes.BOOLEAN;
   }

   public Component getLabel() {
      return this.label;
   }

   public boolean isInitial() {
      return this.initial;
   }

   public String getOnTrue() {
      return this.onTrue;
   }

   public String getOnFalse() {
      return this.onFalse;
   }
}
