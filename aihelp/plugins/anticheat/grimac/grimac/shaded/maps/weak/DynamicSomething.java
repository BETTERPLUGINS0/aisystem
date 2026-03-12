package ac.grim.grimac.shaded.maps.weak;

import ac.grim.grimac.shaded.maps.LiteJoiner;
import java.util.stream.Stream;

class DynamicSomething extends AbstractDynamic<Object> implements Dynamic, Describer {
   public DynamicSomething(Object inner) {
      super(inner);
   }

   public Dynamic get(Object key) {
      return new ParentAbsence.Barren(this, key);
   }

   public String describe() {
      return this.inner.getClass().getSimpleName();
   }

   protected Object keyLiteral() {
      return "root";
   }

   public String toString() {
      return this.keyLiteral() + ":" + this.describe();
   }

   public Stream<Dynamic> children() {
      return Stream.empty();
   }

   static class Child extends DynamicSomething implements DynamicChild {
      private final Dynamic parent;
      private final Object key;

      Child(Dynamic parent, Object key, Object inner) {
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
