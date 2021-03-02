package ng.com.plustech.subscribers.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {
    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber) : Long

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber)

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber)

    @Query("DELETE FROM subscribers")
    suspend fun deleteAllSubscribers()

    @Query("SELECT * FROM subscribers")
    fun selectAllSubscribers(): LiveData<List<Subscriber>>
}