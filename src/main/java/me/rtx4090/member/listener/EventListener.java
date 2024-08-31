package me.rtx4090.member.listener;

import me.rtx4090.member.config.MemberConfig;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        int votesAvailable = MemberConfig.invite.size() + MemberConfig.kick.size();

        if (votesAvailable == 0) return;
        // Create the main message component
        TextComponent message = new TextComponent("[Member] There's " + votesAvailable + " available votes now, ");
        message.setColor(net.md_5.bungee.api.ChatColor.YELLOW);

        // Create the clickable component
        TextComponent clickHere = new TextComponent("Click Here");
        clickHere.setUnderlined(true);
        clickHere.setColor(net.md_5.bungee.api.ChatColor.BLUE);
        clickHere.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/member vote"));

        // Create the remaining message component
        TextComponent toVote = new TextComponent(" to vote");
        toVote.setColor(net.md_5.bungee.api.ChatColor.YELLOW);

        // Combine the components
        message.addExtra(clickHere);
        message.addExtra(toVote);

        event.getPlayer().spigot().sendMessage(message);
    }
}