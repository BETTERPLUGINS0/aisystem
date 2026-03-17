package com.bergerkiller.bukkit.tc.controller.persistence;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;

public class EntityTagsPersistentCartAttribute implements PersistentCartAttribute<CommonMinecart<?>> {
   public void save(CommonMinecart<?> entity, ConfigurationNode data) {
      Set<String> tags = ((Minecart)entity.getEntity()).getScoreboardTags();
      if (!tags.isEmpty()) {
         data.set("entityTags", new ArrayList(tags));
      } else {
         data.remove("entityTags");
      }

   }

   public void load(CommonMinecart<?> commonEntity, ConfigurationNode data) {
      if (data.contains("entityTags")) {
         Entity entity = commonEntity.getEntity();
         Set<String> existingTags = entity.getScoreboardTags();
         List<String> tags = data.getList("entityTags", String.class);
         Iterator var6 = existingTags.iterator();

         String tag;
         while(var6.hasNext()) {
            tag = (String)var6.next();
            if (!tags.contains(tag)) {
               entity.removeScoreboardTag(tag);
            }
         }

         var6 = tags.iterator();

         while(var6.hasNext()) {
            tag = (String)var6.next();
            if (!existingTags.contains(tag)) {
               entity.addScoreboardTag(tag);
            }
         }
      }

   }
}
