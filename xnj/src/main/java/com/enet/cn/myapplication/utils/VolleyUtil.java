package com.enet.cn.myapplication.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.enet.cn.myapplication.MyApplication;
import com.enet.cn.myapplication.interface_.ResultListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by programmer on 2016/9/6 0006.
 */
public class VolleyUtil {
    private static Handler handler = new Handler();
    private static RequestQueue requestQueue = null;
    /**
     * 保证内存中只有一个队列实例
     *
     * @param context
     * @return
     */
    private static RequestQueue getQueue(Context context) {
        if (requestQueue == null) {
            return Volley.newRequestQueue(context);
        } else {
            return requestQueue;
        }
    }

 /*      public static <T> void RequestData(Context context, HashMap<String, String> requestType, final String url, final Class<T> beanType, final ResultListener<T> mListener) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                T bean = JsonUtil.json2Bean(s, beanType);//解析json数据
                mListener.onResponse(bean);//返回json数据


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyApplication.getContext(), "访问失败", Toast.LENGTH_SHORT).show();

            }
        });
        // 将请求添加到队列中
        MyApplication.getHttpQueue().add(request);
    }
*/


    /**
     * post请求
     */
    // 创建StringRequest，定义字符串请求的请求方式为POST，
    public static <T> void RequestDataInfo(Context context, final HashMap<String, String> requestType, final String url, final Class<T> beanType, final ResultListener<T> mListener) {
        // 2、获取缓存文件
        File cahceFile = getCacheFile(url);
        // 3、判断缓存文件是否有效
        if (cacheFileIsValid(cahceFile)) {
            // 4、如果缓存文件是有效的，读缓存
            getDataFromCache(cahceFile, beanType, mListener);
        } else {
            // 5、如果缓存文件是无效的，读网络
            getDataFromIntent(requestType, url, beanType, mListener);
           // getDataFromGet(requestType, url, beanType, mListener);
        }
    }

    private static <T> void getDataFromGet(HashMap<String, String> requestType, final String url, final Class<T> beanType, final ResultListener<T> mListener) {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                T bean = JsonUtil.json2Bean(s, beanType);//解析json数据
                cacheData(s, url);

                mListener.onResponse(bean);//返回json数据


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyApplication.getContext(), "访问失败", Toast.LENGTH_SHORT).show();

            }
        });
        // 将请求添加到队列中
        MyApplication.getHttpQueue().add(request);
    }


    /**
     * 从缓存中获取数据
     *
     * @param cahceFile             缓存文件
     * @param beanType              指定要把json解析成的JavaBean类型
     * @param requestResultListener 用于接收请求结果的监听器
     */
    private static <T> void getDataFromCache(final File cahceFile, final Object beanType,
                                             final ResultListener<T> requestResultListener) {
        new AsyncTask<Void, Void, String>() {
            // 这个方法会运行在子线程
            @Override
            protected String doInBackground(Void... params) {
                // 把cahceFile中的json数据读取出来
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(cahceFile));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    return sb.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            // 这个方法会运行到UI线程
            protected void onPostExecute(String json) {
                Log.i("数据", "获取了缓存，json = " + json);
                T bean = JsonUtil.json2Bean(json, beanType);    // 解析Json数据
                requestResultListener.onResponse(bean);    // 返回Json数据
            }
        }.execute();
    }
    /**
     * 判断缓存是否有效，如果有效返回true
     *
     * @param cahceFile
     * @return
     */
    private static boolean cacheFileIsValid(File cahceFile) {
        if (cahceFile == null || !cahceFile.exists()) {
            return false;
        }
        long validExistTime = 1000 * 60 * 3;        // 有效时间是3分钟
        long cacheFileExistTime = System.currentTimeMillis() - cahceFile.lastModified();    // 缓存文件存在的时间
        return cacheFileExistTime < validExistTime;    // 如果缓存文件存在的时间小于有效时间，则缓存是有效的
    }
    /**
     * 从网络上获取数据
     *
     * @param requestType
     * @param url
     * @param beanType
     * @param mListener
     * @param <T>
     */
    private static <T> void getDataFromIntent(final HashMap<String, String> requestType, final String url, final Class<T> beanType, final ResultListener<T> mListener) {

       StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 请求成功后执行的函数
            @Override
            public void onResponse(String s) {
                System.out.print("我是第二次的"+s);
                cacheData(s, url);
                T bean = JsonUtil.json2Bean(s, beanType);//解析json数据
                mListener.onResponse(bean);//返回json数据
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyApplication.getContext(), "连接失败，请检查网络", Toast.LENGTH_SHORT).show();

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return requestType;
            }
        };
        // 设置该请求的标签
        request.setTag("abcPost");
        // 将请求添加到队列中
        MyApplication.getHttpQueue().add(request);
    }




    /**
     * 缓存json数据
     *
     * @param json
     * @param fileName
     */
    protected static void cacheData(String json, String fileName) {
        BufferedWriter writer = null;
        try {
            File cacheFile = getCacheFile(fileName);
            writer = new BufferedWriter(new FileWriter(cacheFile));
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个缓存的文件目录
     *
     * @param url
     * @return
     */
    private static File getCacheFile(String url) {
        try {
            // 获取data/data//cache 目录
            File cacheDir = MyApplication.getContext().getCacheDir();
            // 对Url进行encode，对url中的特殊符号进行转义，因为缓存文件名不允许使用特殊符号
            url = URLEncoder.encode(url);
            // 封装缓存文件对象
            File cacheFile = new File(cacheDir, url);
            return cacheFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
