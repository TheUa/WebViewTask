package the.ua.webviewtask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class StartActivity extends AppCompatActivity {

    TextView location;
    EditText link;
    Button enter;
    Button youtube;
    private static final String KEY_ENTER = "ENTER";
    private static final String KEY_LOCATION = "LOCATION";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        link = findViewById(R.id.input_link);
        enter = findViewById(R.id.link_button);
        youtube = findViewById(R.id.youtube_button);
        location = findViewById(R.id.location);

        IpTest ipTest = new IpTest();
        ipTest.execute();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Patterns.WEB_URL.matcher(link.getText()).matches()) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    intent.putExtra(KEY_ENTER, link.getText().toString());
                    intent.putExtra(KEY_LOCATION, location.getText().toString());
                    startActivity(intent);
                } else
                    Toast.makeText(StartActivity.this, "Ссылка введена некорректно", Toast.LENGTH_LONG).show();
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra(KEY_ENTER, "http://m.youtube.com");
                intent.putExtra(KEY_LOCATION, location.getText().toString());
                startActivity(intent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class IpTest extends AsyncTask<Void, Void, Void> {

        String region;

        @Override
        protected Void doInBackground(Void... voids) {

            Document doc = null;
            try {
                doc = Jsoup.connect("https://www.myip.com/").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc != null) {
                Elements element = doc.select("body > div.container-fluid.principal > div.container-fluid.contenido > div:nth-child(2) > div.col-xs-12.col-lg-4 > div > div > div.info > div.info_2");
                region = element.text();
            } else
                region = "Ошибка";

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            location.setText(region);

        }
    }
}
