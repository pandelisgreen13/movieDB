package gr.pchasapis.moviedb.database.keysDao

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import gr.pchasapis.moviedb.database.theaterDao.TheaterDbTable
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "RemoteKey")
data class RemoteKey(@PrimaryKey val nextKey: Int)

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKey)

    @Query("DELETE FROM RemoteKey")
    fun deleteAll()

    @Query("select * from RemoteKey")
    suspend fun loadAll(): List<RemoteKey>
}