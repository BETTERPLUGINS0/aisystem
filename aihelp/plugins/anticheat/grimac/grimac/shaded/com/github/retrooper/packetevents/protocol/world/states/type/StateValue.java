package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Attachment;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Axis;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.CopperGolemPose;
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
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.SideChainPart;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.South;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Thickness;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Tilt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.TrialSpawnerState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Type;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.VaultState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.VerticalDirection;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.West;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import java.util.function.Function;

public enum StateValue {
   AGE("age", Integer.TYPE, Integer::parseInt),
   ATTACHED("attached", Boolean.TYPE, Boolean::parseBoolean),
   ATTACHMENT("attachment", Attachment.class, Attachment::valueOf),
   AXIS("axis", Axis.class, Axis::valueOf),
   BERRIES("berries", Boolean.TYPE, Boolean::parseBoolean),
   BITES("bites", Integer.TYPE, Integer::parseInt),
   BLOOM("bloom", Boolean.TYPE, Boolean::parseBoolean),
   BOTTOM("bottom", Boolean.TYPE, Boolean::parseBoolean),
   CANDLES("candles", Integer.TYPE, Integer::parseInt),
   CAN_SUMMON("can_summon", Boolean.TYPE, Boolean::parseBoolean),
   CHARGES("charges", Integer.TYPE, Integer::parseInt),
   CONDITIONAL("conditional", Boolean.TYPE, Boolean::parseBoolean),
   CRACKED("cracked", Boolean.TYPE, Boolean::parseBoolean),
   CRAFTING("crafting", Boolean.TYPE, Boolean::parseBoolean),
   DELAY("delay", Integer.TYPE, Integer::parseInt),
   DISARMED("disarmed", Boolean.TYPE, Boolean::parseBoolean),
   DISTANCE("distance", Integer.TYPE, Integer::parseInt),
   DOWN("down", Boolean.TYPE, Boolean::parseBoolean),
   DRAG("drag", Boolean.TYPE, Boolean::parseBoolean),
   DUSTED("dusted", Integer.TYPE, Integer::parseInt),
   EAST("east", East.class, East::valueOf),
   EGGS("eggs", Integer.TYPE, Integer::parseInt),
   ENABLED("enabled", Boolean.TYPE, Boolean::parseBoolean),
   EXTENDED("extended", Boolean.TYPE, Boolean::parseBoolean),
   EYE("eye", Boolean.TYPE, Boolean::parseBoolean),
   FACE("face", Face.class, Face::valueOf),
   FACING("facing", BlockFace.class, BlockFace::valueOf),
   FLOWER_AMOUNT("flower_amount", Integer.TYPE, Integer::parseInt),
   HALF("half", Half.class, Half::valueOf),
   HANGING("hanging", Boolean.TYPE, Boolean::parseBoolean),
   HAS_BOOK("has_book", Boolean.TYPE, Boolean::parseBoolean),
   HAS_BOTTLE_0("has_bottle_0", Boolean.TYPE, Boolean::parseBoolean),
   HAS_BOTTLE_1("has_bottle_1", Boolean.TYPE, Boolean::parseBoolean),
   HAS_BOTTLE_2("has_bottle_2", Boolean.TYPE, Boolean::parseBoolean),
   HAS_RECORD("has_record", Boolean.TYPE, Boolean::parseBoolean),
   HATCH("hatch", Integer.TYPE, Integer::parseInt),
   HINGE("hinge", Hinge.class, Hinge::valueOf),
   HONEY_LEVEL("honey_level", Integer.TYPE, Integer::parseInt),
   IN_WALL("in_wall", Boolean.TYPE, Boolean::parseBoolean),
   INSTRUMENT("instrument", Instrument.class, Instrument::valueOf),
   INVERTED("inverted", Boolean.TYPE, Boolean::parseBoolean),
   LAYERS("layers", Integer.TYPE, Integer::parseInt),
   LEAVES("leaves", Leaves.class, Leaves::valueOf),
   LEVEL("level", Integer.TYPE, Integer::parseInt),
   LIT("lit", Boolean.TYPE, Boolean::parseBoolean),
   TIP("tip", Boolean.TYPE, Boolean::parseBoolean),
   LOCKED("locked", Boolean.TYPE, Boolean::parseBoolean),
   MODE("mode", Mode.class, Mode::valueOf),
   MOISTURE("moisture", Integer.TYPE, Integer::parseInt),
   NORTH("north", North.class, North::valueOf),
   NOTE("note", Integer.TYPE, Integer::parseInt),
   OCCUPIED("occupied", Boolean.TYPE, Boolean::parseBoolean),
   OMINOUS("ominous", Boolean.TYPE, Boolean::parseBoolean),
   OPEN("open", Boolean.TYPE, Boolean::parseBoolean),
   ORIENTATION("orientation", Orientation.class, Orientation::valueOf),
   PART("part", Part.class, Part::valueOf),
   PERSISTENT("persistent", Boolean.TYPE, Boolean::parseBoolean),
   PICKLES("pickles", Integer.TYPE, Integer::parseInt),
   POWER("power", Integer.TYPE, Integer::parseInt),
   POWERED("powered", Boolean.TYPE, Boolean::parseBoolean),
   ROTATION("rotation", Integer.TYPE, Integer::parseInt),
   SCULK_SENSOR_PHASE("sculk_sensor_phase", SculkSensorPhase.class, SculkSensorPhase::valueOf),
   SHAPE("shape", Shape.class, Shape::valueOf),
   SHORT("short", Boolean.TYPE, Boolean::parseBoolean),
   SHRIEKING("shrieking", Boolean.TYPE, Boolean::parseBoolean),
   SIGNAL_FIRE("signal_fire", Boolean.TYPE, Boolean::parseBoolean),
   SLOT_0_OCCUPIED("slot_0_occupied", Boolean.TYPE, Boolean::parseBoolean),
   SLOT_1_OCCUPIED("slot_1_occupied", Boolean.TYPE, Boolean::parseBoolean),
   SLOT_2_OCCUPIED("slot_2_occupied", Boolean.TYPE, Boolean::parseBoolean),
   SLOT_3_OCCUPIED("slot_3_occupied", Boolean.TYPE, Boolean::parseBoolean),
   SLOT_4_OCCUPIED("slot_4_occupied", Boolean.TYPE, Boolean::parseBoolean),
   SLOT_5_OCCUPIED("slot_5_occupied", Boolean.TYPE, Boolean::parseBoolean),
   SNOWY("snowy", Boolean.TYPE, Boolean::parseBoolean),
   STAGE("stage", Integer.TYPE, Integer::parseInt),
   SOUTH("south", South.class, South::valueOf),
   THICKNESS("thickness", Thickness.class, Thickness::valueOf),
   TILT("tilt", Tilt.class, Tilt::valueOf),
   TRIAL_SPAWNER_STATE("trial_spawner_state", TrialSpawnerState.class, TrialSpawnerState::valueOf),
   TRIGGERED("triggered", Boolean.TYPE, Boolean::parseBoolean),
   TYPE("type", Type.class, Type::valueOf),
   UNSTABLE("unstable", Boolean.TYPE, Boolean::parseBoolean),
   UP("up", Boolean.TYPE, Boolean::parseBoolean),
   VAULT_STATE("vault_state", VaultState.class, VaultState::valueOf),
   VERTICAL_DIRECTION("vertical_direction", VerticalDirection.class, VerticalDirection::valueOf),
   WATERLOGGED("waterlogged", Boolean.TYPE, Boolean::parseBoolean),
   WEST("west", West.class, West::valueOf),
   @ApiStatus.Obsolete
   CREAKING("creaking", CreakingHeartState.class, CreakingHeartState::valueOf),
   @ApiStatus.Obsolete
   ACTIVE("active", Boolean.TYPE, Boolean::parseBoolean),
   NATURAL("natural", Boolean.TYPE, Boolean::parseBoolean),
   SEGMENT_AMOUNT("segment_amount", Integer.TYPE, Integer::parseInt),
   CREAKING_HEART_STATE("creaking_heart_state", CreakingHeartState.class, CreakingHeartState::valueOf),
   MAP("map", Boolean.TYPE, Boolean::parseBoolean),
   HYDRATION("hydration", Integer.TYPE, Integer::parseInt),
   SIDE_CHAIN("side_chain", SideChainPart.class, SideChainPart::valueOf),
   COPPER_GOLEM_POSE("copper_golem_pose", CopperGolemPose.class, CopperGolemPose::valueOf);

   public static final Index<String, StateValue> NAME_INDEX = Index.create(StateValue.class, StateValue::getName);
   private final String name;
   private final Class<?> dataClass;
   private final Function<String, Object> parser;

   private StateValue(String name, Class<?> dataClass, Function<String, Object> parser) {
      this.name = name;
      this.dataClass = dataClass;
      this.parser = parser;
   }

   @Nullable
   public static StateValue byName(String name) {
      return (StateValue)NAME_INDEX.value(name);
   }

   public Object parse(String input) {
      return this.parser.apply(input);
   }

   public String getName() {
      return this.name;
   }

   public Class<?> getDataClass() {
      return this.dataClass;
   }

   public Function<String, Object> getParser() {
      return this.parser;
   }

   // $FF: synthetic method
   private static StateValue[] $values() {
      return new StateValue[]{AGE, ATTACHED, ATTACHMENT, AXIS, BERRIES, BITES, BLOOM, BOTTOM, CANDLES, CAN_SUMMON, CHARGES, CONDITIONAL, CRACKED, CRAFTING, DELAY, DISARMED, DISTANCE, DOWN, DRAG, DUSTED, EAST, EGGS, ENABLED, EXTENDED, EYE, FACE, FACING, FLOWER_AMOUNT, HALF, HANGING, HAS_BOOK, HAS_BOTTLE_0, HAS_BOTTLE_1, HAS_BOTTLE_2, HAS_RECORD, HATCH, HINGE, HONEY_LEVEL, IN_WALL, INSTRUMENT, INVERTED, LAYERS, LEAVES, LEVEL, LIT, TIP, LOCKED, MODE, MOISTURE, NORTH, NOTE, OCCUPIED, OMINOUS, OPEN, ORIENTATION, PART, PERSISTENT, PICKLES, POWER, POWERED, ROTATION, SCULK_SENSOR_PHASE, SHAPE, SHORT, SHRIEKING, SIGNAL_FIRE, SLOT_0_OCCUPIED, SLOT_1_OCCUPIED, SLOT_2_OCCUPIED, SLOT_3_OCCUPIED, SLOT_4_OCCUPIED, SLOT_5_OCCUPIED, SNOWY, STAGE, SOUTH, THICKNESS, TILT, TRIAL_SPAWNER_STATE, TRIGGERED, TYPE, UNSTABLE, UP, VAULT_STATE, VERTICAL_DIRECTION, WATERLOGGED, WEST, CREAKING, ACTIVE, NATURAL, SEGMENT_AMOUNT, CREAKING_HEART_STATE, MAP, HYDRATION, SIDE_CHAIN, COPPER_GOLEM_POSE};
   }
}
