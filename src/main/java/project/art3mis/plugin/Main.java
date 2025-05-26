package project.Art3mis.Plugin;

import org.bukkit.plugin.java.JavaPlugin;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource; // For 1.20+ use appropriate Paper/Spigot wrapper
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import org.bukkit.event.Listener;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends JavaPlugin implements Listener{

    @override
    public void onEnable() {
        registerBrigadierCommand();
    }
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("This Application must be run as PaperMC plugin!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void registerBrigadierCommand() {
        // Paper provides a helper to get the dispatcher
        PluginCommand mapCommand = getCommand("map");
        // The actual registration depends on the Paper version
        // For modern Paper:
        getServer().getCommandMap().getDispatcher().register("map",
                LiteralArgumentBuilder.<CommandSourceStack>literal("map")
                        .then(RequiredArgumentBuilder.argument("map_id", IntegerArgumentType.integer())
                                .executes(context -> {
                                    return giveMap(context);
                                })
                        )
        );
    }

    private int giveMap(CommandContext<CommandSourceStack> context) {
        int mapId = IntegerArgumentType.getInteger(context, "map_id");
        Player player = (Player) context.getSource().getBukkitSender();

        if (!player.hasPermission("Art3misCmds.map")) {
            player.sendMessage("You don't have permission to use this command.");
            return 0;
        }

        MapView mapView = Bukkit.getMap(mapId);
        if (mapView == null) {
            player.sendMessage("Map with ID " + mapId + " does not exist.");
            return 0;
        }

        ItemStack mapItem = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        if (mapMeta != null) {
            mapMeta.setMapView(mapView);
            mapItem.setItemMeta(mapMeta);
        }

        player.getInventory().addItem(mapItem);
        player.sendMessage("You've been given Map ID: " + mapId);
        return 1; // Success
    }
}
