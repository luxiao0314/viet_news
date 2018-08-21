package cn.magicwindow.interfaces;

import android.text.Editable;
import android.widget.EditText;

/**
 * @Description 带有清空功能的edittext对应的触发事件
 * @Author lucio
 * @Email xiao.lu@magicwindow.cn
 * @Date 14/08/2017 2:00 PM
 * @Version 1.0.0
 */
public interface EditClearface {
    void clearCallBack(EditText clearface);

    void focusChangeCallBack(EditText editext, boolean isFocus);

    void onTextChanged(CharSequence s);

    void afterTextChanged(Editable s);
}
