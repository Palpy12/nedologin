package ru.marduk.nedologin.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record MessageRequestLogin() implements CustomPayload {
    public static final CustomPayload.Id<MessageRequestLogin> ID = new CustomPayload.Id<>(NetworkLoader.REQUEST_LOGIN);
    public static final PacketCodec<RegistryByteBuf, MessageRequestLogin> CODEC = PacketCodec.unit(new MessageRequestLogin());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}