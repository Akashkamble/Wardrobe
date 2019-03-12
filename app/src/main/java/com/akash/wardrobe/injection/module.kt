package com.akash.wardrobe.injection

import androidx.room.Room
import com.akash.wardrobe.database.WardrobeDatabase
import com.akash.wardrobe.repository.WardrobeRepository
import com.akash.wardrobe.repository.WardrobeRepositoryImpl
import com.akash.wardrobe.viewmodels.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    // Room Database instance
    single {
        Room.databaseBuilder(androidApplication(), WardrobeDatabase::class.java, "wardrobe_db")
            .build()
    }
    factory { get<WardrobeDatabase>().wardrobeDao()}
    factory { WardrobeRepositoryImpl(get())as WardrobeRepository }
    viewModel { MainViewModel(get())}
}