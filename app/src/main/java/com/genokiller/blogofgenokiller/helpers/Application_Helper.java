package com.genokiller.blogofgenokiller.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.genokiller.Blog_Application;
import com.genokiller.blogofgenokiller.controllers.Application_Controller;
import com.genokiller.blogofgenokiller.controllers.R;
import com.genokiller.blogofgenokiller.models.Application_Model;
import com.genokiller.blogofgenokiller.models.Article_Model;
import com.genokiller.blogofgenokiller.utils.Comment;
import com.genokiller.blogofgenokiller.utils.Item;
import com.novoda.imageloader.core.model.ImageTagFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

public class Application_Helper extends ArrayAdapter<HashMap<String, Item>>
{
	private final Activity								context;

	private LayoutInflater								layoutInflater;
	private final ArrayList<HashMap<String, Item>>	maps;
	ImageTagFactory										imageTagFactory;
	private Application_Controller						main;
	private Item[]									infonames	= new Item[50];


	public Application_Helper(Activity context, ArrayList<HashMap<String, Item>> maps, Application_Controller application_Controller)
	{
		super(context, R.layout.activity_article, maps);
		this.context = context;
		this.maps = maps;
		this.main = application_Controller;
		layoutInflater = LayoutInflater.from(application_Controller);
		/* Image Loader */

		imageTagFactory = ImageTagFactory.newInstance(context, R.id.description);
		imageTagFactory.setErrorImageId(R.drawable.bg_img_notfound);
	}

	@SuppressWarnings(
	{ "unused" })
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
        View row = convertView;
        View elem;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.list, parent, false);


        String description = maps.get(position).get(Article_Model.DESCRIPTION).getText();
        String title = maps.get(position).get(Article_Model.TITLE).getText();
        String image_url = maps.get(position).get(Article_Model.IMAGE_URL).getText();
        String comment_url = maps.get(position).get(Article_Model.COMMENT_URL).getText();
        String comment_count = maps.get(position).get(Article_Model.COMMENT_COUNT).getText();
        final String article_id = maps.get(position).get(Article_Model.ID).getText();

        RelativeLayout list = (RelativeLayout)row.findViewById(R.id.listview);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView titre = new TextView(context);
        titre.setText(title);
        titre.setId(1);
        elem = titre;
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
        list.addView(titre, layoutParams1);

        layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView img = new ImageView(context);
        img.setTag(imageTagFactory.build(image_url, context));
        layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
        img.setId(elem.getId() + 1);

        try
        {
            Blog_Application.getImageLoader().getLoader().load(img);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        elem = img;
        list.addView(img, layoutParams1);
        layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView desc = new TextView(context);
        desc.setText(description);
        desc.setId(elem.getId() + 1);
        layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
        elem = desc;
        list.addView(desc, layoutParams1);


        for (int i = 0; i < infonames.length; i++)
        {
            infonames[i] = null;
        }
        for (int i = 0; i < maps.get(position).size(); i++)
        {
            if (maps.get(position).get("info_" + i) != null && !maps.get(position).get("info_" + i).equals(""))
                infonames[i] = maps.get(position).get("info_" + i);
        }
        for(int i = 0; i < infonames.length; i++)
        {
            if(infonames[i] == null) continue;
            layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView info = new TextView(context);
            info.setId(elem.getId() + 1);
            layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
            info.setText(infonames[i].getText());
            elem = info;
            list.addView(info, layoutParams1);

            if(infonames[i].getButton_less() != null)
            {
                final int id = infonames[i].getId();
                layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
                Button more = new Button(context);
                Button less = new Button(context);
                more.setText("+");
                less.setText("-");
                less.setId(elem.getId() + 1);
                less.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "http://blog-of-genokiller.herokuapp.com/admin/articles/updateInfo/" + id;
                        new Article_Model(Application_Model.METHOD_PUT).execute(url, "type", "less");
                    }
                });
                elem = less;
                list.addView(less, layoutParams1);
                layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams1.addRule(RelativeLayout.BELOW, elem.getId() - 1);
                layoutParams1.addRule(RelativeLayout.RIGHT_OF, elem.getId());
                more.setId(elem.getId() + 1);
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "http://blog-of-genokiller.herokuapp.com/admin/articles/updateInfo/" + id;
                        new Article_Model(Application_Model.METHOD_PUT).execute(url, "type", "more");
                    }
                });
                elem = more;
                list.addView(more, layoutParams1);
            }
        }

        String[] images = image_url.split("/");

		return row;
	}

}