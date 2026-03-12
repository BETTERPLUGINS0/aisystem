package emanondev.itemedit.aliases;

import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;

public class AttributeAliasesNew extends RegistryAliasSet<Attribute> implements AttributeAliases {
   public AttributeAliasesNew() {
      super("attribute", Registry.ATTRIBUTE);
   }

   public String getName(Attribute type) {
      String name = super.getName((Keyed)type);
      if (name.startsWith("generic_")) {
         name = name.substring("generic_".length());
      }

      return name;
   }
}
