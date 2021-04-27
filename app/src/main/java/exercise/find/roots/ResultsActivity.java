package exercise.find.roots;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_results);

    TextView root1TV = findViewById(R.id.root1);
    TextView root2TV = findViewById(R.id.root2);
    TextView originalNumberTV = findViewById(R.id.original_number);
    TextView calcTimeTV = findViewById(R.id.calculation_time);

    Intent resultsIntent = getIntent();
    long root1 = resultsIntent.getLongExtra("root1", -1);
    long root2 = resultsIntent.getLongExtra("root2", -1);
    long originalNumber = resultsIntent.getLongExtra("original_number", -1);
    long calcTime = resultsIntent.getLongExtra("calc_time", -1);

    root1TV.setText(String.valueOf(root1));
    root2TV.setText(String.valueOf(root2));
    originalNumberTV.setText(String.valueOf(originalNumber));
    calcTimeTV.setText(getString(R.string.calculation_time_label, calcTime / 1000f));
  }
}