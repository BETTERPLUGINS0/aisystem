package ac.grim.grimac.shaded.maps.weak;

import java.util.LinkedList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DynamicChildLogic {
   private final DynamicChild child;

   static DynamicChildLogic using(DynamicChild child) {
      return new DynamicChildLogic(child);
   }

   private DynamicChildLogic(DynamicChild child) {
      this.child = child;
   }

   public LinkedList<Object> getAscendingKeyChainWithRoot() {
      LinkedList<DynamicChild> ascending = this.getAscendingChainAllWith((dc) -> {
         return true;
      });
      return (LinkedList)Stream.concat(Stream.of(((DynamicChild)ascending.getFirst()).parent()), ascending.stream()).map((child) -> {
         return child.key().asObject();
      }).collect(Collectors.toCollection(LinkedList::new));
   }

   public LinkedList<DynamicChild> getAscendingChainAllWith(Predicate<DynamicChild> pd) {
      LinkedList<DynamicChild> chain = new LinkedList();
      if (!pd.test(this.child)) {
         return chain;
      } else {
         chain.add(this.child);

         for(Dynamic nextParent = this.child.parent(); nextParent instanceof DynamicChild && pd.test((DynamicChild)nextParent); nextParent = ((DynamicChild)nextParent).parent()) {
            chain.addFirst((DynamicChild)nextParent);
         }

         return chain;
      }
   }
}
