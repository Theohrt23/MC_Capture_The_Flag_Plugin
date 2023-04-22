package util;

import entity.Flag;
import entity.Spawn;
import org.bukkit.World;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReadYamlFiles {

    public static List<Flag> getFlagListByWorld(World world, File file) throws FileNotFoundException {
        InputStream input = new FileInputStream(file);
        Yaml yaml = new Yaml();
        Map<String, Map<String, Object>> data = yaml.load(input);
        List<Flag> flagList = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> entry : data.entrySet()) {
            Map<String, Object> values = entry.getValue();
            String yamlWorld = values.get("world").toString();

            if (world.getName().equalsIgnoreCase(yamlWorld)) {
                String name = entry.getKey();

                List<Integer> coordinates = (List<Integer>) values.get("coordinates");
                int x = coordinates.get(0);
                int y = coordinates.get(1);
                int z = coordinates.get(2);
                String color = ((List<String>) values.get("color")).get(0);

                Flag flag = new Flag();

                flag.setName(name);
                flag.setWorld(world);
                flag.setX(x);
                flag.setY(y);
                flag.setZ(z);

                if (color.equalsIgnoreCase(TeamColors.RED_TEAM.getColor())) {
                    flag.setColors(TeamColors.RED_TEAM);
                } else {
                    flag.setColors(TeamColors.BLUE_TEAM);
                }

                flagList.add(flag);
            }
        }
        return flagList;
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
