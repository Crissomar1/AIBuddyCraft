package com.crissomar.aibuddycraft;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * aibuddycraft java plugin
 */
public class Plugin extends JavaPlugin
{
  private static final Logger LOGGER=Logger.getLogger("aibuddycraft");

  public void onEnable()
  {
    LOGGER.info("aibuddycraft enabled");
  }

  public void onDisable()
  {
    LOGGER.info("aibuddycraft disabled");
  }
}
