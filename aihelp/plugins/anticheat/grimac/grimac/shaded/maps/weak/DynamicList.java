package ac.grim.grimac.shaded.maps.weak;

import ac.grim.grimac.shaded.maps.LiteJoiner;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class DynamicList extends AbstractDynamic<List> implements Dynamic, Describer {
   public DynamicList(List inner) {
      super(inner);
   }

   public Dynamic get(Object key) {
      if (((List)this.inner).isEmpty()) {
         return new ParentAbsence.Empty(this, key);
      } else {
         Integer index = (Integer)Optional.ofNullable(key).flatMap((k) -> {
            return Converter.convert(k).maybe().intoInteger();
         }).orElse((Object)null);
         if (index == null) {
            return new ChildAbsence.Missing(this, key);
         } else if (index >= 0 && index < ((List)this.inner).size()) {
            Object val = ((List)this.inner).get(index);
            return (Dynamic)(val != null ? DynamicChild.from(this, index, val) : new ChildAbsence.Null(this, index));
         } else {
            return new ChildAbsence.Missing(this, index);
         }
      }
   }

   public Stream<Dynamic> children() {
      return IntStream.range(0, ((List)this.inner).size()).mapToObj(this::get);
   }

   public String describe() {
      String type = "List";
      switch(((List)this.inner).size()) {
      case 0:
         return "Empty-List";
      case 1:
         return "List[0]";
      case 2:
         return "List[0, 1]";
      default:
         return String.format("%s[0..%d]", "List", ((List)this.inner).size() - 1);
      }
   }

   protected Object keyLiteral() {
      return "root";
   }

   public String toString() {
      return this.keyLiteral() + ":" + this.describe();
   }

   static class Child extends DynamicList implements DynamicChild {
      private final Dynamic parent;
      private final Object key;

      Child(Dynamic parent, Object key, List inner) {
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
