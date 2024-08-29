package me.rtx4090.member.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PlayerProfile {
    public List<UUID> reject = new ArrayList<>();
    public List<UUID> accept = new ArrayList<>();
    public Date date = new Date();
    public UUID invitor = null;

    @Override
    public String toString() {
        return "accept: " + accept.size() + ", reject: " + reject.size();
    }
}
