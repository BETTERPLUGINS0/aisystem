package ac.grim.grimac.shaded.maps.weak;

import ac.grim.grimac.shaded.maps.LiteJoiner;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class ParentAbsence<Parent extends Dynamic> extends AbstractAbsence<Parent> implements IssueDescribingChild {
   public ParentAbsence(Parent parent, Object key) {
      super(parent, key);
   }

   protected abstract String describeIssue(LinkedList<Object> var1, Object var2);

   public final String describeIssue(List<Object> childKeys) {
      LinkedList<Object> keyChain = DynamicChildLogic.using(this).getAscendingKeyChainWithRoot();
      keyChain.set(keyChain.size() - 2, "*" + keyChain.get(keyChain.size() - 2).toString() + "*");
      keyChain.addAll(childKeys);
      return this.describeIssue(keyChain, this.parent.key().asObject());
   }

   public Dynamic get(Object key) {
      return new DescriptionDeferringAbsence(this, key);
   }

   public Object asObject() {
      throw new NoSuchElementException(this.describeIssue(Collections.emptyList()));
   }

   public static class Barren<P extends Dynamic & Describer> extends ParentAbsence<P> {
      public Barren(P parent, Object key) {
         super(parent, key);
      }

      protected String describeIssue(LinkedList<Object> ascendingMarkedKeyChain, Object parentKey) {
         return String.format("%s '%s' premature end of path %s", ((Describer)this.parent).describe(), parentKey, LiteJoiner.on("->").join(ascendingMarkedKeyChain));
      }
   }

   public static class Empty<P extends Dynamic & Describer> extends ParentAbsence<P> {
      public Empty(P parent, Object key) {
         super(parent, key);
      }

      protected String describeIssue(LinkedList<Object> ascendingMarkedKeyChain, Object parentKey) {
         return String.format("%s '%s' premature end of path %s", ((Describer)this.parent).describe(), parentKey, LiteJoiner.on("->").join(ascendingMarkedKeyChain));
      }
   }
}
