package hr.konjetic.fishy.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import hr.konjetic.fishy.network.model.Fish
import hr.konjetic.fishy.network.model.FishyAPIService

class FishPagingSource(private val service: FishyAPIService) : PagingSource<Int, Fish>() {
    override fun getRefreshKey(state: PagingState<Int, Fish>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Fish> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = service.getAllFishPaging(nextPageNumber, 5)

            if (response.items.isEmpty()) {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            } else {
                LoadResult.Page(
                    data = response.items,
                    prevKey = null,
                    nextKey = response.currentPage + 1
                )
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}