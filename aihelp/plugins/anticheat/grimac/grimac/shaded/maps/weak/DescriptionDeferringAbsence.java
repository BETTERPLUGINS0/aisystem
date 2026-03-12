package ac.grim.grimac.shaded.maps.weak;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

class DescriptionDeferringAbsence extends AbstractAbsence<DynamicChild> {
   DescriptionDeferringAbsence(DescriptionDeferringAbsence parent, Object key) {
      super(parent, key);
   }

   DescriptionDeferringAbsence(IssueDescribingChild parent, Object key) {
      super(parent, key);
   }

   public Dynamic get(Object key) {
      return new DescriptionDeferringAbsence(this, key);
   }

   public Object asObject() {
      DynamicChildLogic var10000 = DynamicChildLogic.using(this);
      DescriptionDeferringAbsence.class.getClass();
      LinkedList<DynamicChild> chainFromDescriber = var10000.getAscendingChainAllWith(DescriptionDeferringAbsence.class::isInstance);
      throw new NoSuchElementException(((IssueDescribingChild)((DynamicChild)chainFromDescriber.getFirst()).parent()).describeIssue((List)chainFromDescriber.stream().map((child) -> {
         return child.key().asObject();
      }).collect(Collectors.toList())));
   }
}
