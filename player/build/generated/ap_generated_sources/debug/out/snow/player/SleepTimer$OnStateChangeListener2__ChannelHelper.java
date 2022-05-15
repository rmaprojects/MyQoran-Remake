package snow.player;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public final class SleepTimer$OnStateChangeListener2__ChannelHelper {
  private static final String KEY_CLASS_NAME = "__class_name";

  private static final String KEY_METHOD_ID = "__method_id";

  private static final String CLASS_NAME = "snow.player.SleepTimer.OnStateChangeListener2";

  private static final int METHOD_ID_1 = 1;

  private static final int METHOD_ID_2 = 2;

  private static final int METHOD_ID_3 = 3;

  private static final int METHOD_ID_4 = 4;

  private SleepTimer$OnStateChangeListener2__ChannelHelper() {
    throw new AssertionError();
  }

  public static final class Emitter implements SleepTimer.OnStateChangeListener2 {
    private channel.helper.Emitter emitter;

    public Emitter(channel.helper.Emitter emitter) {
      this.emitter = emitter;
    }

    private void sendMessage(int id, Map<String, Object> args) {
      args.put(KEY_CLASS_NAME, CLASS_NAME);
      args.put(KEY_METHOD_ID, id);
      emitter.emit(args);
    }

    @Override
    public void onTimerStart(long time, long startTime, SleepTimer.TimeoutAction action) {
      Map<String, Object> args = new HashMap<>();
      args.put("time", time);
      args.put("startTime", startTime);
      args.put("action", action.ordinal());
      sendMessage(METHOD_ID_1, args);
    }

    @Override
    public void onTimerEnd() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_2, args);
    }

    @Override
    public void onTimerStart(long time, long startTime, SleepTimer.TimeoutAction action,
        boolean waitPlayComplete) {
      Map<String, Object> args = new HashMap<>();
      args.put("time", time);
      args.put("startTime", startTime);
      args.put("action", action.ordinal());
      args.put("waitPlayComplete", waitPlayComplete);
      sendMessage(METHOD_ID_3, args);
    }

    @Override
    public void onTimeout(boolean actionComplete) {
      Map<String, Object> args = new HashMap<>();
      args.put("actionComplete", actionComplete);
      sendMessage(METHOD_ID_4, args);
    }
  }

  public static final class Dispatcher implements channel.helper.Dispatcher {
    private final WeakReference<SleepTimer.OnStateChangeListener2> callbackWeakReference;

    public Dispatcher(SleepTimer.OnStateChangeListener2 callback) {
      this.callbackWeakReference = new WeakReference<>(callback);
    }

    @Override
    public boolean match(Map<String, Object> data) {
      return CLASS_NAME.equals(data.get(KEY_CLASS_NAME));
    }

    @Override
    public boolean dispatch(Map<String, Object> data) {
      if (!match(data)) {
        return false;
      }
      int methodId = (int) data.get(KEY_METHOD_ID);
      SleepTimer.OnStateChangeListener2 callback = callbackWeakReference.get();
      if (callback == null) {
        return false;
      }
      switch (methodId) {
        case METHOD_ID_1:
        long METHOD_ID_1_time = (long) data.get("time");
        long METHOD_ID_1_startTime = (long) data.get("startTime");
        SleepTimer.TimeoutAction METHOD_ID_1_action = SleepTimer.TimeoutAction.values()[(int) data.get("action")];
        callback.onTimerStart(METHOD_ID_1_time,METHOD_ID_1_startTime,METHOD_ID_1_action);
        return true;
        case METHOD_ID_2:
        callback.onTimerEnd();
        return true;
        case METHOD_ID_3:
        long METHOD_ID_3_time = (long) data.get("time");
        long METHOD_ID_3_startTime = (long) data.get("startTime");
        SleepTimer.TimeoutAction METHOD_ID_3_action = SleepTimer.TimeoutAction.values()[(int) data.get("action")];
        boolean METHOD_ID_3_waitPlayComplete = (boolean) data.get("waitPlayComplete");
        callback.onTimerStart(METHOD_ID_3_time,METHOD_ID_3_startTime,METHOD_ID_3_action,METHOD_ID_3_waitPlayComplete);
        return true;
        case METHOD_ID_4:
        boolean METHOD_ID_4_actionComplete = (boolean) data.get("actionComplete");
        callback.onTimeout(METHOD_ID_4_actionComplete);
        return true;
      }
      return false;
    }
  }
}
