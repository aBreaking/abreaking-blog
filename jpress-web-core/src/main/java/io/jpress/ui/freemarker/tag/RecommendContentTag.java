package io.jpress.ui.freemarker.tag;

import io.jpress.core.render.freemarker.JTag;
import io.jpress.model.Content;
import io.jpress.model.query.ContentQuery;


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
