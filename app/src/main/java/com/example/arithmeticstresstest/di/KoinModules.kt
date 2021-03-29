package com.example.arithmeticstresstest.di

import com.example.arithmeticstresstest.model.DataViewModel
import com.example.arithmeticstresstest.model.DataViewModelFactory
import com.example.arithmeticstresstest.repository.FirebaseRepository
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module{
    viewModel{
        DataViewModel(get())
    }
}

val repositoryModule = module {
    fun provideFirebaseRepository() : FirebaseRepository {
        return FirebaseRepository()
    }

    single { provideFirebaseRepository() }
}

val factoryModule = module{
    factory {
        DataViewModelFactory(get())
    }
}