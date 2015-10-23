package de.lehrbaum.tworooms;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;

import java.util.List;

import de.lehrbaum.tworooms.view.ChooseRoleActivity;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;

/**
 * Created by Sebastian on 21.10.2015.
 */
public class ChooseRolesTest extends ActivityInstrumentationTestCase2<ChooseRoleActivity> {

    ChooseRoleActivity mActivity;

    public ChooseRolesTest() {
        super(ChooseRoleActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    public void testSelectCategory() {
        onData(hasToString(startsWith("Base roles")))
                .inAdapterView(withId(R.id.spinner))
                .perform(click());

        onView(withId(android.R.id.list)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView v = (ListView) view;
                assertEquals(v.getCount(), 6);
                return true;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));
    }
}
