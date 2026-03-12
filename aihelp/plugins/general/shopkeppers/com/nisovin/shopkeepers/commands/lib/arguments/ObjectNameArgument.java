package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;

public abstract class ObjectNameArgument extends ObjectIdArgument<String> {
   public static final int DEFAULT_MINIMUM_COMPLETION_INPUT = 0;

   public ObjectNameArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public ObjectNameArgument(String name, ArgumentFilter<? super String> filter) {
      this(name, false, filter, 0);
   }

   public ObjectNameArgument(String name, boolean joinRemainingArgs, ArgumentFilter<? super String> filter, int minimumCompletionInput) {
      super(name, new StringArgument(name + ":string", joinRemainingArgs), filter, minimumCompletionInput);
   }

   protected String toString(String id) {
      return id;
   }
}
