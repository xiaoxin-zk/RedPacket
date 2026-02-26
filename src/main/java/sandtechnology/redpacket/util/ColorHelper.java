package sandtechnology.redpacket.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorHelper {
    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6})");

    private ColorHelper() {
    }

    public static String format(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(sb, ChatColor.of("#" + hex).toString());
        }
        matcher.appendTail(sb);
        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }

    public static BaseComponent[] formatToComponents(String message) {
        if (message == null || message.isEmpty()) {
            return new BaseComponent[]{new TextComponent("")};
        }
        String formatted = format(message);
        return TextComponent.fromLegacyText(formatted);
    }
}
