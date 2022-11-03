package Commands;

import DataManagement.Data;
import DataManagement.DataManager;
import Main.Character;
import Main.CharacterControll;
import Main.Grid;
import Utils.PlayerData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.configuration.file.FileConfiguration;

import java.awt.*;
import java.util.List;

public class TextCommandExecutor {

    TextChannel channel;
    String[] message;
    String id;
    FileConfiguration config = CharacterControll.instance.config;
    String prefix;

    TextCommandExecutor(TextChannel channel, String[] message, String id, String prefix) {
        this.channel = channel;
        this.message = message;
        this.id = id;
        this.prefix = prefix;
    }

    public void executeClaimCommand() {
        if (message.length != 2){
            channel.sendMessage("Invalid command. Please use the following format: `" + prefix + config.getString("discord.commands.claim_command") +" <username>`").queue();
            return;
        }

        if (DataManager.matchCharacter(message[1], id)){
            channel.sendMessage("Character has been successfully claimed. You can start it using the following command: `" + prefix + config.getString("discord.commands.start_command") + "`").queue();
        } else {
            channel.sendMessage("Character could not be claimed.").queue();
        }
    }

    public void executeDeleteCommand(List<PlayerData> activePlayers){
        if (message.length != 1){
            channel.sendMessage("Invalid command. Please use the following format: `" + prefix + config.getString("discord.commands.delete_command") + "`").queue();
            return;
        }

        if (PlayerData.containsId(id, activePlayers)){
            PlayerData d = PlayerData.getPlayerData(id, activePlayers);
            activePlayers.remove(d);
        }

        Data data = DataManager.getCharacterData(id);
        if (data != null && DataManager.deleteCharacter(id)){
            Character character = new Character(data.getUuid(), data.getLocation());
            if (character.getArmorStand() != null){
                character.getArmorStand().remove();
            }

            channel.sendMessage("Character has been successfully deleted.").queue();
        } else {
            channel.sendMessage("You do not have a character currently created.").queue();
        }
    }

    public void executeStartCommand(List<PlayerData> activePlayers) {
        if (message.length != 1) {
            channel.sendMessage("Invalid command. Please use the following format: `" + prefix + config.getString("discord.commands.start_command") + "`").queue();
            return;
        }

        if (PlayerData.containsId(id, activePlayers)){
            channel.sendMessage("You are already in a session. You can stop it using the following command: `" + prefix + config.getString("discord.commands.stop_command") + "`").queue();;
        } else {
            Data data = DataManager.getCharacterData(id);
            if (data != null) {
                Character character = new Character(data.getUuid(), data.getLocation());

                Message message = channel.sendMessage(Grid.getGrid(character.getArmorStand())).complete();

                activePlayers.add(new PlayerData(id, character, message));

            } else {
                channel.sendMessage("You do not have a character currently created.").queue();
            }
        }
    }

    public void executeStopCommand(List<PlayerData> activePlayers) {
        if (message.length != 1) {
            channel.sendMessage("Invalid command. Please use the following format: `" + prefix + config.getString("discord.commands.stop_command") + "`").queue();
            return;
        }

        if (PlayerData.containsId(id, activePlayers)) {
            PlayerData data = PlayerData.getPlayerData(id, activePlayers);
            Character character = data.getCharacter();
            if (DataManager.saveCharacter(id, character.getArmorStand().getLocation())){
                activePlayers.remove(data);
                data.getMessage().delete().queue();
                channel.sendMessage("Session has been stopped.").queue();
            }
        } else {
            channel.sendMessage("You are not currently in a session. You can start one using the following command: `" + prefix + config.getString("discord.commands.start_command") + "`").queue();
        }
    }

    public void executeHelpCommand(User author){
        EmbedBuilder eb = new EmbedBuilder();

        String prefix = config.getString("discord.prefix");

        eb.setTitle("2DMC Help");
        //This was a huge pain to write out
        eb.setDescription(String.format("**Commands:**" +
                "\n`%s%s <username>` - Claims a Character created in Minecraft" +
                "\n`%s%s` - Starts a 2DMC session" +
                "\n`%s%s` - Stops a 2DMC session" +
                "\n`%s%s` - Deletes your Character from 2DMC and Minecraft" +
                "\n`%s%s` - Displays a list of 2DMC commands, game controls, and blocks" +
                "\n\n**Game Controls:**" +
                "\n `w` - Moves the character foward" +
                "\n `a` - Moves the character left" +
                "\n `s` - Moves the character backwards" +
                "\n `d` - Moves the character right" +
                "\n `e` - Rotates the character 90° to the right" +
                "\n `q` - Rotates the character 90° to the left" +
                "\n `e` - Rotates the character 90° to the right" +
                "\n `z` - Makes the character jump and place a block below it" +
                "\n `x` - Places a block on the ground in front of the character" +
                "\n `c` - Places a block in front of the character" +
                "\n `v` - Places two blocks in front of the character" +
                "\n `f` - Kills all entities in an 11x11 box around the character" +
                "\n `r` - Mines two blocks in front of the character" +
                "\n\n**Emoji Key:**" +
                "\n \uD83D\uDE33 - Characters" +
                "\n \uD83E\uDD28 - Players" +
                "\n \uD83D\uDE08 - Hostile Entities" +
                "\n \uD83D\uDC37 - All Other Entities" +
                "\n ⬛ - Air" +
                "\n \uD83D\uDFE6 - Water" +
                "\n \uD83D\uDFE5 - Lava" +
                "\n ⬜ - All Other Blocks",
                prefix, config.getString("discord.commands.claim_command"),
                prefix, config.getString("discord.commands.start_command"),
                prefix, config.getString("discord.commands.stop_command"),
                prefix, config.getString("discord.commands.delete_command"),
                prefix, config.getString("discord.commands.help_command")));

        eb.setAuthor(author.getName(), author.getAvatarUrl(), author.getAvatarUrl());
        eb.setColor(new Color(96,209,246,255));
        eb.setFooter("This took an hour to write"); //Had to make sure everyone knows

        channel.sendMessageEmbeds(eb.build()).queue();
    }
}
