package com.streamchamps.sportshub;

import android.app.*;import android.os.*;import android.graphics.Color;import android.content.*;import android.net.Uri;import android.view.*;import android.widget.*;import org.xmlpull.v1.XmlPullParser;import org.xmlpull.v1.XmlPullParserFactory;import java.net.*;import java.util.*;

public class MainActivity extends Activity{
  LinearLayout list; TextView title;
  Map<String,String> feeds=new HashMap<>();
  public void onCreate(Bundle b){super.onCreate(b);
    feeds.put("NFL News","https://www.espn.com/espn/rss/nfl/news");
    feeds.put("NBA News","https://www.espn.com/espn/rss/nba/news");
    feeds.put("NHL News","https://www.espn.com/espn/rss/nhl/news");
    feeds.put("Soccer News","https://www.espn.com/espn/rss/soccer/news");
    buildHome();
  }
  TextView tv(String s,int sp,int c){TextView v=new TextView(this);v.setText(s);v.setTextSize(sp);v.setTextColor(c);v.setPadding(24,18,24,18);return v;}
  void buildHome(){ScrollView sv=new ScrollView(this);LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(Color.rgb(4,12,26));sv.addView(root);
    ImageView logo=new ImageView(this);logo.setImageResource(getResources().getIdentifier("streamchamps_logo","drawable",getPackageName()));logo.setAdjustViewBounds(true);logo.setPadding(30,30,30,10);root.addView(logo,new LinearLayout.LayoutParams(-1,420));
    root.addView(tv("⚡ STREAMCHAMPS SPORTS HUB",26,Color.WHITE));
    root.addView(tv("League-wide sports news. NFL • NBA • NHL • Soccer",15,Color.LTGRAY));
    for(String k:new String[]{"NFL News","NBA News","NHL News","Soccer News"}){Button btn=new Button(this);btn.setText(k);btn.setTextSize(20);btn.setAllCaps(false);btn.setOnClickListener(v->loadFeed(k,feeds.get(k)));root.addView(btn,new LinearLayout.LayoutParams(-1,120));}
    setContentView(sv);
  }
  void loadFeed(String name,String url){ScrollView sv=new ScrollView(this);list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);list.setBackgroundColor(Color.rgb(4,12,26));sv.addView(list);title=tv(name,26,Color.WHITE);list.addView(title);list.addView(tv("Loading latest stories...",16,Color.LTGRAY));setContentView(sv);new Thread(()->fetch(name,url)).start();}
  void fetch(String name,String url){ArrayList<Item> items=new ArrayList<>();try{XmlPullParser p=XmlPullParserFactory.newInstance().newPullParser();p.setInput(new URL(url).openStream(),null);Item cur=null;String tag="";int ev=p.getEventType();while(ev!=XmlPullParser.END_DOCUMENT){if(ev==XmlPullParser.START_TAG){tag=p.getName(); if("item".equals(tag))cur=new Item();}else if(ev==XmlPullParser.TEXT&&cur!=null){String t=p.getText(); if("title".equals(tag))cur.title+=t; if("description".equals(tag))cur.desc+=t; if("link".equals(tag))cur.link+=t;}else if(ev==XmlPullParser.END_TAG&&"item".equals(p.getName())&&cur!=null){items.add(cur);cur=null;} ev=p.next();}}catch(Exception e){Item it=new Item();it.title="Could not load feed";it.desc=e.toString();items.add(it);}runOnUiThread(()->showItems(name,items));}
  void showItems(String name,ArrayList<Item> items){list.removeAllViews();TextView back=tv("← Back",18,Color.CYAN);back.setOnClickListener(v->buildHome());list.addView(back);list.addView(tv(name,26,Color.WHITE));for(Item it:items){TextView v=tv(it.title.replace("<![CDATA[","").replace("]]>",""),18,Color.WHITE);v.setOnClickListener(x->showStory(name,it));list.addView(v);TextView d=tv(clean(it.desc),14,Color.LTGRAY);list.addView(d);} }
  String clean(String s){return s.replaceAll("<[^>]*>","").replace("<![CDATA[","").replace("]]>","").trim();}
  void showStory(String name,Item it){ScrollView sv=new ScrollView(this);LinearLayout r=new LinearLayout(this);r.setOrientation(LinearLayout.VERTICAL);r.setBackgroundColor(Color.rgb(4,12,26));sv.addView(r);TextView back=tv("← Back to "+name,18,Color.CYAN);back.setOnClickListener(v->loadFeed(name,feeds.get(name)));r.addView(back);r.addView(tv(it.title,24,Color.WHITE));r.addView(tv(clean(it.desc),17,Color.LTGRAY));Button open=new Button(this);open.setText("Open source story");open.setOnClickListener(v->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(it.link))));r.addView(open);setContentView(sv);} 
  static class Item{String title="",desc="",link="";}
}
