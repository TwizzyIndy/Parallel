package xworks.aungkhantmyo.parallel;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * Created by Aung Khant Myo on 2/22/15.
 */

public class CustomTypefaceSpan extends TypefaceSpan {

    private final Typeface newType;

    public CustomTypefaceSpan(String family, Typeface typeface) {
        super(family);
        newType = typeface;
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        applyCustomTypeface(paint, newType);
    }

    private void applyCustomTypeface(Paint paint, Typeface tf) {


        int oldStyle;
        Typeface old = paint.getTypeface();
        if ( old == null ){
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }
        int fake = oldStyle & ~tf.getStyle();
        if((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0){
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);

    }
}
