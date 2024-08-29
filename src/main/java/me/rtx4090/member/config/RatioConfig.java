package me.rtx4090.member.config;

import me.rtx4090.member.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class RatioConfig {
    public static List<String> conditions = new ArrayList<>();

    public static void load() {
        ConfigUtil.readStatic(Path.of("ratio.yml"), RatioConfig.class);
    }

    public static boolean evaluate(UUID votedPlayer, Boolean isInvite) {

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        boolean evaluateResult = true;

        if (isInvite) {

            int accept = MemberConfig.invite.get(votedPlayer).accept.size();
            int reject = MemberConfig.invite.get(votedPlayer).reject.size();
            engine.put("accept_amount", accept);
            engine.put("reject_amount", reject);
            engine.put("whitelisted_member_amount", Bukkit.getWhitelistedPlayers().size());
            engine.put("total_votes", accept + reject);

            for (String condition : conditions) {
                try {
                    evaluateResult = (boolean) engine.eval(condition);
                } catch (ScriptException e) {
                    throw new RuntimeException(e);
                }
                if (!evaluateResult) break;
            }
        }
        return evaluateResult;
    }
}
