package com.example.servicestest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.servicestest1.adapters.FileManagerAdapter;

public class FileManager extends AppCompatActivity {
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;
    private static final int REQUEST_CODE_OPEN_DIRECTORY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        if (!checkPermissionForReadExtertalStorage(this)) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            try {
                requestPermissionForReadExtertalStorage(this);
            } catch (Exception e) {
                Log.e("files", "Permission denied to read files and folders");
            }
        } else {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            openDirectoryPicker();
        }

    }

    public boolean checkPermissionForReadExtertalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage(Context context) {
        try {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void openDirectoryPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_CODE_OPEN_DIRECTORY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Get the URI of the selected directory
                String directoryUriString = data.getDataString();
                Log.d("TAG", "Selected Directory URI: " + directoryUriString);

                // Convert the URI to a path
                String directoryPath = getDirectoryPathFromUri(Uri.parse(directoryUriString));
                Log.d("TAG", "Selected Directory Path: " + directoryPath);

                // Now you can access the directory using directoryPath
                // Do whatever you need with the selected directory
            }
        }
    }

    private String getDirectoryPathFromUri(Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // For SAF documents
            String documentId = DocumentsContract.getDocumentId(uri);
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                String[] split = documentId.split(":");
                if (split.length >= 2 && "primary".equalsIgnoreCase(split[0])) {
                    path = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
        } else {
            // For regular file URIs
            path = uri.getPath();
        }
        return path;
    }
}