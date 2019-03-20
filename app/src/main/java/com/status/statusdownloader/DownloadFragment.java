package com.status.statusdownloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;

public class DownloadFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_downloaded,container,false);
    }
    ArrayList<File> arrayList;
    RecyclerView recyclerView;
    Adapter adapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycler);
        arrayList=new ArrayList<>();
        adapter=new Adapter(arrayList,1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        checkUserPermission();
    }

    private void checkUserPermission(){
        if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
         ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},123);
        }
        else{
            loadSong();
        }
    }

    private void loadSong() {
    try{
        File file = new File(Environment.getExternalStorageDirectory(), "Status-Downloader");
        File[] pictures = file.listFiles();
        load(pictures);
    }catch (Exception e){
        e.printStackTrace();
    }
    }

    private void load(File[] pictures) {
        for (File file:pictures) {
            String mimeType = URLConnection.guessContentTypeFromName(file.getPath());
            if( mimeType != null && mimeType.startsWith("video")) {
                arrayList.add(file);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayList.clear();
        loadSong();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==123){
            loadSong();
        }

    }
}
