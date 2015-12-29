package com.mc.vending.tools;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import com.mc.vending.config.Constant;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {
    static {
        // L.logLevel = Constant.LOGLEVEL;
    }

    // 异步加载图片
    private HashMap<String, SoftReference<Drawable>> imageCache;
    Drawable                                         drawable;
    private String                                   filePath;

    public AsyncImageLoader() {

        imageCache = new HashMap<String, SoftReference<Drawable>>();
        this.filePath = Constant.DOWNLOAD_URL;
    }

    public AsyncImageLoader(String filePath) {

        imageCache = new HashMap<String, SoftReference<Drawable>>();
        this.filePath = filePath;
    }

    public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback, final String size) {

        if (imageCache.containsKey(imageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            if (drawable != null) {
                drawable = null;
            }
            drawable = softReference.get();
            if (drawable != null) {
                return drawable;
            }
        }
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Drawable) message.obj, imageUrl, size);
            }
        };
        new Thread() {
            @Override
            public void run() {
                if (imageUrl != null && !"".equals(imageUrl)) {
                    if (drawable != null) {
                        drawable = null;
                    }
                    drawable = loadImageFromUrl(imageUrl);
                    if (drawable == null)
                        return;
                    String fileName = Tools.getFileName(imageUrl);
                    if (drawable != null && filePath != null && !filePath.equals("")) {
                        Tools.saveImageToSD(((BitmapDrawable) drawable).getBitmap(), filePath, fileName);
                    }
                    imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                    Message message = handler.obtainMessage(0, drawable);
                    handler.sendMessage(message);
                }

            }
        }.start();
        return null;
    }

    public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
        if (imageCache.containsKey(imageUrl)) {

            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            Drawable drawable = softReference.get();
//            ZillionLog.i("imageCache", drawable);
            if (drawable != null) {
                return drawable;
            }
        }
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Drawable) message.obj, imageUrl, imageUrl);
            }
        };
        new Thread() {
            @Override
            public void run() {

                if (drawable != null) {
                    drawable = null;
                }
                String fileName = Tools.getFileName(imageUrl);
                drawable = loadImageFromSD(fileName);

                if (drawable == null)
                    drawable = loadImageFromUrl(imageUrl);

                if (drawable == null)
                    return;

                if (drawable != null && filePath != null && !filePath.equals("")) {

                    Tools.saveImageToSD(((BitmapDrawable) drawable).getBitmap(), filePath, fileName);
                }
                imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));

                Message message = handler.obtainMessage(0, drawable);
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }

    /**
     * 从sd卡取图片
     * 
     * @param fileName
     * @return
     */
    private Drawable loadImageFromSD(String fileName) {

        String sdFileName = filePath + fileName;
//        ZillionLog.i("loadImageFromSD", sdFileName);
        Drawable d = null;
        try {
            d = Drawable.createFromPath(sdFileName);
        } catch (Exception e) {
            ZillionLog.e("loadImageFromSD  " + e.getMessage());
        }

        return d;
    }

    /**
     * 取服务器图片
     * 
     * @param url
     * @return
     */
    private Drawable loadImageFromUrl(String url) {

//        ZillionLog.i("loadImageFromUrl", url);
        URL m;
        Drawable d = null;
        InputStream i = null;
        try {
            System.setProperty("http.keepAlive", "false");
            m = new URL(url);
            if (m == null || m.openStream() == null) {
                return null;
            }
            i = m.openStream();

        } catch (Exception e) {
            ZillionLog.e(e.getMessage());
            if (i != null) {
                try {
                    i.close();
                } catch (IOException e1) {
                    ZillionLog.e(e1.getMessage());
                    return null;
                }
            }
            return null;
        }
        try {
            d = Drawable.createFromStream(i, "src");
        } catch (Exception e) {
            ZillionLog.e(e.getMessage());
            return null;
        }

        if (i != null) {
            try {
                i.close();
            } catch (IOException e) {
                ZillionLog.e(e.getMessage());
                return null;
            }
        }
        return d;
    }

    public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable, String imageUrl, String tag);
    }

}