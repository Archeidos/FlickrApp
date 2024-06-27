package com.xylematic.flickrapp.viewmodel

import com.xylematic.flickrapp.data.FlickrRepository
import com.xylematic.flickrapp.data.Media
import com.xylematic.flickrapp.data.PhotoItem
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val flickrRepository: FlickrRepository = mock()

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val testCoroutineRule = object : TestWatcher() {
        override fun starting(description: Description?) {
            super.starting(description)
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description?) {
            super.finished(description)
            Dispatchers.resetMain()
        }
    }

    @Before
    fun setup() {
        viewModel = HomeViewModel("porcupine", flickrRepository)
    }

    @Test
    fun `init should update search results with default search text`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        // Given
        val photoItems = listOf(
            PhotoItem(
                title = "title",
                link = "url",
                media = Media(m = "media_url"),
                dateTaken = "2024-01-01",
                description = "description",
                published = "2024-01-01T00:00:00Z",
                author = "author",
                authorId = "author_id",
                tags = "tags"
            )
        )
        whenever(flickrRepository.searchPhotos("porcupine")).thenReturn(Result.success(photoItems))

        // When
        val photos = viewModel.photos.first()

        // Then
        assertEquals(photoItems, photos)
    }

    @Test
    fun `updateSearchResults should update photos with search results`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val searchText = "new search text"
        val photoItems = listOf(
            PhotoItem(
                title = "title",
                link = "url",
                media = Media(m = "media_url"),
                dateTaken = "2024-01-01",
                description = "description",
                published = "2024-01-01T00:00:00Z",
                author = "author",
                authorId = "author_id",
                tags = "tags"
            )
        )
        whenever(flickrRepository.searchPhotos(searchText)).thenReturn(Result.success(photoItems))

        viewModel.updateSearchResults(searchText)
        val photos = viewModel.photos.first()

        assertEquals(photoItems, photos)
    }
}