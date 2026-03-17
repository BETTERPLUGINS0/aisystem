package com.bergerkiller.bukkit.tc.attachments.ui.item;

import com.bergerkiller.bukkit.common.map.MapBlendMode;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.util.Model;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Vector3;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.Util;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MapWidgetItemPreview extends MapWidget {
   private final Object renderLock = new Object();
   private Thread renderThread = null;
   private volatile boolean renderThreadStopping = false;
   private volatile MapWidgetItemPreview.RenderOptions lastRenderOptions = new MapWidgetItemPreview.RenderOptions();
   private volatile MapTexture lastRenderResult = null;

   public void setItem(ItemStack item) {
      if (item != null) {
         this.lastRenderOptions = new MapWidgetItemPreview.RenderOptions(this.lastRenderOptions, item);
      } else {
         this.lastRenderOptions = new MapWidgetItemPreview.RenderOptions();
      }

      synchronized(this.renderLock) {
         this.renderLock.notifyAll();
      }
   }

   public void onAttached() {
      super.onAttached();
      this.updateRenderRotation();
      this.renderThreadStopping = false;
      if (this.renderThread == null) {
         this.renderThread = new Thread(this::asyncRender);
         this.renderThread.setDaemon(true);
         this.renderThread.start();
      }

   }

   public void onDetached() {
      super.onDetached();
      this.renderThreadStopping = true;
      this.renderThread = null;
      synchronized(this.renderLock) {
         this.renderLock.notifyAll();
      }
   }

   private void asyncRender() {
      MapWidgetItemPreview.RenderOptions opt = null;

      while(!this.renderThreadStopping && this.getDisplay() != null) {
         synchronized(this.renderLock) {
            if (opt == this.lastRenderOptions) {
               try {
                  this.renderLock.wait(1000L);
               } catch (InterruptedException var5) {
               }
               continue;
            }

            opt = this.lastRenderOptions;
         }

         this.lastRenderResult = opt.render(this.view.getWidth(), this.view.getHeight());
         this.invalidate();
      }

   }

   public void onTick() {
      this.updateRenderRotation();
   }

   private void updateRenderRotation() {
      List<Player> viewers = this.display.getViewers();
      if (!viewers.isEmpty()) {
         Location loc = Util.getRealEyeLocation((Player)viewers.get(0));
         MapWidgetItemPreview.RenderOptions opt = new MapWidgetItemPreview.RenderOptions(this.lastRenderOptions, loc);
         if (MapWidgetItemPreview.RenderOptions.isDifferent(this.lastRenderOptions, opt)) {
            synchronized(this.renderLock) {
               this.lastRenderOptions = opt;
               this.renderLock.notifyAll();
            }
         }
      }

   }

   public void onDraw() {
      MapTexture result = this.lastRenderResult;
      if (result != null) {
         this.view.setBlendMode(MapBlendMode.NONE);
         this.view.draw(result, 0, 0);
      }

   }

   private static class RenderOptions {
      public final ItemStack item;
      public final Model model;
      public final float yaw;
      public final float pitch;

      public RenderOptions() {
         this.item = null;
         this.model = null;
         this.yaw = 0.0F;
         this.pitch = 0.0F;
      }

      public RenderOptions(MapWidgetItemPreview.RenderOptions orig, ItemStack item) {
         if (item == null) {
            throw new IllegalArgumentException("Null item");
         } else {
            this.item = item;
            this.model = TCConfig.resourcePack.getItemModel(item);
            this.yaw = orig.yaw;
            this.pitch = orig.pitch;
         }
      }

      public RenderOptions(MapWidgetItemPreview.RenderOptions orig, Location eyeLocation) {
         this.item = orig.item;
         this.model = orig.model;
         this.yaw = eyeLocation.getYaw();
         this.pitch = eyeLocation.getPitch() - 90.0F;
      }

      public MapTexture render(int width, int height) {
         MapTexture texture = MapTexture.createEmpty(width, height);
         if (this.model != null) {
            double scale = (double)width / 64.0D;
            Matrix4x4 transform = new Matrix4x4();
            transform.translate((double)width / 2.0D, 0.0D, (double)height / 2.0D);
            transform.scale(scale);
            transform.rotateX((double)this.pitch);
            transform.rotateY((double)this.yaw);
            transform.translate(-8.0D / scale, -8.0D / scale, -8.0D / scale);
            texture.setLightOptions(0.0F, 1.0F, new Vector3(-1.0D, 1.0D, -1.0D));
            texture.drawModel(this.model, transform);
         }

         return texture;
      }

      public static boolean isDifferent(MapWidgetItemPreview.RenderOptions opt1, MapWidgetItemPreview.RenderOptions opt2) {
         return opt1.model != opt2.model || MathUtil.getAngleDifference(opt1.yaw, opt2.yaw) > 2.0F || MathUtil.getAngleDifference(opt1.pitch, opt2.pitch) > 2.0F;
      }
   }
}
