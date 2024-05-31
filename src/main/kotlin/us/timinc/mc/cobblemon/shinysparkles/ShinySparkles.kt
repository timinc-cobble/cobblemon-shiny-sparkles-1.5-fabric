package us.timinc.mc.cobblemon.shinysparkles

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import us.timinc.mc.cobblemon.shinysparkles.blocks.ShinySparklesBlocks
import us.timinc.mc.cobblemon.shinysparkles.blocks.blockentities.BlockEntities
import us.timinc.mc.cobblemon.shinysparkles.config.ConfigBuilder
import us.timinc.mc.cobblemon.shinysparkles.config.ShinySparklesConfig
import us.timinc.mc.cobblemon.shinysparkles.spawning.ShinySparkleSoulStealer

object ShinySparkles : ModInitializer {
    val MOD_ID = "shinysparkles"

    val config = ConfigBuilder.load(ShinySparklesConfig::class.java, MOD_ID)

    override fun onInitialize() {
        BlockEntities.register()
        ShinySparklesBlocks.register()
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.LOWEST) { ShinySparkleSoulStealer.possiblyStealSoul(it) }
    }

    fun modResource(name: String): Identifier {
        return Identifier(MOD_ID, name)
    }
}