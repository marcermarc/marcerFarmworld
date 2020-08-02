package de.marcermarc.farmworld.listener;

import de.marcermarc.farmworld.controller.PluginController;
import de.marcermarc.farmworld.models.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDied implements Listener {
    private final PluginController controller;

    public PlayerDied(PluginController controller) {
        this.controller = controller;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        PlayerData data = controller.getPlayerData().getData(event.getEntity().getUniqueId());

        if (data == null) {
            return;
        }

        controller.getPlayerController().setSpawnpoint(event.getEntity(), data.getSpawnpoint());

        controller.getPlayerData().removeData(event.getEntity().getUniqueId());
    }
}
