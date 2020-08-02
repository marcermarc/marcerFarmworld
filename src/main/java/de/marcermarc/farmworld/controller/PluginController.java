package de.marcermarc.farmworld.controller;

import de.marcermarc.farmworld.Main;

public class PluginController {
    private final ConfigController config;
    private final Main main;
    private final PlayerDataController playerData;
    private final PlayerController playerController;
    private final WorldController worldController;

    public PluginController(Main main) {
        this.main = main;
        this.config = new ConfigController(this);
        this.playerData = new PlayerDataController(this);
        this.playerController = new PlayerController(this);
        this.worldController = new WorldController(this);
    }

    public ConfigController getConfig() {
        return config;
    }

    public Main getMain() {
        return main;
    }

    public PlayerDataController getPlayerData() {
        return playerData;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public WorldController getWorldController() {
        return worldController;
    }
}