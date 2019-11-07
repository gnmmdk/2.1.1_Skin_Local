package com.kangjj.skin.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.kangjj.skin.lib.R;
import com.kangjj.skin.lib.core.ViewsMatch;
import com.kangjj.skin.lib.model.AttrsBean;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 2.1.1_Skin_Local
 * @Package: com.kangjj.skin.lib.views
 * @CreateDate: 2019/11/7 10:04
 */
public class SkinnableImageView extends AppCompatImageView implements ViewsMatch {

    private AttrsBean attrsBean;

    public SkinnableImageView(Context context) {
        this(context, null);
    }

    public SkinnableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinnableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attrsBean = new AttrsBean();
        // 根据自定义属性，匹配控件属性的类型集合，如：src
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SkinnableImageView,
                defStyleAttr, 0);
        //存储到临时JavaBean对象
        attrsBean.saveViewResource(typedArray,R.styleable.SkinnableImageView);
        typedArray.recycle();
    }

    @Override
    public void skinnableView() {
        int key = R.styleable.SkinnableImageView[R.styleable.SkinnableImageView_android_src];
        int bgResId = attrsBean.getViewResource(key);
        if(bgResId>0){
            //兼容包转换
            Drawable drawable = ContextCompat.getDrawable(getContext(),bgResId);
            setImageDrawable(drawable);
        }
    }
}
