package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Attachment;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Axis;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Bloom;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.CreakingHeartState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.East;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Face;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Hinge;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Instrument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Leaves;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Mode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.North;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Orientation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Part;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.SculkSensorPhase;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Shape;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.South;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Thickness;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Tilt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.TrialSpawnerState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Type;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.VerticalDirection;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.West;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateValue;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.MappingHelper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class WrappedBlockState {
   private static final ClientVersion[] MAPPING_VERSION_STEPS;
   private static final byte[] MAPPING_INDEXES;
   private static final ClientVersion[] MAPPING_VERSIONS;
   private static final byte AIR_MAPPING_INDEX = 0;
   private static final byte LEGACY_MAPPING_INDEX = 1;
   private static final byte HIGHEST_MAPPING_INDEX;
   private static final String MAPPINGS_ASSETS_PREFIX = "mappings/data/block_state/";
   private static final String MAPPINGS_ASSETS_LEGACY = "mappings/data/block_state/legacy";
   private static final boolean PRELOAD_BLOCK_STATE_MAPPINGS;
   private static final WrappedBlockState AIR;
   private static final Map<String, WrappedBlockState>[] BY_STRING;
   private static final Map<Integer, WrappedBlockState>[] BY_ID;
   private static final Map<WrappedBlockState, String>[] INTO_STRING;
   private static final Map<WrappedBlockState, Integer>[] INTO_ID;
   private static final Map<StateType, WrappedBlockState>[] DEFAULT_STATES;
   private static final Map<String, String> STRING_UPDATER;
   int globalID;
   StateType type;
   Map<StateValue, Object> data = new HashMap(0);
   boolean hasClonedData = false;
   byte mappingsIndex;

   /** @deprecated */
   @Deprecated
   public WrappedBlockState(StateType type, String[] data, int globalID, byte mappingsIndex) {
      this.type = type;
      this.globalID = globalID;
      if (data != null) {
         String[] var5 = data;
         int var6 = data.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String s = var5[var7];

            try {
               String[] split = s.split("=");
               StateValue value = StateValue.byName(split[0]);
               this.data.put(value, value.getParser().apply(split[1].toUpperCase(Locale.ROOT)));
            } catch (Exception var11) {
               var11.printStackTrace();
               PacketEvents.getAPI().getLogManager().warn("Failed to parse block state: " + s);
            }
         }
      }

      this.mappingsIndex = mappingsIndex;
   }

   public WrappedBlockState(StateType type, Map<StateValue, Object> data, int globalID, byte mappingsIndex) {
      this.globalID = globalID;
      this.type = type;
      this.data = data;
      this.mappingsIndex = mappingsIndex;
   }

   private static byte loadMappings(ClientVersion version) {
      byte mappingsIndex = getMappingsIndex(version);
      if (!PRELOAD_BLOCK_STATE_MAPPINGS && BY_ID[mappingsIndex].isEmpty()) {
         loadMappings0(getMappingsVersion(version), mappingsIndex);
      }

      return mappingsIndex;
   }

   private static synchronized void loadMappings0(ClientVersion version, byte mappingsIndex) {
      if (BY_ID[mappingsIndex].isEmpty()) {
         PacketEvents.getAPI().getLogger().info("Loading block mappings for " + version + "/" + mappingsIndex + "...");
         long start = System.nanoTime();
         if (mappingsIndex == 1) {
            loadLegacy(buildStateDataCache());
         } else {
            loadModern(buildStateDataCache(), version);
         }

         double timeDiff = (double)(System.nanoTime() - start) / 1000000.0D;
         PacketEvents.getAPI().getLogger().info("Finished loading block mappings for " + version + "/" + mappingsIndex + " in " + timeDiff + "ms");
      }
   }

   private static Map<Map<StateValue, Object>, WrappedBlockState.StateCacheValue> buildStateDataCache() {
      Map<Map<StateValue, Object>, WrappedBlockState.StateCacheValue> cache = new HashMap();

      for(byte i = 0; i < HIGHEST_MAPPING_INDEX; ++i) {
         Map<Integer, WrappedBlockState> map = BY_ID[i];
         if (map != null) {
            Iterator var3 = map.values().iterator();

            while(var3.hasNext()) {
               WrappedBlockState state = (WrappedBlockState)var3.next();
               cache.computeIfAbsent(state.data, WrappedBlockState.StateCacheValue::new);
            }
         }
      }

      return cache;
   }

   public static WrappedBlockState decode(NBT nbt, ClientVersion version) {
      if (nbt instanceof NBTString) {
         StateType type = StateTypes.getByName(((NBTString)nbt).getValue());
         return getDefaultState(version, type);
      } else {
         NBTCompound compound = (NBTCompound)nbt;
         String blockName = compound.getStringTagValueOrThrow("Name");
         StateType block = StateTypes.getByName(blockName);
         WrappedBlockState state = getDefaultState(version, block);
         if (state != AIR) {
            NBTCompound propsTag = compound.getCompoundTagOrNull("Properties");
            StateValue stateValue;
            Object value;
            if (propsTag != null) {
               for(Iterator var7 = propsTag.getTags().entrySet().iterator(); var7.hasNext(); state.getInternalData().put(stateValue, value)) {
                  Entry<String, NBT> entry = (Entry)var7.next();
                  stateValue = (StateValue)AdventureIndexUtil.indexValueOrThrow(StateValue.NAME_INDEX, (String)entry.getKey());
                  if (stateValue.getDataClass() == Boolean.TYPE) {
                     value = ((NBTByte)entry.getValue()).getAsBool();
                  } else if (entry.getValue() instanceof NBTNumber) {
                     Number num = ((NBTNumber)entry.getValue()).getAsNumber();
                     value = stateValue.parse(num.toString());
                  } else {
                     value = stateValue.parse(((NBTString)entry.getValue()).getValue());
                  }
               }
            }
         }

         return state;
      }
   }

   public static NBT encode(WrappedBlockState state, ClientVersion version) {
      String stateTypeStr = state.type.getMapped().getName().toString();
      WrappedBlockState defaultState;
      if (!state.getInternalData().isEmpty() && !state.equals(defaultState = getDefaultState(version, state.type))) {
         NBTCompound propsTag = new NBTCompound();
         Iterator var5 = state.getInternalData().entrySet().iterator();

         while(var5.hasNext()) {
            Entry<StateValue, Object> dataEntry = (Entry)var5.next();
            StateValue stateValue = (StateValue)dataEntry.getKey();
            if (!Objects.equals(defaultState.getInternalData().get(stateValue), dataEntry.getValue())) {
               Object valueTag;
               if (stateValue.getDataClass() == Boolean.TYPE) {
                  valueTag = new NBTByte((Boolean)dataEntry.getValue());
               } else if (stateValue.getDataClass() == Integer.TYPE) {
                  valueTag = new NBTInt((Integer)dataEntry.getValue());
               } else {
                  valueTag = new NBTString(dataEntry.getValue().toString());
               }

               propsTag.setTag(stateValue.getName(), (NBT)valueTag);
            }
         }

         NBTCompound compound = new NBTCompound();
         compound.setTag("Name", new NBTString(stateTypeStr));
         compound.setTag("Properties", propsTag);
         return compound;
      } else {
         return new NBTString(stateTypeStr);
      }
   }

   @NotNull
   public static WrappedBlockState getByGlobalId(int globalID) {
      return getByGlobalId(globalID, true);
   }

   @NotNull
   public static WrappedBlockState getByGlobalId(int globalID, boolean clone) {
      return getByGlobalId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), globalID, clone);
   }

   @NotNull
   public static WrappedBlockState getByGlobalId(ClientVersion version, int globalID) {
      return getByGlobalId(version, globalID, true);
   }

   @NotNull
   public static WrappedBlockState getByGlobalId(ClientVersion version, int globalID, boolean clone) {
      if (globalID == 0) {
         return AIR;
      } else {
         byte mappingsIndex = loadMappings(version);
         WrappedBlockState state = (WrappedBlockState)BY_ID[mappingsIndex].getOrDefault(globalID, AIR);
         return clone ? state.clone() : state;
      }
   }

   @NotNull
   public static WrappedBlockState getByString(String string) {
      return getByString(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), string);
   }

   @NotNull
   public static WrappedBlockState getByString(ClientVersion version, String string) {
      return getByString(version, string, true);
   }

   @NotNull
   public static WrappedBlockState getByString(ClientVersion version, String string, boolean clone) {
      byte mappingsIndex = loadMappings(version);
      WrappedBlockState state = (WrappedBlockState)BY_STRING[mappingsIndex].getOrDefault(string.replace("minecraft:", ""), AIR);
      return clone ? state.clone() : state;
   }

   @NotNull
   public static WrappedBlockState getDefaultState(StateType type) {
      return getDefaultState(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), type);
   }

   @NotNull
   public static WrappedBlockState getDefaultState(ClientVersion version, StateType type) {
      return getDefaultState(version, type, true);
   }

   @NotNull
   public static WrappedBlockState getDefaultState(ClientVersion version, StateType type, boolean clone) {
      if (type == StateTypes.AIR) {
         return AIR;
      } else {
         byte mappingsIndex = loadMappings(version);
         WrappedBlockState state = (WrappedBlockState)DEFAULT_STATES[mappingsIndex].get(type);
         if (state == null) {
            PacketEvents.getAPI().getLogger().config("Default state for " + type.getName() + " is null. Returning AIR");
            return AIR;
         } else {
            return clone ? state.clone() : state;
         }
      }
   }

   private static byte getMappingsIndex(ClientVersion version) {
      return MAPPING_INDEXES[version.ordinal()];
   }

   private static ClientVersion getMappingsVersion(ClientVersion version) {
      return MAPPING_VERSIONS[version.ordinal()];
   }

   private static void loadLegacy(Map<Map<StateValue, Object>, WrappedBlockState.StateCacheValue> cache) {
      Map<Integer, WrappedBlockState> stateByIdMap = new HashMap();
      Map<WrappedBlockState, Integer> stateToIdMap = new HashMap();
      Map<String, WrappedBlockState> stateByStringMap = new HashMap();
      Map<WrappedBlockState, String> stateToStringMap = new HashMap();
      HashMap stateTypeToBlockStateMap = new HashMap();

      try {
         SequentialNBTReader.Compound compound = MappingHelper.decompress("mappings/data/block_state/legacy");

         try {
            compound.skipOne();
            Iterator var7 = ((SequentialNBTReader.Compound)compound.next().getValue()).iterator();

            while(true) {
               if (!var7.hasNext()) {
                  BY_ID[1] = stateByIdMap;
                  INTO_ID[1] = stateToIdMap;
                  BY_STRING[1] = stateByStringMap;
                  INTO_STRING[1] = stateToStringMap;
                  DEFAULT_STATES[1] = stateTypeToBlockStateMap;
                  break;
               }

               Entry<String, NBT> entry = (Entry)var7.next();
               SequentialNBTReader.Compound inner = (SequentialNBTReader.Compound)entry.getValue();
               StateType type = StateTypes.getByName((String)entry.getKey());
               if (type == null) {
                  PacketEvents.getAPI().getLogger().warning("Could not find type for " + (String)entry.getKey());
                  inner.skip();
               } else {
                  Iterator var11 = inner.iterator();

                  while(var11.hasNext()) {
                     Entry<String, NBT> element = (Entry)var11.next();
                     String elementName = (String)element.getKey();
                     int idIndex = elementName.indexOf(58);
                     int id = Integer.parseInt(elementName.substring(0, idIndex));
                     int data = Integer.parseInt(elementName.substring(idIndex + 1));
                     int combinedID = id << 4 | data;
                     SequentialNBTReader.Compound dataContent = (SequentialNBTReader.Compound)element.getValue();
                     WrappedBlockState.StateCacheValue stateCache;
                     if (!dataContent.hasNext()) {
                        stateCache = WrappedBlockState.StateCacheValue.EMPTY;
                     } else {
                        Map<StateValue, Object> dataMap = new LinkedHashMap(3);
                        Iterator var21 = dataContent.iterator();

                        while(true) {
                           while(var21.hasNext()) {
                              Entry<String, NBT> props = (Entry)var21.next();
                              StateValue state = StateValue.byName((String)props.getKey());
                              if (state == null) {
                                 PacketEvents.getAPI().getLogger().warning("Could not find value for " + (String)props.getKey());
                              } else {
                                 NBT value = (NBT)props.getValue();
                                 Object v;
                                 if (value instanceof NBTByte) {
                                    v = ((NBTByte)value).getAsInt() == 1;
                                 } else if (value instanceof NBTNumber) {
                                    v = ((NBTNumber)value).getAsInt();
                                 } else {
                                    if (!(value instanceof NBTString)) {
                                       PacketEvents.getAPI().getLogger().warning("Unknown NBT type in legacy mapping: " + value.getClass().getSimpleName());
                                       continue;
                                    }

                                    v = ((NBTString)value).getValue();
                                 }

                                 dataMap.put(state, state.getParser().apply(v.toString().toUpperCase(Locale.ROOT)));
                              }
                           }

                           stateCache = (WrappedBlockState.StateCacheValue)cache.computeIfAbsent(dataMap, WrappedBlockState.StateCacheValue::new);
                           break;
                        }
                     }

                     String fullString = (String)entry.getKey() + stateCache.getString();
                     WrappedBlockState state = new WrappedBlockState(type, stateCache.map, combinedID, (byte)1);
                     stateByIdMap.put(combinedID, state);
                     stateToStringMap.put(state, fullString);
                     stateToIdMap.put(state, combinedID);
                     stateByStringMap.putIfAbsent(fullString, state);
                     stateTypeToBlockStateMap.putIfAbsent(type, state);
                  }
               }
            }
         } catch (Throwable var27) {
            if (compound != null) {
               try {
                  compound.close();
               } catch (Throwable var26) {
                  var27.addSuppressed(var26);
               }
            }

            throw var27;
         }

         if (compound != null) {
            compound.close();
         }

      } catch (IOException var28) {
         throw new RuntimeException("Failed to load legacy block mappings", var28);
      }
   }

   private static void loadModern(Map<Map<StateValue, Object>, WrappedBlockState.StateCacheValue> cache, ClientVersion version) {
      try {
         SequentialNBTReader.Compound compound = MappingHelper.decompress("mappings/data/block_state/" + version.name());

         try {
            compound.skipOne();
            byte mappingIndex = getMappingsIndex(version);
            SequentialNBTReader.List list = (SequentialNBTReader.List)compound.next().getValue();
            Map<Integer, WrappedBlockState> stateByIdMap = new HashMap();
            Map<WrappedBlockState, Integer> stateToIdMap = new HashMap();
            Map<String, WrappedBlockState> stateByStringMap = new HashMap();
            Map<WrappedBlockState, String> stateToStringMap = new HashMap();
            Map<StateType, WrappedBlockState> stateTypeToBlockStateMap = new HashMap();
            int id = 0;
            Iterator var11 = list.iterator();

            while(true) {
               if (!var11.hasNext()) {
                  BY_ID[mappingIndex] = stateByIdMap;
                  INTO_ID[mappingIndex] = stateToIdMap;
                  BY_STRING[mappingIndex] = stateByStringMap;
                  INTO_STRING[mappingIndex] = stateToStringMap;
                  DEFAULT_STATES[mappingIndex] = stateTypeToBlockStateMap;
                  break;
               }

               NBT e = (NBT)var11.next();
               SequentialNBTReader.Compound element = (SequentialNBTReader.Compound)e;
               String typeString = ((NBTString)element.next().getValue()).getValue();
               StateType type = StateTypes.getByName(typeString);
               if (type == null) {
                  Entry stringEntry;
                  for(Iterator var16 = STRING_UPDATER.entrySet().iterator(); var16.hasNext(); typeString = typeString.replace((CharSequence)stringEntry.getKey(), (CharSequence)stringEntry.getValue())) {
                     stringEntry = (Entry)var16.next();
                  }

                  type = StateTypes.getByName(typeString);
                  if (type == null) {
                     PacketEvents.getAPI().getLogger().warning("Unknown block type: " + typeString);
                     element.skip();
                     continue;
                  }
               }

               Entry<String, NBT> next = element.next();
               int defaultIdx = 0;
               if (!((String)next.getKey()).equals("def")) {
                  PacketEvents.getAPI().getLogger().warning("No default state for " + type + " using 0");
               } else {
                  defaultIdx = ((NBTNumber)next.getValue()).getAsInt();
                  next = element.next();
               }

               int index = 0;

               for(Iterator var19 = ((SequentialNBTReader.List)next.getValue()).iterator(); var19.hasNext(); ++index) {
                  NBT nbt = (NBT)var19.next();
                  SequentialNBTReader.Compound dataContent = (SequentialNBTReader.Compound)nbt;
                  WrappedBlockState.StateCacheValue stateCache;
                  if (!dataContent.hasNext()) {
                     stateCache = WrappedBlockState.StateCacheValue.EMPTY;
                  } else {
                     Map<StateValue, Object> dataMap = new LinkedHashMap(3);
                     Iterator var24 = dataContent.iterator();

                     while(true) {
                        while(var24.hasNext()) {
                           Entry<String, NBT> props = (Entry)var24.next();
                           StateValue state = StateValue.byName((String)props.getKey());
                           if (state == null) {
                              PacketEvents.getAPI().getLogger().warning("Could not find value for " + (String)props.getKey());
                           } else {
                              NBT value = (NBT)props.getValue();
                              Object v;
                              if (value instanceof NBTByte) {
                                 v = ((NBTByte)value).getAsInt() == 1;
                              } else if (value instanceof NBTNumber) {
                                 v = ((NBTNumber)value).getAsInt();
                              } else {
                                 if (!(value instanceof NBTString)) {
                                    PacketEvents.getAPI().getLogger().warning("Unknown NBT typeString in modern mapping: " + value.getClass().getSimpleName());
                                    continue;
                                 }

                                 v = ((NBTString)value).getValue();
                              }

                              dataMap.put(state, state.getParser().apply(v.toString().toUpperCase(Locale.ROOT)));
                           }
                        }

                        stateCache = (WrappedBlockState.StateCacheValue)cache.computeIfAbsent(dataMap, WrappedBlockState.StateCacheValue::new);
                        break;
                     }
                  }

                  String fullString = typeString + stateCache.getString();
                  WrappedBlockState state = new WrappedBlockState(type, stateCache.map, id, mappingIndex);
                  if (defaultIdx == index) {
                     stateTypeToBlockStateMap.put(type, state);
                  }

                  stateByStringMap.put(fullString, state);
                  stateByIdMap.put(id, state);
                  stateToStringMap.put(state, fullString);
                  stateToIdMap.put(state, id);
                  ++id;
               }
            }
         } catch (Throwable var30) {
            if (compound != null) {
               try {
                  compound.close();
               } catch (Throwable var29) {
                  var30.addSuppressed(var29);
               }
            }

            throw var30;
         }

         if (compound != null) {
            compound.close();
         }

      } catch (IOException var31) {
         throw new RuntimeException("Failed to load modern block mappings", var31);
      }
   }

   public WrappedBlockState clone() {
      return new WrappedBlockState(this.type, this.data, this.globalID, this.mappingsIndex);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof WrappedBlockState)) {
         return false;
      } else {
         WrappedBlockState that = (WrappedBlockState)o;
         return this.type == that.type && this.data.equals(that.data);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.type, this.data});
   }

   public StateType getType() {
      return this.type;
   }

   public Object getData(StateValue stateValue) {
      return this.data.get(stateValue);
   }

   public void setData(StateValue stateValue, Object object) {
      this.checkIfCloneNeeded();
      this.data.put(stateValue, object);
      this.checkIsStillValid();
   }

   public int getAge() {
      return (Integer)this.data.get(StateValue.AGE);
   }

   public void setAge(int age) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.AGE, age);
      this.checkIsStillValid();
   }

   public boolean isAttached() {
      return (Boolean)this.data.get(StateValue.ATTACHED);
   }

   public void setAttached(boolean attached) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.ATTACHED, attached);
      this.checkIsStillValid();
   }

   public Attachment getAttachment() {
      return (Attachment)this.data.get(StateValue.ATTACHMENT);
   }

   public void setAttachment(Attachment attachment) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.ATTACHMENT, attachment);
      this.checkIsStillValid();
   }

   public Axis getAxis() {
      return (Axis)this.data.get(StateValue.AXIS);
   }

   public void setAxis(Axis axis) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.AXIS, axis);
      this.checkIsStillValid();
   }

   public boolean isBerries() {
      return (Boolean)this.data.get(StateValue.BERRIES);
   }

   public void setBerries(boolean berries) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.BERRIES, berries);
      this.checkIsStillValid();
   }

   public int getBites() {
      return (Integer)this.data.get(StateValue.BITES);
   }

   public void setBites(int bites) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.BITES, bites);
      this.checkIsStillValid();
   }

   public boolean isBottom() {
      return (Boolean)this.data.get(StateValue.BOTTOM);
   }

   public void setBottom(boolean bottom) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.BOTTOM, bottom);
      this.checkIsStillValid();
   }

   public int getCandles() {
      return (Integer)this.data.get(StateValue.CANDLES);
   }

   public void setCandles(int candles) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CANDLES, candles);
      this.checkIsStillValid();
   }

   public int getCharges() {
      return (Integer)this.data.get(StateValue.CHARGES);
   }

   public void setCharges(int charges) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CHARGES, charges);
      this.checkIsStillValid();
   }

   public boolean isConditional() {
      return (Boolean)this.data.get(StateValue.CONDITIONAL);
   }

   public void setConditional(boolean conditional) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CONDITIONAL, conditional);
      this.checkIsStillValid();
   }

   public int getDelay() {
      return (Integer)this.data.get(StateValue.DELAY);
   }

   public void setDelay(int delay) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.DELAY, delay);
      this.checkIsStillValid();
   }

   public boolean isDisarmed() {
      return (Boolean)this.data.get(StateValue.DISARMED);
   }

   public void setDisarmed(boolean disarmed) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.DISARMED, disarmed);
      this.checkIsStillValid();
   }

   public int getDistance() {
      return (Integer)this.data.get(StateValue.DISTANCE);
   }

   public void setDistance(int distance) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.DISTANCE, distance);
      this.checkIsStillValid();
   }

   public boolean isDown() {
      return (Boolean)this.data.get(StateValue.DOWN);
   }

   public void setDown(boolean down) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.DOWN, down);
      this.checkIsStillValid();
   }

   public boolean isDrag() {
      return (Boolean)this.data.get(StateValue.DRAG);
   }

   public void setDrag(boolean drag) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.DRAG, drag);
      this.checkIsStillValid();
   }

   public boolean isDusted() {
      return (Boolean)this.data.get(StateValue.DUSTED);
   }

   public void setDusted(boolean dusted) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.DUSTED, dusted);
      this.checkIsStillValid();
   }

   public int getEggs() {
      return (Integer)this.data.get(StateValue.EGGS);
   }

   public void setEggs(int eggs) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.EGGS, eggs);
      this.checkIsStillValid();
   }

   public boolean isEnabled() {
      return (Boolean)this.data.get(StateValue.ENABLED);
   }

   public void setEnabled(boolean enabled) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.ENABLED, enabled);
      this.checkIsStillValid();
   }

   public boolean isExtended() {
      return (Boolean)this.data.get(StateValue.EXTENDED);
   }

   public void setExtended(boolean extended) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.EXTENDED, extended);
      this.checkIsStillValid();
   }

   public boolean isEye() {
      return (Boolean)this.data.get(StateValue.EYE);
   }

   public void setEye(boolean eye) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.EYE, eye);
      this.checkIsStillValid();
   }

   public Face getFace() {
      return (Face)this.data.get(StateValue.FACE);
   }

   public void setFace(Face face) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.FACE, face);
      this.checkIsStillValid();
   }

   public BlockFace getFacing() {
      return (BlockFace)this.data.get(StateValue.FACING);
   }

   public void setFacing(BlockFace facing) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.FACING, facing);
      this.checkIsStillValid();
   }

   public int getFlowerAmount() {
      return (Integer)this.data.get(StateValue.FLOWER_AMOUNT);
   }

   public void setFlowerAmount(int flowerAmount) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.FLOWER_AMOUNT, flowerAmount);
      this.checkIsStillValid();
   }

   public Half getHalf() {
      return (Half)this.data.get(StateValue.HALF);
   }

   public void setHalf(Half half) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HALF, half);
      this.checkIsStillValid();
   }

   public boolean isHanging() {
      return (Boolean)this.data.get(StateValue.HANGING);
   }

   public void setHanging(boolean hanging) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HANGING, hanging);
      this.checkIsStillValid();
   }

   public boolean isHasBook() {
      return (Boolean)this.data.get(StateValue.HAS_BOOK);
   }

   public void setHasBook(boolean hasBook) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HAS_BOOK, hasBook);
      this.checkIsStillValid();
   }

   public boolean isHasBottle0() {
      return (Boolean)this.data.get(StateValue.HAS_BOTTLE_0);
   }

   public void setHasBottle0(boolean hasBottle0) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HAS_BOTTLE_0, hasBottle0);
      this.checkIsStillValid();
   }

   public boolean isHasBottle1() {
      return (Boolean)this.data.get(StateValue.HAS_BOTTLE_1);
   }

   public void setHasBottle1(boolean hasBottle1) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HAS_BOTTLE_1, hasBottle1);
      this.checkIsStillValid();
   }

   public boolean isHasBottle2() {
      return (Boolean)this.data.get(StateValue.HAS_BOTTLE_2);
   }

   public void setHasBottle2(boolean hasBottle2) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HAS_BOTTLE_2, hasBottle2);
      this.checkIsStillValid();
   }

   public boolean isHasRecord() {
      return (Boolean)this.data.get(StateValue.HAS_RECORD);
   }

   public void setHasRecord(boolean hasRecord) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HAS_RECORD, hasRecord);
      this.checkIsStillValid();
   }

   public int getHatch() {
      return (Integer)this.data.get(StateValue.HATCH);
   }

   public void setHatch(int hatch) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HATCH, hatch);
      this.checkIsStillValid();
   }

   public Hinge getHinge() {
      return (Hinge)this.data.get(StateValue.HINGE);
   }

   public void setHinge(Hinge hinge) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HINGE, hinge);
      this.checkIsStillValid();
   }

   public int getHoneyLevel() {
      return (Integer)this.data.get(StateValue.HONEY_LEVEL);
   }

   public void setHoneyLevel(int honeyLevel) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HONEY_LEVEL, honeyLevel);
      this.checkIsStillValid();
   }

   public boolean isInWall() {
      return (Boolean)this.data.get(StateValue.IN_WALL);
   }

   public void setInWall(boolean inWall) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.IN_WALL, inWall);
      this.checkIsStillValid();
   }

   public Instrument getInstrument() {
      return (Instrument)this.data.get(StateValue.INSTRUMENT);
   }

   public void setInstrument(Instrument instrument) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.INSTRUMENT, instrument);
      this.checkIsStillValid();
   }

   public boolean isInverted() {
      return (Boolean)this.data.get(StateValue.INVERTED);
   }

   public void setInverted(boolean inverted) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.INVERTED, inverted);
      this.checkIsStillValid();
   }

   public int getLayers() {
      return (Integer)this.data.get(StateValue.LAYERS);
   }

   public void setLayers(int layers) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.LAYERS, layers);
      this.checkIsStillValid();
   }

   public Leaves getLeaves() {
      return (Leaves)this.data.get(StateValue.LEAVES);
   }

   public void setLeaves(Leaves leaves) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.LEAVES, leaves);
      this.checkIsStillValid();
   }

   public int getLevel() {
      return (Integer)this.data.get(StateValue.LEVEL);
   }

   public void setLevel(int level) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.LEVEL, level);
      this.checkIsStillValid();
   }

   public boolean isLit() {
      return (Boolean)this.data.get(StateValue.LIT);
   }

   public void setLit(boolean lit) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.LIT, lit);
      this.checkIsStillValid();
   }

   public boolean isTip() {
      return (Boolean)this.data.get(StateValue.TIP);
   }

   public void setTip(boolean tip) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.TIP, tip);
      this.checkIsStillValid();
   }

   public boolean isLocked() {
      return (Boolean)this.data.get(StateValue.LOCKED);
   }

   public void setLocked(boolean locked) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.LOCKED, locked);
      this.checkIsStillValid();
   }

   public Mode getMode() {
      return (Mode)this.data.get(StateValue.MODE);
   }

   public void setMode(Mode mode) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.MODE, mode);
      this.checkIsStillValid();
   }

   public int getMoisture() {
      return (Integer)this.data.get(StateValue.MOISTURE);
   }

   public void setMoisture(int moisture) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.MOISTURE, moisture);
      this.checkIsStillValid();
   }

   public North getNorth() {
      return (North)this.data.get(StateValue.NORTH);
   }

   public void setNorth(North north) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.NORTH, north);
      this.checkIsStillValid();
   }

   public int getNote() {
      return (Integer)this.data.get(StateValue.NOTE);
   }

   public void setNote(int note) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.NOTE, note);
      this.checkIsStillValid();
   }

   public boolean isOccupied() {
      return (Boolean)this.data.get(StateValue.OCCUPIED);
   }

   public void setOccupied(boolean occupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.OCCUPIED, occupied);
      this.checkIsStillValid();
   }

   public boolean isShrieking() {
      return (Boolean)this.data.get(StateValue.SHRIEKING);
   }

   public void setShrieking(boolean shrieking) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SHRIEKING, shrieking);
      this.checkIsStillValid();
   }

   public boolean isCanSummon() {
      return (Boolean)this.data.get(StateValue.CAN_SUMMON);
   }

   public void setCanSummon(boolean canSummon) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CAN_SUMMON, canSummon);
      this.checkIsStillValid();
   }

   public boolean isOpen() {
      return (Boolean)this.data.get(StateValue.OPEN);
   }

   public void setOpen(boolean open) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.OPEN, open);
      this.checkIsStillValid();
   }

   public Orientation getOrientation() {
      return (Orientation)this.data.get(StateValue.ORIENTATION);
   }

   public void setOrientation(Orientation orientation) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.ORIENTATION, orientation);
      this.checkIsStillValid();
   }

   public Part getPart() {
      return (Part)this.data.get(StateValue.PART);
   }

   public void setPart(Part part) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.PART, part);
      this.checkIsStillValid();
   }

   public boolean isPersistent() {
      return (Boolean)this.data.get(StateValue.PERSISTENT);
   }

   public void setPersistent(boolean persistent) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.PERSISTENT, persistent);
      this.checkIsStillValid();
   }

   public int getPickles() {
      return (Integer)this.data.get(StateValue.PICKLES);
   }

   public void setPickles(int pickles) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.PICKLES, pickles);
      this.checkIsStillValid();
   }

   public int getPower() {
      return (Integer)this.data.get(StateValue.POWER);
   }

   public void setPower(int power) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.POWER, power);
      this.checkIsStillValid();
   }

   public boolean isPowered() {
      return (Boolean)this.data.get(StateValue.POWERED);
   }

   public void setPowered(boolean powered) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.POWERED, powered);
      this.checkIsStillValid();
   }

   public int getRotation() {
      return (Integer)this.data.get(StateValue.ROTATION);
   }

   public void setRotation(int rotation) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.ROTATION, rotation);
      this.checkIsStillValid();
   }

   public SculkSensorPhase getSculkSensorPhase() {
      return (SculkSensorPhase)this.data.get(StateValue.SCULK_SENSOR_PHASE);
   }

   public void setSculkSensorPhase(SculkSensorPhase sculkSensorPhase) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SCULK_SENSOR_PHASE, sculkSensorPhase);
      this.checkIsStillValid();
   }

   public Shape getShape() {
      return (Shape)this.data.get(StateValue.SHAPE);
   }

   public void setShape(Shape shape) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SHAPE, shape);
      this.checkIsStillValid();
   }

   public boolean isShort() {
      return (Boolean)this.data.get(StateValue.SHORT);
   }

   public void setShort(boolean short_) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SHORT, short_);
      this.checkIsStillValid();
   }

   public boolean isSignalFire() {
      return (Boolean)this.data.get(StateValue.SIGNAL_FIRE);
   }

   public void setSignalFire(boolean signalFire) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SIGNAL_FIRE, signalFire);
      this.checkIsStillValid();
   }

   public boolean isSlotZeroOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_0_OCCUPIED);
   }

   public void setSlotZeroOccupied(boolean slotZeroOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_0_OCCUPIED, slotZeroOccupied);
      this.checkIsStillValid();
   }

   public boolean isSlotOneOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_1_OCCUPIED);
   }

   public void setSlotOneOccupied(boolean slotOneOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_1_OCCUPIED, slotOneOccupied);
      this.checkIsStillValid();
   }

   public boolean isSlotTwoOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_2_OCCUPIED);
   }

   public void setSlotTwoOccupied(boolean slotTwoOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_2_OCCUPIED, slotTwoOccupied);
      this.checkIsStillValid();
   }

   public boolean isSlotThreeOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_3_OCCUPIED);
   }

   public void setSlotThreeOccupied(boolean slotThreeOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_3_OCCUPIED, slotThreeOccupied);
      this.checkIsStillValid();
   }

   public boolean isSlotFourOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_4_OCCUPIED);
   }

   public void setSlotFourOccupied(boolean slotFourOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_4_OCCUPIED, slotFourOccupied);
      this.checkIsStillValid();
   }

   public boolean isSlotFiveOccupied() {
      return (Boolean)this.data.get(StateValue.SLOT_5_OCCUPIED);
   }

   public void setSlotFiveOccupied(boolean slotFiveOccupied) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SLOT_5_OCCUPIED, slotFiveOccupied);
      this.checkIsStillValid();
   }

   public boolean isSnowy() {
      return (Boolean)this.data.get(StateValue.SNOWY);
   }

   public void setSnowy(boolean snowy) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SNOWY, snowy);
      this.checkIsStillValid();
   }

   public int getStage() {
      return (Integer)this.data.get(StateValue.STAGE);
   }

   public void setStage(int stage) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.STAGE, stage);
      this.checkIsStillValid();
   }

   public South getSouth() {
      return (South)this.data.get(StateValue.SOUTH);
   }

   public void setSouth(South south) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SOUTH, south);
      this.checkIsStillValid();
   }

   public Thickness getThickness() {
      return (Thickness)this.data.get(StateValue.THICKNESS);
   }

   public void setThickness(Thickness thickness) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.THICKNESS, thickness);
      this.checkIsStillValid();
   }

   public Tilt getTilt() {
      return (Tilt)this.data.get(StateValue.TILT);
   }

   public void setTilt(Tilt tilt) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.TILT, tilt);
      this.checkIsStillValid();
   }

   public boolean isTriggered() {
      return (Boolean)this.data.get(StateValue.TRIGGERED);
   }

   public void setTriggered(boolean triggered) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.TRIGGERED, triggered);
      this.checkIsStillValid();
   }

   public Type getTypeData() {
      return (Type)this.data.get(StateValue.TYPE);
   }

   public void setTypeData(Type type) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.TYPE, type);
      this.checkIsStillValid();
   }

   public boolean isUnstable() {
      return (Boolean)this.data.get(StateValue.UNSTABLE);
   }

   public void setUnstable(boolean unstable) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.UNSTABLE, unstable);
      this.checkIsStillValid();
   }

   public boolean isUp() {
      return (Boolean)this.data.get(StateValue.UP);
   }

   public void setUp(boolean up) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.UP, up);
      this.checkIsStillValid();
   }

   public VerticalDirection getVerticalDirection() {
      return (VerticalDirection)this.data.get(StateValue.VERTICAL_DIRECTION);
   }

   public void setVerticalDirection(VerticalDirection verticalDirection) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.VERTICAL_DIRECTION, verticalDirection);
      this.checkIsStillValid();
   }

   public boolean isWaterlogged() {
      return (Boolean)this.data.get(StateValue.WATERLOGGED);
   }

   public void setWaterlogged(boolean waterlogged) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.WATERLOGGED, waterlogged);
      this.checkIsStillValid();
   }

   public East getEast() {
      return (East)this.data.get(StateValue.EAST);
   }

   public void setEast(East west) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.EAST, west);
      this.checkIsStillValid();
   }

   public West getWest() {
      return (West)this.data.get(StateValue.WEST);
   }

   public void setWest(West west) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.WEST, west);
      this.checkIsStillValid();
   }

   public Bloom getBloom() {
      return (Bloom)this.data.get(StateValue.BLOOM);
   }

   public void setBloom(Bloom bloom) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.BLOOM, bloom);
      this.checkIsStillValid();
   }

   public boolean isCracked() {
      return (Boolean)this.data.get(StateValue.CRACKED);
   }

   public void setCracked(boolean cracked) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CRACKED, cracked);
      this.checkIsStillValid();
   }

   public boolean isCrafting() {
      return (Boolean)this.data.get(StateValue.CRAFTING);
   }

   public void setCrafting(boolean crafting) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CRAFTING, crafting);
      this.checkIsStillValid();
   }

   public TrialSpawnerState getTrialSpawnerState() {
      return (TrialSpawnerState)this.data.get(StateValue.TRIAL_SPAWNER_STATE);
   }

   public void setTrialSpawnerState(TrialSpawnerState trialSpawnerState) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.TRIAL_SPAWNER_STATE, trialSpawnerState);
      this.checkIsStillValid();
   }

   @ApiStatus.Obsolete
   public CreakingHeartState getCreaking() {
      return (CreakingHeartState)this.data.get(StateValue.CREAKING);
   }

   @ApiStatus.Obsolete
   public void setCreaking(CreakingHeartState creakingHeartState) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CREAKING, creakingHeartState);
      this.checkIsStillValid();
   }

   @ApiStatus.Obsolete
   public boolean isActive() {
      return (Boolean)this.data.get(StateValue.ACTIVE);
   }

   @ApiStatus.Obsolete
   public void setActive(boolean active) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.ACTIVE, active);
      this.checkIsStillValid();
   }

   public boolean isNatural() {
      return (Boolean)this.data.get(StateValue.NATURAL);
   }

   public void setNatural(boolean natural) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.NATURAL, natural);
      this.checkIsStillValid();
   }

   public int getSegmentAmount() {
      return (Integer)this.data.get(StateValue.SEGMENT_AMOUNT);
   }

   public void setSegmentAmount(int segmentAmount) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.SEGMENT_AMOUNT, segmentAmount);
      this.checkIsStillValid();
   }

   public CreakingHeartState getCreakingHeartState() {
      return (CreakingHeartState)this.data.get(StateValue.CREAKING_HEART_STATE);
   }

   public void setCreakingHeartState(CreakingHeartState creakingHeartState) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.CREAKING_HEART_STATE, creakingHeartState);
      this.checkIsStillValid();
   }

   public boolean isMap() {
      return (Boolean)this.data.get(StateValue.MAP);
   }

   public void setMap(boolean map) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.MAP, map);
      this.checkIsStillValid();
   }

   public int getHydration() {
      return (Integer)this.data.get(StateValue.HYDRATION);
   }

   public void setHydration(int hydration) {
      this.checkIfCloneNeeded();
      this.data.put(StateValue.HYDRATION, hydration);
      this.checkIsStillValid();
   }

   private void checkIfCloneNeeded() {
      if (!this.hasClonedData) {
         this.data = new HashMap(this.data);
         this.hasClonedData = true;
      }

   }

   private void checkIsStillValid() {
      int oldGlobalID = this.globalID;
      this.globalID = this.getGlobalIdNoCache();
      if (this.globalID == -1) {
         WrappedBlockState blockState = ((WrappedBlockState)BY_ID[this.mappingsIndex].getOrDefault(oldGlobalID, AIR)).clone();
         this.type = blockState.type;
         this.globalID = blockState.globalID;
         this.data = new HashMap(blockState.data);
         if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
            PacketEvents.getAPI().getLogManager().warn("Attempt to modify an unknown property for this game version and block!");
            PacketEvents.getAPI().getLogManager().warn("Block: " + this.type.getName());
            Iterator var3 = this.data.entrySet().iterator();

            while(var3.hasNext()) {
               Entry<StateValue, Object> entry = (Entry)var3.next();
               PacketEvents.getAPI().getLogManager().warn(entry.getKey() + ": " + entry.getValue());
            }

            (new IllegalStateException("An invalid modification was made to a block!")).printStackTrace();
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public Map<StateValue, Object> getInternalData() {
      return this.data;
   }

   public boolean hasProperty(StateValue property) {
      return this.data.containsKey(property);
   }

   public int getGlobalId() {
      return this.globalID;
   }

   private int getGlobalIdNoCache() {
      return (Integer)INTO_ID[this.mappingsIndex].getOrDefault(this, -1);
   }

   public String toString() {
      return (String)INTO_STRING[this.mappingsIndex].get(this);
   }

   @ApiStatus.Internal
   public static void ensureLoad() {
      if (PRELOAD_BLOCK_STATE_MAPPINGS) {
         Logger logger = PacketEvents.getAPI().getLogger();
         logger.info("Preloading block mappings...");
         long start = System.nanoTime();
         Map<Map<StateValue, Object>, WrappedBlockState.StateCacheValue> cache = new HashMap();
         loadLegacy(cache);
         ClientVersion[] var4 = MAPPING_VERSION_STEPS;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ClientVersion version = var4[var6];
            loadModern(cache, version);
         }

         double timeDiff = (double)(System.nanoTime() - start) / 1000000.0D;
         logger.info("Finish preloading block mappings in " + timeDiff + "ms");
      }
   }

   static {
      MAPPING_VERSION_STEPS = new ClientVersion[]{ClientVersion.V_1_13, ClientVersion.V_1_13_2, ClientVersion.V_1_14, ClientVersion.V_1_15, ClientVersion.V_1_16, ClientVersion.V_1_16_2, ClientVersion.V_1_17, ClientVersion.V_1_19, ClientVersion.V_1_19_3, ClientVersion.V_1_19_4, ClientVersion.V_1_20, ClientVersion.V_1_20_2, ClientVersion.V_1_20_3, ClientVersion.V_1_20_5, ClientVersion.V_1_21_2, ClientVersion.V_1_21_4, ClientVersion.V_1_21_5, ClientVersion.V_1_21_6, ClientVersion.V_1_21_9};
      PRELOAD_BLOCK_STATE_MAPPINGS = Boolean.getBoolean("packetevents.mappings.preload");
      ClientVersion[] versions = ClientVersion.values();
      MAPPING_INDEXES = new byte[versions.length];
      MAPPING_VERSIONS = new ClientVersion[versions.length];
      ClientVersion mappingVersion = versions[0];
      int i = 0;

      for(int j = 0; i < versions.length; ++i) {
         ClientVersion version = versions[i];
         if (j < MAPPING_VERSION_STEPS.length && version == MAPPING_VERSION_STEPS[j]) {
            ++j;
            mappingVersion = version;
         }

         MAPPING_INDEXES[version.ordinal()] = (byte)(1 + j);
         MAPPING_VERSIONS[version.ordinal()] = mappingVersion;
      }

      HIGHEST_MAPPING_INDEX = MAPPING_INDEXES[versions.length - 1];
      AIR = new WrappedBlockState(StateTypes.AIR, new EnumMap(StateValue.class), 0, (byte)0);
      BY_STRING = new Map[HIGHEST_MAPPING_INDEX + 1];
      BY_ID = new Map[HIGHEST_MAPPING_INDEX + 1];
      INTO_STRING = new Map[HIGHEST_MAPPING_INDEX + 1];
      INTO_ID = new Map[HIGHEST_MAPPING_INDEX + 1];
      DEFAULT_STATES = new Map[HIGHEST_MAPPING_INDEX + 1];
      STRING_UPDATER = new HashMap();
      STRING_UPDATER.put("grass_path", "dirt_path");
      Arrays.fill(BY_STRING, Collections.emptyMap());
      Arrays.fill(BY_ID, Collections.emptyMap());
      Arrays.fill(INTO_STRING, Collections.emptyMap());
      Arrays.fill(INTO_ID, Collections.emptyMap());
      Arrays.fill(DEFAULT_STATES, Collections.emptyMap());
      String airName = AIR.getType().getMapped().getName().getKey();
      BY_STRING[0] = Collections.singletonMap(airName, AIR);
      BY_ID[0] = Collections.singletonMap(AIR.getGlobalId(), AIR);
      INTO_STRING[0] = Collections.singletonMap(AIR, airName);
      INTO_ID[0] = Collections.singletonMap(AIR, AIR.getGlobalId());
      DEFAULT_STATES[0] = Collections.singletonMap(AIR.getType(), AIR);
   }

   private static final class StateCacheValue {
      public static final WrappedBlockState.StateCacheValue EMPTY = new WrappedBlockState.StateCacheValue(Collections.emptyMap());
      private final Map<StateValue, Object> map;
      private String string;

      public StateCacheValue(Map<StateValue, Object> map) {
         this.map = map;
      }

      public String getString() {
         if (this.string == null) {
            StringBuilder builder = new StringBuilder();
            Iterator var2 = this.map.entrySet().iterator();

            while(var2.hasNext()) {
               Entry<StateValue, Object> entry = (Entry)var2.next();
               builder.append(((StateValue)entry.getKey()).getName()).append('=').append(String.valueOf(entry.getValue()).toLowerCase(Locale.ROOT)).append(',');
            }

            this.string = builder.length() == 0 ? "" : '[' + builder.substring(0, builder.length() - 1) + ']';
         }

         return this.string;
      }
   }
}
