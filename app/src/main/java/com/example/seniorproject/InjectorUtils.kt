package com.example.seniorproject

import com.example.seniorproject.Auth.AuthViewModelFactory
import com.example.seniorproject.data.Firebase.FirebaseData
import com.example.seniorproject.data.repositories.PostRepository
import com.example.seniorproject.data.repositories.UserAuthRepo
//import com.example.seniorproject.viewModels.AuthenticationViewModelFactory
import com.example.seniorproject.viewModels.HomeFragmentViewModelFactory
//import com.example.seniorproject.viewModels.NewPostFragmentViewModelFactory

object InjectorUtils {



    fun providePostViewModelFactory(): HomeFragmentViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return HomeFragmentViewModelFactory(postrepo)
    }

   /* fun provideNewPostViewModelFactory(): NewPostFragmentViewModelFactory {
        val postrepo: PostRepository = PostRepository.getInstance(FirebaseData())
        return NewPostFragmentViewModelFactory(postrepo)
    }*/

}