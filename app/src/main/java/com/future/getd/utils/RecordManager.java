package com.future.getd.utils;

import android.media.MediaRecorder;
import android.text.format.DateFormat;

import com.future.getd.log.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class RecordManager {
    MediaRecorder mMediaRecorder;
    String filePath = "";
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
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            String fileName = DateFormat.format("yyyy-MM-dd_HH:mm:ss", Calendar.getInstance()) + ".m4a";
//            String fileName = DateFormat.format("yyyy-MM-dd_HH:mm:ss", Calendar.getInstance()) + ".wav";
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
        } catch (IllegalStateException e) {
//            LogUtil.i("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
//            LogUtil.i("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        };
    }

    public String stopRecord() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
//            filePath = "";
        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
//            File file = new File(filePath);
//            if (file.exists())
//                file.delete();
//
//            filePath = "";
        }
        return filePath;
    }
}
