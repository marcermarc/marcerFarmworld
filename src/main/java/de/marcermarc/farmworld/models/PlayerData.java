package de.marcermarc.farmworld.models;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class PlayerData {
    public static String RETURN_LOCATION = "returnLocation";
    public static String HAS_TO_RETURN = "hasToReturn";
    public static String WORLD = "world";
    public static String SPAWNPOINT = "spawnpoint";

    private UUID uuid;
    private Location returnLocation;
    private boolean hasToReturn;
    private String world;
    private Location spawnpoint;

    public PlayerData() {
    }

    public PlayerData(UUID uuid, ConfigurationSection configurationSection) {
        this.uuid = uuid;
        this.returnLocation = configurationSection.getLocation(RETURN_LOCATION);
        this.hasToReturn = configurationSection.getBoolean(HAS_TO_RETURN);
        this.world = configurationSection.getString(WORLD);
        this.spawnpoint = configurationSection.getLocation(SPAWNPOINT);
    }

    public void fillConfig(ConfigurationSection configurationSection) {
        configurationSection.set(RETURN_LOCATION, returnLocation);
        configurationSection.set(HAS_TO_RETURN, hasToReturn);
        configurationSection.set(WORLD, world);
        configurationSection.set(SPAWNPOINT, spawnpoint);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Location getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(Location returnLocation) {
        this.returnLocation = returnLocation;
    }

    public boolean hasToReturn() {
        return hasToReturn;
    }

    public void setHasToReturn(boolean hasToReturn) {
        this.hasToReturn = hasToReturn;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Location getSpawnpoint() {
        return spawnpoint;
    }

    public void setSpawnpoint(Location spawnpoint) {
        this.spawnpoint = spawnpoint;
    }
}
