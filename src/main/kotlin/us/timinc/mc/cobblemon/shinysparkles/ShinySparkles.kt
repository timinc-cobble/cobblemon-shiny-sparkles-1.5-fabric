package us.timinc.mc.cobblemon.shinysparkles

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.storage.player.PlayerDataExtensionRegistry
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.util.Identifier
import us.timinc.mc.cobblemon.shinysparkles.blocks.ShinySparklesBlocks
import us.timinc.mc.cobblemon.shinysparkles.blocks.blockentities.BlockEntities
import us.timinc.mc.cobblemon.shinysparkles.commands.Commands
import us.timinc.mc.cobblemon.shinysparkles.config.ConfigBuilder
import us.timinc.mc.cobblemon.shinysparkles.config.ShinySparklesConfig
import us.timinc.mc.cobblemon.shinysparkles.spawning.ShinySparkleSoulStealer
import us.timinc.mc.cobblemon.shinysparkles.store.player.SparklesData

object ShinySparkles : ModInitializer {
    val MOD_ID = "shinysparkles"

    val config = ConfigBuilder.load(ShinySparklesConfig::class.java, MOD_ID)

    override fun onInitialize() {
        PlayerDataExtensionRegistry.register(SparklesData.NAME, SparklesData::class.java)
        BlockEntities.register()
        ShinySparklesBlocks.register()
        CommandRegistrationCallback.EVENT.register(Commands::register)
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.LOWEST) { ShinySparkleSoulStealer.possiblyStealSoul(it) }
    }

    fun modResource(name: String): Identifier {
        return Identifier(MOD_ID, name)
    }

    fun debug(msg: String) {
        if (!config.debug) return
        println(msg)
    }
}