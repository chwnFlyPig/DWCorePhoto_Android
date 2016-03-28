package davidwang.tm.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by changwenna on 2016/3/17.
 */
public class ImageBrowseShareBean implements Parcelable, Serializable {
    public float x;
    public float y;
    public float width;
    public float height;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
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

    @Override
    public String toString() {
        return "ImageBDInfo{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(x);
        dest.writeFloat(y);
        dest.writeFloat(width);
        dest.writeFloat(height);
    }

    public static final Creator<ImageBrowseShareBean> CREATOR = new Creator<ImageBrowseShareBean>() {
        @Override
        public ImageBrowseShareBean createFromParcel(Parcel source) {
            ImageBrowseShareBean imageBrowseParams = new ImageBrowseShareBean();
            imageBrowseParams.setX(source.readFloat());
            imageBrowseParams.setY(source.readFloat());
            imageBrowseParams.setWidth(source.readFloat());
            imageBrowseParams.setHeight(source.readFloat());
            return imageBrowseParams;
        }

        @Override
        public ImageBrowseShareBean[] newArray(int size) {
            return new ImageBrowseShareBean[size];
        }
    };
}
