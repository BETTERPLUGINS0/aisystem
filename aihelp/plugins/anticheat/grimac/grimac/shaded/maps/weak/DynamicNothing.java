package ac.grim.grimac.shaded.maps.weak;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

enum DynamicNothing implements Dynamic, Describer {
   INSTANCE;

   public Dynamic get(Object key) {
      return new ParentAbsence.Barren(this, key);
   }

   public boolean isPresent() {
      return false;
   }

   public Object asObject() {
      throw new NoSuchElementException("null 'root' premature end of path *root*");
   }

   public Stream<Dynamic> children() {
      return Stream.empty();
   }

   public Dynamic key() {
      return DynamicChild.key(this, "root");
   }

   public String describe() {
      return "null";
   }

   public String toString() {
      return "root:" + this.describe();
   }
}
