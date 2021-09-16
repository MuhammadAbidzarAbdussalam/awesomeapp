package com.abidzar.awesomeapp.view

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.abidzar.awesomeapp.R
import com.abidzar.awesomeapp.view.adapter.CuratedPhotosPagedListAdapter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ScrollingActivityTest{

    private lateinit var scenario: ActivityScenario<ScrollingActivity>
    val LIST_ITEM_IN_TEST = 5


    @Before
    fun setup() {
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testChangeTypeList() {
        onView(withId(R.id.imvGridType)).perform(click())
        onView(withId(R.id.imvListType)).perform(click())
    }

    @Test
    fun selectItemList() {
//        onView(withId(R.id.rvCuratedPhotos)).perform(actionOnItemAtPosition<CuratedPhotosPagedListAdapter.PhotoItemViewHolder>(LIST_ITEM_IN_TEST, click()))

//        onView(withId(R.id.fab)).perform(click())
    }
}