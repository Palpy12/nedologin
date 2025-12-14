package ru.marduk.nedologin.server.storage;

import ru.marduk.nedologin.Nedologin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class StorageProviderSQL {
    protected Connection conn;

    public StorageProviderSQL(Connection conn) {
        this.conn = conn;
        try {
            conn.createStatement()
                    .execute("""
                            CREATE TABLE IF NOT EXISTS nl_entries
                            (
                                username        varchar(32),
                                defaultGameType tinyint,
                                password        varchar(255),
                                PRIMARY KEY (username)
                            )""");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public boolean checkPassword(String username, String password) {
        try {
            checkValidity();
            PreparedStatement st = conn.prepareStatement("""
                    SELECT password
                    FROM nl_entries
                    WHERE username = ?""");
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) return false;

            return true;
            //return BCrypt.verifyer().verify(password.toCharArray(), rs.getString("password")).verified;
        } catch (SQLException ex) {
            Nedologin.logger.error("Error looking up password", ex);
            return false;
        }
    }

    public void unregister(String username) {
        try {
            checkValidity();
            PreparedStatement st = conn.prepareStatement("""
                    DELETE
                    FROM nl_entries
                    WHERE username = ?""");
            st.setString(1, username);
            st.execute();
        } catch (SQLException ex) {
            Nedologin.logger.error("Error deleting entry", ex);
        }
    }

    public boolean registered(String username) {
        try {
            checkValidity();
            PreparedStatement st = conn.prepareStatement("SELECT EXISTS(SELECT * from nl_entries WHERE username = ?)");
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            return rs.next() && rs.getBoolean(1);
        } catch (SQLException ex) {
            Nedologin.logger.error("Error looking up entry", ex);
            return false;
        }
    }

    public void register(String username, String password) {
        if (registered(username)) return;
        try {
            checkValidity();
            PreparedStatement st = conn.prepareStatement("INSERT INTO nl_entries (username, password)\n" +
                    "VALUES (?, ?, ?)");
            st.setString(1, username);
            st.setString(2, /*BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(NLConstants.BCRYPT_COST, password.toCharArray())*/"t");
            st.execute();
        } catch (SQLException ex) {
            Nedologin.logger.error("Error registering entry", ex);
        }
    }

    public void save() {
        // NO-OP
    }

    // Reconnects the server to the database in case the connection becomes invalid
    protected void checkValidity() throws SQLException {
        // Literally nothing
    }
}
