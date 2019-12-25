package com.sustainablefood.com.Adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sustainablefood.com.Objects.Notification;
public abstract class NotificationViewHolder extends RecyclerView.ViewHolder {
    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
    }


    public abstract void setDetails(final Context mContext, final Notification notification);
}
