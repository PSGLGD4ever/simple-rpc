package com.being.rpc.nameservice.jdbc;

import com.beinglee.rpc.NameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class JdbcNameService implements NameService, Closeable {

    private static final Logger log = LoggerFactory.getLogger(JdbcNameService.class);

    private final Collection<String> schemes = Collections.singleton("jdbc");

    private Connection connection;

    @Override
    public Collection<String> supportedSchemes() {
        return schemes;
    }

    @Override
    public void connect(URI nameServiceUri) {
        try {
            close();
            String userName = "root";
            String password = "beinglee";
            this.connection = DriverManager.getConnection(nameServiceUri.toString(), userName, password);
            this.maybeExecuteDDl(connection);
        } catch (Exception e) {
            log.error("JdbcNameService connect error!", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void registerService(String serviceName, URI uri) {
        try (PreparedStatement statement = connection.prepareStatement(readSql("register-service"))) {
            statement.setString(1, serviceName);
            statement.setString(2, uri.toString());
            statement.execute();
        } catch (Exception e) {
            log.error("JdbcNameService registerService error!", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public URI lookupService(String serviceName) {
        try (PreparedStatement statement = this.connection.prepareStatement(this.readSql("lookup-service"))) {
            statement.setString(1, serviceName);
            ResultSet resultSet = statement.executeQuery();
            List<URI> uris = new ArrayList<>();
            while (resultSet.next()) {
                uris.add(URI.create(resultSet.getString(1)));
            }
            return uris.get(ThreadLocalRandom.current().nextInt(uris.size()));
        } catch (Exception e) {
            log.error("JdbcNameService lookupService error!", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("JdbcNameService close occur error!", e);
            }
        }
    }

    private void maybeExecuteDDl(Connection connection) throws SQLException, IOException {
        try (Statement statement = connection.createStatement()) {
            String ddlSqlString = this.readSql("ddl");
            statement.execute(ddlSqlString);
        }
    }

    private String readSql(String fileName) throws IOException {
        String sqlFile = this.toFileName(fileName);
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(sqlFile)) {
            if (in != null) {
                byte[] bytes = new byte[in.available()];
                int ignore = in.read(bytes);
                return new String(bytes, StandardCharsets.UTF_8);
            }
        }
        throw new IOException(sqlFile + " not found in classpath!");
    }

    private String toFileName(String fileName) {
        return fileName + ".sql";
    }
}
