package ch.jalu.configme.resource;

import ch.jalu.configme.configurationdata.ConfigurationData;
import org.jetbrains.annotations.NotNull;

public interface PropertyResource {
   @NotNull
   PropertyReader createReader();

   void exportProperties(@NotNull ConfigurationData var1);
}
