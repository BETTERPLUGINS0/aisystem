package ac.grim.grimac.shaded.maps.weak;

import ac.grim.grimac.shaded.maps.LiteJoiner;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class ChildAbsence<Parent extends Dynamic> extends AbstractAbsence<Parent> implements IssueDescribingChild {
   protected ChildAbsence(Parent parent, Object key) {
      super(parent, key);
   }

   protected abstract String describeIssue(LinkedList<Object> var1, LinkedList<Object> var2);

   public final String describeIssue(List<Object> childKeys) {
      LinkedList<Object> keyChainUntilSelf = DynamicChildLogic.using(this).getAscendingKeyChainWithRoot();
      keyChainUntilSelf.removeLast();
      LinkedList<Object> fullKeyChain = new LinkedList(keyChainUntilSelf);
      fullKeyChain.addLast("*" + this.key + "*");
      fullKeyChain.addAll(childKeys);
      return this.describeIssue(fullKeyChain, keyChainUntilSelf);
   }

   public Dynamic get(Object key) {
      return new DescriptionDeferringAbsence(this, key);
   }

   public Object asObject() {
      throw new NoSuchElementException(this.describeIssue(Collections.emptyList()));
   }

   public static class Missing<P extends Dynamic & Describer> extends ChildAbsence<P> {
      public Missing(P parent, Object key) {
         super(parent, key);
      }

      protected String describeIssue(LinkedList<Object> ascendingMarkedKeyChain, LinkedList<Object> ascendingKeyChainBeforeSelf) {
         return String.format("'%s' key is missing in path %s, from %s: %s", this.key, LiteJoiner.on("->").join(ascendingMarkedKeyChain), LiteJoiner.on("->").join(ascendingKeyChainBeforeSelf), ((Describer)this.parent).describe());
      }
   }

   public static class Null extends ChildAbsence<Dynamic> {
      public Null(Dynamic parent, Object key) {
         super(parent, key);
      }

      protected String describeIssue(LinkedList<Object> ascendingMarkedKeyChain, LinkedList<Object> ascendingKeyChainBeforeSelf) {
         return String.format("null '%s' premature end of path %s", this.key, LiteJoiner.on("->").join(ascendingMarkedKeyChain));
      }
   }
}
