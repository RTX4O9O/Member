package me.rtx4090.member;

import me.rtx4090.member.commands.MemberCommand;
import me.rtx4090.member.config.MemberConfig;
import me.rtx4090.member.config.RatioConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public final class Member extends JavaPlugin {
    public static Member Instance = null;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("scale").setExecutor(new MemberCommand());
        Instance = this;
        MemberConfig.load();
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
                if (isMoreThanAWeekAgo(value.date)) {
                    if (RatioConfig.evaluate(key, true)) getServer().getOfflinePlayer(key).setWhitelisted(true);
                    MemberConfig.invite.remove(key);
                }
            });
            MemberConfig.kick.forEach((key, value) -> {
                if (isMoreThanAWeekAgo(value.date)) {
                    if (RatioConfig.evaluate(key, false)) getServer().getOfflinePlayer(key).setWhitelisted(false);
                    MemberConfig.kick.remove(key);
                }
            });
            MemberConfig.save();
        }, oneHourInTicks, oneHourInTicks);
    }


/*    private void schedulePeriodicCheck() {
        // Convert 1 hour to ticks (20 ticks = 1 second)
        long oneHourInTicks = 20L * 60L * 60L;

        // Schedule the task to run every hour
        getServer().getScheduler().runTaskTimer(this, this::removeOutdated, oneHourInTicks, oneHourInTicks);
    }

    private void removeOutdated() {
        // Remove outdated entries from the invite and kick lists
        MemberConfig.invite.entrySet().removeIf(entry -> isMoreThanAWeekAgo(entry.getValue().date));
        MemberConfig.kick.entrySet().removeIf(entry -> isMoreThanAWeekAgo(entry.getValue().date));
        MemberConfig.save();
    }*/

    public static boolean isMoreThanAWeekAgo(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        return date.before(calendar.getTime());
    }

}
