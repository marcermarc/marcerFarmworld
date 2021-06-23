package de.marcermarc.farmworld.controller;

import de.marcermarc.farmworld.models.WorldSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ConfigController {

    private static final String CONFIG_NAME = "config.yml";

    private static final String WORLDS = "worlds";
    private static final String DEFAULT_RETURN_LOCATION = "defaultReturnLocation";
    private static final String ENTER_VIA_COMMAND_NONOP = "allowedToEnterWithCommandNonOP";
    private static final String ENTER_VIA_COMMANDBLOCK = "allowedToEnterWithCommandblock";

    private final PluginController controller;
    private final YamlConfiguration configuration;
    private final File configFile;
    private ConfigurationSection worldSection;

    private Location defaultReturnLocation;
    private boolean enterViaCommandNonOp;
    private boolean enterViaCommandblock;
    private final HashMap<String, WorldSettings> worldSettings = new HashMap<>();

    public ConfigController(PluginController controller) {
        this.controller = controller;

        this.configFile = new File(controller.getMain().getDataFolder(), CONFIG_NAME);

        configuration = YamlConfiguration.loadConfiguration(configFile);

        setDefaultConfig();

        loadConfig();
    }

    private void setDefaultConfig() {
        configuration.addDefault(DEFAULT_RETURN_LOCATION, new Location(Bukkit.getWorlds().get(0), 0, 60, 0));
        configuration.addDefault(ENTER_VIA_COMMAND_NONOP, false);
        configuration.addDefault(ENTER_VIA_COMMANDBLOCK, false);

        configuration.options().copyDefaults(true);
        saveConfig();
    }

    //region load
    public boolean loadConfig() {
        worldSection = configuration.getConfigurationSection(WORLDS);
        if (worldSection == null) {
            worldSection = configuration.createSection(WORLDS);
        }

        defaultReturnLocation = configuration.getLocation(DEFAULT_RETURN_LOCATION);
        enterViaCommandNonOp = configuration.getBoolean(ENTER_VIA_COMMAND_NONOP);
        enterViaCommandblock = configuration.getBoolean(ENTER_VIA_COMMANDBLOCK);

        loadWorlds();

        return true;
    }

    private void loadWorlds() {
        for (String name : worldSection.getKeys(false)) {
            worldSettings.put(name, new WorldSettings(Objects.requireNonNull(worldSection.getConfigurationSection(name))));
        }
    }
    //endregion

    //region save
    public boolean saveConfig() {
        try {
            configuration.save(configFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    //endregion

    //region worlds
    public void addOrUpdateWorld(String name, WorldSettings settings) {
        ConfigurationSection section = worldSection.getConfigurationSection(name);
        if (section == null) {
            section = worldSection.createSection(name);
        }

        settings.fillConfig(section);
        saveConfig();

        worldSettings.put(name, settings);
    }

    public void removeWorld(String name) {
        worldSection.set(name, null);
        saveConfig();

        worldSettings.remove(name);
    }
    //endregion

    //region getters and setters
    public PluginController getController() {
        return controller;
    }

    public HashMap<String, WorldSettings> getWorldSettings() {
        return worldSettings;
    }

    public Location getDefaultReturnLocation() {
        return defaultReturnLocation;
    }

    public void setDefaultReturnLocation(Location defaultReturnLocation) {
        this.defaultReturnLocation = defaultReturnLocation;

        configuration.set(DEFAULT_RETURN_LOCATION, defaultReturnLocation);
    }

    public boolean isEnterViaCommandNonOp() {
        return enterViaCommandNonOp;
    }

    public boolean isEnterViaCommandblock() {
        return enterViaCommandblock;
    }

    //endregion
}
