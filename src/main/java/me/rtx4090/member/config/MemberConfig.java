package me.rtx4090.member.config;

import me.rtx4090.member.player.PlayerProfile;
import me.rtx4090.member.utils.ConfigUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemberConfig {
    public static Map<UUID, PlayerProfile> invite = new HashMap<>();
    public static Map<UUID, PlayerProfile> kick = new HashMap<>();



    public static void load() {
        Path configPath = ConfigUtil.ofPath("member.yml");
        if (!Files.exists(configPath)) {
            ConfigUtil.copyDefault("member.yml");
        }
        ConfigUtil.readStatic(configPath, MemberConfig.class);
    }
    public static void save() {
        Path configPath = ConfigUtil.ofPath("member.yml");
        Map<String, Object> data = new HashMap<>();
        data.put("invite", invite);
        data.put("kick", kick);
        ConfigUtil.writeStatic(configPath, data);
    }

}
