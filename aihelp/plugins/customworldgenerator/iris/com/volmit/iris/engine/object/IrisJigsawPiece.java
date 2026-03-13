package com.volmit.iris.engine.object;

import com.google.gson.Gson;
import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;
import java.io.IOException;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.util.BlockVector;

@Desc("Represents a structure tile")
public class IrisJigsawPiece extends IrisRegistrant {
   @RegistryListResource(IrisObject.class)
   @Required
   @Desc("The object this piece represents")
   private String object = "";
   @ArrayType(
      type = IrisJigsawPieceConnector.class
   )
   @Desc("The connectors this object contains")
   private KList<IrisJigsawPieceConnector> connectors = new KList();
   @Desc("Configure everything about the object placement. Please don't define this unless you actually need it as using this option will slow down the jigsaw deign stage. Use this where you need it, just avoid using it everywhere to keep things fast.")
   private IrisObjectPlacement placementOptions;
   private transient AtomicCache<Integer> max2dDim;
   private transient AtomicCache<Integer> max3dDim;

   public int getMax2dDimension() {
      return (Integer)this.max2dDim.aquire(() -> {
         try {
            BlockVector var1 = IrisObject.sampleSize(this.getLoader().getObjectLoader().findFile(this.getObject()));
            return Math.max(var1.getBlockX(), var1.getBlockZ());
         } catch (IOException var2) {
            Iris.reportError(var2);
            var2.printStackTrace();
            return 0;
         }
      });
   }

   public int getMax3dDimension() {
      return (Integer)this.max3dDim.aquire(() -> {
         try {
            BlockVector var1 = IrisObject.sampleSize(this.getLoader().getObjectLoader().findFile(this.getObject()));
            return Math.max(Math.max(var1.getBlockX(), var1.getBlockZ()), var1.getBlockY());
         } catch (IOException var2) {
            Iris.reportError(var2);
            var2.printStackTrace();
            return -1;
         }
      });
   }

   public IrisJigsawPieceConnector getConnector(IrisPosition relativePosition) {
      Iterator var2 = this.connectors.iterator();

      IrisJigsawPieceConnector var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (IrisJigsawPieceConnector)var2.next();
      } while(!var3.getPosition().equals(var1));

      return var3;
   }

   public IrisJigsawPiece copy() {
      Gson var1 = this.getLoader().getGson();
      IrisJigsawPiece var2 = (IrisJigsawPiece)var1.fromJson(var1.toJson(this), IrisJigsawPiece.class);
      var2.setLoader(this.getLoader());
      var2.setLoadKey(this.getLoadKey());
      var2.setLoadFile(this.getLoadFile());
      return var2;
   }

   public boolean isTerminal() {
      return this.connectors.size() == 1;
   }

   public ObjectPlaceMode getPlaceMode() {
      return this.getPlacementOptions().getMode();
   }

   public String getFolderName() {
      return "jigsaw-pieces";
   }

   public String getTypeName() {
      return "Jigsaw Piece";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisJigsawPiece() {
      this.placementOptions = (new IrisObjectPlacement()).setMode(ObjectPlaceMode.FAST_MAX_HEIGHT);
      this.max2dDim = new AtomicCache();
      this.max3dDim = new AtomicCache();
   }

   @Generated
   public IrisJigsawPiece(final String object, final KList<IrisJigsawPieceConnector> connectors, final IrisObjectPlacement placementOptions, final AtomicCache<Integer> max2dDim, final AtomicCache<Integer> max3dDim) {
      this.placementOptions = (new IrisObjectPlacement()).setMode(ObjectPlaceMode.FAST_MAX_HEIGHT);
      this.max2dDim = new AtomicCache();
      this.max3dDim = new AtomicCache();
      this.object = var1;
      this.connectors = var2;
      this.placementOptions = var3;
      this.max2dDim = var4;
      this.max3dDim = var5;
   }

   @Generated
   public String getObject() {
      return this.object;
   }

   @Generated
   public KList<IrisJigsawPieceConnector> getConnectors() {
      return this.connectors;
   }

   @Generated
   public IrisObjectPlacement getPlacementOptions() {
      return this.placementOptions;
   }

   @Generated
   public AtomicCache<Integer> getMax2dDim() {
      return this.max2dDim;
   }

   @Generated
   public AtomicCache<Integer> getMax3dDim() {
      return this.max3dDim;
   }

   @Generated
   public IrisJigsawPiece setObject(final String object) {
      this.object = var1;
      return this;
   }

   @Generated
   public IrisJigsawPiece setConnectors(final KList<IrisJigsawPieceConnector> connectors) {
      this.connectors = var1;
      return this;
   }

   @Generated
   public IrisJigsawPiece setPlacementOptions(final IrisObjectPlacement placementOptions) {
      this.placementOptions = var1;
      return this;
   }

   @Generated
   public IrisJigsawPiece setMax2dDim(final AtomicCache<Integer> max2dDim) {
      this.max2dDim = var1;
      return this;
   }

   @Generated
   public IrisJigsawPiece setMax3dDim(final AtomicCache<Integer> max3dDim) {
      this.max3dDim = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = this.getObject();
      return "IrisJigsawPiece(object=" + var10000 + ", connectors=" + String.valueOf(this.getConnectors()) + ", placementOptions=" + String.valueOf(this.getPlacementOptions()) + ", max2dDim=" + String.valueOf(this.getMax2dDim()) + ", max3dDim=" + String.valueOf(this.getMax3dDim()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisJigsawPiece)) {
         return false;
      } else {
         IrisJigsawPiece var2 = (IrisJigsawPiece)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label47: {
               String var3 = this.getObject();
               String var4 = var2.getObject();
               if (var3 == null) {
                  if (var4 == null) {
                     break label47;
                  }
               } else if (var3.equals(var4)) {
                  break label47;
               }

               return false;
            }

            KList var5 = this.getConnectors();
            KList var6 = var2.getConnectors();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisObjectPlacement var7 = this.getPlacementOptions();
            IrisObjectPlacement var8 = var2.getPlacementOptions();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisJigsawPiece;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      String var3 = this.getObject();
      int var6 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getConnectors();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisObjectPlacement var5 = this.getPlacementOptions();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }
}
