package Utils;

import Main.Character;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class PlayerData {
    private String id;
    private Character character;
    private Message message;

    public PlayerData(String id, Character character, Message message) {
        this.id = id;
        this.character = character;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public Character getCharacter() {
        return character;
    }

    public Message getMessage() {
        return message;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public static boolean containsId(String id, List<PlayerData> list) {
        for (PlayerData playerData : list) {
            if (playerData.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static PlayerData getPlayerData(String id, List<PlayerData> list) {
        for (PlayerData playerData : list) {
            if (playerData.getId().equals(id)) {
                return playerData;
            }
        }
        return null;
    }
}