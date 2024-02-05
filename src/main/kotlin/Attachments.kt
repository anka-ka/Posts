abstract class Attachment {
    abstract val type: String
}

open class Video(val id: Int,
                 val title: String,
                 val duration: Int,
                 val description: String,
                 var views: Int
)
class VideoAttachment(val video :Video):Attachment(){
    override val type: String
        get() = "video"
}
open class Audio(val id:Int,
                 val title: String,
                 val duration: Int,
                 val url:String,
                 val albumId:Int
    )
class AudioAttachment( val audio:Audio, ):Attachment(){
    override val type: String
        get() = "audio"

}
open class Photo(val id: Int,
                 val text: String,
                 val albumId:Int,
                 val ownerId:Int,
                 var date: Long = System.currentTimeMillis() / 1000L
)
class PhotoAttachment(val photo: Photo):Attachment(){
    override val type: String
        get() = "photo"

}