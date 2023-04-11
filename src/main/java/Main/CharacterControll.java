package Main;

import Commands.CommandManager;
import DataManagement.DataManager;
import DataManagement.FileCreator;
import Utils.PlayerData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public final class CharacterControll extends JavaPlugin implements CommandExecutor {
    JDA jda = null;
    public FileConfiguration config;
    public static CharacterControll instance;
    private boolean discordStarted = false;

    @Override
    public void onEnable() {

        instance = this;
        this.saveDefaultConfig();
        config = this.getConfig();

        FileCreator.setup();

        CommandManager m = new CommandManager();
        getCommand("createcharacter").setExecutor(m);

        try {
            //Creates a discord bot
            JDABuilder builder = JDABuilder.createDefault(config.getString("discord.token"));
            builder.setActivity(Activity.playing("Minecraft"));
            builder.addEventListeners(m);
            jda = builder.build();
            jda.awaitReady();
            discordStarted = true;
        } catch (LoginException|InterruptedException e) {//Yes this is lazy but it also removes a lot of "if statements"
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[2DMC] ERROR: Cannot start the Discord bot."
                    + ChatColor.RESET + "\n"
                    + ChatColor.RED + "[2DMC] Please make sure that the token in the config.yml file is correct.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        //Saves the location of all active characters
        for (PlayerData p : CommandManager.activePlayers){
            DataManager.saveCharacter(p.getId(), p.getCharacter().getArmorStand().getLocation());
        }

        //Shuts down the discord bot
        if (discordStarted) jda.shutdownNow();
    }
}

