package cn.xy.unittext.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by pxw on 2018/5/29.
 */
@Entity
public class URLBean {

    @Id(autoincrement = true)
    public Long id;

    public String url;

    @Generated(hash = 1135954869)
    public URLBean(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    @Generated(hash = 360342627)
    public URLBean() {
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
