package com.kernal.smartvision.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import com.kernal.smartvision.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WenTong on 2018/12/10.
 */

/**
 * 展示结果用的自定义 View
 */
public class ResultLayout  extends LinearLayout {
    //最大长度,vin 为 17
    private int maxLength = 17;
    //0:vin 1:phone
    private int resultType = 0;
    //设置子View状态indexc
    private int inputIndex = 0;
    private List<String> mResultList;//结果字符
    private ResultLayout.resultChangeListener resultChangeListener;//状态改变监听
    private Context mContext;

    private boolean mIsShowInputLine;
    private int mInputColor;
    private int mNoinputColor;
    private int mLineColor;
    private int mTxtInputColor;
    private int mDrawType;
    private int mInterval;
    private int mItemWidth;
    private int mItemHeight;
    private int mShowResultType;
    private int mTxtSize;
    private int mBoxLineSize;
    private boolean isFocus = false;

    public void setResultChangeListener(ResultLayout.resultChangeListener pwdChangeListener) {
        this.resultChangeListener = pwdChangeListener;
    }

    public ResultLayout(Context context) {
        this(context, null);
    }

    public ResultLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResultLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 初始化View
     */
    private void initView(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ResultLayoutStyle);
        mInputColor = ta.getResourceId(R.styleable.ResultLayoutStyle_box_input_color, R.color.pass_view_rect_input);
        mNoinputColor = ta.getResourceId(R.styleable.ResultLayoutStyle_box_no_input_color, R.color.regi_line_color);
        mLineColor = ta.getResourceId(R.styleable.ResultLayoutStyle_input_line_color, R.color.pass_view_rect_input);
        mTxtInputColor = ta.getResourceId(R.styleable.ResultLayoutStyle_text_input_color, R.color.pass_view_rect_input);
        mDrawType = ta.getInt(R.styleable.ResultLayoutStyle_box_draw_type, 0);
        mInterval = ta.getDimensionPixelOffset(R.styleable.ResultLayoutStyle_interval_width, 4);
        maxLength = ta.getInt(R.styleable.ResultLayoutStyle_pass_leng, 17);
        mItemWidth = ta.getDimensionPixelOffset(R.styleable.ResultLayoutStyle_item_width, 40);
        mItemHeight = ta.getDimensionPixelOffset(R.styleable.ResultLayoutStyle_item_height, 40);
        mShowResultType = ta.getInt(R.styleable.ResultLayoutStyle_result_input_type, 0);
        mTxtSize = ta.getDimensionPixelOffset(R.styleable.ResultLayoutStyle_draw_txt_size, 18);
        mBoxLineSize = ta.getDimensionPixelOffset(R.styleable.ResultLayoutStyle_draw_box_line_size, 4);
        mIsShowInputLine = ta.getBoolean(R.styleable.ResultLayoutStyle_is_show_input_line, true);
        resultType = ta.getInt(R.styleable.ResultLayoutStyle_result_type, 0);

        ta.recycle();
        mResultList = new ArrayList<>();
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        //设置点击时弹出输入法
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setFocusable(true);
                setFocusableInTouchMode(true);
                requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(ResultLayout.this, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        this.setOnKeyListener(new MyKeyListener());//按键监听

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //由于弹出键盘会重新 measure，导致界面重绘，这里加一个变量控制
                isFocus =  true;
                if (b) {
                    ResultView resultView = (ResultView) getChildAt(inputIndex);
                    if (resultView != null) {
                        resultView.setmIsShowRemindLine(mIsShowInputLine);
                        resultView.startInputState();
                    }
                } else {
                    ResultView resultView = (ResultView) getChildAt(inputIndex);
                    if (resultView != null) {
                        resultView.setmIsShowRemindLine(false);
                        resultView.updateInputState(false);
                    }
                }
            }
        });
    }

    /**
     * 添加子View
     * @param context
     */
    private void addChildVIews(Context context) {
        for (int i = 0; i < maxLength; i++) {
            ResultView resultView = new ResultView(context);
            LayoutParams params = new LayoutParams(mItemWidth, mItemHeight);
            if (i > 0) {                                       //第一个和最后一个子View不添加边距
                params.leftMargin = mInterval;
            }
            resultView.setInputStateColor(mInputColor);
            resultView.setNoinputColor(mNoinputColor);
            resultView.setInputStateTextColor(mTxtInputColor);
            resultView.setRemindLineColor(mLineColor);
            resultView.setmBoxDrawType(mDrawType);
            resultView.setmShowPassType(mShowResultType);
            resultView.setmDrawTxtSize(mTxtSize);
            resultView.setmDrawBoxLineSize(mBoxLineSize);
            resultView.setmIsShowRemindLine(mIsShowInputLine);
            addView(resultView, params);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {     //判断 子View宽+边距是否超过了父布局 超过了则重置宽高
            if ((maxLength * mItemWidth + (maxLength - 1) * mInterval) > getMeasuredWidth()) {
                mItemWidth = (getMeasuredWidth() - (maxLength - 1) * mInterval) / maxLength;
                mItemHeight = mItemWidth;
            }
            addChildVIews(getContext());
        }

        if (getChildCount() > 1){

            for (int i = 0; i < getChildCount(); i++) {
                ResultView resultView = (ResultView) getChildAt(i);
                if (i > mResultList.size() - 1) {
                    if (resultView != null) {
                        resultView.setmIsShowRemindLine(false);
                        resultView.updateInputState(false);
                    }
                    break;
                }

                if (resultView != null && mResultList !=null && mResultList.size() == getChildCount()) {
                    resultView.setmPassText(mResultList.get(i));
                    resultView.updateInputState(true);
                    //  mResultList.add(mResultList.get(i));
                }
            }
        }
        //识别失败的情况
        if (mResultList.size() ==4 && getChildCount() >= 4){
            for (int i = 0; i < getChildCount(); i++) {
                ResultView resultView = (ResultView) getChildAt(i);
                if (i > mResultList.size() - 1) {
                    if (resultView != null) {
                        resultView.setmIsShowRemindLine(false);
                        resultView.updateInputState(false);
                    }
                    break;
                }
            }
            for(int i = 0; i < mResultList.size(); i ++){
                ResultView resultView = (ResultView) getChildAt(i);
                resultView.setmPassText(mResultList.get(i));
                resultView.updateInputState(true);
            }
        }

        if (mResultList.size() == 0 && isFocus){
            ResultView resultView = (ResultView) getChildAt(inputIndex);
            if (resultView != null) {
                resultView.setmIsShowRemindLine(mIsShowInputLine);
                resultView.startInputState();
            }
        }
    }
    //设置识别结果
    public void setContent(String content){
        mResultList.clear();
        if (content != null && content.length() > 1){
            for (int i = 0; i < content.length(); i ++){
                mResultList.add(content.charAt(i) + "");
            }
        }
        if (content.length() >= maxLength){
            inputIndex = content.length();
        }else if (content.length() == 4){
            //识别失败
            inputIndex = 4;
        }else {
            inputIndex = 0;
        }
    }

    /**
     * 添加字符
     * @param result
     */
    public void addResult(String result) {
        if (mResultList != null && mResultList.size() < maxLength) {
            mResultList.add(result + "");
            setNextInput(result);
        }
        String tempResult = getResultString();
        if (resultChangeListener != null && !"".equals(tempResult)) {
            resultChangeListener.onChange(getResultString());

        }
    }

    /**
     * 删除字符
     */
    public void removeResult() {
        if (mResultList != null && mResultList.size() > 0) {
            mResultList.remove(mResultList.size() - 1);
            setPreviosInput();
        }
        if (resultChangeListener != null) {
            if (mResultList.size() > 0) {
                resultChangeListener.onChange(getResultString());
            } else {
                resultChangeListener.onNull();
            }
        }
    }

    /**
     * 清空所有
     */
    public void removeAllResult() {
        if (mResultList != null) {
            for (int i = mResultList.size(); i >= 0; i--) {
                if (i > 0) {
                    setNoInput(i, false, "");
                } else if (i == 0) {
                    ResultView resultView = (ResultView) getChildAt(i);
                    if (resultView != null) {
                        resultView.setmPassText("");
                        resultView.startInputState();
                    }
                }
            }
            mResultList.clear();
            inputIndex = 0;
        }
        if (resultChangeListener != null) {
            resultChangeListener.onNull();
        }
    }

    /**
     * 获取结果
     * @return pwd
     */
    public String getResultString() {
        StringBuffer resultString = new StringBuffer();
        for (String i : mResultList) {
            resultString.append(i);
        }
        return resultString.toString();
    }

    /**
     * 设置下一个View为输入状态
     */
    private void setNextInput(String str) {
        if (inputIndex < maxLength) {
            setNoInput(inputIndex, true, str);
            inputIndex++;
            ResultView passWordView = (ResultView) getChildAt(inputIndex);

            if (passWordView != null) {
                passWordView.setmPassText(str + "");
                passWordView.startInputState();
            }
        }

    }

    /**
     * 设置上一个View为输入状态
     */
    private void setPreviosInput() {
        if (inputIndex > 0) {
            setNoInput(inputIndex, false, "");
            inputIndex--;
            ResultView resultView = (ResultView) getChildAt(inputIndex);
            if (resultView != null) {
                resultView.setmPassText("");
                resultView.startInputState();
            }
        } else if (inputIndex == 0) {
            ResultView resultView = (ResultView) getChildAt(inputIndex);
            if (resultView != null) {
                resultView.setmPassText("");
                resultView.startInputState();
            }
        }
    }

    /**
     * 设置指定View为不输入状态
     * @param index   view下标
     * @param isinput 是否输入过
     */
    public void setNoInput(int index, boolean isinput, String txt) {
        if (index < 0) {
            return;
        }
        ResultView resultView = (ResultView) getChildAt(index);
        if (resultView != null) {
            resultView.setmPassText(txt);
            resultView.updateInputState(isinput);
        }
    }


    public interface resultChangeListener {
        void onChange(String result);
        void onNull();
        void onFinished(String result);
        void onBack();
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        //outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;          //显示数字键盘
        if (resultType == 1){
            outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
        }
        //outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        return new ZanyInputConnection(this, false);
    }


    private class ZanyInputConnection extends BaseInputConnection {

        @Override
        public boolean commitText(CharSequence txt, int newCursorPosition) {
            return super.commitText(txt, newCursorPosition);
        }

        public ZanyInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return super.sendKeyEvent(event);
        }


        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }


    /**
     * 按键监听器
     */
    class MyKeyListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {


            if (event.getAction() == KeyEvent.ACTION_UP ){
                if (event.isShiftPressed()){
                    if (resultType == 0 && keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z){
                        addResult(mathcLetter(keyCode));
                        return true;
                    }
                    return false;
                }
            }

            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                if (event.isShiftPressed() ) {//处理*#等键
                    return false;
                }
                if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {//处理数字
                    addResult(keyCode - 7 + "");
                    return true;
                }
                if (resultType == 0 && keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z){ //vin
                    addResult(matchLowercase(keyCode));
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_DEL) {       //点击删除
                    removeResult();
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_BACK){
                    resultChangeListener.onBack();
                }
                InputMethodManager imm = (InputMethodManager) mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
            return false;
        }
    }


    //恢复状态
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mResultList = savedState.saveString;
        inputIndex = mResultList.size();
        if (mResultList.isEmpty()) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            ResultView resultView = (ResultView) getChildAt(i);
            if (i > mResultList.size() - 1) {
                if (resultView != null) {
                    resultView.setmIsShowRemindLine(false);
                    resultView.updateInputState(false);
                }
                break;
            }

            if (resultView != null) {
                resultView.setmPassText(mResultList.get(i));
                resultView.updateInputState(true);
            }
        }

    }

    //保存状态
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.saveString = this.mResultList;
        return savedState;
    }


    public static class SavedState extends BaseSavedState {
        public List<String> saveString;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeList(saveString);
        }

        private SavedState(Parcel in) {
            super(in);
            if (in == null || saveString == null)
                return;
            in.readStringList(saveString);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private String mathcLetter(int keyCode){
        String code = "";
        switch (keyCode){
            case KeyEvent.KEYCODE_A:
                return "A";

            case KeyEvent.KEYCODE_B:
                return "B";

            case KeyEvent.KEYCODE_C:
                return "C";

            case KeyEvent.KEYCODE_D:
                return "D";

            case KeyEvent.KEYCODE_E:
                return "E";

            case KeyEvent.KEYCODE_F:
                return "F";

            case KeyEvent.KEYCODE_G:
                return "G";

            case KeyEvent.KEYCODE_H:
                return "H";

            case KeyEvent.KEYCODE_I:
                return "I";

            case KeyEvent.KEYCODE_J:
                return "J";

            case KeyEvent.KEYCODE_K:
                return "K";

            case KeyEvent.KEYCODE_L:
                return "L";

            case KeyEvent.KEYCODE_M:
                return "M";

            case KeyEvent.KEYCODE_N:
                return "N";

            case KeyEvent.KEYCODE_O:
                return "O";

            case KeyEvent.KEYCODE_P:
                return "P";

            case KeyEvent.KEYCODE_Q:
                return "Q";

            case KeyEvent.KEYCODE_R:
                return "R";

            case KeyEvent.KEYCODE_S:
                return "S";

            case KeyEvent.KEYCODE_T:
                return "T";

            case KeyEvent.KEYCODE_U:
                return "U";

            case KeyEvent.KEYCODE_V:
                return "V";

            case KeyEvent.KEYCODE_W:
                return "W";

            case KeyEvent.KEYCODE_X:
                return "X";
            case KeyEvent.KEYCODE_Y:
                return "Y";

            case KeyEvent.KEYCODE_Z:
                return "Z";
                default:
                    break;
        }
        return code;
    }

    private  String matchLowercase(int keyCode){
        String code = "";
        switch (keyCode){
            case KeyEvent.KEYCODE_A:
                return "a";

            case KeyEvent.KEYCODE_B:
                return "b";

            case KeyEvent.KEYCODE_C:
                return "c";

            case KeyEvent.KEYCODE_D:
                return "d";

            case KeyEvent.KEYCODE_E:
                return "e";

            case KeyEvent.KEYCODE_F:
                return "f";

            case KeyEvent.KEYCODE_G:
                return "g";

            case KeyEvent.KEYCODE_H:
                return "h";

            case KeyEvent.KEYCODE_I:
                return "i";

            case KeyEvent.KEYCODE_J:
                return "j";

            case KeyEvent.KEYCODE_K:
                return "k";

            case KeyEvent.KEYCODE_L:
                return "l";

            case KeyEvent.KEYCODE_M:
                return "m";

            case KeyEvent.KEYCODE_N:
                return "n";

            case KeyEvent.KEYCODE_O:
                return "o";

            case KeyEvent.KEYCODE_P:
                return "p";

            case KeyEvent.KEYCODE_Q:
                return "q";

            case KeyEvent.KEYCODE_R:
                return "r";

            case KeyEvent.KEYCODE_S:
                return "s";

            case KeyEvent.KEYCODE_T:
                return "t";

            case KeyEvent.KEYCODE_U:
                return "u";

            case KeyEvent.KEYCODE_V:
                return "v";

            case KeyEvent.KEYCODE_W:
                return "w";

            case KeyEvent.KEYCODE_X:
                return "x";
            case KeyEvent.KEYCODE_Y:
                return "y";

            case KeyEvent.KEYCODE_Z:
                return "z";
            default:
                break;
        }
        return code;
    }

}