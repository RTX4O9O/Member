import me.rtx4090.member.player.PlayerProfile;
import me.rtx4090.member.utils.ConfigUtil;
import me.rtx4090.member.utils.MojangAPI;

import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Testing {
    public static Map<UUID, PlayerProfile> invite = new HashMap<>();
    public static Map<UUID, PlayerProfile> kick = new HashMap<>();
    public static Map<UUID, Date> date = new HashMap<>();



    public static void main(String[] args) {
        String playerName = "Notch"; // Replace with the player's name
        UUID uuid = MojangAPI.getUUID(playerName);

        if (uuid != null) {
            System.out.println("UUID of player " + playerName + " is: " + uuid);
        } else {
            System.out.println("Could not retrieve UUID for player " + playerName);
            return;
        }

        String name = MojangAPI.getName(uuid);

        if (name != null) {
            System.out.println("Name of player " + uuid + " is: " + name);
        } else {
            System.out.println("Could not retrieve name for player " + uuid);
        }
    }
}
