package de.lehrbaum.tworooms;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lehrbaum.tworooms.view.ChooseRoleActivity;
import de.lehrbaum.tworooms.view.CreateSetActivity;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

/**
 * Created by findu on 23.10.2015.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateSetTest extends ActivityInstrumentationTestCase2<CreateSetActivity> {

    CreateSetActivity mActivity;

    public CreateSetTest() {
        super(CreateSetActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    @Test
    public void testInsertStandardSet() {

    }

    private void setNameDescription(String name, String description) {
        if(name != null)
            onView(withId(R.id.name_text)).perform(typeText(name), closeSoftKeyboard());
        if(description != null)
            onView(withId(R.id.desc_text)).perform(typeText(description), closeSoftKeyboard());
    }
}
