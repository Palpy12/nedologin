package ru.marduk.nedologin;


import net.minecraft.server.MinecraftServer;

public final class NLConstants {
    public static final String MODID = "nedologin";
    public static MinecraftServer minecraftServer;
    public static final int MARIADB_TIMEOUT = 10;

    public static void setServer(MinecraftServer server) {
        minecraftServer = server;
    }
}
