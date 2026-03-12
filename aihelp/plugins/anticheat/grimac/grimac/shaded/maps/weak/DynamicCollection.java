package ac.grim.grimac.shaded.maps.weak;

import ac.grim.grimac.shaded.maps.LiteJoiner;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

class DynamicCollection extends AbstractDynamic<Collection> implements Dynamic, Describer {
   static final String NO_KEY = "?";

   DynamicCollection(Collection inner) {
      super(inner);
   }

   protected Object keyLiteral() {
      return "root";
   }

   public Dynamic get(Object childKey) {
      return (Dynamic)(((Collection)this.inner).isEmpty() ? new ParentAbsence.Empty(this, childKey) : new ChildAbsence.Missing(this, childKey));
   }

   public Stream<Dynamic> children() {
      return ((Collection)this.inner).stream().map((val) -> {
         return (Dynamic)(val == null ? new ChildAbsence.Null(this, "?") : DynamicChild.from(this, "?", val));
      });
   }

   public String describe() {
      String type = this.inner instanceof Set ? "Set" : "Collection";
      return ((Collection)this.inner).isEmpty() ? "Empty-" + type : type + "[size:" + ((Collection)this.inner).size() + "]";
   }

   public String toString() {
      return this.keyLiteral() + ":" + this.describe();
   }

   static class Child extends DynamicCollection implements DynamicChild {
      private final Dynamic parent;
      private final Object key;

      Child(Dynamic parent, Object key, Collection inner) {
         super(inner);
         this.parent = parent;
         this.key = key;
      }

      public Dynamic parent() {
         return this.parent;
      }

      public Object keyLiteral() {
         return this.key;
      }

      public String toString() {
         return LiteJoiner.on("->").join(DynamicChildLogic.using(this).getAscendingKeyChainWithRoot()) + ":" + this.describe();
      }
   }
}
