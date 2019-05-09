package co.ronash.pushesample.as;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import co.ronash.pushe.Pushe;

/**
 * For further information Go to <a href="https://pushe.co/docs">Docs</a>
 * @author Mahdi Malvandi
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pushe.initialize(this, true);
    }
}
