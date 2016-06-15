package net.flyget.bluetoothchat.sound;

import java.util.HashMap;

import net.flyget.bluetoothchat.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundEffect implements OnLoadCompleteListener {
	public static final int SOUND_SEND = 0;
	public static final int SOUND_RECV = 1;
	public static final int SOUND_ERR = 2;
	public static final int SOUND_PLAY = 3;
	private static SoundEffect mSound;
	private SoundPool mSoundPool;
	private int mLoadNum = 0;
	private HashMap<Integer, Integer> mSoundMap;
	
	public static SoundEffect getInstance(Context context){
		if(mSound == null){
			mSound = new SoundEffect(context);
		}
		return mSound;
	}
	
	private SoundEffect(Context context){
		mSoundMap = new HashMap<Integer, Integer>();
		// SoundPool(int maxStreams, int streamType, int srcQuality)
		mSoundPool= new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
		// load(Context context, int resId, int priority)
		mSoundMap.put(SOUND_SEND, mSoundPool.load(context, R.raw.send, 1));
		mSoundMap.put(SOUND_RECV, mSoundPool.load(context, R.raw.recv, 1));
		mSoundMap.put(SOUND_ERR, mSoundPool.load(context, R.raw.error, 1));
		mSoundMap.put(SOUND_PLAY, mSoundPool.load(context, R.raw.play_completed, 1));
		mSoundPool.setOnLoadCompleteListener(this);
	}

	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		mLoadNum ++;
	}
	
	/**
	 * 0：send sound
	 * 1: recv sound
	 * 2: error sound
	 * 3: play sound
	 * @param idx
	 */
	public void play(int idx){
		if(idx > mSoundMap.size() || idx < 0)
			return;
		if(mLoadNum < 4)
			return;
		// play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
		mSoundPool.play(mSoundMap.get(idx), 1, 1, 0, 0, 1);
	}
}
