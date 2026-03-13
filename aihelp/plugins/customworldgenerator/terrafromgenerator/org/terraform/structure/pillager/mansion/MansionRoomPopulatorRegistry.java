package org.terraform.structure.pillager.mansion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.structure.pillager.mansion.ground.MansionGrandStairwayPopulator;
import org.terraform.structure.pillager.mansion.ground.MansionGroundFloorHallwayPopulator;
import org.terraform.structure.pillager.mansion.ground.MansionGroundLevelBrewingRoomPopulator;
import org.terraform.structure.pillager.mansion.ground.MansionGroundLevelDiningRoomPopulator;
import org.terraform.structure.pillager.mansion.ground.MansionGroundLevelForgePopulator;
import org.terraform.structure.pillager.mansion.ground.MansionGroundLevelKitchenPopulator;
import org.terraform.structure.pillager.mansion.ground.MansionGroundLevelLibraryPopulator;
import org.terraform.structure.pillager.mansion.ground.MansionGroundLevelMushroomFarmPopulator;
import org.terraform.structure.pillager.mansion.ground.MansionGroundLevelWarroomPopulator;
import org.terraform.structure.pillager.mansion.secondfloor.MansionSecondFloorBedroomPopulator;
import org.terraform.structure.pillager.mansion.secondfloor.MansionSecondFloorBunkPopulator;
import org.terraform.structure.pillager.mansion.secondfloor.MansionSecondFloorHallwayPopulator;
import org.terraform.structure.pillager.mansion.secondfloor.MansionSecondFloorLoungePopulator;
import org.terraform.structure.pillager.mansion.secondfloor.MansionSecondFloorPianoRoomPopulator;
import org.terraform.structure.pillager.mansion.secondfloor.MansionSecondFloorStoreroomPopulator;
import org.terraform.structure.pillager.mansion.secondfloor.MansionSecondFloorStudyPopulator;
import org.terraform.structure.room.CubeRoom;

public enum MansionRoomPopulatorRegistry {
   GROUND_3_3(new MansionRoomPopulator[]{new MansionGrandStairwayPopulator((CubeRoom)null, (HashMap)null)}),
   GROUND_2_2(new MansionRoomPopulator[]{new MansionGroundLevelLibraryPopulator((CubeRoom)null, (HashMap)null), new MansionGroundLevelWarroomPopulator((CubeRoom)null, (HashMap)null)}),
   GROUND_1_2(new MansionRoomPopulator[]{new MansionGroundLevelKitchenPopulator((CubeRoom)null, (HashMap)null), new MansionGroundLevelMushroomFarmPopulator((CubeRoom)null, (HashMap)null), new MansionGroundLevelForgePopulator((CubeRoom)null, (HashMap)null)}),
   GROUND_2_1(new MansionRoomPopulator[]{new MansionGroundLevelDiningRoomPopulator((CubeRoom)null, (HashMap)null), new MansionGroundLevelBrewingRoomPopulator((CubeRoom)null, (HashMap)null)}),
   GROUND_1_1(new MansionRoomPopulator[]{new MansionGroundFloorHallwayPopulator((CubeRoom)null, (HashMap)null)}),
   SECOND_3_3(new MansionRoomPopulator[]{new MansionGrandStairwayPopulator((CubeRoom)null, (HashMap)null)}),
   SECOND_2_2(new MansionRoomPopulator[]{new MansionSecondFloorBedroomPopulator((CubeRoom)null, (HashMap)null), new MansionSecondFloorStudyPopulator((CubeRoom)null, (HashMap)null)}),
   SECOND_1_2(new MansionRoomPopulator[]{new MansionSecondFloorLoungePopulator((CubeRoom)null, (HashMap)null), new MansionSecondFloorPianoRoomPopulator((CubeRoom)null, (HashMap)null)}),
   SECOND_2_1(new MansionRoomPopulator[]{new MansionSecondFloorBunkPopulator((CubeRoom)null, (HashMap)null), new MansionSecondFloorStoreroomPopulator((CubeRoom)null, (HashMap)null)}),
   SECOND_1_1(new MansionRoomPopulator[]{new MansionSecondFloorHallwayPopulator((CubeRoom)null, (HashMap)null)});

   @NotNull
   final ArrayList<MansionRoomPopulator> populators = new ArrayList();

   private MansionRoomPopulatorRegistry(MansionRoomPopulator... param3) {
      this.populators.addAll(Arrays.asList(populators));
   }

   @Nullable
   public static MansionRoomPopulatorRegistry getByRoomSize(@NotNull MansionRoomSize size, boolean isGround) {
      if (isGround) {
         if (size.getWidthX() == 3 && size.getWidthZ() == 3) {
            return GROUND_3_3;
         }

         if (size.getWidthX() == 2 && size.getWidthZ() == 2) {
            return GROUND_2_2;
         }

         if (size.getWidthX() == 1 && size.getWidthZ() == 2) {
            return GROUND_1_2;
         }

         if (size.getWidthX() == 2 && size.getWidthZ() == 1) {
            return GROUND_2_1;
         }

         if (size.getWidthX() == 1 && size.getWidthZ() == 1) {
            return GROUND_1_1;
         }
      } else {
         if (size.getWidthX() == 3 && size.getWidthZ() == 3) {
            return SECOND_3_3;
         }

         if (size.getWidthX() == 2 && size.getWidthZ() == 2) {
            return SECOND_2_2;
         }

         if (size.getWidthX() == 1 && size.getWidthZ() == 2) {
            return SECOND_1_2;
         }

         if (size.getWidthX() == 2 && size.getWidthZ() == 1) {
            return SECOND_2_1;
         }

         if (size.getWidthX() == 1 && size.getWidthZ() == 1) {
            return SECOND_1_1;
         }
      }

      return null;
   }

   @NotNull
   public ArrayList<MansionRoomPopulator> getPopulators() {
      return new ArrayList(this.populators);
   }

   // $FF: synthetic method
   private static MansionRoomPopulatorRegistry[] $values() {
      return new MansionRoomPopulatorRegistry[]{GROUND_3_3, GROUND_2_2, GROUND_1_2, GROUND_2_1, GROUND_1_1, SECOND_3_3, SECOND_2_2, SECOND_1_2, SECOND_2_1, SECOND_1_1};
   }
}
