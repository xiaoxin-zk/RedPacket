package sandtechnology.redpacket.database;

import java.sql.DriverManager;

import static sandtechnology.redpacket.RedPacketPlugin.config;
import static sandtechnology.redpacket.RedPacketPlugin.getInstance;

public class SqliteManager extends AbstractDatabaseManager {


    public SqliteManager(String tableName) {
        setup(tableName);
    }

    @Override
    void setup(String tableName) {
        try{
            Class.forName("org.sqlite.JDBC");
            this.tableName = tableName;
            connection = DriverManager.getConnection("jdbc:sqlite:" + getInstance().getDataFolder().toPath().resolve(config().getString("Database.FileName")).toString());
            executeUpdate(
                    "create table if not exists " + tableName + " (" +
                            "UUID Text PRIMARY KEY," +
                            "playerUUID Text NOT NULL," +
                            "giveType Text NOT NULL," +
                            "RedPacketType Text NOT NULL," +
                            "currencyType Text NOT NULL DEFAULT 'MONEY'," +
                            "amount INTEGER NOT NULL," +
                            "money real NOT NULL," +
                            "moneyMap Text NOT NULL," +
                            "extraData Text NOT NULL," +
                            "givers Text NOT NULL," +
                            "expireTime INTEGER NOT NULL," +
                            "timeZone TEXT NOT NULL,"+
                            "expired INTEGER NOT NULL)"
            );
            // 为现有数据库添加currencyType字段（如果不存在）
            try {
                executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN currencyType Text NOT NULL DEFAULT 'MONEY'");
            } catch (Exception e) {
                // 字段已存在，忽略错误
            }
            executeUpdate("CREATE INDEX if not exists searchIndex ON " + tableName + " (playerUUID, expireTime)");
            connection.setAutoCommit(false);
            setRunning(true);
            startCommitTimer();
        } catch (Exception ex) {
            throw new RuntimeException("数据库初始化出现错误，将关闭本插件！", ex);
        }
    }
}
