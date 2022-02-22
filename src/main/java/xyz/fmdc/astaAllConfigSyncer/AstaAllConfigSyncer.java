/// Copyright (c) 2022 anatawa12 and other contributors
/// This file is part of *All Config Syncer, released under MIT License
/// See LICENSE at https://github.com/freedom-mod-development-community/asta-all-config-syncer for more details

package xyz.fmdc.astaAllConfigSyncer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@Mod(modid = "asta-all-config-syncer")
public class AstaAllConfigSyncer {
    public static final Logger LOGGER = LogManager.getLogger("AstaAllConfigSyncer");
    private static final @Nonnull Map<AstaAllModConfigField, String> clientCache = new HashMap<>();
    private static SimpleNetworkWrapper NETWORK;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        AstaAllMod.MineAll.fields = ModFields.getMod("net.minecraft.scalar.mineall.mod_MineAllSMP");
        AstaAllMod.CutAll.fields = ModFields.getMod("net.minecraft.scalar.cutall.mod_CutAllSMP");
        AstaAllMod.DigAll.fields = ModFields.getMod("net.minecraft.scalar.digall.mod_DigAllSMP");
        LOGGER.info(AstaAllMod.MineAll.fields != null ? "MineAll enabled" : "MineAll disabled");
        LOGGER.info(AstaAllMod.CutAll.fields != null ? "CutAll enabled" : "CutAll disabled");
        LOGGER.info(AstaAllMod.DigAll.fields != null ? "DigAll enabled" : "DigAll disabled");

        NETWORK = new SimpleNetworkWrapper("asta-all-config-sync");
        NETWORK.registerMessage(PacketSyncConfig.HANDLER, PacketSyncConfig.class, 1, Side.CLIENT);
    }

    @SubscribeEvent
    public void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        LOGGER.info("PlayerLoggedInEvent");

        for (AstaAllModConfigField field : AstaAllModConfigField.values()) {
            if (field.mod.fields != null) {
                if (field.isBlock) {
                    NETWORK.sendTo(new PacketSyncConfig(field, field.mod.fields.getBlocks()), (EntityPlayerMP) event.player);
                } else {
                    NETWORK.sendTo(new PacketSyncConfig(field, field.mod.fields.getItems()), (EntityPlayerMP) event.player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (!event.getWorld().isRemote) return;
        LOGGER.info("WorldEvent.Unload");

        for (Map.Entry<AstaAllModConfigField, String> entry : clientCache.entrySet()) {
            entry.getKey().set(entry.getValue());
        }

        clientCache.clear();
    }

    public static void setConfig(AstaAllModConfigField field, String config) {
        if (field.mod.fields != null) {
            LOGGER.info("setting {} to '{}'", field, config);
            clientCache.put(field, field.get());
            field.set(config);
        }
    }
}
