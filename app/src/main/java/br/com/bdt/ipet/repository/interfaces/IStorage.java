package br.com.bdt.ipet.repository.interfaces;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public interface IStorage {
    Task<Uri> saveImg(String path, Uri uri,String id);
    Task<Void> deleteImg(String path, String url);
}
