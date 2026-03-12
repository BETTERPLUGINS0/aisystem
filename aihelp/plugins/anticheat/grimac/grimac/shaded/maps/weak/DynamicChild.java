package ac.grim.grimac.shaded.maps.weak;

import ac.grim.grimac.shaded.maps.LiteJoiner;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

interface DynamicChild extends Dynamic {
   String ARROW = "->";

   static DynamicChild from(Dynamic parent, Object key, Object val) {
      Objects.requireNonNull(parent);
      Objects.requireNonNull(key);
      Objects.requireNonNull(val);
      if (val instanceof Map) {
         return new DynamicMap.Child(parent, key, (Map)val);
      } else if (val instanceof List) {
         return new DynamicList.Child(parent, key, (List)val);
      } else {
         return (DynamicChild)(val instanceof Collection ? new DynamicCollection.Child(parent, key, (Collection)val) : new DynamicSomething.Child(parent, key, val));
      }
   }

   static DynamicChild key(Dynamic parent, Object key) {
      return from(parent, key, key);
   }

   Dynamic parent();

   default <T> T as(Class<T> type) {
      try {
         return type.cast(this.asObject());
      } catch (ClassCastException var5) {
         LinkedList<Object> ascendingKeyChain = DynamicChildLogic.using(this).getAscendingKeyChainWithRoot();
         Object thisKey = ascendingKeyChain.pollLast();
         ascendingKeyChain.add(String.format("*%s*", thisKey));
         throw new ClassCastException(String.format("'%s' miscast in path %s: %s. Avoid by checking `if (aDynamic.is(%s.class)) ...` or using `aDynamic.maybe().as(%<s.class)`", thisKey, LiteJoiner.on("->").join(ascendingKeyChain), var5.getMessage(), type.getSimpleName()));
      }
   }
}
