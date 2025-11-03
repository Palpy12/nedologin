package ru.marduk.nedologin.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import ru.marduk.nedologin.Nedologin;

public final class ClientLoader {

    public static void clientSetup() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ClientConnection connection = handler.getConnection();

            // Проверяем, что это не локальное соединение (singleplayer)
            if (connection.isLocal()) return;

            Nedologin.logger.debug("Sending login packet to the server...");

            // Формируем данные пакета (аналог MessageLogin)
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeString(PasswordHolder.instance().password());

            // Отправляем пакет на сервер
            //ClientPlayNetworking.send(LOGIN_PACKET_ID, buf);
        });
    }
}
