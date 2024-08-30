package me.rtx4090.member.player;

import java.util.*;

public class PlayerProfile {
    public List<UUID> reject = new ArrayList<>();
    public List<UUID> accept = new ArrayList<>();
    public Date dueDate = getDateSevenDaysLater();
    public UUID invitor = null;

    @Override
    public String toString() {
        return "accept: " + accept.size() + ", reject: " + reject.size();
    }
    public static Date getDateSevenDaysLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        return calendar.getTime();
    }
}
