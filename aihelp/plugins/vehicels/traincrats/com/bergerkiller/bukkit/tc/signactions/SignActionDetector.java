package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.detector.DetectorRegion;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.offline.sign.OfflineSign;
import com.bergerkiller.bukkit.tc.offline.sign.OfflineSignMetadataHandler;
import com.bergerkiller.bukkit.tc.offline.sign.OfflineSignStore;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.detector.DetectorSign;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class SignActionDetector extends TrainCartsSignAction {
   public static final SignActionDetector INSTANCE = new SignActionDetector();

   public SignActionDetector() {
      super("detector");
   }

   public void enable(TrainCarts plugin) {
      plugin.getOfflineSigns().registerHandler(DetectorSign.Metadata.class, new OfflineSignMetadataHandler<DetectorSign.Metadata>() {
         public int getMetadataVersion() {
            return 1;
         }

         public void onUpdated(OfflineSignStore store, OfflineSign sign, DetectorSign.Metadata oldValue, DetectorSign.Metadata newValue) {
            if (oldValue.owner != newValue.owner) {
               this.onUnloaded(store, sign, oldValue);
               if (newValue.owner == null) {
                  this.onAdded(store, sign, newValue);
               }
            }

         }

         public void onAdded(OfflineSignStore store, OfflineSign sign, DetectorSign.Metadata metadata) {
            metadata.owner = new DetectorSign(store, sign, metadata);
            metadata.region.register(metadata.owner);
         }

         public void onUnloaded(OfflineSignStore store, OfflineSign sign, DetectorSign.Metadata metadata) {
            DetectorSign prevOwner = metadata.owner;
            if (prevOwner != null) {
               metadata.owner = null;
               metadata.region.unregister(prevOwner);
            }

         }

         public void onRemoved(OfflineSignStore store, OfflineSign sign, DetectorSign.Metadata metadata) {
            DetectorSign prevOwner = metadata.owner;
            if (prevOwner != null) {
               metadata.owner = null;
               metadata.region.unregister(prevOwner);
               if (!metadata.region.isRegistered()) {
                  metadata.region.remove();
               }

               DetectorSign.Metadata otherMeta = (DetectorSign.Metadata)store.get(metadata.otherSign, metadata.otherSignFront, DetectorSign.Metadata.class);
               if (otherMeta != null && metadata.region == otherMeta.region) {
                  store.remove(metadata.otherSign, metadata.otherSignFront, DetectorSign.Metadata.class);
               }
            }

         }

         public void onEncode(DataOutputStream stream, OfflineSign sign, DetectorSign.Metadata value) throws IOException {
            value.otherSign.getPosition().write(stream);
            stream.writeBoolean(value.otherSignFront);
            StreamUtil.writeUUID(stream, value.region.getUniqueId());
            stream.writeBoolean(value.isLeverDown);
         }

         public DetectorSign.Metadata onDecode(DataInputStream stream, OfflineSign sign) throws IOException {
            OfflineBlock otherSign = sign.getWorld().getBlockAt(IntVector3.read(stream));
            boolean otherSignFront = stream.readBoolean();
            DetectorRegion region = DetectorRegion.getRegion(StreamUtil.readUUID(stream));
            boolean isLeverDown = stream.readBoolean();
            if (region == null) {
               throw new OfflineSignMetadataHandler.InvalidMetadataException();
            } else {
               return new DetectorSign.Metadata(otherSign, otherSignFront, region, isLeverDown);
            }
         }

         public OfflineSignMetadataHandler.DataMigrationDecoder<DetectorSign.Metadata> getMigrationDecoder(OfflineSign gsign, int gdataVersion) {
            return gdataVersion == 0 ? (stream, sign, dataVersion) -> {
               OfflineBlock otherSign = sign.getWorld().getBlockAt(IntVector3.read(stream));
               DetectorRegion region = DetectorRegion.getRegion(StreamUtil.readUUID(stream));
               boolean isLeverDown = stream.readBoolean();
               if (region == null) {
                  throw new OfflineSignMetadataHandler.InvalidMetadataException();
               } else {
                  return new DetectorSign.Metadata(otherSign, true, region, isLeverDown);
               }
            } : OfflineSignMetadataHandler.super.getMigrationDecoder(gsign, gdataVersion);
         }
      });
   }

   public void disable(TrainCarts plugin) {
      plugin.getOfflineSigns().unregisterHandler(DetectorSign.Metadata.class);
   }

   public boolean canSupportFakeSign(SignActionEvent info) {
      return false;
   }

   public boolean matchLabel(SignActionEvent info, String label) {
      if (!this.match(info)) {
         return false;
      } else {
         String otherLabel = this.getLabel(info);
         return label == null ? otherLabel == null : label.equalsIgnoreCase(otherLabel);
      }
   }

   public String getLabel(SignActionEvent info) {
      String data = info.getLine(1);
      int index = Util.minStringIndex(data.indexOf(32), data.indexOf(58));
      return index == -1 ? null : data.substring(index + 1).trim();
   }

   public void execute(SignActionEvent info) {
      if ((info.getAction().isRedstone() || info.isAction(SignActionType.GROUP_ENTER)) && info.getTrainCarts().getOfflineSigns().get(info.getTrackedSign(), DetectorSign.Metadata.class) == null) {
         this.handlePlacement(info);
      }

   }

   public String getDescriptiveOutputName(SignActionEvent event) {
      return "Train activates detector";
   }

   public boolean build(SignChangeActionEvent event) {
      if (!SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_DETECTOR).setName("train detector").setDescription("detect trains between this detector sign and another").setTraincartsWIKIHelp("TrainCarts/Signs/Detector").handle(event)) {
         return false;
      } else if (!event.getTrackedSign().isRealSign()) {
         event.getPlayer().sendMessage(ChatColor.RED + "Detector signs must be placed using real signs");
         return false;
      } else if (!event.hasRails()) {
         event.getPlayer().sendMessage(ChatColor.RED + "No rails are nearby: This detector sign has not been activated!");
         return true;
      } else if (!this.handlePlacement(event)) {
         event.getPlayer().sendMessage(ChatColor.RED + "Failed to find a second detector sign: No region set.");
         event.getPlayer().sendMessage(ChatColor.YELLOW + "Place a second connected detector sign to finish this region!");
         return true;
      } else {
         event.getPlayer().sendMessage(ChatColor.GREEN + "A second detector sign was found: Region set.");
         return true;
      }
   }

   private boolean handlePlacement(SignActionEvent event) {
      if (event.hasRails() && event.getTrackedSign().isRealSign()) {
         RailLookup.TrackedRealSign startSign = (RailLookup.TrackedRealSign)event.getTrackedSign();
         Block startrails = event.getRails();
         BlockFace dir = event.getFacing();
         String label = this.getLabel(event);
         return this.tryBuild(event.getTrainCarts(), label, startrails, startSign, dir) || this.tryBuild(event.getTrainCarts(), label, startrails, startSign, FaceUtil.rotate(dir, 2)) || this.tryBuild(event.getTrainCarts(), label, startrails, startSign, FaceUtil.rotate(dir, -2));
      } else {
         return false;
      }
   }

   public boolean tryBuild(TrainCarts traincarts, String label, Block startrails, RailLookup.TrackedRealSign startSign, BlockFace direction) {
      TrackWalkingPoint walker = new TrackWalkingPoint(startrails, direction);
      HashSet coords = new HashSet();

      while(walker.moveFull() && walker.movedTotal <= (double)TCConfig.maxDetectorLength) {
         if (coords.add(walker.state.railPiece().blockPosition())) {
            RailLookup.TrackedSign[] var8 = walker.state.railPiece().signs();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               RailLookup.TrackedSign sign = var8[var10];
               if (sign.isRealSign() && !sign.isRemoved() && (!sign.signBlock.equals(startSign.signBlock) || ((RailLookup.TrackedRealSign)sign).isFrontText() != startSign.isFrontText())) {
                  SignActionEvent info = new SignActionEvent(sign);
                  if (this.matchLabel(info, label)) {
                     DetectorRegion region = DetectorRegion.create((World)walker.state.railWorld(), coords);
                     OfflineSignStore store = traincarts.getOfflineSigns();
                     store.put((RailLookup.TrackedSign)startSign, new DetectorSign.Metadata(sign, region, false));
                     store.put((RailLookup.TrackedSign)sign, new DetectorSign.Metadata(startSign, region, false));
                     CommonUtil.nextTick(() -> {
                        region.detectMinecarts();
                     });
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }
}
