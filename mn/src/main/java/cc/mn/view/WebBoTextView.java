package cc.mn.view;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: 仿新浪微博话题@用户
 * Date: 2015-09-22
 * Time: 11:06
 * Version 1.0
 *
 * 用法如下：
 * 1, 一般在adapter使用：setTextContent();
 * 2, 设置setOnTextViewClickListener();
 * 注:@用户时，用户后面必须有空格: @张无忌 的。。。
 */

public class WebBoTextView extends TextView {

    private int style0 = 0; //话题

    private int style1 = 1; //@用户

    private OnTextViewClickListener onTextViewClickListener = null;

    public WebBoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebBoTextView(Context context) {
        super(context);
    }

    public WebBoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置微博内容
     * @param text
     * */
    public void setTextContent(CharSequence text){
        this.setText(text);

        SpannableStringBuilder sb = new SpannableStringBuilder(this.getText());

        Pattern htPattern = getHtPattern("#\\w+#"); //话题规则
        Matcher htMatcher = htPattern.matcher(this.getText()); //话题
        setClickableSpan(sb, htMatcher, style0);

        Pattern atPattern = getHtPattern("@\\w+"); //@用户规则
        Matcher atMatcher = atPattern.matcher(this.getText());  //@用户
        setClickableSpan(sb, atMatcher, style1);

        //.......添加其他过滤规则........

    }

    /**
     * 获取正则表达式的模式
     * @param pattern
     * @return
     * */
    private Pattern getHtPattern(String pattern){
        return Pattern.compile(pattern);
    }

    /**
     * 设置某个区域的点击
     *
     * @param matcher
     * @param sb
     * */
    private void setClickableSpan(SpannableStringBuilder sb, Matcher matcher, int style){
        int i = 0;
        while (matcher.find()){
            final String key = matcher.group();
            sb.setSpan(new WBClickableSpan(style, key, i), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            this.setText(sb);
            this.setMovementMethod(LinkMovementMethod.getInstance());
            i++;
        }
    }

    public class WBClickableSpan extends ClickableSpan{

        private int style;
        private String key;
        private int index;

        /**
         * @param style 哪一种：@，#
         * @param key
         * @param index
         * */
        public WBClickableSpan(int style, String key, int index){
            this.style = style;
            this.key = key;
            this.index = index;
        }

        public void onClick(View widget) {
            if(onTextViewClickListener==null) return;
            onTextViewClickListener.clickTextView(style, key, index);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.BLUE);
            ds.setUnderlineText(false);
        }
    }

    public void setOnTextViewClickListener(OnTextViewClickListener onTextViewClickListener) {
        this.onTextViewClickListener = onTextViewClickListener;
    }

    public interface OnTextViewClickListener{
        void clickTextView(int style, String key, int index);
    }

}

