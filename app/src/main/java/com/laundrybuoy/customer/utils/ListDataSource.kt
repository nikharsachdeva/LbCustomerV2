package com.laundrybuoy.customer.utils

import android.os.Handler
import android.os.Looper
import androidx.paging.PositionalDataSource
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel.Data.Partner
import java.util.concurrent.Executor

class ListDataSource<T>(private val items: MutableList<Partner>) : PositionalDataSource<Partner>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Partner>) {
        callback.onResult(items, 0, items.size)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Partner>) {
        val start = params.startPosition
        val end = params.startPosition + params.loadSize
        callback.onResult(items.subList(start, end))
    }
}

// UiThreadExecutor implementation example
class UiThreadExecutor : Executor {
    private val handler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable) {
        handler.post(command)
    }
}