package snow.player;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public final class Player__ChannelHelper {
  private static final String KEY_CLASS_NAME = "__class_name";

  private static final String KEY_METHOD_ID = "__method_id";

  private static final String CLASS_NAME = "snow.player.Player";

  private static final int METHOD_ID_1 = 1;

  private static final int METHOD_ID_2 = 2;

  private static final int METHOD_ID_3 = 3;

  private static final int METHOD_ID_4 = 4;

  private static final int METHOD_ID_5 = 5;

  private static final int METHOD_ID_6 = 6;

  private static final int METHOD_ID_7 = 7;

  private static final int METHOD_ID_8 = 8;

  private static final int METHOD_ID_9 = 9;

  private static final int METHOD_ID_10 = 10;

  private static final int METHOD_ID_11 = 11;

  private static final int METHOD_ID_12 = 12;

  private static final int METHOD_ID_13 = 13;

  private Player__ChannelHelper() {
    throw new AssertionError();
  }

  public static final class Emitter implements Player {
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
    public void play() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_1, args);
    }

    @Override
    public void pause() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_2, args);
    }

    @Override
    public void stop() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_3, args);
    }

    @Override
    public void playPause() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_4, args);
    }

    @Override
    public void seekTo(int progress) {
      Map<String, Object> args = new HashMap<>();
      args.put("progress", progress);
      sendMessage(METHOD_ID_5, args);
    }

    @Override
    public void fastForward() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_6, args);
    }

    @Override
    public void rewind() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_7, args);
    }

    @Override
    public void skipToNext() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_8, args);
    }

    @Override
    public void skipToPrevious() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_9, args);
    }

    @Override
    public void skipToPosition(int position) {
      Map<String, Object> args = new HashMap<>();
      args.put("position", position);
      sendMessage(METHOD_ID_10, args);
    }

    @Override
    public void playPause(int position) {
      Map<String, Object> args = new HashMap<>();
      args.put("position", position);
      sendMessage(METHOD_ID_11, args);
    }

    @Override
    public void setPlayMode(PlayMode playMode) {
      Map<String, Object> args = new HashMap<>();
      args.put("playMode", playMode.ordinal());
      sendMessage(METHOD_ID_12, args);
    }

    @Override
    public void setSpeed(float speed) {
      Map<String, Object> args = new HashMap<>();
      args.put("speed", speed);
      sendMessage(METHOD_ID_13, args);
    }
  }

  public static final class Dispatcher implements channel.helper.Dispatcher {
    private final WeakReference<Player> callbackWeakReference;

    public Dispatcher(Player callback) {
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
      Player callback = callbackWeakReference.get();
      if (callback == null) {
        return false;
      }
      switch (methodId) {
        case METHOD_ID_1:
        callback.play();
        return true;
        case METHOD_ID_2:
        callback.pause();
        return true;
        case METHOD_ID_3:
        callback.stop();
        return true;
        case METHOD_ID_4:
        callback.playPause();
        return true;
        case METHOD_ID_5:
        int METHOD_ID_5_progress = (int) data.get("progress");
        callback.seekTo(METHOD_ID_5_progress);
        return true;
        case METHOD_ID_6:
        callback.fastForward();
        return true;
        case METHOD_ID_7:
        callback.rewind();
        return true;
        case METHOD_ID_8:
        callback.skipToNext();
        return true;
        case METHOD_ID_9:
        callback.skipToPrevious();
        return true;
        case METHOD_ID_10:
        int METHOD_ID_10_position = (int) data.get("position");
        callback.skipToPosition(METHOD_ID_10_position);
        return true;
        case METHOD_ID_11:
        int METHOD_ID_11_position = (int) data.get("position");
        callback.playPause(METHOD_ID_11_position);
        return true;
        case METHOD_ID_12:
        PlayMode METHOD_ID_12_playMode = PlayMode.values()[(int) data.get("playMode")];
        callback.setPlayMode(METHOD_ID_12_playMode);
        return true;
        case METHOD_ID_13:
        float METHOD_ID_13_speed = (float) data.get("speed");
        callback.setSpeed(METHOD_ID_13_speed);
        return true;
      }
      return false;
    }
  }
}
