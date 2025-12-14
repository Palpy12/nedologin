package ru.marduk.nedologin.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import ru.marduk.nedologin.Nedologin;
import ru.marduk.nedologin.server.handler.PlayerLoginHandler;

public record MessageLogin(String password) implements CustomPayload {
    public static final CustomPayload.Id<MessageLogin> ID = new CustomPayload.Id<>(NetworkLoader.LOGIN);
    public static final PacketCodec<RegistryByteBuf, MessageLogin> CODEC = PacketCodec.tuple(PacketCodecs.STRING, MessageLogin::password, MessageLogin::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<MessageLogin> {
        @Override
        public void receive(MessageLogin payload, ServerPlayNetworking.Context context) {
            context.server().execute(() -> {
                Nedologin.logger.info(context.player().getGameProfile().name());
                PlayerLoginHandler.instance().login(context.player().getGameProfile().name(), payload.password());
            });
        }
    }

}
