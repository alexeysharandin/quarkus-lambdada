package com.github.alexeysharandin.quarkus.lambdada.io.sqlite;

import com.github.alexeysharandin.quarkus.lambdada.runtime.EntryMetadata;
import com.github.alexeysharandin.quarkus.lambdada.runtime.ProfileStackTraceElement;
import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.supplier.AgroalPropertiesReader;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SqlHelper {
    private final static String FILE_NAME = System.getProperty("java.io.tmpdir") + "profiler.db";

    private final static String PROFILER_MAIN_TABLE_NAME = "quarkus_profiler";
    private final static String PROFILER_META_TABLE_NAME = "quarkus_profiler_meta";
    private final static String QUERY_IS_TABLE_EXIT = "SELECT name FROM sqlite_schema WHERE type='table' and name='" + PROFILER_MAIN_TABLE_NAME + "' ORDER BY name";
    private final static String QUERY_CREATE_MAIN_TABLE = "" +
            "create table " + PROFILER_MAIN_TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY ASC, " +
            "parent_id INTEGER, " +
            "uuid TEXT, " +
            "start_time INTEGER, " +
            "end_time INTEGER, " +
            "delta INTEGER, " +
            "class_name TEXT, " +
            "method_name TEXT " +
            //"FOREIGN KEY parent_id REFERENCES " + PROFILER_MAIN_TABLE_NAME + "(id) " +
            ")";

    private final static String QUERY_INSERT_MAIN = "" +
            "insert into " + PROFILER_MAIN_TABLE_NAME + "(" +
            "parent_id, uuid, start_time, end_time, delta, class_name, method_name) " +
            "values (?, ?, ?, ?, ?, ?, ?)";

    private final static String QUERY_CREATE_META_TABLE = "" +
            "create table " + PROFILER_META_TABLE_NAME + " (" +
            "parent_id INTEGER, " +
            "idx INTEGER, " +
            "key TEXT, " +
            "value TEXT " +
            /*"FOREIGN KEY parent_id REFERENCES " + PROFILER_MAIN_TABLE_NAME + "(id) " +*/
            ")";

    private final static String QUERY_INSERT_META = "" +
            "insert into " + PROFILER_META_TABLE_NAME + "(" +
            "parent_id, idx, key, value) " +
            "values (?, ?, ?, ?)";

    private volatile AgroalDataSource ds = null;

    public int store(ProfileStackTraceElement el, int parent) {
        try {
            try (Connection conn = getDataSource().getConnection()) {
                try (PreparedStatement ps = conn.prepareStatement(QUERY_INSERT_MAIN, Statement.RETURN_GENERATED_KEYS)) {
                    // "parent_id, uuid, start_time, end_time, delta, class_name, method_name " +
                    EntryMetadata meta = el.meta();
                    ps.setInt(1, parent);
                    ps.setString(2, el.uuid().toString());
                    ps.setLong(3, el.startTime());
                    ps.setLong(4, el.stopTime());
                    ps.setLong(5, el.result());

                    ps.setString(6, meta.className());
                    ps.setString(7, meta.methodName());
                    int rows = ps.executeUpdate();
                    try(ResultSet keys = ps.getGeneratedKeys()) {
                        if(keys.next()) {
                            int idx = keys.getInt(1);
                            storeMetadata(conn, idx, meta);
                            return idx;
                        }
                    }
                }
            }
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return -1;
    }

    private void storeMetadata(Connection conn, int idx, EntryMetadata meta) {
        try {
            int order = 0;
            for (Map.Entry<String, Object> item : meta.metadata().entrySet()) {
                try (PreparedStatement ps = conn.prepareStatement(QUERY_INSERT_META)) {
                    //"parent_id, order, key, value) "
                    ps.setInt(1, idx);
                    ps.setInt(2, order++);
                    ps.setString(3, item.getKey());
                    ps.setString(4, String.valueOf(item.getValue()));
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private AgroalDataSource getDataSource() {
        if (ds == null) {
            synchronized (this) {
                if (ds == null) {
                    System.out.println("FILE_NAME = " + FILE_NAME);
                    Map<String, String> props = new HashMap<>();

                    props.put(AgroalPropertiesReader.MAX_SIZE, "10");
                    props.put(AgroalPropertiesReader.MIN_SIZE, "10");
                    props.put(AgroalPropertiesReader.INITIAL_SIZE, "10");
                    props.put(AgroalPropertiesReader.MAX_LIFETIME_S, "300");
                    props.put(AgroalPropertiesReader.ACQUISITION_TIMEOUT_S, "30");
                    props.put(AgroalPropertiesReader.JDBC_URL, "jdbc:sqlite:" + FILE_NAME);
                    //props.put(AgroalPropertiesReader.PRINCIPAL,"username");
                    //props.put(AgroalPropertiesReader.CREDENTIAL,"password");

                    try {
                        ds = AgroalDataSource.from(
                                new AgroalPropertiesReader()
                                        .readProperties(props)
                                        .get());
                        prepare();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return ds;
    }

    private void prepare() throws SQLException {
        if (!isMainTableExists()) {
            createTables();
        }
    }

    private void createTables() throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(QUERY_CREATE_MAIN_TABLE);
                statement.executeUpdate(QUERY_CREATE_META_TABLE);
            }
        }
    }

    private boolean isMainTableExists() throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet rs = statement.executeQuery(QUERY_IS_TABLE_EXIT)) {
                    return rs.next();
                }
            }
        }
    }
}
