package com.laundrybuoy.customer.adapter.subscription

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.JsonObject
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel.Data.Partner
import com.laundrybuoy.customer.retrofit.CustomerAPI

class CustomerSubscriptionPagingSource(private val customerApi: CustomerAPI) : PagingSource<Int, Partner>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Partner> {
        return try {
            val position = params.key ?: 1
            val response = customerApi.getCustomerSubscription(position)
            return LoadResult.Page(
                data = response.body()?.data?.partners!!,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.body()?.data?.partners!!.isEmpty()) null else position + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Partner>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}