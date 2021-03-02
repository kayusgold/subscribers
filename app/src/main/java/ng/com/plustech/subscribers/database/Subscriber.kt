package ng.com.plustech.subscribers.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscribers")
data class Subscriber (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subscriber_id")
    val id: Int,

    var name: String,

    var email: String
    )