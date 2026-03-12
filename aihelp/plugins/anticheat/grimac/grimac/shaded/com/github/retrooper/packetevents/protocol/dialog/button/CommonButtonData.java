package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.button;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CommonButtonData {
   private final Component label;
   @Nullable
   private final Component tooltip;
   private final int width;

   public CommonButtonData(Component label, @Nullable Component tooltip, int width) {
      this.label = label;
      this.tooltip = tooltip;
      this.width = width;
   }

   public static CommonButtonData decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      AdventureSerializer serializer = AdventureSerializer.serializer(wrapper);
      Component label = (Component)compound.getOrThrow("label", serializer, wrapper);
      Component tooltip = (Component)compound.getOrNull("tooltip", serializer, wrapper);
      int width = compound.getNumberTagValueOrDefault("width", 150).intValue();
      return new CommonButtonData(label, tooltip, width);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, CommonButtonData button) {
      AdventureSerializer serializer = AdventureSerializer.serializer(wrapper);
      compound.set("label", button.label, serializer, wrapper);
      if (button.tooltip != null) {
         compound.set("tooltip", button.tooltip, serializer, wrapper);
      }

      if (button.width != 150) {
         compound.setTag("width", new NBTInt(button.width));
      }

   }

   public Component getLabel() {
      return this.label;
   }

   @Nullable
   public Component getTooltip() {
      return this.tooltip;
   }

   public int getWidth() {
      return this.width;
   }
}
