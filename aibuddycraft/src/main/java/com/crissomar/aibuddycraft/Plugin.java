package com.crissomar.aibuddycraft;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import com.crissomar.aibuddycraft.commands.SummonNPCCommand;
import com.crissomar.aibuddycraft.commands.BuddyEditCommand;

/*
 * aibuddycraft java plugin
 */
public class Plugin extends JavaPlugin
{
  public static final Logger LOGGER=Logger.getLogger("aibuddycraft");
  public static String OPENAI_API_KEY;
  public static String OPENAI_MODEL;
  public static String OPENAI_TEMPERATURE;
  public static String OPENAI_MAX_TOKENS;
  public static String OPENAI_TOP_P;
  public static String OPENAI_FREQUENCY_PENALTY;
  public static String OPENAI_PRESENCE_PENALTY;


  public void onEnable()
  {
    LOGGER.info("aibuddycraft enabled");
    saveDefaultConfig();
    getCommand("summonbuddy").setExecutor(new SummonNPCCommand());
    getCommand("buddy").setExecutor(new BuddyEditCommand());
    getServer().getPluginManager().registerEvents(new com.crissomar.aibuddycraft.listeners.NPCListeners(this), this);

    OPENAI_API_KEY = getConfig().getString("API_KEY");
    if (OPENAI_API_KEY == ""){
      LOGGER.warning("API_KEY not found in config.yml");
    }
    OPENAI_MODEL = getConfig().getString("chatgpt.model");
    OPENAI_TEMPERATURE = getConfig().getString("chatgpt.temperature");
    OPENAI_MAX_TOKENS = getConfig().getString("chatgpt.max-tokens");
    OPENAI_TOP_P = getConfig().getString("chatgpt.top-p");
    OPENAI_FREQUENCY_PENALTY = getConfig().getString("chatgpt.frequency-penalty");
    OPENAI_PRESENCE_PENALTY = getConfig().getString("chatgpt.presence-penalty");

  }

  public void onDisable()
  {
    LOGGER.info("aibuddycraft disabled");
  }
}
