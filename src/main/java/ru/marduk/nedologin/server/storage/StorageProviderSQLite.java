package ru.marduk.nedologin.server.storage;

import ru.marduk.nedologin.utils.ServerUtil;

import java.sql.DriverManager;
import java.sql.SQLException;

public final class StorageProviderSQLite extends StorageProviderSQL {


    public StorageProviderSQLite() throws SQLException {
        super(DriverManager.getConnection("jdbc:sqlite:" + ServerUtil.getServerRoot().resolve("nl_entries.db")));
    }
}