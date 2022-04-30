###  movieDB
We use Retrofit to consume the MovieDb Api. We choose the MVVM Architechure with ViewModel, LiveData/Flow, Coroutines and Room Database.
For Dependency Injection we are using [Hilt](https://developer.android.com/training/dependency-injection/hilt-android).

The navigation is handle by [navigation component](https://developer.android.com/guide/navigation/navigation-getting-started).

Migration to [Jetpack compose](https://developer.android.com/jetpack/compose) is in progress, check FavouriteFragment which is fully migrated. 

We have the Home activity where the user can find any movie or tv-series by typing at the search editext.
The user can add a movie/tv-series as a favourite to his/her Watchlist. The favourite are stored locally at the database.

If you like to run this project, you have to get an api key from https://developers.themoviedb.org/3/getting-started/introduction
and replace at Definitions class the variable API_KEY with your api key.

<br><b>Home Activity</b><br>
![alt tag](https://user-images.githubusercontent.com/21217572/65449578-f076bf80-de43-11e9-8ef7-e2c0fe9f99d8.png)

<br><b>Watchlist</b><br>
![alt tag](https://user-images.githubusercontent.com/21217572/65449579-f076bf80-de43-11e9-8195-e5205206d28f.png)

<br><b>Details</b><br>
![alt tag](https://user-images.githubusercontent.com/21217572/65449577-f076bf80-de43-11e9-99f0-288cd33e380c.png)
