package de.marcermarc.farmworld;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Messages {
    private static final String MESSAGE_PREFIX = ChatColor.DARK_GREEN + "[marcerFarmworlds]" + ChatColor.RESET + " ";
    private static final String INFO = MESSAGE_PREFIX + "marcerFarmworld is a plugin that allows you to generate farmworlds.";
    private static final String WORLD_SETTINGS_CHANGED = "World '%s' has changed settings. Regenerarte it to apply these settings.";
    private static final String WORLD_CREATION_FAILED_CONSOLE = "World '%s' could not be generated.";
    private static final String WORLD_CREATION_FAILED_PLAYER =  MESSAGE_PREFIX + ChatColor.RED + "World '%s' could not be generated.";
    private static final String WORLD_DELETE = MESSAGE_PREFIX + "World will be deleted, teleporting you back.";
    private static final String WORLD_RETURN_NOT_AIR = MESSAGE_PREFIX + "World '%s' has an invalid return point (not air). Used default returnpoint.";
    private static final String DEFAULT_RETURN_NOT_AIR = MESSAGE_PREFIX + "Default returnpoint is invalid (not air). Teleport to top y of this point.";
    private static final String WORLD_DELETE_FAILED = "Delete of World %s failed.";
    private static final String NOT_ALLOWED = MESSAGE_PREFIX + ChatColor.RED + "You're not allowed to do this.";
    private static final String DEFAULT_LOC_GET = MESSAGE_PREFIX + "Default return location is x:%d/y:%d/z:%d in world %s.";
    private static final String WORLD_NOT_EXISTS = MESSAGE_PREFIX + ChatColor.RED + "World not exists or is not a farmworld.";
    private static final String PLAYER_NOT_EXISTS = MESSAGE_PREFIX + ChatColor.RED + "Specified player not found.";
    private static final String PLAYER_NOT_EXISTS_OR_IN_FARMWORLD = MESSAGE_PREFIX + ChatColor.RED + "Player not in a farmworld.";
    private static final String PLAYER_NOT_SPECIFIED = MESSAGE_PREFIX + ChatColor.RED + "No player specified.";
    private static final String WORLD_REMOVE_FAILED = MESSAGE_PREFIX + ChatColor.RED + "Remove failed.";
    private static final String WORLD_REMOVE_SUCCESSFUL = MESSAGE_PREFIX + ChatColor.RED + "Remove completed.";
    private static final String WORLD_RECREATION_FAILED = MESSAGE_PREFIX + ChatColor.RED + "Recreation failed.";
    private static final String WORLD_RECREATION_SUCCESSFUL = MESSAGE_PREFIX + ChatColor.RED + "Recreation completed.";
    private static final String WORLD_CREATION_SUCCESSFUL = MESSAGE_PREFIX + ChatColor.RED + "Creation completed.";

    private Messages() {
    }

    public static String getInfo() {
        return INFO;
    }

    public static String getWorldSettingsChanged(String worldName) {
        return String.format(WORLD_SETTINGS_CHANGED, worldName);
    }

    public static String getWorldCreationFailedConsole(String worldName) {
        return String.format(WORLD_CREATION_FAILED_CONSOLE, worldName);
    }

    public static String getWorldCreationFailedPlayer(String worldName) {
        return String.format(WORLD_CREATION_FAILED_PLAYER, worldName);
    }

    public static String getWorldDelete() {
        return WORLD_DELETE;
    }

    public static String getWorldReturnNotAir(String worldName) {
        return String.format(WORLD_RETURN_NOT_AIR, worldName);
    }

    public static String getDefaultReturnNotAir() {
        return DEFAULT_RETURN_NOT_AIR;
    }

    public static String getWorldDeleteFailed(String worldName) {
        return String.format(WORLD_DELETE_FAILED, worldName);
    }

    public static String getNotAllowed() {
        return NOT_ALLOWED;
    }

    public static String getDefaultLocGet(Location location) {
        return String.format(DEFAULT_LOC_GET, location.getX(), location.getY(), location.getZ(), location.getWorld());
    }

    public static String getPlayerNotExists() {
        return PLAYER_NOT_EXISTS;
    }

    public static String getPlayerNotSpecified() {
        return PLAYER_NOT_SPECIFIED;
    }

    public static String getWorldNotExists() {
        return WORLD_NOT_EXISTS;
    }

    public static String getPlayerNotExistsOrInFarmworld() {
        return PLAYER_NOT_EXISTS_OR_IN_FARMWORLD;
    }

    public static String getWorldRemoveFailed() {
        return WORLD_REMOVE_FAILED;
    }

    public static String getWorldRemoveSuccessful() {
        return WORLD_REMOVE_SUCCESSFUL;
    }

    public static String getWorldRecreationFailed() {
        return WORLD_RECREATION_FAILED;
    }

    public static String getWorldRecreationSuccessful() {
        return WORLD_RECREATION_SUCCESSFUL;
    }

    public static String getWorldCreationSuccessful() {
        return WORLD_CREATION_SUCCESSFUL;
    }
}
