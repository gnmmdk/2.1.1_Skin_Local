package com.kangjj.skin.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.kangjj.skin.lib.R;
import com.kangjj.skin.lib.SkinManager;
import com.kangjj.skin.lib.core.ViewsMatch;
import com.kangjj.skin.lib.model.AttrsBean;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 2.1.1_Skin_Local
 * @Package: com.kangjj.skin.lib.views
 * @CreateDate: 2019/11/7 10:11
 */
public class SkinnableLinearLayout extends LinearLayoutCompat implements ViewsMatch {
    private AttrsBean attrsBean;
    public SkinnableLinearLayout(Context context) {
        this(context,null);
    }

    public SkinnableLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SkinnableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attrsBean = new AttrsBean();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SkinnableLinearLayout,defStyleAttr,0);
        attrsBean.saveViewResource(typedArray,R.styleable.SkinnableLinearLayout);
        typedArray.recycle();
    }

    @Override
    public void skinnableView() {
// 根据自定义属性，获取styleable中的background属性
        int key = R.styleable.SkinnableLinearLayout[R.styleable.SkinnableLinearLayout_android_background];
        // 根据styleable获取控件某属性的resourceId
        int backgroundResourceId = attrsBean.getViewResource(key);
        if (backgroundResourceId > 0) {
            // 是否默认皮肤
            if (SkinManager.getInstance().isDefaultSkin()) {
                // 兼容包转换
                Drawable drawable = ContextCompat.getDrawable(getContext(), backgroundResourceId);
                // 控件自带api，这里不用setBackgroundColor()因为在9.0测试不通过
                // setBackgroundDrawable在这里是过时了
                setBackground(drawable);
            }else{
                // 获取皮肤包资源
                Object skinResourceId = SkinManager.getInstance().getBackgroundOrSrc(backgroundResourceId);
                // 兼容包转换
                if (skinResourceId instanceof Integer) {
                    int color = (int) skinResourceId;
                    setBackgroundColor(color);
                    // setBackgroundResource(color); // 未做兼容测试
                } else {
                    Drawable drawable = (Drawable) skinResourceId;
                    setBackground(drawable);
                }
            }
        }
    }
}
