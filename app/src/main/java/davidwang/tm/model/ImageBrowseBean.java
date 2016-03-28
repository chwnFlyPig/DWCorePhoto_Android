package davidwang.tm.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by changwenna on 2016/3/17.
 */
public class ImageBrowseBean implements Parcelable, Serializable {
    public String url;
    public float width;
    public float height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public ImageBrowseBean() {
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错
        dest.writeString(url);
        dest.writeFloat(width);
        dest.writeFloat(height);
    }

    public static final Creator<ImageBrowseBean> CREATOR = new Creator<ImageBrowseBean>() {
        @Override
        public ImageBrowseBean createFromParcel(Parcel source) {
            ImageBrowseBean imageBrowseBean = new ImageBrowseBean();
            imageBrowseBean.setUrl(source.readString());
            imageBrowseBean.setWidth(source.readFloat());
            imageBrowseBean.setHeight(source.readFloat());
            return imageBrowseBean;
        }

        @Override
        public ImageBrowseBean[] newArray(int size) {
            return new ImageBrowseBean[size];
        }
    };

    @Override
    public String toString() {
        return "ImageInfo{" +
                "url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
