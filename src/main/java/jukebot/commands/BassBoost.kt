package jukebot.commands

import jukebot.framework.Command
import jukebot.framework.CommandCategory
import jukebot.framework.CommandProperties
import jukebot.framework.Context
import jukebot.utils.Helpers

@CommandProperties(aliases = ["bb", "bass"], description = "Bass boosts the audio", category = CommandCategory.CONTROLS)
class BassBoost : Command(ExecutionType.REQUIRE_MUTUAL) {

    override fun execute(context: Context) {
        val handler = context.getAudioPlayer()

        if (!handler.isPlaying) {
            return context.embed("Not Playing", "Nothing is currently playing.")
        }

        if (!context.isDJ(true)) {
            return context.embed("Not a DJ", "You need to be a DJ to use this command.\n[See here on how to become a DJ](https://jukebot.serux.pro/faq)")
        }

        val boost = context.args.firstOrNull()?.toFloatOrNull()
                ?: return context.embed("Bass Boost", "${createBar(handler.bassBooster.percentage)} `${handler.bassBooster.percentage.toInt()}`")

        if (boost < 0 || boost > 200) {
            return context.embed("Bass Boost", "You need to specify a valid number from 0-200.")
        }

        handler.bassBooster.boost(boost)
        context.embed("Bass Boost", "${createBar(boost)} `${boost.toInt()}`")
    }

    private fun createBar(v: Float) = Helpers.createBar(v.toInt(), 200, 10)

}
