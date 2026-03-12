package fr.xephi.authme.util.lazytags;

import java.util.function.Supplier;

public class SimpleTag<A> implements Tag<A> {
   private final String name;
   private final Supplier<String> replacementFunction;

   public SimpleTag(String name, Supplier<String> replacementFunction) {
      this.name = name;
      this.replacementFunction = replacementFunction;
   }

   public String getName() {
      return this.name;
   }

   public String getValue(A argument) {
      return (String)this.replacementFunction.get();
   }
}
