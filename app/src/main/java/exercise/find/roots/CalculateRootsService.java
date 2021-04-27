package exercise.find.roots;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.sql.Time;
import java.util.concurrent.TimeoutException;

public class CalculateRootsService extends IntentService {
  private static final int CHECK_FACTOR = 1009;  // number of iterations until next time check.

  public CalculateRootsService() {
    super("CalculateRootsService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent == null) return;
    long timeStartMs = System.currentTimeMillis();
    long numberToCalculateRootsFor = intent.getLongExtra("number_for_service", 0);
    if (numberToCalculateRootsFor <= 0) {
      Log.e("CalculateRootsService", "can't calculate roots for non-positive input" + numberToCalculateRootsFor);
      return;
    }

//    numberToCalculateRootsFor = Long.parseLong("9181531581341931811");

    long root = 1;
    for (long i = 2 ; i <= Math.sqrt(numberToCalculateRootsFor) ; i++){
      if (numberToCalculateRootsFor % i == 0){
        root = i;
        break;
      }
      if (i % CHECK_FACTOR == 0){
        System.out.println(System.currentTimeMillis() - timeStartMs);
        if (System.currentTimeMillis() - timeStartMs > 20 * 1000){
          root = -1;
          break;
        }
      }
    }

    Intent resultIntent = new Intent();

    if (root > 0){
      resultIntent.setAction("found_roots");
      resultIntent.putExtra("root1", root);
      resultIntent.putExtra("root2", numberToCalculateRootsFor / root);
      resultIntent.putExtra("calc_time", System.currentTimeMillis() - timeStartMs);
    }else{
      resultIntent.setAction("stopped_calculations");
      resultIntent.putExtra("time_until_give_up_seconds", System.currentTimeMillis() - timeStartMs);
    }
    resultIntent.putExtra("original_number", numberToCalculateRootsFor);
    this.sendBroadcast(resultIntent);
  }
}