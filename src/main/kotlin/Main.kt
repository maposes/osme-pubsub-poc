import com.google.api.core.ApiFuture
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import com.google.pubsub.v1.PubsubMessage
import com.google.pubsub.v1.TopicName
import java.util.concurrent.TimeUnit


// Google Pub/Sub configuration
const val projectId = "osme-pubsub-poc"
const val topicId = "my-topic-data-poc"

fun main() {
    println("Starting Google Pub/Sub POC...")
    publishToTopic(projectId, topicId, "Mensaje enviado desde la app Kotlin osme-pubsub!")
    println("Finishing Google Pub/Sub POC successfully...")
}

fun publishToTopic(projectId: String, topicId: String, message: String) {
    val topicName = TopicName.of(projectId, topicId)

    var publisher: Publisher? = null
    try {
        // Create a publisher instance with default settings bound to the topic
        publisher = Publisher.newBuilder(topicName).build()
        val data: ByteString = ByteString.copyFromUtf8(message)
        val pubsubMessage = PubsubMessage.newBuilder().setData(data).build()

        // Once published, returns a server-assigned message id (unique within the topic)
        val messageIdFuture: ApiFuture<String> = publisher.publish(pubsubMessage)
        val messageId = messageIdFuture.get()
        println("Published message ID: $messageId")
    } catch (e: Exception) {
        println("Error publishing message to topic on Google Pub/Sub POC...")
        throw e
    } finally {
        publisher?.shutdown()
        publisher?.awaitTermination(1, TimeUnit.MINUTES)
    }

}
