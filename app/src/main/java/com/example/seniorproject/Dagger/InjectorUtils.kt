package com.example.seniorproject.Dagger

import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.repositories.PostRepository
import com.example.seniorproject.viewModels.Factories.*
//import com.example.seniorproject.viewModels.AuthenticationViewModelFactory

//import com.example.seniorproject.viewModels.NewPostFragmentViewModelFactory

object InjectorUtils {

    fun providePostViewModelFactory(): HomeFragmentViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return HomeFragmentViewModelFactory(postrepo)
    }

    fun provideListViewModelFactory(): ListViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return ListViewModelFactory(postrepo)
    }

    fun provideCommunityPostViewModelFacotry(): CommunityPostViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return CommunityPostViewModelFactory(postrepo)
    }

    fun provideSubscriptionsPostViewModelFactory(): SubscriptionsViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return SubscriptionsViewModelFactory(postrepo)
    }

    fun provideProfileViewModelFactory() : ProfileViewModelFactory {
        val postrepo = PostRepository.getInstance(FirebaseData())
        return ProfileViewModelFactory(postrepo)
    }



   /* fun provideNewPostViewModelFactory(): NewPostFragmentViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return NewPostFragmentViewModelFactory(postrepo)
    }*/

}