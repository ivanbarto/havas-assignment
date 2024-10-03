
# MVVM Updates
Previously, `PostsViewModel` only had a single flow. changing to `LiveData` could be better? It  might be better for certain scenarios:
- Lifecycle Awareness: LiveData is lifecycle-aware, meaning it automatically handles UI updates based on the lifecycle of the Activity or Fragment. This reduces the chance of memory leaks and ensures that the UI is only updated when it's in an active state.
- Industry Standard: LiveData has been a common standard in Android development.

BUT I’ve chosen `StateFlow` and here is a couple of reasons why it might be better:
- Moderness: `StateFlow` is more aligned with Kotlin coroutines, which is becoming a more modern approach in Android development.
- Coroutines spread: the code is based on coroutines, so using StateFlow could be a better match. Using LiveData might feel out of place, especially if everything else is based on reactive coroutines.

I was also wondering if handling screen states instead of just using the loadstates of Paging data is redundant or not because `PagingData.LoadState` is part of Android's Paging 3 library, and it already has built-in states. Handling screen states separately can seem redundant at first, but there are good reasons to manage it:
- Separation of Concerns: By managing our own `ScreenState`  we keep control of the entire screen’s state. This is more flexible if the screen has complex interactions
- Combining Multiple Data Sources: If the screen has multiple data sources (like posts and profiles or subreddits), we might want to control how the entire screen reacts. The paging library’s loadstate only deals with paginated data, so we need a separate screen state.
- Explicit UI Control: With a `ScreenState`, we can explicitly handle scenarios like empty states, errors or retry mechanisms. Paging LoadState works great for lists but is limited in terms of custom error handling, retry logic, or displaying a different UI.
  
In this assessment, it is redundant because the screen only shows the paginated list, and loading/error states are handled directly in the adapter (using the paging library’s LoadState), but for demo purposes I’ve added the screen state management.

An Interactor was added to separate concerns even more.

I've also changed how the Composable view is used (removing the `XML layout` and using `ComposeView` directly)

# Memory Leaks Examples
There are 3 memory leaks examples divided into 2 classes. Let's have a look:

### PostsFragment and MemoryLeakPostFragment

#### Example 1:

![Screenshot from 2024-10-03 11-32-41](https://github.com/user-attachments/assets/ed7a6a80-2f82-43e7-b1bd-401e0e1e788c)

This could potentially lead to a memory leaks because the binding variable is declared as `lateinit`, but it is never nullified in `onDestroyView()`. This can lead to a memory leak if the fragment's view is destroyed and recreated (e.g, during configuration changes). If the `binding` variable is declared as `lateinit` and is not nullified in `onDestroyView()`, it retains a reference to the destroyed View. This means the View cannot be garbage collected even if the Fragment itself is no longer in use. To fix it, we need to set this variable as nullable and mark it as `null` in `onDestroyView()`.

![image](https://github.com/user-attachments/assets/bed38e9b-d806-4807-8896-114812b00a93)


#### Example 2:

![image](https://github.com/user-attachments/assets/54a4f6ab-f163-4267-81d1-ec2f962770c8)

While `lifecycleScope` offers a convenient way to automatically cancel long-running tasks when the Lifecycle reaches the`DESTROYED` state, collecting a flow without taking care lifecycle states (e.g, if the UI is visible or not) can lead to unncesary computations (which consumes resources because the flow still emits data and it's collected by the fragment) and app crashes (usually our flow is going to provide us with UI data that we need to show, but UI is not visible). We need to collect flow emitions when the lifecycle enters the `STARTED` state and stop collecting when it transitions to `STOPPED`.
In such cases, `Lifecycle` and `LifecycleOwner` provide the suspend `repeatOnLifecycle` API, which allow us to manage these transitions. This ensures that flow emissions are processed only when the UI is visible, which helps conserve resources and can prevent app crashes.

![image](https://github.com/user-attachments/assets/f468f3e2-e9a6-46e7-84a9-bacf71bb74cc)




### PostDetailFragment and MemoryLeakPostDetailFragment
![image](https://github.com/user-attachments/assets/d70b5472-377f-42e4-aaa3-5e06c658721d)

When Composables hold references to Views or other objects, failing to dispose of them can lead to memory leaks. This is particularly critical in long-lived components (like this fragment) that may be recreated multiple times throughout their lifecycle. Also, if a `ComposeView` continues to hold references to Composables after the Fragment's View has been destroyed, it may lead to crashes if those Composables attempt to interact with views that are no longer valid.

To fix this, we need to use `ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed` when creating a `ComposeView` within a `Fragment`. This strategy is needed for managing the lifecycle of the Composable content to prevent memory leaks, and ensures that the Composable content associated with the ComposeView is disposed when the view tree lifecycle is destroyed. This is important for managing resources properly and preventing the leaks I mentioned.

![image](https://github.com/user-attachments/assets/aba6ed52-c4f1-4fad-99a1-f21908132ef7)




# Reddit Assessment
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













