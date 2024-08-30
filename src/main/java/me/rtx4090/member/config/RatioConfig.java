package me.rtx4090.member.config;

import me.rtx4090.member.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.nfunk.jep.JEP;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RatioConfig {
    public static List<String> conditions = new ArrayList<>();

    public static void load() {
        Path configPath = ConfigUtil.ofPath("ratio.yml");
        if (!Files.exists(configPath)) {
            ConfigUtil.copyDefault("ratio.yml");
        }
        ConfigUtil.readStatic(configPath, RatioConfig.class);
    }

    public static boolean evaluate(UUID votedPlayer, Boolean isInvite) {

        JEP jep = new JEP();

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        double evaluateResult = 1.0;

        if (isInvite) {

            int accept = MemberConfig.invite.get(votedPlayer).accept.size();
            int reject = MemberConfig.invite.get(votedPlayer).reject.size();

            jep.addVariable("accept_amount", accept);
            jep.addVariable("reject_amount", reject);
            jep.addVariable("whitelisted_member_amount", Bukkit.getWhitelistedPlayers().size());
            jep.addVariable("total_votes", accept + reject);

            for (String condition : conditions) {
                jep.parseExpression(condition);
                evaluateResult = evaluateResult * jep.getValue();

                if (evaluateResult == 0) break;
            }
        }
        return evaluateResult != 0.0;
    }
}
