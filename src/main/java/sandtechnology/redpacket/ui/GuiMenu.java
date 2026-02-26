package sandtechnology.redpacket.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sandtechnology.redpacket.redpacket.RedPacket;
import sandtechnology.redpacket.session.CreateSession;
import sandtechnology.redpacket.session.SessionManager;
import sandtechnology.redpacket.util.ColorHelper;

import java.util.ArrayList;
import java.util.List;

public class GuiMenu {
    public static final String TITLE = ChatColor.RED + "有序之境" + ChatColor.GOLD + "红包" + ChatColor.YELLOW + "助手";

    public static void open(Player player) {
        CreateSession session = SessionManager.getSessionManager().getSession(player);
        if (session == null) {
            session = SessionManager.getSessionManager().createSession(player);
        }

        Inventory inv = Bukkit.createInventory(null, 54, TITLE);
        ItemStack glass = createItem(Material.GRAY_STAINED_GLASS_PANE, " ");

        // 边框装饰
        for (int i = 0; i < 54; i++) {
            if (i < 9 || i > 44 || i % 9 == 0 || (i + 1) % 9 == 0) {
                inv.setItem(i, glass);
            }
        }

        RedPacket.Builder builder = session.getBuilder();

        // 红包类型
        Material typeMat = Material.PAPER;
        String typeName = "普通红包";
        String typeDesc = "最基础的红包类型";
        if (builder.getType() == RedPacket.RedPacketType.PasswordRedPacket) {
            typeMat = Material.WRITABLE_BOOK;
            typeName = "口令红包";
            typeDesc = "需要输入口令才能领取";
        } else if (builder.getType() == RedPacket.RedPacketType.JieLongRedPacket) {
            typeMat = Material.BOOK;
            typeName = "成语接龙";
            typeDesc = "需要成语接龙才能领取";
        }
        inv.setItem(20, createItem(typeMat, ChatColor.GOLD + "✦ 红包类型 ✦",
            ChatColor.YELLOW + "当前: " + ChatColor.WHITE + typeName,
            ChatColor.GRAY + typeDesc,
            "",
            ChatColor.AQUA + "▶ 点击切换类型"));

        // 分配方式
        Material giveMat = builder.getGiveType() == RedPacket.GiveType.LuckyAmount ? Material.SUNFLOWER : Material.GOLD_INGOT;
        String giveTypeName = builder.getGiveType() == RedPacket.GiveType.LuckyAmount ? "拼手气" : "固定金额";
        String giveTypeDesc = builder.getGiveType() == RedPacket.GiveType.LuckyAmount ? "每个红包金额随机" : "每个红包金额相同";
        inv.setItem(21, createItem(giveMat, ChatColor.GOLD + "✦ 分配方式 ✦",
            ChatColor.YELLOW + "当前: " + ChatColor.WHITE + giveTypeName,
            ChatColor.GRAY + giveTypeDesc,
            "",
            ChatColor.AQUA + "▶ 点击切换分配方式"));

        // 货币类型 - 新增
        Material currencyMat = builder.getCurrencyType() == RedPacket.CurrencyType.MONEY ? Material.GOLD_NUGGET : Material.NETHER_STAR;
        String currencyDesc = builder.getCurrencyType() == RedPacket.CurrencyType.MONEY ? "使用服务器经济货币" : "使用玩家点券系统";
        inv.setItem(22, createItem(currencyMat, ChatColor.GOLD + "✦ 货币类型 ✦",
            ChatColor.YELLOW + "当前: " + ChatColor.WHITE + builder.getCurrencyType().getName(),
            ChatColor.GRAY + currencyDesc,
            "",
            ChatColor.AQUA + "▶ 点击切换货币类型"));

        // 红包总额
        String currencyUnit = builder.getCurrencyType() == RedPacket.CurrencyType.MONEY ? "元" : "点券";
        String moneyDisplay = builder.getCurrencyType() == RedPacket.CurrencyType.POINTS ? String.valueOf((int)builder.getMoney()) : String.valueOf(builder.getMoney());
        inv.setItem(23, createItem(Material.EMERALD, ChatColor.GOLD + "✦ 红包总额 ✦",
            ChatColor.YELLOW + "当前: " + ChatColor.GREEN + moneyDisplay + " " + currencyUnit,
            ChatColor.GRAY + "设置红包的总金额",
            "",
            ChatColor.AQUA + "▶ 点击后在聊天框输入"));

        // 红包数量
        inv.setItem(24, createItem(Material.HOPPER, ChatColor.GOLD + "✦ 红包数量 ✦",
            ChatColor.YELLOW + "当前: " + ChatColor.WHITE + builder.getAmount() + " 个",
            ChatColor.GRAY + "设置可领取的红包数量",
            "",
            ChatColor.AQUA + "▶ 点击后在聊天框输入"));

        // 红包内容
        String contentPreview = builder.getExtraData();
        if (contentPreview == null || contentPreview.isEmpty()) {
            contentPreview = ChatColor.GRAY + "(未设置)";
        } else if (player.hasPermission("redpacket.color")) {
            contentPreview = ColorHelper.format(contentPreview);
        }
        List<String> contentLore = new ArrayList<>();
        contentLore.add(ChatColor.YELLOW + "当前: " + ChatColor.RESET + contentPreview);
        contentLore.add(ChatColor.GRAY + "设置红包的祝福语或口令");
        contentLore.add("");
        if (player.hasPermission("redpacket.color")) {
            contentLore.add(ChatColor.LIGHT_PURPLE + "✦ 支持彩色代码 &a #66ccff");
        }
        contentLore.add(ChatColor.AQUA + "▶ 点击后在聊天框输入");
        inv.setItem(31, createItem(Material.NAME_TAG, ChatColor.GOLD + "✦ 红包内容 ✦", contentLore.toArray(new String[0])));

        // 确认按钮
        boolean valid = builder.isValid();
        Material confirmMat = valid ? Material.GOLD_BLOCK : Material.BARRIER;
        List<String> confirmLore = new ArrayList<>();
        confirmLore.add(ChatColor.GRAY + "━━━━━━━━━━━━━━━━━━━━");
        confirmLore.add(ChatColor.YELLOW + "✦ 当前设置预览 ✦");
        confirmLore.add("");
        confirmLore.add(ChatColor.WHITE + " ▸ 类型: " + ChatColor.AQUA + typeName);
        confirmLore.add(ChatColor.WHITE + " ▸ 分配: " + ChatColor.AQUA + (builder.getGiveType() == RedPacket.GiveType.LuckyAmount ? "拼手气" : "固定金额"));
        confirmLore.add(ChatColor.WHITE + " ▸ 货币: " + ChatColor.AQUA + builder.getCurrencyType().getName());
        confirmLore.add(ChatColor.WHITE + " ▸ 总额: " + ChatColor.GREEN + moneyDisplay + " " + currencyUnit);
        confirmLore.add(ChatColor.WHITE + " ▸ 数量: " + ChatColor.AQUA + builder.getAmount() + " 个");
        confirmLore.add(ChatColor.WHITE + " ▸ 内容: " + ChatColor.RESET + contentPreview);
        confirmLore.add("");
        confirmLore.add(ChatColor.GRAY + "━━━━━━━━━━━━━━━━━━━━");
        if (valid) {
            confirmLore.add(ChatColor.GREEN + "✔ 点击立即发放红包！");
        } else {
            confirmLore.add(ChatColor.RED + "✘ 设置未完成，无法发放");
        }
        inv.setItem(49, createItem(confirmMat, (valid ? ChatColor.GREEN + "✦ " : ChatColor.RED + "✦ ") + "确认发放" + (valid ? ChatColor.GREEN + " ✦" : ChatColor.RED + " ✦"), confirmLore.toArray(new String[0])));

        player.openInventory(inv);
    }

    private static ItemStack createItem(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> list = new ArrayList<>();
            for (String s : lore) {
                list.add(s);
            }
            meta.setLore(list);
            item.setItemMeta(meta);
        }
        return item;
    }
}
