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

package com.sohail.alam.http.common.counters;

import com.sohail.alam.http.common.utils.HttpMethodCodes;
import io.netty.handler.codec.http.HttpMethod;

import java.util.concurrent.atomic.AtomicLong;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/9/13
 * Time: 8:35 PM
 */
public class CounterManager {

    public static final CounterManager COUNTER_MANAGER = new CounterManager();
    // HTTP Request counters - net, success and failure counters
    private final AtomicLong TOTAL_HTTP_REQUESTS = new AtomicLong(0);
    private final AtomicLong TOTAL_HTTP_SUCCESS_REQUESTS = new AtomicLong(0);
    private final AtomicLong TOTAL_HTTP_FAILURE_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_CONNECT_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_CONNECT_SUCCESS_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_CONNECT_FAILURE_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_DELETE_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_DELETE_SUCCESS_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_DELETE_FAILURE_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_GET_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_GET_SUCCESS_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_GET_FAILURE_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_HEAD_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_HEAD_SUCCESS_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_HEAD_FAILURE_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_OPTIONS_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_OPTIONS_SUCCESS_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_OPTIONS_FAILURE_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_PATCH_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_PATCH_SUCCESS_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_PATCH_FAILURE_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_POST_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_POST_SUCCESS_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_POST_FAILURE_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_PUT_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_PUT_SUCCESS_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_PUT_FAILURE_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_TRACE_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_TRACE_SUCCESS_REQUESTS = new AtomicLong(0);
    private final AtomicLong HTTP_TRACE_FAILURE_REQUESTS = new AtomicLong(0);
    // Response Sent Counter
    private final AtomicLong RESPONSE_SENT_SUCCESS = new AtomicLong(0);
    private final AtomicLong RESPONSE_SENT_FAILED = new AtomicLong(0);
    private final AtomicLong RESPONSE_200_OK_SUCCESS = new AtomicLong(0);
    private final AtomicLong RESPONSE_200_OK_FAILURE = new AtomicLong(0);
    private final AtomicLong RESPONSE_400_BAD_SUCCESS = new AtomicLong(0);
    private final AtomicLong RESPONSE_400_BAD_FAILURE = new AtomicLong(0);
    private final AtomicLong RESPONSE_401_UNAUTHORIZED_SUCCESS = new AtomicLong(0);
    private final AtomicLong RESPONSE_401_UNAUTHORIZED_FAILURE = new AtomicLong(0);
    private final AtomicLong RESPONSE_403_FORBIDDEN_SUCCESS = new AtomicLong(0);
    private final AtomicLong RESPONSE_403_FORBIDDEN_FAILURE = new AtomicLong(0);
    private final AtomicLong RESPONSE_404_NOT_FOUND_SUCCESS = new AtomicLong(0);
    private final AtomicLong RESPONSE_404_NOT_FOUND_FAILURE = new AtomicLong(0);
    private final AtomicLong RESPONSE_405_METHOD_NOT_ALLOWED_SUCCESS = new AtomicLong(0);
    private final AtomicLong RESPONSE_405_METHOD_NOT_ALLOWED_FAILURE = new AtomicLong(0);
    private final AtomicLong RESPONSE_407_PROXY_AUTHENTICATION_REQUIRED_SUCCESS = new AtomicLong(0);
    private final AtomicLong RESPONSE_407_PROXY_AUTHENTICATION_REQUIRED_FAILURE = new AtomicLong(0);
    private final AtomicLong RESPONSE_500_INTERNAL_SERVER_ERROR_SUCCESS = new AtomicLong(0);
    private final AtomicLong RESPONSE_500_INTERNAL_SERVER_ERROR_FAILURE = new AtomicLong(0);
    // Data Counter
    private final AtomicLong DATA_RECEIVED = new AtomicLong(0);
    private final AtomicLong DATA_SENT = new AtomicLong(0);
    // Exceptions Caught
    private final AtomicLong EXCEPTION_CAUGHT = new AtomicLong(0);

    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------

    public long total_http_requests() {
        return this.TOTAL_HTTP_REQUESTS.get();
    }

    public void increment_total_http_requests() {
        this.TOTAL_HTTP_REQUESTS.incrementAndGet();
    }

    public long total_http_success_requests() {
        return this.TOTAL_HTTP_SUCCESS_REQUESTS.get();
    }

    public void increment_total_http_success_requests() {
        this.TOTAL_HTTP_SUCCESS_REQUESTS.incrementAndGet();
    }

    public long total_http_failure_requests() {
        return this.TOTAL_HTTP_FAILURE_REQUESTS.get();
    }

    public void increment_total_http_failure_requests() {
        this.TOTAL_HTTP_FAILURE_REQUESTS.incrementAndGet();
    }

    // -------------------------------------------------------------------------
    public long total_http_requests_for_type(HttpMethod type) {
        switch (HttpMethodCodes.httpMethodCode.get(type)) {

            case HttpMethodCodes.CONNECT:
                return HTTP_CONNECT_REQUESTS.get();

            case HttpMethodCodes.DELETE:
                return HTTP_DELETE_REQUESTS.get();

            case HttpMethodCodes.GET:
                return HTTP_GET_REQUESTS.get();

            case HttpMethodCodes.HEAD:
                return HTTP_HEAD_REQUESTS.get();

            case HttpMethodCodes.OPTIONS:
                return HTTP_OPTIONS_REQUESTS.get();

            case HttpMethodCodes.PATCH:
                return HTTP_PATCH_REQUESTS.get();

            case HttpMethodCodes.POST:
                return HTTP_POST_REQUESTS.get();

            case HttpMethodCodes.PUT:
                return HTTP_PUT_REQUESTS.get();

            case HttpMethodCodes.TRACE:
                return HTTP_TRACE_REQUESTS.get();

            default:
                return -1;
        }
    }

    public void increment_total_http_requests_for_type(HttpMethod type) {
        switch (HttpMethodCodes.httpMethodCode.get(type)) {

            case HttpMethodCodes.CONNECT:
                HTTP_CONNECT_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.DELETE:
                HTTP_DELETE_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.GET:
                HTTP_GET_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.HEAD:
                HTTP_HEAD_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.OPTIONS:
                HTTP_OPTIONS_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.PATCH:
                HTTP_PATCH_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.POST:
                HTTP_POST_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.PUT:
                HTTP_PUT_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.TRACE:
                HTTP_TRACE_REQUESTS.incrementAndGet();
                break;

            default:
                break;
        }
    }

    // -------------------------------------------------------------------------
    public long total_http_success_requests_for_type(HttpMethod type) {
        switch (HttpMethodCodes.httpMethodCode.get(type)) {

            case HttpMethodCodes.CONNECT:
                return HTTP_CONNECT_SUCCESS_REQUESTS.get();

            case HttpMethodCodes.DELETE:
                return HTTP_DELETE_SUCCESS_REQUESTS.get();

            case HttpMethodCodes.GET:
                return HTTP_GET_SUCCESS_REQUESTS.get();

            case HttpMethodCodes.HEAD:
                return HTTP_HEAD_SUCCESS_REQUESTS.get();

            case HttpMethodCodes.OPTIONS:
                return HTTP_OPTIONS_SUCCESS_REQUESTS.get();

            case HttpMethodCodes.PATCH:
                return HTTP_PATCH_SUCCESS_REQUESTS.get();

            case HttpMethodCodes.POST:
                return HTTP_POST_SUCCESS_REQUESTS.get();

            case HttpMethodCodes.PUT:
                return HTTP_PUT_SUCCESS_REQUESTS.get();

            case HttpMethodCodes.TRACE:
                return HTTP_TRACE_SUCCESS_REQUESTS.get();

            default:
                return -1;
        }
    }

    public void increment_total_http_success_requests_for_type(HttpMethod type) {
        switch (HttpMethodCodes.httpMethodCode.get(type)) {

            case HttpMethodCodes.CONNECT:
                HTTP_CONNECT_SUCCESS_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.DELETE:
                HTTP_DELETE_SUCCESS_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.GET:
                HTTP_GET_SUCCESS_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.HEAD:
                HTTP_HEAD_SUCCESS_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.OPTIONS:
                HTTP_OPTIONS_SUCCESS_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.PATCH:
                HTTP_PATCH_SUCCESS_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.POST:
                HTTP_POST_SUCCESS_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.PUT:
                HTTP_PUT_SUCCESS_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.TRACE:
                HTTP_TRACE_SUCCESS_REQUESTS.incrementAndGet();
                break;

            default:
                break;
        }
    }

    // -------------------------------------------------------------------------
    public long total_http_failure_requests_for_type(HttpMethod type) {
        switch (HttpMethodCodes.httpMethodCode.get(type)) {

            case HttpMethodCodes.CONNECT:
                return HTTP_CONNECT_FAILURE_REQUESTS.get();

            case HttpMethodCodes.DELETE:
                return HTTP_DELETE_FAILURE_REQUESTS.get();

            case HttpMethodCodes.GET:
                return HTTP_GET_FAILURE_REQUESTS.get();

            case HttpMethodCodes.HEAD:
                return HTTP_HEAD_FAILURE_REQUESTS.get();

            case HttpMethodCodes.OPTIONS:
                return HTTP_OPTIONS_FAILURE_REQUESTS.get();

            case HttpMethodCodes.PATCH:
                return HTTP_PATCH_FAILURE_REQUESTS.get();

            case HttpMethodCodes.POST:
                return HTTP_POST_FAILURE_REQUESTS.get();

            case HttpMethodCodes.PUT:
                return HTTP_PUT_FAILURE_REQUESTS.get();

            case HttpMethodCodes.TRACE:
                return HTTP_TRACE_FAILURE_REQUESTS.get();

            default:
                return -1;
        }
    }

    public void increment_total_http_failure_requests_for_type(HttpMethod type) {
        switch (HttpMethodCodes.httpMethodCode.get(type)) {

            case HttpMethodCodes.CONNECT:
                HTTP_CONNECT_FAILURE_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.DELETE:
                HTTP_DELETE_FAILURE_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.GET:
                HTTP_GET_FAILURE_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.HEAD:
                HTTP_HEAD_FAILURE_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.OPTIONS:
                HTTP_OPTIONS_FAILURE_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.PATCH:
                HTTP_PATCH_FAILURE_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.POST:
                HTTP_POST_FAILURE_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.PUT:
                HTTP_PUT_FAILURE_REQUESTS.incrementAndGet();
                break;

            case HttpMethodCodes.TRACE:
                HTTP_TRACE_FAILURE_REQUESTS.incrementAndGet();
                break;

            default:
                break;
        }
    }

    // -------------------------------------------------------------------------
    public long total_http_response_sent_success() {
        return RESPONSE_SENT_SUCCESS.get();
    }

    public void increment_total_http_response_sent_success() {
        RESPONSE_SENT_SUCCESS.incrementAndGet();
    }

    public long total_http_response_sent_failure() {
        return RESPONSE_SENT_FAILED.get();
    }

    public void increment_total_http_response_sent_failure() {
        RESPONSE_SENT_FAILED.incrementAndGet();
    }

    public long total_http_response_sent_success_of_type(int response_code) {
        switch (response_code) {
            case 200:
                return RESPONSE_200_OK_SUCCESS.get();
            case 400:
                return RESPONSE_400_BAD_SUCCESS.get();
            case 401:
                return RESPONSE_401_UNAUTHORIZED_SUCCESS.get();
            case 403:
                return RESPONSE_403_FORBIDDEN_SUCCESS.get();
            case 404:
                return RESPONSE_404_NOT_FOUND_SUCCESS.get();
            case 405:
                return RESPONSE_405_METHOD_NOT_ALLOWED_SUCCESS.get();
            case 407:
                return RESPONSE_407_PROXY_AUTHENTICATION_REQUIRED_SUCCESS.get();
            case 500:
                return RESPONSE_500_INTERNAL_SERVER_ERROR_SUCCESS.get();
            default:
                return -1;
        }
    }

    public void increment_total_http_response_sent_success_of_type(int response_code) {
        switch (response_code) {
            case 200:
                RESPONSE_200_OK_SUCCESS.incrementAndGet();
                break;
            case 400:
                RESPONSE_400_BAD_SUCCESS.incrementAndGet();
                break;
            case 401:
                RESPONSE_401_UNAUTHORIZED_SUCCESS.incrementAndGet();
                break;
            case 403:
                RESPONSE_403_FORBIDDEN_SUCCESS.incrementAndGet();
                break;
            case 404:
                RESPONSE_404_NOT_FOUND_SUCCESS.incrementAndGet();
                break;
            case 405:
                RESPONSE_405_METHOD_NOT_ALLOWED_SUCCESS.incrementAndGet();
                break;
            case 407:
                RESPONSE_407_PROXY_AUTHENTICATION_REQUIRED_SUCCESS.incrementAndGet();
                break;
            case 500:
                RESPONSE_500_INTERNAL_SERVER_ERROR_SUCCESS.incrementAndGet();
                break;
            default:
                break;
        }
    }

    public long total_http_response_sent_failure_of_type(int response_code) {
        switch (response_code) {
            case 200:
                return RESPONSE_200_OK_FAILURE.get();
            case 400:
                return RESPONSE_400_BAD_FAILURE.get();
            case 401:
                return RESPONSE_401_UNAUTHORIZED_FAILURE.get();
            case 403:
                return RESPONSE_403_FORBIDDEN_FAILURE.get();
            case 404:
                return RESPONSE_404_NOT_FOUND_FAILURE.get();
            case 405:
                return RESPONSE_405_METHOD_NOT_ALLOWED_FAILURE.get();
            case 407:
                return RESPONSE_407_PROXY_AUTHENTICATION_REQUIRED_FAILURE.get();
            case 500:
                return RESPONSE_500_INTERNAL_SERVER_ERROR_FAILURE.get();
            default:
                return -1;
        }
    }

    public void increment_total_http_response_sent_failure_of_type(int response_code) {
        switch (response_code) {
            case 200:
                RESPONSE_200_OK_FAILURE.incrementAndGet();
                break;
            case 400:
                RESPONSE_400_BAD_FAILURE.incrementAndGet();
                break;
            case 401:
                RESPONSE_401_UNAUTHORIZED_FAILURE.incrementAndGet();
                break;
            case 403:
                RESPONSE_403_FORBIDDEN_FAILURE.incrementAndGet();
                break;
            case 404:
                RESPONSE_404_NOT_FOUND_FAILURE.incrementAndGet();
                break;
            case 405:
                RESPONSE_405_METHOD_NOT_ALLOWED_FAILURE.incrementAndGet();
                break;
            case 407:
                RESPONSE_407_PROXY_AUTHENTICATION_REQUIRED_FAILURE.incrementAndGet();
                break;
            case 500:
                RESPONSE_500_INTERNAL_SERVER_ERROR_FAILURE.incrementAndGet();
                break;
            default:
                break;
        }
    }

    // -------------------------------------------------------------------------
    public long total_data_received() {
        return DATA_RECEIVED.get();
    }

    public void increment_total_data_received(long length) {
        long temp = DATA_RECEIVED.get();
        DATA_RECEIVED.set(temp + length);
    }

    public long total_data_sent() {
        return DATA_SENT.get();
    }

    public void increment_total_data_sent(long length) {
        long temp = DATA_SENT.get();
        DATA_SENT.set(temp + length);
    }
    // -------------------------------------------------------------------------

    public long total_exceptions_caught() {
        return EXCEPTION_CAUGHT.get();
    }

    public void increment_exceptions_Caught() {
        EXCEPTION_CAUGHT.incrementAndGet();
    }
}
