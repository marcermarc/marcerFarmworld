package de.marcermarc.farmworld.listener;

import de.marcermarc.farmworld.controller.PluginController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private final PluginController controller;

    public PlayerJoin(PluginController controller) {
        this.controller = controller;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        controller.getPlayerController().testPlayerHasToReturn(event.getPlayer());
    }
}
