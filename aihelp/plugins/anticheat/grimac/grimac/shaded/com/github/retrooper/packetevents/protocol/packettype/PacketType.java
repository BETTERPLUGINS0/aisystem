package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.PacketSide;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_12;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_12_1;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_13;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_14;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_14_4;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_15;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_15_2;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_16;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_16_2;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_17;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_18;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_1;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_3;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_4;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_20_2;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_20_3;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_20_5;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_21;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_21_2;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_21_5;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_21_6;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_21_9;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_7_10;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_8;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_9;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_9_3;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_20_2;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_20_3;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_20_5;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_21;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_21_6;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_21_9;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.config.serverbound.ServerboundConfigPacketType_1_20_2;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.config.serverbound.ServerboundConfigPacketType_1_20_5;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.config.serverbound.ServerboundConfigPacketType_1_21_6;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.config.serverbound.ServerboundConfigPacketType_1_21_9;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_12;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_12_1;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_13;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_14;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_15_2;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_16;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_16_2;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_17;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_1;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_3;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_4;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_20_2;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_20_3;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_20_5;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_21_2;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_21_4;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_21_5;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_21_6;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_21_9;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_7_10;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_8;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_9;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.VersionMapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientAcceptCodeOfConduct;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientConfigurationEndAck;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientCookieResponse;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientCustomClickAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientKeepAlive;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientPluginMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientPong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientResourcePackStatus;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientSelectKnownPacks;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientSettings;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerClearDialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerCodeOfConduct;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerConfigurationEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerCookieRequest;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerCustomReportDetails;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerDisconnect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerKeepAlive;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerPing;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerPluginMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerResetChat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerResourcePackRemove;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerResourcePackSend;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerSelectKnownPacks;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerServerLinks;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerShowDialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerStoreCookie;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerTransfer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerUpdateEnabledFeatures;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientCookieResponse;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientEncryptionResponse;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginStart;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginSuccessAck;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientPluginResponse;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerCookieRequest;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerDisconnect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerEncryptionRequest;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerPluginRequest;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerSetCompression;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAdvancementTab;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAnimation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChangeGameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatAck;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommandUnsigned;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatPreview;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatSessionUpdate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChunkBatchAck;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindowButton;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientTickEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCloseWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientConfigurationAck;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCookieResponse;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCraftRecipeRequest;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCreativeInventoryAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCustomClickAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientDebugPing;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientDebugSampleSubscription;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientDebugSubscriptionRequest;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEditBook;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientGenerateStructure;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientLockDifficulty;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientNameItem;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPickItem;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPickItemFromBlock;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPickItemFromEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerInput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerLoaded;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientQueryBlockNBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientQueryEntityNBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientResourcePackStatus;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSelectBundleItem;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSelectTrade;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetBeaconEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetDifficulty;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetDisplayedRecipe;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetRecipeBookState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetStructureBlock;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSetTestBlock;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSlotStateChange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSpectate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerBoat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTeleportConfirm;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTestInstanceBlockAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateCommandBlock;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateCommandBlockMinecart;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateJigsawBlock;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientVehicleMove;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerAcknowledgeBlockChanges;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerAcknowledgePlayerDigging;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerActionBar;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerAttachEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockEntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBossBar;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBundle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatPreview;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkBatchBegin;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkBatchEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkDataBulk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerClearDialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerClearTitles;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCollectItem;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCombatEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerConfigurationStart;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCookieRequest;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCraftRecipeResponse;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCustomChatCompletions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCustomReportDetails;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDamageEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDeathCombatEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDebugBlockValue;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDebugChunkValue;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDebugEntityValue;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDebugEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDebugPong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDebugSample;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDeclareCommands;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDeclareRecipes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDeleteChat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDifficulty;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisguisedChat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEndCombatEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEnterCombatEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityHeadLook;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMovement;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityPositionSync;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMove;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMoveAndRotation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRotation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntitySoundEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerExplosion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerFacePlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerGameTestHighlightPos;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerHeldItemChange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerHurtAnimation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerInitializeWorldBorder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerKeepAlive;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMapData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMerchantOffers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMoveMinecart;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerNBTQueryResponse;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenBook;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenHorseWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenSignEditor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerChatHeader;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerRotation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerProjectilePower;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRecipeBookAdd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRecipeBookRemove;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRecipeBookSettings;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRemoveEntityEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerResetScore;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerResourcePackRemove;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerResourcePackSend;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSelectAdvancementsTab;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerServerData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerServerLinks;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetCompression;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetCooldown;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetCursorItem;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetDisplayChatPreview;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetExperience;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPlayerInventory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleSubtitle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleText;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleTimes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerShowDialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnExperienceOrb;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnPainting;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnPosition;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnWeatherEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerStatistics;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerStoreCookie;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTabComplete;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTestInstanceBlockStatus;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTickingState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTickingStep;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTimeUpdate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTransfer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUnloadChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAdvancements;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateEnabledFeatures;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateEntityNBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateHealth;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateLight;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateSimulationDistance;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateViewDistance;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateViewPosition;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUseBed;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerVehicleMove;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWaypoint;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowProperty;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWorldBorder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWorldBorderCenter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWorldBorderSize;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWorldBorderWarningReach;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayWorldBorderLerpSize;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayWorldBorderWarningDelay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.status.client.WrapperStatusClientPing;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.status.client.WrapperStatusClientRequest;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerPong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class PacketType {
   private static volatile boolean PREPARED = false;
   private static final Object PREPARE_LOCK = new Object();
   private static final VersionMapper CLIENTBOUND_PLAY_VERSION_MAPPER;
   private static final VersionMapper SERVERBOUND_PLAY_VERSION_MAPPER;
   private static final VersionMapper CLIENTBOUND_CONFIG_VERSION_MAPPER;
   private static final VersionMapper SERVERBOUND_CONFIG_VERSION_MAPPER;

   private PacketType() {
   }

   @ApiStatus.Internal
   public static void prepare() {
      if (!PREPARED) {
         synchronized(PREPARE_LOCK) {
            if (!PREPARED) {
               PacketType.Play.Client.load();
               PacketType.Play.Server.load();
               PacketType.Configuration.Client.load();
               PacketType.Configuration.Server.load();
               PREPARED = true;
            }
         }
      }
   }

   @ApiStatus.Internal
   public static boolean isPrepared() {
      return PREPARED;
   }

   public static PacketTypeCommon getById(PacketSide side, ConnectionState state, ClientVersion version, int packetID) {
      switch(state) {
      case HANDSHAKING:
         if (side == PacketSide.CLIENT) {
            return PacketType.Handshaking.Client.getById(packetID);
         }

         return PacketType.Handshaking.Server.getById(packetID);
      case STATUS:
         if (side == PacketSide.CLIENT) {
            return PacketType.Status.Client.getById(packetID);
         }

         return PacketType.Status.Server.getById(packetID);
      case LOGIN:
         if (side == PacketSide.CLIENT) {
            return PacketType.Login.Client.getById(packetID);
         }

         return PacketType.Login.Server.getById(packetID);
      case PLAY:
         if (side == PacketSide.CLIENT) {
            return PacketType.Play.Client.getById(version, packetID);
         }

         return PacketType.Play.Server.getById(version, packetID);
      case CONFIGURATION:
         if (side == PacketSide.CLIENT) {
            return PacketType.Configuration.Client.getById(version, packetID);
         }

         return PacketType.Configuration.Server.getById(version, packetID);
      default:
         return null;
      }
   }

   static {
      CLIENTBOUND_PLAY_VERSION_MAPPER = new VersionMapper(new ClientVersion[]{ClientVersion.V_1_7_2, ClientVersion.V_1_8, ClientVersion.V_1_9, ClientVersion.V_1_10, ClientVersion.V_1_12, ClientVersion.V_1_12_1, ClientVersion.V_1_13, ClientVersion.V_1_14, ClientVersion.V_1_14_4, ClientVersion.V_1_15, ClientVersion.V_1_15_2, ClientVersion.V_1_16, ClientVersion.V_1_16_2, ClientVersion.V_1_17, ClientVersion.V_1_18, ClientVersion.V_1_19, ClientVersion.V_1_19_1, ClientVersion.V_1_19_3, ClientVersion.V_1_19_4, ClientVersion.V_1_20_2, ClientVersion.V_1_20_3, ClientVersion.V_1_20_5, ClientVersion.V_1_21, ClientVersion.V_1_21_2, ClientVersion.V_1_21_5, ClientVersion.V_1_21_6, ClientVersion.V_1_21_9});
      SERVERBOUND_PLAY_VERSION_MAPPER = new VersionMapper(new ClientVersion[]{ClientVersion.V_1_7_2, ClientVersion.V_1_8, ClientVersion.V_1_9, ClientVersion.V_1_12, ClientVersion.V_1_12_1, ClientVersion.V_1_13, ClientVersion.V_1_14, ClientVersion.V_1_15_2, ClientVersion.V_1_16, ClientVersion.V_1_16_2, ClientVersion.V_1_17, ClientVersion.V_1_19, ClientVersion.V_1_19_1, ClientVersion.V_1_19_3, ClientVersion.V_1_19_4, ClientVersion.V_1_20_2, ClientVersion.V_1_20_3, ClientVersion.V_1_20_5, ClientVersion.V_1_21_2, ClientVersion.V_1_21_4, ClientVersion.V_1_21_5, ClientVersion.V_1_21_6, ClientVersion.V_1_21_9});
      CLIENTBOUND_CONFIG_VERSION_MAPPER = new VersionMapper(new ClientVersion[]{ClientVersion.V_1_20_2, ClientVersion.V_1_20_3, ClientVersion.V_1_20_5, ClientVersion.V_1_21, ClientVersion.V_1_21_6, ClientVersion.V_1_21_9});
      SERVERBOUND_CONFIG_VERSION_MAPPER = new VersionMapper(new ClientVersion[]{ClientVersion.V_1_20_2, ClientVersion.V_1_20_5, ClientVersion.V_1_21_6, ClientVersion.V_1_21_9});
   }

   public static class Play {
      public static enum Server implements PacketTypeCommon, ClientBoundPacket {
         SET_COMPRESSION(WrapperPlayServerSetCompression.class),
         MAP_CHUNK_BULK(WrapperPlayServerChunkDataBulk.class),
         UPDATE_ENTITY_NBT(WrapperPlayServerUpdateEntityNBT.class),
         UPDATE_SIGN((Class)null),
         USE_BED(WrapperPlayServerUseBed.class),
         SPAWN_WEATHER_ENTITY(WrapperPlayServerSpawnWeatherEntity.class),
         TITLE(WrapperPlayServerSetTitleSubtitle.class),
         WORLD_BORDER(WrapperPlayServerWorldBorder.class),
         COMBAT_EVENT(WrapperPlayServerCombatEvent.class),
         ENTITY_MOVEMENT(WrapperPlayServerEntityMovement.class),
         SPAWN_LIVING_ENTITY(WrapperPlayServerSpawnLivingEntity.class),
         SPAWN_PAINTING(WrapperPlayServerSpawnPainting.class),
         SCULK_VIBRATION_SIGNAL((Class)null),
         ACKNOWLEDGE_PLAYER_DIGGING(WrapperPlayServerAcknowledgePlayerDigging.class),
         CHAT_PREVIEW_PACKET(WrapperPlayServerChatPreview.class),
         NAMED_SOUND_EFFECT((Class)null),
         PLAYER_CHAT_HEADER(WrapperPlayServerPlayerChatHeader.class),
         PLAYER_INFO(WrapperPlayServerPlayerInfo.class),
         DISPLAY_CHAT_PREVIEW(WrapperPlayServerSetDisplayChatPreview.class),
         UPDATE_ENABLED_FEATURES(WrapperPlayServerUpdateEnabledFeatures.class),
         SPAWN_PLAYER(WrapperPlayServerSpawnPlayer.class),
         WINDOW_CONFIRMATION(WrapperPlayServerWindowConfirmation.class),
         SPAWN_ENTITY(WrapperPlayServerSpawnEntity.class),
         @ApiStatus.Obsolete
         SPAWN_EXPERIENCE_ORB(WrapperPlayServerSpawnExperienceOrb.class),
         ENTITY_ANIMATION(WrapperPlayServerEntityAnimation.class),
         STATISTICS(WrapperPlayServerStatistics.class),
         BLOCK_BREAK_ANIMATION(WrapperPlayServerBlockBreakAnimation.class),
         BLOCK_ENTITY_DATA(WrapperPlayServerBlockEntityData.class),
         BLOCK_ACTION(WrapperPlayServerBlockAction.class),
         BLOCK_CHANGE(WrapperPlayServerBlockChange.class),
         BOSS_BAR(WrapperPlayServerBossBar.class),
         SERVER_DIFFICULTY(WrapperPlayServerDifficulty.class),
         CLEAR_TITLES(WrapperPlayServerClearTitles.class),
         TAB_COMPLETE(WrapperPlayServerTabComplete.class),
         MULTI_BLOCK_CHANGE(WrapperPlayServerMultiBlockChange.class),
         DECLARE_COMMANDS(WrapperPlayServerDeclareCommands.class),
         CLOSE_WINDOW(WrapperPlayServerCloseWindow.class),
         WINDOW_ITEMS(WrapperPlayServerWindowItems.class),
         WINDOW_PROPERTY(WrapperPlayServerWindowProperty.class),
         SET_SLOT(WrapperPlayServerSetSlot.class),
         SET_COOLDOWN(WrapperPlayServerSetCooldown.class),
         PLUGIN_MESSAGE(WrapperPlayServerPluginMessage.class),
         DISCONNECT(WrapperPlayServerDisconnect.class),
         ENTITY_STATUS(WrapperPlayServerEntityStatus.class),
         EXPLOSION(WrapperPlayServerExplosion.class),
         UNLOAD_CHUNK(WrapperPlayServerUnloadChunk.class),
         CHANGE_GAME_STATE(WrapperPlayServerChangeGameState.class),
         OPEN_HORSE_WINDOW(WrapperPlayServerOpenHorseWindow.class),
         INITIALIZE_WORLD_BORDER(WrapperPlayServerInitializeWorldBorder.class),
         KEEP_ALIVE(WrapperPlayServerKeepAlive.class),
         CHUNK_DATA(WrapperPlayServerChunkData.class),
         EFFECT(WrapperPlayServerEffect.class),
         PARTICLE(WrapperPlayServerParticle.class),
         UPDATE_LIGHT(WrapperPlayServerUpdateLight.class),
         JOIN_GAME(WrapperPlayServerJoinGame.class),
         MAP_DATA(WrapperPlayServerMapData.class),
         MERCHANT_OFFERS(WrapperPlayServerMerchantOffers.class),
         ENTITY_RELATIVE_MOVE(WrapperPlayServerEntityRelativeMove.class),
         ENTITY_RELATIVE_MOVE_AND_ROTATION(WrapperPlayServerEntityRelativeMoveAndRotation.class),
         ENTITY_ROTATION(WrapperPlayServerEntityRotation.class),
         VEHICLE_MOVE(WrapperPlayServerVehicleMove.class),
         OPEN_BOOK(WrapperPlayServerOpenBook.class),
         OPEN_WINDOW(WrapperPlayServerOpenWindow.class),
         OPEN_SIGN_EDITOR(WrapperPlayServerOpenSignEditor.class),
         PING(WrapperPlayServerPing.class),
         CRAFT_RECIPE_RESPONSE(WrapperPlayServerCraftRecipeResponse.class),
         PLAYER_ABILITIES(WrapperPlayServerPlayerAbilities.class),
         END_COMBAT_EVENT(WrapperPlayServerEndCombatEvent.class),
         ENTER_COMBAT_EVENT(WrapperPlayServerEnterCombatEvent.class),
         DEATH_COMBAT_EVENT(WrapperPlayServerDeathCombatEvent.class),
         FACE_PLAYER(WrapperPlayServerFacePlayer.class),
         PLAYER_POSITION_AND_LOOK(WrapperPlayServerPlayerPositionAndLook.class),
         @ApiStatus.Obsolete
         UNLOCK_RECIPES((Class)null),
         DESTROY_ENTITIES(WrapperPlayServerDestroyEntities.class),
         REMOVE_ENTITY_EFFECT(WrapperPlayServerRemoveEntityEffect.class),
         RESOURCE_PACK_SEND(WrapperPlayServerResourcePackSend.class),
         RESPAWN(WrapperPlayServerRespawn.class),
         ENTITY_HEAD_LOOK(WrapperPlayServerEntityHeadLook.class),
         SELECT_ADVANCEMENTS_TAB(WrapperPlayServerSelectAdvancementsTab.class),
         ACTION_BAR(WrapperPlayServerActionBar.class),
         WORLD_BORDER_CENTER(WrapperPlayServerWorldBorderCenter.class),
         WORLD_BORDER_LERP_SIZE(WrapperPlayWorldBorderLerpSize.class),
         WORLD_BORDER_SIZE(WrapperPlayServerWorldBorderSize.class),
         WORLD_BORDER_WARNING_DELAY(WrapperPlayWorldBorderWarningDelay.class),
         WORLD_BORDER_WARNING_REACH(WrapperPlayServerWorldBorderWarningReach.class),
         CAMERA(WrapperPlayServerCamera.class),
         HELD_ITEM_CHANGE(WrapperPlayServerHeldItemChange.class),
         UPDATE_VIEW_POSITION(WrapperPlayServerUpdateViewPosition.class),
         UPDATE_VIEW_DISTANCE(WrapperPlayServerUpdateViewDistance.class),
         SPAWN_POSITION(WrapperPlayServerSpawnPosition.class),
         DISPLAY_SCOREBOARD(WrapperPlayServerDisplayScoreboard.class),
         ENTITY_METADATA(WrapperPlayServerEntityMetadata.class),
         ATTACH_ENTITY(WrapperPlayServerAttachEntity.class),
         ENTITY_VELOCITY(WrapperPlayServerEntityVelocity.class),
         ENTITY_EQUIPMENT(WrapperPlayServerEntityEquipment.class),
         SET_EXPERIENCE(WrapperPlayServerSetExperience.class),
         UPDATE_HEALTH(WrapperPlayServerUpdateHealth.class),
         SCOREBOARD_OBJECTIVE(WrapperPlayServerScoreboardObjective.class),
         SET_PASSENGERS(WrapperPlayServerSetPassengers.class),
         TEAMS(WrapperPlayServerTeams.class),
         UPDATE_SCORE(WrapperPlayServerUpdateScore.class),
         UPDATE_SIMULATION_DISTANCE(WrapperPlayServerUpdateSimulationDistance.class),
         SET_TITLE_SUBTITLE(WrapperPlayServerSetTitleSubtitle.class),
         TIME_UPDATE(WrapperPlayServerTimeUpdate.class),
         SET_TITLE_TEXT(WrapperPlayServerSetTitleText.class),
         SET_TITLE_TIMES(WrapperPlayServerSetTitleTimes.class),
         ENTITY_SOUND_EFFECT(WrapperPlayServerEntitySoundEffect.class),
         SOUND_EFFECT(WrapperPlayServerSoundEffect.class),
         STOP_SOUND((Class)null),
         PLAYER_LIST_HEADER_AND_FOOTER(WrapperPlayServerPlayerListHeaderAndFooter.class),
         NBT_QUERY_RESPONSE(WrapperPlayServerNBTQueryResponse.class),
         COLLECT_ITEM(WrapperPlayServerCollectItem.class),
         ENTITY_TELEPORT(WrapperPlayServerEntityTeleport.class),
         UPDATE_ADVANCEMENTS(WrapperPlayServerUpdateAdvancements.class),
         UPDATE_ATTRIBUTES(WrapperPlayServerUpdateAttributes.class),
         ENTITY_EFFECT(WrapperPlayServerEntityEffect.class),
         DECLARE_RECIPES(WrapperPlayServerDeclareRecipes.class),
         TAGS(WrapperPlayServerTags.class),
         CHAT_MESSAGE(WrapperPlayServerChatMessage.class),
         ACKNOWLEDGE_BLOCK_CHANGES(WrapperPlayServerAcknowledgeBlockChanges.class),
         SERVER_DATA(WrapperPlayServerServerData.class),
         SYSTEM_CHAT_MESSAGE(WrapperPlayServerSystemChatMessage.class),
         DELETE_CHAT(WrapperPlayServerDeleteChat.class),
         CUSTOM_CHAT_COMPLETIONS(WrapperPlayServerCustomChatCompletions.class),
         DISGUISED_CHAT(WrapperPlayServerDisguisedChat.class),
         PLAYER_INFO_REMOVE(WrapperPlayServerPlayerInfoRemove.class),
         PLAYER_INFO_UPDATE(WrapperPlayServerPlayerInfoUpdate.class),
         DAMAGE_EVENT(WrapperPlayServerDamageEvent.class),
         HURT_ANIMATION(WrapperPlayServerHurtAnimation.class),
         BUNDLE(WrapperPlayServerBundle.class),
         CHUNK_BIOMES((Class)null),
         CHUNK_BATCH_END(WrapperPlayServerChunkBatchEnd.class),
         CHUNK_BATCH_BEGIN(WrapperPlayServerChunkBatchBegin.class),
         DEBUG_PONG(WrapperPlayServerDebugPong.class),
         CONFIGURATION_START(WrapperPlayServerConfigurationStart.class),
         RESET_SCORE(WrapperPlayServerResetScore.class),
         RESOURCE_PACK_REMOVE(WrapperPlayServerResourcePackRemove.class),
         TICKING_STATE(WrapperPlayServerTickingState.class),
         TICKING_STEP(WrapperPlayServerTickingStep.class),
         COOKIE_REQUEST(WrapperPlayServerCookieRequest.class),
         DEBUG_SAMPLE(WrapperPlayServerDebugSample.class),
         STORE_COOKIE(WrapperPlayServerStoreCookie.class),
         TRANSFER(WrapperPlayServerTransfer.class),
         PROJECTILE_POWER(WrapperPlayServerProjectilePower.class),
         CUSTOM_REPORT_DETAILS(WrapperPlayServerCustomReportDetails.class),
         SERVER_LINKS(WrapperPlayServerServerLinks.class),
         MOVE_MINECART(WrapperPlayServerMoveMinecart.class),
         SET_CURSOR_ITEM(WrapperPlayServerSetCursorItem.class),
         SET_PLAYER_INVENTORY(WrapperPlayServerSetPlayerInventory.class),
         ENTITY_POSITION_SYNC(WrapperPlayServerEntityPositionSync.class),
         PLAYER_ROTATION(WrapperPlayServerPlayerRotation.class),
         RECIPE_BOOK_ADD(WrapperPlayServerRecipeBookAdd.class),
         RECIPE_BOOK_REMOVE(WrapperPlayServerRecipeBookRemove.class),
         RECIPE_BOOK_SETTINGS(WrapperPlayServerRecipeBookSettings.class),
         TEST_INSTANCE_BLOCK_STATUS(WrapperPlayServerTestInstanceBlockStatus.class),
         WAYPOINT(WrapperPlayServerWaypoint.class),
         CLEAR_DIALOG(WrapperPlayServerClearDialog.class),
         SHOW_DIALOG(WrapperPlayServerShowDialog.class),
         DEBUG_BLOCK_VALUE(WrapperPlayServerDebugBlockValue.class),
         DEBUG_CHUNK_VALUE(WrapperPlayServerDebugChunkValue.class),
         DEBUG_ENTITY_VALUE(WrapperPlayServerDebugEntityValue.class),
         DEBUG_EVENT(WrapperPlayServerDebugEvent.class),
         GAME_TEST_HIGHLIGHT_POS(WrapperPlayServerGameTestHighlightPos.class);

         private static int INDEX = 0;
         private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap();
         private final int[] ids;
         private final Class<? extends PacketWrapper<?>> wrapper;

         private Server(@Nullable Class<? extends PacketWrapper<?>> wrapper) {
            this.ids = new int[PacketType.CLIENTBOUND_PLAY_VERSION_MAPPER.getVersions().length];
            Arrays.fill(this.ids, -1);
            this.wrapper = wrapper;
         }

         public Class<? extends PacketWrapper<?>> getWrapperClass() {
            return this.wrapper;
         }

         public int getId(ClientVersion version) {
            PacketType.prepare();
            int index = PacketType.CLIENTBOUND_PLAY_VERSION_MAPPER.getIndex(version);
            return this.ids[index];
         }

         @Nullable
         public static PacketTypeCommon getById(ClientVersion version, int packetId) {
            PacketType.prepare();
            int index = PacketType.CLIENTBOUND_PLAY_VERSION_MAPPER.getIndex(version);
            Map<Integer, PacketTypeCommon> map = (Map)PACKET_TYPE_ID_MAP.get((byte)index);
            return (PacketTypeCommon)map.get(packetId);
         }

         public PacketSide getSide() {
            return PacketSide.SERVER;
         }

         private static void loadPacketIds(Enum<?>[] enumConstants) {
            int index = INDEX;
            Enum[] var2 = enumConstants;
            int var3 = enumConstants.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Enum<?> constant = var2[var4];
               int id = constant.ordinal();
               PacketType.Play.Server value = valueOf(constant.name());
               value.ids[index] = id;
               Map<Integer, PacketTypeCommon> packetIdMap = (Map)PACKET_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
                  return new HashMap();
               });
               packetIdMap.put(id, value);
            }

            ++INDEX;
         }

         public static void load() {
            INDEX = 0;
            loadPacketIds(ClientboundPacketType_1_7_10.values());
            loadPacketIds(ClientboundPacketType_1_8.values());
            loadPacketIds(ClientboundPacketType_1_9.values());
            loadPacketIds(ClientboundPacketType_1_9_3.values());
            loadPacketIds(ClientboundPacketType_1_12.values());
            loadPacketIds(ClientboundPacketType_1_12_1.values());
            loadPacketIds(ClientboundPacketType_1_13.values());
            loadPacketIds(ClientboundPacketType_1_14.values());
            loadPacketIds(ClientboundPacketType_1_14_4.values());
            loadPacketIds(ClientboundPacketType_1_15.values());
            loadPacketIds(ClientboundPacketType_1_15_2.values());
            loadPacketIds(ClientboundPacketType_1_16.values());
            loadPacketIds(ClientboundPacketType_1_16_2.values());
            loadPacketIds(ClientboundPacketType_1_17.values());
            loadPacketIds(ClientboundPacketType_1_18.values());
            loadPacketIds(ClientboundPacketType_1_19.values());
            loadPacketIds(ClientboundPacketType_1_19_1.values());
            loadPacketIds(ClientboundPacketType_1_19_3.values());
            loadPacketIds(ClientboundPacketType_1_19_4.values());
            loadPacketIds(ClientboundPacketType_1_20_2.values());
            loadPacketIds(ClientboundPacketType_1_20_3.values());
            loadPacketIds(ClientboundPacketType_1_20_5.values());
            loadPacketIds(ClientboundPacketType_1_21.values());
            loadPacketIds(ClientboundPacketType_1_21_2.values());
            loadPacketIds(ClientboundPacketType_1_21_5.values());
            loadPacketIds(ClientboundPacketType_1_21_6.values());
            loadPacketIds(ClientboundPacketType_1_21_9.values());
         }

         // $FF: synthetic method
         private static PacketType.Play.Server[] $values() {
            return new PacketType.Play.Server[]{SET_COMPRESSION, MAP_CHUNK_BULK, UPDATE_ENTITY_NBT, UPDATE_SIGN, USE_BED, SPAWN_WEATHER_ENTITY, TITLE, WORLD_BORDER, COMBAT_EVENT, ENTITY_MOVEMENT, SPAWN_LIVING_ENTITY, SPAWN_PAINTING, SCULK_VIBRATION_SIGNAL, ACKNOWLEDGE_PLAYER_DIGGING, CHAT_PREVIEW_PACKET, NAMED_SOUND_EFFECT, PLAYER_CHAT_HEADER, PLAYER_INFO, DISPLAY_CHAT_PREVIEW, UPDATE_ENABLED_FEATURES, SPAWN_PLAYER, WINDOW_CONFIRMATION, SPAWN_ENTITY, SPAWN_EXPERIENCE_ORB, ENTITY_ANIMATION, STATISTICS, BLOCK_BREAK_ANIMATION, BLOCK_ENTITY_DATA, BLOCK_ACTION, BLOCK_CHANGE, BOSS_BAR, SERVER_DIFFICULTY, CLEAR_TITLES, TAB_COMPLETE, MULTI_BLOCK_CHANGE, DECLARE_COMMANDS, CLOSE_WINDOW, WINDOW_ITEMS, WINDOW_PROPERTY, SET_SLOT, SET_COOLDOWN, PLUGIN_MESSAGE, DISCONNECT, ENTITY_STATUS, EXPLOSION, UNLOAD_CHUNK, CHANGE_GAME_STATE, OPEN_HORSE_WINDOW, INITIALIZE_WORLD_BORDER, KEEP_ALIVE, CHUNK_DATA, EFFECT, PARTICLE, UPDATE_LIGHT, JOIN_GAME, MAP_DATA, MERCHANT_OFFERS, ENTITY_RELATIVE_MOVE, ENTITY_RELATIVE_MOVE_AND_ROTATION, ENTITY_ROTATION, VEHICLE_MOVE, OPEN_BOOK, OPEN_WINDOW, OPEN_SIGN_EDITOR, PING, CRAFT_RECIPE_RESPONSE, PLAYER_ABILITIES, END_COMBAT_EVENT, ENTER_COMBAT_EVENT, DEATH_COMBAT_EVENT, FACE_PLAYER, PLAYER_POSITION_AND_LOOK, UNLOCK_RECIPES, DESTROY_ENTITIES, REMOVE_ENTITY_EFFECT, RESOURCE_PACK_SEND, RESPAWN, ENTITY_HEAD_LOOK, SELECT_ADVANCEMENTS_TAB, ACTION_BAR, WORLD_BORDER_CENTER, WORLD_BORDER_LERP_SIZE, WORLD_BORDER_SIZE, WORLD_BORDER_WARNING_DELAY, WORLD_BORDER_WARNING_REACH, CAMERA, HELD_ITEM_CHANGE, UPDATE_VIEW_POSITION, UPDATE_VIEW_DISTANCE, SPAWN_POSITION, DISPLAY_SCOREBOARD, ENTITY_METADATA, ATTACH_ENTITY, ENTITY_VELOCITY, ENTITY_EQUIPMENT, SET_EXPERIENCE, UPDATE_HEALTH, SCOREBOARD_OBJECTIVE, SET_PASSENGERS, TEAMS, UPDATE_SCORE, UPDATE_SIMULATION_DISTANCE, SET_TITLE_SUBTITLE, TIME_UPDATE, SET_TITLE_TEXT, SET_TITLE_TIMES, ENTITY_SOUND_EFFECT, SOUND_EFFECT, STOP_SOUND, PLAYER_LIST_HEADER_AND_FOOTER, NBT_QUERY_RESPONSE, COLLECT_ITEM, ENTITY_TELEPORT, UPDATE_ADVANCEMENTS, UPDATE_ATTRIBUTES, ENTITY_EFFECT, DECLARE_RECIPES, TAGS, CHAT_MESSAGE, ACKNOWLEDGE_BLOCK_CHANGES, SERVER_DATA, SYSTEM_CHAT_MESSAGE, DELETE_CHAT, CUSTOM_CHAT_COMPLETIONS, DISGUISED_CHAT, PLAYER_INFO_REMOVE, PLAYER_INFO_UPDATE, DAMAGE_EVENT, HURT_ANIMATION, BUNDLE, CHUNK_BIOMES, CHUNK_BATCH_END, CHUNK_BATCH_BEGIN, DEBUG_PONG, CONFIGURATION_START, RESET_SCORE, RESOURCE_PACK_REMOVE, TICKING_STATE, TICKING_STEP, COOKIE_REQUEST, DEBUG_SAMPLE, STORE_COOKIE, TRANSFER, PROJECTILE_POWER, CUSTOM_REPORT_DETAILS, SERVER_LINKS, MOVE_MINECART, SET_CURSOR_ITEM, SET_PLAYER_INVENTORY, ENTITY_POSITION_SYNC, PLAYER_ROTATION, RECIPE_BOOK_ADD, RECIPE_BOOK_REMOVE, RECIPE_BOOK_SETTINGS, TEST_INSTANCE_BLOCK_STATUS, WAYPOINT, CLEAR_DIALOG, SHOW_DIALOG, DEBUG_BLOCK_VALUE, DEBUG_CHUNK_VALUE, DEBUG_ENTITY_VALUE, DEBUG_EVENT, GAME_TEST_HIGHLIGHT_POS};
         }
      }

      public static enum Client implements PacketTypeCommon, ServerBoundPacket {
         CHAT_PREVIEW(WrapperPlayClientChatPreview.class),
         TELEPORT_CONFIRM(WrapperPlayClientTeleportConfirm.class),
         QUERY_BLOCK_NBT(WrapperPlayClientQueryBlockNBT.class),
         SET_DIFFICULTY(WrapperPlayClientSetDifficulty.class),
         CHAT_MESSAGE(WrapperPlayClientChatMessage.class),
         CLIENT_STATUS(WrapperPlayClientClientStatus.class),
         CLIENT_SETTINGS(WrapperPlayClientSettings.class),
         TAB_COMPLETE(WrapperPlayClientTabComplete.class),
         WINDOW_CONFIRMATION(WrapperPlayClientWindowConfirmation.class),
         CLICK_WINDOW_BUTTON(WrapperPlayClientClickWindowButton.class),
         CLICK_WINDOW(WrapperPlayClientClickWindow.class),
         CLOSE_WINDOW(WrapperPlayClientCloseWindow.class),
         PLUGIN_MESSAGE(WrapperPlayClientPluginMessage.class),
         EDIT_BOOK(WrapperPlayClientEditBook.class),
         QUERY_ENTITY_NBT(WrapperPlayClientQueryEntityNBT.class),
         INTERACT_ENTITY(WrapperPlayClientInteractEntity.class),
         GENERATE_STRUCTURE(WrapperPlayClientGenerateStructure.class),
         KEEP_ALIVE(WrapperPlayClientKeepAlive.class),
         LOCK_DIFFICULTY(WrapperPlayClientLockDifficulty.class),
         PLAYER_POSITION(WrapperPlayClientPlayerPosition.class),
         PLAYER_POSITION_AND_ROTATION(WrapperPlayClientPlayerPositionAndRotation.class),
         PLAYER_ROTATION(WrapperPlayClientPlayerRotation.class),
         PLAYER_FLYING(WrapperPlayClientPlayerFlying.class),
         VEHICLE_MOVE(WrapperPlayClientVehicleMove.class),
         STEER_BOAT(WrapperPlayClientSteerBoat.class),
         @ApiStatus.Obsolete
         PICK_ITEM(WrapperPlayClientPickItem.class),
         CRAFT_RECIPE_REQUEST(WrapperPlayClientCraftRecipeRequest.class),
         PLAYER_ABILITIES(WrapperPlayClientPlayerAbilities.class),
         PLAYER_DIGGING(WrapperPlayClientPlayerDigging.class),
         ENTITY_ACTION(WrapperPlayClientEntityAction.class),
         @ApiStatus.Obsolete
         STEER_VEHICLE(WrapperPlayClientSteerVehicle.class),
         PONG(WrapperPlayClientPong.class),
         RECIPE_BOOK_DATA((Class)null),
         SET_DISPLAYED_RECIPE(WrapperPlayClientSetDisplayedRecipe.class),
         SET_RECIPE_BOOK_STATE(WrapperPlayClientSetRecipeBookState.class),
         NAME_ITEM(WrapperPlayClientNameItem.class),
         RESOURCE_PACK_STATUS(WrapperPlayClientResourcePackStatus.class),
         ADVANCEMENT_TAB(WrapperPlayClientAdvancementTab.class),
         SELECT_TRADE(WrapperPlayClientSelectTrade.class),
         SET_BEACON_EFFECT(WrapperPlayClientSetBeaconEffect.class),
         HELD_ITEM_CHANGE(WrapperPlayClientHeldItemChange.class),
         UPDATE_COMMAND_BLOCK(WrapperPlayClientUpdateCommandBlock.class),
         UPDATE_COMMAND_BLOCK_MINECART(WrapperPlayClientUpdateCommandBlockMinecart.class),
         CREATIVE_INVENTORY_ACTION(WrapperPlayClientCreativeInventoryAction.class),
         UPDATE_JIGSAW_BLOCK(WrapperPlayClientUpdateJigsawBlock.class),
         UPDATE_STRUCTURE_BLOCK(WrapperPlayClientSetStructureBlock.class),
         UPDATE_SIGN(WrapperPlayClientUpdateSign.class),
         ANIMATION(WrapperPlayClientAnimation.class),
         SPECTATE(WrapperPlayClientSpectate.class),
         PLAYER_BLOCK_PLACEMENT(WrapperPlayClientPlayerBlockPlacement.class),
         USE_ITEM(WrapperPlayClientUseItem.class),
         CHAT_COMMAND(WrapperPlayClientChatCommand.class),
         CHAT_ACK(WrapperPlayClientChatAck.class),
         CHAT_SESSION_UPDATE(WrapperPlayClientChatSessionUpdate.class),
         CHUNK_BATCH_ACK(WrapperPlayClientChunkBatchAck.class),
         CONFIGURATION_ACK(WrapperPlayClientConfigurationAck.class),
         DEBUG_PING(WrapperPlayClientDebugPing.class),
         SLOT_STATE_CHANGE(WrapperPlayClientSlotStateChange.class),
         CHAT_COMMAND_UNSIGNED(WrapperPlayClientChatCommandUnsigned.class),
         COOKIE_RESPONSE(WrapperPlayClientCookieResponse.class),
         @ApiStatus.Obsolete
         DEBUG_SAMPLE_SUBSCRIPTION(WrapperPlayClientDebugSampleSubscription.class),
         CLIENT_TICK_END(WrapperPlayClientClientTickEnd.class),
         SELECT_BUNDLE_ITEM(WrapperPlayClientSelectBundleItem.class),
         PLAYER_INPUT(WrapperPlayClientPlayerInput.class),
         PICK_ITEM_FROM_BLOCK(WrapperPlayClientPickItemFromBlock.class),
         PICK_ITEM_FROM_ENTITY(WrapperPlayClientPickItemFromEntity.class),
         PLAYER_LOADED(WrapperPlayClientPlayerLoaded.class),
         SET_TEST_BLOCK(WrapperPlayClientSetTestBlock.class),
         TEST_INSTANCE_BLOCK_ACTION(WrapperPlayClientTestInstanceBlockAction.class),
         CHANGE_GAME_MODE(WrapperPlayClientChangeGameMode.class),
         CUSTOM_CLICK_ACTION(WrapperPlayClientCustomClickAction.class),
         DEBUG_SUBSCRIPTION_REQUEST(WrapperPlayClientDebugSubscriptionRequest.class);

         private static int INDEX = 0;
         private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap();
         private final int[] ids;
         private final Class<? extends PacketWrapper<?>> wrapper;

         private Client(@Nullable Class<? extends PacketWrapper<?>> wrapper) {
            this.ids = new int[PacketType.SERVERBOUND_PLAY_VERSION_MAPPER.getVersions().length];
            Arrays.fill(this.ids, -1);
            this.wrapper = wrapper;
         }

         public Class<? extends PacketWrapper<?>> getWrapperClass() {
            return this.wrapper;
         }

         @Nullable
         public static PacketTypeCommon getById(ClientVersion version, int packetId) {
            PacketType.prepare();
            int index = PacketType.SERVERBOUND_PLAY_VERSION_MAPPER.getIndex(version);
            Map<Integer, PacketTypeCommon> packetIdMap = (Map)PACKET_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
               return new HashMap();
            });
            return (PacketTypeCommon)packetIdMap.get(packetId);
         }

         private static void loadPacketIds(Enum<?>[] enumConstants) {
            int index = INDEX;
            Enum[] var2 = enumConstants;
            int var3 = enumConstants.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Enum<?> constant = var2[var4];
               int id = constant.ordinal();
               PacketType.Play.Client value = valueOf(constant.name());
               value.ids[index] = id;
               Map<Integer, PacketTypeCommon> packetIdMap = (Map)PACKET_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
                  return new HashMap();
               });
               packetIdMap.put(id, value);
            }

            ++INDEX;
         }

         public static void load() {
            INDEX = 0;
            loadPacketIds(ServerboundPacketType_1_7_10.values());
            loadPacketIds(ServerboundPacketType_1_8.values());
            loadPacketIds(ServerboundPacketType_1_9.values());
            loadPacketIds(ServerboundPacketType_1_12.values());
            loadPacketIds(ServerboundPacketType_1_12_1.values());
            loadPacketIds(ServerboundPacketType_1_13.values());
            loadPacketIds(ServerboundPacketType_1_14.values());
            loadPacketIds(ServerboundPacketType_1_15_2.values());
            loadPacketIds(ServerboundPacketType_1_16.values());
            loadPacketIds(ServerboundPacketType_1_16_2.values());
            loadPacketIds(ServerboundPacketType_1_17.values());
            loadPacketIds(ServerboundPacketType_1_19.values());
            loadPacketIds(ServerboundPacketType_1_19_1.values());
            loadPacketIds(ServerboundPacketType_1_19_3.values());
            loadPacketIds(ServerboundPacketType_1_19_4.values());
            loadPacketIds(ServerboundPacketType_1_20_2.values());
            loadPacketIds(ServerboundPacketType_1_20_3.values());
            loadPacketIds(ServerboundPacketType_1_20_5.values());
            loadPacketIds(ServerboundPacketType_1_21_2.values());
            loadPacketIds(ServerboundPacketType_1_21_4.values());
            loadPacketIds(ServerboundPacketType_1_21_5.values());
            loadPacketIds(ServerboundPacketType_1_21_6.values());
            loadPacketIds(ServerboundPacketType_1_21_9.values());
         }

         public int getId(ClientVersion version) {
            PacketType.prepare();
            int index = PacketType.SERVERBOUND_PLAY_VERSION_MAPPER.getIndex(version);
            return this.ids[index];
         }

         public PacketSide getSide() {
            return PacketSide.CLIENT;
         }

         // $FF: synthetic method
         private static PacketType.Play.Client[] $values() {
            return new PacketType.Play.Client[]{CHAT_PREVIEW, TELEPORT_CONFIRM, QUERY_BLOCK_NBT, SET_DIFFICULTY, CHAT_MESSAGE, CLIENT_STATUS, CLIENT_SETTINGS, TAB_COMPLETE, WINDOW_CONFIRMATION, CLICK_WINDOW_BUTTON, CLICK_WINDOW, CLOSE_WINDOW, PLUGIN_MESSAGE, EDIT_BOOK, QUERY_ENTITY_NBT, INTERACT_ENTITY, GENERATE_STRUCTURE, KEEP_ALIVE, LOCK_DIFFICULTY, PLAYER_POSITION, PLAYER_POSITION_AND_ROTATION, PLAYER_ROTATION, PLAYER_FLYING, VEHICLE_MOVE, STEER_BOAT, PICK_ITEM, CRAFT_RECIPE_REQUEST, PLAYER_ABILITIES, PLAYER_DIGGING, ENTITY_ACTION, STEER_VEHICLE, PONG, RECIPE_BOOK_DATA, SET_DISPLAYED_RECIPE, SET_RECIPE_BOOK_STATE, NAME_ITEM, RESOURCE_PACK_STATUS, ADVANCEMENT_TAB, SELECT_TRADE, SET_BEACON_EFFECT, HELD_ITEM_CHANGE, UPDATE_COMMAND_BLOCK, UPDATE_COMMAND_BLOCK_MINECART, CREATIVE_INVENTORY_ACTION, UPDATE_JIGSAW_BLOCK, UPDATE_STRUCTURE_BLOCK, UPDATE_SIGN, ANIMATION, SPECTATE, PLAYER_BLOCK_PLACEMENT, USE_ITEM, CHAT_COMMAND, CHAT_ACK, CHAT_SESSION_UPDATE, CHUNK_BATCH_ACK, CONFIGURATION_ACK, DEBUG_PING, SLOT_STATE_CHANGE, CHAT_COMMAND_UNSIGNED, COOKIE_RESPONSE, DEBUG_SAMPLE_SUBSCRIPTION, CLIENT_TICK_END, SELECT_BUNDLE_ITEM, PLAYER_INPUT, PICK_ITEM_FROM_BLOCK, PICK_ITEM_FROM_ENTITY, PLAYER_LOADED, SET_TEST_BLOCK, TEST_INSTANCE_BLOCK_ACTION, CHANGE_GAME_MODE, CUSTOM_CLICK_ACTION, DEBUG_SUBSCRIPTION_REQUEST};
         }
      }
   }

   public static class Configuration {
      public static enum Server implements PacketTypeCommon, ClientBoundPacket {
         PLUGIN_MESSAGE(WrapperConfigServerPluginMessage.class),
         DISCONNECT(WrapperConfigServerDisconnect.class),
         CONFIGURATION_END(WrapperConfigServerConfigurationEnd.class),
         KEEP_ALIVE(WrapperConfigServerKeepAlive.class),
         PING(WrapperConfigServerPing.class),
         REGISTRY_DATA(WrapperConfigServerRegistryData.class),
         RESOURCE_PACK_SEND(WrapperConfigServerResourcePackSend.class),
         UPDATE_ENABLED_FEATURES(WrapperConfigServerUpdateEnabledFeatures.class),
         UPDATE_TAGS((Class)null),
         RESOURCE_PACK_REMOVE(WrapperConfigServerResourcePackRemove.class),
         COOKIE_REQUEST(WrapperConfigServerCookieRequest.class),
         RESET_CHAT(WrapperConfigServerResetChat.class),
         STORE_COOKIE(WrapperConfigServerStoreCookie.class),
         TRANSFER(WrapperConfigServerTransfer.class),
         SELECT_KNOWN_PACKS(WrapperConfigServerSelectKnownPacks.class),
         CUSTOM_REPORT_DETAILS(WrapperConfigServerCustomReportDetails.class),
         SERVER_LINKS(WrapperConfigServerServerLinks.class),
         CLEAR_DIALOG(WrapperConfigServerClearDialog.class),
         SHOW_DIALOG(WrapperConfigServerShowDialog.class),
         CODE_OF_CONDUCT(WrapperConfigServerCodeOfConduct.class);

         private static int INDEX = 0;
         private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap();
         private final int[] ids;
         private final Class<? extends PacketWrapper<?>> wrapper;

         private Server(@Nullable Class<? extends PacketWrapper<?>> wrapper) {
            this.ids = new int[PacketType.CLIENTBOUND_CONFIG_VERSION_MAPPER.getVersions().length];
            Arrays.fill(this.ids, -1);
            this.wrapper = wrapper;
         }

         public Class<? extends PacketWrapper<?>> getWrapperClass() {
            return this.wrapper;
         }

         public static void load() {
            INDEX = 0;
            loadPacketIds(ClientboundConfigPacketType_1_20_2.values());
            loadPacketIds(ClientboundConfigPacketType_1_20_3.values());
            loadPacketIds(ClientboundConfigPacketType_1_20_5.values());
            loadPacketIds(ClientboundConfigPacketType_1_21.values());
            loadPacketIds(ClientboundConfigPacketType_1_21_6.values());
            loadPacketIds(ClientboundConfigPacketType_1_21_9.values());
         }

         private static void loadPacketIds(Enum<?>[] enumConstants) {
            int index = INDEX;
            Enum[] var2 = enumConstants;
            int var3 = enumConstants.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Enum<?> constant = var2[var4];
               int id = constant.ordinal();
               PacketType.Configuration.Server value = valueOf(constant.name());
               value.ids[index] = id;
               Map<Integer, PacketTypeCommon> packetIdMap = (Map)PACKET_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
                  return new HashMap();
               });
               packetIdMap.put(id, value);
            }

            ++INDEX;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetId) {
            return getById(ClientVersion.getLatest(), packetId);
         }

         @Nullable
         public static PacketTypeCommon getById(ClientVersion version, int packetId) {
            PacketType.prepare();
            int index = PacketType.CLIENTBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
            Map<Integer, PacketTypeCommon> map = (Map)PACKET_TYPE_ID_MAP.get((byte)index);
            return (PacketTypeCommon)map.get(packetId);
         }

         /** @deprecated */
         @Deprecated
         public int getId() {
            return this.getId(ClientVersion.getLatest());
         }

         public int getId(ClientVersion version) {
            PacketType.prepare();
            int index = PacketType.CLIENTBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
            return this.ids[index];
         }

         public PacketSide getSide() {
            return PacketSide.SERVER;
         }

         // $FF: synthetic method
         private static PacketType.Configuration.Server[] $values() {
            return new PacketType.Configuration.Server[]{PLUGIN_MESSAGE, DISCONNECT, CONFIGURATION_END, KEEP_ALIVE, PING, REGISTRY_DATA, RESOURCE_PACK_SEND, UPDATE_ENABLED_FEATURES, UPDATE_TAGS, RESOURCE_PACK_REMOVE, COOKIE_REQUEST, RESET_CHAT, STORE_COOKIE, TRANSFER, SELECT_KNOWN_PACKS, CUSTOM_REPORT_DETAILS, SERVER_LINKS, CLEAR_DIALOG, SHOW_DIALOG, CODE_OF_CONDUCT};
         }
      }

      public static enum Client implements PacketTypeCommon, ServerBoundPacket {
         CLIENT_SETTINGS(WrapperConfigClientSettings.class),
         PLUGIN_MESSAGE(WrapperConfigClientPluginMessage.class),
         CONFIGURATION_END_ACK(WrapperConfigClientConfigurationEndAck.class),
         KEEP_ALIVE(WrapperConfigClientKeepAlive.class),
         PONG(WrapperConfigClientPong.class),
         RESOURCE_PACK_STATUS(WrapperConfigClientResourcePackStatus.class),
         COOKIE_RESPONSE(WrapperConfigClientCookieResponse.class),
         SELECT_KNOWN_PACKS(WrapperConfigClientSelectKnownPacks.class),
         CUSTOM_CLICK_ACTION(WrapperConfigClientCustomClickAction.class),
         ACCEPT_CODE_OF_CONDUCT(WrapperConfigClientAcceptCodeOfConduct.class);

         private static int INDEX = 0;
         private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap();
         private final int[] ids;
         private final Class<? extends PacketWrapper<?>> wrapper;

         private Client(@Nullable Class<? extends PacketWrapper<?>> wrapper) {
            this.ids = new int[PacketType.SERVERBOUND_CONFIG_VERSION_MAPPER.getVersions().length];
            Arrays.fill(this.ids, -1);
            this.wrapper = wrapper;
         }

         public Class<? extends PacketWrapper<?>> getWrapperClass() {
            return this.wrapper;
         }

         public static void load() {
            INDEX = 0;
            loadPacketIds(ServerboundConfigPacketType_1_20_2.values());
            loadPacketIds(ServerboundConfigPacketType_1_20_5.values());
            loadPacketIds(ServerboundConfigPacketType_1_21_6.values());
            loadPacketIds(ServerboundConfigPacketType_1_21_9.values());
         }

         private static void loadPacketIds(Enum<?>[] enumConstants) {
            int index = INDEX;
            Enum[] var2 = enumConstants;
            int var3 = enumConstants.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Enum<?> constant = var2[var4];
               int id = constant.ordinal();
               PacketType.Configuration.Client value = valueOf(constant.name());
               value.ids[index] = id;
               Map<Integer, PacketTypeCommon> packetIdMap = (Map)PACKET_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
                  return new HashMap();
               });
               packetIdMap.put(id, value);
            }

            ++INDEX;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetId) {
            return getById(ClientVersion.getLatest(), packetId);
         }

         @Nullable
         public static PacketTypeCommon getById(ClientVersion version, int packetId) {
            PacketType.prepare();
            int index = PacketType.SERVERBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
            Map<Integer, PacketTypeCommon> map = (Map)PACKET_TYPE_ID_MAP.get((byte)index);
            return (PacketTypeCommon)map.get(packetId);
         }

         /** @deprecated */
         @Deprecated
         public int getId() {
            return this.getId(ClientVersion.getLatest());
         }

         public int getId(ClientVersion version) {
            PacketType.prepare();
            int index = PacketType.SERVERBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
            return this.ids[index];
         }

         public PacketSide getSide() {
            return PacketSide.CLIENT;
         }

         // $FF: synthetic method
         private static PacketType.Configuration.Client[] $values() {
            return new PacketType.Configuration.Client[]{CLIENT_SETTINGS, PLUGIN_MESSAGE, CONFIGURATION_END_ACK, KEEP_ALIVE, PONG, RESOURCE_PACK_STATUS, COOKIE_RESPONSE, SELECT_KNOWN_PACKS, CUSTOM_CLICK_ACTION, ACCEPT_CODE_OF_CONDUCT};
         }
      }
   }

   public static class Handshaking {
      public static enum Server implements PacketTypeConstant, ClientBoundPacket {
         LEGACY_SERVER_LIST_RESPONSE(254, (Class)null);

         private final int id;
         private final Class<? extends PacketWrapper<?>> wrapperClass;

         private Server(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
            this.id = id;
            this.wrapperClass = wrapperClass;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetID) {
            return packetID == 254 ? LEGACY_SERVER_LIST_RESPONSE : null;
         }

         public Class<? extends PacketWrapper<?>> getWrapperClass() {
            return this.wrapperClass;
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.SERVER;
         }

         // $FF: synthetic method
         private static PacketType.Handshaking.Server[] $values() {
            return new PacketType.Handshaking.Server[]{LEGACY_SERVER_LIST_RESPONSE};
         }
      }

      public static enum Client implements PacketTypeConstant, ServerBoundPacket {
         HANDSHAKE(0, WrapperHandshakingClientHandshake.class),
         LEGACY_SERVER_LIST_PING(254, (Class)null);

         private final int id;
         private final Class<? extends PacketWrapper<?>> wrapperClass;

         private Client(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
            this.id = id;
            this.wrapperClass = wrapperClass;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetID) {
            if (packetID == 0) {
               return HANDSHAKE;
            } else {
               return packetID == 254 ? LEGACY_SERVER_LIST_PING : null;
            }
         }

         public int getId() {
            return this.id;
         }

         public Class<? extends PacketWrapper<?>> getWrapperClass() {
            return this.wrapperClass;
         }

         public PacketSide getSide() {
            return PacketSide.CLIENT;
         }

         // $FF: synthetic method
         private static PacketType.Handshaking.Client[] $values() {
            return new PacketType.Handshaking.Client[]{HANDSHAKE, LEGACY_SERVER_LIST_PING};
         }
      }
   }

   public static class Status {
      public static enum Server implements PacketTypeConstant, ClientBoundPacket {
         RESPONSE(0, WrapperStatusServerResponse.class),
         PONG(1, WrapperStatusServerPong.class);

         private final int id;
         private final Class<? extends PacketWrapper<?>> wrapperClass;

         private Server(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
            this.id = id;
            this.wrapperClass = wrapperClass;
         }

         public Class<? extends PacketWrapper<?>> getWrapperClass() {
            return this.wrapperClass;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetID) {
            if (packetID == 0) {
               return RESPONSE;
            } else {
               return packetID == 1 ? PONG : null;
            }
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.SERVER;
         }

         // $FF: synthetic method
         private static PacketType.Status.Server[] $values() {
            return new PacketType.Status.Server[]{RESPONSE, PONG};
         }
      }

      public static enum Client implements PacketTypeConstant, ServerBoundPacket {
         REQUEST(0, WrapperStatusClientRequest.class),
         PING(1, WrapperStatusClientPing.class);

         private final int id;
         private final Class<? extends PacketWrapper<?>> wrapperClass;

         private Client(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
            this.id = id;
            this.wrapperClass = wrapperClass;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetId) {
            if (packetId == 0) {
               return REQUEST;
            } else {
               return packetId == 1 ? PING : null;
            }
         }

         public Class<? extends PacketWrapper<?>> getWrapperClass() {
            return this.wrapperClass;
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.CLIENT;
         }

         // $FF: synthetic method
         private static PacketType.Status.Client[] $values() {
            return new PacketType.Status.Client[]{REQUEST, PING};
         }
      }
   }

   public static class Login {
      public static enum Server implements PacketTypeConstant, ClientBoundPacket {
         DISCONNECT(0, WrapperLoginServerDisconnect.class),
         ENCRYPTION_REQUEST(1, WrapperLoginServerEncryptionRequest.class),
         LOGIN_SUCCESS(2, WrapperLoginServerLoginSuccess.class),
         SET_COMPRESSION(3, WrapperLoginServerSetCompression.class),
         LOGIN_PLUGIN_REQUEST(4, WrapperLoginServerPluginRequest.class),
         COOKIE_REQUEST(5, WrapperLoginServerCookieRequest.class);

         private final int id;
         private final Class<? extends PacketWrapper<?>> wrapperClass;

         private Server(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
            this.id = id;
            this.wrapperClass = wrapperClass;
         }

         public Class<? extends PacketWrapper<?>> getWrapperClass() {
            return this.wrapperClass;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetID) {
            switch(packetID) {
            case 0:
               return DISCONNECT;
            case 1:
               return ENCRYPTION_REQUEST;
            case 2:
               return LOGIN_SUCCESS;
            case 3:
               return SET_COMPRESSION;
            case 4:
               return LOGIN_PLUGIN_REQUEST;
            case 5:
               return COOKIE_REQUEST;
            default:
               return null;
            }
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.SERVER;
         }

         // $FF: synthetic method
         private static PacketType.Login.Server[] $values() {
            return new PacketType.Login.Server[]{DISCONNECT, ENCRYPTION_REQUEST, LOGIN_SUCCESS, SET_COMPRESSION, LOGIN_PLUGIN_REQUEST, COOKIE_REQUEST};
         }
      }

      public static enum Client implements PacketTypeConstant, ServerBoundPacket {
         LOGIN_START(0, WrapperLoginClientLoginStart.class),
         ENCRYPTION_RESPONSE(1, WrapperLoginClientEncryptionResponse.class),
         LOGIN_PLUGIN_RESPONSE(2, WrapperLoginClientPluginResponse.class),
         LOGIN_SUCCESS_ACK(3, WrapperLoginClientLoginSuccessAck.class),
         COOKIE_RESPONSE(4, WrapperLoginClientCookieResponse.class);

         private final int id;
         private final Class<? extends PacketWrapper<?>> wrapperClass;

         private Client(int id, @Nullable Class<? extends PacketWrapper<?>> wrapperClass) {
            this.id = id;
            this.wrapperClass = wrapperClass;
         }

         public Class<? extends PacketWrapper<?>> getWrapperClass() {
            return this.wrapperClass;
         }

         @Nullable
         public static PacketTypeCommon getById(int packetID) {
            switch(packetID) {
            case 0:
               return LOGIN_START;
            case 1:
               return ENCRYPTION_RESPONSE;
            case 2:
               return LOGIN_PLUGIN_RESPONSE;
            case 3:
               return LOGIN_SUCCESS_ACK;
            case 4:
               return COOKIE_RESPONSE;
            default:
               return null;
            }
         }

         public int getId() {
            return this.id;
         }

         public PacketSide getSide() {
            return PacketSide.CLIENT;
         }

         // $FF: synthetic method
         private static PacketType.Login.Client[] $values() {
            return new PacketType.Login.Client[]{LOGIN_START, ENCRYPTION_RESPONSE, LOGIN_PLUGIN_RESPONSE, LOGIN_SUCCESS_ACK, COOKIE_RESPONSE};
         }
      }
   }
}
