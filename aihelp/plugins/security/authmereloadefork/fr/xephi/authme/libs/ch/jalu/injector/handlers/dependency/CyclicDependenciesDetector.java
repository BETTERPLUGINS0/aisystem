package fr.xephi.authme.libs.ch.jalu.injector.handlers.dependency;

import fr.xephi.authme.libs.ch.jalu.injector.context.ObjectIdentifier;
import fr.xephi.authme.libs.ch.jalu.injector.context.ResolutionContext;
import fr.xephi.authme.libs.ch.jalu.injector.exceptions.InjectorException;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.Handler;
import fr.xephi.authme.libs.ch.jalu.injector.handlers.instantiation.Resolution;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class CyclicDependenciesDetector implements Handler {
   public Resolution<?> resolve(ResolutionContext context) {
      ObjectIdentifier duplicateIdentifier = findRepeatedIdentifier(context);
      if (duplicateIdentifier != null) {
         String traversalList = buildParentsList(context);
         throw new InjectorException("Found cyclic dependency' - already traversed '" + duplicateIdentifier + "' (full traversal list: " + traversalList + " -> " + context.getIdentifier() + ")");
      } else {
         return null;
      }
   }

   @Nullable
   private static ObjectIdentifier findRepeatedIdentifier(ResolutionContext context) {
      Set<Type> types = new HashSet();
      types.add(context.getIdentifier().getType());
      Iterator var2 = context.getParents().iterator();

      ResolutionContext parent;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         parent = (ResolutionContext)var2.next();
      } while(types.add(parent.getIdentifier().getType()));

      return parent.getIdentifier();
   }

   private static String buildParentsList(ResolutionContext context) {
      return (String)context.getParents().stream().map((ctx) -> {
         return ctx.getIdentifier().getType().getTypeName();
      }).collect(Collectors.joining(" -> "));
   }
}
