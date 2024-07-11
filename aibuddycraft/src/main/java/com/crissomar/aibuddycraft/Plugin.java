package com.crissomar.aibuddycraft;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import com.crissomar.aibuddycraft.commands.SummonNPCCommand;
import com.crissomar.aibuddycraft.commands.BuddyEditCommand;
import com.crissomar.aibuddycraft.utils.OpenAIChatCompletioner;

/*
 * aibuddycraft java plugin
 */
public class Plugin extends JavaPlugin
{
  public static final Logger LOGGER=Logger.getLogger("aibuddycraft");
  public static String OPENAI_API_KEY;
  public static String OPENAI_MODEL;
  public static String OPENAI_MAX_TOKENS;
  public static OpenAIChatCompletioner openAIChatCompletioner;


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
    OPENAI_MAX_TOKENS = getConfig().getString("chatgpt.max-tokens");

    openAIChatCompletioner = new OpenAIChatCompletioner(
      OPENAI_API_KEY, 
      OPENAI_MODEL, 
      OPENAI_MAX_TOKENS);

  }

  public void onDisable()
  {
    LOGGER.info("aibuddycraft disabled");
  }
}
