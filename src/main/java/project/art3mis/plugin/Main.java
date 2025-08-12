package project.Art3mis.Plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends JavaPlugin implements Listener{

    public static final Pattern patternBrackets = Pattern.compile("\\{#[0-9a-fA-F]{6}\\}");
    public static final Pattern pattern = Pattern.compile("#[0-9a-fA-F]{6}");

    private final Map<UUID, GivePetTransferAttempt> transferAttempts = new HashMap<>();

    public Map<UUID, GivePetTransferAttempt> transferAttempts() {
        return transferAttempts;
    }

    public boolean cancelTransfer(UUID uuid) {
        return transferAttempts.remove(uuid) != null;
    }

    public static String colorMsg(String s) {
        s = removeBrackets(s);
        Matcher match = pattern.matcher(s);
        while (match.find()) {
            String color = s.substring(match.start(), match.end());
            s = s.replace(color, "" + net.md_5.bungee.api.ChatColor.of(color));
            match = pattern.matcher(s);
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', s);
    }

    private static String removeBrackets(String text) {
        Matcher m = patternBrackets.matcher(text);
        String replaced = text;
        while (m.find()) {
            String hexcode = m.group();
            String fixed = hexcode.substring(2, 8);
            replaced = replaced.replace(hexcode, "#" + fixed);
        }
        return replaced;
    }

    public String getMsg(String key) {
        return colorMsg(getConfig().getString(key));
    }

    @Override
    public void onEnable() {
        FileConfiguration cfg = getConfig();
        cfg.options().copyDefaults(true);
        cfg.addDefault("rightClickPrompt", "&ePlease right click the pet you would like to give");
        cfg.addDefault("playerNotFound", "&cThat player could not be found!");
        cfg.addDefault("cancelFail", "&cYou haven't tried to transfer a pet!");
        cfg.addDefault("cancelSuccess", "&aCancelled transferring a pet!");
        cfg.addDefault("playerLeft", "&cThe player you were trying to give that pet to has since left the server.");
        cfg.addDefault("sentReceiverMsg", "{sender}&a gave you a pet &f{type}&a!");
        cfg.addDefault("sentSenderMsg", "&aYou gave your pet successfully!");
        cfg.addDefault("notOwned", "&cThat's not your pet!");
        cfg.addDefault("selfGive", "&cYou can't give a pet to yourself!");
        saveConfig();

        GivePetCommand cmd = new GivePetCommand(this);

        getCommand("givepet").setExecutor(cmd);
        getCommand("givepet").setTabCompleter(cmd);

        getServer().getPluginManager().registerEvents(new GivePetInteractListener(this), this);

        getCommand("map").setExecutor(new MapCommand());
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
}
