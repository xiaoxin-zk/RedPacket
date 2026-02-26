package sandtechnology.redpacket.ui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import sandtechnology.redpacket.redpacket.RedPacket;
import sandtechnology.redpacket.session.CreateSession;
import sandtechnology.redpacket.session.SessionManager;
import sandtechnology.redpacket.util.IdiomManager;
import sandtechnology.redpacket.util.MessageHelper;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(GuiMenu.TITLE)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null || clicked.getType().isAir()) {
            return;
        }

        CreateSession session = SessionManager.getSessionManager().getSession(player);
        if (session == null) {
            player.closeInventory();
            return;
        }

        RedPacket.Builder builder = session.getBuilder();
        int slot = event.getRawSlot();

        switch (slot) {
            case 20: // 红包类型
                RedPacket.RedPacketType currentType = builder.getType();
                if (currentType == RedPacket.RedPacketType.CommonRedPacket) {
                    builder.type(RedPacket.RedPacketType.PasswordRedPacket);
                } else if (currentType == RedPacket.RedPacketType.PasswordRedPacket) {
                    builder.type(RedPacket.RedPacketType.JieLongRedPacket);
                    if (builder.getExtraData() == null || builder.getExtraData().isEmpty()) {
                        builder.extraData(IdiomManager.getRandomIdiom());
                    }
                } else {
                    builder.type(RedPacket.RedPacketType.CommonRedPacket);
                }
                GuiMenu.open(player);
                break;

            case 21: // 分配方式
                builder.giveType(builder.getGiveType() == RedPacket.GiveType.LuckyAmount ?
                    RedPacket.GiveType.FixAmount : RedPacket.GiveType.LuckyAmount);
                GuiMenu.open(player);
                break;

            case 22: // 货币类型 - 新增
                builder.currencyType(builder.getCurrencyType() == RedPacket.CurrencyType.MONEY ?
                    RedPacket.CurrencyType.POINTS : RedPacket.CurrencyType.MONEY);
                GuiMenu.open(player);
                break;

            case 23: // 红包总额
                session.setState(CreateSession.State.WaitMoney);
                player.closeInventory();
                MessageHelper.sendSimpleMsg(player, ChatColor.GREEN, "请在聊天框输入红包总额 (数字):");
                break;

            case 24: // 红包数量
                session.setState(CreateSession.State.WaitAmount);
                player.closeInventory();
                MessageHelper.sendSimpleMsg(player, ChatColor.GREEN, "请在聊天框输入红包数量 (整数):");
                break;

            case 31: // 红包内容
                session.setState(CreateSession.State.WaitExtra);
                player.closeInventory();
                MessageHelper.sendSimpleMsg(player, ChatColor.GREEN, "请在聊天框输入红包内容 (支持彩色/HEX):");
                break;

            case 49: // 确认发放
                if (builder.isValid()) {
                    player.closeInventory();
                    player.performCommand("redpacket session create");
                } else {
                    MessageHelper.sendSimpleMsg(player, ChatColor.RED, "红包设置尚未完成，请检查金额、数量和内容！");
                }
                break;
        }
    }
}
