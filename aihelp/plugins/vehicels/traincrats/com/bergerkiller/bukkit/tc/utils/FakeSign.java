package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.bases.BlockStateBase;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.generated.org.bukkit.block.SignHandle;
import com.bergerkiller.mountiplex.MountiplexUtil;
import com.bergerkiller.mountiplex.dep.org.objectweb.asm.FieldVisitor;
import com.bergerkiller.mountiplex.dep.org.objectweb.asm.Label;
import com.bergerkiller.mountiplex.dep.org.objectweb.asm.MethodVisitor;
import com.bergerkiller.mountiplex.dep.org.objectweb.asm.Opcodes;
import com.bergerkiller.mountiplex.reflection.util.ExtendedClassWriter;
import com.bergerkiller.mountiplex.reflection.util.asm.MPLType;
import java.lang.reflect.Constructor;
import java.util.function.Function;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;

public abstract class FakeSign extends BlockStateBase implements Sign {
   private static final Function<Block, FakeSign> constructor = (Function)LogicUtil.tryCreate(FakeSign::createFakeSignConstructor, (err) -> {
      return (block) -> {
         throw new IllegalStateException("Failed to create FakeSign implementation", err);
      };
   });
   public FakeSign.Handler handler = null;

   protected FakeSign(Block block) {
      super(block);
   }

   public static FakeSign create(Block signBlock) {
      if (signBlock == null) {
         throw new IllegalArgumentException("Sign block is null");
      } else {
         return (FakeSign)constructor.apply(signBlock);
      }
   }

   public void setHandler(FakeSign.Handler handler) {
      this.handler = handler;
   }

   public FakeSign.Handler getHandler() {
      return this.handler;
   }

   /** @deprecated */
   @Deprecated
   public String[] getLines() {
      String[] lines = new String[4];

      for(int i = 0; i < 4; ++i) {
         lines[i] = this.handler.getFrontLine(i);
      }

      return lines;
   }

   /** @deprecated */
   @Deprecated
   public String getLine(int index) {
      return this.handler.getFrontLine(index);
   }

   /** @deprecated */
   @Deprecated
   public void setLine(int index, String text) {
      this.handler.setFrontLine(index, text);
   }

   public PersistentDataContainer getPersistentDataContainer() {
      BlockState state = this.getBlock().getState();
      return state instanceof PersistentDataHolder ? ((PersistentDataHolder)state).getPersistentDataContainer() : null;
   }

   public boolean isEditable() {
      return false;
   }

   public void setEditable(boolean editable) {
   }

   public DyeColor getColor() {
      return null;
   }

   public void setColor(DyeColor arg0) {
   }

   public boolean isGlowingText() {
      return false;
   }

   public void setGlowingText(boolean arg0) {
   }

   public boolean update() {
      return this.handler.update(false, true);
   }

   public boolean update(boolean force) {
      return this.handler.update(force, true);
   }

   public boolean update(boolean force, boolean applyPhysics) {
      return this.handler.update(force, applyPhysics);
   }

   private static Function<Block, FakeSign> createFakeSignConstructor() {
      Class frontSideClass;
      Class backSideClass;
      if (CommonCapabilities.HAS_SIGN_BACK_TEXT) {
         frontSideClass = generateFakeSignSide("Front");
         backSideClass = generateFakeSignSide("Back");
      } else {
         backSideClass = null;
         frontSideClass = null;
      }

      ExtendedClassWriter<FakeSign> classWriter = ExtendedClassWriter.builder(FakeSign.class).setExactName(FakeSign.class.getName() + "$Impl").build();
      String ctorDesc = "(" + MPLType.getDescriptor(Block.class) + ")V";
      MethodVisitor methodVisitor;
      if (CommonCapabilities.HAS_SIGN_BACK_TEXT) {
         Class<?> signSideType = CommonUtil.getClass("org.bukkit.block.sign.SignSide");
         String signSideDesc = MPLType.getDescriptor(signSideType);
         String fakeSignDesc = MPLType.getDescriptor(FakeSign.class);
         FieldVisitor fieldVisitor = classWriter.visitField(18, "front", signSideDesc, (String)null, (Object)null);
         fieldVisitor.visitEnd();
         fieldVisitor = classWriter.visitField(18, "back", signSideDesc, (String)null, (Object)null);
         fieldVisitor.visitEnd();
         methodVisitor = classWriter.visitMethod(1, "<init>", ctorDesc, (String)null, (String[])null);
         methodVisitor.visitCode();
         methodVisitor.visitVarInsn(25, 0);
         methodVisitor.visitVarInsn(25, 1);
         methodVisitor.visitMethodInsn(183, MPLType.getInternalName(FakeSign.class), "<init>", ctorDesc, false);
         methodVisitor.visitVarInsn(25, 0);
         methodVisitor.visitTypeInsn(187, MPLType.getInternalName(frontSideClass));
         methodVisitor.visitInsn(89);
         methodVisitor.visitVarInsn(25, 0);
         methodVisitor.visitMethodInsn(183, MPLType.getInternalName(frontSideClass), "<init>", "(" + fakeSignDesc + ")V", false);
         methodVisitor.visitFieldInsn(181, classWriter.getInternalName(), "front", signSideDesc);
         methodVisitor.visitVarInsn(25, 0);
         methodVisitor.visitTypeInsn(187, MPLType.getInternalName(backSideClass));
         methodVisitor.visitInsn(89);
         methodVisitor.visitVarInsn(25, 0);
         methodVisitor.visitMethodInsn(183, MPLType.getInternalName(backSideClass), "<init>", "(" + fakeSignDesc + ")V", false);
         methodVisitor.visitFieldInsn(181, classWriter.getInternalName(), "back", signSideDesc);
         methodVisitor.visitInsn(177);
         methodVisitor.visitMaxs(4, 2);
         methodVisitor.visitEnd();
         Class<?> sideType = CommonUtil.getClass("org.bukkit.block.sign.Side");
         String sideDesc = MPLType.getDescriptor(sideType);
         methodVisitor = classWriter.visitMethod(1, "getSide", "(" + sideDesc + ")" + signSideDesc, (String)null, (String[])null);
         methodVisitor.visitCode();
         methodVisitor.visitVarInsn(25, 1);
         methodVisitor.visitFieldInsn(178, MPLType.getInternalName(sideType), "FRONT", sideDesc);
         Label label0 = new Label();
         methodVisitor.visitJumpInsn(166, label0);
         methodVisitor.visitVarInsn(25, 0);
         methodVisitor.visitFieldInsn(180, classWriter.getInternalName(), "front", signSideDesc);
         Label label1 = new Label();
         methodVisitor.visitJumpInsn(167, label1);
         methodVisitor.visitLabel(label0);
         methodVisitor.visitFrame(3, 0, (Object[])null, 0, (Object[])null);
         methodVisitor.visitVarInsn(25, 0);
         methodVisitor.visitFieldInsn(180, classWriter.getInternalName(), "back", signSideDesc);
         methodVisitor.visitLabel(label1);
         methodVisitor.visitFrame(4, 0, (Object[])null, 1, new Object[]{MPLType.getInternalName(signSideType)});
         methodVisitor.visitInsn(176);
         methodVisitor.visitMaxs(2, 2);
         methodVisitor.visitEnd();
      } else {
         methodVisitor = classWriter.visitMethod(1, "<init>", ctorDesc, (String)null, (String[])null);
         methodVisitor.visitCode();
         methodVisitor.visitVarInsn(25, 0);
         methodVisitor.visitVarInsn(25, 1);
         methodVisitor.visitMethodInsn(183, MPLType.getInternalName(FakeSign.class), "<init>", ctorDesc, false);
         methodVisitor.visitInsn(177);
         methodVisitor.visitMaxs(2, 2);
         methodVisitor.visitEnd();
      }

      Constructor<? extends FakeSign> ctor = classWriter.generateConstructor(new Class[]{Block.class});
      return (block) -> {
         try {
            return (FakeSign)ctor.newInstance(block);
         } catch (Throwable var3) {
            throw MountiplexUtil.uncheckedRethrow(var3);
         }
      };
   }

   private static Class<?> generateFakeSignSide(String sideName) {
      Class<?> signSideType = CommonUtil.getClass("org.bukkit.block.sign.SignSide");
      ExtendedClassWriter<?> classWriter = ExtendedClassWriter.builder(signSideType).setExactName(FakeSign.class.getName() + "$FakeSignSide" + sideName).build();
      String fakeSignName = MPLType.getInternalName(FakeSign.class);
      String fakeSignDesc = MPLType.getDescriptor(FakeSign.class);
      FieldVisitor fieldVisitor = classWriter.visitField(18, "fakeSign", fakeSignDesc, (String)null, (Object)null);
      fieldVisitor.visitEnd();
      MethodVisitor methodVisitor = classWriter.visitMethod(1, "<init>", "(" + fakeSignDesc + ")V", (String)null, (String[])null);
      methodVisitor.visitCode();
      methodVisitor.visitVarInsn(25, 0);
      methodVisitor.visitMethodInsn(183, "java/lang/Object", "<init>", "()V", false);
      methodVisitor.visitVarInsn(25, 0);
      methodVisitor.visitVarInsn(25, 1);
      methodVisitor.visitFieldInsn(181, classWriter.getInternalName(), "fakeSign", fakeSignDesc);
      methodVisitor.visitInsn(177);
      methodVisitor.visitMaxs(2, 2);
      methodVisitor.visitEnd();
      methodVisitor = classWriter.visitMethod(1, "getLines", "()[Ljava/lang/String;", (String)null, (String[])null);
      methodVisitor.visitCode();
      methodVisitor.visitInsn(7);
      methodVisitor.visitTypeInsn(189, "java/lang/String");
      methodVisitor.visitVarInsn(58, 1);
      methodVisitor.visitInsn(3);
      methodVisitor.visitVarInsn(54, 2);
      Label label0 = new Label();
      methodVisitor.visitLabel(label0);
      methodVisitor.visitFrame(1, 2, new Object[]{"[Ljava/lang/String;", Opcodes.INTEGER}, 0, (Object[])null);
      methodVisitor.visitVarInsn(21, 2);
      methodVisitor.visitInsn(7);
      Label label1 = new Label();
      methodVisitor.visitJumpInsn(162, label1);
      methodVisitor.visitVarInsn(25, 1);
      methodVisitor.visitVarInsn(21, 2);
      methodVisitor.visitVarInsn(25, 0);
      methodVisitor.visitVarInsn(21, 2);
      methodVisitor.visitMethodInsn(182, classWriter.getInternalName(), "getLine", "(I)Ljava/lang/String;", false);
      methodVisitor.visitInsn(83);
      methodVisitor.visitIincInsn(2, 1);
      methodVisitor.visitJumpInsn(167, label0);
      methodVisitor.visitLabel(label1);
      methodVisitor.visitFrame(2, 1, (Object[])null, 0, (Object[])null);
      methodVisitor.visitVarInsn(25, 1);
      methodVisitor.visitInsn(176);
      methodVisitor.visitMaxs(4, 3);
      methodVisitor.visitEnd();
      methodVisitor = classWriter.visitMethod(1, "getLine", "(I)Ljava/lang/String;", (String)null, new String[]{"java/lang/IndexOutOfBoundsException"});
      methodVisitor.visitCode();
      methodVisitor.visitVarInsn(25, 0);
      methodVisitor.visitFieldInsn(180, classWriter.getInternalName(), "fakeSign", fakeSignDesc);
      methodVisitor.visitMethodInsn(182, fakeSignName, "getHandler", "()" + MPLType.getDescriptor(FakeSign.Handler.class), false);
      methodVisitor.visitVarInsn(21, 1);
      methodVisitor.visitMethodInsn(185, MPLType.getInternalName(FakeSign.Handler.class), "get" + sideName + "Line", "(I)Ljava/lang/String;", true);
      methodVisitor.visitInsn(176);
      methodVisitor.visitMaxs(2, 2);
      methodVisitor.visitEnd();
      methodVisitor = classWriter.visitMethod(1, "setLine", "(ILjava/lang/String;)V", (String)null, new String[]{"java/lang/IndexOutOfBoundsException"});
      methodVisitor.visitCode();
      methodVisitor.visitVarInsn(25, 0);
      methodVisitor.visitFieldInsn(180, classWriter.getInternalName(), "fakeSign", fakeSignDesc);
      methodVisitor.visitMethodInsn(182, fakeSignName, "getHandler", "()" + MPLType.getDescriptor(FakeSign.Handler.class), false);
      methodVisitor.visitVarInsn(21, 1);
      methodVisitor.visitVarInsn(25, 2);
      methodVisitor.visitMethodInsn(185, MPLType.getInternalName(FakeSign.Handler.class), "set" + sideName + "Line", "(ILjava/lang/String;)V", true);
      methodVisitor.visitInsn(177);
      methodVisitor.visitMaxs(3, 3);
      methodVisitor.visitEnd();
      methodVisitor = classWriter.visitMethod(1, "isGlowingText", "()Z", (String)null, (String[])null);
      methodVisitor.visitCode();
      methodVisitor.visitInsn(3);
      methodVisitor.visitInsn(172);
      methodVisitor.visitMaxs(1, 1);
      methodVisitor.visitEnd();
      methodVisitor = classWriter.visitMethod(1, "setGlowingText", "(Z)V", (String)null, (String[])null);
      methodVisitor.visitCode();
      methodVisitor.visitInsn(177);
      methodVisitor.visitMaxs(0, 2);
      methodVisitor.visitEnd();
      methodVisitor = classWriter.visitMethod(1, "getColor", "()" + MPLType.getDescriptor(DyeColor.class), (String)null, (String[])null);
      methodVisitor.visitCode();
      methodVisitor.visitFieldInsn(178, MPLType.getInternalName(DyeColor.class), "BLACK", MPLType.getDescriptor(DyeColor.class));
      methodVisitor.visitInsn(176);
      methodVisitor.visitMaxs(1, 1);
      methodVisitor.visitEnd();
      methodVisitor = classWriter.visitMethod(1, "setColor", "(" + MPLType.getDescriptor(DyeColor.class) + ")V", (String)null, (String[])null);
      methodVisitor.visitCode();
      methodVisitor.visitInsn(177);
      methodVisitor.visitMaxs(0, 2);
      methodVisitor.visitEnd();
      classWriter.visitEnd();
      return classWriter.generate();
   }

   public interface Handler {
      String getFrontLine(int var1);

      void setFrontLine(int var1, String var2);

      String getBackLine(int var1);

      void setBackLine(int var1, String var2);

      default boolean update(boolean force, boolean applyPhysics) {
         return true;
      }
   }

   public static class HandlerSignFallback implements FakeSign.Handler {
      private final Block signBlock;

      public HandlerSignFallback(Block signBlock) {
         this.signBlock = signBlock;
      }

      private SignHandle accessSign() {
         SignHandle sign = SignHandle.createHandle(BlockUtil.getSign(this.signBlock));
         if (sign == null) {
            throw new IllegalStateException("No sign is set at " + this.signBlock);
         } else {
            return sign;
         }
      }

      public String getFrontLine(int index) {
         return this.accessSign().getFrontLine(index);
      }

      public void setFrontLine(int index, String text) {
         SignHandle sign = this.accessSign();
         sign.setFrontLine(index, text);
         ((Sign)sign.getRaw()).update(true);
      }

      public String getBackLine(int index) {
         return this.accessSign().getBackLine(index);
      }

      public void setBackLine(int index, String text) {
         SignHandle sign = this.accessSign();
         sign.setBackLine(index, text);
         ((Sign)sign.getRaw()).update(true);
      }
   }
}
