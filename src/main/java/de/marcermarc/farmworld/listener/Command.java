package de.marcermarc.farmworld.listener;

import de.marcermarc.farmworld.Messages;
import de.marcermarc.farmworld.controller.PluginController;
import de.marcermarc.farmworld.models.WorldSettings;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Command implements CommandExecutor, TabCompleter, Listener {
    /* Possible commands:
    - marcerFarmworld defaultReturnLocation -> get the Location
    - marcerFarmworld defaultReturnLocation x y z (world) -> set the Location
    - marcerFarmworld defaultReturnLocation here -> set the Location
    - marcerFarmworld worlds add name environment type structures size -> create farmworld
    - marcerFarmworld worlds edit (environment/type/structures/size) -> edit farmworld
    - marcerFarmworld worlds remove name -> delete farmworld
    - marcerFarmworld worlds recreate name -> recreate farmworld
    - marcerFarmworld join worldname -> join farmworld yourself (bzw. nearest player in commandblock)
    - marcerFarmworld join worldname playername -> join farmworld player
    - marcerFarmworld return -> return back
    - marcerFarmworld return playername -> return back player
    */

    private static final String DEFAULT_RETURN_LOC = "defaultReturnLocation";
    private static final String WORLDS = "worlds";
    private static final String JOIN = "join";
    private static final String RETURN = "return";

    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String EDIT = "edit";
    private static final String RECREATE = "recreate";
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private static final String HERE = "here";

    private static final String ENVIRONMENT = "environment";
    private static final String TYPE = "type";
    private static final String STRUCTURES = "structures";
    private static final String SIZE = "size";

//    private static final List<String> ARG1_NO_OP = Arrays.asList(CONFIG, CHUNK);
//    private static final List<String> ARG1_OP = Arrays.asList(CONFIG, CHUNK);
//    private static final List<String> ARG1_CONSOLE = Collections.singletonList(CONFIG);
//    private static final List<String> ARG2_NO_OP_CONFIG = Arrays.asList(BASE_BLOCKS, ITEMS, MAX_TIER);
//    private static final List<String> ARG2_OP_CONFIG = Arrays.asList(BASE_BLOCKS, ITEMS, MAX_TIER, DEBUG);
//    private static final List<String> ARG3_CONFIG_ADDREMOVE = Arrays.asList(ADD, REMOVE);
//    private static final List<String> ARG3_CONFIG_TRUEFALSE = Arrays.asList(TRUE, FALSE);

    private PluginController controller;

    public Command(PluginController controller) {
        this.controller = controller;
    }

    //region CommandExecutor
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(Messages.getInfo());
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case DEFAULT_RETURN_LOC:
                    return commandDefaultReturnLocation(commandSender, args);
                case WORLDS:
                    return commandWorlds(commandSender, args);
                case JOIN:
                    return commandJoin(commandSender, args);
                case RETURN:
                    return commandReturn(commandSender, args);
            }
        }

        return false;
    }

    private boolean commandDefaultReturnLocation(CommandSender commandSender, String[] args) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(Messages.getNotAllowed());
            return true;
        } else {
            World world = null;
            if (commandSender instanceof Player) {
                world = ((Player) commandSender).getWorld();
            }

            switch (args.length) {
                case 1:
                    // get default Location
                    commandSender.sendMessage(Messages.getDefaultLocGet(controller.getConfig().getDefaultReturnLocation()));
                    return true;
                case 2:
                    // set here
                    if (args[1].equals(HERE) && commandSender instanceof Player) {
                        Location location = ((Player) commandSender).getLocation();
                        controller.getConfig().setDefaultReturnLocation(location); // ToDo not a farmworld
                        return true;

                    } else {
                        return false;
                    }

                case 5:
                    if (Bukkit.getWorlds().stream().noneMatch(x -> x.getName().equals(args[4]))) {
                        // ToDo send message
                        return false;
                    }
                    world = Bukkit.getWorld(args[4]);

                    //fallthrough
                case 4:
                    if (!NumberUtils.isNumber(args[1]) || !NumberUtils.isNumber(args[2]) || !NumberUtils.isNumber(args[3])) {
                        // ToDo send message
                        return false;
                    } else if (world == null) {
                        // ToDo send another message
                        return false;
                    }

                    Location location = new Location(world, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                    controller.getConfig().setDefaultReturnLocation(location);
                    return true;
            }
        }
        return false;
    }

    // region worlds
    private boolean commandWorlds(CommandSender commandSender, String[] args) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(Messages.getNotAllowed());
            return true;
        } else if (args.length < 3) {
            return false;
        }

        switch (args[1]) {
            case ADD:
                return commandWorldsAdd(commandSender, args);
            case REMOVE:
                return commandWorldsRemove(commandSender, args);
            case RECREATE:
                return commandWorldsRecreate(commandSender, args);
            case EDIT:
                return commandWorldsEdit(commandSender, args);
        }
        return false;
    }

    private boolean commandWorldsAdd(CommandSender commandSender, String[] args) {
        if (args.length != 7) {
            return false;
        }

        // ToDo Test Args for validity

        WorldSettings settings = new WorldSettings();
        settings.setEnvironment(World.Environment.valueOf(args[3].toUpperCase()));
        settings.setType(WorldType.getByName(args[4]));
        settings.setStructures(Boolean.parseBoolean(args[5]));
        settings.setWorldsize(Double.parseDouble(args[6]));

        if (controller.getWorldController().createAndSaveConfig(args[2], settings, commandSender instanceof Player ? (Player) commandSender : null)) {
            commandSender.sendMessage(Messages.getWorldCreationSuccessful());
        }

        return false;
    }

    private boolean commandWorldsRemove(CommandSender commandSender, String[] args) {
        if (!controller.getConfig().getWorldSettings().containsKey(args[2])) {
            commandSender.sendMessage(Messages.getWorldNotExists());
        } else if (controller.getWorldController().deleteAndRemoveFromConfig(args[2])) {
            commandSender.sendMessage(Messages.getWorldRemoveSuccessful());
        } else {
            commandSender.sendMessage(Messages.getWorldRemoveFailed());
        }
        return true;
    }

    private boolean commandWorldsEdit(CommandSender commandSender, String[] args) {
        //TODO
        return false;
    }

    private boolean commandWorldsRecreate(CommandSender commandSender, String[] args) {
        if (!controller.getConfig().getWorldSettings().containsKey(args[2])) {
            commandSender.sendMessage(Messages.getWorldNotExists());
        } else if (controller.getWorldController().recreate(args[2], commandSender instanceof Player ? (Player) commandSender : null)) {
            commandSender.sendMessage(Messages.getWorldRecreationSuccessful());
        } else {
            commandSender.sendMessage(Messages.getWorldRecreationFailed());
        }
        return true;
    }
    //endregion

    //region join / return
    private boolean commandJoin(CommandSender commandSender, String[] args) {
        // ToDo not working in farmworld

        if (args.length < 2 || args.length > 3) {
            return false;
        }

        List<Player> players = testAndGetPlayer(commandSender, args.length == 3 ? args[2] : null);

        if (players == null) {
            return true;
        }

        for (Player player : players) {
            if (!controller.getPlayerController().sendPlayer(player, args[1])) {
                commandSender.sendMessage(Messages.getWorldNotExists());
            }
        }
        return true;
    }

    private boolean commandReturn(CommandSender commandSender, String[] args) {
        // ToDo should only work in farmworlds

        if (args.length > 2) {
            return false;
        }

        List<Player> players = testAndGetPlayer(commandSender, args.length == 2 ? args[1] : null);

        if (players == null) {
            return true;
        }

        for (Player player : players) {
            if (!controller.getPlayerController().returnPlayer(player)) {
                commandSender.sendMessage(Messages.getPlayerNotExistsOrInFarmworld());
            }
        }
        return true;
    }

    private List<Player> testAndGetPlayer(CommandSender commandSender, String playerArg) {
        if (!commandSender.isOp() && commandSender instanceof Player && !controller.getConfig().isEnterViaCommandNonOp()) {
            commandSender.sendMessage(Messages.getNotAllowed());
            return null;
        } else if (commandSender instanceof BlockCommandSender && !controller.getConfig().isEnterViaCommandblock()) {
            Bukkit.getLogger().log(Level.WARNING, Messages.getNotAllowed());
            commandSender.sendMessage(Messages.getNotAllowed());
            return null;
        }

        if (playerArg != null) {
            if (!commandSender.isOp() && !(commandSender instanceof BlockCommandSender)) {
                commandSender.sendMessage(Messages.getNotAllowed());
                return null;
            }

            Player player = Bukkit.getPlayer(playerArg);
            if (player == null) {
                commandSender.sendMessage(Messages.getPlayerNotExists());
                return null;
            }
            return Collections.singletonList(player);
        }

        if (commandSender instanceof Player) {
            return Collections.singletonList((Player) commandSender);
        } else if (commandSender instanceof BlockCommandSender) {
            Location commandblockLocation = ((BlockCommandSender) commandSender).getBlock().getLocation();

            return commandblockLocation.getWorld()
                    .getNearbyEntities(commandblockLocation, 10.0, 10.0, 10.0)
                    .stream()
                    .filter(x -> x instanceof Player)
                    .map(x -> (Player) x)
                    .collect(Collectors.toList());
        }

        commandSender.sendMessage(Messages.getPlayerNotSpecified());
        return null;
    }
    //endregion
    //endregion

    //region TabComplete
    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        switch (args.length) {
//            case 1:
//                return onTabCompleteArg1(sender, args);
//            case 2:
//                return onTabCompleteArg2(sender, args);
//            case 3:
//                return onTabCompleteArg3(sender, args);
//            case 4:
//                return onTabCompleteArg4(sender, args);
            default:
                return null;
        }
    }

  /*  private List<String> onTabCompleteArg1(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return Util.tabCompleteFilter(ARG1_CONSOLE, args[0]);
        } else if (sender.isOp()) {
            return Util.tabCompleteFilter(ARG1_OP, args[0]);
        } else {
            return Util.tabCompleteFilter(ARG1_NO_OP, args[0]);
        }
    }

    private List<String> onTabCompleteArg2(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase(CONFIG)) {
            if (sender.isOp()) {
                return Util.tabCompleteFilter(ARG2_OP_CONFIG, args[1]);
            } else {
                return Util.tabCompleteFilter(ARG2_NO_OP_CONFIG, args[1]);
            }
        }
        return null;
    }

    private List<String> onTabCompleteArg3(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase(CONFIG))
            if (args[1].equalsIgnoreCase(BASE_BLOCKS) || args[1].equalsIgnoreCase(ITEMS)) {
                return Util.tabCompleteFilter(ARG3_CONFIG_ADDREMOVE, args[2]);
            } else if (args[1].equalsIgnoreCase(MAX_TIER)) {
                return Collections.singletonList("[<tier>]");
            } else if (args[1].equalsIgnoreCase(DEBUG)) {
                return Util.tabCompleteFilter(ARG3_CONFIG_TRUEFALSE, args[2]);
            }
        return null;
    }

    private List<String> onTabCompleteArg4(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase(CONFIG) && sender.isOp()) {
            if (args[1].equalsIgnoreCase(BASE_BLOCKS)) {
                if (args[2].equalsIgnoreCase(ADD)) {
                    return Util.tabCompleteFilter(
                            Stream.of(Material.values())
                                    .filter(Material::isBlock)
                                    .filter(material -> !controller.getConfig().getAllowedBaseBlocks().contains(material))
                                    .map(Util::materialToString)
                                    .collect(Collectors.toList()),
                            args[3]);

                } else if (args[2].equals(REMOVE)) {
                    return Util.tabCompleteFilter(
                            Stream.of(Material.values())
                                    .filter(Material::isBlock)
                                    .filter(material -> controller.getConfig().getAllowedBaseBlocks().contains(material))
                                    .map(Util::materialToString)
                                    .collect(Collectors.toList()),
                            args[3]);
                }
            } else if (args[1].equalsIgnoreCase(ITEMS)) {
                if (args[2].equalsIgnoreCase(ADD)) {
                    return Util.tabCompleteFilter(
                            Stream.of(Material.values())
                                    .filter(Material::isItem)
                                    .filter(material -> !controller.getConfig().getAllowedActivationItems().contains(material))
                                    .map(Util::materialToString)
                                    .collect(Collectors.toList()),
                            args[3]);

                } else if (args[2].equals(REMOVE)) {
                    return Util.tabCompleteFilter(
                            Stream.of(Material.values())
                                    .filter(Material::isItem)
                                    .filter(material -> controller.getConfig().getAllowedActivationItems().contains(material))
                                    .map(Util::materialToString)
                                    .collect(Collectors.toList()),
                            args[3]);
                }
            }
        }
        return null;
    }*/


    //endregion
}