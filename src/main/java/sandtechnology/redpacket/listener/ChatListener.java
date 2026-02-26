package sandtechnology.redpacket.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import sandtechnology.redpacket.redpacket.RedPacket;
import sandtechnology.redpacket.session.CreateSession;
import sandtechnology.redpacket.ui.GuiMenu;

import java.util.Arrays;

import static sandtechnology.redpacket.RedPacketPlugin.getInstance;
import static sandtechnology.redpacket.session.SessionManager.getSessionManager;
import static sandtechnology.redpacket.util.EcoAndPermissionHelper.hasPermissionSilently;
import static sandtechnology.redpacket.util.RedPacketManager.getRedPacketManager;


public class ChatListener implements Listener {

    private static final CreateSession.State[] inputNeededState = {CreateSession.State.WaitAmount, CreateSession.State.WaitExtra, CreateSession.State.WaitGiver, CreateSession.State.WaitMoney};
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player=event.getPlayer();

        //判断是否在输入创建红包的数据
        boolean isCreatingRedPacket = getSessionManager().hasSession(player) && Arrays.stream(inputNeededState).anyMatch(state -> state == getSessionManager().getSession(player).getState());

        // 聊天颜色代码转换（不在创建红包时转换）
        if (!isCreatingRedPacket && hasPermissionSilently(player, "redpacket.chat.color")) {
            String message = event.getMessage();
            if (message.contains("&")) {
                event.setMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }

        if (isCreatingRedPacket) {
            // 检查是否输入"cancel"取消操作
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                getSessionManager().getSession(player).cancel();
                event.setCancelled(true);
                return;
            }

            getSessionManager().getSession(player).parse(event.getPlayer(),event.getMessage());
            event.setCancelled(true);

            // 输入完成后重新打开GUI界面
            if (getSessionManager().hasSession(player)) {
                Bukkit.getScheduler().runTask(getInstance(), () -> {
                    if (player.isOnline() && getSessionManager().hasSession(player)) {
                        GuiMenu.open(player);
                    }
                });
            }
        }
        //确保异步执行
        if(event.isAsynchronous()){
           checkRedPacket(event);
        }else {
            Bukkit.getScheduler().runTaskAsynchronously(getInstance(),()->checkRedPacket(event));
        }
    }

    private void checkRedPacket(AsyncPlayerChatEvent event){
        getRedPacketManager().getRedPackets().stream().filter(redPacket -> redPacket.getType().equals(RedPacket.RedPacketType.JieLongRedPacket) || redPacket.getType().equals(RedPacket.RedPacketType.PasswordRedPacket)).forEach(redPacket -> redPacket.giveIfValid(event.getPlayer(), event.getMessage()));
    }

}
