package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentRejectedException;
import com.nisovin.shopkeepers.commands.lib.arguments.TypedFirstOfArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;

public class UserArgument extends CommandArgument<User> {
   protected final ArgumentFilter<? super User> filter;
   private final UserByUUIDArgument userUUIDArgument;
   private final UserByNameArgument userNameArgument;
   private final TypedFirstOfArgument<User> firstOfArgument;

   public UserArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public UserArgument(String name, ArgumentFilter<? super User> filter) {
      this(name, filter, 0, 3);
   }

   public UserArgument(String name, ArgumentFilter<? super User> filter, int minimalNameCompletionInput, int minimumUUIDCompletionInput) {
      super(name);
      Validate.notNull(filter, (String)"filter is null");
      this.filter = filter;
      this.userUUIDArgument = new UserByUUIDArgument(name + ":uuid", filter, minimumUUIDCompletionInput) {
         protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
            return ((UserArgument)Unsafe.initialized(UserArgument.this)).getUUIDCompletionSuggestions(input, context, minimumCompletionInput, idPrefix);
         }
      };
      this.userNameArgument = new UserByNameArgument(name + ":name", filter, minimalNameCompletionInput) {
         @Nullable
         public User getObject(CommandInput input, CommandContextView context, String nameInput) throws ArgumentParseException {
            return ((UserArgument)Unsafe.initialized(UserArgument.this)).getUserByName(nameInput);
         }

         protected Iterable<? extends String> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
            return ((UserArgument)Unsafe.initialized(UserArgument.this)).getNameCompletionSuggestions(input, context, minimumCompletionInput, idPrefix);
         }
      };
      this.firstOfArgument = new TypedFirstOfArgument(name + ":firstOf", Arrays.asList(this.userUUIDArgument, this.userNameArgument), false, false);
      this.firstOfArgument.setParent(this);
   }

   public User parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      return (User)this.firstOfArgument.parseValue(input, context, argsReader);
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return this.firstOfArgument.complete(input, context, argsReader);
   }

   @Nullable
   public User getUserByName(String nameInput) throws ArgumentRejectedException {
      return this.userNameArgument.getDefaultUserByName(nameInput);
   }

   protected Iterable<? extends String> getNameCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      return UserNameArgument.getDefaultCompletionSuggestions(input, context, minimumCompletionInput, idPrefix, this.filter, true);
   }

   protected Iterable<? extends UUID> getUUIDCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      return UserUUIDArgument.getDefaultCompletionSuggestions(input, context, minimumCompletionInput, idPrefix, this.filter);
   }
}
