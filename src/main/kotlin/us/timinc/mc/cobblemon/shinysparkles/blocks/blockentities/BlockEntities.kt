package us.timinc.mc.cobblemon.shinysparkles.blocks.blockentities

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import us.timinc.mc.cobblemon.shinysparkles.ShinySparkles.modResource
import us.timinc.mc.cobblemon.shinysparkles.blocks.ShinySparklesBlocks


object BlockEntities {
    lateinit var SHINY_SPARKLE_BE: BlockEntityType<ShinySparkleBlockEntity>

    fun register() {
        SHINY_SPARKLE_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
        modResource("sparkle_be"),
        FabricBlockEntityTypeBuilder.create(::ShinySparkleBlockEntity, ShinySparklesBlocks.SHINY_SPARKLE).build()
        )
    }
}