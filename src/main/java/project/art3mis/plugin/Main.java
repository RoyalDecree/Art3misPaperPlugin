package project.Art3mis.Plugin;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.event.Listener;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends JavaPlugin implements Listener{

    public void onEnable() {
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
