package com.swisherdominicana.molde.utils

import org.apache.commons.codec.digest.DigestUtils

class HashUtil {
    public static String getMD5(final String data) {
        return DigestUtils.md5Hex(data)
    }
}
