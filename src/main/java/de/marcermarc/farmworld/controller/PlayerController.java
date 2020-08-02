package de.marcermarc.farmworld.controller;

import de.marcermarc.farmworld.Messages;
import de.marcermarc.farmworld.Util;
import de.marcermarc.farmworld.models.PlayerData;
import de.marcermarc.farmworld.models.WorldSettings;
import jdk.internal.jline.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerController {
    private final PluginController controller;

    public PlayerController(PluginController controller) {
        this.controller = controller;
    }

    public void testPlayerHasToReturn(Player player) {
        PlayerData playerData = controller.getPlayerData().getData(player.getUniqueId());
        if (playerData != null && playerData.hasToReturn()) {
            returnPlayer(player, playerData);
        }
    }

    public boolean sendPlayer(Player player, String world) {
        WorldSettings worldSettings = controller.getConfig().getWorldSettings().get(world);
        if (worldSettings == null) {
            return false;
        }

        Bukkit.getLogger().info("Get:" + player.getBedSpawnLocation().toString());

        PlayerData playerData = new PlayerData();
        playerData.setUuid(player.getUniqueId());
        playerData.setReturnLocation(player.getLocation());
        playerData.setWorld(world);
        playerData.setSpawnpoint(player.getBedSpawnLocation());

        controller.getPlayerData().setData(playerData);

        Location destination = Util.getFarmWorldSpawnLocation(Bukkit.getWorld(world));
        player.teleport(destination);

        return true;
    }

    public boolean returnPlayer(Player player) {
        PlayerData playerData = controller.getPlayerData().getData(player.getUniqueId());

        if (playerData == null) {
            return false;
        }

        returnPlayer(player, playerData);
        return true;
    }

    private void returnPlayer(Player player, PlayerData data) {
        returnPlayerTeleportation(player, data);

        setSpawnpoint(player, data.getSpawnpoint());

        controller.getPlayerData().removeData(player.getUniqueId());
    }

    private void returnPlayerTeleportation(Player player, PlayerData data) {
        if (Util.testForAir(data.getReturnLocation())) {
            player.teleport(data.getReturnLocation());
            return;
        }

        if (controller.getConfig().getWorldSettings().containsKey(data.getWorld())) {
            WorldSettings worldSettings = controller.getConfig().getWorldSettings().get(data.getWorld());

            if (Util.testForAir(worldSettings.getReturnWorldLocation())) {
                player.teleport(controller.getConfig().getWorldSettings().get(data.getWorld()).getReturnWorldLocation());
                return;
            } else {
                Bukkit.getConsoleSender().sendMessage(Messages.getWorldReturnNotAir(data.getWorld()));
            }
        }

        Location defaultReturnLocation = controller.getConfig().getDefaultReturnLocation();
        if (Util.testForAir(defaultReturnLocation)) {
            player.teleport(defaultReturnLocation);
            return;
        } else {
            Bukkit.getConsoleSender().sendMessage(Messages.getDefaultReturnNotAir());
        }

        Location newLocation = new Location(
                defaultReturnLocation.getWorld(),
                defaultReturnLocation.getX(),
                Util.getSpawnY(defaultReturnLocation.getWorld(), defaultReturnLocation.getBlockX(), defaultReturnLocation.getBlockZ()),
                defaultReturnLocation.getZ());

        player.teleport(newLocation);
    }

    public void setSpawnpoint(Player player, @Nullable Location spawnpoint) {
        Location current = player.getBedSpawnLocation();

        if ((current == null && spawnpoint != null) || controller.getConfig().getWorldSettings().containsKey(current.getWorld().getName())) {
            player.setBedSpawnLocation(spawnpoint, true);
        }
    }
}

