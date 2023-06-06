# kotlin-repos

First of all, I want to thank Swiftly for this great opportunity! It has been a fun challenge to develop this product.
I hope you like the end result!

Instructions to compile the project:

1- Clone it

2- Open it with Android Studio and compile it.




## About the app

This app allows us to see the repositories related to the Kotlin language based on a master-detail pattern. To achieve this, it consumes the public Github API. 

There is also a Jetpack Compose Version! Check it out: https://github.com/ivanbarto/kotlin-repos-compose

Let's take a quick look at it:

![Screenshot_20230605_233307 Medium](https://github.com/ivanbarto/kotlin-repos/assets/66323499/eff80fda-f55f-48ac-9840-5db4e9dab58a)

This is the main screen, where we can see all the repositories. The view is built using the pagination concept. At the top, we have a button to clear the cache and reload the repositories.

![Screenshot_20230605_233335 Medium](https://github.com/ivanbarto/kotlin-repos/assets/66323499/adf6990b-b554-4140-9e96-7d480063e2ac)

This is the detail screen, where we can view other details of the repository, such as the creation date and the links to clone it (which can be copied with the copy button at the end of the container). Also, you can share the repository using other apps with the top bar button, or open it directly in the browser with the "View on GitHub" button.

![Screenshot_20230606_000623 Medium](https://github.com/ivanbarto/kotlin-repos/assets/66323499/8acce804-c982-45f3-ab13-d55f32de9a64)

This app also has support for dark and light theme!




## Technical Info

This app respects the principles of the Clean Architecture, so it is organized in 3 well-defined layers: Presentation, Domain, and Data Layer.

![image](https://github.com/ivanbarto/kotlin-repos/assets/66323499/2923aaf4-ba7d-4b07-9c54-a89aa4b011c7)

Behind the scenes, the app supports offline functionality with the use of a RemoteMediator, combined with the paging provided by Paging 3 and the persistence that Room Database offers. Roughly speaking, the RemoteMediator allows the app to request API resources as the user scrolls through the list of repositories. The information it receives is stored in a local database that acts as a cache. With this, the user is able to check the repositories despite of the network state.

Using a PagingDataAdapter (combined with a LoadStateAdapter to show the current state of the list, such as a progress if items are loading or a "Retry" button if a network problem occurs) it is possible to render the components in a paginated way, improving the user experience and the performance of the app.

Also, the app has support for Dependency Injection pattern, using Hilt library.

The app also implements test cases to test the Retrofit service that returns the repositories, and the RemoteMediator that takes care of loading the pages and storing them.




## Pending improvements

In the future I would like to add support for tablets (implementing layout variations) and enable the possibility that if the internet connection returns, the app automatically reloads the information if necessary.




## Known issues(only in XML version) 

There is a problem related to the strategy used to obtain the Next Page Number when the app is opened without internet connection and the cache is empty (for example, in the first use of the app). 

It happens that when invoked, the first LoadType through which the RemoteMediator passes is REFRESH, and in this case the first page number is 1. The RemoteMediator searches for the page, encounters an error for not having Internet connection and returns a MediatorResult.Error(), which is fine. 

The problem lies in that immediately after REFRESH, the RemoteMediator moves to the LoadType APPEND, and not finding a page number in the auxiliary table that stores the page number of each element (because there is no element yet) it returns by default a MediatorResult.Success(endOfPaginationReached = true) status and interprets that the end of the page was already reached, so the "Retry" button is not shown as it overlaps the previous LoadType. The way to adjust this visual bug is to check that the app is running for the first time, so you should change the strategy of the load() function of the RemoteMediator.

This does not happen in the Compose version, because there is not a LoadStateAdapter as such, but the item that indicates the progress or the "Retry" button is a Composable that works based on the LoadType REFRESH only, instead a LoadStateAdapter uses the three LoadTypes (REFRESH, APPEND and PREPEND).













