package com.lncucc.authentication.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.widget.ImageView;

import com.askia.common.util.StringUtils;
import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */

        if (path instanceof Integer) {
            imageView.setImageDrawable(context.getResources().getDrawable((Integer)path));
        }else {
            String url = (String) path;
            byte[] decode = null;
            if(isBase64Img(url))
            {
                url = url.split(",")[1];
                decode = Base64.decode(url, Base64.DEFAULT);
                Bitmap decodedByte = byteToBitmap(decode);
                imageView.setImageBitmap(decodedByte);
            }
            else if(isBase64Gif(url))
            {
                url = url.split(",")[1];
                //  decode = Base64.decode(url, Base64.DEFAULT);
                //   Bitmap decodedByte = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                Glide.with(context).asGif().load(base64ToFile(url)).into(imageView);
                //Glide.with(context).load(decodedByte).asGif().into(imageView);
            }
            else
            {
                Glide.with(context).load((String)path).into(imageView);
            }
        }
    }

    public static boolean isBase64Img(String imgurl){
        if(!StringUtils.isEmpty(imgurl)&&(imgurl.startsWith("data:image/png;base64,")
                ||imgurl.startsWith("data:image/*;base64,")||imgurl.startsWith("data:image/jpg;base64,") || imgurl.startsWith("data:image/jpeg;base64,"))
        )
        {
            return true;
        }
        return false;
    }

    public static boolean isBase64Gif(String imgurl){
        if(!StringUtils.isEmpty(imgurl)&&(imgurl.startsWith("data:image/gif;base64,")))
        {
            return true;
        }
        return false;
    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
    @Override
    public ImageView createImageView(Context context) {
        //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
        //  SimpleDraweeView simpleDraweeView=new SimpleDraweeView(context);
        ImageView imageView = new ImageView(context);
        //   imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    /**
     * base64字符串转文件
     * @param base64
     * @return
     */
    public static File base64ToFile(String base64) {
        File file = null;
        String fileName = Math.abs(base64.hashCode()) + ".gif";
        FileOutputStream out = null;
        try {
            File dir  = new File(Environment.getExternalStorageDirectory() + "/eduresource");
            if(!dir.exists())
                dir.mkdir();
            // 解码，然后将字节转换为文件
            file = new File(Environment.getExternalStorageDirectory() + "/eduresource/", fileName);
            if (!file.exists())
                file.createNewFile();
            else
            {
                return file;
            }
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);// 将字符串转换为byte数组
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            out = new FileOutputStream(file);
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread); // 文件写操作
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (out!= null) {
                    out.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return file;
    }

    public static Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        if(imgByte.length > 1024 *1024 * 30)
            options.inSampleSize = 8;
        else if(imgByte.length > 1024 *1024 * 20)
            options.inSampleSize = 6;
        else if(imgByte.length > 1024 *1024 * 10)
            options.inSampleSize = 4;
        else if(imgByte.length > 1024 *1024 * 5)
            options.inSampleSize = 2;

        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }


}
