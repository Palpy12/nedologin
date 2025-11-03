package ru.marduk.nedologin.network;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import static ru.marduk.nedologin.network.NetworkLoader.LOGIN;

public class MessageLogin {
    public static void send(String password) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(password);
        ClientPlayNetworking.send(LOGIN, buf);
    }

    public static void handle(ServerPlayerEntity player, String password) {
        // Логика обработки пароля
    }
}
