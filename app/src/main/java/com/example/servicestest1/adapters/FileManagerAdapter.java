package com.example.servicestest1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicestest1.R;

import java.io.File;
import java.util.List;

public class FileManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_FILE = 1;
    private static final int TYPE_FOLDER = 2;

    private Context mContext;
    private List<File> mFiles;

    public FileManagerAdapter(Context context, List<File> files) {
        mContext = context;
        mFiles = files;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FILE) {
            View fileView = LayoutInflater.from(mContext).inflate(R.layout.explorer_file_item, parent, false);
            return new FileViewHolder(fileView);
        } else {
            View folderView = LayoutInflater.from(mContext).inflate(R.layout.explorer_folder_item, parent, false);
            return new FolderViewHolder(folderView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        File file = mFiles.get(position);
        if (getItemViewType(position) == TYPE_FILE) {
            ((FileViewHolder) holder).tvFileName.setText(file.getName());
        } else {
            ((FolderViewHolder) holder).tvFolderName.setText(file.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    @Override
    public int getItemViewType(int position) {
        File file = mFiles.get(position);
        if (file.isDirectory()) {
            return TYPE_FOLDER;
        } else {
            return TYPE_FILE;
        }
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFile;
        TextView tvFileName;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFile = itemView.findViewById(R.id.iv_file);
            tvFileName = itemView.findViewById(R.id.tv_file_name);
        }
    }

    public static class FolderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFolder;
        TextView tvFolderName;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFolder = itemView.findViewById(R.id.iv_folder);
            tvFolderName = itemView.findViewById(R.id.tv_folder_name);
        }
    }
}