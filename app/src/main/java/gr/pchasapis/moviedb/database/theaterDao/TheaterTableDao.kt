package gr.pchasapis.moviedb.database.theaterDao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TheaterTableDao {

    @Upsert
    suspend fun upsertAll(theaterList: List<TheaterDbTable>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(theaterList: List<TheaterDbTable>)

    @Query("DELETE FROM TheaterDbTable")
    fun deleteAll()

    @Query("SELECT * FROM TheaterDbTable ORDER BY dateAdded ASC")
    fun pagingSource(): PagingSource<Int, TheaterDbTable>

}