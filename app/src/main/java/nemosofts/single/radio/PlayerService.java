package nemosofts.single.radio;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.Surface;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import nemosofts.single.radio.Constant.Constant;
import nemosofts.single.radio.Receiver.HardButtonReceiver;
import nemosofts.single.radio.Receiver.MediaButtonIntentReceiver;
import nemosofts.single.radio.Receiver.Setting;


/**
 * Created by thivakaran
 */
public class PlayerService extends IntentService implements Player.EventListener, HardButtonReceiver.HardButtonListener {

    public static final String ACTION_TOGGLE = "action.ACTION_TOGGLE";
    public static final String ACTION_PLAY = "action.ACTION_PLAY";
    public static final String ACTION_STOP = "action.ACTION_STOP";

    static SimpleExoPlayer exoPlayer;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder notification;
    RemoteViews smallViews;
    DefaultBandwidthMeter bandwidthMeter;
    DataSource.Factory dataSourceFactory;
    ExtractorsFactory extractorsFactory;
    static PlayerService playerService;
    Boolean isNewSong = false;
    ComponentName componentName;
    AudioManager mAudioManager;
    PowerManager.WakeLock mWakeLock;
    private HardButtonReceiver mButtonReceiver;

    public PlayerService() {
        super(null);
    }

    static public PlayerService getInstance() {
        if (playerService == null) {
            playerService = new PlayerService();
        }
        return playerService;
    }

    public static Boolean getIsPlayling() {
        return exoPlayer != null && exoPlayer.getPlayWhenReady();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    @Override
    public void onCreate() {

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        componentName = new ComponentName(getPackageName(), MediaButtonIntentReceiver.class.getName());
        mAudioManager.registerMediaButtonEventReceiver(componentName);

        bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), "nemosofts"), bandwidthMeter);
        extractorsFactory = new DefaultExtractorsFactory();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
        exoPlayer.addListener(this);

        exoPlayer.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onPlayerStateChanged(EventTime eventTime, boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onTimelineChanged(EventTime eventTime, int reason) {

            }

            @Override
            public void onPositionDiscontinuity(EventTime eventTime, int reason) {

            }

            @Override
            public void onSeekStarted(EventTime eventTime) {

            }

            @Override
            public void onSeekProcessed(EventTime eventTime) {

            }

            @Override
            public void onPlaybackParametersChanged(EventTime eventTime, PlaybackParameters playbackParameters) {

            }

            @Override
            public void onRepeatModeChanged(EventTime eventTime, int repeatMode) {

            }

            @Override
            public void onShuffleModeChanged(EventTime eventTime, boolean shuffleModeEnabled) {

            }

            @Override
            public void onLoadingChanged(EventTime eventTime, boolean isLoading) {

            }

            @Override
            public void onPlayerError(EventTime eventTime, ExoPlaybackException error) {

            }

            @Override
            public void onTracksChanged(EventTime eventTime, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadStarted(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData) {

            }

            @Override
            public void onLoadCompleted(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData) {

            }

            @Override
            public void onLoadCanceled(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData) {

            }

            @Override
            public void onLoadError(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {

            }

            @Override
            public void onDownstreamFormatChanged(EventTime eventTime, MediaSourceEventListener.MediaLoadData mediaLoadData) {

            }

            @Override
            public void onUpstreamDiscarded(EventTime eventTime, MediaSourceEventListener.MediaLoadData mediaLoadData) {

            }

            @Override
            public void onMediaPeriodCreated(EventTime eventTime) {

            }

            @Override
            public void onMediaPeriodReleased(EventTime eventTime) {

            }

            @Override
            public void onReadingStarted(EventTime eventTime) {

            }

            @Override
            public void onBandwidthEstimate(EventTime eventTime, int totalLoadTimeMs, long totalBytesLoaded, long bitrateEstimate) {

            }

            @Override
            public void onViewportSizeChange(EventTime eventTime, int width, int height) {

            }

            @Override
            public void onNetworkTypeChanged(EventTime eventTime, @Nullable NetworkInfo networkInfo) {

            }

            @Override
            public void onMetadata(EventTime eventTime, Metadata metadata) {

            }

            @Override
            public void onDecoderEnabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {

            }

            @Override
            public void onDecoderInitialized(EventTime eventTime, int trackType, String decoderName, long initializationDurationMs) {

            }

            @Override
            public void onDecoderInputFormatChanged(EventTime eventTime, int trackType, Format format) {

            }

            @Override
            public void onDecoderDisabled(EventTime eventTime, int trackType, DecoderCounters decoderCounters) {

            }

            @Override
            public void onAudioSessionId(EventTime eventTime, int audioSessionId) {

            }


            @Override
            public void onAudioUnderrun(EventTime eventTime, int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {

            }

            @Override
            public void onDroppedVideoFrames(EventTime eventTime, int droppedFrames, long elapsedMs) {

            }

            @Override
            public void onVideoSizeChanged(EventTime eventTime, int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

            }

            @Override
            public void onRenderedFirstFrame(EventTime eventTime, Surface surface) {

            }

            @Override
            public void onDrmKeysLoaded(EventTime eventTime) {

            }

            @Override
            public void onDrmSessionManagerError(EventTime eventTime, Exception error) {

            }

            @Override
            public void onDrmKeysRestored(EventTime eventTime) {

            }

            @Override
            public void onDrmKeysRemoved(EventTime eventTime) {

            }
        });

                PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.setReferenceCounted(false);


        IntentFilter iF = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        /**
         * Assign the intent filter a high priority so this receiver
         * gets called first on a button press which can then determine
         * whether to pass it down to other apps or not (i.e. the Music app)
         *
         * Initially I found the SYSTEM_HIGH_PRIORITY didn't work, lastfm seemed
         * to get first request of the button press, then the music player and
         * this example didn't get access to it.
         *
         * So while +1 works, its extremely easy to break (i.e. another developer uses
         * SYSTEM_HIGH_PRIORITY + 1 then you may not get access to the button event).
         *
         * Also the Document says to use a value between SYSTEM_LOW_PRIORITY and
         * SYSTEM_HIGH_PRIORITY (i.e. -1000 & 1000)
         */
        iF.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY + 1);
        // register the receiver
        registerReceiver(mButtonReceiver, iF);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        try {
            String action = intent.getAction();
            switch (action) {

                case ACTION_PLAY:
                    startNewSong();
                    break;
                case ACTION_TOGGLE:
                    togglePlay();
                    break;
                case ACTION_STOP:
                    stop(intent);
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    private void startNewSong() {
        isNewSong = true;
        MainActivity.rl_loading.setVisibility(View.VISIBLE);
        MainActivity.play.setVisibility(View.INVISIBLE);
        String url = "";
        try {
            if (BuildConfig.APPLICATION_ID.equals(Setting.itemAbout.getPackage_name())){
                url = Constant.Url_fm;
            }

            final String finalUrl = url;

            MediaSource mediaSource;

            if (finalUrl.endsWith("_Other")) {
                finalUrl.replace("_Other", "");
            }

            dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), "nemosofts"), bandwidthMeter);
            if (finalUrl.endsWith(".m3u8") || finalUrl.endsWith(".M3U8")) {
                mediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                        .setAllowChunklessPreparation(false)
                        .setExtractorFactory(
                                new DefaultHlsExtractorFactory())
                        .createMediaSource(Uri.parse(finalUrl));
            }else {
                mediaSource = new ExtractorMediaSource(Uri.parse(finalUrl),
                        dataSourceFactory, extractorsFactory, null, null);
            }

            exoPlayer.prepare(mediaSource);

            exoPlayer.setPlayWhenReady(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void togglePlay() {
        if (exoPlayer.getPlayWhenReady()) {
            exoPlayer.setPlayWhenReady(false);
        } else {
            exoPlayer.setPlayWhenReady(true);
        }
        updateNotiPlay(exoPlayer.getPlayWhenReady());
    }

    private void onCompletion() {
        startNewSong();
    }



    private void stop(Intent intent) {
        if(Setting.isPlayed){
            MainActivity.play.setBackgroundResource(R.drawable.ic_play_arrow_white_24dp);
        }
        try {
            Setting.isPlayed = false;
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
            try {
                mAudioManager.abandonAudioFocus(focusChangeListener);
                mAudioManager.unregisterMediaButtonEventReceiver(componentName);
                unregisterReceiver(onHeadPhoneDetect);
            } catch (Exception e) {
                e.printStackTrace();
            }
            stopService(intent);
            stopForeground(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNoti() {
        smallViews = new RemoteViews(getPackageName(), R.layout.layout_noti_small);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.putExtra("isnoti", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent playIntent = new Intent(this, PlayerService.class);
        playIntent.setAction(ACTION_TOGGLE);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);



        Intent closeIntent = new Intent(this, PlayerService.class);
        closeIntent.setAction(ACTION_STOP);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        String NOTIFICATION_CHANNEL_ID = "nemosofts_ch_1";
        notification = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name))
                .setPriority(Notification.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker(getString(R.string.app_name))
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setOnlyAlertOnce(true);

        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(mChannel);

            MediaSessionCompat mMediaSession;
            mMediaSession = new MediaSessionCompat(getApplicationContext(), getString(R.string.app_name));
            mMediaSession.setFlags(
                    MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

            notification.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mMediaSession.getSessionToken())
                    .setShowCancelButton(true)
                    .setShowActionsInCompactView(0, 1)
                    .setCancelButtonIntent(
                            MediaButtonReceiver.buildMediaButtonPendingIntent(getApplicationContext(), PlaybackStateCompat.ACTION_STOP)))
                    .addAction(new NotificationCompat.Action(
                            R.drawable.ic_pause_white_24dp, "Pause",
                            pplayIntent))
                    .addAction(new NotificationCompat.Action(
                            R.drawable.ic_close_white_24dp, "Close",
                            pcloseIntent));

            notification.setContentTitle(getString(R.string.app_name));
            notification.setContentText(Constant.is_playing_in_background);
        } else {
            smallViews.setOnClickPendingIntent(R.id.imageView_noti_play, pplayIntent);

            smallViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

            smallViews.setImageViewResource(R.id.imageView_noti_play, R.drawable.ic_pause_white_24dp);

            smallViews.setTextViewText(R.id.status_bar_track_name, getString(R.string.app_name));

            smallViews.setTextViewText(R.id.status_bar_artist_name, Constant.is_playing_in_background);

            notification.setCustomContentView(smallViews).setCustomBigContentView(smallViews);
        }

        startForeground(101, notification.build());
        updateNotiPlay(exoPlayer.getPlayWhenReady());
    }





    @SuppressLint("RestrictedApi")
    private void updateNotiPlay(Boolean isPlay) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification.mActions.remove(0);
                Intent playIntent = new Intent(this, PlayerService.class);
                playIntent.setAction(ACTION_TOGGLE);
                PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                if (isPlay) {
                    notification.mActions.add(0, new NotificationCompat.Action(
                            R.drawable.ic_pause_white_24dp, "Pause",
                            ppreviousIntent));

                    MainActivity.play.setBackgroundResource(R.drawable.ic_pause_white_24dp);

                } else {
                    notification.mActions.add(0, new NotificationCompat.Action(
                            R.drawable.ic_play_arrow_white_24dp, "Play",
                            ppreviousIntent));
                    MainActivity.play.setBackgroundResource(R.drawable.ic_play_arrow_white_24dp);
                }
            } else {
                if (isPlay) {
                    smallViews.setImageViewResource(R.id.imageView_noti_play, R.drawable.ic_pause_white_24dp);
                    MainActivity.play.setBackgroundResource(R.drawable.ic_pause_white_24dp);
                } else {
                    smallViews.setImageViewResource(R.id.imageView_noti_play, R.drawable.ic_play_arrow_white_24dp);
                    MainActivity.play.setBackgroundResource(R.drawable.ic_play_arrow_white_24dp);
                }
            }

            mNotificationManager.notify(101, notification.build());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED) {
            onCompletion();
        }
        if (playbackState == Player.STATE_READY && playWhenReady) {
            if (isNewSong) {
                isNewSong = false;

                MainActivity.rl_loading.setVisibility(View.INVISIBLE);
                MainActivity.play.setVisibility(View.VISIBLE);


                Setting.isPlayed = true;
                if (notification == null) {
                    createNoti();
                }
            } else {
                updateNotiPlay(exoPlayer.getPlayWhenReady());
            }
        }

        if(playWhenReady) {
            if(!mWakeLock.isHeld()) {
                mWakeLock.acquire(60000);
            }
        } else {
            if(mWakeLock.isHeld()) {
                mWakeLock.release();
            }
        }
    }



    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    BroadcastReceiver onHeadPhoneDetect = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (exoPlayer.getPlayWhenReady()) {
                    togglePlay();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    };


    private AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) :
                    // Lower the volume while ducking.
                    exoPlayer.setVolume(0.2f);
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) :
                    togglePlay();
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS) :
                    togglePlay();
                    break;
                case (AudioManager.AUDIOFOCUS_GAIN) :
                    exoPlayer.setVolume(1f);
                    togglePlay();
                    break;
                default: break;
            }
        }
    };



    @Override
    public void onDestroy() {

        try {
            if(mWakeLock.isHeld()) {
                mWakeLock.release();
            }
            exoPlayer.stop();
            exoPlayer.release();
//            exoPlayer.removeListener(eventListener);

            try {
                mAudioManager.abandonAudioFocus(focusChangeListener);
                unregisterReceiver(onHeadPhoneDetect);
                mAudioManager.unregisterMediaButtonEventReceiver(componentName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public void onPrevButtonPress() {

    }

    @Override
    public void onNextButtonPress() {

    }

    @Override
    public void onPlayPauseButtonPress() {
        togglePlay();
    }

    public int getAudioSessionId() {
        return exoPlayer.getAudioSessionId();
    }
}