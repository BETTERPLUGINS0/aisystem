package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class NBTList<T extends NBT> extends NBT {
   protected final NBTType<T> type;
   protected final List<T> tags;

   public NBTList(NBTType<T> type) {
      this.type = type;
      this.tags = new ArrayList();
   }

   public NBTList(NBTType<T> type, int size) {
      this.type = type;
      this.tags = new ArrayList(size);
   }

   public NBTList(NBTType<T> type, List<T> tags) {
      this.type = type;
      this.tags = new ArrayList();
      this.tags.addAll(tags);
   }

   public static NBTList<NBTCompound> createCompoundList() {
      return new NBTList(NBTType.COMPOUND);
   }

   public static NBTList<NBTString> createStringList() {
      return new NBTList(NBTType.STRING);
   }

   public static NBTType<?> getCommonTagType(List<? extends NBT> tags) {
      NBTType<?> type = NBTType.END;
      Iterator var2 = tags.iterator();

      while(var2.hasNext()) {
         NBT tag = (NBT)var2.next();
         if (type == NBTType.END) {
            type = tag.getType();
         } else if (type != tag.getType()) {
            return NBTType.COMPOUND;
         }
      }

      return type;
   }

   public NBTType<NBTList> getType() {
      return NBTType.LIST;
   }

   public NBTType<T> getTagsType() {
      return this.type;
   }

   public boolean isEmpty() {
      return this.tags.isEmpty();
   }

   public int size() {
      return this.tags.size();
   }

   public List<T> getTags() {
      return Collections.unmodifiableList(this.tags);
   }

   public T getTag(int index) {
      return (NBT)this.tags.get(index);
   }

   public void setTag(int index, T tag) {
      this.validateAddTag(tag);
      this.tags.set(index, tag);
   }

   public void addTag(int index, T tag) {
      this.validateAddTag(tag);
      this.tags.add(index, tag);
   }

   public void addTag(T tag) {
      this.validateAddTag(tag);
      this.tags.add(tag);
   }

   public void addTagUnsafe(int index, NBT nbt) {
      this.addTag(index, nbt);
   }

   public void addTagUnsafe(NBT nbt) {
      this.addTag(nbt);
   }

   public void removeTag(int index) {
      this.tags.remove(index);
   }

   protected void validateAddTag(T tag) {
      if (this.type != tag.getType()) {
         throw new IllegalArgumentException(MessageFormat.format("Invalid tag type. Expected {0}, got {1}.", this.type.getNBTClass(), tag.getClass()));
      }
   }

   public void addTagOrWrap(NBT tag) {
      if (this.type == tag.getType()) {
         this.tags.add(tag);
      } else {
         if (this.type != NBTType.COMPOUND) {
            throw new IllegalArgumentException("Can't add or wrap tag " + tag + " to list of type " + this.type);
         }

         NBTCompound wrapped = new NBTCompound();
         wrapped.setTag("", tag);
         this.tags.add(wrapped);
      }

   }

   private static NBT tryUnwrap(NBTCompound tag) {
      if (tag.tags.size() == 1) {
         NBT unwrapped = tag.getTagOrNull("");
         if (unwrapped != null) {
            return unwrapped;
         }
      }

      return tag;
   }

   public List<? extends NBT> unwrapTags() {
      if (this.type != NBTType.COMPOUND) {
         return new ArrayList(this.tags);
      } else {
         List<NBT> tags = new ArrayList(this.tags.size());
         Iterator var2 = this.tags.iterator();

         while(var2.hasNext()) {
            T tag = (NBT)var2.next();
            if (tag instanceof NBTCompound) {
               tags.add(tryUnwrap((NBTCompound)tag));
            } else {
               tags.add(tag);
            }
         }

         return tags;
      }
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         NBTList<T> other = (NBTList)obj;
         return Objects.equals(this.type, other.type) && Objects.equals(this.tags, other.tags);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.type, this.tags});
   }

   public NBTList<T> copy() {
      List<T> newTags = new ArrayList();
      Iterator var2 = this.tags.iterator();

      while(var2.hasNext()) {
         T tag = (NBT)var2.next();
         newTags.add(tag.copy());
      }

      return new NBTList(this.type, newTags);
   }

   public String toString() {
      return "List(" + this.tags + ")";
   }
}
