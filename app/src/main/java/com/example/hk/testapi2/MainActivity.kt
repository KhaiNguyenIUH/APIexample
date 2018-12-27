package com.example.hk.testapi2

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo RecyclerView.
        val rvUsers = findViewById(R.id.rv_users) as RecyclerView
        rvUsers.layoutManager = LinearLayoutManager(this)

        // Khởi tạo OkHttpClient để lấy dữ liệu.
        val client = OkHttpClient()

        // Khởi tạo Moshi adapter để biến đổi json sang model java (ở đây là User)
        val moshi = Moshi.Builder().build()
        val usersType = Types.newParameterizedType(List::class.java, User::class.java)
        val jsonAdapter = moshi.adapter<List<User>>(usersType)

        // Tạo request lên server.
        val request = Request.Builder()
            .url("https://api.github.com/users")
            .build()

        // Thực thi request.
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Error", "Network Error")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                // Lấy thông tin JSON trả về. Bạn có thể log lại biến json này để xem nó như thế nào.
                val json = response.body().string()
                val users = jsonAdapter.fromJson(json).toMutableList();// toMutableList()

                // Cho hiển thị lên RecyclerView.
                runOnUiThread { rvUsers.adapter = UserAdapter(users, this@MainActivity) }
            }
        })
        //

    }
}
