package ac.grim.grimac.shaded.incendo.cloud.injection;

import ac.grim.grimac.shaded.incendo.cloud.services.type.Service;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@FunctionalInterface
@API(
   status = Status.STABLE
)
public interface InjectionService<C> extends Service<InjectionRequest<C>, Object> {
}
