package cc.crm.dropdownmenu;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * 项目名称：DropDownMenu
 * 类描述：
 * 创建人：张志华
 * 创建时间：2017/8/10 13:48@郑州卡卡罗特科技有限公司
 * 修改人：Administrator
 * 修改时间：2017/8/10 13:48
 * 修改备注：
 *
 * @version 1
 *          相关联类：
 * @see
 * @see
 */

public class DropDownMenu extends FrameLayout {
    boolean expend = false;
    boolean isanim = false;
    private int dp3,dp8,dp5;


    @ColorInt int selectorColor,defColor;
    @DrawableRes int tvBackground;


    public void setItemClickListener(MyItemClickListener<String> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    MyItemClickListener<String> itemClickListener;

    private DropDownMenu(@NonNull Context context) {
        super(context);
        initView(null);
    }


    public DropDownMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView( attrs);
    }

    public DropDownMenu(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    Handler myHandler = new Handler() {
        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                requestLayout();
                postInvalidate();
            } else if (msg.what == 2) {
                onChooseChange();
            }

        }
    };

    private void onChooseChange() {
        bringChildToFront(currv);

        if (itemClickListener!=null){
            String text="";
            if (currv instanceof TextView){
                text= ((TextView)currv).getText().toString();
            }
            itemClickListener.onItemViewClick(currv,text,0,0);
        }
    }

    private void initView( AttributeSet attrs) {
        dp3= (int) dp2px(3f);
        dp5= (int) dp2px(5f);
        dp8= (int) dp2px(8f);
        if (attrs!=null){
            TypedArray _TypedArray = getContext().getTheme().obtainStyledAttributes(attrs,  R.styleable.DropDownMenu, 0, 0);
            try {
                selectorColor = _TypedArray.getColor(R.styleable.DropDownMenu_ddnenu_item_select_color,getResources().getColor(R.color.ddmenu_color_primary));
                defColor = _TypedArray.getColor(R.styleable.DropDownMenu_ddnenu_item_defaut_color,getResources().getColor(R.color.ddmenu_text_color));
                tvBackground= _TypedArray.getResourceId(R.styleable.DropDownMenu_ddnenu_item_background,R.drawable.ddmenu_statistical_car_bg);
            } finally {
                _TypedArray.recycle();
            }
        }
        for (int i=0;i<getChildCount();i++){
            getChildAt(i).setTag(i);
        }
    }



    public void setData(String... datas){
        /*for (int i = 0; i < datas.length; i++) {
           TextView tv=new TextView(getContext());
//            tv.setBackgroundResource(tvBackground);
           tv.setPadding(dp8,dp5,dp8,dp5);
           tv.setText(datas[i]);
           tv.setTag(i);
            MarginLayoutParams mlp=  new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
           mlp.setMargins(0,dp3,0,dp3);
           tv.setLayoutParams(mlp);
           addView(tv);

        }*/
    }
    public  float dp2px( float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    View currv;
    private OnClickListener cclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            currv = v;
            toggle();
        }
    };

    /**
     * {@inheritDoc}
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;

        int cCount = getChildCount();

        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;


        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */

        for(int i=0;i<getChildCount();i++){
            View childView = getChildAt(i);
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            cWidth = childView.getMeasuredWidth() + cParams.leftMargin + cParams.rightMargin;
            cHeight = childView.getMeasuredHeight() + cParams.topMargin + cParams.bottomMargin;
            if (width<cWidth)
            width = cWidth;
        }

        if (expend) {//如果展开的话高度是子view的累加高度
            height = (cHeight + cParams.bottomMargin + cParams.topMargin) * cCount;
        } else {//如果是闭合的话高度是折叠的高度
            if (isanim) {
                height = (cHeight + cParams.bottomMargin + cParams.topMargin) * cCount;
            } else {
                height = cHeight + cParams.bottomMargin + cParams.topMargin * cCount;
            }
        }

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }

    /**
     * {@inheritDoc}
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         */
        for (int i = 0; i < cCount; i++) {
            TextView childView = (TextView) getChildAt(i);
            if (i==cCount-1){
                childView.setTextColor(selectorColor);
            }else {
                childView.setTextColor(defColor);
            }
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            childView.setClickable(true);
            childView.setOnClickListener(cclick);
            int cl = 0, ct = 0, cr = 0, cb = 0;
            cr = cl + cWidth;
            if (expend) {
                ct = (int) ((cHeight + cParams.topMargin + cParams.bottomMargin) * (cCount - 1 - i) * af);
                cb = cHeight + ct;
            } else {
                ct = (int) (((cHeight + cParams.topMargin + cParams.bottomMargin) * i * af) + (cParams.topMargin) * (cCount - 1 - i));
                cb = cHeight + ct;
            }
            childView.layout(cl, ct, cr, cb);



        }
    }

    public void toggle() {
        if (!expend) {
            getExpendAnim().start();
        } else {
            getCloseAnim().start();
        }

    }

    public void toggle(boolean expend) {
        if (this.expend==expend){
            return;
        }
        this.expend = expend;
        if (!expend) {
            getExpendAnim().start();
        } else {
            getCloseAnim().start();
        }
    }
    public void reset(){
       View fcv= findViewWithTag(0);
        currv=fcv;
        bringChildToFront(currv);
       /*for (int i=0;i<getChildCount();i++){
           if (getChildAt(i) instanceof TextView){
               EmsModleTextView v= (EmsModleTextView) getChildAt(i);
               if (v.getModel().equals("l")){
                   currv=v;
                   bringChildToFront(currv);
                   return;
               }
           }
       }*/
       toggle(true);
    }

    ValueAnimator vanimator;

    private ValueAnimator getExpendAnim() {
        if (vanimator == null) {
            vanimator = new ValueAnimator();
            vanimator.setFloatValues(0, 1);
            vanimator.setInterpolator(new OvershootInterpolator());
            vanimator.setDuration(300);
            vanimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    af = (float) animation.getAnimatedValue();
                    myHandler.sendEmptyMessage(1);

                }
            });
            vanimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    expend = true;
                    isanim = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    expend = true;
                    isanim = false;

                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    expend = true;
                    isanim = false;

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return vanimator;
    }

    ValueAnimator closeAnim;
    float af = 0;

    private ValueAnimator getCloseAnim() {
        if (closeAnim == null) {
            closeAnim = new ValueAnimator();
            closeAnim.setFloatValues(1, 0);
            closeAnim.setDuration(300);
            closeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    af = (float) animation.getAnimatedValue();
                    myHandler.sendEmptyMessage(1);
                }
            });
            closeAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isanim = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    expend = false;
                    isanim = false;
                    myHandler.sendEmptyMessage(2);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    expend = false;
                    isanim = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return closeAnim;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (vanimator != null) {
            vanimator.cancel();
            vanimator = null;
        }
    }

    public interface MyItemClickListener<T> {
      void  onItemViewClick(View v, T t, int position, int type);
    }
}
