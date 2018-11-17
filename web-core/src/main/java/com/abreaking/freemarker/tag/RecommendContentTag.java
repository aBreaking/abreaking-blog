package com.abreaking.freemarker.tag;

import com.abreaking.core.render.freemarker.JTag;
import com.abreaking.model.Content;
import com.abreaking.model.query.ContentQuery;


public class RecommendContentTag extends JTag {
    public static final String TAG_NAME = "jp.recommendContent";
    @Override
    public void onRender() {
        String id = getParam("id");
        Content content = ContentQuery.me().findById(id);
        Content next = ContentQuery.me().findPrevious(content);
        if(null == next){
            setVariable("next",content);
        }else{
            setVariable("next", next);
        }
        renderBody();
    }
}
