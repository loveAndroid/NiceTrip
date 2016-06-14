package com.example.aidl.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;

/**
 * Created by azz on 15/11/19.
 */
public class Utils {
    /**
     * 密度
     */
    public static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;

    public static int dp2px(int dp) {
        return Math.round(dp * DENSITY);
    }
    
    public static Bitmap createBitmapFromView(View view) {
    	
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        if (bitmap != null) {
            Canvas mCanvas = new Canvas();
            
			synchronized (mCanvas) {
                mCanvas.setBitmap(bitmap);
                view.draw(mCanvas);
                mCanvas.setBitmap(null); //清除引用
            }
        }
        return bitmap;
    }
    
    
    public static int getScreenWidth(){
    	return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    
    /** 
     * ͼƬ��ת 
     * @param img 
     * @return 
     */  
    public static Bitmap rotation(Bitmap img,int degree){  
        Matrix matrix = new Matrix();  
        matrix.postRotate(degree); /*��ת90��*/  
        int width = img.getWidth();  
        int height =img.getHeight();  
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);  
        return img;  
    }  
    
    
}
