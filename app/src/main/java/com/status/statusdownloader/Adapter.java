package com.status.statusdownloader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    public static final int DOWNLOAD=1;
    public static final int STATUS=0;
    Context context;
    ArrayList<File> arrayList;
    int fragment;
    FileOutputStream fileOutputStream;
    FileInputStream fileInputStream;

    public Adapter(ArrayList<File> arrayList, int fragment) {
        this.arrayList = arrayList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_row_files,viewGroup,false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        final File file=arrayList.get(i);
        if(fragment==DOWNLOAD){
            holder.btnDownload.setImageResource(R.drawable.ic_delete_black_24dp);
            holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    file.delete();
                    arrayList.remove(file);
                    notifyItemRemoved(i);
                    Toast.makeText(context,file.getName()+" deleted successfully",Toast.LENGTH_SHORT).show();
                }
            });
            // on click listener
        }else{
            holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyFileOrDirectory(file.getPath(),"/storage/emulated/0/Status-Downloader");
                    Toast.makeText(context,file.getName()+" downloaded successfully",Toast.LENGTH_SHORT).show();
                }
            });
        }
        holder.tvName.setText(file.getName());
        Worker worker=new Worker(holder.imageView,file.getPath());
        worker.execute();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PlayerActivity.class);
                if(fragment==DOWNLOAD){
                    intent.putExtra("status",false);
                }
                intent.putExtra("path",file.getPath());
                intent.putExtra("name",file.getName());
                context.startActivity(intent);
            }
        });
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PlayerActivity.class);
                intent.putExtra("path",file.getPath());
                if(fragment==DOWNLOAD){
                    intent.putExtra("status",false);
                } intent.putExtra("name",file.getName());
                context.startActivity(intent);
            }
        });
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=null;
                if(fragment==DOWNLOAD){
                    uri=Uri.fromFile(file);
                }else{
                    copyFileOrDirectory(file.getPath(),"/storage/emulated/0/Status-Downloader");
                    uri = Uri.parse("/storage/emulated/0/Status-Downloader/"+file.getName());
                }
                Intent videoshare = new Intent(Intent.ACTION_SEND);
                videoshare.setType("*/*");
                videoshare.setPackage("com.whatsapp");
                videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                videoshare.putExtra(Intent.EXTRA_STREAM,uri);
                videoshare.putExtra(Intent.EXTRA_TEXT,"Download status downloader app  https://play.google.com/store/apps/details?id=com.status.statusdownloader ");
                context.startActivity(videoshare);
            }
        });
    }




    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageButton btnShare,btnDownload,btnPlay;
        TextView tvName;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            btnDownload=itemView.findViewById(R.id.btnDownload);
            btnPlay=itemView.findViewById(R.id.btnPlay);
            btnShare=itemView.findViewById(R.id.btnShare);
            tvName=itemView.findViewById(R.id.tvName);
        }
    }
    public void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
