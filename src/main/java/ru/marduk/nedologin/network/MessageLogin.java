package ru.marduk.nedologin.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record MessageLogin(String password) implements CustomPayload {
    public static final CustomPayload.Id<MessageLogin> ID = new CustomPayload.Id<>(NetworkLoader.LOGIN);
    public static final PacketCodec<RegistryByteBuf, MessageLogin> CODEC = PacketCodec.tuple(PacketCodecs.STRING, MessageLogin::password, MessageLogin::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
