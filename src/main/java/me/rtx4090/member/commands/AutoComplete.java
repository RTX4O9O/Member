package me.rtx4090.member.commands;

import me.rtx4090.member.config.MemberConfig;
import me.rtx4090.member.utils.MojangAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AutoComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("member")) {
            if (args.length == 1) {
                // Suggest subcommands for the first argument
                return Arrays.asList("invite", "kick", "cancel", "vote", "endvote");
            } else if (args.length == 2) {
                // Suggest player names for the second argument
                if (Arrays.asList("invite", "kick", "cancel", "endvote").contains(args[0])) {
                    String partialPlayerName = args[1].toLowerCase();
                    return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(partialPlayerName))
                            .collect(Collectors.toList());
                }
            } else if (args.length == 3) {
                // Suggest "accept" or "reject" for the third argument when using the "vote" subcommand
                if (args[0].equalsIgnoreCase("endvote") || args[0].equalsIgnoreCase("cancel")) {
                    List<String> beingVoteds = MemberConfig.invite.keySet().stream()
                            .map(MojangAPI::getName)
                            .collect(Collectors.toList());
                    beingVoteds.addAll(MemberConfig.kick.keySet().stream()
                            .map(MojangAPI::getName)
                            .collect(Collectors.toList()));

                    return beingVoteds;
                }
            }
        }
        return null; // No suggestions if conditions are not met
    }
}
