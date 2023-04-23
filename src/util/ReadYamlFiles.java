package util;

import entity.Flag;
import entity.Spawn;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReadYamlFiles {

    public static List<Flag> getFlagListByWorld(World world, File file) throws FileNotFoundException {
        List<Flag> flags = new ArrayList<>();
        YamlConfiguration flagConfig = YamlConfiguration.loadConfiguration(file);

        for (String flagKey : flagConfig.getKeys(false)) {
            String[] keyParts = flagKey.split("\\.");
            String flagName = keyParts[0];
            String flagWorld = flagConfig.getString(flagKey + ".world");
            if (flagWorld != null && flagWorld.equals(world.getName())) {
                List<Integer> coordinates = flagConfig.getIntegerList(flagKey + ".coordinates");
                String flagColor = flagConfig.getString(flagKey + ".color");
                TeamColors teamColors;
                if (flagColor.equals("[Red]")){
                    teamColors = TeamColors.RED_TEAM;
                }else {
                    teamColors = TeamColors.BLUE_TEAM;
                }

                if (coordinates.size() == 3 && flagColor != null) {
                    Flag flag = new Flag(flagName, teamColors, world, coordinates.get(0), coordinates.get(1), coordinates.get(2));
                    flags.add(flag);
                }
            }
        }

        return flags;
    }

    public static Spawn getSpawnByWorld(World world, File file) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        FileInputStream input = new FileInputStream(file);
        Map<String, Object> spawnData = yaml.load(input);

        Map<String, Object> worldData = (Map<String, Object>) spawnData.get(world.getName());
        Map<String, Object> redData = (Map<String, Object>) worldData.get("red");
        Map<String, Object> blueData = (Map<String, Object>) worldData.get("blue");

        List<Integer> coordinatesRed = (List<Integer>) redData.get("coordinates");
        List<Integer> coordinatesBlue = (List<Integer>) blueData.get("coordinates");

        int xRed = coordinatesRed.get(0);
        int yRed = coordinatesRed.get(1);
        int zRed = coordinatesRed.get(2);

        int xBlue = coordinatesBlue.get(0);
        int yBlue = coordinatesBlue.get(1);
        int zBlue = coordinatesBlue.get(2);

        return new Spawn(xBlue, yBlue, zBlue, xRed, yRed, zRed, world);
    }
}
