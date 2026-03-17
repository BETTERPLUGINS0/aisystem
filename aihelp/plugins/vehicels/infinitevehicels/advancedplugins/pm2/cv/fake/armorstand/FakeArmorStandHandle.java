package advancedplugins.pm2.cv.fake.armorstand;

import advancedplugins.pm2.cv.api.enums.EnumRotableLimb;
import advancedplugins.pm2.cv.api.enums.EnumStandSlot;
import advancedplugins.pm2.cv.enums.EnumStandProperty;
import advancedplugins.pm2.cv.fake.FakeEntityHandle;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FakeArmorStandHandle extends FakeEntityHandle<EnumStandProperty> {
   void applyEquipment(@NotNull EnumStandSlot slot, @Nullable ItemStack value);

   void sendEquipment();

   void applyLimbRotation(@NotNull EnumRotableLimb limb, float x, float y, float z);
}
