package me.rtx4090.member.commands;

import me.rtx4090.member.Member;
import me.rtx4090.member.config.MemberConfig;
import me.rtx4090.member.config.RatioConfig;
import me.rtx4090.member.player.PlayerProfile;
import me.rtx4090.member.utils.MojangAPI;
import me.rtx4090.member.voting.Gui;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class MemberCommand implements CommandExecutor {
    private Map<UUID, String> playerTokens = new HashMap<>();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§c[Member] Only players can use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        UUID playerUUID = player.getUniqueId();
        if (strings.length == 0) {
            commandSender.sendMessage("§r---------------------------");
            commandSender.sendMessage("§r§bMember v1.0 §rby §bRTX4090");
            commandSender.sendMessage("§r§7Manage whitelist with every members within the server.");
            commandSender.sendMessage("");
            commandSender.sendMessage("§r/member §3- §rshow help and introduction");
            commandSender.sendMessage("§r/member invite §3- §radd a member to member adding suggestion");
            commandSender.sendMessage("§r/member kick §3- §radd a member to member kicking suggestion");
            commandSender.sendMessage("§r/member cancel §3- §rcancel a member suggestion (only works for suggester and admin)");
            commandSender.sendMessage("§r/member vote §3- §ropen the voting gui");
            commandSender.sendMessage("§r---------------------------");
            return true;
        }
        switch (strings[0]) {
            case "invite": // add a member suggestion
                if (strings.length < 2) {
                    commandSender.sendMessage("§c[Member] Please use /member invite <player>");
                } else if (MojangAPI.getUUID(strings[1]) == null) {
                    commandSender.sendMessage("§c[Member] Player not found.");
                } else if (MemberConfig.invite.containsKey(MojangAPI.getUUID(strings[1]))) {
                    commandSender.sendMessage("§c[Member] Player is already in the invite list.");
                } else {
                    if (MemberConfig.invite.size() >= 5) {
                        commandSender.sendMessage("§c[Member] You can only suggest up to 5 players at a time.");
                        return true;
                    }

                    PlayerProfile invitePlayerProfile = new PlayerProfile();
                    invitePlayerProfile.invitor = MojangAPI.getUUID(commandSender.getName());
                    MemberConfig.invite.put(MojangAPI.getUUID(strings[1]), invitePlayerProfile);
                    MemberConfig.save();
                    commandSender.sendMessage("§a[Member] Successfully added player " + strings[1] + " to the invite list.");
                }

                return true;

            case "kick": // add a kick member suggestion
                if (strings.length < 2) {
                    commandSender.sendMessage("§c[Member] Please use /member kick <player>");
                } else if (MojangAPI.getUUID(strings[1]) == null) {
                    commandSender.sendMessage("§c[Member] Player not found.");
                } else if (MemberConfig.kick.containsKey(MojangAPI.getUUID(strings[1]))) {
                    commandSender.sendMessage("§c[Member] Player is already in the kick list.");
                } else {
                    if (MemberConfig.kick.size() >= 5) {
                        commandSender.sendMessage("§c[Member] You can only suggest up to 5 players at a time.");
                        return true;
                    }
                    OfflinePlayer kickPlayer = Bukkit.getOfflinePlayer(strings[1]);
                    PlayerProfile kickPlayerProfile = new PlayerProfile();
                    kickPlayerProfile.invitor = MojangAPI.getUUID(commandSender.getName());
                    MemberConfig.kick.put(kickPlayer.getUniqueId(), kickPlayerProfile);
                    MemberConfig.save();
                    commandSender.sendMessage("§a[Member] Successfully added player " + strings[1] + " to the kick list.");
                }

                return true;

            case "cancel": // cancel a member suggestion (can only execute by the member who suggested and admin)
                if (strings.length < 2) {
                    commandSender.sendMessage("§c[Member] Please use /member cancel <player>");
                } else {
                    UUID targetUUID = MojangAPI.getUUID(strings[1]);
                    UUID senderUUID = MojangAPI.getUUID(commandSender.getName());

                    if (targetUUID == null) {
                        commandSender.sendMessage("§c[Member] Player not found.");
                    } else if (!commandSender.isOp() && !isInvitor(targetUUID, senderUUID)) {
                        commandSender.sendMessage("§c[Member] You have to be suggester or admin to perform this action.");
                    } else if (MemberConfig.invite.containsKey(targetUUID)) {
                        MemberConfig.invite.remove(targetUUID);
                        MemberConfig.save();
                        commandSender.sendMessage("§a[Member] Successfully removed player " + strings[1] + " from the invite list.");
                    } else if (MemberConfig.kick.containsKey(targetUUID)) {
                        MemberConfig.kick.remove(targetUUID);
                        MemberConfig.save();
                        commandSender.sendMessage("§a[Member] Successfully removed player " + strings[1] + " from the invite list.");
                    } else {
                        commandSender.sendMessage("§c[Member] Player is not in the suggestion list.");
                    }
                }
                return true;

            case "vote": // open voting gui
                if (strings.length == 4) {
                    // book executing
                    String providedToken = strings[1];
                    if (!playerTokens.containsKey(playerUUID) || !playerTokens.get(playerUUID).equals(providedToken)) {
                        commandSender.sendMessage("§c[Member] Error. Please use the book to execute the command.");
                        return true;
                    }

                    //OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(strings[3]);
                    if (MojangAPI.getName(UUID.fromString(strings[3])) == null) {
                        commandSender.sendMessage("§c[Member] Player not found.");
                        return true;
                    } else if (!MemberConfig.invite.containsKey(UUID.fromString(strings[3])) && !MemberConfig.kick.containsKey(UUID.fromString(strings[3]))) {
                        commandSender.sendMessage("§c[Member] Player is not in the suggestion list.");
                        return true;
                    }

                    Map<UUID, PlayerProfile> targetMap = MemberConfig.invite.containsKey(UUID.fromString(strings[3])) ? MemberConfig.invite : MemberConfig.kick;
                    PlayerProfile profile = targetMap.get(UUID.fromString(strings[3]));
                    UUID senderUUID = ((Player) commandSender).getUniqueId();

                    switch (strings[2]) {
                        case "accept":
                            profile.accept.add(senderUUID);
                            if (profile.reject.contains(senderUUID)) {
                                profile.reject.remove(senderUUID);
                                commandSender.sendMessage("[Member] You voted not agree before. Your vote has been changed to agree.");
                            }
                            break;
                        case "reject":
                            profile.reject.add(senderUUID);
                            if (profile.accept.contains(senderUUID)) {
                                profile.accept.remove(senderUUID);
                                commandSender.sendMessage("[Member] You voted agree before. Your vote has been changed to not agree.");
                            }
                            break;
                        default:
                            commandSender.sendMessage("§c[Member] Error! Error Code: gfhhe");
                            return true;
                    }

                    MemberConfig.save();
                    return true;
                } else {
                    // Open book
                    Gui gui = new Gui();
                    gui.newBook(player);
                    playerTokens.put(playerUUID, gui.token);
                    gui.open(player);
                    return true;
                }
            case "endvote":
                if (strings.length != 2) {
                    commandSender.sendMessage("§c[Member] Please use /member endvote <player>");
                } else {
                    UUID targetUUID = MojangAPI.getUUID(strings[1]);
                    UUID senderUUID = MojangAPI.getUUID(commandSender.getName());

                    if (targetUUID == null) {
                        commandSender.sendMessage("§c[Member] Player not found.");
                    } else if (!commandSender.isOp() && !isInvitor(targetUUID, senderUUID)) {
                        commandSender.sendMessage("§c[Member] You have to be suggester or admin to perform this action.");
                    } else if (MemberConfig.invite.containsKey(targetUUID)) {
                        if (RatioConfig.evaluate(targetUUID, true)) {
                            getServer().getOfflinePlayer(targetUUID).setWhitelisted(true);
                            Member.sendMessageToAllPlayers("§a[Member] " + strings[1] + " has pass the vote and become one of us!");
                        } else {
                            Member.sendMessageToAllPlayers("§a[Member] " + strings[1] + " didn't pass the vote!");
                        }

                        MemberConfig.invite.remove(targetUUID);
                        MemberConfig.save();
                    } else if (MemberConfig.kick.containsKey(targetUUID)) {
                        if (RatioConfig.evaluate(targetUUID, false)) {
                            getServer().getOfflinePlayer(targetUUID).setWhitelisted(true);
                            Member.sendMessageToAllPlayers("§a[Member] " + strings[1] + " has pass the vote and leave us!");
                        } else {
                            Member.sendMessageToAllPlayers("§a[Member] " + strings[1] + " didn't pass the vote!");
                        }
                        MemberConfig.kick.remove(targetUUID);
                        MemberConfig.save();
                    } else {
                        commandSender.sendMessage("§c[Member] Player is not in the suggestion list.");
                    }
                }
                return true;
            default: // Invalid command -> help message
                commandSender.sendMessage("§r---------------------------");
                commandSender.sendMessage("§r§bMember v1.0 §rby §bRTX4090");
                commandSender.sendMessage("§r§7Manage whitelist with every members within the server.");
                commandSender.sendMessage("");
                commandSender.sendMessage("§r/member §3- §rshow help and introduction");
                commandSender.sendMessage("§r/member invite §3- §radd a member to member adding suggestion");
                commandSender.sendMessage("§r/member kick §3- §radd a member to member kicking suggestion");
                commandSender.sendMessage("§r/member cancel §3- §rcancel a member suggestion (only works for suggester and admin)");
                commandSender.sendMessage("§r/member vote §3- §ropen the voting gui");
                commandSender.sendMessage("§r---------------------------");
                return true;
        }
    }

    private boolean isInvitor(UUID playerUUID, UUID senderUUID) {
        return senderUUID.equals(MemberConfig.invite.get(playerUUID).invitor) ||
                senderUUID.equals(MemberConfig.kick.get(playerUUID).invitor);
    }
}