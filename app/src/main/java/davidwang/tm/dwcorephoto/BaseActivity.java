package davidwang.tm.dwcorephoto;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import davidwang.tm.model.ImageBrowseBean;
import davidwang.tm.model.ImageBrowseShareBean;
import davidwang.tm.tools.ImageLoaders;

/**
 * Created by DavidWang on 15/8/31.
 */
public class BaseActivity extends AppCompatActivity {

    private Toast mToast;
    // 屏幕宽度
    public float Width;
    // 屏幕高度
    public float Height;
    protected View forgroundView;
    protected ImageView showimg;
    protected ViewGroup rootView;

//    private final Spring mSpring = SpringSystem
//            .create()
//            .createSpring()
//            .addListener(new ExampleSpringListener());

    protected ImageBrowseShareBean shareBean;
    protected ImageBrowseBean imageInfo;
    private float size, size_h, minSize;

    private float img_w;
    private float img_h;

    //原图高
    private float y_img_h;
    protected float to_x = 0;
    protected float to_y = 0;
    private float tx;
    private float ty;
    private int statusBarHeight;
    private int titleBarHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Width = dm.widthPixels;
        Height = dm.heightPixels;
        setToolbar(0xff009688);
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        //statusBarHeight是上面所求的状态栏的高度
        titleBarHeight = contentTop - statusBarHeight;
    }

    protected void setToolbar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
//        int color = Color.argb(255, Color.red(255), Color.green(255), Color.blue(255));
        tintManager.setTintColor(color);

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 添加头部
     */
    protected void AddToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
    }


    /**
     * 获取资源
     */
    protected void findID() {
        rootView = (RelativeLayout) findViewById(R.id.rl_root_view);
    }

    /**
     * 监听
     */
    protected void Listener() {
    }

    /**
     * 对传递数据处
     */
    protected void initIntent() {

    }

    /**
     * 初始
     */
    public void InData() {
    }

    /**
     * 显示提示信息
     *
     * @param text 提示文本
     */
    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 清空Toast
     */
    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    protected void showShareView() {
        showimg = new ImageView(this);
        showimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoaders.setsendimg(imageInfo.getUrl(), showimg);
        img_w = shareBean.getWidth();
        img_h = shareBean.getHeight();
        size = Width / img_w;
        y_img_h = imageInfo.getHeight() * Width / imageInfo.getWidth();
        size_h = y_img_h / img_h;
        minSize = Math.min(size, size_h);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams((int) img_w, (int) img_h);
        showimg.setLayoutParams(p);
        p.setMargins((int) (shareBean.getX()), (int) (shareBean.getY()), (int) (Width - (shareBean.getX() + img_w)), (int) (Height - (shareBean.getY() + img_h)));
        showimg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        rootView.addView(showimg);
        rootView.setVisibility(View.VISIBLE);
        showimg.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                showEntryAnim();
            }
        }, 10);
    }


//    protected void setShowimage(){
//        if (mSpring.getEndValue() == 0){
//            mSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(170, 5));
//            tx = Width/2 - (bdInfo.x+img_w/2);
//            ty = Height/2-(bdInfo.y+img_h/2);
//            MoveView();
//            return;
//        }
//        mSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(1, 5));
//        mSpring.setEndValue(0);
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                //execute the task
//                MoveBackView();
//            }
//        }, 300);
//
//    }

//    private class ExampleSpringListener implements SpringListener {
//
//        @Override
//        public void onSpringUpdate(Spring spring) {
//            double CurrentValue = spring.getCurrentValue();
//            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(CurrentValue, 0, 1, 1, size);
//            float mapy = (float) SpringUtil.mapValueFromRangeToRange(CurrentValue, 0, 1, 1, size_h);
//            showimg.setScaleX(mappedValue);
//            showimg.setScaleY(mapy);
//            if (CurrentValue == 1) {
//                EndSoring();
//            }
//        }
//
//        @Override
//        public void onSpringAtRest(Spring spring) {
//
//        }
//
//        @Override
//        public void onSpringActivate(Spring spring) {
//
//        }
//
//        @Override
//        public void onSpringEndStateChange(Spring spring) {
//
//        }
//    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    protected void showEntryAnim() {
        tx = Width / 2 - (shareBean.getX() + img_w / 2);
        ty = Height / 2 - (shareBean.getY() + img_h / 2);
        moveView();
    }

    protected void showExitAnim() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                moveBackView();
            }
        }, 10);
    }

    private void moveView() {
        forgroundView.setVisibility(View.VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(showimg, "translationX", tx).setDuration(400),
                ObjectAnimator.ofFloat(showimg, "translationY", ty).setDuration(400),
                ObjectAnimator.ofFloat(rootView, "alpha", 1).setDuration(300),
                ObjectAnimator.ofFloat(showimg, "scaleX", 1, size).setDuration(300),
                ObjectAnimator.ofFloat(showimg, "scaleY", 1, minSize).setDuration(300),
                ObjectAnimator.ofFloat(showimg, "alpha", 1f, 0f).setDuration(200),
                ObjectAnimator.ofFloat(forgroundView, "alpha", 0f, 1f).setDuration(300)
        );
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                endEntry();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();

    }

    private void moveBackView() {
        if (shareBean == null) {
            endExit();
            return;
        }
        rootView.setBackgroundColor(getResources().getColor(R.color.transparent));
        showimg.setVisibility(View.VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(forgroundView, "alpha", 0f).setDuration(100),
                ObjectAnimator.ofFloat(showimg, "alpha", 0f, 0.6f, 0.4f).setDuration(300),
                ObjectAnimator.ofFloat(showimg, "translationX", to_x).setDuration(400),
                ObjectAnimator.ofFloat(showimg, "translationY", to_y).setDuration(400),
                ObjectAnimator.ofFloat(showimg, "scaleX", 1, 0f).setDuration(300),
                ObjectAnimator.ofFloat(showimg, "scaleY", 1, 0f).setDuration(300)
        );
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                endExit();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();

    }

    protected void endEntry() {
        if (showimg != null) {
            showimg.setVisibility(View.GONE);
        }
    }

    protected void endExit() {
        forgroundView.setVisibility(View.GONE);
    }
}
