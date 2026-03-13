package com.ryandw11.structure.api.structaddon;

import org.jetbrains.annotations.NotNull;

public interface StructureSectionProvider {
   @NotNull
   StructureSection createSection();
}
