package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemTool {
   private List<ItemTool.Rule> rules;
   private float defaultMiningSpeed;
   private int damagePerBlock;
   private boolean canDestroyBlocksInCreative;

   public ItemTool(List<ItemTool.Rule> rules, float defaultMiningSpeed, int damagePerBlock) {
      this(rules, defaultMiningSpeed, damagePerBlock, true);
   }

   public ItemTool(List<ItemTool.Rule> rules, float defaultMiningSpeed, int damagePerBlock, boolean canDestroyBlocksInCreative) {
      this.rules = rules;
      this.defaultMiningSpeed = defaultMiningSpeed;
      this.damagePerBlock = damagePerBlock;
      this.canDestroyBlocksInCreative = canDestroyBlocksInCreative;
   }

   public static ItemTool read(PacketWrapper<?> wrapper) {
      List<ItemTool.Rule> rules = wrapper.readList(ItemTool.Rule::read);
      float defaultMiningSpeed = wrapper.readFloat();
      int damagePerBlock = wrapper.readVarInt();
      boolean canDestroyBlocksInCreative = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5) || wrapper.readBoolean();
      return new ItemTool(rules, defaultMiningSpeed, damagePerBlock, canDestroyBlocksInCreative);
   }

   public static void write(PacketWrapper<?> wrapper, ItemTool tool) {
      wrapper.writeList(tool.rules, ItemTool.Rule::write);
      wrapper.writeFloat(tool.defaultMiningSpeed);
      wrapper.writeVarInt(tool.damagePerBlock);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         wrapper.writeBoolean(tool.canDestroyBlocksInCreative);
      }

   }

   public void addRule(ItemTool.Rule rule) {
      this.rules.add(rule);
   }

   public List<ItemTool.Rule> getRules() {
      return this.rules;
   }

   public void setRules(List<ItemTool.Rule> rules) {
      this.rules = rules;
   }

   public float getDefaultMiningSpeed() {
      return this.defaultMiningSpeed;
   }

   public void setDefaultMiningSpeed(float defaultMiningSpeed) {
      this.defaultMiningSpeed = defaultMiningSpeed;
   }

   public int getDamagePerBlock() {
      return this.damagePerBlock;
   }

   public void setDamagePerBlock(int damagePerBlock) {
      this.damagePerBlock = damagePerBlock;
   }

   public boolean isCanDestroyBlocksInCreative() {
      return this.canDestroyBlocksInCreative;
   }

   public void setCanDestroyBlocksInCreative(boolean canDestroyBlocksInCreative) {
      this.canDestroyBlocksInCreative = canDestroyBlocksInCreative;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemTool)) {
         return false;
      } else {
         ItemTool itemTool = (ItemTool)obj;
         if (Float.compare(itemTool.defaultMiningSpeed, this.defaultMiningSpeed) != 0) {
            return false;
         } else if (this.damagePerBlock != itemTool.damagePerBlock) {
            return false;
         } else if (!this.rules.equals(itemTool.rules)) {
            return false;
         } else {
            return this.canDestroyBlocksInCreative == itemTool.canDestroyBlocksInCreative;
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.rules, this.defaultMiningSpeed, this.damagePerBlock, this.canDestroyBlocksInCreative});
   }

   public static class Rule {
      private MappedEntitySet<StateType.Mapped> blocks;
      @Nullable
      private Float speed;
      @Nullable
      private Boolean correctForDrops;

      public Rule(MappedEntitySet<StateType.Mapped> blocks, @Nullable Float speed, @Nullable Boolean correctForDrops) {
         this.blocks = blocks;
         this.speed = speed;
         this.correctForDrops = correctForDrops;
      }

      public static ItemTool.Rule read(PacketWrapper<?> wrapper) {
         MappedEntitySet<StateType.Mapped> blocks = MappedEntitySet.read(wrapper, StateTypes::getMappedById);
         Float speed = (Float)wrapper.readOptional(PacketWrapper::readFloat);
         Boolean correctForDrops = (Boolean)wrapper.readOptional(PacketWrapper::readBoolean);
         return new ItemTool.Rule(blocks, speed, correctForDrops);
      }

      public static void write(PacketWrapper<?> wrapper, ItemTool.Rule rule) {
         MappedEntitySet.write(wrapper, rule.blocks);
         wrapper.writeOptional(rule.speed, PacketWrapper::writeFloat);
         wrapper.writeOptional(rule.correctForDrops, PacketWrapper::writeBoolean);
      }

      public MappedEntitySet<StateType.Mapped> getBlocks() {
         return this.blocks;
      }

      public void setBlocks(MappedEntitySet<StateType.Mapped> blocks) {
         this.blocks = blocks;
      }

      @Nullable
      public Float getSpeed() {
         return this.speed;
      }

      public void setSpeed(@Nullable Float speed) {
         this.speed = speed;
      }

      @Nullable
      public Boolean getCorrectForDrops() {
         return this.correctForDrops;
      }

      public void setCorrectForDrops(@Nullable Boolean correctForDrops) {
         this.correctForDrops = correctForDrops;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof ItemTool.Rule)) {
            return false;
         } else {
            ItemTool.Rule rule = (ItemTool.Rule)obj;
            if (!this.blocks.equals(rule.blocks)) {
               return false;
            } else {
               return !Objects.equals(this.speed, rule.speed) ? false : Objects.equals(this.correctForDrops, rule.correctForDrops);
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.blocks, this.speed, this.correctForDrops});
      }
   }
}
