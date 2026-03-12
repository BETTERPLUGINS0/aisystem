package fr.xephi.authme.libs.ch.jalu.configme.resource;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;

public interface PropertyResource {
   PropertyReader createReader();

   void exportProperties(ConfigurationData var1);
}
