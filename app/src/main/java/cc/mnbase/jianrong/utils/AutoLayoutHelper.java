package cc.mnbase.jianrong.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import cc.mnbase.R;
import cc.mnbase.jianrong.AutoLayoutInfo;
import cc.mnbase.jianrong.attr.HeightAttr;
import cc.mnbase.jianrong.attr.MarginAttr;
import cc.mnbase.jianrong.attr.MarginBottomAttr;
import cc.mnbase.jianrong.attr.MarginLeftAttr;
import cc.mnbase.jianrong.attr.MarginRightAttr;
import cc.mnbase.jianrong.attr.MarginTopAttr;
import cc.mnbase.jianrong.attr.PaddingAttr;
import cc.mnbase.jianrong.attr.PaddingBottomAttr;
import cc.mnbase.jianrong.attr.PaddingLeftAttr;
import cc.mnbase.jianrong.attr.PaddingRightAttr;
import cc.mnbase.jianrong.attr.PaddingTopAttr;
import cc.mnbase.jianrong.attr.TextSizeAttr;
import cc.mnbase.jianrong.attr.WidthAttr;
import cc.mnbase.jianrong.conifg.AutoLayoutConifg;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-11
 * Time: 17:19
 * Version 1.0
 */

public class AutoLayoutHelper {

    private final ViewGroup mHost;

    private static final int[] LL = new int[]
            {
                    android.R.attr.textSize, // 字体大小
                    android.R.attr.padding,  // 内边距
                    android.R.attr.paddingLeft, // 内边距：左
                    android.R.attr.paddingTop,  // 内边距：上
                    android.R.attr.paddingRight, // 内边距：右
                    android.R.attr.paddingBottom, // 内边距：下
                    android.R.attr.layout_width,  // 宽
                    android.R.attr.layout_height, // 高
                    android.R.attr.layout_margin, // 外边距
                    android.R.attr.layout_marginLeft, // 外边距：左
                    android.R.attr.layout_marginTop,  // 外边距：上
                    android.R.attr.layout_marginRight, // 外边距：右
                    android.R.attr.layout_marginBottom, // 外边距：下
            };

    private static final int INDEX_TEXT_SIZE = 0;
    private static final int INDEX_PADDING = 1;
    private static final int INDEX_PADDING_LEFT = 2;
    private static final int INDEX_PADDING_TOP = 3;
    private static final int INDEX_PADDING_RIGHT = 4;
    private static final int INDEX_PADDING_BOTTOM = 5;
    private static final int INDEX_WIDTH = 6;
    private static final int INDEX_HEIGHT = 7;
    private static final int INDEX_MARGIN = 8;
    private static final int INDEX_MARGIN_LEFT = 9;
    private static final int INDEX_MARGIN_TOP = 10;
    private static final int INDEX_MARGIN_RIGHT = 11;
    private static final int INDEX_MARGIN_BOTTOM = 12;

    private static AutoLayoutConifg mAutoLayoutConifg;

    public AutoLayoutHelper(ViewGroup host){
        mHost = host;

        if(mAutoLayoutConifg == null){
            initAutoLayoutConifg(host);
        }
    }

    private void initAutoLayoutConifg(ViewGroup host){
        mAutoLayoutConifg = AutoLayoutConifg.getIntance();
        mAutoLayoutConifg.init(host.getContext());
    }

    public void abjustChildren(){
        AutoLayoutConifg.getIntance().checkParams();
        for(int i=0, n=mHost.getChildCount(); i<n; i++){
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if(params instanceof AutoLayoutParams){
                AutoLayoutInfo info = ((AutoLayoutParams) params).getAutoLayoutInfo();
                if(info != null){
                    info.fillAttrs(view);
                }
            }
        }
    }

    public static AutoLayoutInfo getAutoLayoutInfo(Context context, AttributeSet atts){
        AutoLayoutInfo info = new AutoLayoutInfo();

        TypedArray a = context.obtainStyledAttributes(atts, R.styleable.AutoLayout_Layout);

        int baseWidth = a.getInt(R.styleable.AutoLayout_Layout_layout_auto_basewidth, 0);
        int baseHeight = a.getInt(R.styleable.AutoLayout_Layout_layout_auto_baseheight, 0);
        a.recycle();

        TypedArray array = context.obtainStyledAttributes(atts, LL);

        int n = array.getIndexCount();
        for(int i=0; i < n; i++){
            int index = array.getIndex(i);
            if(!isPxVal(array.peekValue(index))) continue;

            int pxVal = 0;

            try {
                pxVal = array.getDimensionPixelOffset(index, 0);
            } catch (Exception e){
                continue;
            }

            switch (index){
                // .......
                case  INDEX_TEXT_SIZE:
                    info.addAttr(new TextSizeAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_HEIGHT:
                    info.addAttr(new HeightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_WIDTH:
                    info.addAttr(new WidthAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN:
                    info.addAttr(new MarginAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_LEFT:
                    info.addAttr(new MarginLeftAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_TOP:
                    info.addAttr(new MarginTopAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_RIGHT:
                    info.addAttr(new MarginRightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_BOTTOM:
                    info.addAttr(new MarginBottomAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING:
                    info.addAttr(new PaddingAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_LEFT:
                    info.addAttr(new PaddingLeftAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_TOP:
                    info.addAttr(new PaddingTopAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_RIGHT:
                    info.addAttr(new PaddingRightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_BOTTOM:
                    info.addAttr(new PaddingBottomAttr(pxVal, baseWidth, baseHeight));
                    break;
                // .........

            }
        }
        array.recycle();
        return info;
    }

    private static boolean isPxVal(TypedValue val){
        if(val != null && val.type == TypedValue.TYPE_DIMENSION && getComplexUnit(val.data) == TypedValue.COMPLEX_UNIT_PX){
            return true;
        }
        return false;
    }

    private static int getComplexUnit(int data){
        return TypedValue.COMPLEX_UNIT_MASK & (data >> TypedValue.COMPLEX_UNIT_SHIFT);
    }

    private static boolean isPxVal(String val){
        if(val.endsWith("px")){
            return true;
        }
        return false;
    }

    public interface AutoLayoutParams {
        AutoLayoutInfo getAutoLayoutInfo();
    }
}
