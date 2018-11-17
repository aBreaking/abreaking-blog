/**
 * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.abreaking.model;

import com.abreaking.model.base.BaseComment;
import com.abreaking.model.core.Table;
import com.abreaking.model.query.CommentQuery;
import com.abreaking.model.query.ContentQuery;

import java.math.BigInteger;

@Table(tableName = "comment", primaryKey = "id")
public class Comment extends BaseComment<Comment> {
	private static final long serialVersionUID = 1L;

	public static final String TYPE_COMMENT = "comment";
	public static String STATUS_DELETE = "delete";
	public static String STATUS_DRAFT = "draft";
	public static String STATUS_NORMAL = "normal";

	public String getUsername() {
		return get("username");
	}

	public String getcontentTitle() {
		return get("content_title");
	}

	public boolean isDelete() {
		return STATUS_DELETE.equals(getStatus());
	}

	public String getContentUrl() {
		BigInteger contentId = getContentId();
		if (contentId == null)
			return null;

		Content c = ContentQuery.me().findById(contentId);
		return c == null ? null : c.getUrl();
	}
	
	public boolean updateCommentCount() {
		long count = CommentQuery.me().findCountByParentIdInNormal(getId());
		if (count > 0) {
			setCommentCount(count);
			return this.update();
		}
		return false;
	}

	public static void main(String args[])	{

    }
}
