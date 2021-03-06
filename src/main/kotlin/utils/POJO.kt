package utils

import main.conn
import main.r
import net.dv8tion.jda.core.entities.User
import obj.SpotifyPublicUser
import org.apache.commons.lang3.text.WordUtils
import translation.ArdentLanguage
import translation.ArdentPhraseTranslation
import translation.Languages

data class TriviaCategory(val title: String, val created_at: String, val updated_at: String, val clues_count: Int) {
    fun getCategoryName(): String {
        return title.split(" ").map { WordUtils.capitalize(it) }.concat()
    }
}

data class SimpleLoggedEvent(val guildId: String, val eventType: EventType, val time: Long = System.currentTimeMillis(), val id: String = r.uuid().run(conn))

enum class EventType { LEFT_GUILD, JOINED_GUILD }

data class PlayedMusic(val guildId: String, val position: Double)

data class TriviaQuestion(val question: String, val answers: List<String>, val category: String, val value: Int)

data class Marriage(var userOne: String, var userTwo: String, val id: String = r.uuid().run(conn))
data class Patron(var id: String, var donationLevel: DonationLevel)

data class Reminder(val userId: String, val creation: Long, val end: Long, val content: String, val repeat: Boolean = false, val id: String = r.uuid().run(conn))
data class SpecialPerson(var id: String, var backer: String)
data class Announcement(val date: String, val dateLong: Long, val writer: User, val content: String)
data class AnnouncementModel(var date: Long, var writer: String, var content: String) {
    fun toAnnouncement(): Announcement {
        return Announcement(date.readableDate(), date, writer.toUser()!!, content)
    }
}

data class QueueModel(val guildId: String, val voiceId: String, val channelId: String?, val music: MutableList<String /* URI */>)
data class ProofreadPhrase(val original: ArdentPhraseTranslation, val phrase: String, val hasChecker: Boolean, val suggestions: MutableList<String> = mutableListOf(), var suggestionString: String = "", var hasSuggestions: Boolean = true)
data class GuildData(val id: String, var prefix: String?, var musicSettings: MusicSettings, var advancedPermissions: MutableList<String>, var iamList: MutableList<Iam> = mutableListOf(),
                     var joinMessage: Pair<String?, String? /* Message then Channel ID */>? = null, var leaveMessage: Pair<String?, String?>? = null,
                     var defaultRole: String? = null, var allowGlobalOverride: Boolean = false, var language: ArdentLanguage?, var blacklistedUsers: MutableList<String>?,
                     var blacklistedRoles: MutableList<String>?, var blacklistedChannels: MutableList<String>?)
data class Iam(var name: String, var roleId: String)
data class MusicSettings(var announceNewMusic: Boolean = false, var singleSongInQueueForMembers: Boolean = false, var membersCanMoveBot: Boolean = true,
                         var membersCanSkipSongs: Boolean = false, var autoQueueSongs: Boolean = false, var stayInChannel: Boolean? = false)

data class UDSearch(val tags: List<String>, val result_type: String, val list: List<UDResult>, val sounds: List<String>)

data class UDResult(val definition: String, val permalink: String, val thumbs_up: Int, val author: String, val word: String,
                    val defid: String, val current_vote: String, val example: String, val thumbs_down: Int)

data class EightBallResult(val magic: Magic)
data class Magic /* The name was not my choice...... */(val question: String, val answer: String, val type: String)

class Punishment(val userId: String, val punisherId: String, val guildId: String, val type: Type, val expiration: Long, val start: Long = System.currentTimeMillis(), val id: String = r.uuid().run(conn)) {
    enum class Type {
        TEMPBAN, MUTE;
        override fun toString(): String {
            return if (this == TEMPBAN) "temp-banned" else "muted"
        }
    }
}

data class UserProfile(val id: String, val points: Int = 0, val likes: UserLikes, val gender: Int = 0, val accounts: ConnectedAccounts, val userPlaylists: List<UserPlaylist>) {
    fun getLevel(): Int {
        var level = 1
        var tempPoints = points
        while (tempPoints > 0) {
            if (tempPoints - (level * 1000) >= 0) {
                level++
                tempPoints -= level * 1000
            }
        }
        return level
    }
}
data class UserLikes(val food: Emoji? = null, val sport: Emoji? = null, val language: ArdentLanguage = Languages.ENGLISH.language)
data class ConnectedAccounts(val spotify: SpotifyPublicUser? = null)
data class UserPlaylist(val tracks: List<String /* URL */>)