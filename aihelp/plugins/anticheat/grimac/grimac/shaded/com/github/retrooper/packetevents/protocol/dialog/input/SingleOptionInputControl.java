package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Iterator;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class SingleOptionInputControl implements InputControl {
   private final int width;
   private final List<SingleOptionInputControl.Entry> options;
   private final Component label;
   private final boolean labelVisible;

   public SingleOptionInputControl(int width, List<SingleOptionInputControl.Entry> options, Component label, boolean labelVisible) {
      boolean initial = false;
      Iterator var6 = options.iterator();

      while(var6.hasNext()) {
         SingleOptionInputControl.Entry entry = (SingleOptionInputControl.Entry)var6.next();
         if (entry.initial) {
            if (initial) {
               throw new IllegalArgumentException("Multiple initial values");
            }

            initial = true;
         }
      }

      this.width = width;
      this.options = options;
      this.label = label;
      this.labelVisible = labelVisible;
   }

   public static SingleOptionInputControl decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      int width = compound.getNumberTagValueOrDefault("width", 200).intValue();
      List<SingleOptionInputControl.Entry> options = compound.getListOrThrow("options", SingleOptionInputControl.Entry::decode, wrapper);
      Component label = (Component)compound.getOrThrow("label", AdventureSerializer.serializer(wrapper), wrapper);
      boolean labelVisible = compound.getBooleanOr("label_visible", true);
      return new SingleOptionInputControl(width, options, label, labelVisible);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, SingleOptionInputControl control) {
      if (control.width != 200) {
         compound.setTag("width", new NBTInt(control.width));
      }

      compound.setList("options", control.options, SingleOptionInputControl.Entry::encode, wrapper);
      compound.set("label", control.label, AdventureSerializer.serializer(wrapper), wrapper);
      if (!control.labelVisible) {
         compound.setTag("label_visible", new NBTByte(false));
      }

   }

   public InputControlType<?> getType() {
      return InputControlTypes.SINGLE_OPTION;
   }

   public int getWidth() {
      return this.width;
   }

   public List<SingleOptionInputControl.Entry> getOptions() {
      return this.options;
   }

   public Component getLabel() {
      return this.label;
   }

   public boolean isLabelVisible() {
      return this.labelVisible;
   }

   public static final class Entry {
      private final String id;
      @Nullable
      private final Component display;
      private final boolean initial;

      public Entry(String id, @Nullable Component display, boolean initial) {
         this.id = id;
         this.display = display;
         this.initial = initial;
      }

      public static SingleOptionInputControl.Entry decode(NBT nbt, PacketWrapper<?> wrapper) {
         if (nbt instanceof NBTString) {
            return new SingleOptionInputControl.Entry(((NBTString)nbt).getValue(), (Component)null, false);
         } else {
            NBTCompound compound = (NBTCompound)nbt;
            String id = compound.getStringTagValueOrThrow("id");
            Component display = (Component)compound.getOrNull("display", AdventureSerializer.serializer(wrapper), wrapper);
            boolean initial = compound.getBooleanOr("initial", false);
            return new SingleOptionInputControl.Entry(id, display, initial);
         }
      }

      public static NBT encode(PacketWrapper<?> wrapper, SingleOptionInputControl.Entry entry) {
         NBTCompound compound = new NBTCompound();
         compound.setTag("id", new NBTString(entry.id));
         if (entry.display != null) {
            compound.set("display", entry.display, AdventureSerializer.serializer(wrapper), wrapper);
         }

         if (entry.initial) {
            compound.setTag("initial", new NBTByte(true));
         }

         return compound;
      }

      public String getId() {
         return this.id;
      }

      @Nullable
      public Component getDisplay() {
         return this.display;
      }

      public boolean isInitial() {
         return this.initial;
      }
   }
}
