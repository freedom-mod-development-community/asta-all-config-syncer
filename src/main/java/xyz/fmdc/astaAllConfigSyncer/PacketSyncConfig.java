/// Copyright (c) 2022 anatawa12 and other contributors
/// This file is part of *All Config Syncer, released under MIT License
/// See LICENSE at https://github.com/freedom-mod-development-community/asta-all-config-syncer for more details

package xyz.fmdc.astaAllConfigSyncer;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

import java.nio.charset.StandardCharsets;

public final class PacketSyncConfig implements IMessage {
    public AstaAllModConfigField mod;
    public String config;

    public PacketSyncConfig() {
    }

    public PacketSyncConfig(AstaAllModConfigField mod, String config) {
        this.mod = mod;
        this.config = config;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        mod = AstaAllModConfigField.getById(buf.readByte());
        byte[] read = new byte[buf.readInt()];
        buf.readBytes(read, 0, read.length);
        config = new String(read, StandardCharsets.UTF_8);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(mod.id);
        byte[] bytes = config.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public static final IMessageHandler<PacketSyncConfig, IMessage> HANDLER = (message, ctx) -> {
        AstaAllConfigSyncer.setConfig(message.mod, message.config);

        return null;
    };
}
