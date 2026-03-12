package ac.grim.grimac.shaded.incendo.cloud.caption;

import java.util.Iterator;
import java.util.LinkedList;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;

@API(
   status = Status.INTERNAL
)
public final class CaptionRegistryImpl<C> implements CaptionRegistry<C> {
   private final LinkedList<CaptionProvider<C>> providers = new LinkedList();

   CaptionRegistryImpl() {
   }

   @NonNull
   public String caption(@NonNull final Caption caption, @NonNull final C sender) {
      Iterator var3 = this.providers.iterator();

      String result;
      do {
         if (!var3.hasNext()) {
            throw new IllegalArgumentException(String.format("There is no caption stored with key '%s'", caption));
         }

         CaptionProvider<C> provider = (CaptionProvider)var3.next();
         result = provider.provide(caption, sender);
      } while(result == null);

      return result;
   }

   @This
   @NonNull
   public CaptionRegistry<C> registerProvider(@NonNull final CaptionProvider<C> provider) {
      this.providers.addFirst(provider);
      return this;
   }
}
