package com.bergerkiller.bukkit.tc.attachments.ui.models.listing;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ListedRootLoader {
   protected ListedRoot root = new ListedRoot();

   protected void loadFromListing(ListedRoot listedRoot, String query) {
      boolean isNameSearch = StringUtil.firstIndexOf(query, new char[]{'/', '\\', ':'}) == -1;
      Iterator var13;
      if (!query.isEmpty() && isNameSearch) {
         Iterator var11 = listedRoot.namespaces().iterator();

         while(var11.hasNext()) {
            ListedNamespace namespace = (ListedNamespace)var11.next();
            var13 = namespace.matchChildrenNameContains(query).iterator();

            while(var13.hasNext()) {
               ListedEntry e = (ListedEntry)var13.next();
               e.assignToRoot(this.root);
            }
         }

      } else {
         List<String> parts = ListedEntry.tokenizePath(query);
         List namespacesToCheck;
         ListedNamespace match;
         if (!parts.isEmpty() && ((String)parts.get(0)).endsWith(":")) {
            String namespace = (String)parts.remove(0);
            match = (ListedNamespace)listedRoot.namespacesByName.get(namespace);
            if (match == null) {
               String namespaceLower = namespace.toLowerCase(Locale.ENGLISH);
               Iterator var9 = listedRoot.namespaces().iterator();

               while(var9.hasNext()) {
                  ListedNamespace n = (ListedNamespace)var9.next();
                  if (n.nameLowerCase().equals(namespaceLower)) {
                     match = n;
                     break;
                  }
               }
            }

            if (match == null) {
               return;
            }

            namespacesToCheck = Collections.singletonList(match);
         } else {
            namespacesToCheck = listedRoot.namespaces();
         }

         var13 = namespacesToCheck.iterator();

         while(var13.hasNext()) {
            match = (ListedNamespace)var13.next();
            Iterator var15 = match.matchWithPathPrefix(parts).iterator();

            while(var15.hasNext()) {
               ListedEntry e = (ListedEntry)var15.next();
               e.assignToRoot(this.root);
            }
         }

      }
   }
}
