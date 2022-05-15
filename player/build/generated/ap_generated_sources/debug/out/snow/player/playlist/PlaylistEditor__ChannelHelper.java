package snow.player.playlist;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import snow.player.audio.MusicItem;

public final class PlaylistEditor__ChannelHelper {
  private static final String KEY_CLASS_NAME = "__class_name";

  private static final String KEY_METHOD_ID = "__method_id";

  private static final String CLASS_NAME = "snow.player.playlist.PlaylistEditor";

  private static final int METHOD_ID_1 = 1;

  private static final int METHOD_ID_2 = 2;

  private static final int METHOD_ID_3 = 3;

  private static final int METHOD_ID_4 = 4;

  private static final int METHOD_ID_5 = 5;

  private static final int METHOD_ID_6 = 6;

  private static final int METHOD_ID_7 = 7;

  private PlaylistEditor__ChannelHelper() {
    throw new AssertionError();
  }

  public static final class Emitter implements PlaylistEditor {
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
    public void insertMusicItem(int position, MusicItem musicItem) {
      Map<String, Object> args = new HashMap<>();
      args.put("position", position);
      args.put("musicItem", musicItem);
      sendMessage(METHOD_ID_1, args);
    }

    @Override
    public void appendMusicItem(MusicItem musicItem) {
      Map<String, Object> args = new HashMap<>();
      args.put("musicItem", musicItem);
      sendMessage(METHOD_ID_2, args);
    }

    @Override
    public void moveMusicItem(int fromPosition, int toPosition) {
      Map<String, Object> args = new HashMap<>();
      args.put("fromPosition", fromPosition);
      args.put("toPosition", toPosition);
      sendMessage(METHOD_ID_3, args);
    }

    @Override
    public void removeMusicItem(MusicItem musicItem) {
      Map<String, Object> args = new HashMap<>();
      args.put("musicItem", musicItem);
      sendMessage(METHOD_ID_4, args);
    }

    @Override
    public void removeMusicItem(int position) {
      Map<String, Object> args = new HashMap<>();
      args.put("position", position);
      sendMessage(METHOD_ID_5, args);
    }

    @Override
    public void setNextPlay(MusicItem musicItem) {
      Map<String, Object> args = new HashMap<>();
      args.put("musicItem", musicItem);
      sendMessage(METHOD_ID_6, args);
    }

    @Override
    public void setPlaylist(Playlist playlist, int position, boolean play) {
      Map<String, Object> args = new HashMap<>();
      args.put("playlist", playlist);
      args.put("position", position);
      args.put("play", play);
      sendMessage(METHOD_ID_7, args);
    }
  }

  public static final class Dispatcher implements channel.helper.Dispatcher {
    private final WeakReference<PlaylistEditor> callbackWeakReference;

    public Dispatcher(PlaylistEditor callback) {
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
      PlaylistEditor callback = callbackWeakReference.get();
      if (callback == null) {
        return false;
      }
      switch (methodId) {
        case METHOD_ID_1:
        int METHOD_ID_1_position = (int) data.get("position");
        MusicItem METHOD_ID_1_musicItem = (MusicItem) data.get("musicItem");
        callback.insertMusicItem(METHOD_ID_1_position,METHOD_ID_1_musicItem);
        return true;
        case METHOD_ID_2:
        MusicItem METHOD_ID_2_musicItem = (MusicItem) data.get("musicItem");
        callback.appendMusicItem(METHOD_ID_2_musicItem);
        return true;
        case METHOD_ID_3:
        int METHOD_ID_3_fromPosition = (int) data.get("fromPosition");
        int METHOD_ID_3_toPosition = (int) data.get("toPosition");
        callback.moveMusicItem(METHOD_ID_3_fromPosition,METHOD_ID_3_toPosition);
        return true;
        case METHOD_ID_4:
        MusicItem METHOD_ID_4_musicItem = (MusicItem) data.get("musicItem");
        callback.removeMusicItem(METHOD_ID_4_musicItem);
        return true;
        case METHOD_ID_5:
        int METHOD_ID_5_position = (int) data.get("position");
        callback.removeMusicItem(METHOD_ID_5_position);
        return true;
        case METHOD_ID_6:
        MusicItem METHOD_ID_6_musicItem = (MusicItem) data.get("musicItem");
        callback.setNextPlay(METHOD_ID_6_musicItem);
        return true;
        case METHOD_ID_7:
        Playlist METHOD_ID_7_playlist = (Playlist) data.get("playlist");
        int METHOD_ID_7_position = (int) data.get("position");
        boolean METHOD_ID_7_play = (boolean) data.get("play");
        callback.setPlaylist(METHOD_ID_7_playlist,METHOD_ID_7_position,METHOD_ID_7_play);
        return true;
      }
      return false;
    }
  }
}
