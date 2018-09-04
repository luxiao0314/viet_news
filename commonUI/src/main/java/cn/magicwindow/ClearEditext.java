package cn.magicwindow;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.magicwindow.commonui.R;
import cn.magicwindow.interfaces.EditClearface;

/**
 * 输入框，带清空按钮
 */
public class ClearEditext extends android.support.v7.widget.AppCompatEditText implements TextWatcher {
    //EditText右侧的删除按钮
    private Drawable mClearDrawable;
    private boolean hasFoucs;

    private EditClearface clearface = null;


    public ClearEditext(Context context) {
        super(context);
    }

    public ClearEditext(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setCusEditClearface(EditClearface clearface) {
        this.clearface = clearface;
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3,）
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.icon_edit_clear);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(new myOnfocuchange());
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
//        try {//修改光标的颜色（反射）
//            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
//            f.setAccessible(true);
//            f.set(this, R.color.textColor5);
//
//
//        } catch (Exception ignored) {
//            // TODO: handle exception
//        }

    }

    /* @说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标
     * event.getX() 获取相对应自身左上角的X坐标
     * event.getY() 获取相对应自身左上角的Y坐标
     * getWidth() 获取控件的宽度
     * getHeight() 获取控件的高度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
     * isInnerWidth:
     *  getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     *  getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     * isInnerHeight:
     *  distance 删除图标顶部边缘到控件顶部边缘的距离
     *  distance + height 删除图标底部边缘到控件顶部边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height();
                int distance = (getHeight() - height) / 2;
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight()) && x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                //只限制width，增大点击响应区域
//                if (isInnerWidth && isInnerHeight) {
                if (isInnerWidth) {
                    this.setText("");

                    if (null != clearface) {
                        clearface.clearCallBack(ClearEditext.this);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当MyEditText焦点发生变化的时候，
     * 输入长度为零，隐藏删除图标，否则，显示删除图标
     */
    private class myOnfocuchange implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            hasFoucs = hasFocus;

            if (null != clearface) {
                clearface.focusChangeCallBack(ClearEditext.this, hasFocus);
            }

            if (hasFocus) {
                setClearIconVisible(getText().length() > 0);
            } else {
                setClearIconVisible(false);
            }
        }

    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
            if (null != clearface) {
                clearface.onTextChanged(s);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (null != clearface) {
            clearface.afterTextChanged(s);
        }
    }

}
