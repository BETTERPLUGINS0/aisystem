package ac.grim.grimac.shaded.incendo.cloud.brigadier.argument;

import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;

public interface BrigadierMappingContributor {
   <C, S> void contribute(CommandManager<C> manager, CloudBrigadierManager<C, S> brigadierManager);
}
