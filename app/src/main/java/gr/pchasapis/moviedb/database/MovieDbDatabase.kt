package gr.pchasapis.moviedb.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gr.pchasapis.moviedb.database.dao.MovieDbTable
import gr.pchasapis.moviedb.database.dao.MovieDbTableDao
import gr.pchasapis.moviedb.database.keysDao.RemoteKey
import gr.pchasapis.moviedb.database.keysDao.RemoteKeyDao
import gr.pchasapis.moviedb.database.theaterDao.TheaterDbTable
import gr.pchasapis.moviedb.database.theaterDao.TheaterTableDao

@Database(
    entities = [
        MovieDbTable::class,
        TheaterDbTable::class,
        RemoteKey::class
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
abstract class MovieDbDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "MovieDB"

        fun get(context: Context): MovieDbDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MovieDbDatabase::class.java,
                DATABASE_NAME
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun movieDbTableDao(): MovieDbTableDao
    abstract fun theaterDbTableDao(): TheaterTableDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}