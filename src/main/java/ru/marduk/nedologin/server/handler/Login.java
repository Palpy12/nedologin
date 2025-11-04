package ru.marduk.nedologin.server.handler;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public final class Login {
    public final String name, gamemode;
    public final long time;
    public final double posX, posY, posZ;

    Login(ServerPlayerEntity player) {
        this.name = player.getGameProfile().name().toLowerCase();
        this.gamemode = player.getGameMode().getId();
        this.time = System.currentTimeMillis();
        this.posX = player.getX();
        this.posY = player.getY();
        this.posZ = player.getZ();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return time == login.time && Double.compare(login.posX, posX) == 0 && Double.compare(login.posY, posY) == 0 && Double.compare(login.posZ, posZ) == 0 && name.equals(login.name) && gamemode.equals(login.gamemode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gamemode, time, posX, posY, posZ);
    }
}