package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ambiguity.AmbiguousInputHandler;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentRejectedException;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectByIdArgument;
import com.nisovin.shopkeepers.commands.lib.arguments.ObjectIdArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.util.UserArgumentUtils;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import java.util.Objects;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.Nullable;

public class UserByNameArgument extends ObjectByIdArgument<String, User> {
   public UserByNameArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public UserByNameArgument(String name, ArgumentFilter<? super User> filter) {
      this(name, filter, 0);
   }

   public UserByNameArgument(String name, ArgumentFilter<? super User> filter, int minimumCompletionInput) {
      super(name, filter, new ObjectByIdArgument.IdArgumentArgs(minimumCompletionInput));
   }

   protected ObjectIdArgument<String> createIdArgument(String name, ObjectByIdArgument.IdArgumentArgs args) {
      return new UserNameArgument(name, ArgumentFilter.acceptAny(), args.minimumCompletionInput) {
         protected Iterable<? extends String> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
            return UserByNameArgument.this.getCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix);
         }
      };
   }

   protected Text getInvalidArgumentErrorMsgText() {
      return Messages.commandPlayerArgumentInvalid;
   }

   protected AmbiguousInputHandler<User> getAmbiguousUserNameHandler(String argumentInput, Iterable<? extends User> matchedUsers) {
      AmbiguousUserNameHandler ambiguousUserNameHandler = new AmbiguousUserNameHandler(argumentInput, matchedUsers);
      if (ambiguousUserNameHandler.isInputAmbiguous()) {
         Text errorMsg = ambiguousUserNameHandler.getErrorMsg();

         assert errorMsg != null;

         errorMsg.setPlaceholderArguments(this.getDefaultErrorMsgArgs());
         errorMsg.setPlaceholderArguments("argument", argumentInput);
      }

      return ambiguousUserNameHandler;
   }

   @Nullable
   public final User getDefaultUserByName(String nameInput) throws ArgumentRejectedException {
      Stream<User> users = UserArgumentUtils.UserNameMatcher.EXACT.match(nameInput);
      Objects.requireNonNull(users);
      AmbiguousInputHandler<User> ambiguousUserNameHandler = this.getAmbiguousUserNameHandler(nameInput, users::iterator);
      if (ambiguousUserNameHandler.isInputAmbiguous()) {
         Text errorMsg = ambiguousUserNameHandler.getErrorMsg();

         assert errorMsg != null;

         throw new ArgumentRejectedException(this, errorMsg);
      } else {
         return (User)ambiguousUserNameHandler.getFirstMatch();
      }
   }

   @Nullable
   protected User getObject(CommandInput input, CommandContextView context, String nameInput) throws ArgumentParseException {
      return this.getDefaultUserByName(nameInput);
   }

   protected Iterable<? extends String> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      return UserNameArgument.getDefaultCompletionSuggestions(input, context, minimumCompletionInput, idPrefix, this.filter, true);
   }
}
