package ac.grim.grimac.api.plugin;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Collection;

public interface GrimPluginDescription {
   String getVersion();

   String getDescription();

   @NotNull
   Collection<String> getAuthors();
}
