package me.rtx4090.member.voting;

import me.rtx4090.member.Member;
import me.rtx4090.member.config.MemberConfig;
import me.rtx4090.member.utils.MojangAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class Book { // 20 words * 15 lines
    public String token = Member.newToken();
    public ItemStack bookItem() {
        // Create a book item
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();


        meta.setTitle("Voting Book");
        meta.setAuthor("");

        // Create clickable text components
//        TextComponent rejectButton = new TextComponent("§cNOT AGREE");
//        rejectButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/member vote " + token + " reject" + " " + player));
//
//        TextComponent acceptButton = new TextComponent("§aAGREE");
//        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/member vote " + token + " accept" + " " + player));

/*        TextComponent rejectedButton = new TextComponent("§c§nNOT AGREE");
        rejectedButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));

        TextComponent acceptedButton = new TextComponent("§a§nAGREE");
        acceptedButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));*/

        // Build the invite page
        ComponentBuilder invitePage = new ComponentBuilder();
        MemberConfig.invite.forEach((key, value) -> {

            TextComponent acceptButton = new TextComponent("§aAGREE");
            acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/member vote " + token + " accept" + " " + key));

            TextComponent rejectButton = new TextComponent("§cNOT AGREE");
            rejectButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/member vote " + token + " reject" + " " + key));

            invitePage.append("§c● " +"§r§l" + MojangAPI.getName(key));
            invitePage.append("\n");
            invitePage.append(acceptButton);
            invitePage.append("     ");
            invitePage.append(rejectButton);
            invitePage.append("\n");
            invitePage.append("Due to: " + Member.formatDate(value.dueDate));
            invitePage.append("\n");
        });

        // Build the kick page
        ComponentBuilder kickPage = new ComponentBuilder();
        MemberConfig.kick.forEach((key, value) -> {
            //OfflinePlayer player = Bukkit.getOfflinePlayer(key);

            TextComponent acceptButton = new TextComponent("§aAGREE");
            acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/member vote " + token + " accept" + " " + key));

            TextComponent rejectButton = new TextComponent("§cNOT AGREE");
            rejectButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/member vote " + token + " reject" + " " + key));

            invitePage.append("§c● " +"§r§l" + MojangAPI.getName(key));
            invitePage.append("\n");
            invitePage.append(acceptButton);
            invitePage.append("     ");
            invitePage.append(rejectButton);
            invitePage.append("\n");
            invitePage.append("Due to: " + Member.formatDate(value.dueDate));
            invitePage.append("\n");
        });

        // Convert the page to a string format suitable for BookMeta
        meta.spigot().addPage(invitePage.create());
        meta.spigot().addPage(kickPage.create());

        book.setItemMeta(meta);

        return book;
    }

}
