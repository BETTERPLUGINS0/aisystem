package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.SingleEntitySelector;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class SingleEntitySelectorParser<C> extends SelectorUtils.EntitySelectorParser<C, SingleEntitySelector> {
   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, SingleEntitySelector> singleEntitySelectorParser() {
      return ParserDescriptor.of(new SingleEntitySelectorParser(), (Class)SingleEntitySelector.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, SingleEntitySelector> singleEntitySelectorComponent() {
      return CommandComponent.builder().parser(singleEntitySelectorParser());
   }

   public SingleEntitySelectorParser() {
      super(true);
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public SingleEntitySelector mapResult(@NonNull final String input, @NonNull final SelectorUtils.EntitySelectorWrapper wrapper) {
      final Entity entity = wrapper.singleEntity();
      return new SingleEntitySelector() {
         @NonNull
         public Entity single() {
            return entity;
         }

         @NonNull
         public String inputString() {
            return input;
         }
      };
   }
}
