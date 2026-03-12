package fr.xephi.authme.libs.org.postgresql.hostchooser;

import fr.xephi.authme.libs.org.postgresql.util.HostSpec;
import java.util.Properties;

public class HostChooserFactory {
   public static HostChooser createHostChooser(HostSpec[] hostSpecs, HostRequirement targetServerType, Properties info) {
      return (HostChooser)(hostSpecs.length == 1 ? new SingleHostChooser(hostSpecs[0], targetServerType) : new MultiHostChooser(hostSpecs, targetServerType, info));
   }
}
