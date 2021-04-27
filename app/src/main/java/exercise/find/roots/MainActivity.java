package exercise.find.roots;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.EOFException;

public class MainActivity extends AppCompatActivity {

  private BroadcastReceiver broadcastReceiverForSuccess = null;
  private BroadcastReceiver broadcastReceiverForFail = null;
  private long numberInput;
  private ProgressBar progressBar;
  private EditText editTextUserInput;
  private Button buttonCalculateRoots;

  private boolean isEditable = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    progressBar = findViewById(R.id.progressBar);
    editTextUserInput = findViewById(R.id.editTextInputNumber);
    buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);


    // set initial UI:
    editTextUserInput.setText(""); // cleanup text in edit-text
    buttonCalculateRoots.setEnabled(false);
    progressBar.setVisibility(View.GONE);
    editTextUserInput.setEnabled(true);


    // set listener on the input written by the keyboard to the edit-text
    editTextUserInput.addTextChangedListener(new TextWatcher() {
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      public void afterTextChanged(Editable s) {
        // text did change
        String newText = editTextUserInput.getText().toString();
        try {
          numberInput = Long.parseLong(newText);
          buttonCalculateRoots.setEnabled(true);
          if (numberInput <= 0) buttonCalculateRoots.setEnabled(false);
        } catch (NumberFormatException e) {
          buttonCalculateRoots.setEnabled(false);
        }
      }
    });

    // set click-listener to the button
    buttonCalculateRoots.setOnClickListener(v -> {
      Intent intentToOpenService = new Intent(MainActivity.this, CalculateRootsService.class);
      intentToOpenService.putExtra("number_for_service", numberInput);
      startService(intentToOpenService);
      setEditableScreen(false);
    });

    // register a broadcast-receiver to handle action "found_roots"
    broadcastReceiverForSuccess = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        if (incomingIntent == null || !incomingIntent.getAction().equals("found_roots")) return;
        // success finding roots!

        long root1 = incomingIntent.getLongExtra("root1", -1);
        long root2 = incomingIntent.getLongExtra("root2", -1);
        long originalNumber = incomingIntent.getLongExtra("original_number", -1);
        long calcTime = incomingIntent.getLongExtra("calc_time", -1);

        moveToResultActivity(root1, root2, originalNumber, calcTime);
        editTextUserInput.setText("");
        setEditableScreen(true);
      }
    };
    registerReceiver(broadcastReceiverForSuccess, new IntentFilter("found_roots"));

    broadcastReceiverForFail = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (intent == null || !intent.getAction().equals("stopped_calculations")) return;

        long originalNumber = intent.getLongExtra("original_number", -1);
        long timePassed = intent.getLongExtra("time_until_give_up_seconds", -1);

        Toast.makeText(MainActivity.this, "Calculation could not be finished for the number " + originalNumber
                + ". Stopped after " + timePassed / 1000f + " seconds. ", Toast.LENGTH_SHORT).show();


        editTextUserInput.setText("");
        setEditableScreen(true);
      }
    };

    registerReceiver(broadcastReceiverForFail, new IntentFilter("stopped_calculations"));
  }

  private void moveToResultActivity(long root1, long root2, long originalNumber, long calcTime) {
    Intent intent = new Intent(this, ResultsActivity.class);
    intent.putExtra("root1", root1);
    intent.putExtra("root2", root2);
    intent.putExtra("original_number", originalNumber);
    intent.putExtra("calc_time", calcTime);
    startActivity(intent);
  }

  private void setEditableScreen(boolean b) {
    progressBar.setVisibility(b ? View.GONE : View.VISIBLE);
    buttonCalculateRoots.setEnabled(b);
    editTextUserInput.setEnabled(b);
    isEditable = b;
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.unregisterReceiver(broadcastReceiverForFail);
    this.unregisterReceiver(broadcastReceiverForSuccess);
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean("is_editable_key", isEditable);
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    setEditableScreen(savedInstanceState.getBoolean("is_editable_key"));
  }
}
