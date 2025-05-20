package project.art3mis.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.inventory.meta.MapMeta;

public class MapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("Art3misCmds.map")) {
            player.sendMessage("You don't have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("Usage: /map <map_id>");
            return true;
        }

        int mapId;
        try {
            mapId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid map ID. Must be a number.");
            return true;
        }

        MapView mapView = Bukkit.getMap(mapId);
        if (mapView == null) {
            player.sendMessage("Map with ID " + mapId + " does not exist.");
            return true;
        }

        ItemStack mapItem = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        if (mapMeta != null) {
            mapMeta.setMapView(mapView);
            mapItem.setItemMeta(mapMeta);
        }

        player.getInventory().addItem(mapItem);
        player.sendMessage("You've been given Map ID: " + mapId);
        return true;
    }
}