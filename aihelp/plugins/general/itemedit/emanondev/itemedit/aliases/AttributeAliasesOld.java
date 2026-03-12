package emanondev.itemedit.aliases;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import org.bukkit.attribute.Attribute;

public class AttributeAliasesOld extends AliasSet<Attribute> implements AttributeAliases {
   public AttributeAliasesOld() {
      super("attribute");
   }

   public Collection<Attribute> getValues() {
      return Arrays.asList((Attribute[])Attribute.class.getEnumConstants());
   }

   public String getName(Attribute type) {
      String name = ((Enum)type).name().toLowerCase(Locale.ENGLISH);
      if (name.startsWith("generic_")) {
         name = name.substring("generic_".length());
      }

      return name;
   }
}
