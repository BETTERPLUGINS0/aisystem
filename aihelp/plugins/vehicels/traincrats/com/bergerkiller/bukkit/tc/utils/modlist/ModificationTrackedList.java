package com.bergerkiller.bukkit.tc.utils.modlist;

import java.util.List;

public interface ModificationTrackedList<E> extends List<E> {
   int getModCount();
}
