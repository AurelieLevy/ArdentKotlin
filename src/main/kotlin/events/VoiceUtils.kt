package events

import commands.music.getGuildAudioPlayer
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.core.hooks.SubscribeEvent
import utils.send

class VoiceUtils {
    @SubscribeEvent
    fun onVoiceLeave(e: GuildVoiceLeaveEvent) {
        val member = e.guild.selfMember
        if (member.voiceState.channel == e.channelLeft && e.channelLeft.members.size == 1) {
            val manager = e.guild.getGuildAudioPlayer(null)
            val player = manager.player
            if (player.playingTrack != null && !player.isPaused) {
                player.isPaused = true
                manager.scheduler.manager.getChannel()?.send(e.guild.selfMember, "Automatically paused because no one else was in the channel")
            }
        }
    }
}