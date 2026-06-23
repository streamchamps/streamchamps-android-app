package com.streamchamps.sportshub;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.graphics.drawable.GradientDrawable;
import java.util.*;

public class MainActivity extends Activity {
    private LinearLayout content;
    private String[] leagues = {"NFL", "NBA", "NHL", "SOCCER"};

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        buildHome();
    }

    private TextView tv(String text, int sp, int color, int style) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextSize(sp);
        v.setTextColor(color);
        v.setTypeface(Typeface.DEFAULT, style);
        v.setPadding(18, 12, 18, 12);
        return v;
    }

    private GradientDrawable bg(int color, int stroke) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color); g.setCornerRadius(28); g.setStroke(stroke, Color.rgb(30,144,255));
        return g;
    }

    private void shell(String title) {
        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(22, 26, 22, 30);
        root.setBackgroundColor(Color.rgb(5,10,24));
        scroll.addView(root);

        ImageView logo = new ImageView(this);
        logo.setImageResource(getResources().getIdentifier("streamchamps_logo", "drawable", getPackageName()));
        logo.setAdjustViewBounds(true);
        logo.setMaxHeight(220);
        root.addView(logo, new LinearLayout.LayoutParams(-1, 210));

        TextView header = tv("⚡ " + title + " ⚡", 26, Color.WHITE, Typeface.BOLD);
        header.setGravity(Gravity.CENTER);
        root.addView(header);

        content = root;
        setContentView(scroll);
    }

    private Button button(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextColor(Color.WHITE);
        b.setTextSize(18);
        b.setAllCaps(false);
        b.setBackground(bg(Color.rgb(11,30,68), 2));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, 110);
        lp.setMargins(0, 10, 0, 10);
        b.setLayoutParams(lp);
        return b;
    }

    private void card(String title, String body) {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(18, 16, 18, 16);
        c.setBackground(bg(Color.rgb(8,20,48), 1));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 10, 0, 12);
        c.setLayoutParams(lp);
        c.addView(tv(title, 19, Color.WHITE, Typeface.BOLD));
        c.addView(tv(body, 15, Color.rgb(210,225,255), Typeface.NORMAL));
        content.addView(c);
    }

    private void buildHome() {
        shell("STREAMCHAMPS SPORTS HUB");
        card("🔥 Live Scores", "League-wide live score center is ready for API connection. Free source mode shows schedules/news first; premium data can unlock faster live scores.");
        Button breaking = button("📰 Breaking News"); breaking.setOnClickListener(v -> showNewsHub()); content.addView(breaking);
        Button video = button("🎥 Video Hub"); video.setOnClickListener(v -> openWeb("https://www.youtube.com/results?search_query=sports+news+highlights+today")); content.addView(video);
        for (String l: leagues) { Button b = button(icon(l)+" " + l); b.setOnClickListener(v -> showLeague(l)); content.addView(b); }
        card("📊 Standings  •  📅 Schedules  •  👤 Player Stats", "Tap a league to open news, standings, schedules, stats, and video searches. Soccer includes World Cup and major club leagues.");
    }

    private String icon(String l) { if(l.equals("NFL")) return "🏈"; if(l.equals("NBA")) return "🏀"; if(l.equals("NHL")) return "🏒"; return "⚽"; }

    private void showNewsHub() {
        shell("BREAKING NEWS");
        for (String l: leagues) { Button b=button(icon(l)+" " + l + " News"); b.setOnClickListener(v -> openWeb(newsUrl(l))); content.addView(b); }
        Button back=button("← Back Home"); back.setOnClickListener(v -> buildHome()); content.addView(back);
    }

    private void showLeague(String l) {
        shell(l + " HUB");
        card(icon(l)+" " + l + " League-Wide", "News, video searches, schedules, standings, team stats, and player stats for the whole league — not just one team.");
        String[] items = {"News Stories", "Video News", "Schedules", "Standings", "Player Stats", "Team Stats"};
        for(String item: items){ Button b=button(item); b.setOnClickListener(v -> openWeb(urlFor(l, item))); content.addView(b); }
        if(l.equals("SOCCER")){
            card("Soccer Coverage", "World Cup, Champions League, Premier League, MLS, La Liga, Bundesliga, Serie A, Ligue 1, Liga MX, Saudi Pro League, and smaller leagues where data is available.");
        }
        Button back=button("← Back Home"); back.setOnClickListener(v -> buildHome()); content.addView(back);
    }

    private String newsUrl(String l){ return urlFor(l,"News Stories"); }
    private String urlFor(String l, String item){
        String q = "";
        if(l.equals("NFL")) q="NFL "; else if(l.equals("NBA")) q="NBA "; else if(l.equals("NHL")) q="NHL "; else q="World Cup Champions League Premier League MLS soccer ";
        if(item.equals("Video News")) return "https://www.youtube.com/results?search_query=" + enc(q + "video news highlights today");
        if(item.equals("Schedules")) return "https://www.google.com/search?q=" + enc(q + "schedule today");
        if(item.equals("Standings")) return "https://www.google.com/search?q=" + enc(q + "standings");
        if(item.equals("Player Stats")) return "https://www.google.com/search?q=" + enc(q + "player stats leaders");
        if(item.equals("Team Stats")) return "https://www.google.com/search?q=" + enc(q + "team stats rankings");
        return "https://www.google.com/search?q=" + enc(q + "latest news stories");
    }
    private String enc(String s){ return s.replace(" ", "+"); }
    private void openWeb(String url){ startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); }
}
