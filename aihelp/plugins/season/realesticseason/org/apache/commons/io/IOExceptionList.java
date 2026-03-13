package org.apache.commons.io;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class IOExceptionList extends IOException {
   private static final long serialVersionUID = 1L;
   private final List<? extends Throwable> causeList;

   public IOExceptionList(List<? extends Throwable> var1) {
      this(String.format("%,d exceptions: %s", var1 == null ? 0 : var1.size(), var1), var1);
   }

   public IOExceptionList(String var1, List<? extends Throwable> var2) {
      super(var1, var2 != null && !var2.isEmpty() ? (Throwable)var2.get(0) : null);
      this.causeList = var2 == null ? Collections.emptyList() : var2;
   }

   public <T extends Throwable> T getCause(int var1) {
      return (Throwable)this.causeList.get(var1);
   }

   public <T extends Throwable> T getCause(int var1, Class<T> var2) {
      return (Throwable)var2.cast(this.causeList.get(var1));
   }

   public <T extends Throwable> List<T> getCauseList() {
      return this.causeList;
   }

   public <T extends Throwable> List<T> getCauseList(Class<T> var1) {
      return this.causeList;
   }
}
