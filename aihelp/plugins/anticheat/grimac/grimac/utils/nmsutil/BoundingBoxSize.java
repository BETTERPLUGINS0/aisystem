package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityGuardian;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHorse;
import ac.grim.grimac.utils.data.packetentity.PacketEntitySizeable;
import ac.grim.grimac.utils.data.packetentity.PacketEntityTrackXRot;
import ac.grim.grimac.utils.math.GrimMath;
import lombok.Generated;

public final class BoundingBoxSize {
   public static float getWidth(GrimPlayer player, PacketEntity packetEntity) {
      float width = getWidthMinusBaby(player, packetEntity);
      return width * (packetEntity.isBaby ? getBabyScaleFactor(packetEntity) : 1.0F);
   }

   private static float getWidthMinusBaby(GrimPlayer player, PacketEntity packetEntity) {
      EntityType type = packetEntity.type;
      if (type == EntityTypes.AXOLOTL) {
         return 0.75F;
      } else if (type == EntityTypes.PANDA) {
         return 1.3F;
      } else if (type != EntityTypes.BAT && type != EntityTypes.PARROT && type != EntityTypes.COD && type != EntityTypes.EVOKER_FANGS && type != EntityTypes.TROPICAL_FISH && type != EntityTypes.FROG && type != EntityTypes.COPPER_GOLEM) {
         if (type != EntityTypes.ARMADILLO && type != EntityTypes.BEE && type != EntityTypes.PUFFERFISH && type != EntityTypes.SALMON && type != EntityTypes.SNOW_GOLEM && type != EntityTypes.CAVE_SPIDER) {
            if (type == EntityTypes.WITHER_SKELETON) {
               return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.7F : 0.72F;
            } else if (type != EntityTypes.WITHER_SKULL && type != EntityTypes.SHULKER_BULLET) {
               if (type != EntityTypes.HOGLIN && type != EntityTypes.ZOGLIN) {
                  if (type != EntityTypes.SKELETON_HORSE && type != EntityTypes.ZOMBIE_HORSE && type != EntityTypes.HORSE && type != EntityTypes.DONKEY && type != EntityTypes.MULE) {
                     if (EntityTypes.isTypeInstanceOf(type, EntityTypes.BOAT)) {
                        return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 1.375F : 1.5F;
                     } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_NAUTILUS)) {
                        return 0.875F;
                     } else if (type == EntityTypes.HAPPY_GHAST) {
                        return 4.0F;
                     } else if (type != EntityTypes.CHICKEN && type != EntityTypes.ENDERMITE && type != EntityTypes.SILVERFISH && type != EntityTypes.VEX && type != EntityTypes.TADPOLE) {
                        if (type == EntityTypes.RABBIT) {
                           return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.4F : 0.6F;
                        } else if (type != EntityTypes.CREAKING && type != EntityTypes.STRIDER && type != EntityTypes.COW && type != EntityTypes.SHEEP && type != EntityTypes.MOOSHROOM && type != EntityTypes.PIG && type != EntityTypes.LLAMA && type != EntityTypes.DOLPHIN && type != EntityTypes.WITHER && type != EntityTypes.TRADER_LLAMA && type != EntityTypes.WARDEN && type != EntityTypes.GOAT) {
                           PacketEntitySizeable sizeable;
                           if (type == EntityTypes.PHANTOM) {
                              if (packetEntity instanceof PacketEntitySizeable) {
                                 sizeable = (PacketEntitySizeable)packetEntity;
                                 return 0.9F + (float)sizeable.size * 0.2F;
                              } else {
                                 return 1.5F;
                              }
                           } else if (packetEntity instanceof PacketEntityGuardian) {
                              PacketEntityGuardian packetEntityGuardian = (PacketEntityGuardian)packetEntity;
                              return packetEntityGuardian.isElder ? 1.9975F : 0.85F;
                           } else if (type == EntityTypes.END_CRYSTAL) {
                              return 2.0F;
                           } else if (type == EntityTypes.ENDER_DRAGON) {
                              return 16.0F;
                           } else if (type == EntityTypes.FIREBALL) {
                              return 1.0F;
                           } else if (type == EntityTypes.GHAST) {
                              return 4.0F;
                           } else if (type == EntityTypes.GIANT) {
                              return 3.6F;
                           } else if (type == EntityTypes.GUARDIAN) {
                              return 0.85F;
                           } else if (type == EntityTypes.IRON_GOLEM) {
                              return 1.4F;
                           } else {
                              float size;
                              if (type == EntityTypes.MAGMA_CUBE) {
                                 if (packetEntity instanceof PacketEntitySizeable) {
                                    sizeable = (PacketEntitySizeable)packetEntity;
                                    size = (float)sizeable.size;
                                    return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5) ? 0.52F * size : (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 2.04F * 0.255F * size : 0.51000005F * size);
                                 } else {
                                    return 0.98F;
                                 }
                              } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.MINECART_ABSTRACT)) {
                                 return 0.98F;
                              } else if (type != EntityTypes.PLAYER && type != EntityTypes.MANNEQUIN) {
                                 if (type == EntityTypes.POLAR_BEAR) {
                                    return 1.4F;
                                 } else if (type == EntityTypes.RAVAGER) {
                                    return 1.95F;
                                 } else if (type == EntityTypes.SHULKER) {
                                    return 1.0F;
                                 } else if (type == EntityTypes.SLIME) {
                                    if (packetEntity instanceof PacketEntitySizeable) {
                                       sizeable = (PacketEntitySizeable)packetEntity;
                                       size = (float)sizeable.size;
                                       return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5) ? 0.52F * size : (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 2.04F * 0.255F * size : 0.51000005F * size);
                                    } else {
                                       return 0.3125F;
                                    }
                                 } else if (type == EntityTypes.SMALL_FIREBALL) {
                                    return 0.3125F;
                                 } else if (type == EntityTypes.SPIDER) {
                                    return 1.4F;
                                 } else if (type == EntityTypes.SQUID) {
                                    return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.8F : 0.95F;
                                 } else if (type == EntityTypes.TURTLE) {
                                    return 1.2F;
                                 } else if (type == EntityTypes.ALLAY) {
                                    return 0.35F;
                                 } else if (type == EntityTypes.SNIFFER) {
                                    return 1.9F;
                                 } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.CAMEL)) {
                                    return 1.7F;
                                 } else if (type == EntityTypes.WIND_CHARGE) {
                                    return 0.3125F;
                                 } else if (type == EntityTypes.ARMOR_STAND) {
                                    return 0.5F;
                                 } else if (type == EntityTypes.FALLING_BLOCK) {
                                    return 0.98F;
                                 } else {
                                    return type == EntityTypes.FIREWORK_ROCKET ? 0.25F : 0.6F;
                                 }
                              } else {
                                 return 0.6F;
                              }
                           }
                        } else {
                           return 0.9F;
                        }
                     } else {
                        return 0.4F;
                     }
                  } else {
                     return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 1.3964844F : 1.4F;
                  }
               } else {
                  return 1.3964844F;
               }
            } else {
               return 0.3125F;
            }
         } else {
            return 0.7F;
         }
      } else {
         return 0.5F;
      }
   }

   public static Vector3d getRidingOffsetFromVehicle(PacketEntity entity, GrimPlayer player) {
      SimpleCollisionBox box = entity.getPossibleCollisionBoxes();
      double x = (box.maxX + box.minX) / 2.0D;
      double y = box.minY;
      double z = (box.maxZ + box.minZ) / 2.0D;
      if (entity instanceof PacketEntityTrackXRot) {
         PacketEntityTrackXRot xRotEntity = (PacketEntityTrackXRot)entity;
         float f;
         float f1;
         if (EntityTypes.isTypeInstanceOf(entity.type, EntityTypes.BOAT)) {
            f = 0.0F;
            f1 = (float)(getPassengerRidingOffset(player, entity) - 0.3499999940395355D);
            if (!entity.passengers.isEmpty()) {
               int i = entity.passengers.indexOf(player.compensatedEntities.self);
               if (i == 0) {
                  f = 0.2F;
               } else if (i == 1) {
                  f = -0.6F;
               }
            }

            Vector3d vec3 = new Vector3d((double)f, 0.0D, 0.0D);
            vec3 = yRot(GrimMath.radians(-xRotEntity.interpYaw) - 1.5707964F, vec3);
            return new Vector3d(x + vec3.x, y + (double)f1, z + vec3.z);
         }

         if (entity.type == EntityTypes.LLAMA) {
            f = player.trigHandler.cos(GrimMath.radians(xRotEntity.interpYaw));
            f1 = player.trigHandler.sin(GrimMath.radians(xRotEntity.interpYaw));
            return new Vector3d(x + (double)(0.3F * f1), y + getPassengerRidingOffset(player, entity) - 0.3499999940395355D, z + (double)(0.3F * f));
         }

         if (entity.type == EntityTypes.CHICKEN) {
            f = player.trigHandler.sin(GrimMath.radians(xRotEntity.interpYaw));
            f1 = player.trigHandler.cos(GrimMath.radians(xRotEntity.interpYaw));
            y += (double)(getHeight(player, entity) * 0.5F);
            return new Vector3d(x + (double)(0.1F * f), y - 0.3499999940395355D, z - (double)(0.1F * f1));
         }
      }

      return new Vector3d(x, y + getPassengerRidingOffset(player, entity) - 0.3499999940395355D, z);
   }

   private static Vector3d yRot(float yaw, Vector3d start) {
      double cos = (double)((float)Math.cos((double)yaw));
      double sin = (double)((float)Math.sin((double)yaw));
      return new Vector3d(start.x * cos + start.z * sin, start.y, start.z * cos - start.x * sin);
   }

   public static float getHeight(GrimPlayer player, PacketEntity packetEntity) {
      float height = getHeightMinusBaby(player, packetEntity);
      return height * (packetEntity.isBaby ? getBabyScaleFactor(packetEntity) : 1.0F);
   }

   public static double getMyRidingOffset(PacketEntity packetEntity) {
      EntityType type = packetEntity.type;
      if (type != EntityTypes.PIGLIN && type != EntityTypes.ZOMBIFIED_PIGLIN && type != EntityTypes.ZOMBIE) {
         if (type == EntityTypes.SKELETON) {
            return -0.6D;
         } else if (type != EntityTypes.ENDERMITE && type != EntityTypes.SILVERFISH) {
            if (type != EntityTypes.EVOKER && type != EntityTypes.ILLUSIONER && type != EntityTypes.PILLAGER && type != EntityTypes.RAVAGER && type != EntityTypes.VINDICATOR && type != EntityTypes.WITCH) {
               if (type != EntityTypes.PLAYER && type != EntityTypes.MANNEQUIN) {
                  return EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_ANIMAL) ? 0.14D : 0.0D;
               } else {
                  return -0.35D;
               }
            } else {
               return -0.45D;
            }
         } else {
            return 0.1D;
         }
      } else {
         return packetEntity.isBaby ? -0.05D : -0.45D;
      }
   }

   public static double getPassengerRidingOffset(GrimPlayer player, PacketEntity packetEntity) {
      if (packetEntity instanceof PacketEntityHorse) {
         return (double)getHeight(player, packetEntity) * 0.75D - 0.25D;
      } else {
         EntityType type = packetEntity.type;
         if (EntityTypes.isTypeInstanceOf(type, EntityTypes.MINECART_ABSTRACT)) {
            return 0.0D;
         } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.BOAT)) {
            return -0.1D;
         } else if (type == EntityTypes.HAPPY_GHAST) {
            return 0.5D;
         } else if (type != EntityTypes.HOGLIN && type != EntityTypes.ZOGLIN) {
            if (type == EntityTypes.LLAMA) {
               return (double)getHeight(player, packetEntity) * 0.67D;
            } else if (type == EntityTypes.PIGLIN) {
               return (double)getHeight(player, packetEntity) * 0.92D;
            } else if (type == EntityTypes.RAVAGER) {
               return 2.1D;
            } else if (type == EntityTypes.SKELETON) {
               return (double)getHeight(player, packetEntity) * 0.75D - 0.1875D;
            } else if (type == EntityTypes.SPIDER) {
               return (double)getHeight(player, packetEntity) * 0.5D;
            } else {
               return type == EntityTypes.STRIDER ? (double)getHeight(player, packetEntity) - 0.19D : (double)getHeight(player, packetEntity) * 0.75D;
            }
         } else {
            return (double)getHeight(player, packetEntity) - (packetEntity.isBaby ? 0.2D : 0.15D);
         }
      }
   }

   private static float getHeightMinusBaby(GrimPlayer player, PacketEntity packetEntity) {
      EntityType type = packetEntity.type;
      if (type == EntityTypes.ARMADILLO) {
         return 0.65F;
      } else if (type == EntityTypes.AXOLOTL) {
         return 0.42F;
      } else if (type != EntityTypes.BEE && type != EntityTypes.DOLPHIN && type != EntityTypes.ALLAY) {
         if (type != EntityTypes.EVOKER_FANGS && type != EntityTypes.VEX) {
            if (type == EntityTypes.SQUID) {
               return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.8F : 0.95F;
            } else if (type != EntityTypes.PARROT && type != EntityTypes.BAT && type != EntityTypes.PIG && type != EntityTypes.SPIDER) {
               if (type != EntityTypes.WITHER_SKULL && type != EntityTypes.SHULKER_BULLET) {
                  if (type == EntityTypes.BLAZE) {
                     return 1.8F;
                  } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.BOAT)) {
                     return 0.5625F;
                  } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.NAUTILUS)) {
                     return 0.95F;
                  } else if (type == EntityTypes.HAPPY_GHAST) {
                     return 4.0F;
                  } else if (type == EntityTypes.CAT) {
                     return 0.7F;
                  } else if (type == EntityTypes.CAVE_SPIDER) {
                     return 0.5F;
                  } else if (type == EntityTypes.FROG) {
                     return 0.55F;
                  } else if (type == EntityTypes.CHICKEN) {
                     return 0.7F;
                  } else if (type != EntityTypes.HOGLIN && type != EntityTypes.ZOGLIN) {
                     if (type == EntityTypes.COW) {
                        return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 1.4F : 1.3F;
                     } else if (type == EntityTypes.STRIDER) {
                        return 1.7F;
                     } else if (type == EntityTypes.CREEPER) {
                        return 1.7F;
                     } else if (type == EntityTypes.DONKEY) {
                        return 1.5F;
                     } else if (packetEntity instanceof PacketEntityGuardian) {
                        PacketEntityGuardian packetEntityGuardian = (PacketEntityGuardian)packetEntity;
                        return packetEntityGuardian.isElder ? 1.9975F : 0.85F;
                     } else if (type != EntityTypes.ENDERMAN && type != EntityTypes.WARDEN) {
                        if (type != EntityTypes.ENDERMITE && type != EntityTypes.COD) {
                           if (type == EntityTypes.END_CRYSTAL) {
                              return 2.0F;
                           } else if (type == EntityTypes.ENDER_DRAGON) {
                              return 8.0F;
                           } else if (type == EntityTypes.FIREBALL) {
                              return 1.0F;
                           } else if (type == EntityTypes.FOX) {
                              return 0.7F;
                           } else if (type == EntityTypes.GHAST) {
                              return 4.0F;
                           } else if (type == EntityTypes.GIANT) {
                              return 12.0F;
                           } else if (type == EntityTypes.GUARDIAN) {
                              return 0.85F;
                           } else if (type == EntityTypes.HORSE) {
                              return 1.6F;
                           } else if (type == EntityTypes.IRON_GOLEM) {
                              return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 2.7F : 2.9F;
                           } else if (type == EntityTypes.CREAKING) {
                              return 2.7F;
                           } else if (type != EntityTypes.LLAMA && type != EntityTypes.TRADER_LLAMA) {
                              if (type == EntityTypes.TROPICAL_FISH) {
                                 return 0.4F;
                              } else {
                                 PacketEntitySizeable sizeable;
                                 float size;
                                 if (type == EntityTypes.MAGMA_CUBE) {
                                    if (packetEntity instanceof PacketEntitySizeable) {
                                       sizeable = (PacketEntitySizeable)packetEntity;
                                       size = (float)sizeable.size;
                                       return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5) ? 0.52F * size : (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 2.04F * 0.255F * size : 0.51000005F * size);
                                    } else {
                                       return 0.7F;
                                    }
                                 } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.MINECART_ABSTRACT)) {
                                    return 0.7F;
                                 } else if (type == EntityTypes.MULE) {
                                    return 1.6F;
                                 } else if (type == EntityTypes.MOOSHROOM) {
                                    return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 1.4F : 1.3F;
                                 } else if (type == EntityTypes.OCELOT) {
                                    return 0.7F;
                                 } else if (type == EntityTypes.PANDA) {
                                    return 1.25F;
                                 } else if (type == EntityTypes.PHANTOM) {
                                    if (packetEntity instanceof PacketEntitySizeable) {
                                       sizeable = (PacketEntitySizeable)packetEntity;
                                       return 0.5F + (float)sizeable.size * 0.1F;
                                    } else {
                                       return 1.8F;
                                    }
                                 } else if (type != EntityTypes.PLAYER && type != EntityTypes.MANNEQUIN) {
                                    if (type == EntityTypes.POLAR_BEAR) {
                                       return 1.4F;
                                    } else if (type == EntityTypes.PUFFERFISH) {
                                       return 0.7F;
                                    } else if (type == EntityTypes.RABBIT) {
                                       return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.5F : 0.7F;
                                    } else if (type == EntityTypes.RAVAGER) {
                                       return 2.2F;
                                    } else if (type == EntityTypes.SALMON) {
                                       return 0.4F;
                                    } else if (type != EntityTypes.SHEEP && type != EntityTypes.GOAT) {
                                       if (type == EntityTypes.SHULKER) {
                                          return 2.0F;
                                       } else if (type == EntityTypes.SILVERFISH) {
                                          return 0.3F;
                                       } else if (type == EntityTypes.SKELETON) {
                                          return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 1.99F : 1.95F;
                                       } else if (type == EntityTypes.SKELETON_HORSE) {
                                          return 1.6F;
                                       } else if (type == EntityTypes.SLIME) {
                                          if (packetEntity instanceof PacketEntitySizeable) {
                                             sizeable = (PacketEntitySizeable)packetEntity;
                                             size = (float)sizeable.size;
                                             return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5) ? 0.52F * size : (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 2.04F * 0.255F * size : 0.51000005F * size);
                                          } else {
                                             return 0.3125F;
                                          }
                                       } else if (type == EntityTypes.SMALL_FIREBALL) {
                                          return 0.3125F;
                                       } else if (type == EntityTypes.SNOW_GOLEM) {
                                          return 1.9F;
                                       } else if (type == EntityTypes.STRAY) {
                                          return 1.99F;
                                       } else if (type == EntityTypes.TURTLE) {
                                          return 0.4F;
                                       } else if (type == EntityTypes.WITHER) {
                                          return 3.5F;
                                       } else if (type == EntityTypes.WITHER_SKELETON) {
                                          return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 2.4F : 2.535F;
                                       } else if (type == EntityTypes.WOLF) {
                                          return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? 0.85F : 0.8F;
                                       } else if (type == EntityTypes.ZOMBIE_HORSE) {
                                          return 1.6F;
                                       } else if (type == EntityTypes.TADPOLE) {
                                          return 0.3F;
                                       } else if (type == EntityTypes.SNIFFER) {
                                          return 1.75F;
                                       } else if (EntityTypes.isTypeInstanceOf(type, EntityTypes.CAMEL)) {
                                          return 2.375F;
                                       } else if (type == EntityTypes.BREEZE) {
                                          return 1.77F;
                                       } else if (type == EntityTypes.BOGGED) {
                                          return 1.99F;
                                       } else if (type == EntityTypes.PARCHED) {
                                          return 1.99F;
                                       } else if (type == EntityTypes.WIND_CHARGE) {
                                          return 0.3125F;
                                       } else if (type == EntityTypes.ARMOR_STAND) {
                                          return 1.975F;
                                       } else if (type == EntityTypes.FALLING_BLOCK) {
                                          return 0.98F;
                                       } else if (type == EntityTypes.VILLAGER && player.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
                                          return 1.8F;
                                       } else if (type == EntityTypes.FIREWORK_ROCKET) {
                                          return 0.25F;
                                       } else {
                                          return type == EntityTypes.COPPER_GOLEM ? 1.0F : 1.95F;
                                       }
                                    } else {
                                       return 1.3F;
                                    }
                                 } else {
                                    return 1.8F;
                                 }
                              }
                           } else {
                              return 1.87F;
                           }
                        } else {
                           return 0.3F;
                        }
                     } else {
                        return 2.9F;
                     }
                  } else {
                     return 1.4F;
                  }
               } else {
                  return 0.3125F;
               }
            } else {
               return 0.9F;
            }
         } else {
            return 0.8F;
         }
      } else {
         return 0.6F;
      }
   }

   private static float getBabyScaleFactor(PacketEntity packetEntity) {
      EntityType type = packetEntity.type;
      if (type == EntityTypes.TURTLE) {
         return 0.3F;
      } else if (type == EntityTypes.HAPPY_GHAST) {
         return 0.2375F;
      } else if (type == EntityTypes.DOLPHIN) {
         return 0.65F;
      } else if (type == EntityTypes.ARMADILLO) {
         return 0.6F;
      } else {
         return EntityTypes.isTypeInstanceOf(type, EntityTypes.CAMEL) ? 0.45F : 0.5F;
      }
   }

   @Generated
   private BoundingBoxSize() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
