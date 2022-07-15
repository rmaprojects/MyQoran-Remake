package snow.player;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public final class PlayerStateSynchronizer__ChannelHelper {
  private static final String KEY_CLASS_NAME = "__class_name";

  private static final String KEY_METHOD_ID = "__method_id";

  private static final String CLASS_NAME = "snow.player.PlayerStateSynchronizer";

  private static final int METHOD_ID_1 = 1;

  private PlayerStateSynchronizer__ChannelHelper() {
    throw new AssertionError();
  }

  public static final class Emitter implements PlayerStateSynchronizer {
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
    public void syncPlayerState(String clientToken) {
      Map<String, Object> args = new HashMap<>();
      args.put("clientToken", clientToken);
      sendMessage(METHOD_ID_1, args);
    }
  }

  public static final class Dispatcher implements channel.helper.Dispatcher {
    private final WeakReference<PlayerStateSynchronizer> callbackWeakReference;

    public Dispatcher(PlayerStateSynchronizer callback) {
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
      PlayerStateSynchronizer callback = callbackWeakReference.get();
      if (callback == null) {
        return false;
      }
      switch (methodId) {
        case METHOD_ID_1:
        String METHOD_ID_1_clientToken = (String) data.get("clientToken");
        callback.syncPlayerState(METHOD_ID_1_clientToken);
        return true;
      }
      return false;
    }
  }
}
