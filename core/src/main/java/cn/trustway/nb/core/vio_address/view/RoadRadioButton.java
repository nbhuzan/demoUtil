package cn.trustway.nb.core.vio_address.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import cn.trustway.nb.core.R;


/**
 * Created by huzan  2017/4/1 13:05.
 * 描述：
 */

public class RoadRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    private Rect rectTitle;
    private Paint paintTitle;
    private String textTitle = "";
    private int colorTitle = Color.GRAY;

    private ColorStateList titleColorStateList;
    private ColorStateList messageColorStateList;
    private Rect rectMessage;
    private Paint paintMessage;
    private String textMessage = "";
    private int colorMessage = Color.BLACK;

    private int maxWidth;

    int width = 0;
    int heigh = 0;

    public RoadRadioButton(Context context) {
        this(context, null);
    }

    public RoadRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.radioButtonStyle);
    }

    public RoadRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.RoadRadioButton);

        int n = t.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = t.getIndex(i);
            if (index == R.styleable.RoadRadioButton_msgColor) {
                messageColorStateList = t.getColorStateList(index);

            } else if (index == R.styleable.RoadRadioButton_titleColor) {
                titleColorStateList = t.getColorStateList(index);

            } else if (index == R.styleable.RoadRadioButton_msgText) {
                textMessage = t.getString(index);

            } else if (index == R.styleable.RoadRadioButton_titleText) {
                textTitle = t.getString(index);

            } else {
                System.out.println("找不到参数："+"context = [" + context + "], attrs = [" + attrs + "], defStyleAttr = [" + defStyleAttr + "]");
            }
        }
        t.recycle();
        rectTitle = new Rect();
        paintTitle = new Paint();
        paintTitle.setDither(true);
        paintTitle.setAntiAlias(true);
        rectMessage = new Rect();
        paintMessage = new Paint();
        paintMessage.setDither(true);
        paintMessage.setAntiAlias(true);

        setPadding(0, 15, 0, 15);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heighSize = MeasureSpec.getSize(heightMeasureSpec);
        int heighMode = MeasureSpec.getMode(heightMeasureSpec);
        paintTitle.setColor(colorTitle);
        paintTitle.setTextSize(getResources().getDimension(R.dimen.y12));
        paintTitle.getTextBounds(textTitle, 0, textTitle.length(), rectTitle);


        paintMessage.setColor(colorMessage);
        paintMessage.setTextSize(getResources().getDimension(R.dimen.y16));
        paintMessage.getTextBounds(textMessage, 0, textMessage.length(), rectMessage);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            maxWidth = (int) Math.max(paintTitle.measureText(textTitle), paintMessage.measureText(textMessage));
            width = getPaddingLeft() + getPaddingRight() + maxWidth;
        }

        if (heighMode == MeasureSpec.EXACTLY) {
            heigh = heighSize;
        } else {
            heigh = (int) (getPaddingTop() + getPaddingBottom() + 20 + rectTitle.height() + rectMessage.height());
        }
        if(heigh<getResources().getDimension(R.dimen.y48)){
            heigh = (int) getResources().getDimension(R.dimen.y48);
        }

        setMeasuredDimension(width, heigh);
    }

    int titleX = 0;
    int titleY = 0;
    int messageX = 0;
    int messageY = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paintTitle.setColor(colorTitle);
        paintTitle.setTextSize(getResources().getDimension(R.dimen.y12));
        paintTitle.getTextBounds(textTitle, 0, textTitle.length(), rectTitle);


        paintMessage.setColor(colorMessage);
        paintMessage.setTextSize(getResources().getDimension(R.dimen.y16));
        paintMessage.getTextBounds(textMessage, 0, textMessage.length(), rectMessage);

        if(titleColorStateList !=null) {
            paintTitle.setColor(titleColorStateList.getColorForState(getDrawableState(), 0));
        }
        if(messageColorStateList!=null){
            paintMessage.setColor(messageColorStateList.getColorForState(getDrawableState(),0));
        }

        titleY = getPaddingTop() - (int)paintTitle.getFontMetrics().ascent;
        titleX = getWidth() / 2 - (int)paintTitle.measureText(textTitle) / 2;
        canvas.drawText(textTitle, titleX, titleY, paintTitle);

        int messageY = titleY + rectTitle.height() + 20;
        int messageX = getWidth() / 2 - (int)paintMessage.measureText(textMessage) / 2;
        canvas.drawText(textMessage, messageX, messageY, paintMessage);
    }



    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
//        postInvalidate();
        requestLayout();
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
//        postInvalidate();
        requestLayout();
    }
}
