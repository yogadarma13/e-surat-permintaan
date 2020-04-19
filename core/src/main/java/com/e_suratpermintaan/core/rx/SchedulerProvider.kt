package com.e_suratpermintaan.core.rx

import io.reactivex.rxjava3.core.Scheduler

// https://dzone.com/articles/android-app-architecture-part-2-domain-layer

interface SchedulerProvider {
    val mainThread: Scheduler
    val io: Scheduler
    val newThread: Scheduler
}