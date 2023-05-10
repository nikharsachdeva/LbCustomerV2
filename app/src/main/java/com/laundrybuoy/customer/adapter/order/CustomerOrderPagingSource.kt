package com.laundrybuoy.customer.adapter.order

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.JsonObject
import com.laundrybuoy.customer.model.order.CustomerOrdersModel
import com.laundrybuoy.customer.model.transaction.CustomerTransactionModel
import com.laundrybuoy.customer.retrofit.CustomerAPI

class CustomerOrderPagingSource(private val customerApi: CustomerAPI) : PagingSource<Int, CustomerOrdersModel.Data.Partner>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CustomerOrdersModel.Data.Partner> {
        return try {
            val position = params.key ?: 1
            val response = customerApi.getCustomerOrders(position)
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

    override fun getRefreshKey(state: PagingState<Int, CustomerOrdersModel.Data.Partner>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}