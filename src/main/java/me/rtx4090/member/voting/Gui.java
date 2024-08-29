package me.rtx4090.member.voting;

import org.bukkit.entity.Player;

public class Gui {
    public void open(Player player) {
        // Open the GUI
        player.openBook(book.bookItem());
    }
    public void newBook(Player player) {
        // Gen a new book
        book = new Book();
        token = book.token;

    }
    public String token;
    public Book book;
}
