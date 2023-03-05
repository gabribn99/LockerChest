package plugin.events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import plugin.core.Main;

public class ChestLocker implements Listener {

    @EventHandler
    public void lockChest(SignChangeEvent event) {
        Player player = event.getPlayer();
        Block sign = event.getBlock();
        if (sign != null && sign.getState() instanceof Sign) {
            BlockData data = sign.getBlockData();
            if (data instanceof Directional) {
                Directional directional = (Directional) data;
                Block blockBehind = sign.getRelative(directional.getFacing().getOppositeFace());

                if (blockBehind.getType() == Material.CHEST) {
                    Chest chest = (Chest) blockBehind.getState();
                    if (event.getLine(0).equals("Bloquear")) {
                        event.setLine(0, ChatColor.BLUE + "[" + player.getName() + "]");
                        event.setLine(1, ChatColor.RED + "Cofre Bloqueado");

                        String playerName = player.getName();
                        Location chestLocation = chest.getLocation();

                        Main.mapLockedChests.put(chestLocation, playerName);
                    }
                }
            }
        }
    }

    @EventHandler
    public void openChest(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock.getType() == Material.CHEST) {
                String playerName = Main.mapLockedChests.get(clickedBlock.getLocation());
                if (playerName != null) {
                    if (!playerName.equals(player.getName())) {
                        player.sendMessage(ChatColor.RED + "No estás autorizado a abrir este cofre");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void breakChest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getState() instanceof Sign) {
            BlockData data = block.getBlockData();
            if (data instanceof Directional) {
                Directional directional = (Directional) data;
                Block blockBehind = block.getRelative(directional.getFacing().getOppositeFace());
                if(blockBehind.getType() == Material.CHEST) {
                    String playerName = Main.mapLockedChests.get(blockBehind.getLocation());
                    if (playerName != null) {
                        if (!playerName.equals(player.getName())) {
                            player.sendMessage(ChatColor.RED + "No puedes romper este cofre");
                            event.setCancelled(true);
                        } else{
                            Main.mapLockedChests.remove(blockBehind.getLocation());
                        }
                    }
                }
            }
        }

        if (block.getType() == Material.CHEST) {
            String playerName = Main.mapLockedChests.get(block.getLocation());
            if (playerName != null) {
                if (!playerName.equals(player.getName())) {
                    player.sendMessage(ChatColor.RED + "No puedes romper este cofre");
                    event.setCancelled(true);
                } else {
                    Main.mapLockedChests.remove(block.getLocation());
                }
            }
        }
    }
}