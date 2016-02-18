package cc.mnbase.util.okhttp;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-15
 * Time: 11:31
 * Version 1.0
 */

public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;

    private final ProgressResponseListener progressListener;

    private BufferedSource bufferedSource;

    private String tag = ProgressResponseBody.class.getSimpleName();

    public ProgressResponseBody(ResponseBody responseBody, ProgressResponseListener progressListener){
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() throws IOException {
        if(bufferedSource == null){
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source){
        Log.i(tag, "----source="+source);
        return new ForwardingSource(source) {
            long totalByteRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalByteRead += bytesRead != -1 ? bytesRead : 0;
                Log.i(tag, "----totalByteRead="+totalByteRead);
                if(progressListener != null){
                    progressListener.update(totalByteRead, responseBody.contentLength(), bytesRead == -1);
                }
                return bytesRead;
            }
        };
    }

    public interface ProgressResponseListener{
        void update(long byteRead, long contentLength, boolean done);
    }
}
