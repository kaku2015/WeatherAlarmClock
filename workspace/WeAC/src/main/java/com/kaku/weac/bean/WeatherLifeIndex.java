package com.kaku.weac.bean;

import java.io.Serializable;

/**
 * 生活指数
 *
 * @author 咖枯
 * @version 1.0 2015/0918
 */
public class WeatherLifeIndex implements Serializable {

    /**
     * 指数名
     */
    private String mIndexName;

    /**
     * 指数建议
     */
    private String mIndexValue;

    /**
     * 指数详细
     */
    private String mIndexDetail;

    public String getIndexDetail() {
        return mIndexDetail;
    }

    public void setIndexDetail(String indexDetail) {
        mIndexDetail = indexDetail;
    }

    public String getIndexName() {
        return mIndexName;
    }

    public void setIndexName(String indexName) {
        mIndexName = indexName;
    }

    public String getIndexValue() {
        return mIndexValue;
    }

    public void setIndexValue(String indexValue) {
        mIndexValue = indexValue;
    }
}
