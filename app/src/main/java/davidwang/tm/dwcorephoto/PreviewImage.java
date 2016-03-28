package davidwang.tm.dwcorephoto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import davidwang.tm.model.ImageBrowseBean;
import davidwang.tm.model.ImageBrowseParam;
import davidwang.tm.tools.ImageLoaders;
import davidwang.tm.tools.ToastUtils;
import davidwang.tm.view.HackyViewPager;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

public class PreviewImage extends BaseActivity implements OnPageChangeListener {
    public static final String VIEW_PHOTOS = "view_photos";
    public final static int PHOTO_BROWSE_TYPE_MIX = 0;
    public final static int PHOTO_BROWSE_TYPE_LIST = 1;
    public final static int PHOTO_BROWSE_TYPE_GRID = 2;
    public final static int PHOTO_BROWSE_TYPE_MIX_GRID = 3;


    private int index = 0;
    private ViewPager viewpager;
    private ArrayList<ImageBrowseBean> browseBeans;

    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private SamplePagerAdapter pagerAdapter;

    private int type;

    private LinearLayout AddLayout;
    private View moveView;
    private RelativeLayout addrelative;

    public static Intent newIntent(Context context, ImageBrowseParam beans) {
        Intent intent = new Intent(context, PreviewImage.class);
        intent.putExtra(PreviewImage.VIEW_PHOTOS, beans);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browseimage);
        ImageBrowseParam browseParam = (ImageBrowseParam) getIntent().getSerializableExtra(PreviewImage.VIEW_PHOTOS);
        if (browseParam != null) {
            browseBeans = browseParam.getBrowseBeanList();
            index = browseParam.getIndex();
            type = browseParam.getType();
            if (browseBeans != null && !browseBeans.isEmpty()) {
                if (index >= browseBeans.size()) {
                    ToastUtils.showLong("图片索引有误！");
                    finish();
                    return;
                }
                imageInfo = browseBeans.get(index);
            }
            shareBean = browseParam.getShareBean();
        }
        if (browseBeans == null) {
            browseBeans = new ArrayList<>();
        }
        findID();
        initWidget();
        showShareView();

        setToolbar(0xff000000);
        AddInstructionsView();
    }

    @Override
    public void findID() {
        // TODO Auto-generated method stub
        super.findID();
        viewpager = (HackyViewPager) findViewById(R.id.bi_viewpager);
        forgroundView = viewpager;
        AddLayout = (LinearLayout) findViewById(R.id.AddLayout);
        moveView = findViewById(R.id.moveView);
        addrelative = (RelativeLayout) findViewById(R.id.addrelative);
    }

    @Override
    public void Listener() {
        // TODO Auto-generated method stub
        super.Listener();
        viewpager.addOnPageChangeListener(this);
    }

    public void initWidget() {
        forgroundView = viewpager;
        pagerAdapter = new SamplePagerAdapter();
        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(index);
        moveView.setX(dip2px(10) * index);
        if (browseBeans.size() == 0) {
            addrelative.setVisibility(View.GONE);
        }
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
        moveView.setX(dip2px(5) + dip2px(10) * arg0 + dip2px(10) * arg1);

    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        if (showimg == null) {
            return;
        }
        ImageBrowseBean info = browseBeans.get(position);
        ImageLoaders.setsendimg(info.getUrl(), showimg);
        if (type == PreviewImage.PHOTO_BROWSE_TYPE_LIST) {
            int move_index = position - index;
            to_x = move_index * shareBean.getWidth();
        } else if (type == PreviewImage.PHOTO_BROWSE_TYPE_GRID || type == PreviewImage.PHOTO_BROWSE_TYPE_MIX_GRID) {
            // 默认是3列一行
            int a = index / 3;
            int b = index % 3;
            int a1 = position / 3;
            int b1 = position % 3;
            int gridPadding;
            if (type == PreviewImage.PHOTO_BROWSE_TYPE_MIX_GRID) {
                gridPadding = dip2px(1);
            } else {
                gridPadding = dip2px(2);
            }
            to_y = (a1 - a) * shareBean.getHeight() + (a1 - a) * gridPadding;
            to_x = (b1 - b) * shareBean.getWidth() + (b1 - b) * gridPadding;
        }
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return browseBeans.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            String path = browseBeans.get(position).getUrl();
            ImageLoader.getInstance().displayImage(path, photoView, options,
                    animateFirstListener);
            // Now just add PhotoView to ViewPager and return it
            photoView.setOnViewTapListener(new OnViewTapListener() {

                @Override
                public void onViewTap(View arg0, float arg1, float arg2) {
                    addrelative.setVisibility(View.GONE);
                    showExitAnim();
//                    finish();
                }
            });
            container.addView(photoView, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(loadedImage);
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            addrelative.setVisibility(View.GONE);
            showExitAnim();
        }
        return true;
    }

    @Override
    protected void endExit() {
        super.endExit();
        finish();
    }

    private void AddInstructionsView() {
        for (int i = 0; i < browseBeans.size(); i++) {
            View addview = new View(PreviewImage.this);
            addview.setBackgroundColor(0xffffffff);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(dip2px(5), dip2px(5));
            addview.setLayoutParams(p);
            p.setMargins(dip2px(5), 0, 0, 0);
            AddLayout.addView(addview);
        }
    }

}