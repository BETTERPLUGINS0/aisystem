package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ScoreComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class ScoreTag {
   public static final String SCORE = "score";
   static final TagResolver RESOLVER = SerializableResolver.claimingComponent("score", ScoreTag::create, ScoreTag::emit);

   private ScoreTag() {
   }

   static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
      String name = args.popOr("A scoreboard member name is required").value();
      String objective = args.popOr("An objective name is required").value();
      return Tag.inserting((Component)Component.score(name, objective));
   }

   @Nullable
   static Emitable emit(final Component component) {
      if (!(component instanceof ScoreComponent)) {
         return null;
      } else {
         ScoreComponent score = (ScoreComponent)component;
         return (emit) -> {
            emit.tag("score").argument(score.name()).argument(score.objective());
         };
      }
   }
}
