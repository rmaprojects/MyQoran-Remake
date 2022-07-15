package snow.player;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public final class PlayerStateSynchronizer$OnSyncPlayerStateListener__ChannelHelper {
  private static final String KEY_CLASS_NAME = "__class_name";

  private static final String KEY_METHOD_ID = "__method_id";

  private static final String CLASS_NAME = "snow.player.PlayerStateSynchronizer.OnSyncPlayerStateListener";

  private static final int METHOD_ID_1 = 1;

  private PlayerStateSynchronizer$OnSyncPlayerStateListener__ChannelHelper() {
    throw new AssertionError();
  }

  public static final class Emitter implements PlayerStateSynchronizer.OnSyncPlayerStateListener {
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
    public void onSyncPlayerState(String clientToken, PlayerState playerState) {
      Map<String, Object> args = new HashMap<>();
      args.put("clientToken", clientToken);
      args.put("playerState", playerState);
      sendMessage(METHOD_ID_1, args);
    }
  }

  public static final class Dispatcher implements channel.helper.Dispatcher {
    private final WeakReference<PlayerStateSynchronizer.OnSyncPlayerStateListener> callbackWeakReference;

    public Dispatcher(PlayerStateSynchronizer.OnSyncPlayerStateListener callback) {
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
      PlayerStateSynchronizer.OnSyncPlayerStateListener callback = callbackWeakReference.get();
      if (callback == null) {
        return false;
      }
      switch (methodId) {
        case METHOD_ID_1:
        String METHOD_ID_1_clientToken = (String) data.get("clientToken");
        PlayerState METHOD_ID_1_playerState = (PlayerState) data.get("playerState");
        callback.onSyncPlayerState(METHOD_ID_1_clientToken,METHOD_ID_1_playerState);
        return true;
      }
      return false;
    }
  }
}
