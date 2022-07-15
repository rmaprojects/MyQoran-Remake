package snow.player;

import android.os.Bundle;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public final class PlayerManager__ChannelHelper {
  private static final String KEY_CLASS_NAME = "__class_name";

  private static final String KEY_METHOD_ID = "__method_id";

  private static final String CLASS_NAME = "snow.player.PlayerManager";

  private static final int METHOD_ID_1 = 1;

  private static final int METHOD_ID_2 = 2;

  private static final int METHOD_ID_3 = 3;

  private static final int METHOD_ID_4 = 4;

  private static final int METHOD_ID_5 = 5;

  private static final int METHOD_ID_6 = 6;

  private PlayerManager__ChannelHelper() {
    throw new AssertionError();
  }

  public static final class Emitter implements PlayerManager {
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
    public void setSoundQuality(SoundQuality soundQuality) {
      Map<String, Object> args = new HashMap<>();
      args.put("soundQuality", soundQuality.ordinal());
      sendMessage(METHOD_ID_1, args);
    }

    @Override
    public void setAudioEffectConfig(Bundle config) {
      Map<String, Object> args = new HashMap<>();
      args.put("config", config);
      sendMessage(METHOD_ID_2, args);
    }

    @Override
    public void setAudioEffectEnabled(boolean enabled) {
      Map<String, Object> args = new HashMap<>();
      args.put("enabled", enabled);
      sendMessage(METHOD_ID_3, args);
    }

    @Override
    public void setOnlyWifiNetwork(boolean onlyWifiNetwork) {
      Map<String, Object> args = new HashMap<>();
      args.put("onlyWifiNetwork", onlyWifiNetwork);
      sendMessage(METHOD_ID_4, args);
    }

    @Override
    public void setIgnoreAudioFocus(boolean ignoreAudioFocus) {
      Map<String, Object> args = new HashMap<>();
      args.put("ignoreAudioFocus", ignoreAudioFocus);
      sendMessage(METHOD_ID_5, args);
    }

    @Override
    public void shutdown() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_6, args);
    }
  }

  public static final class Dispatcher implements channel.helper.Dispatcher {
    private final WeakReference<PlayerManager> callbackWeakReference;

    public Dispatcher(PlayerManager callback) {
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
      PlayerManager callback = callbackWeakReference.get();
      if (callback == null) {
        return false;
      }
      switch (methodId) {
        case METHOD_ID_1:
        SoundQuality METHOD_ID_1_soundQuality = SoundQuality.values()[(int) data.get("soundQuality")];
        callback.setSoundQuality(METHOD_ID_1_soundQuality);
        return true;
        case METHOD_ID_2:
        Bundle METHOD_ID_2_config = (Bundle) data.get("config");
        callback.setAudioEffectConfig(METHOD_ID_2_config);
        return true;
        case METHOD_ID_3:
        boolean METHOD_ID_3_enabled = (boolean) data.get("enabled");
        callback.setAudioEffectEnabled(METHOD_ID_3_enabled);
        return true;
        case METHOD_ID_4:
        boolean METHOD_ID_4_onlyWifiNetwork = (boolean) data.get("onlyWifiNetwork");
        callback.setOnlyWifiNetwork(METHOD_ID_4_onlyWifiNetwork);
        return true;
        case METHOD_ID_5:
        boolean METHOD_ID_5_ignoreAudioFocus = (boolean) data.get("ignoreAudioFocus");
        callback.setIgnoreAudioFocus(METHOD_ID_5_ignoreAudioFocus);
        return true;
        case METHOD_ID_6:
        callback.shutdown();
        return true;
      }
      return false;
    }
  }
}
