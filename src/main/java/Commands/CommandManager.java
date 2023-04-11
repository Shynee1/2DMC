package Commands;

import Main.Character;
import Main.CharacterControll;
import DataManagement.DataManager;
import Main.Grid;
import Utils.Util;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import Utils.PlayerData;
import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter implements CommandExecutor {

    FileConfiguration config = CharacterControll.instance.config;
    public static List<PlayerData> activePlayers = new ArrayList<>();

    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getAuthor().isBot()){
            return;
        }

        Bukkit.getScheduler().runTask(CharacterControll.instance, () -> {
            String prefix = config.getString("discord.prefix");

            String[] message = e.getMessage().getContentRaw().split(" ");
            String id = e.getAuthor().getId();
            TextCommandExecutor exec = new TextCommandExecutor(e.getTextChannel(), message, id, prefix);
            String cmd = message[0].replaceAll(prefix, "");

            boolean isActivePlayer = PlayerData.containsId(id, activePlayers);
            if (!isActivePlayer && !message[0].contains(prefix)) return;

            // Delete command messages
            e.getMessage().delete().queue();

            if (cmd.equalsIgnoreCase(config.getString("discord.commands.claim_command"))) {
                exec.executeClaimCommand();
            } else if (cmd.equalsIgnoreCase(config.getString("discord.commands.delete_command"))) {
                exec.executeDeleteCommand(activePlayers);
            } else if (cmd.equalsIgnoreCase(config.getString("discord.commands.start_command"))) {
                exec.executeStartCommand(activePlayers);
            } else if (cmd.equalsIgnoreCase(config.getString("discord.commands.stop_command"))) {
                exec.executeStopCommand(activePlayers);
            } else if (cmd.equalsIgnoreCase(config.getString("discord.commands.help_command"))){
                exec.executeHelpCommand(e.getAuthor());
            }

            //Checks for basic movement commands (ex: "w")
            if (isActivePlayer) {
                PlayerData playerData = PlayerData.getPlayerData(id, activePlayers);

                if (playerData == null){
                    System.out.println("Playerdata is null");
                }

                Character character = playerData.getCharacter();

                if (character.getArmorStand() == null){
                    System.out.println("Character is null");
                }

                //Loads chunk initially to prevent errors
                if (!character.getArmorStand().getLocation().getChunk().isLoaded())
                    character.getArmorStand().getLocation().getChunk().load(true);

                Message m = playerData.getMessage();

                switch (message[0].toLowerCase()) {
                    case "w" -> character.move(Util.Movement.FOWARD);
                    case "s" -> character.move(Util.Movement.BACKWARD);
                    case "a" -> character.move(Util.Movement.LEFT);
                    case "d" -> character.move(Util.Movement.RIGHT);
                    case "e" -> character.rotate(Util.Rotation.RIGHT);
                    case "q" -> character.rotate(Util.Rotation.LEFT);
                    case "z" -> character.placeFloorBlock();
                    case "x" -> character.placeBlock();
                    case "c" -> character.placeOneBlock();
                    case "v" -> character.placeTwoBlocks();
                    case "f" -> character.attack();
                    case "r" -> character.mine();
                    case "t" -> character.jump();
                }

                character.addGravity(character.getArmorStand());

                //Loads chunk after character moves in case they move out of chunk
                if (!character.getArmorStand().getLocation().getChunk().isLoaded())
                    character.getArmorStand().getLocation().getChunk().load(true);


                m.editMessage(Grid.getGrid(character.getArmorStand())).queue();
            }

        });
    }


    //Create character command
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;

            ItemStack skull = Util.getPlayerSkull(player);

            Location playerLoc = player.getLocation();

            ArmorStand a = player.getWorld().spawn(new Location(playerLoc.getWorld(), Math.floor(playerLoc.getX()) + 0.500, playerLoc.getY(), Math.floor(playerLoc.getZ()) + 0.500), ArmorStand.class);
            a.setArms(true);
            a.setCustomName(player.getDisplayName());
            a.getLocation().setYaw(0);
            a.setHelmet(skull);
            a.setCustomNameVisible(true);
            a.setGravity(false);
            a.setInvulnerable(true);
            a.setVisible(true);

            if (DataManager.initalizeCharacter(player.getDisplayName(), a.getUniqueId().toString(), a.getLocation())){
                player.sendMessage(ChatColor.GREEN + "Character successfully created");
                player.sendMessage(ChatColor.GOLD + "Use the " + config.getString("discord.prefix") + config.getString("discord.commands.claim_command") + " command in Discord to claim this character");
            } else {
                player.sendMessage(ChatColor.RED + "You have already created a character");
            }

        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
        }
        return true;
    }


}
