package us.timinc.mc.cobblemon.shinysparkles.blocks

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import us.timinc.mc.cobblemon.shinysparkles.ShinySparkles.modResource

object ShinySparklesBlocks {
    val SHINY_SPARKLE = ShinySparkle()

    fun register() {
        registerBlock("shiny_sparkle", SHINY_SPARKLE)
    }

    private fun registerBlock(name: String, block: Block) {
        Registry.register(Registries.BLOCK, modResource(name), block)
        Registry.register(
            Registries.ITEM, modResource(name), BlockItem(block, Item.Settings())
        )
    }
}