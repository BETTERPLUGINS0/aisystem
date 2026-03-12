package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class AbstractMappedEntity implements MappedEntity {
   @Nullable
   protected final TypesBuilderData data;

   protected AbstractMappedEntity(@Nullable TypesBuilderData data) {
      this.data = data;
   }

   @Nullable
   public TypesBuilderData getRegistryData() {
      return this.data;
   }

   public ResourceLocation getName() {
      if (this.data != null) {
         return this.data.getName();
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public int getId(ClientVersion version) {
      if (this.data != null) {
         return this.data.getId(version);
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public boolean isRegistered() {
      return this.data != null;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         if (this == obj) {
            return true;
         } else {
            AbstractMappedEntity that = (AbstractMappedEntity)obj;
            if (this.data != null && that.data != null) {
               return this.data.getName().equals(that.data.getName());
            } else {
               return this instanceof DeepComparableEntity ? ((DeepComparableEntity)this).deepEquals(obj) : false;
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.data != null) {
         return Objects.hash(new Object[]{this.getClass(), this.data.getName()});
      } else {
         return this instanceof DeepComparableEntity ? ((DeepComparableEntity)this).deepHashCode() : System.identityHashCode(this);
      }
   }

   public String toString() {
      return this.getClass().getSimpleName() + "[" + (this.data == null ? this.hashCode() : this.data.getName()) + ']';
   }
}
