package com.qgqaqgqa.deviceinfomanager.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.Util;
import com.qgqaqgqa.deviceinfomanager.R;
import com.qgqaqgqa.deviceinfomanager.base.BaseActivity;
import com.qgqaqgqa.deviceinfomanager.dialog.MyProgressDialog;
import com.qgqaqgqa.deviceinfomanager.finals.AppConfig;
import com.qgqaqgqa.deviceinfomanager.intf.SelectImageListener;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.constant.PermissionConstants;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.LogUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.PermissionUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.ToastUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.bumptech.glide.Glide.with;

/**
 * Created by WangXY on 2015/11/10.14:36.
 * 图片工具类 包括网络以及本地图片加载  图片裁剪  图片保存等图片相关功能
 */
public class ImageUtil {
    /**
     * 是否要裁剪图片
     */
    public static boolean isNeedCrop = true;
    /**
     * 内存图片软引用缓冲
     */
    private static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

    /**
     * 录音
     */
    public static final int DATA_WITH_SOUND = 0x5;//录音
    /**
     * 录像
     */
    public static final int DATA_WITH_VIDEO = 0x6;//录像
    /**
     * 拍照
     */
    public static final int DATA_WITH_CAMERA = 0x7;//拍照
    /**
     * 拍照并裁剪
     */
    public static final int DATA_WITH_CAMERA_CROP = 0x10;//拍照并裁剪
    /**
     * 从相册中选择照片不裁剪
     */
    public static final int DATA_WITH_PHOTO_PICKED = 0x8;//从相册中选择
    /**
     * 从相册中选择照片裁剪
     */
    public static final int DATA_WITH_PHOTO_PICKED_CROP = 0x9;//从相册中选择并裁剪
    public static final int REQUEST_TAKE_PHOTO_PERMISSION = 0x99;
    private static Dialog dialog;

    public static String hasImageFile(final String imageURL) {
//        if (Utility.isSDcardOK()) {
//            /**
//             *加上一个对本地缓存的查找
//             */
//            File cacheDir = new File(AppConfig.DIR_IMG);
//            File[] cacheFiles = cacheDir.listFiles();
//            String imgName = imageURL;
//            if (imageURL.lastIndexOf("/") > -1) {
//                imgName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
//            }
//            int i = 0;
//            if (null != cacheFiles) {
//                for (; i < cacheFiles.length; i++) {
//                    if (imgName.equals(cacheFiles[i].getName())) {
//                        break;
//                    }
//                }
//                if (i < cacheFiles.length) {
//                    return AppConfig.DIR_IMG + File.separator + imgName;
//                }
//            }
//            final String finalImgName = imgName;
//            new Thread() {
//                @Override
//                public void run() {
//                    save(imageURL, AppConfig.DIR_IMG + File.separator + finalImgName);
//                }
//            }.start();
//        }
        return imageURL;
    }

    public void loadBitmap(final ImageView imageView, final String imageURL, String imgName) {
        if (Util.isOnMainThread())

            //在内存缓存中，则返回Bitmap 对象
            if (imageCache.containsKey(imgName)) {
                SoftReference<Bitmap> reference = imageCache.get(imgName);
                Bitmap bitmap = reference.get();
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            } else {
                /**
                 *加上一个对本地缓存的查找
                 */
//            String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
//            File file = new File(AppConfig.DIR_IMG + File.separator+imgName);
////                    + "temp.jpg");
                File cacheDir = new File(AppConfig.DIR_IMG);
                File[] cacheFiles = cacheDir.listFiles();
                int i = 0;
                if (null != cacheFiles) {
                    for (; i < cacheFiles.length; i++) {
                        if (imgName.equals(cacheFiles[i].getName())) {
                            break;
                        }
                    }
                    if (i < cacheFiles.length) {
                        imageView.setImageBitmap(BitmapFactory.decodeFile(AppConfig.DIR_IMG + File.separator + imgName));
                        return;
                    }
                }
                save(imageURL, AppConfig.DIR_IMG + File.separator + imgName);

            }
//        final Handler handler = new Handler() {
//            /*(non-Javadoc)
//            *@seeandroid.os.Handler#handleMessage(android.os.Message)
//            */
//            @Override
//            public void handleMessage(Message msg) {
//                imageCallBack.imageLoad(imageView, (Bitmap) msg.obj);
//            }
//        };
//        // 如果不在内存缓存中，也不在本地（被jvm回收掉），则开启线程下载图片
//        new Thread() {
//            /*(non-Javadoc)
//            *@seejava.lang.Thread#run()
//            */
//            @Override
//            public void run() {
//                InputStream bitmapIs=new InputStream() {
//                    @Override
//                    public int read() throws IOException {
//                        return 0;
//                    }
//                };
////                InputStream bitmapIs = HttpUtils.getStreamFromURL(imageURL);
//                Bitmap bitmap = BitmapFactory.decodeStream(bitmapIs);
//                imageCache.put(imageURL, new SoftReference<>(bitmap));
//                Message msg = handler.obtainMessage(0, bitmap);
//                handler.sendMessage(msg);
//                File dir = new File("/mnt/sdcard/test/");
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//                File bitmapFile = new File("/mnt/sdcard/test/" +
//                        imageURL.substring(imageURL.lastIndexOf("/") + 1));
//                if (!bitmapFile.exists()) {
//                    try {
//                        bitmapFile.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                FileOutputStream fos;
//                try {
//                    fos = new FileOutputStream(bitmapFile);
//                    bitmap.compress(Bitmap.CompressFormat.PNG,
//                            100, fos);
//                    fos.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//        return;
    }

//    public interface ImageCallBack {
//        public void imageLoad(ImageView imageView, Bitmap bitmap);
//    }

    /**
     * 加载图片不包含监听事件
     *
     * @param context
     * @param imageview
     * @param url
     * @param mDefaultImageId
     */
    public static void loadBlurImage(BaseActivity context, ImageView imageview, String url, int mDefaultImageId, int mErrorImageId) {
        if (Util.isOnMainThread())
//        FutureTarget<File> future = Glide.with(context)
//                .load(url)
//                .downloadOnly(500, 500);
//        try {
//            File cacheFile = future.get();
//            MyLogUtil.e("cacheFile",cacheFile.getAbsolutePath());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
            with(context)
                    .load(url)//设值图片加载链接
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(mDefaultImageId)//设置未显示图片时的加载图片
                    .error(mErrorImageId)//设置图片无法显示时的图片
//                    .crossFade(200)//设置图片显示时的动画效果
                    .centerCrop()
                    .transform(new GlideBlurTransform())
                    .into(imageview);//设置显示imageview
    }

    /**
     * 加载头像
     * 加载图片不包含监听事件
     *
     * @param context
     * @param imageview
     * @param url
     * @param mDefaultImageId
     * @param mLoadImageRadius
     */
    public static void loadImage(BaseActivity context, ImageView imageview, String url, int mDefaultImageId, int mLoadImageRadius) {
        if (Util.isOnMainThread())
//        FutureTarget<File> future = Glide.with(context)
//                .load(url)
//                .downloadOnly(500, 500);
//        try {
//            File cacheFile = future.get();
//            MyLogUtil.e("cacheFile",cacheFile.getAbsolutePath());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
            with(context)
                    .load(url)//设值图片加载链接
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(mDefaultImageId)//设置未显示图片时的加载图片
                    .error(mDefaultImageId)//设置图片无法显示时的图片
//                    .crossFade(200)//设置图片显示时的动画效果
                    .centerCrop()
                    .transform(new GlideRoundTransform(mLoadImageRadius))
                    .into(imageview);//设置显示imageview
    }

    /**
     * 加载头像
     * 加载图片不包含监听事件
     *
     * @param context
     * @param imageview
     * @param url
     * @param mDefaultImageId
     * @param mLoadImageRadius
     */
    public static void loadImage(BaseActivity context, ImageView imageview, String url,
                                 int mDefaultImageId, int mLoadImageRadius, int width,
                                 int height) {
        if (Util.isOnMainThread())
//        FutureTarget<File> future = Glide.with(context)
//                .load(url)
//                .downloadOnly(500, 500);
//        try {
//            File cacheFile = future.get();
//            MyLogUtil.e("cacheFile",cacheFile.getAbsolutePath());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
            with(context)
                    .load(url)//设值图片加载链接
                    .override(width, height)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(mDefaultImageId)//设置未显示图片时的加载图片
                    .error(mDefaultImageId)//设置图片无法显示时的图片
//                    .crossFade(200)//设置图片显示时的动画效果
                    .centerCrop()
                    .transform(new GlideRoundTransform(mLoadImageRadius))
                    .into(imageview);//设置显示imageview
    }

    public static void loadImage(Activity context, String url, int mDefaultImageId, ImageView imageView) {
        if (Util.isOnMainThread())
//        FutureTarget<File> future = Glide.with(context)
//                .load(url)
//                .downloadOnly(500, 500);
//        try {
//            File cacheFile = future.get();
//            MyLogUtil.e("cacheFile",cacheFile.getAbsolutePath());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
            if (url == null || url.indexOf("http") != -1 || url.isEmpty()) {
                with(context)
                        .load(url)//设值图片加载链接
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.color.t00000000)//设置未显示图片时的加载图片
                        .error(mDefaultImageId)//设置图片无法显示时的图片
//                        .crossFade(200)//设置图片显示时的动画效果
                        .centerCrop()
                        .into(imageView);//设置显示imageview
            } else {
//            imageView.setImageBitmap(BitmapUtil.getimage(url));
                with(context)
                        .load(url)//设值图片加载链接
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.color.t00000000)//设置未显示图片时的加载图片
                        .signature(new ObjectKey(UUID.randomUUID().toString()))  // 重点在这行
                        .error(mDefaultImageId)//设置图片无法显示时的图片
//                        .crossFade(200)//设置图片显示时的动画效果
                        .centerCrop()
                        .into(imageView);//设置显示imageview
            }
    }

    //
    public static void loadImage(Activity context, String url, ImageView imageView) {
        if (Util.isOnMainThread())
//        final FutureTarget<File> future = Glide.with(context)
//                .load(url)
//                .downloadOnly(500, 500);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    File cacheFile = future.get();
//                    MyLogUtil.e("cacheFile",cacheFile.getAbsolutePath());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
            with(context)
                    .load(url)//设值图片加载链接
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.color.t00000000)//设置未显示图片时的加载图片
//                    .crossFade(200)//设置图片显示时的动画效果
                    .fitCenter()
                    .into(imageView);//设置显示imageview
    }

    public static void loadBitmap(Activity context, String url, SimpleTarget s) {
        if (Util.isOnMainThread())
//        FutureTarget<File> future = Glide.with(context)
//                .load(url)
//                .downloadOnly(500, 500);
//        try {
//            File cacheFile = future.get();
//            MyLogUtil.e("cacheFile",cacheFile.getAbsolutePath());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

            with(context).load(url)
//                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(s);
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadImageFitWidth(Context context, final String imageUrl, int mDefaultImageId, final ImageView imageView) {
        if (Util.isOnMainThread())
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (imageView == null) {
                                return false;
                            }
                            if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                            ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                            float scale = (float) vw / (float) resource.getIntrinsicWidth();
                            int vh = Math.round(resource.getIntrinsicHeight() * scale);
                            params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                            imageView.setLayoutParams(params);
                            return false;
                        }
                    })
                    .error(mDefaultImageId)
                    .into(imageView);
    }

//    /**
//     * 下载图片 获取bitmap
//     *
//     * @param context
//     * @param url
//     * @return
//     */
//    public static Bitmap getDiskCache(Activity context, String url) {
//        try {
//            return Glide.with(context)
//                    .load(url)
//                    .centerCrop()
//                    .into(new SimpleTarget<Drawable>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//
//                        }
//                    })
//                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


//    /**
//     * 包含监听事件的加载
//     *
//     * @param context
//     * @param imageview
//     * @param url
//     * @param mDefaultImageId
//     * @param mLoadImageRadius
//     * @param imageListener    加载监听事件
//     */
//    public static void loadImage(Activity context, ImageView imageview, String url,
//                                 int mDefaultImageId, int mLoadImageRadius, final LoadImageListener
//                                         imageListener) {
//        FutureTarget<File> future = with(context)
//                .load(url)
//                .downloadOnly(500, 500);
//        try {
//            File cacheFile = future.get();
//            LogUtils.e("cacheFile", cacheFile.getAbsolutePath());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        with(context)
//                .load(url)//设值图片加载链接
//                .placeholder(mDefaultImageId)//设置未显示图片时的加载图片
//                .error(mDefaultImageId)//设置图片无法显示时的图片
//                .crossFade(200)//设置图片显示时的动画效果
//                .centerCrop()
//                .transform(new GlideRoundTransform(context, mLoadImageRadius))
//                .into(new GlideDrawableImageViewTarget(imageview) {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                        imageListener.onLoadingStart();
//                    }
//
//                    @Override
//                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                        super.onLoadFailed(e, errorDrawable);
//                        imageListener.onLoadingFailed(e, errorDrawable);
//                    }
//
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                        super.onResourceReady(resource, animation);
//                        imageListener.onLoadingComplete(resource, animation);
//                    }
//
//                });//设置显示imageview
//    }

    /**
     * 拍照
     *
     * @param context
     * @param filename
     */
    public static void takePhoto(Activity context, String filename) {
        takePhoto(context, filename, true);
    }

    /**
     * 拍照
     *
     * @param context
     * @param filename
     */
    public static void takePhoto(final Activity context, final String filename, final Boolean crop) {
//        if (!PermissionUtils.isGranted(PermissionConstants.CAMERA,PermissionConstants.STORAGE)) {
//            PermissionUtils.permission(PermissionConstants.CAMERA,PermissionConstants.STORAGE).callback(new PermissionUtils.FullCallback() {
//                @Override
//                public void onGranted(List<String> permissionsGranted) {
//                    takePhoto(context, filename, crop);
//                }
//
//                @Override
//                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
//
//                }
//            }).request();
//        } else {
            isNeedCrop = crop;
            Intent intent = getIntent(DATA_WITH_CAMERA, getUri(context, AppConfig.DIR_IMG + File.separator + filename));
            if (intent != null) {
                try {
                    context.startActivityForResult(intent, DATA_WITH_CAMERA);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showLong("拍照功能不可用");
                }
            } else{
                ToastUtils.showLong("拍照功能不可用");
            }
//        }
    }
    /**
     * 录音
     *
     * @param context
     * @param filename
     */
    public static void takeSound(final Activity context, final String filename) {
        Intent intent = getIntent(DATA_WITH_SOUND, getUri(context, AppConfig.DIR_SOUND + File.separator + filename));
        if (intent != null) {
            try {
                context.startActivityForResult(intent, DATA_WITH_SOUND);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showLong("录像功能不可用");
            }
        } else
            ToastUtils.showLong("录像功能不可用");
    }

    /**
     * 录像
     *
     * @param context
     * @param filename
     */
    public static void takeVideo(final Activity context, final String filename) {
            Intent intent = getIntent(DATA_WITH_VIDEO, getUri(context, AppConfig.DIR_VIDEO + File.separator + filename));
            if (intent != null) {
                try {
                    context.startActivityForResult(intent, DATA_WITH_VIDEO);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showLong("录像功能不可用");
                }
            } else
                ToastUtils.showLong("录像功能不可用");
    }

    private static Uri getUri(Context context, String file) {
        return getUri(context, new File(file));
    }

    private static Uri getUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() +
                            ".fileprovider", file);
        } else {
            return Uri.fromFile(file);//"file://" + AppConfig.DIR_IMG + File.separator + filename));
        }
    }

    /**
     * 从相册中选择，不裁剪
     *
     * @param context
     * @param filename
     */
    public static void selectFromAlbumFull(Activity context, String filename) {
        Intent intent = getIntent(DATA_WITH_PHOTO_PICKED, getUri(context, AppConfig.DIR_IMG + File.separator + filename));
        if (intent != null) {
            try {
                context.startActivityForResult(intent, DATA_WITH_PHOTO_PICKED);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "无法获取图片", Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(context, "无法获取图片", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 从相册中选择图片，需要裁剪
     *
     * @param context
     */
    public static void selectFromAlbum(Activity context) {
        selectFromAlbum(context, true);
    }

    /**
     * 从相册中选择图片，需要裁剪
     *
     * @param context
     */
    public static void selectFromAlbum(final Activity context, final boolean crop) {
        if (!PermissionUtils.isGranted(PermissionConstants.STORAGE)) {
            PermissionUtils.permission(PermissionConstants.STORAGE).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                    selectFromAlbum(context, crop);
                }

                @Override
                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {

                }
            }).request();
        } else {
            isNeedCrop = crop;
            Intent intent = getIntent(DATA_WITH_PHOTO_PICKED);
            if (intent != null) {
                try {
                    context.startActivityForResult(intent, DATA_WITH_PHOTO_PICKED);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "无法获取图片", Toast.LENGTH_SHORT);
                }
            } else {
                Toast.makeText(context, "无法获取图片", Toast.LENGTH_SHORT);
            }
        }
    }


    /**
     * 获得跳转的Intent
     *
     * @param type
     * @param uri
     * @return
     */
    private static Intent getIntent(int type, Uri uri) {
        if (type == DATA_WITH_CAMERA) {//拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            return intent;
        }else if(type==DATA_WITH_VIDEO){//录像
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);    // MediaStore.EXTRA_VIDEO_QUALITY 表示录制视频的质量，从 0-1，越大表示质量越好，同时视频也越大
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30000);   // 设置视频录制的最长时间
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            return intent;
        }else if(type==DATA_WITH_SOUND){//录音
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            return intent;
        }
        return null;
    }


    private static Intent getIntent(int type) {
        if (type == DATA_WITH_PHOTO_PICKED) {//从相册选取
            Intent intent = null;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            intent.setType("image/*");
            return intent;
        }
        return null;
    }


//    /**
//     * @param uri
//     * @param xWeight
//     * @param yWeight
//     * @param width
//     * @param height
//     * @return
//     */
//    private static Intent getIntent(Uri uri, int xWeight, int yWeight, int width, int height) {
//        Intent intent = null;
//        if (Build.VERSION.SDK_INT < 19) {
//            intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//        } else {
//            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        }
//        intent.setType("image/*");
//        intent.putExtra("crop", "true");
//        if (xWeight > 0 && xWeight > 0) {
//            intent.putExtra("aspectX", xWeight);
//            intent.putExtra("aspectY", xWeight);
//        }
//        if (width > 0 && height > 0) {
//            intent.putExtra("outputX", width);
//            intent.putExtra("outputY", height);
//        }
//        intent.putExtra("return-data", true);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        return intent;
//    }

    /**
     * 裁剪图片
     *
     * @param context
     * @return
     * @Title cropImage
     * i=0是拍照，i=1是相册
     */

    public static Intent cropImage(Context context, Uri uri, Uri cropImage) {
        return cropImage(context, uri, cropImage, 1000, 1000);
    }

    /**
     * 裁剪图片
     *
     * @param context
     * @return
     * @Title cropImage
     * i=0是拍照，i=1是相册
     */

    public static Intent cropImage(Context context, Uri uri, Uri cropImage, int outputX, int outputY) {
        try {
            // 裁剪图片意图
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            }
            intent.setDataAndType(uri, "image/*");//类型
            intent.putExtra("crop", true);// 输出格式nb
//            intent.putExtra("aspectX", 1);// 裁剪比例
//            intent.putExtra("aspectY", 1);// 裁剪比例
            intent.putExtra("aspectX", outputX);// 裁剪比例
            intent.putExtra("aspectY", outputY);// 裁剪比例
            intent.putExtra("outputX", outputX);// 输出大小
            intent.putExtra("outputY", outputY);// 裁剪比例后输出比例
            intent.putExtra("scale", true);// 缩放
            intent.putExtra("scaleUpIfNeeded", true);// 如果小于要求输出大小，就放大
            intent.putExtra("return-data", false);// 不返回缩略图
            intent.putExtra("noFaceDetection", true);// 关闭人脸识别
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());// 输出文件

            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImage);
            return intent;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isPhotoCallBack(int requestCode) {
        return requestCode == DATA_WITH_CAMERA || requestCode == DATA_WITH_PHOTO_PICKED ||
                requestCode == DATA_WITH_PHOTO_PICKED_CROP;
    }

    public static String save(String url, String filename) {
        try {
            if (filename.lastIndexOf(".") > -1) {
                filename.substring(0, filename.lastIndexOf("."));
            }
            byte[] data = readInputStream(new URL(url).openStream());
            if (data.length == 0)
                return null;
            File filexists = new File(filename);
            filexists.createNewFile();
            FileOutputStream fos = new FileOutputStream(filexists);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                    data.length);
            if (filename.substring(filename.lastIndexOf(".") + 1).equals("png"))
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            else
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //压缩图片的宽高
    public static Bitmap getimage(String srcPath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);

        int w = options.outWidth;
        float ww = 640f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (options.outWidth / ww);
        }
        if (be <= 0)
            be = 1;
        // Calculate inSampleSize
        options.inSampleSize = be;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return reviewPicRotate(compressImage(BitmapFactory.decodeFile(srcPath, options)),
                srcPath);
    }

    //压缩图片的内存大小
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;//每次都减少10
//        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 获取图片文件的信息，是否旋转了90度，如果是则反转
     *
     * @param bitmap 需要旋转的图片
     * @param path   图片的路径
     */
    public static Bitmap reviewPicRotate(Bitmap bitmap, String path) {
        int degree = getPicRotate(path);
        if (degree != 0) {
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(degree); // 旋转angle度
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
        }
        return bitmap;
    }

    /**
     * 读取图片文件旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片旋转的角度
     */
    public static int getPicRotate(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static String save(String filename, Bitmap bitmap, int maxkb) {
        try {
            Bitmap.CompressFormat cf;
            if (filename.substring(filename.lastIndexOf(".") + 1).equals("png")) {
                cf = Bitmap.CompressFormat.PNG;// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos
            } else {
                cf = Bitmap.CompressFormat.JPEG;// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos
            }
            if (filename.lastIndexOf(".") > -1) {
                filename.substring(0, filename.lastIndexOf("."));
            }
//            byte[] data = readInputStream(new URL(url).openStream());
//            if(data.length==0)
//                return null;
            File filexists = new File(filename);
            filexists.delete();
            filexists.createNewFile();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//            int options = 100;
//            MyLogUtil.e("test","原始大小" + baos.toByteArray().length);
////      Log.i("test","原始大小" + baos.toByteArray().length);
//            while (baos.toByteArray().length / 1024 > maxkb) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
////          Log.i("test","压缩一次!");
//                baos.reset();// 重置baos即清空baos
//                options -= 10;// 每次都减少10
//                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//            }
//            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
//                    data.length);
            int options = 100;
            bitmap.compress(cf, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos
//            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//            LogUtils.e("test", "原始大小" + baos.toByteArray().length);
            int lastBaosLength = 0;
            while (baos.toByteArray().length != lastBaosLength && (baos.toByteArray().length / 1024) > maxkb && options > 0) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
                lastBaosLength = baos.toByteArray().length;
//                MyLogUtil.e("test", "options" + options + "压缩前大小" + baos.toByteArray().length);
                baos.reset();// 重置baos即清空baos
                options -= 10;// 每次都减少10
                bitmap.compress(cf, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//                LogUtils.e("test", "options" + options + "压缩后大小" + baos.toByteArray().length);
            }
//            LogUtils.e("test", "压缩后大小" + baos.toByteArray().length);
            FileOutputStream fos = new FileOutputStream(filexists);
//            baos.writeTo(fos);
            fos.write(baos.toByteArray());
//            if (filename.substring(filename.lastIndexOf(".") + 1).equals("png"))
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            else
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            baos.close();
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    public static String save(String url, String filename, int maxkb) {
//        try {
//            if (filename.lastIndexOf(".") > -1) {
//                filename.substring(0, filename.lastIndexOf("."));
//            }
//            byte[] data = readInputStream(new URL(url).openStream());
//            if(data.length==0)
//                return null;
//            File filexists = new File(filename);
//            filexists.delete();
//            filexists.createNewFile();
////            ByteArrayOutputStream baos = new ByteArrayOutputStream();
////            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
////            int options = 100;
////            MyLogUtil.e("test","原始大小" + baos.toByteArray().length);
//////      Log.i("test","原始大小" + baos.toByteArray().length);
////            while (baos.toByteArray().length / 1024 > maxkb) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
//////          Log.i("test","压缩一次!");
////                baos.reset();// 重置baos即清空baos
////                options -= 10;// 每次都减少10
////                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
////            }
////            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
//                    data.length);
//            int options = 100;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//            MyLogUtil.e("test","原始大小" + baos.toByteArray().length);
//            while (baos.toByteArray().length / 1024 > maxkb) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
//                baos.reset();// 重置baos即清空baos
//                options -= 10;// 每次都减少10
//                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//            }
//            MyLogUtil.e("test","压缩后大小" + baos.toByteArray().length);
//            FileOutputStream fos = new FileOutputStream(filexists);
////            baos.writeTo(fos);
//            fos.write(baos.toByteArray());
////            if (filename.substring(filename.lastIndexOf(".") + 1).equals("png"))
////                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
////            else
////                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//            baos.close();
//            return filename;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static String save(String filename, Bitmap bitmap) {
//        File file = null;
        try {
            File filexists = new File(filename);
            filexists.createNewFile();
            FileOutputStream fos = new FileOutputStream(filexists);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            LogUtils.e("saveImage", filexists.length() + "");
            fos.flush();
            fos.close();
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    private static String filename = "temp.jpg";
//    private static Uri imageUri = Uri.parse("file://" + AppConfig.DIR_IMG
//            + File.separator + filename);

    /**
     * 获取文件名称
     *
     * @return
     */
    public static String getFileName() {
        return filename;
    }

    /**
     * 因为只能在Activity中使用，故开启新方法替代
     *
     * @param context
     * @param requestCode
     * @param resultCode
     * @param data
     * @param listener
     */
    public static void onActivityResult(Activity context, int requestCode, int resultCode,
                                        Intent data, SelectImageListener listener) {
        onActivityResult(context, requestCode, resultCode, data, 1000, 1001, listener);
    }

    /**
     * 因为只能在Activity中使用，故开启新方法替代
     * 部分手机裁切如果输出宽高比例是1:1的话裁切UI自动转换为圆形
     *
     * @param context
     * @param requestCode
     * @param resultCode
     * @param data
     * @param outputX     输出宽度
     * @param outputY     输出高度
     * @param listener
     */
    public static void onActivityResult(Activity context, int requestCode, int resultCode,
                                        Intent data, int outputX, int outputY, SelectImageListener listener) {

        if (requestCode == DATA_WITH_VIDEO) {
//            Uri videoUri = data.getData();//getUri(context, AppConfig.DIR_IMG + File.separator + "temp.mp4");
//            filename = System.currentTimeMillis() + ".mp4";
//            Uri imageUri1 = Uri.fromFile(new File(AppConfig.DIR_VIDEO + File.separator + filename));
            filename = AppConfig.DIR_VIDEO + File.separator + "temp.mp4";
            listener.selectPic();
        } else if (requestCode == DATA_WITH_SOUND) {
            filename = getAudioFilePathFromUri(context,data.getData());
//            filename = AppConfig.DIR_VIDEO + File.separator + "temp.amr";
            listener.selectPic();
        } else if (requestCode == DATA_WITH_CAMERA) {
            Uri imageUri = getUri(context, AppConfig.DIR_IMG + File.separator + "temp.jpg");
            filename = System.currentTimeMillis() + ".jpg";
            Uri imageUri1 = Uri.fromFile(new File(AppConfig.DIR_IMG + File.separator + filename));
//            try {
//                new File(AppConfig.DIR_IMG + File.separator + filename).mkdirs();
//                new File(AppConfig.DIR_IMG + File.separator + filename).createNewFile();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            if (isNeedCrop) {
                ((Activity) context).startActivityForResult(cropImage(context, imageUri, imageUri1, outputX, outputY),
                        DATA_WITH_CAMERA_CROP);
            } else {
                filename = AppConfig.DIR_IMG + File.separator + "temp.jpg";
                listener.selectPic();
            }
        } else if (requestCode == DATA_WITH_CAMERA_CROP) {
            // 将拍照获取的原图删除
            File file = new File(AppConfig.DIR_IMG + File.separator
                    + "temp.jpg");
            filename = AppConfig.DIR_IMG + File.separator + filename;
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            listener.selectPic();
        } else if (requestCode == DATA_WITH_PHOTO_PICKED) {
            Uri imageUri = data.getData();
            filename = System.currentTimeMillis() + ".jpg";
            Uri imageUri1 = Uri.fromFile(new File(AppConfig.DIR_IMG + File.separator + filename));
            //getUri(context, AppConfig.DIR_IMG + File.separator + filename);
            if (isNeedCrop) {
                ((Activity) context).startActivityForResult(cropImage(context, imageUri, imageUri1, outputX, outputY),
                        DATA_WITH_PHOTO_PICKED_CROP);
            } else {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = context.getContentResolver().query(imageUri,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filename = cursor.getString(columnIndex);
                    cursor.close();
                } else {
                    filename = data.getData().getPath();
                }
                listener.selectPic();
            }
        } else if (requestCode == DATA_WITH_PHOTO_PICKED_CROP) {
            filename = AppConfig.DIR_IMG + File.separator + filename;
            listener.selectPic();
        } else if (requestCode == REQUEST_TAKE_PHOTO_PERMISSION) {
            takePhoto(context, "temp.jpg");
        }
    }

    public static void showPhotoDialog(final Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_selecthead, null);
        TextView take_photo = (TextView) view.findViewById(R.id.take_photo);
        TextView select_album = (TextView) view.findViewById(R.id.select_album);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ImageUtil.takePhoto(context, "temp.jpg");
            }
        });
        select_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ImageUtil.selectFromAlbum(context);
            }
        });
        dialog = MyProgressDialog.getDialog(context, view, Gravity.BOTTOM, false);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public static String getImgPathFromCache(Context context, String url) {
        FutureTarget<File> future = with(context).load(url).downloadOnly(100, 100);
        try {
            File cacheFile = future.get();
            String path = cacheFile.getAbsolutePath();
            return path;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过Uri，获取录音文件的路径（绝对路径）
     *
     * @param uri 录音文件的uri
     * @return 录音文件的路径（String）
     */
    private static String getAudioFilePathFromUri(Activity context, Uri uri) {
        Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        String temp = cursor.getString(index);
        cursor.close();
        return temp;
    }
}
