package com.next.easynavigition.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.next.easynavigition.R;
import com.next.easynavigition.adapter.ViewPagerAdapter;
import com.next.easynavigition.constant.Anim;
import com.next.easynavigition.utils.SmallUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jue on 2018/6/1.
 */

public class NavigitionAddBar extends LinearLayout {

    private OnItemClickListener mOnItemClickListener;
    //private OnDoubleClickListener mOnDoubleClickListener;

    private OnAddClickListener onAddClickListener;
    private int addImageRes;
    private RelativeLayout containerLayout;
    private float addContainerHeight;

    //Tab数量
    private int tabCount = 0;

    private LinearLayout navigitionLayout;
    private RelativeLayout mLinearLayout;

    //未选中Tab字体颜色
    private int normalTextColor = 0xff555555;
    //选中字体颜色
    private int selectTextColor = 0xff555555;
    //分割线高度
    private float lineHeight = 1;
    //分割线颜色
    private int lineColor = 0xcccccc;
    //消息红点字体大小
    private float msgPointTextSize = SmallUtil.sp2px(getContext(), 9);
    //消息红点大小
    private float msgPointSize = SmallUtil.dip2px(getContext(), 18);
    //提示红点大小
    private float hintPointSize = SmallUtil.dip2px(getContext(), 6);
    //提示红点距Tab图标的距离
    private float hintPointLeft = 0;
    //消息红点距Tab图标的距离
    private float msgPointLeft = -SmallUtil.dip2px(getContext(), 5);
    //Tab文字距Tab图标的距离
    private float tabTextTop = SmallUtil.dip2px(getContext(), 2);

    private int doubleClickPositon = -1;

    //Tab图标大小
    private float iconSize = SmallUtil.dip2px(getContext(), 20);

    //红点距顶部的距离
    private float redPointTop = SmallUtil.dip2px(getContext(), 5);

    //消息红点距顶部的距离
    private float msgPointTop = SmallUtil.dip2px(getContext(), 3);

    //Tab文字大小
    private float tabTextSize = SmallUtil.sp2px(getContext(), 10);

    //未选图片
    private int[] normalIcon;

    //已选图片集合
    private int[] selectIcon;

    //红点集合
    private List<View> hintPointList = new ArrayList<>();

    //消息数量集合
    private List<TextView> msgPointList = new ArrayList<>();

    //底部Image集合
    private List<ImageView> imageViewList = new ArrayList<>();

    //底部Text集合
    private List<TextView> textViewList = new ArrayList<>();

    //底部TabLayout（除中间加号）
    private List<View> tabList = new ArrayList<>();

    private CustomViewPager mViewPager;
    //private GestureDetector detector;

    private Techniques anim = null;
    private boolean smoothScroll = false;
    private float addImageSize = SmallUtil.dip2px(getContext(), 36);
    private ViewGroup addViewLayout;

    public NavigitionAddBar(Context context) {
        super(context);

        initViews(context, null);
    }

    public NavigitionAddBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();

        initViews(context, attrs);
    }

    private void init() {

       /* detector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mOnDoubleClickListener != null)
                    mOnDoubleClickListener.onDoubleClickEvent(doubleClickPositon);
                return super.onDoubleTap(e);
            }
        });*/
    }

    private void initViews(Context context, AttributeSet attrs) {

        mLinearLayout = (RelativeLayout) View.inflate(context, R.layout.container_layout, null);

        addViewLayout  = mLinearLayout.findViewById(R.id.add_view_ll);
        containerLayout = mLinearLayout.findViewById(R.id.container_rl);
        navigitionLayout = mLinearLayout.findViewById(R.id.navigition_ll);
        mViewPager = mLinearLayout.findViewById(R.id.mViewPager);

        View common_horizontal_line = mLinearLayout.findViewById(R.id.common_horizontal_line);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.NavigitionBar);
        if (attributes != null) {
            float navigitionHeight = attributes.getDimensionPixelSize(R.styleable.NavigitionBar_navigition_height, SmallUtil.dip2px(context, 48));
            int navigitionBackground = attributes.getColor(R.styleable.NavigitionBar_navigition_background, 0xffffffff);
            navigitionLayout.setBackgroundColor(navigitionBackground);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) navigitionLayout.getLayoutParams();
            params.height = (int) navigitionHeight;
            navigitionLayout.setLayoutParams(params);

            tabTextSize = attributes.getDimension(R.styleable.NavigitionBar_tab_textsize, SmallUtil.sp2px(context, 10));
            redPointTop = attributes.getDimension(R.styleable.NavigitionBar_red_point_top, SmallUtil.dip2px(context, 5));
            msgPointTop = attributes.getDimension(R.styleable.NavigitionBar_msg_point_top, SmallUtil.dip2px(context, 3));
            iconSize = attributes.getDimension(R.styleable.NavigitionBar_tab_icon_size, SmallUtil.dip2px(context, 20));
            hintPointSize = attributes.getDimension(R.styleable.NavigitionBar_hint_point_size, SmallUtil.dip2px(context, 6));
            msgPointSize = attributes.getDimension(R.styleable.NavigitionBar_msg_point_size, SmallUtil.dip2px(context, 18));
            hintPointLeft = attributes.getDimension(R.styleable.NavigitionBar_hint_point_left, SmallUtil.dip2px(context, 0));
            msgPointLeft = attributes.getDimension(R.styleable.NavigitionBar_msg_point_left, -SmallUtil.dip2px(context, 5));
            msgPointTextSize = attributes.getDimension(R.styleable.NavigitionBar_msg_point_textsize, SmallUtil.sp2px(context, 9));
            tabTextTop = attributes.getDimension(R.styleable.NavigitionBar_tab_text_top, SmallUtil.dip2px(context, 2));
            addImageSize = attributes.getDimension(R.styleable.NavigitionBar_add_image_size, SmallUtil.dip2px(context, 36));
            addImageRes = attributes.getInteger(R.styleable.NavigitionBar_add_image_res, R.drawable.add_image);

            addContainerHeight = attributes.getDimension(R.styleable.NavigitionBar_addNavigition_height, navigitionHeight);
            LayoutParams addLayoutParams = (LayoutParams) containerLayout.getLayoutParams();
            addLayoutParams.height = (int) addContainerHeight;
            containerLayout.setLayoutParams(addLayoutParams);

            lineHeight = attributes.getDimension(R.styleable.NavigitionBar_line_height, 1);
            lineColor = attributes.getColor(R.styleable.NavigitionBar_line_color, 0xcccccccc);
            common_horizontal_line.setBackgroundColor(lineColor);

            RelativeLayout.LayoutParams lineParams = (RelativeLayout.LayoutParams) common_horizontal_line.getLayoutParams();
            lineParams.height = (int) lineHeight;
            common_horizontal_line.setLayoutParams(lineParams);

            normalTextColor = attributes.getColor(R.styleable.NavigitionBar_tab_normal_color, 0xff555555);
            selectTextColor = attributes.getColor(R.styleable.NavigitionBar_tab_select_color, 0xff555555);

            attributes.recycle();
        }

        addView(mLinearLayout);
    }


    public CustomViewPager getmViewPager() {
        return mViewPager;
    }


    /**
     * @param tabText                Tab文字
     * @param normalIcon             未选中Tab图标
     * @param selectIcon             选中时图标
     * @param fragments              Fragment集合
     * @param supportFragmentManager 必传
     * @param anim                   点击时Tab的动画
     * @param smoothScroll           ViewPager滑动动画
     * @param mOnItemClickListener   点击Tab的监听
     */
    public void setData(String[] tabText, int[] normalIcon, int[] selectIcon, List<Fragment> fragments, int addIcon,
                        FragmentManager supportFragmentManager, Anim anim, final boolean smoothScroll, final OnItemClickListener mOnItemClickListener, final OnAddClickListener onAddClickListener) {
        if ((tabText.length != normalIcon.length) || (tabText.length != selectIcon.length) || (normalIcon.length != selectIcon.length))
            return;

        this.mOnItemClickListener = mOnItemClickListener;
        this.onAddClickListener = onAddClickListener;
        if (anim != null)
            this.anim = anim.getYoyo();
        tabCount = tabText.length + 1;
        int index = 0;

        this.normalIcon = normalIcon;
        this.selectIcon = selectIcon;
        this.smoothScroll = smoothScroll;
        addImageRes = addIcon;

        hintPointList.clear();
        hintPointList.clear();
        imageViewList.clear();
        textViewList.clear();
        tabList.clear();

        navigitionLayout.removeAllViews();

        ViewPagerAdapter adapter = new ViewPagerAdapter(supportFragmentManager, fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < tabCount; i++) {

            if (i == tabCount / 2) {
                RelativeLayout addLayout = new RelativeLayout(getContext());
                RelativeLayout.LayoutParams addParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                addParams.width = SmallUtil.getScreenWidth(getContext()) / tabCount;
                //addParams.bottomMargin = SmallUtil.dip2px(getContext(),100);
                addLayout.setLayoutParams(addParams);
                navigitionLayout.addView(addLayout);
            } else {

                if (i > 1) {
                    index = i - 1;
                } else {
                    index = i;
                }

                View itemView = View.inflate(getContext(), R.layout.navigition_tab_layout, null);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params.width = SmallUtil.getScreenWidth(getContext()) / tabCount;

                itemView.setLayoutParams(params);
                itemView.setId(index);

                TextView text = itemView.findViewById(R.id.tab_text_tv);
                ImageView icon = itemView.findViewById(R.id.tab_icon_iv);
                LayoutParams iconParams = (LayoutParams) icon.getLayoutParams();
                iconParams.width = (int) iconSize;
                iconParams.height = (int) iconSize;
                icon.setLayoutParams(iconParams);

                imageViewList.add(icon);
                textViewList.add(text);

                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onItemClickEvent(view, view.getId());
                        mViewPager.setCurrentItem(view.getId(), smoothScroll);
                    }
                });

                LayoutParams textParams = (LayoutParams) text.getLayoutParams();
                textParams.topMargin = (int) tabTextTop;
                text.setLayoutParams(textParams);
                text.setText(tabText[index]);
                text.setTextSize(SmallUtil.px2sp(getContext(), tabTextSize));


                View hintPoint = itemView.findViewById(R.id.red_point);

                //提示红点
                RelativeLayout.LayoutParams hintPointParams = (RelativeLayout.LayoutParams) hintPoint.getLayoutParams();
                hintPointParams.topMargin = (int) redPointTop;
                hintPointParams.width = (int) hintPointSize;
                hintPointParams.height = (int) hintPointSize;
                hintPointParams.leftMargin = (int) hintPointLeft;
                hintPoint.setLayoutParams(hintPointParams);

                //消息红点
                TextView msgPoint = itemView.findViewById(R.id.msg_point_tv);
                msgPoint.setTextSize(SmallUtil.px2sp(getContext(), msgPointTextSize));
                RelativeLayout.LayoutParams msgPointParams = (RelativeLayout.LayoutParams) msgPoint.getLayoutParams();
                msgPointParams.topMargin = (int) msgPointTop;
                msgPointParams.width = (int) msgPointSize;
                msgPointParams.height = (int) msgPointSize;
                msgPointParams.leftMargin = (int) msgPointLeft;
                msgPoint.setLayoutParams(msgPointParams);


                hintPointList.add(hintPoint);
                msgPointList.add(msgPoint);


                tabList.add(itemView);
                navigitionLayout.addView(itemView);
            }
        }


        RelativeLayout addLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams addParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        addParams.width = SmallUtil.getScreenWidth(getContext()) / tabCount;
        //addParams.height = (int) addContainerHeight;
        addParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        // addLayout.setBackgroundResource(R.drawable.oval_white);
        // addLayout.setPadding(SmallUtil.dip2px(getContext(),5),SmallUtil.dip2px(getContext(),5),SmallUtil.dip2px(getContext(),5),SmallUtil.dip2px(getContext(),5));
        addLayout.setLayoutParams(addParams);

        ImageView addImage = new ImageView(getContext());
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageParams.width = (int) addImageSize;
        imageParams.height = (int) addImageSize;
        //imageParams.bottomMargin = SmallUtil.dip2px(getContext(),100);
        imageParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        addImage.setLayoutParams(imageParams);

        addImage.setImageResource(addImageRes);
        addImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddClickListener.OnAddClickEvent(view);
               /* if (view.getTag()==null||(int)view.getTag() == 0) {
                    //view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate_left));
                    view.setTag(1);
                    addViewLayout.setVisibility(VISIBLE);
                } else {
                    //view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_rotate_right));
                    view.setTag(0);
                    addViewLayout.setVisibility(GONE);
                }*/
            }
        });

        addLayout.addView(addImage, imageParams);
        containerLayout.addView(addLayout, addParams);

        for (int i = 0; i < imageViewList.size(); i++) {
            if (i == 0) {
                imageViewList.get(i).setImageResource(selectIcon[i]);
                textViewList.get(i).setTextColor(selectTextColor);
            } else {
                imageViewList.get(i).setImageResource(normalIcon[i]);
                textViewList.get(i).setTextColor(normalTextColor);
            }
        }
    }

    public void setAddViewLayout(View addViewLayout) {
        FrameLayout.LayoutParams addParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addViewLayout.addView(addViewLayout,addParams);
    }

    public ViewGroup getAddViewLayout() {
        return addViewLayout;
    }

    /*  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        if (tabCount < 1) {
            return;
        }
        for (int i = 0; i < tabCount; i++) {
            final int finalI = i;
            navigitionLayout.getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClickEvent(finalI);
                }
            });
        }
    }*/

    /* public void setOnDoubleClickListener(OnDoubleClickListener mOnDoubleClickListener) {
         this.mOnDoubleClickListener = mOnDoubleClickListener;
         if (tabCount < 1) {
             return;
         }
         for (int i = 0; i < tabCount; i++) {
             final int finalI = i;
             navigitionLayout.getChildAt(i).setOnTouchListener(new OnTouchListener() {
                 @Override
                 public boolean onTouch(View v, MotionEvent event) {
                     doubleClickPositon = finalI;
                     return detector.onTouchEvent(event);
                 }
             });
         }
     }
 */

    /**
     * 选中第几项
     *
     * @param position
     */
    public void selectPosition(int position) {
        for (int i = 0; i < imageViewList.size(); i++) {
            if (i == position) {
                if (anim != null)
                    YoYo.with(anim).duration(300).playOn(tabList.get(i));
                imageViewList.get(i).setImageResource(selectIcon[i]);
                textViewList.get(i).setTextColor(selectTextColor);
            } else {
                imageViewList.get(i).setImageResource(normalIcon[i]);
                textViewList.get(i).setTextColor(normalTextColor);
            }
        }
    }


    /**
     * 设置是否显示小红点
     *
     * @param position 第几个tab
     * @param isShow   是否显示
     */
    public void setHintPoint(int position, boolean isShow) {
        if (hintPointList == null || hintPointList.size() < (position + 1))
            return;
        if (isShow) {
            hintPointList.get(position).setVisibility(VISIBLE);
        } else {
            hintPointList.get(position).setVisibility(GONE);
        }
    }


    /**
     * 设置消息数量
     *
     * @param position 第几个tab
     * @param count    显示的数量  99个以上显示99+  少于1则不显示
     */
    public void setMsgPointCount(int position, int count) {
        if (msgPointList == null || msgPointList.size() < (position + 1))
            return;
        if (count > 99) {
            msgPointList.get(position).setText("99+");
            msgPointList.get(position).setVisibility(VISIBLE);
        } else if (count < 1) {
            msgPointList.get(position).setVisibility(GONE);
        } else {
            msgPointList.get(position).setText(count + "");
            msgPointList.get(position).setVisibility(VISIBLE);
        }
    }

    /**
     * 清空所有提示红点
     */
    public void clearAllHintPoint() {
        for (int i = 0; i < hintPointList.size(); i++) {
            hintPointList.get(i).setVisibility(GONE);
        }
    }

    /**
     * 清空所有消息红点
     */
    public void clearAllMsgPoint() {
        for (int i = 0; i < msgPointList.size(); i++) {
            msgPointList.get(i).setVisibility(GONE);
        }
    }


    public interface OnItemClickListener {
        void onItemClickEvent(View view, int position);
    }

    public interface OnAddClickListener {
        void OnAddClickEvent(View view);
    }

  /*  //双击事件
    interface OnDoubleClickListener {
        void onDoubleClickEvent(int position);
    }*/
}