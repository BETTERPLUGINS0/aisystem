package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WrapperPlayServerUpdateAttributes extends PacketWrapper<WrapperPlayServerUpdateAttributes> {
   private static final List<Entry<String, Attribute>> PRE_1_16_ATTRIBUTES;
   private static final Map<String, Attribute> PRE_1_16_ATTRIBUTES_MAP;
   private static final Map<Attribute, String> PRE_1_16_ATTRIBUTES_RMAP;
   private int entityID;
   private List<WrapperPlayServerUpdateAttributes.Property> properties;

   public WrapperPlayServerUpdateAttributes(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerUpdateAttributes(int entityID, List<WrapperPlayServerUpdateAttributes.Property> properties) {
      super((PacketTypeCommon)PacketType.Play.Server.UPDATE_ATTRIBUTES);
      this.entityID = entityID;
      this.properties = properties;
   }

   public void read() {
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.entityID = this.readInt();
      } else {
         this.entityID = this.readVarInt();
      }

      this.readProperties();
   }

   protected void readProperties() {
      int propertyCount;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
         propertyCount = this.readVarInt();
      } else {
         propertyCount = this.readInt();
      }

      this.properties = new ArrayList(propertyCount);

      for(int i = 0; i < propertyCount; ++i) {
         Attribute attribute;
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            attribute = (Attribute)this.readMappedEntity(Attributes::getById);
         } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            attribute = Attributes.getByName(this.readIdentifier().toString());
         } else {
            String attributeName = this.readString(64);
            attribute = (Attribute)PRE_1_16_ATTRIBUTES_MAP.get(attributeName);
            if (attribute == null) {
               attribute = Attributes.getByName(attributeName);
            }

            if (attribute == null) {
               throw new IllegalStateException("Can't find attribute for name " + attributeName + " (version: " + this.serverVersion.name() + ")");
            }
         }

         double value = this.readDouble();
         int modifiersLength;
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            modifiersLength = this.readShort();
         } else {
            modifiersLength = this.readVarInt();
         }

         List<WrapperPlayServerUpdateAttributes.PropertyModifier> modifiers = new ArrayList(modifiersLength);

         for(int j = 0; j < modifiersLength; ++j) {
            ResourceLocation name;
            UUID uuid;
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
               name = this.readIdentifier();
               uuid = WrapperPlayServerUpdateAttributes.PropertyModifier.generateSemiUniqueId(name);
            } else {
               uuid = this.readUUID();
               name = new ResourceLocation(uuid.toString());
            }

            double amount = this.readDouble();
            byte operationIndex = this.readByte();
            WrapperPlayServerUpdateAttributes.PropertyModifier.Operation operation = WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.VALUES[operationIndex];
            modifiers.add(new WrapperPlayServerUpdateAttributes.PropertyModifier(name, uuid, amount, operation));
         }

         this.properties.add(new WrapperPlayServerUpdateAttributes.Property(attribute, value, modifiers));
      }

   }

   public void write() {
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.writeInt(this.entityID);
      } else {
         this.writeVarInt(this.entityID);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
         this.writeVarInt(this.properties.size());
      } else {
         this.writeInt(this.properties.size());
      }

      Iterator var1 = this.properties.iterator();

      while(var1.hasNext()) {
         WrapperPlayServerUpdateAttributes.Property property = (WrapperPlayServerUpdateAttributes.Property)var1.next();
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            this.writeVarInt(property.getAttribute().getId(this.serverVersion.toClientVersion()));
         } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            this.writeIdentifier(property.getAttribute().getName(this.serverVersion.toClientVersion()));
         } else {
            String str = (String)PRE_1_16_ATTRIBUTES_RMAP.get(property.getAttribute());
            this.writeString(str != null ? str : property.getAttribute().getName().toString());
         }

         this.writeDouble(property.value);
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            this.writeShort(property.modifiers.size());
         } else {
            this.writeVarInt(property.modifiers.size());
         }

         Iterator var5 = property.modifiers.iterator();

         while(var5.hasNext()) {
            WrapperPlayServerUpdateAttributes.PropertyModifier modifier = (WrapperPlayServerUpdateAttributes.PropertyModifier)var5.next();
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
               this.writeIdentifier(modifier.name);
            } else {
               this.writeUUID(modifier.uuid);
            }

            this.writeDouble(modifier.amount);
            this.writeByte(modifier.operation.ordinal());
         }
      }

   }

   public void copy(WrapperPlayServerUpdateAttributes wrapper) {
      this.entityID = wrapper.entityID;
      this.properties = wrapper.properties;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public List<WrapperPlayServerUpdateAttributes.Property> getProperties() {
      return this.properties;
   }

   public void setProperties(List<WrapperPlayServerUpdateAttributes.Property> properties) {
      this.properties = properties;
   }

   static {
      PRE_1_16_ATTRIBUTES = Collections.unmodifiableList(Arrays.asList(new SimpleEntry("generic.maxHealth", Attributes.MAX_HEALTH), new SimpleEntry("Max Health", Attributes.MAX_HEALTH), new SimpleEntry("zombie.spawnReinforcements", Attributes.SPAWN_REINFORCEMENTS), new SimpleEntry("Spawn Reinforcements Chance", Attributes.SPAWN_REINFORCEMENTS), new SimpleEntry("horse.jumpStrength", Attributes.HORSE_JUMP_STRENGTH), new SimpleEntry("Jump Strength", Attributes.HORSE_JUMP_STRENGTH), new SimpleEntry("generic.followRange", Attributes.FOLLOW_RANGE), new SimpleEntry("Follow Range", Attributes.FOLLOW_RANGE), new SimpleEntry("generic.knockbackResistance", Attributes.KNOCKBACK_RESISTANCE), new SimpleEntry("Knockback Resistance", Attributes.KNOCKBACK_RESISTANCE), new SimpleEntry("generic.movementSpeed", Attributes.MOVEMENT_SPEED), new SimpleEntry("Movement Speed", Attributes.MOVEMENT_SPEED), new SimpleEntry("generic.flyingSpeed", Attributes.FLYING_SPEED), new SimpleEntry("Flying Speed", Attributes.FLYING_SPEED), new SimpleEntry("generic.attackDamage", Attributes.ATTACK_DAMAGE), new SimpleEntry("generic.attackKnockback", Attributes.ATTACK_KNOCKBACK), new SimpleEntry("generic.attackSpeed", Attributes.ATTACK_SPEED), new SimpleEntry("generic.armorToughness", Attributes.ARMOR_TOUGHNESS), new SimpleEntry("generic.armor", Attributes.ARMOR), new SimpleEntry("generic.luck", Attributes.LUCK)));
      PRE_1_16_ATTRIBUTES_MAP = (Map)PRE_1_16_ATTRIBUTES.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
      PRE_1_16_ATTRIBUTES_RMAP = (Map)PRE_1_16_ATTRIBUTES.stream().collect(Collectors.toMap(Entry::getValue, Entry::getKey, (s1, s2) -> {
         return s1;
      }));
   }

   public static class PropertyModifier {
      private ResourceLocation name;
      private UUID uuid;
      private double amount;
      private WrapperPlayServerUpdateAttributes.PropertyModifier.Operation operation;

      public PropertyModifier(UUID uuid, double amount, WrapperPlayServerUpdateAttributes.PropertyModifier.Operation operation) {
         this(new ResourceLocation(uuid.toString()), uuid, amount, operation);
      }

      public PropertyModifier(ResourceLocation name, double amount, WrapperPlayServerUpdateAttributes.PropertyModifier.Operation operation) {
         this(name, generateSemiUniqueId(name), amount, operation);
      }

      public PropertyModifier(ResourceLocation name, UUID uuid, double amount, WrapperPlayServerUpdateAttributes.PropertyModifier.Operation operation) {
         this.name = name;
         this.uuid = uuid;
         this.amount = amount;
         this.operation = operation;
      }

      @ApiStatus.Internal
      public static UUID generateSemiUniqueId(ResourceLocation name) {
         String extendedName = "packetevents_" + name.toString();
         return UUID.nameUUIDFromBytes(extendedName.getBytes(StandardCharsets.UTF_8));
      }

      public ResourceLocation getName() {
         return this.name;
      }

      public void setName(ResourceLocation name) {
         this.name = name;
         this.uuid = generateSemiUniqueId(name);
      }

      @ApiStatus.Obsolete
      public UUID getUUID() {
         return this.uuid;
      }

      @ApiStatus.Obsolete
      public void setUUID(UUID uuid) {
         this.name = new ResourceLocation(uuid.toString());
         this.uuid = uuid;
      }

      public void setNameAndUUID(ResourceLocation name, UUID uuid) {
         this.name = name;
         this.uuid = uuid;
      }

      public double getAmount() {
         return this.amount;
      }

      public void setAmount(double amount) {
         this.amount = amount;
      }

      public WrapperPlayServerUpdateAttributes.PropertyModifier.Operation getOperation() {
         return this.operation;
      }

      public void setOperation(WrapperPlayServerUpdateAttributes.PropertyModifier.Operation operation) {
         this.operation = operation;
      }

      public static enum Operation {
         ADDITION,
         MULTIPLY_BASE,
         MULTIPLY_TOTAL;

         public static final WrapperPlayServerUpdateAttributes.PropertyModifier.Operation[] VALUES = values();

         // $FF: synthetic method
         private static WrapperPlayServerUpdateAttributes.PropertyModifier.Operation[] $values() {
            return new WrapperPlayServerUpdateAttributes.PropertyModifier.Operation[]{ADDITION, MULTIPLY_BASE, MULTIPLY_TOTAL};
         }
      }
   }

   public static class Property {
      private Attribute attribute;
      private double value;
      private List<WrapperPlayServerUpdateAttributes.PropertyModifier> modifiers;
      private transient Double calculatedValue;

      /** @deprecated */
      @Deprecated
      public Property(String key, double value, List<WrapperPlayServerUpdateAttributes.PropertyModifier> modifiers) {
         this(Attributes.getByName(key), value, modifiers);
      }

      public Property(Attribute attribute, double value, List<WrapperPlayServerUpdateAttributes.PropertyModifier> modifiers) {
         this.calculatedValue = null;
         this.attribute = attribute;
         this.value = value;
         this.modifiers = modifiers;
      }

      public double calcValue() {
         if (this.calculatedValue == null) {
            this.calculatedValue = this.calcValue0();
         }

         return this.calculatedValue;
      }

      public double calcValue0() {
         double base = this.getValue();
         Iterator var3 = this.modifiers.iterator();

         while(var3.hasNext()) {
            WrapperPlayServerUpdateAttributes.PropertyModifier modifier = (WrapperPlayServerUpdateAttributes.PropertyModifier)var3.next();
            if (modifier.getOperation() == WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.ADDITION) {
               base += modifier.getAmount();
            }
         }

         double value = base;
         Iterator var5 = this.modifiers.iterator();

         WrapperPlayServerUpdateAttributes.PropertyModifier modifier;
         while(var5.hasNext()) {
            modifier = (WrapperPlayServerUpdateAttributes.PropertyModifier)var5.next();
            if (modifier.getOperation() == WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.MULTIPLY_BASE) {
               value += base * modifier.getAmount();
            }
         }

         var5 = this.modifiers.iterator();

         while(var5.hasNext()) {
            modifier = (WrapperPlayServerUpdateAttributes.PropertyModifier)var5.next();
            if (modifier.getOperation() == WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.MULTIPLY_TOTAL) {
               value *= 1.0D + modifier.getAmount();
            }
         }

         return this.attribute.sanitizeValue(value);
      }

      public Attribute getAttribute() {
         return this.attribute;
      }

      public void setAttribute(Attribute attribute) {
         this.attribute = attribute;
      }

      /** @deprecated */
      @Deprecated
      public String getKey() {
         return this.getAttribute().getName().toString();
      }

      /** @deprecated */
      @Deprecated
      public void setKey(String key) {
         this.setAttribute(Attributes.getByName(key));
      }

      public double getValue() {
         return this.value;
      }

      public void setValue(double value) {
         this.value = value;
         this.setDirty();
      }

      public void addModifier(WrapperPlayServerUpdateAttributes.PropertyModifier modifier) {
         this.modifiers.add(modifier);
         this.setDirty();
      }

      public boolean removeModifier(ResourceLocation modifierId) {
         return this.removeModifierIf((modifier) -> {
            return modifierId.equals(modifier.getName());
         });
      }

      @ApiStatus.Obsolete
      public boolean removeModifier(UUID modifierId) {
         return this.removeModifierIf((modifier) -> {
            return modifierId.equals(modifier.getUUID());
         });
      }

      @ApiStatus.Obsolete
      public boolean removeModifier(ResourceLocation modifierId, UUID modifierUId) {
         return this.removeModifierIf((modifier) -> {
            return modifierUId.equals(modifier.getUUID()) || modifierId.equals(modifier.getName());
         });
      }

      public boolean removeModifierIf(Predicate<WrapperPlayServerUpdateAttributes.PropertyModifier> predicate) {
         boolean ret = this.modifiers.removeIf(predicate);
         if (ret) {
            this.setDirty();
         }

         return ret;
      }

      public List<WrapperPlayServerUpdateAttributes.PropertyModifier> getModifiers() {
         return this.modifiers;
      }

      public void setModifiers(List<WrapperPlayServerUpdateAttributes.PropertyModifier> modifiers) {
         this.modifiers = modifiers;
         this.setDirty();
      }

      public void setDirty() {
         this.calculatedValue = null;
      }
   }
}
