package com.bergerkiller.bukkit.tc.utils.modlist;

import java.util.ArrayList;

public final class ModificationTrackedArrayList<E> extends ArrayList<E> implements ModificationTrackedList<E> {
   private static final long serialVersionUID = -6451538676293234885L;

   public int getModCount() {
      return this.modCount;
   }
}
