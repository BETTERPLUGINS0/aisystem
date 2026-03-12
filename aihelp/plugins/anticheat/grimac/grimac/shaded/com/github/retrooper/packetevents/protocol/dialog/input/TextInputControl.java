package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class TextInputControl implements InputControl {
   private final int width;
   private final Component label;
   private final boolean labelVisible;
   private final String initial;
   private final int maxLength;
   @Nullable
   private final TextInputControl.MultilineOptions multiline;

   public TextInputControl(int width, Component label, boolean labelVisible, String initial, int maxLength, @Nullable TextInputControl.MultilineOptions multiline) {
      if (initial.length() > maxLength) {
         throw new IllegalArgumentException("Default text length exceeds allowed size");
      } else {
         this.width = width;
         this.label = label;
         this.labelVisible = labelVisible;
         this.initial = initial;
         this.maxLength = maxLength;
         this.multiline = multiline;
      }
   }

   public static TextInputControl decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      int width = compound.getNumberTagValueOrDefault("width", 200).intValue();
      Component label = (Component)compound.getOrThrow("label", AdventureSerializer.serializer(wrapper), wrapper);
      boolean labelVisible = compound.getBooleanOr("label_visible", true);
      String initial = compound.getStringTagValueOrDefault("initial", "");
      int maxLength = compound.getNumberTagValueOrDefault("max_length", 32).intValue();
      TextInputControl.MultilineOptions multiline = (TextInputControl.MultilineOptions)compound.getOrNull("multiline", TextInputControl.MultilineOptions::decode, wrapper);
      return new TextInputControl(width, label, labelVisible, initial, maxLength, multiline);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, TextInputControl control) {
      if (control.width != 200) {
         compound.setTag("width", new NBTInt(control.width));
      }

      compound.set("label", control.label, AdventureSerializer.serializer(wrapper), wrapper);
      if (!control.labelVisible) {
         compound.setTag("label_visible", new NBTByte(false));
      }

      if (!control.initial.isEmpty()) {
         compound.setTag("initial", new NBTString(control.initial));
      }

      if (control.maxLength != 32) {
         compound.setTag("max_length", new NBTInt(control.maxLength));
      }

      if (control.multiline != null) {
         compound.set("multiline", control.multiline, TextInputControl.MultilineOptions::encode, wrapper);
      }

   }

   public InputControlType<?> getType() {
      return InputControlTypes.TEXT;
   }

   public int getWidth() {
      return this.width;
   }

   public Component getLabel() {
      return this.label;
   }

   public boolean isLabelVisible() {
      return this.labelVisible;
   }

   public String getInitial() {
      return this.initial;
   }

   public int getMaxLength() {
      return this.maxLength;
   }

   @Nullable
   public TextInputControl.MultilineOptions getMultiline() {
      return this.multiline;
   }

   public static final class MultilineOptions {
      @Nullable
      private final Integer maxLines;
      @Nullable
      private final Integer height;

      public MultilineOptions(@Nullable Integer maxLines, @Nullable Integer height) {
         this.maxLines = maxLines;
         this.height = height;
      }

      public static TextInputControl.MultilineOptions decode(NBT nbt, PacketWrapper<?> wrapper) {
         NBTCompound compound = (NBTCompound)nbt;
         Number maxLines = compound.getNumberTagValueOrNull("max_lines");
         Number height = compound.getNumberTagValueOrNull("height");
         return new TextInputControl.MultilineOptions(maxLines != null ? maxLines.intValue() : null, height != null ? height.intValue() : null);
      }

      public static NBT encode(PacketWrapper<?> wrapper, TextInputControl.MultilineOptions options) {
         NBTCompound compound = new NBTCompound();
         if (options.maxLines != null) {
            compound.setTag("max_lines", new NBTInt(options.maxLines));
         }

         if (options.height != null) {
            compound.setTag("height", new NBTInt(options.height));
         }

         return compound;
      }

      @Nullable
      public Integer getMaxLines() {
         return this.maxLines;
      }

      @Nullable
      public Integer getHeight() {
         return this.height;
      }
   }
}
