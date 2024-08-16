package us.timinc.mc.cobblemon.shinysparkles.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

object Commands {
    fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registry: CommandRegistryAccess,
        registrationEnvironment: CommandManager.RegistrationEnvironment
    ) {
        FindSparkle.register(dispatcher)
    }
}