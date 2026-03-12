package fr.xephi.authme.util.lazytags;

import java.util.function.Function;

public class DependentTag<A> implements Tag<A> {
   private final String name;
   private final Function<A, String> replacementFunction;

   public DependentTag(String name, Function<A, String> replacementFunction) {
      this.name = name;
      this.replacementFunction = replacementFunction;
   }

   public String getName() {
      return this.name;
   }

   public String getValue(A argument) {
      return (String)this.replacementFunction.apply(argument);
   }
}
