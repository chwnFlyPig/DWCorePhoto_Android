package davidwang.tm.dwcorephoto;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import davidwang.tm.model.ImageBrowseBean;
import davidwang.tm.model.ImageBrowseParam;
import davidwang.tm.model.ImageBrowseShareBean;
import davidwang.tm.tools.ImageLoaders;


public class SingleShow extends BaseActivity implements View.OnClickListener {


    private ImageView show_img;
    private ArrayList<ImageBrowseBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_show);
        findID();
        Listener();
        AddToolbar();
    }

    @Override
    protected void findID() {
        super.findID();
        data = new ArrayList<>();
        shareBean = new ImageBrowseShareBean();
        imageInfo = new ImageBrowseBean();
        imageInfo.width = 1280;
        imageInfo.height = 720;
        imageInfo.url = "http://img4q.duitang.com/uploads/item/201408/11/20140811141753_iNtAF.jpeg";
        data.add(imageInfo);
        show_img = (ImageView) findViewById(R.id.show_img);
        ImageLoaders.setsendimg(imageInfo.url, show_img);
    }

    @Override
    protected void Listener() {
        super.Listener();
        show_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_img:
                shareBean.x = show_img.getLeft();
                shareBean.y = show_img.getTop();
                shareBean.width = show_img.getLayoutParams().width;
                shareBean.height = show_img.getLayoutParams().height;

                ImageBrowseParam beans = new ImageBrowseParam();
                beans.setBrowseBeanList(data);
                beans.setIndex(0);
                beans.setShareBean(shareBean);
                beans.setType(PreviewImage.PHOTO_BROWSE_TYPE_GRID);
                startActivity(PreviewImage.newIntent(this, beans));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
