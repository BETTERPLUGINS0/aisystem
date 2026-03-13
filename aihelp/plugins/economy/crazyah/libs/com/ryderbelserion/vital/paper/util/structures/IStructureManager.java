package libs.com.ryderbelserion.vital.paper.util.structures;

import java.io.File;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public interface IStructureManager {
   void applyStructure(@Nullable File var1);

   org.bukkit.structure.StructureManager getStructureManager();

   void saveStructure(@Nullable File var1, @Nullable Location var2, @Nullable Location var3, boolean var4);

   void pasteStructure(@Nullable Location var1, boolean var2);

   void removeStructure();

   Set<Location> getBlocks(@Nullable Location var1);

   double getStructureX();

   double getStructureY();

   double getStructureZ();

   Set<Location> getNearbyBlocks();

   List<Material> getBlockBlacklist();

   void createStructure();

   File getStructureFile();
}
