package ac.grim.grimac.shaded.incendo.cloud.brigadier.permission;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import ac.grim.grimac.shaded.incendo.cloud.permission.Permission;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   since = "2.0.0"
)
public final class BrigadierPermissionPredicate<C, S> implements Predicate<S> {
   private final SenderMapper<S, C> senderMapper;
   private final BrigadierPermissionChecker<C> permissionChecker;
   private final CommandNode<?> node;

   public BrigadierPermissionPredicate(@NonNull final SenderMapper<S, C> senderMapper, @NonNull final BrigadierPermissionChecker<C> permissionChecker, @NonNull final CommandNode<?> node) {
      this.senderMapper = senderMapper;
      this.permissionChecker = permissionChecker;
      this.node = node;
   }

   public boolean test(@NonNull final S source) {
      C cloudSender = this.senderMapper.map(source);
      Map<Type, Permission> accessMap = (Map)this.node.nodeMeta().getOrDefault(CommandNode.META_KEY_ACCESS, Collections.emptyMap());
      Iterator var4 = accessMap.entrySet().iterator();

      Entry entry;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         entry = (Entry)var4.next();
      } while(!GenericTypeReflector.isSuperType((Type)entry.getKey(), cloudSender.getClass()) || !this.permissionChecker.hasPermission(cloudSender, (Permission)entry.getValue()));

      return true;
   }
}
