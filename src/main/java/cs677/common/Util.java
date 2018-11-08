package cs677.common;

import org.json.JSONException;
import org.json.JSONObject;

public class Util {

  public static long getSeconds(JSONObject jsonObject) {
    long seconds;
    try {
      String timeString = jsonObject.getString(Constants.CREATED_UTC);
      seconds = Long.parseLong(timeString);
    } catch (JSONException e) {
      try {
        seconds = jsonObject.getLong(Constants.CREATED_UTC);
      } catch (JSONException err) {
        System.out.println(e.getMessage());
        return 0;
      }
    }
    return seconds;
  }
}
