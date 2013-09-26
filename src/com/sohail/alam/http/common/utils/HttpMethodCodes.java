/*
 * Copyright 2013 The Http Server & Proxy
 *
 *  The Http Server & Proxy Project licenses this file to you under the Apache License, version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at:
 *
 *               http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.sohail.alam.http.common.utils;

import io.netty.handler.codec.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 23/9/13
 * Time: 10:46 PM
 */
public class HttpMethodCodes {

    public static final Map<HttpMethod, Integer> httpMethodCode;
    public static final int OPTIONS = 0;
    public static final int GET = 1;
    public static final int HEAD = 2;
    public static final int POST = 3;
    public static final int PUT = 4;
    public static final int PATCH = 5;
    public static final int DELETE = 6;
    public static final int TRACE = 7;
    public static final int CONNECT = 8;

    static {
        httpMethodCode = new HashMap<HttpMethod, Integer>();

        httpMethodCode.put(HttpMethod.CONNECT, CONNECT);
        httpMethodCode.put(HttpMethod.DELETE, DELETE);
        httpMethodCode.put(HttpMethod.GET, GET);
        httpMethodCode.put(HttpMethod.HEAD, HEAD);
        httpMethodCode.put(HttpMethod.OPTIONS, OPTIONS);
        httpMethodCode.put(HttpMethod.PATCH, PATCH);
        httpMethodCode.put(HttpMethod.POST, POST);
        httpMethodCode.put(HttpMethod.PUT, PUT);
        httpMethodCode.put(HttpMethod.TRACE, TRACE);
    }
}
