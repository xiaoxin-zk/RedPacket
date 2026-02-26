package sandtechnology.redpacket;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import sandtechnology.redpacket.command.CommandHandler;
import sandtechnology.redpacket.database.AbstractDatabaseManager;
import sandtechnology.redpacket.database.MysqlManager;
import sandtechnology.redpacket.database.SqliteManager;
import sandtechnology.redpacket.listener.ChatListener;
import sandtechnology.redpacket.listener.MessageSender;
import sandtechnology.redpacket.redpacket.RedPacket;
import sandtechnology.redpacket.session.SessionManager;
import sandtechnology.redpacket.ui.MenuListener;
import sandtechnology.redpacket.util.*;

import java.util.logging.Level;

public class RedPacketPlugin extends JavaPlugin {

    private static RedPacketPlugin instance;
    private static AbstractDatabaseManager databaseManager;
    private int sessionCleanupTaskId = -1;
    private boolean startup;

    public static RedPacketPlugin getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalStateException("插件未正常开启！请查看报错信息");
        }
    }

    public RedPacketPlugin() {
        instance = this;
    }

    public static AbstractDatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static FileConfiguration config() {
        return instance.getConfig();
    }

    public static void log(Level level, String msg, Object... format) {
        getInstance().getLogger().log(level, String.format(msg, format));
    }

    public boolean reload() {
        try {
            if (sessionCleanupTaskId != -1) {
                Bukkit.getScheduler().cancelTask(sessionCleanupTaskId);
                sessionCleanupTaskId = -1;
            }
            RedPacketManager.getRedPacketManager().stop();
            if (databaseManager != null) {
                databaseManager.setRunning(false);
            }
            MessageHelper.setStatus(false);
            reloadConfig();
            updateConfig();
            IdiomManager.reload();
            databaseManager = config().getString("Database.Type").equalsIgnoreCase("sqlite")
                ? new SqliteManager(config().getString("Database.TableName"))
                : new MysqlManager(config().getString("Database.TableName"));
            RedPacketManager.getRedPacketManager().getRedPackets().clear();
            RedPacketManager.getRedPacketManager().setup();
            MessageHelper.setStatus(true);
            return true;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "重载插件时出错！", e);
            return false;
        }
    }

    private void setIfAbsent(String node, Object value) {
        if (!getConfig().isSet(node)) {
            getConfig().set(node, value);
        }
    }

    private void updateConfig() {
        setIfAbsent("Version", 2);
        setIfAbsent("Database.Type", "sqlite");
        setIfAbsent("Database.FileName", "database.db");
        setIfAbsent("Database.IP", "127.0.0.1");
        setIfAbsent("Database.Port", 3306);
        setIfAbsent("Database.UserName", "");
        setIfAbsent("Database.Password", "");
        setIfAbsent("Database.DatabaseName", "database");
        setIfAbsent("Database.MySQLArgument", "");
        setIfAbsent("Database.TableName", "redpacket");
        setIfAbsent("RedPacket.MaxAmount", 10000);
        setIfAbsent("RedPacket.MaxMoney", 10000.0);
        setIfAbsent("RedPacket.MinMoney", 1.0);
        setIfAbsent("RedPacket.Expired", false);
        setIfAbsent("RedPacket.ExpiredTime", 86400000);
        setIfAbsent("RedPacket.SessionExpiredTime", 500000);
        saveConfig();
    }
    @Override
    public void onEnable() {
        if (startup) {
            log(Level.WARNING, "检测到服务器重载，将使用重载逻辑！");
            reload();
        }
        try {
            saveDefaultConfig();
            getConfig();
            getLogger().info("初始化插件...");
            CompatibilityHelper.setup();
            EcoAndPermissionHelper.setup();
            IdiomManager.setup();
            getLogger().info("更新配置文件...");
            updateConfig();
            sessionCleanupTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously(this,
                () -> SessionManager.getSessionManager().cleanUp(), 1200L, 1200L).getTaskId();
            if (config().getString("Database.Type").equalsIgnoreCase("sqlite")) {
                databaseManager = new SqliteManager(config().getString("Database.TableName"));
            } else {
                databaseManager = new MysqlManager(config().getString("Database.TableName"));
            }
            getLogger().info("注册监听器...");
            getServer().getPluginManager().registerEvents(new ChatListener(), this);
            getServer().getPluginManager().registerEvents(new MessageSender(), this);
            getServer().getPluginManager().registerEvents(new MenuListener(), this);
            getLogger().info("注册命令...");
            getCommand("RedPacket").setExecutor(CommandHandler.getCommandHandler());
            getCommand("RedPacket").setTabCompleter(CommandHandler.getCommandHandler());
            getLogger().info("注册完成！等待其他插件加载完成...");
            //为避免需要的经济插件被放在该插件后面加载造成出错
            //将调用Vault API的方法延迟到服务器完全启动后
            Bukkit.getScheduler().runTask(this, () -> {
                getLogger().info("正在载入红包信息，请稍等...");
                RedPacketManager.getRedPacketManager().setup();
                MessageHelper.setStatus(true);
                getLogger().info("初始化插件完成！");
                startup = true;
            });
        } catch (RuntimeException e) {
            getServer().getPluginManager().disablePlugin(this);
            throw e;
        }
    }

    @Override
    public void onDisable() {
        log(Level.INFO, "正在关闭插件...");
        MessageHelper.setStatus(false);
        RedPacketManager.getRedPacketManager().stop();
        if (sessionCleanupTaskId != -1) {
            Bukkit.getScheduler().cancelTask(sessionCleanupTaskId);
            sessionCleanupTaskId = -1;
        }
        if (RedPacketManager.getRedPacketManager() != null) {
            log(Level.INFO, "正在保存运行中的红包数据...");
            RedPacketManager.getRedPacketManager().getRedPackets().forEach(rp -> {
                if (!rp.isExpired() && rp.getCurrentAmount() > 0) {
                    databaseManager.update(rp);
                }
            });
        }
        if (databaseManager != null) {
            databaseManager.setRunning(false);
            databaseManager.close();
        }
        log(Level.INFO, "插件已安全关闭。");
    }
}
