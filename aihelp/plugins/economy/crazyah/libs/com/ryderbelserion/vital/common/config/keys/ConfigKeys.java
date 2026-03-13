package libs.com.ryderbelserion.vital.common.config.keys;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.PropertyInitializer;
import libs.com.ryderbelserion.vital.common.config.beans.Plugin;

public class ConfigKeys implements SettingsHolder {
   @Comment({"Configure the library bundled with the plugin."})
   public static final Property<Plugin> settings = PropertyInitializer.newBeanProperty(Plugin.class, "settings", (new Plugin()).populate());
}
