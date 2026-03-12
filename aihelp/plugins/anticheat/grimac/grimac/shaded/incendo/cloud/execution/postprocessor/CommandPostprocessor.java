package ac.grim.grimac.shaded.incendo.cloud.execution.postprocessor;

import ac.grim.grimac.shaded.incendo.cloud.services.type.ConsumerService;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(
   status = Status.STABLE
)
public interface CommandPostprocessor<C> extends ConsumerService<CommandPostprocessingContext<C>> {
}
