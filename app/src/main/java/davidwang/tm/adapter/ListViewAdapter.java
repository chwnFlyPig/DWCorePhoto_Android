package davidwang.tm.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

import davidwang.tm.dwcorephoto.ListViewActivity;
import davidwang.tm.dwcorephoto.PreviewImage;
import davidwang.tm.dwcorephoto.R;
import davidwang.tm.model.ImageBrowseBean;
import davidwang.tm.model.ImageBrowseParam;
import davidwang.tm.model.ImageBrowseShareBean;
import davidwang.tm.tools.ImageLoaders;

/**
 * Created by DavidWang on 15/9/2.
 */
public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ImageBrowseBean> data;
    private ImageBrowseShareBean bdInfo;

    public ListViewAdapter(Context context, ArrayList<ImageBrowseBean> data) {
        this.context = context;
        this.data = data;
        bdInfo = new ImageBrowseShareBean();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ImageBrowseBean info = data.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.list_view, null);
            holder.list_img = (ImageView) convertView.findViewById(R.id.list_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageLoaders.setsendimg(info.getUrl(), holder.list_img);
        holder.list_img.setOnClickListener(new ImageOnclick(position, holder.list_img));

        return convertView;
    }

    public class ViewHolder {
        ImageView list_img;
    }

    private class ImageOnclick implements View.OnClickListener {

        private int index;
        private ImageView imageView;

        public ImageOnclick(int index, ImageView imageView) {
            this.index = index;
            this.imageView = imageView;
        }

        @Override
        public void onClick(View v) {
            Rect rect = new Rect();
            v.getGlobalVisibleRect(rect);
            ImageBrowseShareBean bdInfo = new ImageBrowseShareBean();
            bdInfo.setWidth(rect.width());
            bdInfo.setHeight(rect.height());
            bdInfo.setX(rect.left);
            bdInfo.setY(rect.top);
            ImageBrowseParam beans = new ImageBrowseParam();
            beans.setBrowseBeanList(data);
            beans.setIndex(index);
            beans.setShareBean(bdInfo);
            beans.setType(PreviewImage.PHOTO_BROWSE_TYPE_LIST);
            context.startActivity(PreviewImage.newIntent(context, beans));
        }
    }

    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
