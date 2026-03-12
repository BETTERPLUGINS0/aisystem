package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.MultipleEntitySelector;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class MultipleEntitySelectorParser<C> extends SelectorUtils.EntitySelectorParser<C, MultipleEntitySelector> {
   private final boolean allowEmpty;

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, MultipleEntitySelector> multipleEntitySelectorParser() {
      return multipleEntitySelectorParser(true);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, MultipleEntitySelector> multipleEntitySelectorParser(final boolean allowEmpty) {
      return ParserDescriptor.of(new MultipleEntitySelectorParser(allowEmpty), (Class)MultipleEntitySelector.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, MultipleEntitySelector> multipleEntitySelectorComponent() {
      return CommandComponent.builder().parser(multipleEntitySelectorParser());
   }

   @API(
      status = Status.STABLE,
      since = "1.8.0"
   )
   public MultipleEntitySelectorParser(final boolean allowEmpty) {
      super(false);
      this.allowEmpty = allowEmpty;
   }

   public MultipleEntitySelectorParser() {
      this(true);
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public MultipleEntitySelector mapResult(@NonNull final String input, @NonNull final SelectorUtils.EntitySelectorWrapper wrapper) {
      final List<Entity> entities = wrapper.entities();
      if (entities.isEmpty() && !this.allowEmpty) {
         (new SelectorUtils.SelectorParser.Thrower(NO_ENTITIES_EXCEPTION_TYPE.get())).throwIt();
      }

      return new MultipleEntitySelector() {
         @NonNull
         public String inputString() {
            return input;
         }

         @NonNull
         public Collection<Entity> values() {
            return Collections.unmodifiableCollection(entities);
         }
      };
   }
}
