package de.maltesermailo.connector;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
			if(Bukkit.getOnlinePlayers().size() == 0) {
				Bukkit.shutdown();
			}
		}, 30*20L, 30*20L);
	}
	
}
