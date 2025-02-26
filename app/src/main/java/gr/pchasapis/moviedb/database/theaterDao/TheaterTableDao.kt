package gr.pchasapis.moviedb.database.theaterDao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TheaterTableDao {

    @Upsert
    suspend fun upsertAll(theaterList: List<TheaterDbTable>)

    @Query("DELETE FROM TheaterDbTable")
    fun deleteAll()

    @Query("select * from TheaterDbTable order by dateAdded DESC")
    fun loadAll(): Flow<List<TheaterDbTable>>

    @Query("SELECT * FROM TheaterDbTable")
    fun pagingSource(): PagingSource<Int, TheaterDbTable>

}