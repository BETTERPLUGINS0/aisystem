package ac.grim.grimac.shaded.maps.weak;

import ac.grim.grimac.shaded.maps.LiteJoiner;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

class DynamicMap extends AbstractDynamic<Map<?, ?>> implements Dynamic, Describer {
   public DynamicMap(Map<?, ?> inner) {
      super(inner);
   }

   public Dynamic get(Object childKey) {
      if (((Map)this.inner).isEmpty()) {
         return new ParentAbsence.Empty(this, childKey);
      } else if (((Map)this.inner).containsKey(childKey)) {
         Object val = ((Map)this.inner).get(childKey);
         return (Dynamic)(val != null ? DynamicChild.from(this, childKey, val) : new ChildAbsence.Null(this, childKey));
      } else {
         if (childKey instanceof String) {
            Iterator var2 = ((Map)this.inner).entrySet().iterator();

            while(var2.hasNext()) {
               Entry<?, ?> entry = (Entry)var2.next();
               if (childKey.equals(entry.getKey().toString())) {
                  return (Dynamic)(entry.getValue() != null ? DynamicChild.from(this, childKey, entry.getValue()) : new ChildAbsence.Null(this, childKey));
               }
            }
         }

         String keyString = childKey.toString();
         if (((Map)this.inner).containsKey(keyString)) {
            Object val = ((Map)this.inner).get(keyString);
            return (Dynamic)(val != null ? DynamicChild.from(this, keyString, val) : new ChildAbsence.Null(this, keyString));
         } else {
            return new ChildAbsence.Missing(this, childKey);
         }
      }
   }

   public Stream<Dynamic> children() {
      return ((Map)this.inner).keySet().stream().map(this::get);
   }

   public String describe() {
      return ((Map)this.inner).isEmpty() ? "Empty-Map" : "Map" + ((Map)this.inner).keySet().toString();
   }

   public Object keyLiteral() {
      return "root";
   }

   public String toString() {
      return this.keyLiteral() + ":" + this.describe();
   }

   static class Child extends DynamicMap implements DynamicChild {
      private final Dynamic parent;
      private final Object key;

      Child(Dynamic parent, Object key, Map inner) {
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
