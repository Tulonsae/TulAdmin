package org.tulonsae.mc.plugins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;

/**
 * Base Bukkit plugin class for TulAdmin.
 * <p />
 * TulAdmin contains a number of commands used for preparing Minecraft worlds
 * for play.  These are particularly dangerous to existing worlds so it is
 * recommended to only enable this plugin when preparing a world.
 *
 * @author Tulonsae
 */
public class TulAdmin extends JavaPlugin {

    public String version;

    /**
     * Called when this plugin is enabled.
     * Loads the configuration; registers for Bukkit events.
     */
    public void onEnable() {

        // get plugin info
        version = this.getDescription().getVersion();

        // register commands
        getCommand("voidregion").setExecutor(new VoidRegion(this));
        getCommand("newregion").setExecutor(new NewRegion(this));
        getCommand("genborder").setExecutor(new GenBorderCommand(this));
        
        // log enable message
        Util.logInfo("version " + version + " enabled.");

        // turn on debug - TODO move this to config
        Util.setDebug(true);
    }

    /**
     * Called when this plugin is disabled.
     */
    public void onDisable() {
        
        // log disable message
        Util.logInfo("version " + version + " disabled.");
    }
}
