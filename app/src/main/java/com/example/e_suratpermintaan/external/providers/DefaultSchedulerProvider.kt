package com.example.e_suratpermintaan.external.providers

import com.e_suratpermintaan.core.rx.SchedulerProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class DefaultSchedulerProvider : SchedulerProvider {
    override val mainThread: Scheduler
        get() = AndroidSchedulers.mainThread()
    override val io: Scheduler
        get() = Schedulers.io()
    override val newThread: Scheduler
        get() = Schedulers.newThread()
}