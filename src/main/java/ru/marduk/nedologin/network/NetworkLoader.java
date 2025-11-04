package ru.marduk.nedologin.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import ru.marduk.nedologin.Nedologin;
import ru.marduk.nedologin.server.handler.PlayerLoginHandler;

public class NetworkLoader {
    public static final Identifier REQUEST_LOGIN = Identifier.of("nedologin", "request_login");
    public static final Identifier LOGIN = Identifier.of("nedologin", "login");

    public static void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(MessageLogin.ID, MessageLogin.CODEC);
        PayloadTypeRegistry.playS2C().register(MessageRequestLogin.ID, MessageRequestLogin.CODEC);
    }

    public static void registerServerHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(MessageLogin.ID, (payload, context) -> {
            context.server().execute(() -> {
                Nedologin.logger.info(context.player().getGameProfile().name());
                PlayerLoginHandler.instance().login(context.player().getGameProfile().name(), payload.password());
            });
        });
    }

    //private NetworkLoader() {
    //    throw new UnsupportedOperationException("No instance");
    //}
}
