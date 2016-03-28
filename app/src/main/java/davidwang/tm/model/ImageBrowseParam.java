package davidwang.tm.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by changwenna on 2016/3/17.
 */
public class ImageBrowseParam implements Serializable {
    private ArrayList<ImageBrowseBean> browseBeanList;
    private int type;
    private int index;
    private ImageBrowseShareBean shareBean;

    public ArrayList<ImageBrowseBean> getBrowseBeanList() {
        return browseBeanList;
    }

    public void setBrowseBeanList(ArrayList<ImageBrowseBean> browseBeanList) {
        this.browseBeanList = browseBeanList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ImageBrowseShareBean getShareBean() {
        return shareBean;
    }

    public void setShareBean(ImageBrowseShareBean shareBean) {
        this.shareBean = shareBean;
    }
}
