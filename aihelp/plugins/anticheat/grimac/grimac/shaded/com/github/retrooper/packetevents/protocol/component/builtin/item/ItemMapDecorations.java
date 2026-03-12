package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.mapdecoration.MapDecorationType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.mapdecoration.MapDecorationTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

public class ItemMapDecorations {
   private Map<String, ItemMapDecorations.Decoration> decorations;

   public ItemMapDecorations(Map<String, ItemMapDecorations.Decoration> decorations) {
      this.decorations = decorations;
   }

   public static ItemMapDecorations read(PacketWrapper<?> wrapper) {
      NBTCompound compound = wrapper.readNBT();
      Map<String, ItemMapDecorations.Decoration> decorations = new HashMap(compound.size());
      Iterator var3 = compound.getTags().entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, NBT> tag = (Entry)var3.next();
         ItemMapDecorations.Decoration decoration = ItemMapDecorations.Decoration.readCompound((NBTCompound)tag.getValue());
         decorations.put((String)tag.getKey(), decoration);
      }

      return new ItemMapDecorations(decorations);
   }

   public static void write(PacketWrapper<?> wrapper, ItemMapDecorations decorations) {
      NBTCompound compound = new NBTCompound();
      Iterator var3 = decorations.decorations.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, ItemMapDecorations.Decoration> decoration = (Entry)var3.next();
         NBTCompound entry = new NBTCompound();
         ItemMapDecorations.Decoration.writeCompound(entry, (ItemMapDecorations.Decoration)decoration.getValue());
         compound.setTag((String)decoration.getKey(), entry);
      }

      wrapper.writeNBT(compound);
   }

   @Nullable
   public ItemMapDecorations.Decoration getDecoration(String key) {
      return (ItemMapDecorations.Decoration)this.decorations.get(key);
   }

   public void setDecoration(String key, @Nullable ItemMapDecorations.Decoration decoration) {
      if (decoration != null) {
         this.decorations.put(key, decoration);
      } else {
         this.decorations.remove(key);
      }

   }

   public Map<String, ItemMapDecorations.Decoration> getDecorations() {
      return this.decorations;
   }

   public void setDecorations(Map<String, ItemMapDecorations.Decoration> decorations) {
      this.decorations = decorations;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemMapDecorations)) {
         return false;
      } else {
         ItemMapDecorations that = (ItemMapDecorations)obj;
         return this.decorations.equals(that.decorations);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.decorations);
   }

   public static final class Decoration {
      private MapDecorationType type;
      private double x;
      private double z;
      private float rotation;

      public Decoration(MapDecorationType type, double x, double z, float rotation) {
         this.type = type;
         this.x = x;
         this.z = z;
         this.rotation = rotation;
      }

      @ApiStatus.Internal
      public static ItemMapDecorations.Decoration readCompound(NBTCompound compound) {
         MapDecorationType type = MapDecorationTypes.getByName(compound.getStringTagValueOrThrow("type"));
         double x = compound.getNumberTagOrThrow("x").getAsDouble();
         double z = compound.getNumberTagOrThrow("z").getAsDouble();
         float rotation = compound.getNumberTagOrThrow("rotation").getAsFloat();
         return new ItemMapDecorations.Decoration(type, x, z, rotation);
      }

      @ApiStatus.Internal
      public static void writeCompound(NBTCompound compound, ItemMapDecorations.Decoration decoration) {
         compound.setTag("type", new NBTString(decoration.type.getName().toString()));
         compound.setTag("x", new NBTDouble(decoration.x));
         compound.setTag("z", new NBTDouble(decoration.z));
         compound.setTag("rotation", new NBTFloat(decoration.rotation));
      }

      public MapDecorationType getType() {
         return this.type;
      }

      public void setType(MapDecorationType type) {
         this.type = type;
      }

      public double getX() {
         return this.x;
      }

      public void setX(double x) {
         this.x = x;
      }

      public double getZ() {
         return this.z;
      }

      public void setZ(double z) {
         this.z = z;
      }

      public float getRotation() {
         return this.rotation;
      }

      public void setRotation(float rotation) {
         this.rotation = rotation;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof ItemMapDecorations.Decoration)) {
            return false;
         } else {
            ItemMapDecorations.Decoration that = (ItemMapDecorations.Decoration)obj;
            if (Double.compare(that.x, this.x) != 0) {
               return false;
            } else if (Double.compare(that.z, this.z) != 0) {
               return false;
            } else {
               return Float.compare(that.rotation, this.rotation) != 0 ? false : this.type.equals(that.type);
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.type, this.x, this.z, this.rotation});
      }
   }
}
