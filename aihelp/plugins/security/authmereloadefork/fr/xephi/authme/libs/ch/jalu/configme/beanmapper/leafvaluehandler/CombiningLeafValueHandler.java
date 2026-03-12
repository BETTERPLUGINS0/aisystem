package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.leafvaluehandler;

import fr.xephi.authme.libs.ch.jalu.configme.utils.TypeInformation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

public class CombiningLeafValueHandler implements LeafValueHandler {
   private final Collection<LeafValueHandler> handlers;

   public CombiningLeafValueHandler(LeafValueHandler... handlers) {
      this((Collection)Arrays.asList(handlers));
   }

   public CombiningLeafValueHandler(Collection<LeafValueHandler> handlers) {
      this.handlers = Collections.unmodifiableCollection(handlers);
   }

   public Object convert(TypeInformation typeInformation, Object value) {
      return this.getFirstNonNull((t) -> {
         return t.convert(typeInformation, value);
      });
   }

   public Object toExportValue(Object value) {
      return this.getFirstNonNull((t) -> {
         return t.toExportValue(value);
      });
   }

   protected final Collection<LeafValueHandler> getHandlers() {
      return this.handlers;
   }

   private Object getFirstNonNull(Function<LeafValueHandler, Object> callback) {
      return this.handlers.stream().map(callback).filter(Objects::nonNull).findFirst().orElse((Object)null);
   }
}
