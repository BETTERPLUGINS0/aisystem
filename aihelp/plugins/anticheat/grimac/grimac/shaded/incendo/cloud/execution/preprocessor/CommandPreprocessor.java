package ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor;

import ac.grim.grimac.shaded.incendo.cloud.services.type.ConsumerService;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(
   status = Status.STABLE
)
public interface CommandPreprocessor<C> extends ConsumerService<CommandPreprocessingContext<C>> {
}
