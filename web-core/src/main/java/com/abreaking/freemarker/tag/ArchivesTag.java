package com.abreaking.freemarker.tag;

import com.abreaking.common.consts.Consts;
import com.abreaking.core.render.freemarker.JTag;
import com.abreaking.model.Content;
import com.abreaking.model.query.ContentQuery;
import com.abreaking.model.vo.Archive;
import com.abreaking.common.util.StringUtils;

import java.util.List;

public class ArchivesTag extends JTag {
	
	public static final String TAG_NAME = "jp.archives";

	@Override
	public void onRender() {

		String module = getParam("module", Consts.MODULE_ARTICLE);
		if (StringUtils.isBlank(module)) {
			renderText("");
			return;
		}

		List<Archive> list = ContentQuery.me().findArchives(module);
		if (list == null || list.isEmpty()) {
			renderText("");
			return;
		}

		List<Content> contents = ContentQuery.me().findArchiveByModule(module);
		if (contents == null || contents.isEmpty()) {
			renderText("");
			return;
		}

		for (Content c : contents) {
			String archiveDate = c.getStr("archiveDate");
			if (archiveDate == null)
				continue;
			for (Archive a : list) {
				if (archiveDate.equals(a.getDate())) {
					a.addData(c);
				}
			}
		}

		setVariable("archives", list);
		renderBody();
	}

}
