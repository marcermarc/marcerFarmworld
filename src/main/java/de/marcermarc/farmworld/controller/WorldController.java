package de.marcermarc.farmworld.controller;

import de.marcermarc.farmworld.Messages;
import de.marcermarc.farmworld.Util;
import de.marcermarc.farmworld.models.WorldSettings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public class WorldController {
    private final PluginController controller;

    public WorldController(PluginController controller) {
        this.controller = controller;
    }

    public void loadOrCreateFromConfig() {
        for (Map.Entry<String, WorldSettings> entry : controller.getConfig().getWorldSettings().entrySet()) {
            String worldName = entry.getKey();
            WorldSettings settings = entry.getValue();

            Optional<World> world = getFarmworld(worldName);

            if (!world.isPresent()) {
                create(worldName, controller.getConfig().getWorldSettings().get(worldName), null);

            } else if (world.get().canGenerateStructures() != settings.isStructures()
                    || world.get().getEnvironment() != settings.getEnvironment()) {

                controller.getMain().getLogger().log(Level.WARNING, Messages.getWorldSettingsChanged(worldName));
            }
        }

        // ToDo Delete unused worlds if possible
    }

    public boolean recreate(String name, @Nullable Player player) {
        WorldSettings worldSettings = controller.getConfig().getWorldSettings().get(name);

        return delete(name, worldSettings)
                && create(name, worldSettings, player);
    }

    public boolean deleteAndRemoveFromConfig(String name) {
        WorldSettings worldSettings = controller.getConfig().getWorldSettings().get(name);

        boolean success = delete(name, worldSettings);

        controller.getConfig().removeWorld(name);

        return success;
    }

    public boolean delete(String name, WorldSettings settings) {
        Optional<World> world = getFarmworld(name);

        if (!world.isPresent()) {
            return false;
        }

        File worldFolder = world.get().getWorldFolder();

        for (Player player : world.get().getPlayers()) {
            controller.getPlayerController().returnPlayer(player);
            player.sendMessage(Messages.getWorldDelete());
        }

        controller.getPlayerData().getAll().stream()
                .filter(x -> x.getWorld().equals(name))
                .forEach(x -> {
                    x.setHasToReturn(true);
                    controller.getPlayerData().setData(x);
                });

        Bukkit.unloadWorld(world.get(), false);

        try {
            Util.deleteRecursively(worldFolder.toPath());
            return true;
        } catch (IOException e) {
            controller.getMain().getLogger().log(Level.SEVERE, Messages.getWorldDeleteFailed(name), e);
            return false;
        }

    }

    public boolean createAndSaveConfig(String name, WorldSettings settings, @Nullable Player player) {
        boolean success = create(name, settings, player);

        if (success) {
            controller.getConfig().addOrUpdateWorld(name, settings);
        }

        return success;
    }

    public boolean create(String name, WorldSettings settings, @Nullable Player player) {
        WorldCreator creator = new WorldCreator(name);

        creator.environment(settings.getEnvironment());
        creator.generateStructures(settings.isStructures());
        creator.type(settings.getType());

        World world = Bukkit.createWorld(creator);

        if (world == null) {
            controller.getMain().getLogger().log(Level.SEVERE, Messages.getWorldCreationFailedConsole(name));
            if (player != null) {
                player.sendMessage(Messages.getWorldCreationFailedPlayer(name));
            }

            return false;
        }

        if (!isWorldNew(world)) {
            return true;
        }

        createReturnPossibility(world);

        world.setSpawnLocation(Util.getFarmWorldSpawnLocation(world));
        world.getWorldBorder().setSize(settings.getWorldsize());
        world.setPVP(false); // ToDo configurable

        return true;
    }

    private void createReturnPossibility(World world) {
        world.loadChunk(0, 0, true);

        int y = Util.getSpawnY(world, 0, 0);

        Block spawnBlock = world.getBlockAt(0, y, 0);
        spawnBlock.setType(Material.COMMAND_BLOCK, false);
        CommandBlock spawnBlockState = (CommandBlock) spawnBlock.getState();
        spawnBlockState.setCommand("marcerfarmworld return");
        spawnBlockState.update(false, false);

        Block button = world.getBlockAt(0, y + 1, 0);
        button.setType(Material.STONE_BUTTON, false);
        Switch buttonFace = (Switch) button.getBlockData();
        buttonFace.setAttachedFace(FaceAttachable.AttachedFace.FLOOR);
        button.setBlockData(buttonFace, false);
    }

    private Optional<World> getFarmworld(String name) {
        return Bukkit.getWorlds().stream()
                .filter(x -> x.getName().equals(name))
                .findAny();
    }

    private boolean isWorldNew(World world) {
        return !world.isChunkGenerated(0, 0);
    }
}
