package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.MerchantOffer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WrapperPlayServerMerchantOffers extends PacketWrapper<WrapperPlayServerMerchantOffers> {
   private int containerId;
   private List<MerchantOffer> merchantOffers;
   private int villagerLevel;
   private int villagerXp;
   private boolean showProgress;
   private boolean canRestock;

   public WrapperPlayServerMerchantOffers(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerMerchantOffers(int containerId, List<MerchantOffer> merchantOffers, int villagerLevel, int villagerXp, boolean showProgress, boolean canRestock) {
      super((PacketTypeCommon)PacketType.Play.Server.MERCHANT_OFFERS);
      this.containerId = containerId;
      this.merchantOffers = merchantOffers;
      this.villagerLevel = villagerLevel;
      this.villagerXp = villagerXp;
      this.showProgress = showProgress;
      this.canRestock = canRestock;
   }

   public void read() {
      this.containerId = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2) ? this.readContainerId() : this.readVarInt();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         this.merchantOffers = this.readList(PacketWrapper::readMerchantOffer);
      } else {
         int size = this.readByte() & 255;
         this.merchantOffers = new ArrayList(size);

         for(int i = 0; i < size; ++i) {
            this.merchantOffers.add(this.readMerchantOffer());
         }
      }

      this.villagerLevel = this.readVarInt();
      this.villagerXp = this.readVarInt();
      this.showProgress = this.readBoolean();
      this.canRestock = this.readBoolean();
   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.writeContainerId(this.containerId);
      } else {
         this.writeVarInt(this.containerId);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         this.writeList(this.merchantOffers, PacketWrapper::writeMerchantOffer);
      } else {
         this.writeByte(this.merchantOffers.size() & 255);
         Iterator var1 = this.merchantOffers.iterator();

         while(var1.hasNext()) {
            MerchantOffer data = (MerchantOffer)var1.next();
            this.writeMerchantOffer(data);
         }
      }

      this.writeVarInt(this.villagerLevel);
      this.writeVarInt(this.villagerXp);
      this.writeBoolean(this.showProgress);
      this.writeBoolean(this.canRestock);
   }

   public void copy(WrapperPlayServerMerchantOffers wrapper) {
      this.containerId = wrapper.containerId;
      this.merchantOffers = wrapper.merchantOffers;
      this.villagerLevel = wrapper.villagerLevel;
      this.villagerXp = wrapper.villagerXp;
      this.showProgress = wrapper.showProgress;
      this.canRestock = wrapper.canRestock;
   }

   public int getContainerId() {
      return this.containerId;
   }

   public void setContainerId(int containerId) {
      this.containerId = containerId;
   }

   public List<MerchantOffer> getMerchantOffers() {
      return this.merchantOffers;
   }

   public void setMerchantOffers(List<MerchantOffer> merchantOffers) {
      this.merchantOffers = merchantOffers;
   }

   public int getVillagerLevel() {
      return this.villagerLevel;
   }

   public void setVillagerLevel(int villagerLevel) {
      this.villagerLevel = villagerLevel;
   }

   public int getVillagerXp() {
      return this.villagerXp;
   }

   public void setVillagerXp(int villagerXp) {
      this.villagerXp = villagerXp;
   }

   public boolean isShowProgress() {
      return this.showProgress;
   }

   public void setShowProgress(boolean showProgress) {
      this.showProgress = showProgress;
   }

   public boolean isCanRestock() {
      return this.canRestock;
   }

   public void setCanRestock(boolean canRestock) {
      this.canRestock = canRestock;
   }
}
