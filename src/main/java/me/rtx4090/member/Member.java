package me.rtx4090.member;

import me.rtx4090.member.commands.AutoComplete;
import me.rtx4090.member.commands.MemberCommand;
import me.rtx4090.member.config.MemberConfig;
import me.rtx4090.member.config.RatioConfig;
import me.rtx4090.member.listener.EventListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;


public final class Member extends JavaPlugin {
    public static Member Instance = null;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger = this.getLogger();
        logger.info("Member plugin is enabled");
        getCommand("member").setExecutor(new MemberCommand());
        getCommand("member").setTabCompleter(new AutoComplete());
        Instance = this;
        MemberConfig.load();
        RatioConfig.load();
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        endOutdated();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static String newToken() {
        Random random = new Random();
        StringBuilder value = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(ALPHABET.length());
            value.append(ALPHABET.charAt(index));
        }
        return value.toString();
    }

    private void endOutdated() {
        // Convert 1 hour to ticks (20 ticks = 1 second)
        long oneHourInTicks = 20L * 60L * 60L;

        // Schedule the task to run every hour
        getServer().getScheduler().runTaskTimer(this, () -> {
            // Remove outdated entries from the invite and kick lists
            MemberConfig.invite.forEach((key, value) -> {
                if (isDatePassed(value.dueDate)) {
                    if (RatioConfig.evaluate(key, true)) getServer().getOfflinePlayer(key).setWhitelisted(true);
                    MemberConfig.invite.remove(key);
                }
            });
            MemberConfig.kick.forEach((key, value) -> {
                if (isDatePassed(value.dueDate)) {
                    if (RatioConfig.evaluate(key, false)) getServer().getOfflinePlayer(key).setWhitelisted(false);
                    MemberConfig.kick.remove(key);
                }
            });
            MemberConfig.save();
        }, oneHourInTicks, oneHourInTicks);
    }

    public static boolean isDatePassed(Date date) {
        Date currentDate = new Date();
        return date.before(currentDate);
    }

    public static void sendMessageToAllPlayers(TextComponent message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(message);
        }
    }
    public static void sendMessageToAllPlayers(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.valueOf(message));
        }
    }

    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        return formatter.format(date);
    }

}
