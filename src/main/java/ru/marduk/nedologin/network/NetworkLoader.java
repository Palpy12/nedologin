package ru.marduk.nedologin.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class NetworkLoader {
    public static final Identifier REQUEST_LOGIN = Identifier.of("nedologin", "request_login");
    public static final Identifier LOGIN = Identifier.of("nedologin", "login");

    public static void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(MessageLogin.ID, MessageLogin.CODEC);
    }

    public static void registerServerHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(MessageLogin.ID, new MessageLogin.Receiver());
    }
}
