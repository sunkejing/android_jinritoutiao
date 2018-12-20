package com.ss.android.gamecommon.thread;


import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.LogUtil;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.MessageFormat;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

import static com.ss.android.gamecommon.util.ConstantUtil.DEVICE_REGISTER;

/**
 * 定义失败重试的拦截器
 */

public class OkHttpRetryInterceptor implements Interceptor {
    public double executionCount;//最大重试次数
    public long retryInterval;//重试的间隔

    public OkHttpRetryInterceptor(double executionCount, int retryInterval) {
        this.executionCount = executionCount;
        this.retryInterval = retryInterval;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Request request = chain.request();

            okhttp3.Response response = chain.proceed(chain.request());
            String requestUrl = request.url().toString();
            int retryNum = 0;

            if (requestUrl.startsWith(ConstantUtil.LOG_URL_HEADER)) {
                //如果请求头不为空，直接proceed
                if (request.body() == null || request.header("Content-Encoding") != null) {
                    //什么都不做
                    return chain.proceed(request);
                }
                //否则，重构request
                request = request.newBuilder()
                        .header("Content-Encoding", "gzip")
                        .method(request.method(), gzip(request.body()))
                        .build();
                return chain.proceed(request);
            }

            while (response == null || !response.isSuccessful() && retryNum <= retryInterval) {
                if (!requestUrl.endsWith(DEVICE_REGISTER)) {
                    break;
                }
                LogUtil.e(MessageFormat.format("intercept request is not successful retryNum={0}", retryNum));
                long nextInterval = getRetryInterval();
                try {
                    Thread.sleep(nextInterval);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                    throw new InterruptedIOException();
                }
                retryNum++;
                response = chain.proceed(chain.request());
            }
            return response;
        } catch (Exception e) {
            throw new IOException(e);
        }

    }


    /**
     * retry间隔时间
     */
    public long getRetryInterval() {
        return this.retryInterval;
    }

    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1; // We don't know the compressed length in advance!
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }


}
