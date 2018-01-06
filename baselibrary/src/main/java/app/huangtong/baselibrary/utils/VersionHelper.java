package app.huangtong.baselibrary.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * 版本升级助手
 *
 * @author andy.gao
 */
public class VersionHelper {

    private static long downloadID;
    public String fileName;
    public Context context;

    public VersionHelper(Context context, String fileName) {
        this.fileName = fileName;
        this.context = context;
    }

    /**
     * 获取当前app版本号
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null){
            return packageInfo.versionCode;
        } else {
            return -1;
        }
    }

    /**
     * 获取版本号
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null){
            return packageInfo.versionName;
        } else {
            return "";
        }
    }

    /**
     * 获取应用信息
     *
     * @return 当前应用的 PackageInfo
     */
    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 下载APK=
     *
     * @param context context
     * @param filePath 下载路径
     * @param fileName  下载至本地的文件名
     */
    public  void downloadApk(Context context, String filePath, String fileName) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(filePath));

        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        //通知栏显示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


        request.setVisibleInDownloadsUi(true);
        request.setTitle(fileName);
//        request.setDescription(filePath);
        request.setDescription("正在下载中...");
        request.setVisibleInDownloadsUi(true);
        /*
         * 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错,
         * 下载后的文件在/mnt/sdcard/Android/data/packageName/files目录下面
         * 不设置，下载后的文件在/cache这个目录下面
         */
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        registerReceiver(context);

        //TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
        downloadID = downloadManager.enqueue(request);
    }


    /**
     * 注册（下载完成）广播监听器
     *
     * @param context context
     */
    private  void registerReceiver(Context context) {
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(completeReceiver, dynamic_filter);
    }

    /**
     * 注销广播监听器
     *
     * @param context context
     */
    private  void unRegisterReceiver(Context context) {
        context.unregisterReceiver(completeReceiver);
    }

    /**
     * 安装apk
     *
     * @param context context
     * @param apkUrl apkUrl
     */
    private  void installAPK(Context context, String apkUrl) {
        File apkFile =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.parse("file://" + apkUrl), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 下载完成监听器
     */
    private BroadcastReceiver completeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())
                    && downloadID == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)) {
                Log.d("VersionHelper", "下载完成。。。");
                unRegisterReceiver(context);

                DownloadManager.Query myDownloadQuery = new DownloadManager.Query();
                myDownloadQuery.setFilterById(downloadID);
                /**查询正在等待、运行、暂停、成功、失败状态的下载任务**/
                myDownloadQuery.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);


                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor myDownload = downloadManager.query(myDownloadQuery);

                Cursor cursor = downloadManager.query(myDownloadQuery);
                if (cursor.moveToFirst()) {
                    int fileUriId = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    /**过时的方式：DownloadManager.COLUMN_LOCAL_FILENAME**/
                    int fileNameId = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);

                    String fileUri = cursor.getString(fileUriId);
                    String fileName = null;

                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (status) {
                        //下载暂停
                        case DownloadManager.STATUS_PAUSED:
                            break;
                        //下载延迟
                        case DownloadManager.STATUS_PENDING:
                            break;
                        //正在下载
                        case DownloadManager.STATUS_RUNNING:
                            break;
                        //下载完成
                        case DownloadManager.STATUS_SUCCESSFUL:
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                //7.0时候
                                fileName = Uri.parse(fileUri).getPath();
                            } else {
                                fileName = cursor.getString(fileNameId);

                            }
                            installAPK(context, fileName);
                            break;
                        //下载失败
                        case DownloadManager.STATUS_FAILED:
                            Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                            break;
                    }


                }
//                if (myDownload.moveToFirst()) {
//                    int fileNameIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
//                    int fileUriIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
//
//                    String fileName = myDownload.getString(fileNameIdx);
//                    String fileUri = myDownload.getString(fileUriIdx);
//
//                    Log.d("VersionHelper", fileName + " : " + fileUri);
//                    installAPK(context, fileName);
//                }
                myDownload.close();
            }
        }
    };

}
