package com.kangjj.skin.lib.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;
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
 * @CreateDate: 2019/11/7 9:20
 */
public class SkinnableButton extends AppCompatButton implements ViewsMatch {
    private AttrsBean attrsBean;

    public SkinnableButton(Context context) {
        this(context, null);
    }

    public SkinnableButton(Context context, AttributeSet defStyleAttr) {
        this(context, defStyleAttr,androidx.appcompat.R.attr.buttonStyle);
    }

    public SkinnableButton(Context context , AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        attrsBean = new AttrsBean();
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SkinnableButton,defStyleAttr,0);
        // 存储到临时JavaBean对象
        attrsBean.saveViewResource(typedArray,R.styleable.SkinnableButton);
        typedArray.recycle();
    }


    @Override
    public void skinnableView() {
        //根据自定义属性，获取styleable的backgroud属性
        int key = R.styleable.SkinnableButton[R.styleable.SkinnableButton_android_background];
        //根据styleable 获取控件某属性的resourceId
        int bgResId = attrsBean.getViewResource(key);
        if(bgResId > 0){
            if(SkinManager.getInstance().isDefaultSkin()){
                //兼容包转换
                Drawable drawable = ContextCompat.getDrawable(getContext(),bgResId);
                //控件自带api,这里不用setBackgroundColor()，因为9.0测试不通过
                //setBackgroundDrawable本来过时了，但是兼容包重写了方法。
                setBackgroundDrawable(drawable);
            }else{
                Object skinResourceId = SkinManager.getInstance().getBackgroundOrSrc(bgResId);
                if (skinResourceId instanceof Integer) {
                    int color = (int)skinResourceId;
                    setBackgroundColor(color);
                    // setBackgroundResource(color); // 未做兼容测试
                }else{
                    Drawable drawable = (Drawable)skinResourceId;
                    setBackgroundDrawable(drawable);
                }
            }

        }
        //根据自定义属性，获取styleable中的textColor属性
        key = R.styleable.SkinnableButton[R.styleable.SkinnableButton_android_textColor];
        int textResId = attrsBean.getViewResource(key);
        if(textResId>0){
            if (SkinManager.getInstance().isDefaultSkin()) {
                ColorStateList color = ContextCompat.getColorStateList(getContext(), textResId);
                setTextColor(color);
            }else{
                ColorStateList color = SkinManager.getInstance().getColorStateList(textResId);
                setTextColor(color);
            }
        }

        key = R.styleable.SkinnableTextView[R.styleable.SkinnableTextView_custom_typeface];
        int textTypefaceResId = attrsBean.getViewResource(key);
        if(textTypefaceResId>0){
            if(SkinManager.getInstance().isDefaultSkin()){
                setTypeface(Typeface.DEFAULT.DEFAULT);
            }else{
                setTypeface(SkinManager.getInstance().getTypeface(textTypefaceResId));
            }
        }
    }
}
