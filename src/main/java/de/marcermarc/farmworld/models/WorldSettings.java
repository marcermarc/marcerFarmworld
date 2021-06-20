package de.marcermarc.farmworld.models;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;

import java.time.ZonedDateTime;

public class WorldSettings {
    private static final String ENVIRONMENT = "environment";
    private static final String HAS_STRUCTURES = "hasStructures";
    private static final String TYPE = "type";
    private static final String WORLD_SIZE = "worldsize";
    private static final String RECREATION_RULE = "recreationRule";
    private static final String LAST_RECREATION = "lastRecreation";

    private static final CronParser CRON_PARSER = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));

    private World.Environment environment;
    private boolean structures;
    private WorldType type;
    private double worldsize;
    private String recreationRule;
    private Cron cronRule;
    private ZonedDateTime lastRecreation;

    private Location returnWorldLocation;

    public WorldSettings() {
    }

    public WorldSettings(ConfigurationSection configurationSection) {
        this.environment = World.Environment.valueOf(configurationSection.getString(ENVIRONMENT));
        this.structures = configurationSection.getBoolean(HAS_STRUCTURES);
        this.type = WorldType.valueOf(configurationSection.getString(TYPE));
        this.worldsize = configurationSection.getDouble(WORLD_SIZE);
        setRecreationRule(configurationSection.getString(RECREATION_RULE, ""));

        String lastRecreation = configurationSection.getString(LAST_RECREATION);
        if (lastRecreation != null && !lastRecreation.equals("")) {
            this.lastRecreation = ZonedDateTime.parse(lastRecreation);
        } else {
            this.lastRecreation = ZonedDateTime.now();
        }
    }

    public void fillConfig(ConfigurationSection configurationSection) {
        configurationSection.set(ENVIRONMENT, environment.toString());
        configurationSection.set(HAS_STRUCTURES, structures);
        configurationSection.set(TYPE, type.toString());
        configurationSection.set(WORLD_SIZE, worldsize);
        configurationSection.set(RECREATION_RULE, recreationRule);
        configurationSection.set(LAST_RECREATION, lastRecreation == null ? "" : lastRecreation.toString());
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

    public String getRecreationRule() {
        return recreationRule;
    }

    public void setRecreationRule(String recreationRule) {
        this.recreationRule = recreationRule;

        if (recreationRule != null && !recreationRule.equals("")) {
            try {
                cronRule = CRON_PARSER.parse(recreationRule).validate();
            } catch (Exception e) {
                cronRule = null;
            }
        }
    }

    public ZonedDateTime getLastRecreation() {
        return lastRecreation;
    }

    public void setLastRecreation(ZonedDateTime lastRecreation) {
        this.lastRecreation = lastRecreation;
    }

    public Cron getCronRule() {
        return cronRule;
    }
}
