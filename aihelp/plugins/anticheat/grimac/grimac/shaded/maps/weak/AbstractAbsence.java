package ac.grim.grimac.shaded.maps.weak;

import ac.grim.grimac.shaded.maps.LiteJoiner;
import java.util.Objects;
import java.util.stream.Stream;

abstract class AbstractAbsence<Parent extends Dynamic> implements DynamicChild {
   protected final Parent parent;
   protected final Object key;

   AbstractAbsence(Parent parent, Object key) {
      this.parent = parent;
      this.key = key;
   }

   public Parent parent() {
      return this.parent;
   }

   public Dynamic key() {
      return DynamicChild.key(this, this.key);
   }

   public boolean isPresent() {
      return false;
   }

   public Stream<Dynamic> children() {
      return Stream.empty();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         AbstractAbsence other = (AbstractAbsence)o;
         return Objects.equals(this.parent, other.parent) && Objects.equals(this.key, other.key);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.parent, this.key});
   }

   public String toString() {
      return LiteJoiner.on("->").join(DynamicChildLogic.using(this).getAscendingKeyChainWithRoot()) + ":absent";
   }
}
