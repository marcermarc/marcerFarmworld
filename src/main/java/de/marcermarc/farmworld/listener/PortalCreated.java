package de.marcermarc.farmworld.listener;

import de.marcermarc.farmworld.controller.PluginController;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class PortalCreated implements Listener {
    private final PluginController controller;

    public PortalCreated(PluginController controller) {
        this.controller = controller;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void OnPortalCreated(PortalCreateEvent event) {
        if (event.isCancelled()) {
            return;
        }

        World world = event.getBlocks().get(0).getWorld();

        if (controller.getConfig().getWorldSettings().containsKey(world.getName())) {
            event.setCancelled(true);
        }
    }

}
