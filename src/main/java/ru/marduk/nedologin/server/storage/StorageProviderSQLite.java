package ru.marduk.nedologin.server.storage;

import net.fabricmc.loader.api.FabricLoader;

import java.sql.DriverManager;
import java.sql.SQLException;

public final class StorageProviderSQLite extends StorageProviderSQL {
    public StorageProviderSQLite() throws SQLException {
        super(DriverManager.getConnection("jdbc:sqlite:" + FabricLoader.getInstance().getGameDir().resolve("nl_entries.db")));
    }
}