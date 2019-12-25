package com.kangjj.skin.lib.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatViewInflater;

import com.kangjj.skin.lib.views.SkinnableButton;
import com.kangjj.skin.lib.views.SkinnableImageView;
import com.kangjj.skin.lib.views.SkinnableLinearLayout;
import com.kangjj.skin.lib.views.SkinnableRelativeLayout;
import com.kangjj.skin.lib.views.SkinnableTextView;

/**
 * 自定义控件加载器（可以考虑该类不被继承）
 * TODO 两个问题？
 * RecyclerView怎么换肤？设置完uiMode，刷新Adapter就可以了
 * 为什么CustomAppCompatViewInflater需要继承 AppCompatViewInflater？我看了课程代码，没有用到AppCompatViewInflater的方法或者属性啊
 * 不需要继承AppCompatViewInflater，只不过这个类和AppCompatViewInflater的功能类似，进行偷梁换柱
 */
public final class CustomAppCompatViewInflater/* extends AppCompatViewInflater */{
    private String name;//控件名
    private Context context;//上下文

    private AttributeSet attrs;//某控件对应所有的属性

    public CustomAppCompatViewInflater(@NonNull Context context) {
        this.context = context;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttrs(AttributeSet attrs) {
        this.attrs = attrs;
    }

    /**
     * todo 2 根据名字（例如：LinearLayout->SkinnableLinearLayout、ImageView->SkinnableImageView）
     *  * 生成对应的自定义控件（偷梁换柱，其实源码就是这样处理）注意这里的自定义控件需要继承
     *  LinearLayoutCompat、AppCompatTextView等对应的兼容控件,进入到SkinnableTextView中
     * @return 自动匹配控件名，并初始化控件对象
     */
    public View autoMatch(){

        View view = null;
        switch (name) {
            case "LinearLayout":
                // view = super.createTextView(context, attrs); // 源码写法
                view = new SkinnableLinearLayout(context,attrs);
                verifyNotNull(view,name);
                break;
            case "RelativeLayout":
                view = new SkinnableRelativeLayout(context,attrs);
                verifyNotNull(view,name);
                break;
            case "TextView":
                view = new SkinnableTextView(context,attrs);
                verifyNotNull(view,name);
                break;
            case "ImageView":
                view = new SkinnableImageView(context,attrs);
                verifyNotNull(view,name);
                break;
            case "Button":
                view = new SkinnableButton(context,attrs);
                verifyNotNull(view,name);
                break;
        }

        return view;
    }

    private void verifyNotNull(View view,String name){
        if(view==null){
            throw new IllegalStateException(this.getClass().getName() +
                    " asked to inflate view for <" + name + ">, but returned null");
        }
    }
}
