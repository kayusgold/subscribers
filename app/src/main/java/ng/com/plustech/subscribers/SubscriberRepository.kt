package ng.com.plustech.subscribers

import ng.com.plustech.subscribers.database.Subscriber
import ng.com.plustech.subscribers.database.SubscriberDAO

class SubscriberRepository(private val dao: SubscriberDAO) {
    //no need of calling this dao function in background thread since room does that by default
    val subscribers = dao.selectAllSubscribers()

    suspend fun insert(subscriber: Subscriber): Long {
        return dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber) {
        dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber) {
        dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll() {
        dao.deleteAllSubscribers()
    }
}