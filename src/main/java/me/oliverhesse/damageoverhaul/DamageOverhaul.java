package me.oliverhesse.damageoverhaul;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class DamageOverhaul extends JavaPlugin {
    private int taskId;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        //create task so i can display stats
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendStatActionBar(player,0d);
            }
        }, 0L, 40L); // 20 ticks = 1 second, so 2 seconds = 40 ticks

        //spawn test entity
        World thisWorld = getServer().getWorlds().get(0);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendStatActionBar(player,0d);
                Location newLocation = player.getLocation().clone();
                newLocation.setX(newLocation.getX()+4);
                LivingEntity newZombie = (LivingEntity) thisWorld.spawnEntity(newLocation,EntityType.ZOMBIE);

                CustomMob customZombie = new CustomMob(this,newZombie,200,100,0.1f,"Custom Zombie");
                customZombie.setNameDisplayed(true);
            }
        }, 0L, 600L);//10seconds


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getScheduler().cancelTask(taskId);
    }
    public static void sendStatActionBar(Player player,Double healthChange){
        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if(maxHealthAttribute == null){

            return;
        }
        Integer displayHealth = (int) Math.ceil(player.getHealth()+healthChange);
        Integer displayMaxHealth = (int) Math.ceil(maxHealthAttribute.getValue());
        player.sendActionBar(Component.text().content(displayHealth+"/"+displayMaxHealth).color(NamedTextColor.RED));
    }

}