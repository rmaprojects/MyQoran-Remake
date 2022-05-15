package snow.player;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public final class SleepTimer__ChannelHelper {
  private static final String KEY_CLASS_NAME = "__class_name";

  private static final String KEY_METHOD_ID = "__method_id";

  private static final String CLASS_NAME = "snow.player.SleepTimer";

  private static final int METHOD_ID_1 = 1;

  private static final int METHOD_ID_2 = 2;

  private static final int METHOD_ID_3 = 3;

  private SleepTimer__ChannelHelper() {
    throw new AssertionError();
  }

  public static final class Emitter implements SleepTimer {
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
    public void startSleepTimer(long time, SleepTimer.TimeoutAction action) {
      Map<String, Object> args = new HashMap<>();
      args.put("time", time);
      args.put("action", action.ordinal());
      sendMessage(METHOD_ID_1, args);
    }

    @Override
    public void cancelSleepTimer() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_2, args);
    }

    @Override
    public void setWaitPlayComplete(boolean waitPlayComplete) {
      Map<String, Object> args = new HashMap<>();
      args.put("waitPlayComplete", waitPlayComplete);
      sendMessage(METHOD_ID_3, args);
    }
  }

  public static final class Dispatcher implements channel.helper.Dispatcher {
    private final WeakReference<SleepTimer> callbackWeakReference;

    public Dispatcher(SleepTimer callback) {
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
      SleepTimer callback = callbackWeakReference.get();
      if (callback == null) {
        return false;
      }
      switch (methodId) {
        case METHOD_ID_1:
        long METHOD_ID_1_time = (long) data.get("time");
        SleepTimer.TimeoutAction METHOD_ID_1_action = SleepTimer.TimeoutAction.values()[(int) data.get("action")];
        callback.startSleepTimer(METHOD_ID_1_time,METHOD_ID_1_action);
        return true;
        case METHOD_ID_2:
        callback.cancelSleepTimer();
        return true;
        case METHOD_ID_3:
        boolean METHOD_ID_3_waitPlayComplete = (boolean) data.get("waitPlayComplete");
        callback.setWaitPlayComplete(METHOD_ID_3_waitPlayComplete);
        return true;
      }
      return false;
    }
  }
}
