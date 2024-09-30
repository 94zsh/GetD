package com.future.getd.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.text.format.DateFormat;

import com.future.getd.log.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

//"message": "Invalid file format. Supported formats: ['flac', 'm4a', 'mp3', 'mp4', 'mpeg', 'mpga', 'oga', 'ogg', 'wav', 'webm']",
public class RecordManager {
    MediaRecorder mMediaRecorder;
    String filePath = "";
    boolean isRecording = false;
    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
//            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
//            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//            String fileName = DateFormat.format("yyyy-MM-dd_HH:mm:ss", Calendar.getInstance()) + ".m4a";
            String fileName = DateFormat.format("yyyy-MM-dd_HH:mm:ss", Calendar.getInstance()) + ".wav";
            String audioSaveDir = SdCardUtil.getSdCardPath(SdCardUtil.SAVE_PATH_AUDIO);
            LogUtils.i("audioSaveDir : " + audioSaveDir);
//            if (!FileUtils.isFolderExist(audioSaveDir)){
//                FileUtils.makeFolders(audioSaveDir);
//            }
            filePath = audioSaveDir + fileName;
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            isRecording = true;
        } catch (IllegalStateException e) {
//            LogUtil.i("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
//            LogUtil.i("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        };
    }


    private AudioManager audioManager;
    public void startRecordVoiceRecognition(Context context) {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);// 设置
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
//            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//            String fileName = DateFormat.format("yyyy-MM-dd_HH:mm:ss", Calendar.getInstance()) + ".m4a";
            String fileName = DateFormat.format("yyyy-MM-dd_HH:mm:ss", Calendar.getInstance()) + ".wav";
            String audioSaveDir = SdCardUtil.getSdCardPath(SdCardUtil.SAVE_PATH_AUDIO);
            LogUtils.i("audioSaveDir : " + audioSaveDir);
//            if (!FileUtils.isFolderExist(audioSaveDir)){
//                FileUtils.makeFolders(audioSaveDir);
//            }
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            audioManager.startBluetoothSco();
            audioManager.setBluetoothScoOn(true);
//            audioManager.setSpeakerphoneOn(false);
            filePath = audioSaveDir + fileName;
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            isRecording = true;
        } catch (IllegalStateException e) {
//            LogUtil.i("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
//            LogUtil.i("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        };
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void deleteFile(){
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
        }
        filePath = "";
    }

    public String getFilePath(){
        return filePath;
    }
    public String stopRecord() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
//            filePath = "";
            if(audioManager != null){
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.stopBluetoothSco();
                audioManager.setBluetoothScoOn(false);
                audioManager.setSpeakerphoneOn(true);
            }
        } catch (RuntimeException e) {
            if(mMediaRecorder != null){
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
//            File file = new File(filePath);
//            if (file.exists())
//                file.delete();
//
//            filePath = "";
        }
        isRecording = false;
        return filePath;
    }

    public void enableNoiseSuppressor(Boolean enable){
        LogUtils.i("是否支持噪音抑制功能：" + NoiseSuppressor.isAvailable());
        if (NoiseSuppressor.isAvailable()) {
//            NoiseSuppressor noise = NoiseSuppressor.create(mMediaRecorder.getLogSessionId());
//            if (noise != null && !noise.getEnabled()) {
//                //用该噪声抑制器的启用方法，将噪声抑制功能设置为打开状态
//                noise.setEnabled(true);
//            }
        }
    }

    public String stopRecordAndDelete() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
//            filePath = "";
        } catch (RuntimeException e) {
            if(mMediaRecorder != null){
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
            deleteFile();
        }
        isRecording = false;
        return filePath;
    }
}
