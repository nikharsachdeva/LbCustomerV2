package com.laundrybuoy.customer.adapter.coins

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.JsonObject
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel
import com.laundrybuoy.customer.retrofit.CustomerAPI

class CustomerCoinsPagingSource(private val customerApi: CustomerAPI) : PagingSource<Int, CustomerTransactionModel.Data.Partner>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CustomerTransactionModel.Data.Partner> {
        return try {
            val position = params.key ?: 1
            val response = customerApi.getCustomerCoins(position)
            return LoadResult.Page(
                data = response.body()?.data?.partners!!,
                prevKey = if (position == 1) null else position - 1,
//                nextKey = if (position == response.body()?.data?.totalPage) null else position + 1
                nextKey = if (response.body()?.data?.partners!!.isEmpty()) null else position + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CustomerTransactionModel.Data.Partner>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}