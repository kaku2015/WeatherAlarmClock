/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.util;

import com.squareup.otto.Bus;

/**
 * otto bus单例
 *
 * @author 咖枯
 * @version 1.0 2016/1/15
 */
public class OttoAppConfig {
    private static Bus sBus;

    public static Bus getInstance() {
        if (sBus == null) {
            sBus = new Bus();
        }
        return sBus;
    }
}
