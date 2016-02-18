package cc.mnbase.util.okhttp;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-15
 * Time: 16:04
 * Version 1.0
 */

public class ProgressRequestBody extends RequestBody {

    private final RequestBody requestBody;

    private final ProgressRequestListener progressRequestListener;

    private BufferedSink bufferedSink;

    private String tag = ProgressRequestBody.class.getSimpleName();

    public ProgressRequestBody(RequestBody requestBody, ProgressRequestListener progressRequestListener){
        this.requestBody = requestBody;
        this.progressRequestListener = progressRequestListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink bufferedSink) throws IOException {
        if(bufferedSink == null){
            bufferedSink = Okio.buffer(sink(bufferedSink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink){
        return new ForwardingSink(sink) {
            long bytesWriten = 0L;
            long contentLength = 0L;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if(contentLength == 0L){
                    contentLength = contentLength();
                }
                Log.i(tag, "----");
                bytesWriten += byteCount;
                if(progressRequestListener != null){
                    progressRequestListener.onRequestProgress(bytesWriten, contentLength, bytesWriten == contentLength);
                }
            }
        };
    }

    public interface ProgressRequestListener{
        void onRequestProgress(long bytesWritten, long contentLength, boolean done);
    }

}
