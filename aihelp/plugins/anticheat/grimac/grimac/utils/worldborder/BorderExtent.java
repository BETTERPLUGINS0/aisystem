package ac.grim.grimac.utils.worldborder;

public interface BorderExtent {
   double size();

   double getMinX(double var1, double var3);

   double getMaxX(double var1, double var3);

   double getMinZ(double var1, double var3);

   double getMaxZ(double var1, double var3);

   BorderExtent tick();

   BorderExtent update();
}
