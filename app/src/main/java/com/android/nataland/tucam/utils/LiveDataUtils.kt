package com.android.nataland.tucam.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

data class Event<out T>(private val content: T) {
    private var handled = false

    fun handle(handler: T.() -> Boolean) {
        enforceMain {
            if (!handled) {
                handled = content.handler()
            }
        }
    }

    fun peekContent(): T = content
}

fun <T> MutableLiveData<in Event<T>>.postEvent(content: T) = postValue(Event(content))

fun <T> LiveData<Event<T>>.subscribeForeverToEvent(observer: (T?) -> Boolean): Subscription {
    val wrapped = Observer<Event<T>> { it?.handle(observer) }
    observeForever(wrapped)
    return object : Subscription {
        override fun dispose() {
            removeObserver(wrapped)
        }
    }
}

fun <T> LiveData<Event<T>>.subscribeToEvent(
    owner: LifecycleOwner,
    observer: (T) -> Boolean
): Subscription {
    val wrapped = Observer<Event<T>> { it?.handle(observer) }
    observe(owner, wrapped)
    return object : Subscription {
        override fun dispose() {
            removeObserver(wrapped)
        }
    }
}

fun <T> LiveData<T>.subscribeForever(observer: (T?) -> Unit): Subscription {
    val wrapped = Observer<T> { observer(it) }
    observeForever(wrapped)
    return object : Subscription {
        override fun dispose() {
            removeObserver(wrapped)
        }
    }
}

fun <T> LiveData<T>.subscribe(owner: LifecycleOwner, observer: (T?) -> Unit): Subscription {
    val wrapped = Observer<T> { observer(it) }
    observe(owner, wrapped)
    return object : Subscription {
        override fun dispose() {
            removeObserver(wrapped)
        }
    }
}

interface Subscription {
    fun dispose()
}

class CompositeSubscription(vararg subscriptions: Subscription) :
    Subscription {
    private val backingList = ArrayList<Subscription>().apply { addAll(subscriptions) }

    operator fun plusAssign(disposables: Iterable<Subscription>) {
        backingList += disposables
    }

    operator fun plusAssign(subscription: Subscription) {
        backingList += subscription
    }

    override fun dispose() {
        backingList.forEach(Subscription::dispose)
        backingList.clear()
    }
}
