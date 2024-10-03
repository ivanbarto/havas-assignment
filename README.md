# reddit

First of all, I want to thank you for this great opportunity! It has been a fun challenge to develop this product.
I hope you like the end result!

Instructions to compile the project:

1- Clone it

2- Open it with Android Studio and compile it.


## About the app

This app allows us to see the reddit posts based on a master-detail pattern. The posts page was built using pagination, so we can scroll infinitely to load hundreds of posts. We can pull-to-refresh to reload the posts page.

![Peek 2024-09-25 23-20](https://github.com/user-attachments/assets/88eb9a2a-4662-4b98-a5b8-32f29cb18634)
![Peek 2024-09-25 23-20 1](https://github.com/user-attachments/assets/f4fcdf97-fdd5-41eb-885a-f25d83e04194)

We also have offline mode support and error handling. We can check previously loaded posts if we lack on internet connection.

![Peek 2024-09-25 23-27 2](https://github.com/user-attachments/assets/f3f5c7bc-822a-4c87-938e-353a7892a739)
![Peek 2024-09-25 23-28 4](https://github.com/user-attachments/assets/8f702de5-6d15-4741-905e-44d66c7862b7)


This app also has support for dark and light theme!

![Peek 2024-09-25 23-30 5](https://github.com/user-attachments/assets/74272a03-3bf2-49c0-a08f-a3f007a4f761)



## Technical Info

This app respects the principles of the Clean Architecture, so it is organized in 3 well-defined layers: Presentation, Domain, and Data Layer.

![image](https://github.com/ivanbarto/kotlin-repos/assets/66323499/2923aaf4-ba7d-4b07-9c54-a89aa4b011c7)

Behind the scenes, the app supports offline functionality with the use of a RemoteMediator, combined with the paging provided by Paging 3 and the persistence that Room Database offers. Roughly speaking, the RemoteMediator allows the app to request API resources as the user scrolls through the list of posts. The information received is stored in a local database that acts as a cache. With this, the user is able to check the posts despite of the network state.

Using a PagingDataAdapter (combined with a LoadStateAdapter to show the current state of the list, such as a progress if items are loading or a "Retry" button if a network problem occurs) it is possible to render the components in a paginated way, improving the user experience and the performance of the app.

Also, the app has support for Dependency Injection pattern, using Hilt library.

The app also implements test cases to test the Retrofit service that returns the posts, and the RemoteMediator that takes care of loading the pages and storing them.




## Pending improvements

In the future I would like to add support for tablets (implementing layout variations) and enable the possibility that if the internet connection returns, the app automatically reloads the information if necessary (without tapping "retry" or pull-to-refreshing).




## Known issues

It happens that sometimes the recycler view of the posts page automatically scrolls down when new data is received. Also, when returning from details page, sometimes the recycler view scrolls down a little bit. It's reported on IssueTracker, and according to the comments, this issue hasn't been fixed yet: https://issuetracker.google.com/issues/123834703













