package com.future.getd.ui.adapter;


import android.content.Context;
import android.media.Image;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.future.getd.R;
import com.future.getd.databinding.ItemChatGptBinding;
import com.future.getd.databinding.ItemChatUserBinding;
import com.future.getd.net.model.Message;
import com.future.getd.utils.SpeechUtils;

import java.util.HashMap;
import java.util.List;

public class ChatAdapter extends BaseQuickAdapter<Message, ChatAdapter.viewHolder> {
    HashMap<Integer,Boolean> speakStatus = new HashMap<>();
    public ChatAdapter(@NonNull List<? extends Message> items) {
        super(items);
        for (int i = 0; i < items.size(); i++) {
            speakStatus.put(i,false);
        }
    }
    int TYPE_SEND = 0;
    int TYPE_SYSTEM = 1;
    int lastSpeakIndex = -1;

    @Override
    protected int getItemViewType(int position, @NonNull List<? extends Message> list) {
        if(getItem(position).role.equals(Message.ROLE_USER)){
            return TYPE_SEND;
        }else{
            return TYPE_SYSTEM;
        }
    }

    @NonNull
    @Override
    protected viewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_SEND/*getItemViewType(position) == TYPE_SEND*/){
            ItemChatUserBinding userBinding = ItemChatUserBinding.inflate(LayoutInflater.from(context),parent,false);
            viewHolder viewHolder = new viewHolder(userBinding.getRoot(), userBinding,null);
            return viewHolder;
        }else{
            ItemChatGptBinding gptBinding = ItemChatGptBinding.inflate(LayoutInflater.from(context),parent,false);
            viewHolder viewHolder = new viewHolder(gptBinding.getRoot(),null, gptBinding);
            return viewHolder;
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder viewHolder, int i, @Nullable Message message) {
        if(message.role.equals(Message.ROLE_USER)){
            if(viewHolder.tvUserContent != null)
            viewHolder.tvUserContent.setText(message.content);
        }else{
            if(viewHolder.tvGptContent != null)
            viewHolder.tvGptContent.setText(message.content);
            int position = viewHolder.getBindingAdapterPosition();
            boolean isSpeaking = false;
            if(!speakStatus.isEmpty() && speakStatus.size() > position){
                isSpeaking = speakStatus.get(position);
            }
            viewHolder.play.setImageResource(isSpeaking ? R.drawable.ic_speech_pause : R.drawable.ic_play);
            boolean finalIsSpeaking = isSpeaking;
            viewHolder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalIsSpeaking){
                        SpeechUtils.getInstance().stopSpeak();
                    }else{
                        if(lastSpeakIndex != -1 && lastSpeakIndex != position){
                            speakStatus.put(lastSpeakIndex,false);
                            notifyItemChanged(lastSpeakIndex);
                        }
                        SpeechUtils.getInstance().setListen(new UtteranceProgressListener() {
                            @Override
                            public void onStart(String utteranceId) {

                            }

                            @Override
                            public void onDone(String utteranceId) {

                            }

                            @Override
                            public void onError(String utteranceId) {

                            }
                        });
                        SpeechUtils.getInstance().speak(message.content,SpeechUtils.TYPE_DEFAULT);
                    }
                    speakStatus.put(position,!finalIsSpeaking);
                    notifyItemChanged(position);
                    lastSpeakIndex = position;
                }
            });
        }
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ImageView ivType;
        TextView tvUserContent;
        TextView tvGptContent;
        ImageView play;
        public viewHolder(@NonNull View itemView,ItemChatUserBinding vbUser,ItemChatGptBinding vbGpt) {
            super(itemView);
            if (vbUser != null) {
                tvUserContent = vbUser.tvContent;
            }
            if (vbGpt != null) {
                tvGptContent = vbGpt.tvContent;
                play = vbGpt.play;
            }
        }
    }
}
