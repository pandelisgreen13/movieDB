package gr.pchasapis.moviedb.database.theaterDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gr.pchasapis.moviedb.database.dao.MovieDbTable
import kotlinx.coroutines.flow.Flow

@Dao
interface TheaterTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModel(movieDbTable: TheaterDbTable)

    @Query("DELETE FROM TheaterDbTable")
    fun deleteAll()

    @Query("select * from TheaterDbTable order by dateAdded DESC")
    fun loadAll(): Flow<List<TheaterDbTable>>

}