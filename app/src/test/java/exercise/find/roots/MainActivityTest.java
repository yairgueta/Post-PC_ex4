package exercise.find.roots;

import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowSystemClock;
import org.w3c.dom.ls.LSOutput;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest extends TestCase {

  @Test
  public void when_activityIsLaunching_then_theButtonShouldStartDisabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // test: make sure that the "calculate" button is disabled
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);
    assertFalse(button.isEnabled());
  }

  @Test
  public void when_activityIsLaunching_then_theEditTextShouldStartEmpty(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // test: make sure that the "input" edit-text has no text
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    String input = inputEditText.getText().toString();
    assertTrue(input == null || input.isEmpty());
  }

  @Test
  public void when_userIsEnteringNumberInput_and_noCalculationAlreadyHappned_then_theButtonShouldBeEnabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text and the button
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    inputEditText.setText("3535");
    assertTrue(button.isEnabled());
  }


  @Test
  public void when_activityLaunches_then_progressBarShouldBeGone(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);

    assertEquals(progressBar.getVisibility(), View.GONE);
  }

  @Test
  public void when_insertingGoodNumber_then_progressBarShouldBeVisible(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    inputEditText.setText("1234");
    button.performClick();

    assertEquals(progressBar.getVisibility(), View.VISIBLE);
  }

  @Test
  public void when_insertingBadNumber_then_buttonShouldBeDisabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    inputEditText.setText("17.4");

    assertFalse(button.isEnabled());
  }

  @Test
  public void when_insertingGoodNegOrZeroNumber_then_buttonShouldBeDisabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    inputEditText.setText("0");
    assertFalse(button.isEnabled());

    inputEditText.setText("-17");
    assertFalse(button.isEnabled());
  }

  @Test
  public void when_userIsEnteringGoodInput_and_thenDeletingIt_then_theButtonShouldBeEnabledAndThenDisabled(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    // find the edit-text and the button
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);
    Button button = mainActivity.findViewById(R.id.buttonCalculateRoots);

    inputEditText.setText("3535");
    assertTrue(button.isEnabled());

    inputEditText.setText("");
    assertFalse(button.isEnabled());
  }


  @Test
  public void when_insertingInputAndFlippingScreen_then_inputShouldBeThere(){
    // create a MainActivity and let it think it's currently displayed on the screen
    MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();

    ProgressBar progressBar = mainActivity.findViewById(R.id.progressBar);
    EditText inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);

    String input = "35467";
    inputEditText.setText(input);
    mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    inputEditText = mainActivity.findViewById(R.id.editTextInputNumber);

    assertEquals(input, inputEditText.getText().toString());
  }



  // TODO: add 1 or 2 more unit tests to the activity. so your "writing tests" skill won't get rusty.
  //  possible flows to unit-test:
  //  - when starting a calculation the button should be locked (disabled)
  //  - when starting a calculation and than activity receives "stopped_calculations" broadcast, the button should be unlocked (enabled)
  //  - when starting a calculation and than activity receives "stopped_calculations" broadcast, "progress" should disappear
  //
  //
  // to mock sending a broadcast:
  //    create the broadcast intent (example: `new Intent("my_action_here")` ) and put extras
  //    call `RuntimeEnvironment.application.sendBroadcast()` to send the broadcast
  //    call `Shadows.shadowOf(Looper.getMainLooper()).idle();` to let the android OS time to process the broadcast the let your activity consume it
}