package de.marcermarc.farmworld.models;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class WorldSettings {
    private static final String ENVIRONMENT = "environment";
    private static final String HAS_STRUCTURES = "hasStructures";
    private static final String TYPE = "type";
    private static final String WORLD_SIZE = "worldsize";

    private World.Environment environment;
    private boolean structures;
    private WorldType type;
    private double worldsize;

    private Location returnWorldLocation;

    public WorldSettings() {
    }

    public WorldSettings(ConfigurationSection configurationSection) {
        this.environment = World.Environment.valueOf(configurationSection.getString(ENVIRONMENT));
        this.structures = configurationSection.getBoolean(HAS_STRUCTURES);
        this.type = WorldType.valueOf(configurationSection.getString(TYPE));
        this.worldsize = configurationSection.getDouble(WORLD_SIZE);
    }

    public void fillConfig(ConfigurationSection configurationSection) {
        configurationSection.set(ENVIRONMENT, environment.toString());
        configurationSection.set(HAS_STRUCTURES, structures);
        configurationSection.set(TYPE, type.toString());
        configurationSection.set(WORLD_SIZE, worldsize);
    }

    public World.Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(World.Environment environment) {
        this.environment = environment;
    }

    public boolean isStructures() {
        return structures;
    }

    public void setStructures(boolean structures) {
        this.structures = structures;
    }

    public WorldType getType() {
        return type;
    }

    public void setType(WorldType type) {
        this.type = type;
    }

    public double getWorldsize() {
        return worldsize;
    }

    public void setWorldsize(double worldsize) {
        this.worldsize = worldsize;
    }

    public Location getReturnWorldLocation() {
        return returnWorldLocation;
    }

    public void setReturnWorldLocation(Location returnWorldLocation) {
        this.returnWorldLocation = returnWorldLocation;
    }
}
