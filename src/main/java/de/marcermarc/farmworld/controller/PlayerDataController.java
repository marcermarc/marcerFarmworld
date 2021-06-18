package de.marcermarc.farmworld.controller;

import de.marcermarc.farmworld.models.PlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerDataController {
    private static final String CONFIG_NAME = "playerData.yml";
    private static final String PLAYERS = "players";

    private final PluginController controller;
    private YamlConfiguration configuration;
    private ConfigurationSection mainSection;
    private File configFile;

    public PlayerDataController(PluginController controller) {
        this.controller = controller;

        this.configFile = new File(controller.getMain().getDataFolder(), CONFIG_NAME);

        load();
    }

    //region Data
    public List<PlayerData> getAll() {
        return mainSection.getKeys(false).stream()
                .map(x -> new PlayerData(UUID.fromString(x), Objects.requireNonNull(mainSection.getConfigurationSection(x))))
                .collect(Collectors.toList());
    }

    @Nullable
    public PlayerData getData(UUID uuid) {
        ConfigurationSection section = mainSection.getConfigurationSection(uuid.toString());

        if (section == null) {
            return null;
        }

        return new PlayerData(uuid, section);
    }

    public void setData(PlayerData data) {
        ConfigurationSection section = mainSection.getConfigurationSection(data.getUuid().toString());

        if (section == null) {
            section = mainSection.createSection(data.getUuid().toString());
        }

        data.fillConfig(section);
    }

    public void removeData(UUID player) {
        mainSection.set(player.toString(), null);
    }
    //endregion

    //region load
    public void load() {
        configuration = YamlConfiguration.loadConfiguration(configFile);

        mainSection = configuration.getConfigurationSection(PLAYERS);

        if (mainSection == null) {
            mainSection = configuration.createSection(PLAYERS);
        }
    }
    //endregion

    //region save
    public boolean save() {
        try {
            configuration.save(configFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    //endregion
}
