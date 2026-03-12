package org.apache.commons.lang3.builder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.Validate;

public class DiffResult<T> implements Iterable<Diff<?>> {
   public static final String OBJECTS_SAME_STRING = "";
   private static final String DIFFERS_STRING = "differs from";
   private final List<Diff<?>> diffList;
   private final T lhs;
   private final T rhs;
   private final ToStringStyle style;

   DiffResult(T var1, T var2, List<Diff<?>> var3, ToStringStyle var4) {
      Validate.notNull(var1, "lhs");
      Validate.notNull(var2, "rhs");
      Validate.notNull(var3, "diffList");
      this.diffList = var3;
      this.lhs = var1;
      this.rhs = var2;
      if (var4 == null) {
         this.style = ToStringStyle.DEFAULT_STYLE;
      } else {
         this.style = var4;
      }

   }

   public T getLeft() {
      return this.lhs;
   }

   public T getRight() {
      return this.rhs;
   }

   public List<Diff<?>> getDiffs() {
      return Collections.unmodifiableList(this.diffList);
   }

   public int getNumberOfDiffs() {
      return this.diffList.size();
   }

   public ToStringStyle getToStringStyle() {
      return this.style;
   }

   public String toString() {
      return this.toString(this.style);
   }

   public String toString(ToStringStyle var1) {
      if (this.diffList.isEmpty()) {
         return "";
      } else {
         ToStringBuilder var2 = new ToStringBuilder(this.lhs, var1);
         ToStringBuilder var3 = new ToStringBuilder(this.rhs, var1);
         Iterator var4 = this.diffList.iterator();

         while(var4.hasNext()) {
            Diff var5 = (Diff)var4.next();
            var2.append(var5.getFieldName(), var5.getLeft());
            var3.append(var5.getFieldName(), var5.getRight());
         }

         return String.format("%s %s %s", var2.build(), "differs from", var3.build());
      }
   }

   public Iterator<Diff<?>> iterator() {
      return this.diffList.iterator();
   }
}
