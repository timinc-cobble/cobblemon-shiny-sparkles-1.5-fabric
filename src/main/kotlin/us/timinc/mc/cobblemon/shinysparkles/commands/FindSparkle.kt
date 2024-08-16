package us.timinc.mc.cobblemon.shinysparkles.commands

import com.mojang.brigadier.context.CommandContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import us.timinc.mc.cobblemon.shinysparkles.ShinySparkles
import us.timinc.mc.cobblemon.shinysparkles.store.player.SparklesData

object FindSparkle : PlayerCommandExecutor(listOf("findsparkle")) {
    override fun check(ctx: CommandContext<ServerCommandSource>, player: PlayerEntity): Int {
        if (!ShinySparkles.config.enableFindCommand) {
            player.sendMessage(Text.translatable("shinysparkles.msg.commanddisabled"), true)
            return 0
        }

        val prevSparklesData = SparklesData.getFromPlayer(player)
        val pos = prevSparklesData.pos ?: run {
            player.sendMessage(Text.translatable("shinysparkles.msg.nosparkleforyou"), true)
            return 0
        }
        player.sendMessage(Text.literal("${pos.x}, ${pos.y}, ${pos.z}"), true)
        return 1
    }
}