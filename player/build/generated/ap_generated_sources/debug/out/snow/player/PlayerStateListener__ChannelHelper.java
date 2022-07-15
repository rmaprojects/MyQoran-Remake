package snow.player;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import snow.player.audio.MusicItem;
import snow.player.playlist.PlaylistManager;

public final class PlayerStateListener__ChannelHelper {
  private static final String KEY_CLASS_NAME = "__class_name";

  private static final String KEY_METHOD_ID = "__method_id";

  private static final String CLASS_NAME = "snow.player.PlayerStateListener";

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

  private static final int METHOD_ID_14 = 14;

  private static final int METHOD_ID_15 = 15;

  private static final int METHOD_ID_16 = 16;

  private PlayerStateListener__ChannelHelper() {
    throw new AssertionError();
  }

  public static final class Emitter implements PlayerStateListener {
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
    public void onPlay(boolean stalled, int playProgress, long playProgressUpdateTime) {
      Map<String, Object> args = new HashMap<>();
      args.put("stalled", stalled);
      args.put("playProgress", playProgress);
      args.put("playProgressUpdateTime", playProgressUpdateTime);
      sendMessage(METHOD_ID_1, args);
    }

    @Override
    public void onPause(int playProgress, long updateTime) {
      Map<String, Object> args = new HashMap<>();
      args.put("playProgress", playProgress);
      args.put("updateTime", updateTime);
      sendMessage(METHOD_ID_2, args);
    }

    @Override
    public void onStop() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_3, args);
    }

    @Override
    public void onError(int errorCode, String errorMessage) {
      Map<String, Object> args = new HashMap<>();
      args.put("errorCode", errorCode);
      args.put("errorMessage", errorMessage);
      sendMessage(METHOD_ID_4, args);
    }

    @Override
    public void onPreparing() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_5, args);
    }

    @Override
    public void onPrepared(int audioSessionId) {
      Map<String, Object> args = new HashMap<>();
      args.put("audioSessionId", audioSessionId);
      sendMessage(METHOD_ID_6, args);
    }

    @Override
    public void onPrepared(int audioSessionId, int duration) {
      Map<String, Object> args = new HashMap<>();
      args.put("audioSessionId", audioSessionId);
      args.put("duration", duration);
      sendMessage(METHOD_ID_7, args);
    }

    @Override
    public void onStalledChanged(boolean stalled, int playProgress, long updateTime) {
      Map<String, Object> args = new HashMap<>();
      args.put("stalled", stalled);
      args.put("playProgress", playProgress);
      args.put("updateTime", updateTime);
      sendMessage(METHOD_ID_8, args);
    }

    @Override
    public void onBufferedProgressChanged(int bufferedProgress) {
      Map<String, Object> args = new HashMap<>();
      args.put("bufferedProgress", bufferedProgress);
      sendMessage(METHOD_ID_9, args);
    }

    @Override
    public void onPlayingMusicItemChanged(MusicItem musicItem, int position, int playProgress) {
      Map<String, Object> args = new HashMap<>();
      args.put("musicItem", musicItem);
      args.put("position", position);
      args.put("playProgress", playProgress);
      sendMessage(METHOD_ID_10, args);
    }

    @Override
    public void onSeekComplete(int progress, long updateTime, boolean stalled) {
      Map<String, Object> args = new HashMap<>();
      args.put("progress", progress);
      args.put("updateTime", updateTime);
      args.put("stalled", stalled);
      sendMessage(METHOD_ID_11, args);
    }

    @Override
    public void onPlaylistChanged(PlaylistManager playlistManager, int position) {
      Map<String, Object> args = new HashMap<>();
      args.put("playlistManager", playlistManager);
      args.put("position", position);
      sendMessage(METHOD_ID_12, args);
    }

    @Override
    public void onPlayModeChanged(PlayMode playMode) {
      Map<String, Object> args = new HashMap<>();
      args.put("playMode", playMode.ordinal());
      sendMessage(METHOD_ID_13, args);
    }

    @Override
    public void onRepeat(MusicItem musicItem, long repeatTime) {
      Map<String, Object> args = new HashMap<>();
      args.put("musicItem", musicItem);
      args.put("repeatTime", repeatTime);
      sendMessage(METHOD_ID_14, args);
    }

    @Override
    public void onSpeedChanged(float speed) {
      Map<String, Object> args = new HashMap<>();
      args.put("speed", speed);
      sendMessage(METHOD_ID_15, args);
    }

    @Override
    public void onShutdown() {
      Map<String, Object> args = new HashMap<>();
      sendMessage(METHOD_ID_16, args);
    }
  }

  public static final class Dispatcher implements channel.helper.Dispatcher {
    private final WeakReference<PlayerStateListener> callbackWeakReference;

    public Dispatcher(PlayerStateListener callback) {
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
      PlayerStateListener callback = callbackWeakReference.get();
      if (callback == null) {
        return false;
      }
      switch (methodId) {
        case METHOD_ID_1:
        boolean METHOD_ID_1_stalled = (boolean) data.get("stalled");
        int METHOD_ID_1_playProgress = (int) data.get("playProgress");
        long METHOD_ID_1_playProgressUpdateTime = (long) data.get("playProgressUpdateTime");
        callback.onPlay(METHOD_ID_1_stalled,METHOD_ID_1_playProgress,METHOD_ID_1_playProgressUpdateTime);
        return true;
        case METHOD_ID_2:
        int METHOD_ID_2_playProgress = (int) data.get("playProgress");
        long METHOD_ID_2_updateTime = (long) data.get("updateTime");
        callback.onPause(METHOD_ID_2_playProgress,METHOD_ID_2_updateTime);
        return true;
        case METHOD_ID_3:
        callback.onStop();
        return true;
        case METHOD_ID_4:
        int METHOD_ID_4_errorCode = (int) data.get("errorCode");
        String METHOD_ID_4_errorMessage = (String) data.get("errorMessage");
        callback.onError(METHOD_ID_4_errorCode,METHOD_ID_4_errorMessage);
        return true;
        case METHOD_ID_5:
        callback.onPreparing();
        return true;
        case METHOD_ID_6:
        int METHOD_ID_6_audioSessionId = (int) data.get("audioSessionId");
        callback.onPrepared(METHOD_ID_6_audioSessionId);
        return true;
        case METHOD_ID_7:
        int METHOD_ID_7_audioSessionId = (int) data.get("audioSessionId");
        int METHOD_ID_7_duration = (int) data.get("duration");
        callback.onPrepared(METHOD_ID_7_audioSessionId,METHOD_ID_7_duration);
        return true;
        case METHOD_ID_8:
        boolean METHOD_ID_8_stalled = (boolean) data.get("stalled");
        int METHOD_ID_8_playProgress = (int) data.get("playProgress");
        long METHOD_ID_8_updateTime = (long) data.get("updateTime");
        callback.onStalledChanged(METHOD_ID_8_stalled,METHOD_ID_8_playProgress,METHOD_ID_8_updateTime);
        return true;
        case METHOD_ID_9:
        int METHOD_ID_9_bufferedProgress = (int) data.get("bufferedProgress");
        callback.onBufferedProgressChanged(METHOD_ID_9_bufferedProgress);
        return true;
        case METHOD_ID_10:
        MusicItem METHOD_ID_10_musicItem = (MusicItem) data.get("musicItem");
        int METHOD_ID_10_position = (int) data.get("position");
        int METHOD_ID_10_playProgress = (int) data.get("playProgress");
        callback.onPlayingMusicItemChanged(METHOD_ID_10_musicItem,METHOD_ID_10_position,METHOD_ID_10_playProgress);
        return true;
        case METHOD_ID_11:
        int METHOD_ID_11_progress = (int) data.get("progress");
        long METHOD_ID_11_updateTime = (long) data.get("updateTime");
        boolean METHOD_ID_11_stalled = (boolean) data.get("stalled");
        callback.onSeekComplete(METHOD_ID_11_progress,METHOD_ID_11_updateTime,METHOD_ID_11_stalled);
        return true;
        case METHOD_ID_12:
        PlaylistManager METHOD_ID_12_playlistManager = (PlaylistManager) data.get("playlistManager");
        int METHOD_ID_12_position = (int) data.get("position");
        callback.onPlaylistChanged(METHOD_ID_12_playlistManager,METHOD_ID_12_position);
        return true;
        case METHOD_ID_13:
        PlayMode METHOD_ID_13_playMode = PlayMode.values()[(int) data.get("playMode")];
        callback.onPlayModeChanged(METHOD_ID_13_playMode);
        return true;
        case METHOD_ID_14:
        MusicItem METHOD_ID_14_musicItem = (MusicItem) data.get("musicItem");
        long METHOD_ID_14_repeatTime = (long) data.get("repeatTime");
        callback.onRepeat(METHOD_ID_14_musicItem,METHOD_ID_14_repeatTime);
        return true;
        case METHOD_ID_15:
        float METHOD_ID_15_speed = (float) data.get("speed");
        callback.onSpeedChanged(METHOD_ID_15_speed);
        return true;
        case METHOD_ID_16:
        callback.onShutdown();
        return true;
      }
      return false;
    }
  }
}
