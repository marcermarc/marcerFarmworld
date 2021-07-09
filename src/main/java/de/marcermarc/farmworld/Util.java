package de.marcermarc.farmworld;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
    private Util() {
    }

    public static List<String> tabCompleteFilter(List<String> full, String startetText) {
        return full.stream().filter(s -> startetText.isEmpty() || s.contains(startetText)).collect(Collectors.toList());
    }

    public static void deleteRecursively(final Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static Location getFarmWorldSpawnLocation(World world) {
        return new Location(world, 0, Util.getSpawnY(world, 0, 0) + 1, 0);
    }

    public static int getSpawnY(World world, int x, int z) {
        if (world.getEnvironment() == World.Environment.NETHER) {
            for (int y = 30; y < 128; y++) {
                Location location = new Location(world, x, y, z);
                if (testForAir(location)) {
                    return y;
                }
            }
        }

        return world.getHighestBlockYAt(x, z) + 1;
    }

    public static boolean testForAir(Location location) {
        if (location == null) {
            return false;
        }

        return location.getBlock().getType().isAir()
                && location.add(0, 1, 0).getBlock().getType().isAir()
                && location.add(0, 2, 0).getBlock().getType().isAir();
    }
}
