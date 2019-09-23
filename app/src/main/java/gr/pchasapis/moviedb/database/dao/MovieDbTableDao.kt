package gr.pchasapis.moviedb.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gr.pchasapis.moviedb.database.dao.MovieDbTable

@Dao
interface MovieDbTableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertModel(movieDbTable: MovieDbTable)

    @Query("DELETE FROM MovieDbTable WHERE id = :id ")
    fun deleteModel(id: Int)

    @Query("DELETE FROM MovieDbTable")
    fun deleteAll()

    @Query("select * from MovieDbTable order by dateAdded DESC")
    fun loadAll(): List<MovieDbTable>

    @Query("SELECT isFavourite FROM MovieDbTable WHERE id =:id")
    fun isFavourite(id: Int): Boolean
}