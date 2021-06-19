package de.marcermarc.farmworld;

import de.marcermarc.farmworld.controller.PluginController;
import de.marcermarc.farmworld.listener.Command;
import de.marcermarc.farmworld.listener.PlayerDied;
import de.marcermarc.farmworld.listener.PlayerJoin;
import de.marcermarc.farmworld.listener.PortalCreated;
import de.marcermarc.farmworld.timer.RecreateTimer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private PluginController controller;

    @Override
    public void onEnable() {
        controller = new PluginController(this);

        PluginManager pluginManager = getServer().getPluginManager();

        registerEvents(pluginManager);

        controller.getWorldController().loadOrCreateFromConfig();
    }

    private void registerEvents(PluginManager pluginManager) {
        pluginManager.registerEvents(new PlayerJoin(controller), this);
        pluginManager.registerEvents(new PlayerDied(controller), this);
        pluginManager.registerEvents(new PortalCreated(controller), this);

        Command c = new Command(controller);
        pluginManager.registerEvents(c, this);

        this.getCommand("marcerFarmworld").setExecutor(c);
        this.getCommand("marcerFarmworld").setTabCompleter(c);

        RecreateTimer timer = new RecreateTimer(controller);
        timer.init();
    }

    @Override
    public void onDisable() {
        controller.getPlayerData().save();
        controller.getConfig().saveConfig();
    }

}
