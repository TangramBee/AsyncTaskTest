package news.huoren.com.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * Created by 13615 on 2016/9/25.
 */

public class ImageLoader {

    private ImageView view;
    private String url = null;

    //创建缓存
    private LruCache<String, Bitmap> mCache;

    public void ImageLoader() {
        //获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mCache = new LruCache<String, Bitmap>(cacheSize){

            //用于获取每个存储对象的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存中调用
                return value.getByteCount();
            }


        };
    }

    public void addBitmapToCache(String key, Bitmap value) {
        if (getBitmapFromCache(key) == null){
            mCache.put(key, value);
        }

    }

    public Bitmap getBitmapFromCache(String urlStr) {
        this.url = urlStr;
        return mCache.get(url);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (view.getTag().equals(url)){
                view.setImageBitmap((Bitmap) msg.obj);
            }

        }
    };

    public void showImageByThread(final ImageView imageView, final String urlStr){
        view = imageView;

        this.url = urlStr;
        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap bitmap = null;
                bitmap = getBitmapFromCache(url);
                if (bitmap == null) {
                    bitmap = getBitmap(url);
                }
                Message message = Message.obtain();
                message.obj = bitmap;
                handler.sendMessage(message);

            }
        }.start();
    }

    public Bitmap getBitmap(String urlString) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            addBitmapToCache(urlString,bitmap);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
