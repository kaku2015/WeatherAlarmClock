/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.kaku.weac.bean;

import java.io.Serializable;

/**
 * 生活指数
 *
 * @author 咖枯
 * @version 1.0 2015/0918
 */
public class WeatherLifeIndex implements Serializable {

    private static final long serialVersionUID = -3927234831261262032L;
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
