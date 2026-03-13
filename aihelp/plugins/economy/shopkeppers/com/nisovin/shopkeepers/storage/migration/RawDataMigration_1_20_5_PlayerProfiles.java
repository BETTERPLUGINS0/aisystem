package com.nisovin.shopkeepers.storage.migration;

import com.nisovin.shopkeepers.util.logging.Log;
import com.nisovin.shopkeepers.util.yaml.YamlUtils;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawDataMigration_1_20_5_PlayerProfiles implements RawDataMigration {
   private static final Pattern PATTERN = Pattern.compile("(?m)(^.*)(==: PlayerProfile$(?:[\r\n])*)(?:(\\1uniqueId: )(.*$)([\r\n])*)?(\\1name: )(.*$)([\r\n])*");
   private static final String UNIQUE_ID_STRING;

   public String getName() {
      return "MC 1.20.5 player profiles (head items)";
   }

   public String apply(String data) throws RawDataMigrationException {
      Matcher matcher = PATTERN.matcher(data);
      String migrated = matcher.replaceAll((matchResult) -> {
         String uniqueIdYaml = matchResult.group(4);
         String nameYaml = matchResult.group(7);

         assert nameYaml != null;

         String uniqueIdReplacement = "$4";
         String name = (String)YamlUtils.fromYaml(nameYaml);
         if (name != null && !isValidPlayerName(name)) {
            Log.warning("Removing invalid profile name '" + name + "' near position " + matchResult.start() + "!");
            nameYaml = "\"\"";
            if (uniqueIdYaml == null || uniqueIdYaml.isEmpty()) {
               Log.warning("Adding missing profile id near position " + matchResult.start() + "!");
               uniqueIdReplacement = "$1" + Matcher.quoteReplacement("uniqueId: " + UNIQUE_ID_STRING + "\n");
            }
         }

         return "$1$2$3" + uniqueIdReplacement + "$5$6" + Matcher.quoteReplacement(nameYaml) + "$8";
      });
      return migrated;
   }

   private static boolean isValidPlayerName(String name) {
      return name.length() > 16 ? false : name.chars().filter((c) -> {
         return c <= 32 || c >= 127;
      }).findAny().isEmpty();
   }

   static {
      UNIQUE_ID_STRING = UUID.nameUUIDFromBytes(StandardCharsets.UTF_8.encode("Shopkeepers_Migration_1_20_5_PlayerProfiles").array()).toString();
   }
}
