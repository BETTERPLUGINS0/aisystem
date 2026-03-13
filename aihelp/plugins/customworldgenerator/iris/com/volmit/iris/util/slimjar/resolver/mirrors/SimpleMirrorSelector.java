package com.volmit.iris.util.slimjar.resolver.mirrors;

import com.volmit.iris.util.slimjar.logging.LocationAwareProcessLogger;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import com.volmit.iris.util.slimjar.resolver.data.Mirror;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SimpleMirrorSelector implements MirrorSelector {
   @NotNull
   private static final ProcessLogger LOGGER = LocationAwareProcessLogger.generic();

   @Contract(
      pure = true
   )
   @NotNull
   public Collection<Repository> select(@NotNull Collection<Repository> var1, @NotNull Collection<Mirror> var2) {
      if (var1.isEmpty()) {
         return var1;
      } else if (var2.isEmpty()) {
         return var1;
      } else {
         Map var3 = (Map)var2.stream().collect(Collectors.toMap((var0) -> {
            return var0.original().toString();
         }, (var0) -> {
            return var0.mirroring().toString();
         }, (var0, var1x) -> {
            LOGGER.error("Duplicate mirror found '{}' and '{}'", var0, var1x);
            return var0;
         }));
         return var1.stream().distinct().map((var1x) -> {
            String var2 = var1x.url().toString();

            String var4;
            for(LinkedHashSet var3x = new LinkedHashSet(); (var4 = (String)var3.get(var2)) != null; var2 = var4) {
               if (!var3x.add(var2)) {
                  LOGGER.error("Circular mirror detected for '{}'", var1x.url());
                  break;
               }
            }

            try {
               return new Repository(URI.create(var2).toURL());
            } catch (MalformedURLException var6) {
               LOGGER.error("Failed to parse mirror URL '{}'", var2);
               return var1x;
            }
         }).distinct().toList();
      }
   }
}
