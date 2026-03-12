package ac.grim.grimac.shaded.incendo.cloud.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
final class ChainedSuggestionProcessor<C> implements SuggestionProcessor<C> {
   private final List<SuggestionProcessor<C>> links;

   ChainedSuggestionProcessor(final List<SuggestionProcessor<C>> links) {
      List<SuggestionProcessor<C>> list = new ArrayList();
      flattenChain(list, links);
      this.links = Collections.unmodifiableList(list);
   }

   private static <C> void flattenChain(@NonNull final List<SuggestionProcessor<C>> into, @NonNull final Collection<SuggestionProcessor<C>> links) {
      Iterator var2 = links.iterator();

      while(var2.hasNext()) {
         SuggestionProcessor<C> link = (SuggestionProcessor)var2.next();
         if (link instanceof ChainedSuggestionProcessor) {
            flattenChain(into, ((ChainedSuggestionProcessor)link).links);
         } else {
            into.add(link);
         }
      }

   }

   @NonNull
   public Stream<Suggestion> process(@NonNull final CommandPreprocessingContext<C> context, @NonNull final Stream<Suggestion> suggestions) {
      Stream<Suggestion> currentLink = suggestions;

      SuggestionProcessor link;
      for(Iterator var4 = this.links.iterator(); var4.hasNext(); currentLink = link.process(context, currentLink)) {
         link = (SuggestionProcessor)var4.next();
      }

      return currentLink;
   }
}
