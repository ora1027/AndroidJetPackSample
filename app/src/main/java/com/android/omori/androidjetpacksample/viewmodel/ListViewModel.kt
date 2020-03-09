package com.android.omori.androidjetpacksample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.omori.androidjetpacksample.model.DogBreed
import com.android.omori.androidjetpacksample.model.DogsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {

    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
//        This hard coding is just temporary. I will implement to get data using API.
//        val dog1 = DogBreed(
//            "1",
//            "Corgi",
//            "15 years",
//            "breedGroup",
//            "bredFor",
//            "temperament",
//            ""
//        )
//
//        val dog2 = DogBreed(
//            "2",
//            "Labrador",
//            "10 years",
//            "breedGroup",
//            "bredFor",
//            "temperament",
//            ""
//        )
//
//        val dog3 = DogBreed(
//            "3",
//            "Rottweiler",
//            "20 years",
//            "breedGroup",
//            "bredFor",
//            "temperament",
//            ""
//        )
//
//        val dogList : ArrayList<DogBreed> = arrayListOf<DogBreed>(dog1, dog2, dog3)
//        dogs.value = dogList
//        dogsLoadError.value = false
//        loading.value = false

        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {

                    override fun onSuccess(dogList : List<DogBreed>) {
                        dogs.value = dogList
                        dogsLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}