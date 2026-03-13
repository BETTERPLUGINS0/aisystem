package com.volmit.iris.engine.object.annotations.functions;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.framework.ListFunction;
import com.volmit.iris.util.collection.KList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.loot.LootTables;

public class LootTableKeyFunction implements ListFunction<KList<String>> {
   public String key() {
      return "loot-table-key";
   }

   public String fancyName() {
      return "LootTable Key";
   }

   public KList<String> apply(IrisData data) {
      return (KList)StreamSupport.stream(Registry.LOOT_TABLES.spliterator(), false).map(LootTables::getLootTable).map(Keyed::getKey).map(NamespacedKey::toString).collect(Collectors.toCollection(KList::new));
   }
}
