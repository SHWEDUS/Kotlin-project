package dev.krylov.news.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.krylov.news.database.dao.ArticleDao
import dev.krylov.news.database.models.ArticleDB
import dev.krylov.news.database.utils.Converters

class NewsDatabase internal constructor(private val database: NewsRoomDatabase) {

    val articleDao: ArticleDao
        get() = database.articleDao()
}

@Database(entities = [ArticleDB::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class NewsRoomDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
}

fun NewsDatabase(applicationContext: Context): NewsDatabase {
    val newsRoomDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        NewsRoomDatabase::class.java,
        "news"
    ).build()
    return NewsDatabase(newsRoomDatabase)
}