package plugin.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.adapters.ChestLockerAdapter;
import plugin.entities.ChestLockerBean;
import plugin.events.ChestLocker;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Main extends JavaPlugin {

    public final static String PATH = "lockerChest/";
    public final static String FILENAME = "chestLockers.json";
    public static Map<Location, String> mapLockedChests = new HashMap<>();

    @Override
    public void onEnable() {
        loadChestLocks();
        setCommands();
        setEvents();
        Bukkit.getConsoleSender().sendMessage("Locker Chest está activado");
    }

    @Override
    public void onDisable() {
        saveChestLocks();
        Bukkit.getConsoleSender().sendMessage("Locker Chest está desactivado");
    }

    private void setEvents() {

        getServer().getPluginManager().registerEvents(new ChestLocker(), this);
    }

    private void setCommands() {

    }

    private void saveChestLocks() {
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChestLockerBean.class, new ChestLockerAdapter());
        Gson gson = builder.create();
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(PATH + FILENAME));
            mapLockedChests.forEach((chestlocker, playerId) -> {
                ChestLockerBean chestLocker = new ChestLockerBean(playerId, chestlocker.getWorld().getName(), chestlocker.getX(), chestlocker.getY(), chestlocker.getZ());
                pw.println(gson.toJson(chestLocker));
            });
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadChestLocks() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChestLockerBean.class, new ChestLockerAdapter());
        Gson gson = builder.create();
        List<ChestLockerBean> chestLockerBeanList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(PATH + FILENAME));
            String jsonString;
            while ((jsonString = br.readLine()) != null) {
                chestLockerBeanList.add(gson.fromJson(jsonString, ChestLockerBean.class));
            }
            chestLockerBeanList.forEach(chestLocker -> {
                World world = Bukkit.getWorld(chestLocker.getWorldName());
                Location location = new Location(world, chestLocker.getX(), chestLocker.getY(), chestLocker.getZ());
                String playerName = chestLocker.getPlayerName();
                mapLockedChests.put(location, playerName);
            });
        } catch (IOException e) {
            System.out.println("Aun no existe el fichero \"" + FILENAME + "\"");
        }
    }
}
